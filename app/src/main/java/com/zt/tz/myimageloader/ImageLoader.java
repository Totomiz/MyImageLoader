package com.zt.tz.myimageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by asus on 2016-05-28 16:44
 * QQ:xxxxxxxx
 */
public class ImageLoader {
    private static Context mContext;

    public ImageLoader(Context mContext) {
        this.mContext = mContext;
    }

    private static ImageLoader instance;

    private ImageLoader() {
    }

    public static ImageLoader getInstance(Context context) {
        if (instance == null) {
            instance = new ImageLoader(context);
        }
        return instance;
    }

    //默认图片
    private DefaultImage defaultImage = new DefaultImage();
    //一级缓存的容量  20张图片
    private static final int MAX_CAPACITY = 20;

    //三级缓存
    //一级缓存：强引用缓存 ，内存溢出时异常
    //key:图片地址， value:图片
    //accessorder true 访问排序， false 插入排序
    //LRU近期最少使用算法（此处用linkedhashmap的原因）
    private static LinkedHashMap<String, Bitmap> firstCacheMap = new LinkedHashMap<String, Bitmap>(MAX_CAPACITY, 0.75F, true) {
        //根据返回值移除map中最老的值
        @Override
        protected boolean removeEldestEntry(Entry<String, Bitmap> eldest) {
            if (this.size() > MAX_CAPACITY) {
                //加入二级缓存
                secondCacheMap.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue()));
                //本地缓存
                diskCache(eldest.getKey(), eldest.getValue());
                //移除一级缓存
                return true;
            }
            return false;
        }
    };

    /**
     * 本地缓存
     *
     * @param key   图片的路径（图片的路径会被当成图片的名称保存到硬盘上）
     * @param value
     */
    private static void diskCache(String key, Bitmap value) {
        //路径（本地文件标示符）
        //消息照耀算法（抗修改性）
        //Message Diagest Version 5
        String fileName = MD5.getMD5(key);
        String filepath = mContext.getCacheDir().getAbsolutePath() + File.separator + fileName + ".JPG";
        //此处判断文件是否存在是因为在重复对一副图的Copress,和decode中，图片失真
        File f = new File(mContext.getCacheDir().getAbsolutePath(), fileName + ".JPG");
        if (f.exists()) {
            return;
        }
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(filepath);
            value.compress(Bitmap.CompressFormat.JPEG, 100, os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //二级缓存：软引用缓存（内存） 内存不足 超过20张
    //线程安全
    private static ConcurrentHashMap<String, SoftReference<Bitmap>> secondCacheMap = new ConcurrentHashMap<String, SoftReference<Bitmap>>();


    //三级缓存：本地缓存（硬盘）
    //离线缓存
    //写进内部存储

    /**
     * 加载图片
     *
     * @param key
     * @param imageView
     */
    public void loadImage(String key, ImageView imageView) {
        //读取缓存
        Bitmap bitmap = getFromCache(key);
        if (bitmap != null) {
            //结束该图片对应的所有任务
            cancleDownload(key, imageView);
            imageView.setMaxHeight(bitmap.getHeight());
            imageView.setMaxWidth(bitmap.getWidth());
            imageView.setImageBitmap(bitmap);
        } else {

            //访问网络
            //设置空白图片
            imageView.setImageDrawable(defaultImage);
            //执行异步任务
            AsynImageLoaderTask task = new AsynImageLoaderTask(imageView);
            task.execute(key);
        }
    }

    private void cancleDownload(String key, ImageView imageView) {
        //可能有多个异步任务在下载同一张图片
        AsynImageLoaderTask task = new AsynImageLoaderTask(imageView);
        if (task != null) {
            String downloadkey = task.key;
            if (downloadkey == null || !downloadkey.equals(key)) {
                //设置标示
                task.cancel(true);
            }
        }
    }

    class AsynImageLoaderTask extends AsyncTask<String, Void, Bitmap> {
        private String key;//图片的地址
        private ImageView imageView;

        public AsynImageLoaderTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            key = params[0];
            return download(key);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (isCancelled()) {
                result = null;
            }
            if (result != null) {
                //添加到一级缓存
                addFirstCache(key, result);
                imageView.setImageBitmap(result);
            }
        }
    }

    /**
     * 添加到一级缓存
     *
     * @param key
     * @param bitmap
     */
    private void addFirstCache(String key, Bitmap bitmap) {
        if (bitmap != null) {
            synchronized (firstCacheMap) {
                firstCacheMap.put(key, bitmap);
            }
        }
    }

    /**
     * 联网下载
     * @param key
     * @return
     */
    private Bitmap download(String key) {
        InputStream inputStream = null;
        try {
            inputStream = HttpUtils.download(key);
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 默认图片
     */
    static class DefaultImage extends ColorDrawable {
        public DefaultImage() {
            super(Color.RED);
        }
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    private Bitmap getFromCache(String key) {
        //1从一级缓存加载
        synchronized (firstCacheMap) {
            Bitmap bitmap = firstCacheMap.get(key);
            //保持图片fresh新鲜
            if (bitmap != null) {
                firstCacheMap.remove(key);
                firstCacheMap.put(key, bitmap);
                return bitmap;
            }
        }
        //2从二级
        SoftReference<Bitmap> soft_bitmap = secondCacheMap.get(key);
        if (soft_bitmap != null) {
            Bitmap bitmap = soft_bitmap.get();
            if (bitmap != null) {
                //再添加到一级缓存
                firstCacheMap.put(key, bitmap);
                return bitmap;
            }
        } else {
            //软引用被回收了
            secondCacheMap.remove(key);
        }
        //3从本地
        Bitmap local_bitmap = getFromLocal(key);
        if (local_bitmap != null) {
            //再次添加到一级缓存
            firstCacheMap.put(key, local_bitmap);
            return local_bitmap;
        }
        return null;
    }

    /**
     * 从本地获取图片
     * @param key
     * @return
     */
    private Bitmap getFromLocal(String key) {
        String fileName = MD5.getMD5(key);
        if (fileName == null) {
            return null;
        }
        String path = mContext.getCacheDir().getAbsolutePath() + File.separator + fileName + ".JPG";
        FileInputStream is = null;
        try {
            is = new FileInputStream(path);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}

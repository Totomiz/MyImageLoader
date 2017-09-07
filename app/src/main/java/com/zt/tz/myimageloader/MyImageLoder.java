package com.zt.tz.myimageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by asus on 2016-05-29 10:36
 * QQ:xxxxxxxx
 */
public class MyImageLoder {
    private DefaultDrawble defaultDrawble = new DefaultDrawble();

    private static Context mContext;
    //一级缓存数量
    private static final int MAX_CAPACITY = 20;
    //一级缓存 true 最少使用排序
    private static LinkedHashMap<String, Bitmap> firstCacheMap = new LinkedHashMap<String, Bitmap>(MAX_CAPACITY, 0.75F, true) {
        @Override
        protected boolean removeEldestEntry(Entry<String, Bitmap> eldest) {
            if (this.size() > MAX_CAPACITY) {
                //加入二级缓存
                secondCacheMap.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue()));
                //存入本地
                diskCache(eldest.getKey(), eldest.getValue());
                return true;
            }
            return false;
        }
    };

    /**
     * 缓存本地
     *
     * @param key
     * @param value
     */
    private static void diskCache(String key, Bitmap value) {
        String fileName = MD5.getMD5(key);
        String filepath = mContext.getCacheDir().getAbsolutePath() + File.separator + fileName;
        File file = new File(filepath);
        if (file.exists()) {
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

    //二级缓存
    private static ConcurrentHashMap<String, SoftReference<Bitmap>> secondCacheMap = new ConcurrentHashMap<String, SoftReference<Bitmap>>();

    /**
     * 加载图片
     *
     * @param key
     * @param imageView
     */
    //默认图片
    public void loadImage(String key, ImageView imageView) {
        //读取缓存
        Bitmap bitmap = getFromCache(key);
        if (bitmap != null) {
            cancelDownload(key, imageView);
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageDrawable(defaultDrawble);
            //开启网络下载
            AsyncDownTask task = new AsyncDownTask(imageView);
            task.execute();
        }
    }

    private void cancelDownload(String key, ImageView imageView) {
        AsyncDownTask task = new AsyncDownTask(imageView);
        if (task != null) {
            String downkey = task.key;
            if (downkey == null || !downkey.equals(key)) {
                task.cancel(true);
            }
        }
    }

    /**
     * 从缓存中读取
     *
     * @param key
     * @return
     */
    private static Bitmap getFromCache(String key) {
        //1.从1级缓存中读取

        synchronized (firstCacheMap) {
            Bitmap bitmap = firstCacheMap.get(key);
            if (bitmap != null) {
                firstCacheMap.remove(key);
                firstCacheMap.put(key, bitmap);
                return bitmap;
            }
        }
        //2.从2级中读取
        SoftReference<Bitmap> soft_bitmap = secondCacheMap.get(key);
        if (soft_bitmap != null) {
            Bitmap bitmap = soft_bitmap.get();
            if (bitmap != null) {
                //再添加到一级缓存
                firstCacheMap.put(key, bitmap);
                return bitmap;
            }
        }
        //3.从本地
        Bitmap bitmap = getFromLocal(key);
        if (bitmap != null) {
            //再次添加到一级缓存
            firstCacheMap.put(key, bitmap);
            return bitmap;
        }
        return null;
    }

    /**
     * 从本地获取bitmap
     *
     * @param key
     * @return
     */
    private static Bitmap getFromLocal(String key) {
        String filename = MD5.getMD5(key);
        String filepath = mContext.getCacheDir().getAbsolutePath() + File.separator + filename;
        FileInputStream is = null;
        try {
            is = new FileInputStream(filepath);
            return BitmapFactory.decodeStream(is);
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

    class AsyncDownTask extends AsyncTask<String, Void, Bitmap> {
        private String key;
        private ImageView imageView;

        public AsyncDownTask(ImageView imageView) {
            this.imageView = imageView;
        }


        @Override
        protected Bitmap doInBackground(String... params) {
            key = params[0];
            return downLoad(key);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if(isCancelled()){
                result=null;
            }
            if (result != null) {
                //添加到一级缓存
                addFirstCache(key, result);
                imageView.setImageBitmap(result);
            }
        }
    }

    class DefaultDrawble extends ColorDrawable {
        public DefaultDrawble() {
            super(Color.RED);
        }
    }

    /**
     * 加入一级缓存
     *
     * @param key
     * @param result
     */
    private void addFirstCache(String key, Bitmap result) {
        if (result != null) {
            synchronized (firstCacheMap) {
                firstCacheMap.put(key, result);
            }
        }
    }

    /**
     * 联网下载
     *
     * @param key
     * @return
     */
    private Bitmap downLoad(String key) {
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
}

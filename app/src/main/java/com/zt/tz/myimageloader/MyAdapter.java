package com.zt.tz.myimageloader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by asus on 2016-05-28 18:50
 * QQ:xxxxxxxx
 */
public class MyAdapter extends BaseAdapter {
    private List<String> data;
    LayoutInflater inflater;
    Context context;

    public MyAdapter(List<String> data, Context context) {
        this.data = data;
        this.context = context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.layout_item,null);
            viewHolder.imageView= (ImageView) convertView.findViewById(R.id.iv_item);
            viewHolder.textView= (TextView) convertView.findViewById(R.id.tv_item);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance(context).loadImage(data.get(position),viewHolder.imageView);
        viewHolder.textView.setText(getItemId(position)+"--"+position);
        return convertView;
    }
    private class ViewHolder{
        private ImageView imageView;
        private TextView textView;
    }
}

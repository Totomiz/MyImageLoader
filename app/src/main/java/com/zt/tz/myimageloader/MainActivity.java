package com.zt.tz.myimageloader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView listView;
    private List<String> list;
    private EditText editText;
    private Button btn,btn_rest;
    private MyAdapter adapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView= (ListView) findViewById(R.id.lv);
        editText= (EditText) findViewById(R.id.edt);
        btn= (Button) findViewById(R.id.btn);
        btn_rest= (Button) findViewById(R.id.btn_rest);
        list=new ArrayList<String>();
        btn.setOnClickListener(this);
        btn_rest.setOnClickListener(this);
        initList();
        adapter = new MyAdapter(list,this);
        listView.setAdapter(adapter);

    }

    private void initList() {
        list.add("http://c.hiphotos.baidu.com/image/h%3D200/sign=20e5edfdbe12c8fcabf3f1cdcc0292b4/cefc1e178a82b90118fe5cee748da9773812ef98.jpg");
        list.add("http://f.hiphotos.baidu.com/image/h%3D200/sign=8387106fbe014a909e3e41bd99763971/472309f790529822ef9e961ed0ca7bcb0b46d4ba.jpg");
        list.add("http://e.hiphotos.baidu.com/image/h%3D200/sign=7770c37229dda3cc14e4bf2031e83905/32fa828ba61ea8d3c1592ee9900a304e241f58e7.jpg");
        list.add("http://c.hiphotos.baidu.com/image/h%3D200/sign=ba7bd1f3750e0cf3bff749fb3a47f23d/2fdda3cc7cd98d102bc070d0263fb80e7bec903e.jpg");
        list.add("http://img4.imgtn.bdimg.com/it/u=3446182043,2669943513&fm=23&gp=0.jpg");
        list.add("http://img0.imgtn.bdimg.com/it/u=3448384857,559990725&fm=23&gp=0.jpg");
        list.add("http://img3.imgtn.bdimg.com/it/u=3696304962,108070839&fm=23&gp=0.jpg");
        list.add("http://img5.imgtn.bdimg.com/it/u=3745200188,492375807&fm=23&gp=0.jpg");
        list.add("http://img5.imgtn.bdimg.com/it/u=4174911245,352305040&fm=23&gp=0.jpg");
        list.add("http://img3.imgtn.bdimg.com/it/u=4257801148,3500919645&fm=23&gp=0.jpg");
        list.add("http://img5.imgtn.bdimg.com/it/u=1117295961,2052287738&fm=23&gp=0.jpg");
        list.add("http://img2.imgtn.bdimg.com/it/u=1118685783,27847895&fm=23&gp=0.jpg");
        list.add("http://img3.imgtn.bdimg.com/it/u=1002846181,3575037912&fm=23&gp=0.jpg");
        list.add("http://img2.imgtn.bdimg.com/it/u=137833483,1020999025&fm=23&gp=0.jpg");
        list.add("http://img3.imgtn.bdimg.com/it/u=1069182513,1576376622&fm=23&gp=0.jpg");
        list.add("http://img4.imgtn.bdimg.com/it/u=1425511055,330861611&fm=23&gp=0.jpg");
        list.add("http://img1.imgtn.bdimg.com/it/u=188146512,1621902103&fm=23&gp=0.jpg");
        list.add("http://img4.imgtn.bdimg.com/it/u=2134535013,2517280499&fm=23&gp=0.jpg");
        list.add("http://img0.imgtn.bdimg.com/it/u=4143123046,2310120672&fm=23&gp=0.jpg");
        list.add("http://img5.imgtn.bdimg.com/it/u=511199021,4199647564&fm=23&gp=0.jpg");
        list.add("http://img0.imgtn.bdimg.com/it/u=718208593,4030028631&fm=23&gp=0.jpg");
        list.add("http://img0.imgtn.bdimg.com/it/u=972362357,2976526513&fm=23&gp=0.jpg");
        list.add("http://img5.imgtn.bdimg.com/it/u=1865705779,2672804324&fm=23&gp=0.jpg");
        list.add("http://img3.imgtn.bdimg.com/it/u=1924292675,1371737134&fm=23&gp=0.jpg");
        list.add("http://img5.imgtn.bdimg.com/it/u=2031708715,2793653348&fm=23&gp=0.jpg");
        list.add("http://img3.imgtn.bdimg.com/it/u=4210037016,633614731&fm=23&gp=0.jpg");
        list.add("http://img4.imgtn.bdimg.com/it/u=439096975,523081509&fm=23&gp=0.jpg");
        list.add("http://img3.imgtn.bdimg.com/it/u=981895402,31312349&fm=23&gp=0.jpg");
        list.add("http://img4.imgtn.bdimg.com/it/u=962515365,4119805043&fm=23&gp=0.jpg");
        list.add("http://img2.imgtn.bdimg.com/it/u=1582796180,3403716268&fm=23&gp=0.jpg");
        list.add("http://img0.imgtn.bdimg.com/it/u=2366411512,515471864&fm=23&gp=0.jpg");
        list.add("http://img1.imgtn.bdimg.com/it/u=2654577534,746677249&fm=23&gp=0.jpg");
        list.add("http://img5.imgtn.bdimg.com/it/u=269822696,775942671&fm=23&gp=0.jpg");
        list.add("http://img2.imgtn.bdimg.com/it/u=2785960555,3974030993&fm=23&gp=0.jpg");
        list.add("http://img4.imgtn.bdimg.com/it/u=3009795804,103522210&fm=23&gp=0.jpg");
        list.add("http://img5.imgtn.bdimg.com/it/u=3713290338,1605950031&fm=23&gp=0.jpg");
        list.add("http://img5.imgtn.bdimg.com/it/u=64835869,742136116&fm=23&gp=0.jpg");
        list.add("http://img3.imgtn.bdimg.com/it/u=3273887848,4032549231&fm=23&gp=0.jpg");
        list.add("http://img4.imgtn.bdimg.com/it/u=1546992057,1028378455&fm=23&gp=0.jpg");
        list.add("http://img4.imgtn.bdimg.com/it/u=1126185919,3103318977&fm=23&gp=0.jpg");
        list.add("http://img3.imgtn.bdimg.com/it/u=1138497231,3770053996&fm=23&gp=0.jpg");
        list.add("http://img0.imgtn.bdimg.com/it/u=197803868,1708320052&fm=23&gp=0.jpg");
        list.add("http://img5.imgtn.bdimg.com/it/u=1998877052,1415621493&fm=23&gp=0.jpg");
        list.add("http://img0.imgtn.bdimg.com/it/u=2038261890,1363447608&fm=23&gp=0.jpg");
        list.add("http://img5.imgtn.bdimg.com/it/u=2079066373,1461030142&fm=23&gp=0.jpg");
        list.add("http://img2.imgtn.bdimg.com/it/u=2091190855,742011943&fm=23&gp=0.jpg");
        list.add("http://img1.imgtn.bdimg.com/it/u=2194950241,831156132&fm=23&gp=0.jpg");
        list.add("http://img4.imgtn.bdimg.com/it/u=2473815648,461216578&fm=23&gp=0.jpg");
        list.add("http://img1.imgtn.bdimg.com/it/u=2529436705,1861517974&fm=23&gp=0.jpg");
    }


    public void searchImage(EditText keyWord){
        String word = keyWord.getText().toString();
        if(word==null||"".equals(word.trim())){
            Toast.makeText(this, "请输入搜索的图片关键字", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            String path = "http://image.baidu.com/i?";
            String param = "tn=baiduimagejson&ie=utf-8&ic=0&rn=20&pn="+1+"&word=" + URLEncoder.encode(word, "UTF-8");
            String json = HttpUtils.sendGETRequest(path+param);
            String str = new String (json.getBytes("GBK"),"UTF-8");

            list = HttpUtils.resolveImageData(str);
            Log.i("maintag",str);
            loadData();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void loadData() {
        adapter.notifyDataSetChanged();
//        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter!=null){
            adapter=null;
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn){
            initList();
            loadData();
        }
        if(v.getId()==R.id.btn_rest){
            list.clear();
            initList();
            loadData();
        }

    }
}

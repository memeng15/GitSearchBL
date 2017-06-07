package com.app.searchbl;

import android.util.Log;

import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * Created by Genflix on 5/17/17.
 */

public class MainIO {
    public static final int TYPE_CAT_TOP        = 0;
    public static final int TYPE_CAT_HEAD       = 1;
    public static final int TYPE_CAT_WORD       = 2;
    public static final int TYPE_CAT_CATEGORY   = 3;
    public static final int TYPE_CAT_PRODUCT    = 4;
    public static final int TYPE_CAT_HISTORY    = 5;
    public static final int TYPE_CAT_KEYWORD    = 6;
    public static final int TYPE_CAT_USER       = 7;
    public static final int TYPE_CAT_DETAIL     = 8;

    public static class ListItem {
        public int type=0;
        public String text="";
        public String url="";
        public String price="";
        public String seller="";
        public String jsonstr="";
    }

    public ArrayList<ListItem> getSearchResult(String key,int page) {
        String url="https://api.bukalapak.com/v2/products.json?keywords="+key+"&page="+page+"&per_page=20";
        String eurl = url;
        try {
            eurl = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException exp) {
            exp.printStackTrace();
        }
        JSONObject object=getJSON(eurl);
        if (object!=null) {
            try {
                ArrayList<ListItem> res=new ArrayList<ListItem>();
                JSONArray products=object.getJSONArray("products");
                for (int i=0;i<products.length();i++){
                    JSONObject obj=products.getJSONObject(i);
                    ListItem item=new ListItem();
                    item.type=TYPE_CAT_DETAIL;
                    item.text=obj.getString("name");
                    JSONArray smimg=obj.getJSONArray("small_images");
                    item.url=smimg.getString(0);
                    int prc=obj.getInt("price");
                    prc=prc/1000;
                    item.price="Rp "+prc+"rb";
                    item.seller=obj.getString("seller_name");
                    item.jsonstr=obj.toString();
                    res.add(item);
                }
                return res;
            } catch (JSONException exp) {
                exp.printStackTrace();
            }
        }
        return null;
    }

//    public ArrayList<ListItem> getWordSuggestion(String key) {
//        String url="https://api.bukalapak.com/v2/omniscience/v2.json?user=3e995240926d49ac9ecf60b58ad62167&key=b58bfa45d99e3f1406ff895553eb2d44&word=";
//        url+=key;
//        String eurl = url;
//        try {
//            eurl = URLDecoder.decode(url, "UTF-8");
//        } catch (UnsupportedEncodingException exp) {
//            exp.printStackTrace();
//        }
//        JSONObject object=getJSON(eurl);
//        if (object!=null) {
//            try {
//                ArrayList<ListItem> res=new ArrayList<ListItem>();
//                JSONArray word=object.getJSONArray("word");
//                if (word!=null && word.length()>0){
//                    ListItem item=new ListItem();
//                    item.type=TYPE_CAT_HEAD;
//                    item.text="KATA KUNCI";
//                    item.url="";
//                    res.add(item);
//                    for (int i=0;i<word.length();i++){
//                        item=new ListItem();
//                        item.type=TYPE_CAT_WORD;
//                        item.text=word.getString(i);
//                        item.url="";
//                        res.add(item);
//                    }
//                }
//                JSONArray category=object.getJSONArray("category");
//                if (category!=null && category.length()>0){
//                    ListItem item=new ListItem();
//                    item.type=TYPE_CAT_HEAD;
//                    item.text="KATEGORI";
//                    item.url="";
//                    res.add(item);
//                    for (int i=0;i<category.length();i++){
//                        JSONObject obj=category.getJSONObject(i);
//                        item=new ListItem();
//                        item.type=TYPE_CAT_CATEGORY;
//                        item.text=obj.getString("name") + " dalam " + obj.getString("category");
//                        item.url="";
//                        res.add(item);
//                    }
//                }
//                JSONArray product=object.getJSONArray("product");
//                if (product!=null && product.length()>0){
//                    ListItem item=new ListItem();
//                    item.type=TYPE_CAT_HEAD;
//                    item.text="BARANG";
//                    item.url="";
//                    res.add(item);
//                    for (int i=0;i<product.length();i++){
//                        JSONObject obj=product.getJSONObject(i);
//                        item=new ListItem();
//                        item.type=TYPE_CAT_PRODUCT;
//                        item.text=obj.getString("name");
//                        item.url=obj.getString("img");
//                        int prc=obj.getInt("price");
//                        prc=prc/1000;
//                        item.price="Rp "+prc+"rb";
//                        res.add(item);
//                    }
//                }
//                JSONArray history=object.getJSONArray("history");
//                if (history!=null && history.length()>0){
//                    ListItem item=new ListItem();
//                    item.type=TYPE_CAT_HEAD;
//                    item.text="RIWAYAT BARANG";
//                    item.url="";
//                    res.add(item);
//                    for (int i=0;i<history.length();i++){
//                        JSONObject obj=history.getJSONObject(i);
//                        item=new ListItem();
//                        item.type=TYPE_CAT_HISTORY;
//                        item.text=obj.getString("name");
//                        item.url=obj.getString("img");
//                        int prc=obj.getInt("price");
//                        prc=prc/1000;
//                        item.price="Rp "+prc+"rb";
//                        res.add(item);
//                    }
//                }
//                JSONArray keyword=object.getJSONArray("keyword");
//                if (keyword!=null && keyword.length()>0){
//                    ListItem item=new ListItem();
//                    item.type=TYPE_CAT_HEAD;
//                    item.text="RIWAYAT KATA KUNCI";
//                    item.url="";
//                    res.add(item);
//                    for (int i=0;i<keyword.length();i++){
//                        item=new ListItem();
//                        item.type=TYPE_CAT_KEYWORD;
//                        item.text=keyword.getString(i);
//                        item.url="";
//                        res.add(item);
//                    }
//                }
//                JSONArray user=object.getJSONArray("user");
//                if (user!=null && user.length()>0){
//                    ListItem item=new ListItem();
//                    item.type=TYPE_CAT_HEAD;
//                    item.text="PELAPAK";
//                    item.url="";
//                    res.add(item);
//                    for (int i=0;i<user.length();i++){
//                        JSONObject obj=user.getJSONObject(i);
//                        item=new ListItem();
//                        item.type=TYPE_CAT_USER;
//                        item.text=obj.getString("name");
//                        item.url=obj.getString("img");
//                        res.add(item);
//                    }
//                }
//                return res;
//            } catch (JSONException exp) {
//                exp.printStackTrace();
//            }
//        }
//        return null;
//    }

//    public ArrayList<ListItem> getEmptySuggestion() {
//        JSONObject object=getJSON("https://api.bukalapak.com/v2/top_keywords/index.json");
//        if (object!=null) {
//            try {
//                if (object.getString("status").equalsIgnoreCase("OK")){
//                    JSONArray top_keywords=object.getJSONArray("top_keywords");
//                    ArrayList<ListItem> res=new ArrayList<ListItem>();
//                    for (int i=0;i<top_keywords.length();i++){
//                        ListItem item=new ListItem();
//                        item.type=TYPE_CAT_TOP;
//                        item.text=top_keywords.getString(i);
//                        item.url="";
//                        res.add(item);
//                    }
//                    return res;
//                }
//            } catch (JSONException exp) {
//                exp.printStackTrace();
//            }
//        }
//        return null;
//    }

    public JSONObject getJSON(String url) {
        try {
            HttpGet httpGet=new HttpGet(url);
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(httpGet);
            if (response!=null) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    InputStream in = response.getEntity().getContent();
                    String str = read(in, (int) response.getEntity().getContentLength());
                    return new JSONObject(str);
                }
            }
        } catch(JSONException exp) {
            exp.printStackTrace();
        } catch (IOException exp) {
            exp.printStackTrace();
        }
        return null;
    }

    private String read(InputStream in,int len) throws IOException {
        StringBuilder sb;
        if (len>0){
            sb= new StringBuilder(len);
        }else{
            sb = new StringBuilder();
        }
        byte b[]=new byte[256];
        int r;
        do {
            r=in.read(b);
            if (r<0)break;
            String s=new String(b,0,r);
            Log.v("BASEIO",s);
            sb.append(s);
        } while (r>0);
        in.close();
        return sb.toString();
    }
}

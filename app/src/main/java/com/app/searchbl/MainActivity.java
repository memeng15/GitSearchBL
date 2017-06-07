package com.app.searchbl;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.yelp.android.webimageview.ImageLoader;
import com.yelp.android.webimageview.WebImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.searchbl.MainIO.TYPE_CAT_CATEGORY;
import static com.app.searchbl.MainIO.TYPE_CAT_DETAIL;
import static com.app.searchbl.MainIO.TYPE_CAT_HEAD;
import static com.app.searchbl.MainIO.TYPE_CAT_HISTORY;
import static com.app.searchbl.MainIO.TYPE_CAT_KEYWORD;
import static com.app.searchbl.MainIO.TYPE_CAT_PRODUCT;
import static com.app.searchbl.MainIO.TYPE_CAT_TOP;
import static com.app.searchbl.MainIO.TYPE_CAT_USER;
import static com.app.searchbl.MainIO.TYPE_CAT_WORD;

////////////////////////////////////////////////////////////////////////
//////////                       RETROFIT                     //////////
//////////           http://www.jsonschema2pojo.org           //////////
//////////   https://tutsplus.com/tutorials/search/retrofit   //////////
////////////////////////////////////////////////////////////////////////

public class MainActivity extends AppCompatActivity {
    private String prevKey="";
    private EditText search=null;
    private ImageButton closebtn=null;

    private SOService mService=null;

    private ListAdapter listAdapter=null;
    private ArrayList<MainIO.ListItem> mList=null;

    private ResultAdapter resultAdapter=null;
    private ArrayList<MainIO.ListItem> rList=null;

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(search, InputMethodManager.SHOW_FORCED);
        search.requestFocusFromTouch();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        hideKeyboard();
        View layoutresult=findViewById(R.id.layoutresult);
        if (layoutresult.getVisibility()==View.VISIBLE) {
            layoutresult.setVisibility(View.GONE);
            rList.clear();
            resultAdapter.notifyDataSetChanged();
            View layoutsuggestion=findViewById(R.id.layoutsuggestion);
            layoutsuggestion.setVisibility(View.VISIBLE);
            search.setEnabled(true);
            search.setFocusable(true);
            search.setFocusableInTouchMode(true);
            search.setText(prevKey);
            if (search.length()>0) {
                closebtn.setVisibility(View.VISIBLE);
            }
            return;
        }
        super.onBackPressed();
    }

    private void onClear() {
        search.setText("");
        onEditTextBegin();
    }

    private void onSearch(String key) {
        if (key.length()>0){
            hideKeyboard();
            search.setEnabled(false);
            search.setFocusable(false);
            search.setFocusableInTouchMode(false);
            if (search.length()==0){
                search.setText(key);
            }
            prevKey=search.getText().toString().trim();
            closebtn.setVisibility(View.GONE);
            GetSearchResult gsr=new GetSearchResult();
            gsr.execute(key);
        }else if (search.length()>0){
            hideKeyboard();
            search.setEnabled(false);
            search.setFocusable(false);
            search.setFocusableInTouchMode(false);
            prevKey=search.getText().toString().trim();
            closebtn.setVisibility(View.GONE);
            GetSearchResult gsr=new GetSearchResult();
            gsr.execute(search.getText().toString().trim());
        }
    }

    private void onEditTextBegin() {
        showKeyboard();
        onEditTextChanged();
    }

    public void onEditTextChanged() {
        if (search.length()>0) {
            View layoutresult=findViewById(R.id.layoutresult);
            if (layoutresult.getVisibility()==View.GONE) {
                closebtn.setVisibility(View.VISIBLE);
            }
            GetSuggestWord(search.getText().toString());
//            GetSuggestWord gsw=new GetSuggestWord();
//            gsw.execute(search.getText().toString());
        } else {
            closebtn.setVisibility(View.GONE);
            GetSuggestEmpty();
//            GetSuggestEmpty gse=new GetSuggestEmpty();
//            gse.execute();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageLoader.initialize(getApplicationContext(), null);
        mService = ApiUtils.getSOService();

        search=(EditText)findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                onEditTextChanged();
            }
        });
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                    onSearch("");
                    return true;
                }
                return false;
            }
        });
        search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onEditTextBegin();
                return false;
            }
        });
        closebtn=(ImageButton) findViewById(R.id.button_right_close);
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onClear();
            }
        });
//        ImageButton searchbtn=(ImageButton) findViewById(R.id.button_right_search);
//        searchbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                onSearch("");
//            }
//        });
        ImageButton rightbtn=(ImageButton) findViewById(R.id.button_right_menu);
        rightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
        ListView listView1 = (ListView) findViewById(R.id.suggestList);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainIO.ListItem it=mList.get(position);
                String key=it.text;
                if (it.type==TYPE_CAT_CATEGORY){
                    int pos=key.indexOf(" dalam ");
                    key=key.substring(0,pos);
                }else if (it.type==TYPE_CAT_HEAD){
                    return;
                }
                onSearch(key);
            }
        });
        mList=new ArrayList<MainIO.ListItem>();
        listAdapter = new ListAdapter(this);
        listView1.setAdapter(listAdapter);

        ListView listView2 = (ListView) findViewById(R.id.resultList);
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainIO.ListItem it=rList.get(position);
                try {
                    JSONObject object=new JSONObject(it.jsonstr);
                    Intent i = new Intent(MainActivity.this, DetailActivity.class);
                    JSONArray smimg=object.getJSONArray("images");
                    String url=smimg.getString(0);
                    i.putExtra("url", url);

                    i.putExtra("name", it.text);
                    i.putExtra("prc", it.price);
                    i.putExtra("sell", it.seller);
                    i.putExtra("desc", object.getString("desc"));
                    startActivity(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        rList=new ArrayList<MainIO.ListItem>();
        resultAdapter = new ResultAdapter(this);
        listView2.setAdapter(resultAdapter);

        onEditTextChanged();
    }

    public void GetSuggestEmpty() {
        View pbar=findViewById(R.id.pbar);
        pbar.setVisibility(View.VISIBLE);

        mService.getEmptySuggestion().enqueue(new Callback<TopKeywords>() {
            @Override
            public void onResponse(Call<TopKeywords> call, Response<TopKeywords> response) {
                if(response.isSuccessful() && response.body()!=null) {
                    ArrayList<MainIO.ListItem> res=new ArrayList<MainIO.ListItem>();
                    for (int i=0;i<response.body().getTopKeywords().size();i++){
                        MainIO.ListItem item=new MainIO.ListItem();
                        item.type=TYPE_CAT_TOP;
                        item.text=response.body().getTopKeywords().get(i);
                        item.url="";
                        res.add(item);
                    }
                    if (res!=null && res.size()>0) {
                        mList.clear();
                        mList.addAll(res);
                        listAdapter.notifyDataSetChanged();
                    }
                }
                View pbar=findViewById(R.id.pbar);
                pbar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<TopKeywords> call, Throwable t) {
                View pbar=findViewById(R.id.pbar);
                pbar.setVisibility(View.GONE);
            }
        });
    }

//    class GetSuggestEmpty extends AsyncTask<String, String, ArrayList<MainIO.ListItem>> {
//        View pbar=findViewById(R.id.pbar);
//        @Override
//        protected void onPreExecute() {
//            pbar.setVisibility(View.VISIBLE);
//            super.onPreExecute();
//        }
//        @Override
//        protected ArrayList<MainIO.ListItem> doInBackground(String...params) {
//            MainIO io=new MainIO();
//            return io.getEmptySuggestion();
//        }
//        @Override
//        protected void onPostExecute(ArrayList<MainIO.ListItem> result) {
//            if (result!=null && result.size()>0) {
//                mList.clear();
//                mList.addAll(result);
//                listAdapter.notifyDataSetChanged();
//            }
//            pbar.setVisibility(View.GONE);
//            super.onPostExecute(result);
//        }
//    }

    public void GetSuggestWord(String word) {
        View pbar=findViewById(R.id.pbar);
        pbar.setVisibility(View.VISIBLE);

        mService.getWordSuggestion(word).enqueue(new Callback<Suggestion>() {
            @Override
            public void onResponse(Call<Suggestion> call, Response<Suggestion> response) {
                if(response.isSuccessful() && response.body()!=null) {
                    ArrayList<MainIO.ListItem> res=new ArrayList<MainIO.ListItem>();
                    if (response.body().getWord()!=null && response.body().getWord().size()>0){
                        MainIO.ListItem item=new MainIO.ListItem();
                        item.type=TYPE_CAT_HEAD;
                        item.text="KATA KUNCI";
                        item.url="";
                        res.add(item);
                        for (int i=0;i<response.body().getWord().size();i++){
                            item=new MainIO.ListItem();
                            item.type=TYPE_CAT_WORD;
                            item.text=response.body().getWord().get(i);
                            item.url="";
                            res.add(item);
                        }
                    }
                    if (response.body().getCategory()!=null && response.body().getCategory().size()>0){
                        MainIO.ListItem item=new MainIO.ListItem();
                        item.type=TYPE_CAT_HEAD;
                        item.text="KATEGORI";
                        item.url="";
                        res.add(item);
                        for (int i=0;i<response.body().getCategory().size();i++){
                            Category obj=response.body().getCategory().get(i);
                            item=new MainIO.ListItem();
                            item.type=TYPE_CAT_CATEGORY;
                            item.text=obj.getName() + " dalam " + obj.getCategory();
                            item.url="";
                            res.add(item);
                        }
                    }
                    if (response.body().getProduct()!=null && response.body().getProduct().size()>0){
                        MainIO.ListItem item=new MainIO.ListItem();
                        item.type=TYPE_CAT_HEAD;
                        item.text="BARANG";
                        item.url="";
                        res.add(item);
                        for (int i=0;i<response.body().getProduct().size();i++){
                            Product obj=response.body().getProduct().get(i);
                            item=new MainIO.ListItem();
                            item.type=TYPE_CAT_PRODUCT;
                            item.text=obj.getName();
                            item.url=obj.getImg();
                            int prc=obj.getPrice();
                            prc=prc/1000;
                            item.price="Rp "+prc+"rb";
                            res.add(item);
                        }
                    }
                    if (response.body().getHistory()!=null && response.body().getHistory().size()>0){
                        MainIO.ListItem item=new MainIO.ListItem();
                        item.type=TYPE_CAT_HEAD;
                        item.text="RIWAYAT BARANG";
                        item.url="";
                        res.add(item);
                        for (int i=0;i<response.body().getHistory().size();i++){
                            try {
                                JSONObject obj=(JSONObject)response.body().getHistory().get(i);
                                item=new MainIO.ListItem();
                                item.type=TYPE_CAT_HISTORY;
                                item.text=obj.getString("name");
                                item.url=obj.getString("img");
                                int prc=obj.getInt("price");
                                prc=prc/1000;
                                item.price="Rp "+prc+"rb";
                                res.add(item);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (response.body().getKeyword()!=null && response.body().getKeyword().size()>0){
                        MainIO.ListItem item=new MainIO.ListItem();
                        item.type=TYPE_CAT_HEAD;
                        item.text="RIWAYAT KATA KUNCI";
                        item.url="";
                        res.add(item);
                        for (int i=0;i<response.body().getKeyword().size();i++){
                            item=new MainIO.ListItem();
                            item.type=TYPE_CAT_KEYWORD;
                            item.text=(String)response.body().getKeyword().get(i);
                            item.url="";
                            res.add(item);
                        }
                    }
                    if (response.body().getUser()!=null && response.body().getUser().size()>0){
                        MainIO.ListItem item=new MainIO.ListItem();
                        item.type=TYPE_CAT_HEAD;
                        item.text="PELAPAK";
                        item.url="";
                        res.add(item);
                        for (int i=0;i<response.body().getUser().size();i++){
                            User obj=response.body().getUser().get(i);
                            item=new MainIO.ListItem();
                            item.type=TYPE_CAT_USER;
                            item.text=obj.getName();
                            item.url=obj.getImg();
                            res.add(item);
                        }
                    }
                    if (res!=null && res.size()>0) {
                        mList.clear();
                        mList.addAll(res);
                        listAdapter.notifyDataSetChanged();
                    }
                }
                View pbar=findViewById(R.id.pbar);
                pbar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<Suggestion> call, Throwable t) {
                View pbar=findViewById(R.id.pbar);
                pbar.setVisibility(View.GONE);
            }
        });
    }

//    class GetSuggestWord extends AsyncTask<String, String, ArrayList<MainIO.ListItem>> {
//        View pbar=findViewById(R.id.pbar);
//        @Override
//        protected void onPreExecute() {
//            pbar.setVisibility(View.VISIBLE);
//            super.onPreExecute();
//        }
//        @Override
//        protected ArrayList<MainIO.ListItem> doInBackground(String...params) {
//            MainIO io=new MainIO();
//            return io.getWordSuggestion(params[0]);
//        }
//        @Override
//        protected void onPostExecute(ArrayList<MainIO.ListItem> result) {
//            if (result!=null && result.size()>0) {
//                mList.clear();
//                mList.addAll(result);
//                listAdapter.notifyDataSetChanged();
//            }
//            pbar.setVisibility(View.GONE);
//            super.onPostExecute(result);
//        }
//    }

    class GetSearchResult extends AsyncTask<String, String, ArrayList<MainIO.ListItem>> {
        View pbar2=findViewById(R.id.pbar2);
        View layoutresult=findViewById(R.id.layoutresult);
        View layoutsuggestion=findViewById(R.id.layoutsuggestion);
        @Override
        protected void onPreExecute() {
            layoutsuggestion.setVisibility(View.GONE);
            layoutresult.setVisibility(View.VISIBLE);
            pbar2.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }
        @Override
        protected ArrayList<MainIO.ListItem> doInBackground(String...params) {
            MainIO io=new MainIO();
            return io.getSearchResult(params[0],1);
        }
        @Override
        protected void onPostExecute(ArrayList<MainIO.ListItem> result) {
            if (result!=null && result.size()>0) {
                rList.clear();
                rList.addAll(result);
                resultAdapter.notifyDataSetChanged();
            }
            pbar2.setVisibility(View.GONE);
            super.onPostExecute(result);
        }
    }

    protected class ListAdapter extends BaseAdapter {
        private Context mContext;
        public ListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            MainIO.ListItem it=mList.get(position);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.layout_listscreen, null);
            } else {
                view = convertView;
            }
            TextView title=(TextView) view.findViewById(R.id.title);
            TextView head=(TextView) view.findViewById(R.id.head);
            TextView subtitle=(TextView) view.findViewById(R.id.subtitle);
            TextView price=(TextView) view.findViewById(R.id.price);
            TextView seller=(TextView) view.findViewById(R.id.seller);
            WebImageView img=(WebImageView) view.findViewById(R.id.image);
            if (it.type==TYPE_CAT_TOP || it.type==TYPE_CAT_WORD || it.type==TYPE_CAT_CATEGORY
                    || it.type==TYPE_CAT_KEYWORD){
                head.setVisibility(View.GONE);
                img.setVisibility(View.GONE);
                subtitle.setVisibility(View.GONE);
                price.setVisibility(View.GONE);
                seller.setVisibility(View.GONE);
                title.setVisibility(View.VISIBLE);
                title.setText(it.text);
            } else if (it.type==TYPE_CAT_HEAD) {
                title.setVisibility(View.GONE);
                img.setVisibility(View.GONE);
                subtitle.setVisibility(View.GONE);
                price.setVisibility(View.GONE);
                seller.setVisibility(View.GONE);
                head.setVisibility(View.VISIBLE);
                head.setText(it.text);
            } else if (it.type==TYPE_CAT_PRODUCT || it.type==TYPE_CAT_HISTORY) {
                head.setVisibility(View.GONE);
                title.setVisibility(View.GONE);
                seller.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
                img.setImageUrl(it.url);
                subtitle.setVisibility(View.VISIBLE);
                subtitle.setText(it.text);
                price.setVisibility(View.VISIBLE);
                price.setText(it.price);
            } else if (it.type==TYPE_CAT_USER) {
                head.setVisibility(View.GONE);
                title.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
                img.setImageUrl(it.url);
                subtitle.setVisibility(View.VISIBLE);
                subtitle.setText(it.text);
                price.setVisibility(View.GONE);
                seller.setVisibility(View.GONE);
            } else if (it.type==TYPE_CAT_DETAIL) {
                head.setVisibility(View.GONE);
                title.setVisibility(View.GONE);
                img.setVisibility(View.VISIBLE);
                img.setImageUrl(it.url);
                subtitle.setVisibility(View.VISIBLE);
                subtitle.setText(it.text);
                price.setVisibility(View.VISIBLE);
                price.setText(it.price);
                seller.setVisibility(View.VISIBLE);
                seller.setText(it.seller);
            }
            return view;
        }
    }

    protected class ResultAdapter extends BaseAdapter {
        private Context mContext;
        public ResultAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return rList.size();
        }

        @Override
        public Object getItem(int position) {
            return rList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            MainIO.ListItem it=rList.get(position);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.layout_listscreen, null);
            } else {
                view = convertView;
            }
            TextView title=(TextView) view.findViewById(R.id.title);
            TextView head=(TextView) view.findViewById(R.id.head);
            TextView subtitle=(TextView) view.findViewById(R.id.subtitle);
            TextView price=(TextView) view.findViewById(R.id.price);
            TextView seller=(TextView) view.findViewById(R.id.seller);
            WebImageView img=(WebImageView) view.findViewById(R.id.image);
            head.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
            img.setVisibility(View.VISIBLE);
            img.setImageUrl(it.url);
            subtitle.setVisibility(View.VISIBLE);
            subtitle.setText(it.text);
            price.setVisibility(View.VISIBLE);
            price.setText(it.price);
            seller.setVisibility(View.VISIBLE);
            seller.setText(it.seller);
            return view;
        }
    }
}

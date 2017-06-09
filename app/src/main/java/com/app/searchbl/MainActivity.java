package com.app.searchbl;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.searchbl.controller.ResultAdapter;
import com.app.searchbl.controller.SuggestionAdapter;
import com.app.searchbl.controller.TopKeywordsAdapter;
import com.app.searchbl.model.Category;
import com.app.searchbl.model.Detail;
import com.app.searchbl.model.DetailPrd;
import com.app.searchbl.model.Head;
import com.app.searchbl.model.Product;
import com.app.searchbl.model.Suggestion;
import com.app.searchbl.model.TopKeywords;
import com.app.searchbl.model.User;
import com.yelp.android.webimageview.ImageLoader;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private TopKeywordsAdapter topKeywordsAdapter=null;
    private SuggestionAdapter suggestionAdapter=null;
    private ResultAdapter resultAdapter=null;

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
        ListView listView = (ListView) findViewById(R.id.suggestList);
        if (listView.getAdapter() instanceof ResultAdapter) {
            if (suggestionAdapter!=null) {
                listView.setAdapter(suggestionAdapter);
            } else {
                listView.setAdapter(topKeywordsAdapter);
            }
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
            GetSearchResult(key);
        }else if (search.length()>0){
            hideKeyboard();
            search.setEnabled(false);
            search.setFocusable(false);
            search.setFocusableInTouchMode(false);
            prevKey=search.getText().toString().trim();
            closebtn.setVisibility(View.GONE);
            GetSearchResult(search.getText().toString().trim());
        }
    }

    private void onEditTextBegin() {
        showKeyboard();
        onEditTextChanged();
    }

    public void onEditTextChanged() {
        if (search.length()>0) {
            closebtn.setVisibility(View.VISIBLE);
            GetSuggestWord(search.getText().toString());
        } else {
            closebtn.setVisibility(View.GONE);
            GetSuggestEmpty();
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
                String key="";
                if (parent.getAdapter() instanceof TopKeywordsAdapter) {
                    key=(String)((TopKeywordsAdapter) parent.getAdapter()).getItem(position);
                    onSearch(key);
                } else if (parent.getAdapter() instanceof SuggestionAdapter) {
                    Object obj=((SuggestionAdapter) parent.getAdapter()).getItem(position);
                    if (obj instanceof String) {
                        key=(String)obj;
                    } else if (obj instanceof Category){
                        key=((Category)obj).getName();
                    } else if (obj instanceof Head) {
                        return;
                    } else if (obj instanceof Product) {
                        key=((Product)obj).getName();
                    } else if (obj instanceof User) {
                        key=((User)obj).getName();
                    }
                    onSearch(key);
                } else if (parent.getAdapter() instanceof ResultAdapter) {
                    DetailPrd prd=(DetailPrd) ((ResultAdapter)parent.getAdapter()).getItem(position);
                    Intent i = new Intent(MainActivity.this, DetailActivity.class);
                    i.putExtra("url", prd.getImages().get(0));
                    i.putExtra("name", prd.getName());
                    i.putExtra("prc", "Rp "+(prd.getPrice()/1000)+"rb");
                    i.putExtra("sell", prd.getSellerName());
                    i.putExtra("desc", prd.getDesc());
                    startActivity(i);
                }
            }
        });
        onEditTextChanged();
    }

    public void GetSuggestEmpty() {
        View pbar=findViewById(R.id.pbar);
        pbar.setVisibility(View.VISIBLE);
        mService.getEmptySuggestion().enqueue(new Callback<TopKeywords>() {
            @Override
            public void onResponse(Call<TopKeywords> call, Response<TopKeywords> response) {
                if(response.isSuccessful() && response.body()!=null) {
                    ListView listView = (ListView) findViewById(R.id.suggestList);
                    if (topKeywordsAdapter==null) {
                        topKeywordsAdapter=new TopKeywordsAdapter(MainActivity.this);
                    }
                    topKeywordsAdapter.setTopKeywords(response.body());
                    if (!(listView.getAdapter() instanceof TopKeywordsAdapter)) {
                        listView.setAdapter(topKeywordsAdapter);
                    } else {
                        topKeywordsAdapter.notifyDataSetChanged();
                    }
                }
                View pbar=findViewById(R.id.pbar);
                pbar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<TopKeywords> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                View pbar=findViewById(R.id.pbar);
                pbar.setVisibility(View.GONE);
            }
        });
    }

    public void GetSuggestWord(String word) {
        View pbar=findViewById(R.id.pbar);
        pbar.setVisibility(View.VISIBLE);

        mService.getWordSuggestion(word).enqueue(new Callback<Suggestion>() {
            @Override
            public void onResponse(Call<Suggestion> call, Response<Suggestion> response) {
                if(response.isSuccessful() && response.body()!=null) {
                    ListView listView = (ListView) findViewById(R.id.suggestList);
                    if (suggestionAdapter==null) {
                        suggestionAdapter=new SuggestionAdapter(MainActivity.this);
                    }
                    suggestionAdapter.setSuggestion(response.body());
                    if (!(listView.getAdapter() instanceof SuggestionAdapter)) {
                        listView.setAdapter(suggestionAdapter);
                    } else {
                        suggestionAdapter.notifyDataSetChanged();
                    }
                }
                View pbar=findViewById(R.id.pbar);
                pbar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<Suggestion> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                View pbar=findViewById(R.id.pbar);
                pbar.setVisibility(View.GONE);
            }
        });
    }

    public void GetSearchResult(String key) {
        View pbar=findViewById(R.id.pbar);
        pbar.setVisibility(View.VISIBLE);

        mService.getSearchResult(key,0).enqueue(new Callback<Detail>() {
            @Override
            public void onResponse(Call<Detail> call, Response<Detail> response) {
                if(response.isSuccessful() && response.body()!=null) {
                    ListView listView = (ListView) findViewById(R.id.suggestList);
                    if (resultAdapter==null) {
                        resultAdapter=new ResultAdapter(MainActivity.this);
                    }
                    resultAdapter.setResultDetail(response.body());
                    if (!(listView.getAdapter() instanceof ResultAdapter)) {
                        listView.setAdapter(resultAdapter);
                    } else {
                        resultAdapter.notifyDataSetChanged();
                    }
                }
                View pbar=findViewById(R.id.pbar);
                pbar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<Detail> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                View pbar=findViewById(R.id.pbar);
                pbar.setVisibility(View.GONE);
            }
        });
    }
}

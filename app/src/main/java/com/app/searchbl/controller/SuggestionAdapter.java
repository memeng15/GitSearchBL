package com.app.searchbl.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.searchbl.R;
import com.app.searchbl.model.Category;
import com.app.searchbl.model.Head;
import com.app.searchbl.model.Product;
import com.app.searchbl.model.Suggestion;
import com.app.searchbl.model.User;
import com.yelp.android.webimageview.WebImageView;

import java.util.ArrayList;

/**
 * Created by Genflix on 6/8/17.
 */

public class SuggestionAdapter extends BaseAdapter {
    private Context mContext = null;
    private ArrayList<Object> mList = null;

    public SuggestionAdapter(Context context) {
        mContext = context;
    }

    public void setSuggestion(Suggestion suggestion) {
        if (mList==null) {
            mList = new ArrayList<Object>();
        } else {
            mList.clear();
        }
        if (suggestion!=null) {
            if (suggestion.getWord()!=null && suggestion.getWord().size()>0) {
                mList.add(new Head("KATA KUNCI"));
                mList.addAll(suggestion.getWord());
            }
            if (suggestion.getCategory()!=null && suggestion.getCategory().size()>0) {
                mList.add(new Head("KATEGORI"));
                mList.addAll(suggestion.getCategory());
            }
            if (suggestion.getUser()!=null && suggestion.getUser().size()>0) {
                mList.add(new Head("PELAPAK"));
                mList.addAll(suggestion.getUser());
            }
            if (suggestion.getHistory()!=null && suggestion.getHistory().size()>0) {
                mList.add(new Head("RIWAYAT BARANG"));
                mList.addAll(suggestion.getHistory());
            }
            if (suggestion.getKeyword()!=null && suggestion.getKeyword().size()>0) {
                mList.add(new Head("RIWAYAT KATA KUNCI"));
                mList.addAll(suggestion.getKeyword());
            }
            if (suggestion.getProduct()!=null && suggestion.getProduct().size()>0) {
                mList.add(new Head("BARANG"));
                mList.addAll(suggestion.getProduct());
            }
        }
    }

    @Override
    public int getCount() {
        if (mList!=null) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mList!=null) {
            return mList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_listscreen1, null);
        } else {
            view = convertView;
        }
        TextView title=(TextView) view.findViewById(R.id.title);
        TextView head=(TextView) view.findViewById(R.id.head);
        TextView subtitle=(TextView) view.findViewById(R.id.subtitle);
        TextView price=(TextView) view.findViewById(R.id.price);
        WebImageView img=(WebImageView) view.findViewById(R.id.image);

        Object obj=mList.get(position);
        if (obj instanceof String) {
            head.setVisibility(View.GONE);
            img.setVisibility(View.GONE);
            subtitle.setVisibility(View.GONE);
            price.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
            title.setText((String)obj);
        } else if (obj instanceof Category){
            head.setVisibility(View.GONE);
            img.setVisibility(View.GONE);
            subtitle.setVisibility(View.GONE);
            price.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
            title.setText(((Category)obj).getName() + " dalam " + ((Category)obj).getCategory());
        } else if (obj instanceof Head) {
            title.setVisibility(View.GONE);
            img.setVisibility(View.GONE);
            subtitle.setVisibility(View.GONE);
            price.setVisibility(View.GONE);
            head.setVisibility(View.VISIBLE);
            head.setText(((Head)obj).getName());
        } else if (obj instanceof Product) {
            head.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
            img.setVisibility(View.VISIBLE);
            img.setImageUrl(((Product)obj).getImg());
            subtitle.setVisibility(View.VISIBLE);
            subtitle.setText(((Product)obj).getName());
            price.setVisibility(View.VISIBLE);
            price.setText("Rp "+(((Product)obj).getPrice()/1000)+"rb");
        } else if (obj instanceof User) {
            head.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
            img.setVisibility(View.VISIBLE);
            img.setImageUrl(((User)obj).getImg());
            subtitle.setVisibility(View.VISIBLE);
            subtitle.setText(((User)obj).getName());
            price.setVisibility(View.GONE);
        }
        return view;
    }
}
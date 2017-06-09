package com.app.searchbl.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.searchbl.R;
import com.app.searchbl.model.TopKeywords;

/**
 * Created by Genflix on 6/8/17.
 */

public class TopKeywordsAdapter extends BaseAdapter {
    private Context mContext = null;
    private TopKeywords topKeywords = null;

    public TopKeywordsAdapter(Context context) {
        this.mContext = context;
    }

    public void setTopKeywords(TopKeywords topKey){
        this.topKeywords = topKey;
    }

    @Override
    public int getCount() {
        if (topKeywords!=null && topKeywords.getTopKeywords()!=null) {
            return topKeywords.getTopKeywords().size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (topKeywords!=null && topKeywords.getTopKeywords()!=null) {
            return topKeywords.getTopKeywords().get(position);
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
            view = inflater.inflate(R.layout.layout_listscreen0, null);
        } else {
            view = convertView;
        }
        TextView title = (TextView) view.findViewById(R.id.title);
        if (topKeywords!=null && topKeywords.getTopKeywords()!=null) {
            title.setText(topKeywords.getTopKeywords().get(position));
        }
        return view;
    }
}
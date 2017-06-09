package com.app.searchbl.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.searchbl.R;
import com.app.searchbl.model.Detail;
import com.app.searchbl.model.DetailPrd;
import com.yelp.android.webimageview.WebImageView;

/**
 * Created by Genflix on 6/9/17.
 */

public class ResultAdapter extends BaseAdapter {
    private Context mContext = null;
    private Detail resultDetail = null;

    public ResultAdapter(Context context) {
        this.mContext = context;
    }

    public void setResultDetail(Detail result){
        this.resultDetail = result;
    }

    @Override
    public int getCount() {
        if (resultDetail!=null && resultDetail.getProducts()!=null) {
            return resultDetail.getProducts().size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (resultDetail!=null && resultDetail.getProducts()!=null) {
            return resultDetail.getProducts().get(position);
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
            view = inflater.inflate(R.layout.layout_listscreen2, null);
        } else {
            view = convertView;
        }
        DetailPrd prd=(DetailPrd)resultDetail.getProducts().get(position);
        WebImageView img=(WebImageView) view.findViewById(R.id.image);
        img.setImageUrl(prd.getSmallImages().get(0));

        TextView subtitle=(TextView) view.findViewById(R.id.subtitle);
        subtitle.setText(prd.getName());

        TextView seller=(TextView) view.findViewById(R.id.seller);
        seller.setText(prd.getSellerName());

        TextView price=(TextView) view.findViewById(R.id.price);
        price.setText("Rp "+(prd.getPrice()/1000)+"rb");

        return view;
    }
}
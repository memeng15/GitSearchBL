package com.app.searchbl;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.yelp.android.webimageview.WebImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Genflix on 5/19/17.
 */

public class DetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String url=getIntent().getStringExtra("url");
        WebImageView img=(WebImageView) findViewById(R.id.image);
        img.setImageUrl(url);

        String name=getIntent().getStringExtra("name");
        TextView subtitle=(TextView) findViewById(R.id.subtitle);
        subtitle.setText(name);

        String prc=getIntent().getStringExtra("prc");
        TextView price=(TextView) findViewById(R.id.price);
        price.setText(prc);

        String sell=getIntent().getStringExtra("sell");
        TextView seller=(TextView) findViewById(R.id.seller);
        seller.setText(sell);

        String de=getIntent().getStringExtra("desc");
        TextView desc=(TextView) findViewById(R.id.desc);
        desc.setText(Html.fromHtml(de));
    }
}

package com.example.apidemoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductDescriptionActivity extends AppCompatActivity {

    ImageView image;
    TextView title, desc, price;
    RelativeLayout layout;
    ProgressBar progressBar;
    Integer productId;
    ProductModel currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra("product_id"))
            productId = intentThatStartedThisActivity.getIntExtra("product_id", 1);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="https://fakestoreapi.com/products/" + String.valueOf(productId);

        image = findViewById(R.id.desc_image);
        title = findViewById(R.id.desc_title);
        desc = findViewById(R.id.desc_description);
        price = findViewById(R.id.desc_price);
        layout = findViewById(R.id.desc_layout);
        progressBar = findViewById(R.id.desc_progressbar);

        progressBar.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                progressBar.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
                Log.e("VOLLEY RESULT", jsonObject.toString());

                try {
                    //JSONObject jsonObject = response.getJSONObject();
                    currentProduct = new ProductModel(
                            jsonObject.getInt("id"),
                            jsonObject.getString("title"),
                            jsonObject.getDouble("price"),
                            jsonObject.getString("category"),
                            jsonObject.getString("description"),
                            jsonObject.getString("image")
                    );
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                Glide.with(getApplicationContext())
                        .load(currentProduct.getImage())
                        .centerCrop()
                        .placeholder(R.drawable.placeholder)
                        .into(image);
                title.setText(currentProduct.getTitle());
                desc.setText(currentProduct.getDescription());
                price.setText(String.valueOf(currentProduct.getPrice()));
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY ERROR", error.toString());
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

            }
        });

        queue.add(jsonObjectRequest);

    }
}
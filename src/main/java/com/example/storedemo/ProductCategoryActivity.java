package com.example.storedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryActivity extends AppCompatActivity implements ProductAdapter.ItemClickListener{

    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<ProductModel> productList = new ArrayList<>();
    ProductAdapter adapter;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);

        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra("category"))
            category = intentThatStartedThisActivity.getStringExtra("category");

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url ="https://fakestoreapi.com/products/category/" + category;

        recyclerView = findViewById(R.id.pc_products_recyclerview);
        progressBar = findViewById(R.id.pc_progressbar);

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        Log.e("VOLLEY RESULT", response.toString());
                        for(int i = 0; i < response.length(); i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                ProductModel product = new ProductModel(
                                        jsonObject.getInt("id"),
                                        jsonObject.getString("title"),
                                        jsonObject.getDouble("price"),
                                        jsonObject.getString("category"),
                                        jsonObject.getString("description"),
                                        jsonObject.getString("image")
                                );
                                productList.add(product);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                        adapter = new ProductAdapter(getApplicationContext(), productList, ProductCategoryActivity.this);
                        recyclerView.setAdapter(adapter);
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

    @Override
    public void onItemClick(View view, int position) {
        int id = productList.get(position).getId();
        Intent intent = new Intent(getApplicationContext(), ProductDescriptionActivity.class);
        intent.putExtra("product_id", id);
        startActivity(intent);
    }
}
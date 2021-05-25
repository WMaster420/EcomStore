package com.example.apidemoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ProductsFragment extends Fragment implements ProductAdapter.ItemClickListener{
    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<ProductModel> productList = new ArrayList<>();
    ProductAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="https://fakestoreapi.com/products";

        View root = inflater.inflate(R.layout.fragment_products, container, false);
        recyclerView = root.findViewById(R.id.products_recyclerview);
        progressBar = root.findViewById(R.id.progressbar);

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
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        adapter = new ProductAdapter(getContext(), productList, ProductsFragment.this);
                        recyclerView.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY ERROR", error.toString());
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();

                    }
                });

        queue.add(jsonObjectRequest);

        return root;
    }

    @Override
    public void onItemClick(View view, int position) {
        int id = productList.get(position).getId();
        Intent intent = new Intent(getContext(), ProductDescriptionActivity.class);
        intent.putExtra("product_id", id);
        startActivity(intent);
    }
}
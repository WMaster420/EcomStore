package com.example.apidemoapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class CategoriesFragment extends Fragment {

    TextView tv1, tv2, tv3, tv4;
    CardView cv1, cv2, cv3, cv4;
    RelativeLayout layout;
    ProgressBar progressBar;
    List<String> categories = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categories, container, false);
        tv1 = root.findViewById(R.id.category1);
        tv2 = root.findViewById(R.id.category2);
        tv3 = root.findViewById(R.id.category3);
        tv4 = root.findViewById(R.id.category4);
        cv1 = root.findViewById(R.id.card1);
        cv2 = root.findViewById(R.id.card2);
        cv3 = root.findViewById(R.id.card3);
        cv4 = root.findViewById(R.id.card4);
        layout = root.findViewById(R.id.category_layout);
        progressBar = root.findViewById(R.id.category_progressbar);

        progressBar.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="https://fakestoreapi.com/products/categories";

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.GONE);
                        layout.setVisibility(View.VISIBLE);
                        Log.e("VOLLEY RESULT", response.toString());
                        for(int i = 0; i < response.length(); i++){
                            try {
                                categories.add(response.getString(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        tv1.setText(categories.get(0));
                        tv2.setText(categories.get(1));
                        tv3.setText(categories.get(2));
                        tv4.setText(categories.get(3));


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY ERROR", error.toString());
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();

                    }
                });

        queue.add(jsonObjectRequest);

        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProductCategoryActivity.class);
                intent.putExtra("category", categories.get(0));
                startActivity(intent);
            }
        });

        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProductCategoryActivity.class);
                intent.putExtra("category", categories.get(1));
                startActivity(intent);
            }
        });

        cv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProductCategoryActivity.class);
                intent.putExtra("category", categories.get(2));
                startActivity(intent);
            }
        });

        cv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProductCategoryActivity.class);
                intent.putExtra("category", categories.get(3));
                startActivity(intent);
            }
        });

        return root;
    }
}
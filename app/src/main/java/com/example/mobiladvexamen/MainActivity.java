package com.example.mobiladvexamen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.OnItemClickListener {
    public static final String RECIPE_ID = "recipeID";
    public static final String DEFAULT_QUERY = "beef";

    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    public static ArrayList<Recipe> mRecipeList;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner mySpinner = (Spinner) findViewById(R.id.spinner1);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.filters));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);



        EditText mSearch = findViewById(R.id.search_text);


        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecipeList = new ArrayList<>();
        mRecipeAdapter = new RecipeAdapter(MainActivity.this, mRecipeList);
        mRecyclerView.setAdapter(mRecipeAdapter);
        mRecipeAdapter.setOnItemClickListener(MainActivity.this);

        mRequestQueue = Volley.newRequestQueue(this);


        String query = mSearch.getText().toString();

        findViewById(R.id.reset_filters).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mRecipeList.clear();
                searchRecipes(DEFAULT_QUERY);
            }
        });

        if(query.isEmpty()) {
            searchRecipes(DEFAULT_QUERY);
        }

        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecipeList.clear();
                String query = mSearch.getText().toString();
                String cuisine = mySpinner.getSelectedItem().toString();
                if (query.isEmpty()) {
                    if (cuisine.equals("Filter Cuisine")) {
                        searchRecipes(DEFAULT_QUERY);
                    }
                    searchByCuisine(DEFAULT_QUERY, cuisine);
                }
                else {
                    if (cuisine.equals("Filter Cuisine")) {
                        searchRecipes(query);
                    }
                    searchByCuisine(query, cuisine);
                }

            }



        });




    }

    private void searchRecipes(String query){
        String url = "https://api.edamam.com/api/recipes/v2?type=public&q=" + query + "&app_id=26d9a8b7&app_key=c8df7e6609724fa671ed4c1d44b2b4a7";

        //eerste 3 paramaters
        JsonRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //parameter 4 vr succes, hier komt request aan indien succesvol
                try {
                    JSONArray jsonArray = response.getJSONArray("hits");

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject hit = jsonArray.getJSONObject(i).getJSONObject("recipe");

                        String label = hit.getString("label");
                        String imageUrl = hit.getString("image");
                        JSONArray MealIngredients = hit.getJSONArray("ingredientLines");
                        ArrayList<String> ingr = new ArrayList<>();
                        for(int o = 0; o < MealIngredients.length(); o++){
                            String ingrs = MealIngredients.getString(o);
                            ingr.add(ingrs);
                        }

                        mRecipeList.add(new Recipe(label, imageUrl, ingr));
                    }

                    mRecipeAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            //parameter 5 voor error, hier komt request aan indien error
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }

    private void searchByCuisine(String query, String cuisine){
        String url = "https://api.edamam.com/api/recipes/v2?type=public&q=" + query + "&app_id=26d9a8b7&app_key=c8df7e6609724fa671ed4c1d44b2b4a7&cuisineType=" + cuisine;

        //eerste 3 paramaters
        JsonRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //parameter 4 vr succes, hier komt request aan indien succesvol
                try {
                    JSONArray jsonArray = response.getJSONArray("hits");

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject hit = jsonArray.getJSONObject(i).getJSONObject("recipe");

                        String label = hit.getString("label");
                        String imageUrl = hit.getString("image");
                        JSONArray MealIngredients = hit.getJSONArray("ingredientLines");
                        ArrayList<String> ingr = new ArrayList<>();
                        for(int o = 0; o < MealIngredients.length(); o++){
                            String ingrs = MealIngredients.getString(o);
                            ingr.add(ingrs);
                        }

                        mRecipeList.add(new Recipe(label, imageUrl, ingr));
                    }

                    mRecipeAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            //parameter 5 voor error, hier komt request aan indien error
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        Recipe clickedItem = mRecipeList.get(position);

        detailIntent.putExtra(RECIPE_ID, position);

        startActivity(detailIntent);
    }
}

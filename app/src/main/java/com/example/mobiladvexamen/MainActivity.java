package com.example.mobiladvexamen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

        if(query.isEmpty()) {
            parseJSON(DEFAULT_QUERY);
        }

        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecipeList.clear();
                String query = mSearch.getText().toString();
                if (query.isEmpty()) return;
                parseJSON(query);
            }



        });




    }

    private void parseJSON(String query){
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

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        Recipe clickedItem = mRecipeList.get(position);

        // Er zijn drie opties om de cat-details naar de DetailActivity te krijgen:
        // 1: alle nodige details één voor één in als EXTRA in de Intent stoppen
        //    Dit was de originele code van Sarah en staat hieronder in comments
        // 2: De Array met katten ergens "publiek" beschikbaar maken en enkel de index van
        //    de gewenste kat in de Intent te steken.
        //    We doen dit in dit voorbeeld door hem public te zetten in MainActivity en dan
        //    vanuit DetailActivity de kat op te zoeken in de Array.
        //    In een "echt" project zou je hier een ViewModel voor gebruiken.
        // 3. Je kan de kat ook als object in de rugzak steken, maar daarvoor moet je
        //    de "Serializable" interface implementeren, zodat Android weet hoe het een hele
        //    kan in een zak kan krijgen (en er weer uit)...
        //    Dat voorbeeld werken we hier niet uit.


        // Stop alle onderdelen van de kat apart in een zak
        // detailIntent.putExtra(EXTRA_URL, clickedItem.getImageUrl());
        // detailIntent.putExtra(EXTRA_CREATOR, clickedItem.getCreator());
        // detailIntent.putExtra(EXTRA_LIKES, clickedItem.getLikeCount());

        // Stop alleen het nummer van de kat in de zak
        detailIntent.putExtra(RECIPE_ID, position);

        startActivity(detailIntent);
    }
}
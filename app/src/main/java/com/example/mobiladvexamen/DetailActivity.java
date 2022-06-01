package com.example.mobiladvexamen;

import android.content.Intent;
import android.os.Bundle;
import android.os.RecoverySystem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.detailLayout);

        Intent intent = getIntent();
        //String imageUrl = intent.getStringExtra(EXTRA_URL);
        //String creatorName = intent.getStringExtra(EXTRA_CREATOR);
        //int likeCount = intent.getIntExtra(EXTRA_LIKES, 0);
        int RecipeID = intent.getIntExtra(MainActivity.RECIPE_ID, 0);

        String imageUrl = MainActivity.mRecipeList.get(RecipeID).getImage();
        String label = MainActivity.mRecipeList.get(RecipeID).getLabel();
        ArrayList<String> ingr = MainActivity.mRecipeList.get(RecipeID).getMealIngredients();

        String[] array = ingr.toArray(new String[0]);


        String ingredientList = "Name:" + label + "\n ingredients:";

        for(int i = 0; i < array.length; i++) {
            TextView textView = new TextView(DetailActivity.this);
            textView.setText(array[i]);

            linearLayout.addView(textView);

        }




        ImageView imageView = findViewById(R.id.image_view_detail);
        TextView textViewCreator = findViewById(R.id.text_view_label_detail);
        Button share = findViewById(R.id.button_share_detail);

        Picasso.get().load(imageUrl).fit().centerInside().into(imageView);
        textViewCreator.setText(label);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuffer sb = new StringBuffer();
                for(int i = 0; i < array.length; i++) {
                    sb.append(array[i] + "\n");
                }
                String ingredients = sb.toString();
                String content = "Recipe name:\n" + label + "\n" + "ingredients:\n" + ingredients;
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, content);
                startActivity(Intent.createChooser(shareIntent, "Share link using"));
            }
        });
    }


}

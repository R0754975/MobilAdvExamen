package com.example.mobiladvexamen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        //String imageUrl = intent.getStringExtra(EXTRA_URL);
        //String creatorName = intent.getStringExtra(EXTRA_CREATOR);
        //int likeCount = intent.getIntExtra(EXTRA_LIKES, 0);
        int catID = intent.getIntExtra(MainActivity.RECIPE_ID, 0);

        String imageUrl = MainActivity.mRecipeList.get(catID).getImage();
        String creatorName = MainActivity.mRecipeList.get(catID).getLabel();

        ImageView imageView = findViewById(R.id.image_view_detail);
        TextView textViewCreator = findViewById(R.id.text_view_label_detail);
        Button share = findViewById(R.id.button_share_detail);

        Picasso.get().load(imageUrl).fit().centerInside().into(imageView);
        textViewCreator.setText(creatorName);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, imageUrl);
                startActivity(Intent.createChooser(shareIntent, "Share link using"));
            }
        });
    }


}

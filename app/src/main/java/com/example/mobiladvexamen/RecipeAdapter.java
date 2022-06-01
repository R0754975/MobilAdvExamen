package com.example.mobiladvexamen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private Context mContext;
    private ArrayList<Recipe> mRecipeList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int poisiton);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public RecipeAdapter(Context context, ArrayList<Recipe> recipelist){
        mContext = context;
        mRecipeList = recipelist;
    }

    @NonNull
    @androidx.annotation.NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull @androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.RecipeViewHolder holder, int position) {
        Recipe currentRecipe = mRecipeList.get(position);

        String imageUrl = currentRecipe.getImage();
        String label = currentRecipe.getLabel();

        holder.mTextViewLabel.setText(label);
        Picasso.get().load(imageUrl).fit().centerInside().into(holder.mImageView);


    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextViewLabel;

        public RecipeViewHolder(@NonNull @androidx.annotation.NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_view);
            mTextViewLabel = itemView.findViewById(R.id.text_view_label);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(mListener != null){
                        int position = getBindingAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });

        }

    }




}

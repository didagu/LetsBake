package com.example.android.letsbake.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.letsbake.R;
import com.example.android.letsbake.pojo.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Dominic Idagu on 01/07/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    final private ListItemClickListener mOnClickListener;
    final private ArrayList<Recipe> recipe;

    public RecipeAdapter(ListItemClickListener mOnClickListener, ArrayList<Recipe> recipe) {
        this.mOnClickListener = mOnClickListener;
        this.recipe = recipe;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_card_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        RecipeViewHolder viewHolder = new RecipeViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return recipe.size();
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        ImageView icon;
        TextView name;
        TextView servings;


        public RecipeViewHolder(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.recipes_image);
            name = (TextView) itemView.findViewById(R.id.recipes_name);
            servings = (TextView) itemView.findViewById(R.id.recipes_servings);
            itemView.setOnClickListener(this);
        }

        void onBind(int position) {
            if (!recipe.isEmpty()) {
                if(recipe.get(position).getImage().isEmpty()){
                    icon.setImageResource(R.drawable.img_no_thumb);
                }else {
                    Picasso.with(itemView.getContext()).load(recipe.get(position).getImage()).into(icon);
                }
                name.setText(recipe.get(position).getName());
                servings.setText(itemView.getContext().getString(R.string.servings) + " " + recipe.get(position).getServings());
            }
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}

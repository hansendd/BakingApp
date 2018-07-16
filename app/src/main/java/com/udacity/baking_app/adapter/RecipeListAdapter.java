package com.udacity.baking_app.adapter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.baking_app.R;
import com.udacity.baking_app.model.Recipe;
import com.udacity.baking_app.utility.NetworkConnectionUtility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListAdapterViewHolder> {
    private List<Recipe> recipeList;
    private final Context context;

    final private RecipeListOnClickHandler recipeListOnClickHandler;

    public interface RecipeListOnClickHandler {
        void onClick(Recipe recipe);
    }

    public RecipeListAdapter(Context context,
                             RecipeListOnClickHandler recipeListOnClickHandler,
                             List<Recipe> recipeList) {
        this.context = context;
        this.recipeListOnClickHandler = recipeListOnClickHandler;
        this.recipeList = recipeList;
    }

    @Override
    public RecipeListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_list_item, parent, false);

        return new RecipeListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeListAdapter.RecipeListAdapterViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);

        if (null != recipe.getImage() &&
            !"".equals(recipe.getImage())) {
            if (NetworkConnectionUtility.haveActiveNetworkConnection(getConnectivityManager())) {
                Picasso.with(context)
                       .load(recipe.getImage())
                       .into(holder.recipeImage);
            }
        }

        holder.recipeName.setText(recipe.getName());
        holder.serviceSize.setText(Integer.toString(recipe.getServings()));
    }

    private ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public int getItemCount() {
        if (null == recipeList) return 0;
        return recipeList.size();
    }

    public class RecipeListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imageview_recipe_image) ImageView recipeImage;
        @BindView(R.id.textview_recipe_name) TextView recipeName;
        @BindView(R.id.textview_serving_size) TextView serviceSize;

        public RecipeListAdapterViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = recipeList.get(adapterPosition);

            recipeListOnClickHandler.onClick(recipe);
        }
    }
}

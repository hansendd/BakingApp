package com.udacity.baking_app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.baking_app.R;
import com.udacity.baking_app.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailStepAdapter extends RecyclerView.Adapter<RecipeDetailStepAdapter.RecipeDetailStepAdapterViewHolder> {
    private List<Step> stepList;
    private final Context context;

    final private RecipeDetailStepOnClickHandler recipeDetailStepOnClickHandler;

    public interface RecipeDetailStepOnClickHandler {
        void onClickRecipeDetailStep(int selectedIndex, List<Step> stepList);
    }

    public RecipeDetailStepAdapter(Context context,
                                   RecipeDetailStepOnClickHandler recipeDetailStepOnClickHandler,
                                   List<Step> stepList) {
        this.context = context;
        this.recipeDetailStepOnClickHandler = recipeDetailStepOnClickHandler;
        this.stepList = stepList;
    }

    @Override
    public RecipeDetailStepAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_detail_step_list_item, parent, false);

        return new RecipeDetailStepAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeDetailStepAdapter.RecipeDetailStepAdapterViewHolder holder, int position) {
        Step step = stepList.get(position);

        String stepDescription = String.format("%s %s",
                                               step.getId() + 1,
                                               step.getShortDescription());

        holder.stepDescriptionTextView.setText(stepDescription);
    }

    @Override
    public int getItemCount() {
        if (null == stepList) return 0;
        return stepList.size();
    }

    public class RecipeDetailStepAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.textview_step_description) TextView stepDescriptionTextView;

        public RecipeDetailStepAdapterViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recipeDetailStepOnClickHandler.onClickRecipeDetailStep(getAdapterPosition(), stepList);
        }
    }

    public void loadSteps(List<Step> stepList) {
        this.stepList.clear();
        this.stepList.addAll(stepList);
        notifyDataSetChanged();
    }
}

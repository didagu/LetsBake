package com.example.android.letsbake.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.letsbake.R;
import com.example.android.letsbake.StepsDetailsActivity;
import com.example.android.letsbake.adapters.IngredientsAdapter;
import com.example.android.letsbake.adapters.StepsAdapter;
import com.example.android.letsbake.pojo.Steps;

import java.util.ArrayList;

import static com.example.android.letsbake.MainActivity.isTablet;
import static com.example.android.letsbake.fragments.RecipeFragment.recipes;

/**
 * Created by Dominic Idagu on 02/07/2017.
 */

public class StepsActivityFragment extends android.support.v4.app.Fragment implements StepsAdapter.ListItemClickListener{

    private RecyclerView stepsRecyclerView;
    private RecyclerView ingredientRecyclerView;
    private StepsAdapter stepsAdapter;
    private IngredientsAdapter ingredientsAdapter;
    private int index = 0;
    public static ArrayList<Steps> steps = new ArrayList<>();


    public StepsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps, container, false);

        stepsRecyclerView = (RecyclerView) view.findViewById(R.id.stepslist);
        ingredientRecyclerView = (RecyclerView) view.findViewById(R.id.ingredientslist);
        stepsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        index = getActivity().getIntent().getExtras().getInt("item");
        steps = recipes.get(index).getSteps();
        stepsAdapter = new StepsAdapter(this, steps);
        ingredientsAdapter = new IngredientsAdapter(recipes.get(index).getIngredients());
        stepsRecyclerView.setAdapter(stepsAdapter);
        ingredientRecyclerView.setAdapter(ingredientsAdapter);

        return view;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (!isTablet) {
            Intent intent = new Intent(getActivity(), StepsDetailsActivity.class);
            intent.putExtra("item", clickedItemIndex);
            startActivity(intent);
        } else {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            StepsDetailsActivityFragment stepsDetailsFragment = new StepsDetailsActivityFragment();
            stepsDetailsFragment.index = clickedItemIndex;
            fragmentManager.beginTransaction()
                    .replace(R.id.stepsdetailsframe, stepsDetailsFragment)
                    .commit();
        }
    }

}


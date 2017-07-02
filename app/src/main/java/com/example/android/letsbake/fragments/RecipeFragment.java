package com.example.android.letsbake.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.letsbake.R;
import com.example.android.letsbake.StepsActivity;
import com.example.android.letsbake.adapters.RecipeAdapter;
import com.example.android.letsbake.pojo.Recipe;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.example.android.letsbake.MainActivity.isTablet;

public class RecipeFragment extends Fragment implements RecipeAdapter.ListItemClickListener,
        SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG =RecipeFragment.class.getSimpleName() ;
    public static ArrayList<Recipe> recipes = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private TextView no_network;
    private SwipeRefreshLayout swipeRefreshLayout;

    public RecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_recipe, container, false);
        
        no_network = (TextView) view.findViewById(R.id.network_error);
        no_network.setVisibility(View.GONE);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        networkUp();
        downloadRecipes();
        return view;
    }

    private void downloadRecipes() {
        if(networkUp()){
            swipeRefreshLayout.setRefreshing(true);
            new FetchRecipeTask().execute();
        }else {
            no_network.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private boolean networkUp() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onRefresh() {
        downloadRecipes();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(getActivity(), StepsActivity.class);
        intent.putExtra("item", clickedItemIndex);
        startActivity(intent);
    }

    public class FetchRecipeTask extends AsyncTask<Void,Void,ArrayList<Recipe>> {

        @Override
        protected ArrayList<Recipe> doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            //Udacity Recipes
            final String UDACITY_BASE_URL_MOVIE = "https://go.udacity.com/android-baking-app-json";

            try {
                Uri builtUri = Uri.parse(UDACITY_BASE_URL_MOVIE)
                        .buildUpon()
                        .build();
                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                JSONArray movieArray = new JSONArray(buffer.toString());
                recipes = new ArrayList<>();
                for (int i = 0; i < movieArray.length(); i++) {
                    recipes.add(new Recipe(movieArray.getJSONObject(i)));
                    Log.e("name: ", recipes.get(i).getName());
                }
                return recipes;
            } catch (Exception e) {
                e.printStackTrace();
                return recipes;
            } finally {
                try {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        reader.close();
                    }
                } catch (Exception e) {

                    Log.d(TAG, e.getMessage());
                }
            }

        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> recipes) {
            loadViews(recipes);
            swipeRefreshLayout.setRefreshing(false);
        }
    }
    private void loadViews(ArrayList<Recipe> bakes) {
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recipe_list);
        RecyclerView.LayoutManager layoutManager;
        if (isTablet) {
            layoutManager = new GridLayoutManager(getActivity(), 3);
        } else {
            if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT)
            {
                layoutManager = new LinearLayoutManager(getActivity());
            }else {
                layoutManager = new GridLayoutManager(getActivity(), 2);
            }
        }
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecipeAdapter(this, bakes);
        recyclerView.setAdapter(adapter);
    }
}

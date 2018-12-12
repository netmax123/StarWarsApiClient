/*
Main activity connects to a remote API and displays a list of StarWars heroes
Created by Maxim G. on December 12, 2018
*/

package com.dvinasystems.heroes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dvinasystems.heroes.json.People;
import com.dvinasystems.heroes.json.ResultsList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private List<PeopleList> peopleList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PeopleAdapter mAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // history activity
        Button historyButton = findViewById(R.id.history_button);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });


        // recyclerview for the main activity
        recyclerView = findViewById(R.id.main_recycler_view);
        mAdapter = new PeopleAdapter(peopleList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);


        // getting a keyword to search
        final EditText searchQuery = findViewById(R.id.search_query);
        Button searchButton = findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (searchQuery != null)
                    searchCall(searchQuery.getText().toString());
            }
        });


    }

    // retrofit request
    public void searchCall(String key) {

        peopleList.clear();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<ResultsList> call = api.getAllResults("https://swapi.co/api/people/?search=" + key);
        call.enqueue(new Callback<ResultsList>() {
            @Override
            public void onResponse(Call<ResultsList> call, Response<ResultsList> response) {

                ResultsList body = response.body();
                List<People> people = body.getResults();

                Log.v("response", "Retrofit OK");

                for (People currentOrders : people) {

                    PeopleList heroes = new PeopleList(currentOrders.getName(), currentOrders.getHeight(),
                            currentOrders.getMass(), currentOrders.getHairColor(), currentOrders.getSkinColor(), currentOrders.getEyeColor(),
                            currentOrders.getGender());
                    peopleList.add(heroes);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResultsList> call, Throwable t) {
                Log.v("response", "failure from retrofit");

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
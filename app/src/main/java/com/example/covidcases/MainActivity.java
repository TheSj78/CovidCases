package com.example.covidcases;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView numberOfCases, lastUpdatedView, numberOfRecovered, numberOfActive, numberOfDeaths;
    private Button viewByCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberOfCases = findViewById(R.id.numOfCountryCasesView);
        lastUpdatedView = findViewById(R.id.lastUpdatedView);
        numberOfRecovered = findViewById(R.id.numOfCountryRecoveredView);
        numberOfActive = findViewById(R.id.numOfCountryActiveView);
        numberOfDeaths = findViewById(R.id.numOfCountryDeathsView);
        viewByCountry = findViewById(R.id.viewByCountryButton);

        //swipe refresh layout:
        SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.bringToFront();
        pullToRefresh.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        pullToRefresh.setOnRefreshListener(() -> {
            new FindData().execute();
            pullToRefresh.setRefreshing(false);
        }); //adding refresh

        new FindData().execute();
    }

    public class FindData extends AsyncTask<Void, Void, Void>
    {
        private String lastUpdated, cases, deaths, recovered, active;

        @Override
        protected void onPreExecute() //setting all TextViews to "Loading…"
        {
            String load = "Loading…";
            numberOfCases.setText(load);
            lastUpdatedView.setText(load);
            numberOfRecovered.setText(load);
            numberOfActive.setText(load);
            numberOfDeaths.setText(load);
            viewByCountry.setClickable(false);
        }

        @Override
        protected Void doInBackground(Void... params) //finding number of cases
        {
            try {
                String fullString = Jsoup.connect("https://www.worldometers.info/coronavirus/").get().text();

                lastUpdated = fullString.substring(
                        fullString.indexOf("Last"),
                        fullString.indexOf(" Weekly")
                ).trim();

                cases = fullString.substring(
                        fullString.indexOf("Cases:")+6,
                        fullString.indexOf(" view")
                ).trim();

                deaths = fullString.substring(
                        fullString.indexOf("Deaths:")+7,
                        fullString.indexOf(" Recovered")
                ).trim();

                recovered = fullString.substring(
                        fullString.indexOf("Recovered:")+10,
                        fullString.indexOf(" Active")
                ).trim();

                active = fullString.substring(
                        fullString.indexOf("Active Cases")+12,
                        fullString.indexOf(" Currently Infected")
                ).trim();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)  //setting textviews to number of cases
        {
            super.onPostExecute(aVoid);
            lastUpdatedView.setText(lastUpdated);
            numberOfCases.setText(cases);
            numberOfActive.setText(active);
            numberOfRecovered.setText(recovered);
            numberOfDeaths.setText(deaths);
            viewByCountry.setClickable(true);
        }
    }

    public void changePages(View view)
    {
        Intent intent = new Intent(this, SelectCountry.class);
        startActivity(intent);
    }
}
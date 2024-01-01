package com.example.covidcases;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import java.io.IOException;

public class CountryCases extends AppCompatActivity {
    private TextView cCases, cNumberOfCases, cRecovered, cNumberOfRecovered, cDeaths, cNumberOfDeaths;
    private TextView e404NotFound;
    private String country;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_cases);

        cCases = findViewById(R.id.countryCaseTextView);
        cNumberOfCases = findViewById(R.id.numOfCountryCasesView);
        cRecovered = findViewById(R.id.countryRecoveredView);
        cNumberOfRecovered = findViewById(R.id.numOfCountryRecoveredView);
        cDeaths = findViewById(R.id.countryDeathsView);
        cNumberOfDeaths = findViewById(R.id.numOfCountryDeathsView);
        e404NotFound = findViewById(R.id.noDataFound);
        e404NotFound.setHeight(0);

        Intent intent = getIntent();
        country = intent.getStringExtra(SelectCountry.EXTRA_MESSAGE);

        TextView countryName = findViewById(R.id.countryName);
        countryName.setText(country);
        updateString(); //making the country name "website-friendly"
        new FindData2().execute();
    }

    public void updateString() //updating the country string to parse
    {
        country = StringUtils.stripAccents(country);
        country = country.toLowerCase();
        country = country.replace(" ", "-");
        country = country.replace(".", "");

        switch(country)
        {
            case "united-states":
                country = "us";
                break;
            case "united-kingdom":
                country = "uk";
                break;
        }
    }

    public void goBackToSelect(View view)
    {
        finish();
    }

    public class FindData2 extends AsyncTask<Void, Void, Void>
    {
        private String number, recovered, deaths;

        protected void onPreExecute()
        {
            //initialize stuff here
            e404NotFound.setVisibility(View.INVISIBLE);
            String load = "Loading…";
            cNumberOfCases.setText(load);
            cNumberOfRecovered.setText(load);
            cNumberOfDeaths.setText(load);
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                String website = "https://www.worldometers.info/coronavirus/country/" + country;
                String text = Jsoup.connect(website).get().text();
                System.out.println(text);

                if (text.substring(0, 3).equals("404"))
                    error();

                number = text.substring(
                        text.indexOf("Coronavirus Cases: ")+19,
                        text.indexOf(" Deaths")
                ).trim();

                recovered = text.substring(
                        text.indexOf("Recovered: ")+11,
                        text.indexOf(" Daily")
                ).trim();

                deaths = text.substring(
                        text.indexOf("Deaths: ")+8,
                        text.indexOf(" Recovered")
                ).trim();
            }
            catch (Exception e)
            {
                error();
            }

            return null;
        }

        public void error()
        {
            cCases.setVisibility(View.INVISIBLE);
            cNumberOfCases.setVisibility(View.INVISIBLE);
            cRecovered.setVisibility(View.INVISIBLE);
            cNumberOfRecovered.setVisibility(View.INVISIBLE);
            cDeaths.setVisibility(View.INVISIBLE);
            cNumberOfDeaths.setVisibility(View.INVISIBLE);
            e404NotFound.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void unused)
        {
            super.onPostExecute(unused);
            e404NotFound.setHeight(0);
            cNumberOfCases.setText(number);
            cNumberOfRecovered.setText(recovered);
            cNumberOfDeaths.setText(deaths);
        }
    }


}
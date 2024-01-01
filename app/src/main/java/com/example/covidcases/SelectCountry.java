package com.example.covidcases;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mikelau.countrypickerx.CountryPickerDialog;

/**
 * TODO: find better CountryPicker, also fix the issue with button
 */
public class SelectCountry extends AppCompatActivity
{
    private CountryPickerDialog countryPickerDialog;
    private Button select;
    private String country;
    private TextView selected;
    private Button go;

    public static final String EXTRA_MESSAGE = "Country";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_country);
        selected = findViewById(R.id.yourCountryView);
        go = findViewById(R.id.nextButton);
        select = findViewById(R.id.selectCountryButton);

        go.setClickable(false);
        countryPickerDialog = new CountryPickerDialog(SelectCountry.this, (c, i) ->
        {
            country = c.getCountryName(SelectCountry.this);
            selected.setText(getString(R.string.country, country));
            //updateString();
            go.setClickable(true);

            select.setClickable(true);
            countryPickerDialog.dismiss();
        }, false, 0);
    }

    public void pickCountry(View view)
    {
        countryPickerDialog.show();
    }

    public void nextPage(View view)
    {
        Intent intent = new Intent(this, CountryCases.class);
        intent.putExtra(EXTRA_MESSAGE, country);
        reset();
        startActivity(intent);
    }

    public void goBack(View view)
    {
        finish();
    }

    public void reset()
    {
        //change color of go button here
        go.setClickable(false);
    }
}
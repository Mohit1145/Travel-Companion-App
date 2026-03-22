package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerCategory, spinnerFrom, spinnerTo;
    EditText etInput;
    Button btnConvert;
    TextView tvResult;

    String[] categories = {"Currency", "Fuel", "Temperature"};
    String[] currencyUnits = {"USD", "AUD", "EUR", "JPY", "GBP"};
    String[] fuelUnits = {"mpg", "km/L", "gallon", "liter", "nautical mile", "kilometer"};
    String[] tempUnits = {"Celsius", "Fahrenheit", "Kelvin"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        etInput = findViewById(R.id.etInput);
        btnConvert = findViewById(R.id.btnConvert);
        tvResult = findViewById(R.id.tvResult);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        updateUnitSpinners("Currency");

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categories[position];
                updateUnitSpinners(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnConvert.setOnClickListener(v -> {
            String inputText = etInput.getText().toString().trim();

            if (inputText.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter a value", Toast.LENGTH_SHORT).show();
                return;
            }

            double inputValue;
            try {
                inputValue = Double.parseDouble(inputText);
            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
                return;
            }

            String category = spinnerCategory.getSelectedItem().toString();
            String fromUnit = spinnerFrom.getSelectedItem().toString();
            String toUnit = spinnerTo.getSelectedItem().toString();

            Double result = null;

            switch (category) {
                case "Currency":
                    result = convertCurrency(fromUnit, toUnit, inputValue);
                    break;
                case "Fuel":
                    result = convertFuel(fromUnit, toUnit, inputValue);
                    break;
                case "Temperature":
                    result = convertTemperature(fromUnit, toUnit, inputValue);
                    break;
            }

            if (result != null) {
                tvResult.setText("Result: " + String.format("%.2f", result));
            } else {
                tvResult.setText("Conversion not supported");
            }
        });
    }

    private void updateUnitSpinners(String category) {
        String[] units;

        switch (category) {
            case "Currency":
                units = currencyUnits;
                break;
            case "Fuel":
                units = fuelUnits;
                break;
            case "Temperature":
                units = tempUnits;
                break;
            default:
                units = new String[]{};
        }

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                units
        );
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFrom.setAdapter(unitAdapter);
        spinnerTo.setAdapter(unitAdapter);
    }

    private double convertCurrency(String from, String to, double value) {
        double toUsd;

        switch (from) {
            case "USD":
                toUsd = value;
                break;
            case "AUD":
                toUsd = value / 1.55;
                break;
            case "EUR":
                toUsd = value / 0.92;
                break;
            case "JPY":
                toUsd = value / 148.50;
                break;
            case "GBP":
                toUsd = value / 0.78;
                break;
            default:
                toUsd = value;
        }

        switch (to) {
            case "USD":
                return toUsd;
            case "AUD":
                return toUsd * 1.55;
            case "EUR":
                return toUsd * 0.92;
            case "JPY":
                return toUsd * 148.50;
            case "GBP":
                return toUsd * 0.78;
            default:
                return toUsd;
        }
    }

    private Double convertFuel(String from, String to, double value) {
        if (from.equals(to)) return value;

        if (from.equals("mpg") && to.equals("km/L")) return value * 0.425;
        if (from.equals("km/L") && to.equals("mpg")) return value / 0.425;

        if (from.equals("gallon") && to.equals("liter")) return value * 3.785;
        if (from.equals("liter") && to.equals("gallon")) return value / 3.785;

        if (from.equals("nautical mile") && to.equals("kilometer")) return value * 1.852;
        if (from.equals("kilometer") && to.equals("nautical mile")) return value / 1.852;

        return null;
    }

    private Double convertTemperature(String from, String to, double value) {
        if (from.equals(to)) return value;

        if (from.equals("Celsius") && to.equals("Fahrenheit")) return (value * 1.8) + 32;
        if (from.equals("Fahrenheit") && to.equals("Celsius")) return (value - 32) / 1.8;

        if (from.equals("Celsius") && to.equals("Kelvin")) return value + 273.15;
        if (from.equals("Kelvin") && to.equals("Celsius")) return value - 273.15;

        if (from.equals("Fahrenheit") && to.equals("Kelvin")) return ((value - 32) / 1.8) + 273.15;
        if (from.equals("Kelvin") && to.equals("Fahrenheit")) return ((value - 273.15) * 1.8) + 32;

        return null;
    }
}
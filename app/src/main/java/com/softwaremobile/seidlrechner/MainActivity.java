package com.softwaremobile.seidlrechner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView textView_von, textView_nach;
    EditText edit_von, edit_nach;
    Button button_calc;
    ImageButton button_plus, button_minus, button_change, button_change_nach;
    float von = 1.0f, total_lt = 0.0f;
    String einheit, einheit_nach;
    public static HashMap<String, String> einheiten;
    Iterator<Map.Entry<String, String>> entries;
    Iterator<Map.Entry<String, String>> entries_nach;
    DecimalFormat df = new DecimalFormat("#.##");
    private long pressedTime;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_von = findViewById(R.id.editText_von);
        edit_von.setText(String.valueOf(von));
        edit_nach = findViewById(R.id.editText_nach);
        button_calc = findViewById(R.id.button_calc);
        button_plus = findViewById(R.id.button_plus);
        button_minus = findViewById(R.id.button_minus);
        /*
        button_change = findViewById(R.id.button_change);
        button_change_nach = findViewById(R.id.button_change_nach);
        textView_von = findViewById(R.id.textView_von);
        textView_nach = findViewById(R.id.textView_nach);
        */
        edit_nach.setText(String.valueOf(einheit_nach));
        Spinner mySpinner = findViewById(R.id.spinner);

        einheiten = getAll();
        if (einheiten.isEmpty()) {
            einheiten.put("1.0f", "Liter");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item);
        einheiten.forEach((k, v) -> {
            adapter.add(v);
        });
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        mySpinner.setAdapter(adapter);

        Spinner mySpinner2 = findViewById(R.id.spinner2);
        mySpinner2.setAdapter(adapter);
        mySpinner2.setSelection(adapter.getPosition("Seiterl"));

        entries = einheiten.entrySet().iterator();
        Map.Entry<String, String> entry = entries.next();
        einheit = entry.getKey();

        entries_nach = einheiten.entrySet().iterator();
        Map.Entry<String, String> entry_nach = entries_nach.next();
        einheit_nach = entry_nach.getKey();

        //textView_nach.setText(einheiten.get(einheit_nach));
        //textView_von.setText(einheiten.get(einheit));

        calc();

        button_calc.setOnClickListener(v -> calc());

        button_plus.setOnClickListener(v -> {
            if (von == 0.0f) {
                button_minus.setEnabled(true);
            }
            von = getVon();
            von += 1;
            edit_von.setText(String.valueOf(von));
            calc();
        });

        button_minus.setOnClickListener(v -> {
            von = getVon();
            if (von <= 1.0) {
                button_minus.setEnabled(false);
            }
            von -= 1.0f;
            total_lt -= Float.parseFloat(einheit);
            edit_von.setText(String.valueOf(von));
            calc();
        });

        mySpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (entries_nach.hasNext()) {
                            String item = (String) adapterView.getItemAtPosition(i);
                            einheiten.forEach((k, v) -> {
                                if (v.equals(item)) {
                                    einheit = k;
                                }
                            });
                        } else {
                            String item = (String) adapterView.getItemAtPosition(i);
                            einheiten.forEach((k, v) -> {
                                if (v.equals(item)) {
                                    einheit = k;
                                }
                            });
                        }
                        //calc()
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                }
        );

        mySpinner2.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (entries_nach.hasNext()) {
                            String item = (String) adapterView.getItemAtPosition(i);
                            einheiten.forEach((k, v) -> {
                                if (v.equals(item)) {
                                    einheit_nach = k;
                                }
                            });
                        } else {
                            String item = (String) adapterView.getItemAtPosition(i);
                            einheiten.forEach((k, v) -> {
                                if (v.equals(item)) {
                                    einheit_nach = k;
                                }
                            });
                        }
                        //calc()
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                }
        );
        /*
        button_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entries.hasNext()) {
                    Map.Entry<String, String> entry = entries.next();
                    einheit = entry.getKey();
                    textView_von.setText(einheiten.get(String.valueOf(einheit)));
                } else {
                    entries = einheiten.entrySet().iterator();
                    Map.Entry<String, String> entry = entries.next();
                    einheit = entry.getKey();
                    textView_von.setText(einheiten.get(String.valueOf(einheit)));
                }
                calc();
            }
        });

        button_change_nach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entries_nach.hasNext()) {
                    Map.Entry<String, String> entry_nach = entries_nach.next();
                    einheit_nach = entry_nach.getKey();
                    textView_nach.setText(einheiten.get(String.valueOf(einheit_nach)));
                } else {
                    entries_nach = einheiten.entrySet().iterator();
                    Map.Entry<String, String> entry_nach = entries_nach.next();
                    einheit_nach = entry_nach.getKey();
                    textView_nach.setText(einheiten.get(String.valueOf(einheit_nach)));
                }
                calc();
            }
        });

         */
    }

    public void calc() {
        von = getVon();
        total_lt = von * Float.parseFloat(einheit);
        edit_nach.setText(df.format(total_lt / Float.parseFloat(einheit_nach)));
    }

    public float getVon() {
        if (!TextUtils.isEmpty(edit_von.getText().toString()))
            return Float.parseFloat(edit_von.getText().toString());
        else return 0.0f;
    }

    public HashMap<String, String> getAll() {
        SharedPreferences prefs = getSharedPreferences("Bier", Context.MODE_PRIVATE);
        return (HashMap<String, String>) new HashMap(prefs.getAll());
    }

    public void toConfig(View view) {
        Intent i = new Intent(MainActivity.this, ConfigActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finishAffinity();
        } else {
            Toast.makeText(getBaseContext(), R.string.back_again, Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
}
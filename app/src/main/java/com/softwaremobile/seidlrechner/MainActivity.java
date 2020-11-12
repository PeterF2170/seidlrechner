package com.softwaremobile.seidlrechner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends BaseActivity {

    TextView textView_von, textView_nach;
    EditText edit_von, edit_nach;
    Button button_calc, button_plus, button_minus, button_change, button_change_nach;
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
        button_change = findViewById(R.id.button_change);
        button_change_nach = findViewById(R.id.button_change_nach);
        textView_von = findViewById(R.id.textView_von);
        textView_nach = findViewById(R.id.textView_nach);
        edit_nach.setText(String.valueOf(einheit_nach));

        einheiten = getAll();
        if (einheiten.isEmpty()) {
            einheiten.put("1.0f", "Liter");
        }

        entries = einheiten.entrySet().iterator();
        Map.Entry<String, String> entry = entries.next();
        einheit = entry.getKey();

        entries_nach = einheiten.entrySet().iterator();
        Map.Entry<String, String> entry_nach = entries_nach.next();
        einheit_nach = entry_nach.getKey();

        textView_nach.setText(einheiten.get(einheit_nach));
        textView_von.setText(einheiten.get(einheit));

        calc();

        button_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calc();
            }
        });

        button_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (von == 0.0f) {
                    button_minus.setEnabled(true);
                }
                von = getVon();
                von += 1;
                edit_von.setText(String.valueOf(von));
                calc();
            }
        });

        button_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                von = getVon();
                if (von <= 1.0) {
                    button_minus.setEnabled(false);
                }
                von -= 1.0f;
                total_lt -= Float.parseFloat(einheit);
                edit_von.setText(String.valueOf(von));
                calc();
            }
        });

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
package com.softwaremobile.seidlrechner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class ConfigActivity extends AppCompatActivity {

    TextView textView_item_name, textView_item_einheit;
    Button button_add_item;
    LinearLayout linearLayout_items;
    EditText input;
    HashMap<String, String> einheiten_all;
    int tv_id_prefix = 0, button_id_prefix = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        einheiten_all = new HashMap<>();
        einheiten_all.put("0.3f", "Seiterl");
        einheiten_all.put("0.5f", "Krügerl");
        einheiten_all.put("2.0f", "Doppler");
        einheiten_all.put("0.25f", "Quartl");
        einheiten_all.put("4.0f", "Doppleliesl");
        einheiten_all.put("5.0f", "Grenadier");
        einheiten_all.put("0.568f", "Pint");
        einheiten_all.put("0.227f", "Half Pint");
        einheiten_all.put("0.33f", "Kugel");
        einheiten_all.put("1.125f", "Kunigundenmaß");
        einheiten_all.put("0.284f", "Ladies' Pint");
        einheiten_all.put("1.0f", "Liter");
        einheiten_all.put("0.1f", "Stößchen");
        einheiten_all.put("3.6f", "Stübchen");
        einheiten_all.put("1.136f", "Yard");

        SharedPreferences prefs = getSharedPreferences("Bier_all", Context.MODE_PRIVATE);
        if (!prefs.contains("0.3f")) mapIntoSp(einheiten_all);

        HashMap<String, String> map = getAll();
        System.out.println(map);
        linearLayout_items = findViewById(R.id.linearlayout_items);

        for (final HashMap.Entry<String, String> entry : map.entrySet()) {

            View item_view = getLayoutInflater().inflate(R.layout.bierliste_item, linearLayout_items, false);
            textView_item_einheit = item_view.findViewById(R.id.textView_item_einheit);
            textView_item_name = item_view.findViewById(R.id.textView_item_name);
            textView_item_name.setTag(R.string.tag_value, entry.getValue());
            textView_item_name.setTag(R.string.tag_key, entry.getKey());
            textView_item_name.setText(entry.getValue());
            textView_item_name.setId(generateID(entry.getKey()));
            button_add_item = item_view.findViewById(R.id.button_add_item);
            button_add_item.setId(10000+generateID(entry.getKey()));
            System.out.println(button_add_item.getId());

            button_add_item.setTag(R.string.tag_key, entry.getKey());

            if (MainActivity.einheiten.containsKey(entry.getKey())) {
                button_add_item.setText("Remove");
                button_add_item.setTag(R.string.button_active, "true");
            } else {
                button_add_item.setTag(R.string.button_active, "false");
                button_add_item.setText("add");
            }
            button_add_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = findViewById(v.getId());
                    if (addOrRemoveItem(v, entry.getKey(), entry.getValue())) {
                        b.setText("add");
                        b.setTag(R.string.button_active, "false");
                    } else {
                        b.setText("remove");
                        b.setTag(R.string.button_active, "true");
                    }
                }
            });


            textView_item_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final String current_tag = (String) v.getTag(R.string.tag_key);     //0.3
                    final TextView current_tv = findViewById(v.getId());
                    final String current_value = current_tv.getText().toString(); //Seidl
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConfigActivity.this);
                    builder.setTitle("edit unit");
                    View customLayout = getLayoutInflater().inflate(R.layout.dialog, null);
                    input = customLayout.findViewById(R.id.editText_dialog);
                    input.setText(current_value);
                    builder.setView(customLayout);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            einheiten_all.replace(current_tag, input.getText().toString());
                            updateSp(current_tag, input.getText().toString());
                            current_tv.setText(input.getText().toString());
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();

                }

            });
            textView_item_einheit.setText(String.valueOf(Float.parseFloat(entry.getKey())) + " L");
            linearLayout_items.addView(item_view);
        }
    }


    private int generateID(String value) {
        float f = Float.parseFloat(value);
        String s = String.valueOf(f).replace(".", "");
        return Integer.parseInt(s);
    }

    public boolean addOrRemoveItem(View v, String key, String value) {
        SharedPreferences prefs = getSharedPreferences("Bier", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (Boolean.parseBoolean(v.getTag(R.string.button_active).toString())) {
            System.out.println("key " + key + " " + value);
            editor.remove(String.valueOf(key));
            Toast.makeText(ConfigActivity.this, "removed " + value, Toast.LENGTH_SHORT).show();
            editor.apply();
            return true;
        } else {
            editor.putString(String.valueOf(key), value);
            Toast.makeText(ConfigActivity.this, "added " + value, Toast.LENGTH_SHORT).show();
            editor.apply();
            return false;
        }
    }

    public void updateSp(String updateKey, String updateValue) {
        SharedPreferences prefs = getSharedPreferences("Bier_all", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(updateKey, updateValue);
        editor.apply();
        SharedPreferences prefs2 = getSharedPreferences("Bier", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = prefs2.edit();
        editor2.putString(updateKey, updateValue);
        editor2.apply();
    }

    public void mapIntoSp(HashMap<String, String> map) {
        SharedPreferences prefs = getSharedPreferences("Bier_all", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        for (HashMap.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey());
            editor.putString(entry.getKey(), entry.getValue());
        }
        editor.apply();
    }

    public HashMap<String, String> getAll() {
        SharedPreferences prefs = getSharedPreferences("Bier_all", Context.MODE_PRIVATE);
        return (HashMap<String, String>) new HashMap(prefs.getAll());
    }

    public void save(View view) {
        Intent i = new Intent(ConfigActivity.this, MainActivity.class);
        startActivity(i);
    }
}
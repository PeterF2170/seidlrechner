package com.softwaremobile.seidlrechner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class ConfigActivity extends AppCompatActivity {

    TextView textView_item_name, textView_item_einheit;
    Button button_add_item;
    LinearLayout linearLayout_items;
    String buttonText;

    HashMap<String, String> einheiten_all;

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
        einheiten_all.put("1.0f", "Maß");
        einheiten_all.put("0.1f", "Stößchen");
        einheiten_all.put("3.6f", "Stübchen");
        einheiten_all.put("1.136f", "Yard");

        linearLayout_items = findViewById(R.id.linearlayout_items);
        int i = 0;
        for (final HashMap.Entry<String, String> entry : einheiten_all.entrySet()) {
            View item_view = getLayoutInflater().inflate(R.layout.bierliste_item, linearLayout_items, false);
            textView_item_einheit = item_view.findViewById(R.id.textView_item_einheit);
            button_add_item = item_view.findViewById(R.id.button_add_item);

            if (MainActivity.einheiten.containsKey(entry.getKey())) {
                button_add_item.setText("Remove");
                button_add_item.setTag("true");
            }else{
                button_add_item.setTag("false");
                button_add_item.setText("add");
            }
            button_add_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(addOrRemoveItem(v,entry.getKey(), entry.getValue())){
                        button_add_item.setText("lol");
                    }else{
                        button_add_item.setText("remove");
                    }
                }
            });
            textView_item_name = item_view.findViewById(R.id.textView_item_name);
            textView_item_name.setText(entry.getValue());
            textView_item_einheit.setText(String.valueOf(Float.parseFloat(entry.getKey())) + " L");
            linearLayout_items.addView(item_view);
        }
    }

    public boolean addOrRemoveItem(View v, String key, String value){
        SharedPreferences prefs = getSharedPreferences("Bier",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if(Boolean.parseBoolean(v.getTag().toString())){
            System.out.println("key " + key + " " + value);
            editor.remove(String.valueOf(key));
            Toast.makeText(ConfigActivity.this, "removed "+ value , Toast.LENGTH_SHORT).show();
            editor.apply();
            return true;
        }else{
            editor.putString(String.valueOf(key),value);
            Toast.makeText(ConfigActivity.this, "added "+ value , Toast.LENGTH_SHORT).show();
            editor.apply();
            return false;
        }
    }

    public void save(View view){
        Intent i = new Intent(ConfigActivity.this,MainActivity.class);
        startActivity(i);
    }
}
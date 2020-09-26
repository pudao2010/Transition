package com.hencoder.hencoderpracticedraw1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by pucheng on 2018/3/4.
 */

public class TestActivity extends AppCompatActivity {

    private ListView lvOne;
    private ListView lvTwo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        lvOne = (ListView) findViewById(R.id.lv_one);
        lvTwo = (ListView) findViewById(R.id.lv_two);

        String[] strings1 = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
        String[] strings2 = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O"};

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, strings1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, strings2);

        lvOne.setAdapter(adapter1);
        lvTwo.setAdapter(adapter2);

    }
}

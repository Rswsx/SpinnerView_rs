package com.example.kanceyuanyaoganfenyuan.spinnerview_rs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {
    private boolean load = true;
    private boolean focus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SpinnerView spinnerView = (SpinnerView) findViewById(R.id.spinner);
        ArrayList<String> ndList = new ArrayList<>();
        ndList.add("2015");
        ndList.add("2016");
        ndList.add("2017");
        ndList.add("2018");
        ndList.add("2019");
        ndList.add("2020");
        spinnerView.setData(ndList);
        spinnerView.setItemSelectedListener(new SpinnerView.itemSelectedListener() {
            @Override
            public void onItemSelected(String value) {
                Toast.makeText(MainActivity.this, value, LENGTH_SHORT).show();
            }
        });
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (load) {
                    List<String> lyList = new ArrayList<>();
                    lyList.add("全部");
                    lyList.add("andorid");
                    lyList.add("ios");
                    spinnerView.refreshListView(lyList);
                    load = false;
                } else {
                    ArrayList<String> ndList = new ArrayList<>();
                    ndList.add("2015");
                    ndList.add("2016");
                    ndList.add("2017");
                    ndList.add("2018");
                    ndList.add("2019");
                    ndList.add("2020");
                    spinnerView.refreshListView(ndList);
                    load = true;
                }
            }
        });
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (focus) {
                    spinnerView.setSpinnerTextFocusable(true);
                    focus=false;
                }else {
                    spinnerView.setSpinnerTextFocusable(false);
                    focus=true;
                }
            }
        });

        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText t=(EditText)findViewById(R.id.editText);
                String value=t.getText().toString();
                int heg = (int) Double.parseDouble(value);
                // spinnerView.setSelection(heg);
                spinnerView.setListViewH(heg);
            }
        });

    }
}


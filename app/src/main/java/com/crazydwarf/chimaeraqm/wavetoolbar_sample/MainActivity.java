package com.crazydwarf.chimaeraqm.wavetoolbar_sample;

import android.app.ActivityManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crazydwarf.chimaeraqm.wavetoolbar.WaveToolbar;

public class MainActivity extends AppCompatActivity
{

    /**
     * exitTime represent the gap between two EXIT button clicks
     */
    private long exitTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WaveToolbar toolBar = findViewById(R.id.top_menu);
        setSupportActionBar(toolBar);
        toolBar.setMenuIconClickListener(new WaveToolbar.MenuIconClickListener() {
            @Override
            public void OnClick(View view) {
                Toast.makeText(getApplicationContext(), "You just clicked MENU button", Toast.LENGTH_LONG).show();
            }
        });

        toolBar.setBackIconClickListener(new WaveToolbar.BackIconClickListener() {
            @Override
            public void OnClick() {
                //Make two continuous click to exit the programme
                activityExit();
            }
        });

        Button bn = findViewById(R.id.bn_change);
        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolBar.setmTitleColor(Color.RED);
            }
        });
    }

    public void activityExit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "One more click to exit the programme", Toast.LENGTH_LONG).show();
            exitTime = System.currentTimeMillis();
        } else {
            Toast.makeText(getApplicationContext(), "Made two continuous click to exit the programme", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}

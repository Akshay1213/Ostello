package com.xoxytech.ostello;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;


public class Filter extends Activity {
    CrystalRangeSeekbar rangeSeekbar;
    TextView txt3;
    //Button b1;
    TextView tvMin, tvMax;
    ToggleButton toggleButtonboys, toggleButtongirls, toggleButtoncoed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        toggleButtonboys = (ToggleButton) findViewById(R.id.toggleboys);
        toggleButtongirls = (ToggleButton) findViewById(R.id.togglegirls);
        toggleButtoncoed = (ToggleButton) findViewById(R.id.togglecoed);
        toggleButtongirls.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                toggleButtonboys.setChecked(false);
                toggleButtoncoed.setChecked(false);
                if (b == true) {
                    toggleButtongirls.setChecked(true);
                    Log.d("*******", "" + b);
                }

            }
        });
        toggleButtoncoed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                toggleButtonboys.setChecked(false);
                toggleButtongirls.setChecked(false);
                if (b == true) {
                    toggleButtoncoed.setChecked(true);
                    Log.d("*******", "" + b);
                }


            }
        });
        toggleButtonboys.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                toggleButtoncoed.setChecked(false);
                toggleButtongirls.setChecked(false);
                if (b == true) {
                    toggleButtonboys.setChecked(true);
                    Log.d("*******", "" + b);
                }


            }
        });
        rangeSeekbar = (CrystalRangeSeekbar) findViewById(R.id.rangeSeekbar3);
//        txt3=(TextView)findViewById(R.id.textView2);
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin = (TextView) findViewById(R.id.txtmin);
                tvMax = (TextView) findViewById(R.id.txtmax);
                tvMin.setText(String.valueOf(minValue) + " Rs.");
                tvMax.setText(String.valueOf(maxValue) + " Rs.");

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.filtercloseanim,R.anim.filtercloseanim);

        Intent setData = new Intent();
        setData.putExtra("filter", "");
        setResult(RESULT_OK, setData);
        finish();
    }

}
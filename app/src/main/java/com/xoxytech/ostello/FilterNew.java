package com.xoxytech.ostello;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

public class FilterNew extends AppCompatActivity {
    ToggleButton togglebuttonboys, togglebuttongirls, togglebuttoncoed, toggleelevator, toggledrinkingwater, togglecot, togglecctv, toggleac, toggleelectricity, togglegym, togglehotwater, toggletv, togglecleaning, toggleparking, togglewashingmachine, togglemess, togglestudytable, togglewifi;
    CrystalRangeSeekbar rangeSeekbar;
    TextView txt3;
    Button submit;
    String togglestatus = "", gender = "", price = "";
    TextView tvMin, tvMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_new);
        togglebuttonboys = (ToggleButton) findViewById(R.id.toggleboys);

        togglebuttongirls = (ToggleButton) findViewById(R.id.togglegirls);
        togglebuttoncoed = (ToggleButton) findViewById(R.id.togglecoed);
        toggleelevator = (ToggleButton) findViewById(R.id.toggleElevator);
        toggledrinkingwater = (ToggleButton) findViewById(R.id.toggleDrinkingwater);
        togglecot = (ToggleButton) findViewById(R.id.toggleCot);
        togglecctv = (ToggleButton) findViewById(R.id.togglecctv);
        toggleac = (ToggleButton) findViewById(R.id.toggleAc);
        toggleelectricity = (ToggleButton) findViewById(R.id.toggleElectricity);
        togglegym = (ToggleButton) findViewById(R.id.toggleGym);
        togglehotwater = (ToggleButton) findViewById(R.id.toggleHotwater);
        toggletv = (ToggleButton) findViewById(R.id.toggleTV);
        togglecleaning = (ToggleButton) findViewById(R.id.toggleCleaning);
        toggleparking = (ToggleButton) findViewById(R.id.toggleParking);
        togglewashingmachine = (ToggleButton) findViewById(R.id.toggleWashingmachine);
        togglemess = (ToggleButton) findViewById(R.id.toggleMess);
        togglestudytable = (ToggleButton) findViewById(R.id.toggleStudytable);
        togglewifi = (ToggleButton) findViewById(R.id.toggleWifi);
        rangeSeekbar = (CrystalRangeSeekbar) findViewById(R.id.rangeSeekbar3);
        submit = (Button) findViewById(R.id.btnSubmit);
        // txt3=(TextView)findViewById(R.id.textView2);
        tvMin = (TextView) findViewById(R.id.txtmin);
        tvMax = (TextView) findViewById(R.id.txtmax);
        //  b1 = (Button)findViewById(R.id.button);


        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {

                tvMin.setText(String.valueOf(minValue) + " Rs.");
                tvMax.setText(String.valueOf(maxValue) + " Rs.");


            }
        });


        CompoundButton.OnCheckedChangeListener changeChecker = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                Toast.makeText(FilterNew.this, compoundButton.getText(), Toast.LENGTH_SHORT).show();
                if (compoundButton == togglebuttonboys) {
                    Toast.makeText(FilterNew.this, compoundButton.getText(), Toast.LENGTH_SHORT).show();
                }


            }
        };
        View.OnClickListener toggleListner = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == togglebuttonboys) {
                    togglebuttongirls.setChecked(false);
                    togglebuttoncoed.setChecked(false);
                    togglebuttonboys.setChecked(true);
                } else if (view == togglebuttongirls) {

                    togglebuttoncoed.setChecked(false);
                    togglebuttonboys.setChecked(false);
                    togglebuttongirls.setChecked(true);
                } else if (view == togglebuttoncoed) {


                    togglebuttonboys.setChecked(false);
                    togglebuttongirls.setChecked(false);
                    togglebuttoncoed.setChecked(true);
                }
            }
        };
        togglebuttonboys.setOnClickListener(toggleListner);
        togglebuttoncoed.setOnClickListener(toggleListner);
        togglebuttongirls.setOnClickListener(toggleListner);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (togglebuttonboys.isChecked())
                    gender = "Boys";

                if (togglebuttongirls.isChecked())
                    gender = "Girls";

                if (togglebuttoncoed.isChecked())
                    gender = "CO-ed";

                if (toggleelevator.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (toggledrinkingwater.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (togglecot.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (togglecctv.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (toggleac.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (toggleelectricity.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (togglegym.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (togglehotwater.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (toggletv.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (togglecleaning.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (toggleparking.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";
                if (togglewashingmachine.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (togglemess.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (togglestudytable.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                if (togglewifi.isChecked())
                    togglestatus += "1";
                else
                    togglestatus += "0";

                price += "Min price=" + tvMin.getText().toString() + " ";
                price += "Max price=" + tvMax.getText().toString() + " ";

                //Toast.makeText(FilterNew.this,togglestatus+gender+price,Toast.LENGTH_LONG).show();

                // togglestatus="";
                //gender="";
                //price="";

                Intent intent = new Intent();
                intent.putExtra("data", togglestatus + gender + price);
                setResult(RESULT_OK, intent);
                finish();

            }
        });


    }


}

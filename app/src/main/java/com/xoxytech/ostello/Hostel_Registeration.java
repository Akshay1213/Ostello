package com.xoxytech.ostello;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Hostel_Registeration extends AppCompatActivity {

    EditText editText_ownername;
    Button submit;
    TextView name, mobilenumber1, mobilenumber2, typetext, hostelname, hosteladdr, price, vacancy;
    EditText textname, txtmobilenumber1, txtmobilenumber2, texthostelname, texthosteladdr, textprice, textvacancy;
    String sname, smobilenumber1, smobilenumber2, stype, sgender, shostelname, saddress, sprice, svacancy, togglestatus = "";
    ListView list;
    Spinner type;
    RadioGroup radioGroup;
    ToggleButton toggleelevator, toggledrinkingwater, togglecot, togglecctv, toggleac, toggleelectricity, togglegym, togglehotwater, toggletv, togglecleaning, toggleparking, togglewashingmachine, togglemess, togglestudytable, togglewifi;
    int flag = 0;
    RequestQueue queue;
    String url;

    TextView messageText;
    int serverResponseCode = 0;
    ProgressDialog dialog1;
    String s, name1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hostel__registeration);

        //SharedPreferences sp = getSharedPreferences("YourSharedPreference",Activity.MODE_PRIVATE);
        // final String username= sp.getString("USER_NAME",null);
        editText_ownername = (EditText) findViewById(R.id.editTextusername);

        queue = Volley.newRequestQueue(Hostel_Registeration.this);
        url = "http://ostallo.com/ostello/addhostel.php";
        submit = (Button) findViewById(R.id.button1);
        name = (TextView) findViewById(R.id.txtusername);
        typetext = (TextView) findViewById(R.id.txttype);
        mobilenumber1 = (TextView) findViewById(R.id.txtmobilenumber1);
        mobilenumber2 = (TextView) findViewById(R.id.txtmobilenumber2);
        textname = (EditText) findViewById(R.id.editTextusername);
        txtmobilenumber1 = (EditText) findViewById(R.id.editTextmobile1);
        txtmobilenumber2 = (EditText) findViewById(R.id.editTextmobile2);
        hostelname = (TextView) findViewById(R.id.txthostelname);
        hosteladdr = (TextView) findViewById(R.id.txtaddress);
        price = (TextView) findViewById(R.id.txtprice);
        vacancy = (TextView) findViewById(R.id.textvacancy);
        texthostelname = (EditText) findViewById(R.id.editTexthostelname);
        texthosteladdr = (EditText) findViewById(R.id.editTextaddress);
        textprice = (EditText) findViewById(R.id.editTextprice);
        textvacancy = (EditText) findViewById(R.id.editTextvacancy);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
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
        togglestudytable = (ToggleButton) findViewById(R.id.toggleStudytable);
        togglewifi = (ToggleButton) findViewById(R.id.toggleWifi);
        togglemess = (ToggleButton) findViewById(R.id.toggleMess);

        type = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editText_ownername.setText("John");

                sname = textname.getText().toString() + " ";
                smobilenumber1 = txtmobilenumber1.getText().toString() + " ";
                smobilenumber2 = txtmobilenumber2.getText().toString() + " ";

                stype = type.getSelectedItem().toString() + " ";
                sgender = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString() + " ";
                shostelname = texthostelname.getText().toString() + " ";
                saddress = texthosteladdr.getText().toString() + " ";
                sprice = textprice.getText().toString() + " ";
                svacancy = textvacancy.getText().toString() + " ";
                if (textname.getText().toString().length() == 0) {
                    textname.setError("First name is required!");
                    flag = 1;
                }


                if (txtmobilenumber1.getText().toString().length() == 0) {
                    txtmobilenumber1.setError("Mobile number is required!");
                    flag = 1;
                }

                if (txtmobilenumber2.getText().toString().length() == 0) {
                    txtmobilenumber2.setError("Email-id is required!");
                    flag = 1;
                }


                if (texthostelname.getText().toString().length() == 0) {
                    texthostelname.setError("Hostel name is required!");
                    flag = 1;
                }

                if (texthosteladdr.getText().toString().length() == 0) {
                    texthosteladdr.setError("Hostel address is required!");
                    flag = 1;
                }

                if (textprice.getText().toString().length() == 0) {
                    textprice.setError("Price is required!");
                    flag = 1;
                }

                if (textvacancy.getText().toString().length() == 0) {
                    textvacancy.setError("vacancy is required!");
                    flag = 1;
                }

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


                if (flag == 0) {

                    StringRequest postrequest = new StringRequest(Request.Method.POST, url + "?phone=" + smobilenumber1.trim() + "&secondaryphone=" + smobilenumber2.trim() + "&hostel_name=" + shostelname.trim() + "&category=" + stype.trim() + "&vacancy=" + svacancy.trim() + "&rate=" + sprice.trim() + "&address=" + saddress.trim() + "&city=" + saddress.trim() + "&type=" + sgender.trim() + "&facilities=" + togglestatus.trim(), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("*****", url + "?phone=" + smobilenumber1.trim() + "&secondaryphone=" + smobilenumber2.trim() + "&hostel_name=" + shostelname.trim() + "&category=" + sgender.trim() + "&vacancy=" + svacancy.trim() + "&rate=" + sprice.trim() + "&address=" + saddress.trim() + "&city=" + saddress.trim() + "&type=" + stype.trim() + "&facilities=" + togglestatus.trim());


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("***", error.toString());

                        }
                    });
                    queue.add(postrequest);
                    dialog1 = new ProgressDialog(Hostel_Registeration.this);
                    dialog1.setMessage("Hostel registered successfully");
                    dialog1.show();

                    Runnable progressRunnable = new Runnable() {

                        @Override
                        public void run() {
                            dialog1.cancel();
                        }
                    };

                    Handler pdCanceller = new Handler();
                    pdCanceller.postDelayed(progressRunnable, 2000);

                }

            }

        });



    }
}






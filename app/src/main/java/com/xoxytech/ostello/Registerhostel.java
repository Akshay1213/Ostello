package com.xoxytech.ostello;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class Registerhostel extends AppCompatActivity {
    Button submit;
    TextView name,addr,mobile,email,typetext,hostelname,hosteladdr,price,vacancy;
    EditText textname,textaddr,textmob,textmail,texthostelname,texthosteladdr,textprice,textvacancy;
    String s="";
    ListView list;
    Spinner type;
    RadioGroup   radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerhostel);
        submit=(Button)findViewById(R.id.button1);
        name=(TextView)findViewById(R.id.textView);
        typetext=(TextView)findViewById(R.id.type);
        addr=(TextView)findViewById(R.id.addresstext);
        mobile=(TextView)findViewById(R.id.textView3);
        email=(TextView)findViewById(R.id.textView4);
        textname=(EditText) findViewById(R.id.editText);
        textaddr=(EditText) findViewById(R.id.address);
        textmob=(EditText) findViewById(R.id.mobile);
        textmail=(EditText) findViewById(R.id.email);
        hostelname=(TextView)findViewById(R.id.hostelname);
        hosteladdr=(TextView)findViewById(R.id.addrs);
        price=(TextView)findViewById(R.id.price);
        vacancy=(TextView)findViewById(R.id.textvacancy);
        texthostelname=(EditText) findViewById(R.id.editText4);
        texthosteladdr=(EditText) findViewById(R.id.addrsedit);
        textprice=(EditText)findViewById(R.id.priceedit);
        textvacancy=(EditText)findViewById(R.id.vacancyedit);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        name.clearFocus();

        // list=(ListView)findViewById(R.id.listview);
        //ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,type);
        //list.setAdapter(adapter);
        type=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                s=textname.getText().toString()+" ";
                s+=textaddr.getText().toString()+" ";
                s+=textmob.getText().toString()+" ";
                s+=textmail.getText().toString()+" ";
                s+=type.getSelectedItem().toString()+" ";
                s+= ((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString()+" ";
                s+=texthostelname.getText().toString()+" ";
                s+=texthosteladdr.getText().toString()+" ";
                s+=textprice.getText().toString()+" ";
                s+=textvacancy.getText().toString()+" ";
                if( textname.getText().toString().length() == 0 )
                    textname.setError( "First name is required!" );

                if( textaddr.getText().toString().length() == 0 )
                    textaddr.setError( "Address is required!" );

                if( textmob.getText().toString().length() == 0 )
                    textmob.setError( "Mobile number is required!" );

                if( textmail.getText().toString().length() == 0 )
                    textmail.setError( "Email-id is required!" );

                if( textmob.getText().toString().length() == 0 )
                    textmob.setError( "Mobile number is required!" );

                // if(type.getSelectedItem().toString().length() == 0)
                // type.setError( "Mobile number is required!" );

                if( texthostelname.getText().toString().length() == 0 )
                    texthostelname.setError( "Hostel name is required!" );

                if( texthosteladdr.getText().toString().length() == 0 )
                    texthosteladdr.setError( "Hostel address is required!" );

                if( textprice.getText().toString().length() == 0 )
                    textprice.setError( "Price is required!" );

                if( textvacancy.getText().toString().length() == 0 )
                    textvacancy.setError( "vacancy is required!" );

            }
        });



    }
}

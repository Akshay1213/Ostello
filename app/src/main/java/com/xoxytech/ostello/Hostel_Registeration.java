package com.xoxytech.ostello;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
    ImageView imageView1, imageView2, imageView3, imageView4, imageView5;
    // Button bntUpload;
    TextView messageText;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    String s, name1;


    Uri selectedImage;
    String upLoadServerUri = "http://www.ostallo.com/ostello/uploadimages.php";
    Bitmap yourSelectedImage;
    View.OnClickListener MyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");

            int id = 0;
            if (view == imageView1)
                id = 1;
            else if (view == imageView2)
                id = 2;
            if (view == imageView3)
                id = 3;
            else if (view == imageView4)
                id = 4;
            else if (view == imageView5)

                id = 5;

            startActivityForResult(photoPickerIntent, id);
        }
    };

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
        // addr=(TextView)findViewById(R.id.addresstext);
        mobilenumber1 = (TextView) findViewById(R.id.txtmobilenumber1);
        mobilenumber2 = (TextView) findViewById(R.id.txtmobilenumber2);
        textname = (EditText) findViewById(R.id.editTextusername);
        //textaddr=(EditText) findViewById(R.id.address);
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
        //name.clearFocus();
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
        imageView1 = (ImageView) findViewById(R.id.image1);
        imageView2 = (ImageView) findViewById(R.id.image2);
        imageView3 = (ImageView) findViewById(R.id.image3);
        imageView4 = (ImageView) findViewById(R.id.image4);
        imageView5 = (ImageView) findViewById(R.id.image5);
        //bntUpload= (Button) findViewById(R.id.btnUpload);
        // messageText=findViewById(messageText);


        imageView1.setOnClickListener(MyClickListener);
        imageView2.setOnClickListener(MyClickListener);
        imageView3.setOnClickListener(MyClickListener);
        imageView4.setOnClickListener(MyClickListener);
        imageView5.setOnClickListener(MyClickListener);
        // upLoadServerUri="public_html/ostello/images";
        //bntUpload.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //   public void onClick(View view) {
        //       new FileUpload().execute();

        //   }
        // });


        // list=(ListView)findViewById(R.id.listview);
        //ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,type);
        //list.setAdapter(adapter);
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
                //s+=textmob.getText().toString()+" ";
                //s+=textmail.getText().toString()+" ";
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

                //if( textaddr.getText().toString().length() == 0 )
                //    textaddr.setError( "Address is required!" );

                if (txtmobilenumber1.getText().toString().length() == 0) {
                    txtmobilenumber1.setError("Mobile number is required!");
                    flag = 1;
                }

                if (txtmobilenumber2.getText().toString().length() == 0) {
                    txtmobilenumber2.setError("Email-id is required!");
                    flag = 1;
                }

                //if( textmob.getText().toString().length() == 0 )
                //    textmob.setError( "Mobile number is required!" );

                // if(type.getSelectedItem().toString().length() == 0)
                // type.setError( "Mobile number is required!" );

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


                    //Toast.makeText(Hostel_Registeration.this,togglestatus,Toast.LENGTH_LONG).show();

                    StringRequest postrequest = new StringRequest(Request.Method.POST, url + "?phone=" + smobilenumber1.trim() + "&secondaryphone=" + smobilenumber2.trim() + "&hostel_name=" + shostelname.trim() + "&category=" + sgender.trim() + "&vacancy=" + svacancy.trim() + "&rate=" + sprice.trim() + "&address=" + saddress.trim() + "&city=" + saddress.trim() + "&type=" + stype.trim() + "&facilities=" + togglestatus.trim(), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //smobilenumber1="9764200290";
                            Log.d("*****", url + "?phone=" + smobilenumber1.trim() + "&secondaryphone=" + smobilenumber2.trim() + "&hostel_name=" + shostelname.trim() + "&category=" + sgender.trim() + "&vacancy=" + svacancy.trim() + "&rate=" + sprice.trim() + "&address=" + saddress.trim() + "&city=" + saddress.trim() + "&type=" + stype.trim() + "&facilities=" + togglestatus.trim());


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("***", error.toString());

                        }
                    });
                    queue.add(postrequest);
                    new FileUpload().execute();

                }
            }

        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (resultCode == RESULT_OK) {
            selectedImage = imageReturnedIntent.getData();

            s = getRealPathFromURI(selectedImage);
            // Log.d("*****",""+s);
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);

            ImageView imageView1 = (ImageView) findViewById(R.id.image1);
            switch (requestCode) {
                case 1:
                    imageView1 = (ImageView) findViewById(R.id.image1);
                    break;
                case 2:
                    imageView1 = (ImageView) findViewById(R.id.image2);
                    break;
                case 3:
                    imageView1 = (ImageView) findViewById(R.id.image3);
                    break;
                case 4:
                    imageView1 = (ImageView) findViewById(R.id.image4);
                    break;
                case 5:
                    imageView1 = (ImageView) findViewById(R.id.image5);
                    break;
            }


            imageView1.setImageBitmap(yourSelectedImage);
            name1 = s.substring(s.lastIndexOf("/") + 1);
            Toast.makeText(getApplicationContext(), "" + name1, Toast.LENGTH_LONG).show();
            //uploadFile(s);

            // Log.d("46551","Url:"+yourSelectedImage);
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
// can post image
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri,
                proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }

    public class FileUpload extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Hostel_Registeration.this);
            dialog.setMessage("Uploading started");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            String fileName = s;

            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(s);

            if (!sourceFile.isFile()) {



                Log.e("uploadFile", "Source File not exist :"
                        + s + "" + name);


                return null;

            } else {
                try {

                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(sourceFile);
                    URL url = new URL(upLoadServerUri);

                    // Open a HTTP  connection to  the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("uploaded_file", fileName);

                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=" + "uploaded_file" + ";filename="
                            + fileName + "" + lineEnd);

                    dos.writeBytes(lineEnd);

                    // create a buffer of  maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    }

                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    // Responses from the server (code and message)

                    String serverResponseMessage = conn.getResponseMessage();

                    Log.i("uploadFile", "HTTP Response is : "
                            + serverResponseMessage + ": " + conn.getResponseCode());

                    if (conn.getResponseCode() == 200) {

                        runOnUiThread(new Runnable() {
                            public void run() {

                                String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                        + " http://www.androidexample.com/media/uploads/"
                                        + name;


                                Toast.makeText(Hostel_Registeration.this, "File Upload Complete.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    //close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                } catch (MalformedURLException ex) {


                    ex.printStackTrace();

                    runOnUiThread(new Runnable() {
                        public void run() {

                            Toast.makeText(Hostel_Registeration.this, "MalformedURLException",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                } catch (Exception e) {


                    e.printStackTrace();

                    runOnUiThread(new Runnable() {
                        public void run() {

                            Toast.makeText(Hostel_Registeration.this, "Got Exception : see logcat ",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.e("severexception", "Exception : "
                            + e.getMessage(), e);
                }

                return null;

            } // End else block
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.setMessage("Upload complete");
            dialog.show();
            dialog.dismiss();

        }
    }
}






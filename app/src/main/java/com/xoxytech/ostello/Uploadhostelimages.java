package com.xoxytech.ostello;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Uploadhostelimages extends AppCompatActivity {
    ImageView imageView1, imageView2, imageView3, imageView4, imageView5;
    Button bntUpload;
    TextView messageText;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    ProgressDialog dialog1;
    String s, name1;

    Uri selectedImage;
    String upLoadServerUri = "http://www.ostallo.com/ostello/uploadimages.php";
    Bitmap yourSelectedImage;
    View.OnClickListener MyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");

          /*  Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);
                imageView1.setAnimation(animation);

            imageView2.setAnimation(animation);
            imageView3.setAnimation(animation);
            imageView4.setAnimation(animation);
            imageView5.setAnimation(animation);*/
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
        imageView1 = (ImageView) findViewById(R.id.image1);
        imageView2 = (ImageView) findViewById(R.id.image2);
        imageView3 = (ImageView) findViewById(R.id.image3);
        imageView4 = (ImageView) findViewById(R.id.image4);
        imageView5 = (ImageView) findViewById(R.id.image5);
        bntUpload = (Button) findViewById(R.id.btnUpload);
        // messageText=findViewById(messageText);


        imageView1.setOnClickListener(MyClickListener);
        imageView2.setOnClickListener(MyClickListener);
        imageView3.setOnClickListener(MyClickListener);
        imageView4.setOnClickListener(MyClickListener);
        imageView5.setOnClickListener(MyClickListener);
        // upLoadServerUri="public_html/ostello/images";
        bntUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FileUpload().execute();

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

            dialog = new ProgressDialog(Uploadhostelimages.this);
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
                        + s + "" + name1);

                return null;

            } else {
                try {

                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(sourceFile);
                    SharedPreferences sp = getSharedPreferences("YourSharedPreference", Activity.MODE_PRIVATE);
                    String phone = sp.getString("USER_PHONE", null);
                    URL url = new URL(upLoadServerUri + "?phone=" + phone);

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
                                        + name1;


                                Toast.makeText(Uploadhostelimages.this, "File Upload Complete.",
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

                            Toast.makeText(Uploadhostelimages.this, "MalformedURLException",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                } catch (Exception e) {


                    e.printStackTrace();

                    runOnUiThread(new Runnable() {
                        public void run() {

                            Toast.makeText(Uploadhostelimages.this, "Got Exception : see logcat ",
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
            dialog1 = new ProgressDialog(Uploadhostelimages.this);
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
            dialog.setMessage("Upload complete");
            dialog.show();
            dialog.dismiss();

        }
    }
}
package com.falconnect.dealermanagementsystem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;
import com.github.mikephil.charting.utils.FileUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import android.util.Base64;
import android.widget.Toast;

import static com.falconnect.dealermanagementsystem.R.layout.spinner_single_item;


public class Contact_Document extends AppCompatActivity {

    ActionBar actionBar;
    static final int DATE_PICKER_ID = 999;
    ImageView back;
    EditText names;
    TextView datesss;
    TextView uploadButton;
    Button save;
    Spinner ids;
    TextView selection_id;
    private int PICK_IMAGE_REQUEST = 1;
    HashMap<String, String> user;
    SessionManager session;
    String selected_contact;
    String Contactid;
    int position_contact;
    ArrayAdapter<String> spinnercontactArrayAdapter;
    public ArrayList<HashMap<String, String>> tab_contact_listview;
    private static final int REQUEST_PICK_FILE = 1;
    private File selectedFile;
    List<NameValuePair> params;
    public  static  String attachedFile;
    String selectedItemText;
    ProgressDialog barProgressDialog;
    HashMap<String, String> tabcontactlistview;
    ArrayList<String> list_new_id_contact = new ArrayList<String>();
    ArrayList<String> list_new = new ArrayList<String>();
    ArrayList<String> list_sample = new ArrayList<String>();
    private int year;
    public static String encodedImage;
    private int month;
    private int date;
    String gallery;
    HashMap<String, String> resultmap;
    public ArrayList<HashMap<String, String>> result_hashmap;
    boolean clicked = false;
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDate) {
            year = selectedYear;
            month = selectedMonth;
            date = selectedDate;
            datesss.setText(new StringBuilder().append(year).append("/").append(month + 1).append("/").append(date));

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact__document);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        actionBar = getSupportActionBar();
        actionBar.hide();

        initialize();

        Contactid = getIntent().getStringExtra("Contactid");

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> photo_list = new ArrayList<>();
                photo_list.add("Gallery");
                photo_list.add("File Manager");
                final AlertDialog.Builder builder = new AlertDialog.Builder(Contact_Document.this);
                final ArrayAdapter<String> aa1 = new ArrayAdapter<String>(Contact_Document.this, R.layout.sort_single_item, R.id.list, photo_list);
                builder.setSingleChoiceItems(aa1, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (photo_list.get(item) == "Gallery") {
                            clicked = true;
                            attachedFile = null;
                            gallery="0";
                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, PICK_IMAGE_REQUEST);
                            dialog.dismiss();

                        } else if (photo_list.get(item) == "File Manager") {
                            clicked = true;
                            gallery="1";
                            Intent intent = new Intent(Contact_Document.this, FilePicker.class);
                            startActivityForResult(intent, REQUEST_PICK_FILE);

                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });

        session = new SessionManager(Contact_Document.this);
        user = session.getUserDetails();

        final Calendar c = Calendar.getInstance();
        date = c.get(Calendar.DATE);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);

        datesss.setText(new StringBuilder().append(year).append("/").append(month + 1).append("/").append(date));

        datesss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_ID);
            }
        });

        new document_proof().execute();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new update_profile().execute();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                return new DatePickerDialog(Contact_Document.this, pickerListener, year, month, date);
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case REQUEST_PICK_FILE:

                    if (gallery == "0") {
                        if (clicked == true) {

                            clicked = false;

                            Uri selectedImage = data.getData();

                            String[] filePathColumn = {MediaStore.Images.Media.DATA};
                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor == null) {

                            } else {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                cursor.close();
                                uploadButton.setText(picturePath.toString());
                                Bitmap bitmap = null;
                                try {
                                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                //convert(bitmap);
                                scaleDown(bitmap, 400, true);
                            }

                        }
                    }
                    else {
                        if (data.hasExtra(FilePicker.EXTRA_FILE_PATH)) {

                            selectedFile = new File(data.getStringExtra(FilePicker.EXTRA_FILE_PATH));
                            uploadButton.setText(selectedFile.getName());
                            InputStream inputStream = null;
                            String encodedFile = "", lastVal;
                            try {
                                inputStream = new FileInputStream(selectedFile.getAbsolutePath());

                                byte[] buffer = new byte[10240];
                                int bytesRead;
                                ByteArrayOutputStream output = new ByteArrayOutputStream();
                                Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

                                while ((bytesRead = inputStream.read(buffer)) != -1) {
                                    output64.write(buffer, 0, bytesRead);
                                }
                                output64.close();
                                encodedFile = output.toString();
                            } catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            attachedFile = encodedFile;
                            Log.e("attachedFile", attachedFile);


                        }
                    }
                    break;

            }
        }
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);

        convert(newBitmap);

        return newBitmap;
    }

    public static String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 4, outputStream);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 4, outputStream);
        encodedImage = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

        attachedFile = encodedImage.toString();

        return encodedImage;
    }

    private class document_proof extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            ServiceHandler sh = new ServiceHandler();

            String queriesurl = Constant.MY_USER_TAB_VIEW + "session_user_id=" + user.get("user_id");

            Log.e("queriesurl", queriesurl);

            String json = sh.makeServiceCall(queriesurl, ServiceHandler.POST);

            if (json != null) {

                tab_contact_listview = new ArrayList<>();

                list_new.add("Select Document ID");

                try {
                    JSONObject jsonObj = new JSONObject(json);

                    JSONArray loan = jsonObj.getJSONArray("document_proof");

                    for (int k = 0; k <= loan.length(); k++) {

                        String doc_id = loan.getJSONObject(k).getString("doc_id");
                        String doc_id_name = loan.getJSONObject(k).getString("doc_id_name");


                        tabcontactlistview = new HashMap<>();

                        tabcontactlistview.put("doc_id", doc_id);
                        tabcontactlistview.put("doc_id_name", doc_id_name);

                        tab_contact_listview.add(tabcontactlistview);

                        list_new.add(doc_id_name.toString());

                        list_sample.add(doc_id_name.toString());

                        list_new_id_contact.add(doc_id.toString());

                    }

                } catch (final JSONException e) {

                }

            } else {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);



            spinnercontactArrayAdapter = new ArrayAdapter<String>(Contact_Document.this, spinner_single_item, list_new) {
                @Override
                public boolean isEnabled(int position) {
                    return position != 0;
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };

            spinnercontactArrayAdapter.setDropDownViewResource(spinner_single_item);
            ids.setAdapter(spinnercontactArrayAdapter);

            ids.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedItemText = (String) parent.getItemAtPosition(position);
                    if (position > 0) {
                        Toast.makeText(getApplicationContext(), "Selected : " + position, Toast.LENGTH_SHORT).show();
                        selected_contact = selectedItemText;
                        position_contact = position;


                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });


        }

    }

    private class update_profile extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(Contact_Document.this, "Loading...", "Please Wait ...", true);

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();

            params = new ArrayList<NameValuePair>();

            String values_dates = datesss.getText().toString();
            String id_text = names.getText().toString();

            params.add(new BasicNameValuePair("document_image", attachedFile));

            String newqueriesurl = Constant.DOCUMENT
                    +"contact_id=" + Contactid
                    + "&session_user_id="+ user.get("user_id")
                    + "&document_dob=" + values_dates
                    + "&document_id_type=" + id_text
                    + "&document_id_number=" + position_contact;

            Log.e("Queriesurl", newqueriesurl);

            String json = sh.makeServiceCall(newqueriesurl, ServiceHandler.POST, params);

            Log.e("Queriesurl", json);

            if (json != null) {

                result_hashmap = new ArrayList<>();
                resultmap = new HashMap<String, String>();
                try {
                    JSONObject obj = new JSONObject(json);

                    for (int i = 0; i <= obj.length(); i++) {

                        String result = obj.getString("Result");
                        String message = obj.getString("message");

                        resultmap.put("REsult", result);
                        resultmap.put("Message", message);
                        result_hashmap.add(resultmap);

                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            barProgressDialog.dismiss();

            if (resultmap.get("REsult").equals("1")) {
                Log.e("Sdadas", "dasdas");
                Contact_Document.this.finish();
            } else {
                Log.e("Sdadas", "11111");
            }

        }

    }

    public void initialize() {
        names = (EditText) findViewById(R.id.Id_number);
        datesss = (TextView) findViewById(R.id.date_Values);
        uploadButton = (TextView) findViewById(R.id.uploadButton);
        selection_id = (TextView) findViewById(R.id.select_id);
        ids = (Spinner) findViewById(R.id.employee);

        back =(ImageView) findViewById(R.id.editemployee_back) ;

        save = (Button) findViewById(R.id.save_documents);
    }

}

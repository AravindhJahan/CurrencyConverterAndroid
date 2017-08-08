package com.falconnect.dealermanagementsystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.ManageFooterDataModel;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddEmployeeActivity extends Activity {

    public static final int PICK_CONTACT = 0;
    public static String encodedImage;
    public static String imagebase64;
    private static RecyclerView.Adapter adapter;
    private static RecyclerView addemployeerecycleview;
    private static ArrayList<ManageFooterDataModel> addemployeedata;
    public ArrayList<HashMap<String, String>> addemployee_list;
    String resultt, message;
    SessionManager sessionManager;
    HashMap<String, String> user;
    ArrayList<String> employee_list = new ArrayList<>();
    Spinner addemployee_spinner;
    ProgressDialog barProgressDialog;
    ArrayAdapter<String> spinneremployeeArrayAdapter;
    String selected_employee;
    String selected_employee_destination;
    int position_employee;
    Spinner destination_spinner, addemployee_desi;
    LinearLayout get_employee_contact;
    ImageView profile_image_employee;
    String selected_employee_gender;
    EditText addemployee_owner_name, addemployee_phone_number, addemployee_email;
    Button submit_addemployee;
    List<NameValuePair> params;
    LinearLayout addemployee_back;
    HashMap<String, String> addemployeelist;
    private int PICK_IMAGE_REQUEST = 1;
    ArrayList<String> gender_list = new ArrayList<>();
    ArrayList<String> destination = new ArrayList<>();

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
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        encodedImage = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

        imagebase64 = encodedImage.toString();

        return encodedImage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_employee);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        employee_list = getIntent().getStringArrayListExtra("role_list");

        ///IDSSSS
        addemployee_spinner = (Spinner) findViewById(R.id.addemployee_spinner);
        get_employee_contact = (LinearLayout) findViewById(R.id.get_employee_contact);
        profile_image_employee = (ImageView) findViewById(R.id.profile_image_employee);
        addemployee_owner_name = (EditText) findViewById(R.id.addemployee_owner_name);
        addemployee_phone_number = (EditText) findViewById(R.id.addemployee_phone_number);
        addemployee_email = (EditText) findViewById(R.id.addemployee_email);
        submit_addemployee = (Button) findViewById(R.id.submit_addemployee);
        addemployee_back = (LinearLayout) findViewById(R.id.addemployee_back);
        destination_spinner = (Spinner) findViewById(R.id.destination_spinner);

        addemployee_desi = (Spinner) findViewById(R.id.addemployee_desi);

//        addemployeerecycleview = (RecyclerView) findViewById(R.id.addemployee_recyclerview);
//        addemployeerecycleview.setHasFixedSize(true);
//        addemployeerecycleview.setLayoutManager(new LinearLayoutManager(AddEmployeeActivity.this, LinearLayoutManager.HORIZONTAL, false));
       /* addemployeedata = new ArrayList<ManageFooterDataModel>();
        for (int i = 0; i < MyFooterManageData.managenameArray.length; i++) {

            addemployeedata.add(new ManageFooterDataModel(
                    MyFooterManageData.managenameArray[i],
                    MyFooterManageData.managedrawableArrayWhite41[i],
                    MyFooterManageData.manageid_[i]
            ));
        }
        adapter = new ManageFooterCustomAdapter(AddEmployeeActivity.this, addemployeedata);
        addemployeerecycleview.setAdapter(adapter);*/

        sessionManager = new SessionManager(AddEmployeeActivity.this);
        user = sessionManager.getUserDetails();

        addemployee_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager in = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(addemployee_email.getWindowToken(), 0);


                AddEmployeeActivity.this.finish();
            }
        });

        spinneremployeeArrayAdapter = new ArrayAdapter<String>(AddEmployeeActivity.this, R.layout.spinner_single_item, employee_list) {
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

        spinneremployeeArrayAdapter.setDropDownViewResource(R.layout.spinner_single_item);
        addemployee_spinner.setAdapter(spinneremployeeArrayAdapter);
        addemployee_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if (position > 0) {
                    // Toast.makeText(getApplicationContext(), "Selected : " + position, Toast.LENGTH_SHORT).show();
                    selected_employee = selectedItemText;
                    position_employee = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        get_employee_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        profile_image_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, PICK_IMAGE_REQUEST);
            }
        });

        gender_list.add("Select Gender");
        gender_list.add("Male");
        gender_list.add("Female");

        spinneremployeeArrayAdapter = new ArrayAdapter<String>(AddEmployeeActivity.this, R.layout.spinner_single_item, gender_list) {
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

        spinneremployeeArrayAdapter.setDropDownViewResource(R.layout.spinner_single_item);
        destination_spinner.setAdapter(spinneremployeeArrayAdapter);
        destination_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if (position > 0) {
                    // Toast.makeText(getApplicationContext(), "Selected : " + position, Toast.LENGTH_SHORT).show();
                    selected_employee_gender = selectedItemText;
                    //position_employee = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        destination.add("Select Designation");
        destination.add("Mr");
        destination.add("Mrs");
        destination.add("Miss");
        destination.add("Dr");


        spinneremployeeArrayAdapter = new ArrayAdapter<String>(AddEmployeeActivity.this, R.layout.spinner_single_item, destination) {
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

        spinneremployeeArrayAdapter.setDropDownViewResource(R.layout.spinner_single_item);
        addemployee_desi.setAdapter(spinneremployeeArrayAdapter);
        addemployee_desi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if (position > 0) {
                    // Toast.makeText(getApplicationContext(), "Selected : " + position, Toast.LENGTH_SHORT).show();
                    selected_employee_destination = selectedItemText;
                    //position_employee = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        submit_addemployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String namess = addemployee_owner_name.getText().toString();
                if (namess.equals("")) {
                    AlertDialog alertbox = new AlertDialog.Builder(AddEmployeeActivity.this)
                            .setMessage("All Fields are Required")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            })

                            .show();
                } else {
                    if (isNetworkAvailable()) {
                        new update_employee().execute();
                    } else {
                        Toast.makeText(AddEmployeeActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        AddEmployeeActivity.this.finish();
                    }
                }
            }
        });


    }

    // Check Internet Connection!!!
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if (requestCode == PICK_CONTACT && resultCode == RESULT_OK && null != data) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        Cursor c = null;
                        try {
                            c = getContentResolver().query(uri, new String[]{
                                            ContactsContract.CommonDataKinds.Phone.NUMBER,
                                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                                    null, null, null);

                            if (c != null && c.moveToFirst()) {
                                String number = c.getString(0);
                                String name = c.getString(1);

                                showSelectedNumber(number, name);
                            }
                        } finally {
                            if (c != null) {
                                c.close();
                            }
                        }
                    }
                }
                break;

            case 1:
                if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    Glide.with(getApplicationContext())
                            .load(selectedImage)
                            .asBitmap()
                            .transform(new RoundImageTransform(AddEmployeeActivity.this))
                            .into(profile_image_employee);

                    Bitmap bitmap = null;

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    scaleDown(bitmap, 200, true);
                } else {

                }
                break;
        }
    }

    public void showSelectedNumber(String number, String name) {

        number = number.replace("(", "");
        number = number.replace(")", "");
        number = number.replace("-", "");
        number = number.replace(" ", "");
        number = number.replace("+91", "");

        addemployee_owner_name.setText(name);
        addemployee_phone_number.setText(number);

        //  Toast.makeText(this, number + name, Toast.LENGTH_LONG).show();
    }

    private class update_employee extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(AddEmployeeActivity.this, "Loading...", "Please Wait ...", true);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();

            params = new ArrayList<NameValuePair>();

            String newemployee_name = addemployee_owner_name.getText().toString().trim();
            newemployee_name = newemployee_name.replace(" ", "%20");
            String newemployee_number = addemployee_phone_number.getText().toString().trim();
            newemployee_number = newemployee_number.replace(" ", "");
            String newemployee_email = addemployee_email.getText().toString().trim();
            newemployee_email = newemployee_email.replace(" ", "%20");

            if (encodedImage != null) {
                imagebase64 = encodedImage;
            } else {
                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);
                scaleDown(largeIcon, 200, true);
                imagebase64 = encodedImage;
            }
            params.add(new BasicNameValuePair("employee_image", imagebase64));

            String newqueriesurl = Constant.ADD_EMPLOYEE +
                    "session_user_id=" + user.get("user_id")
                    + "&employee_type=" + position_employee
                    + "&employee_designation=" + selected_employee_destination
                    + "&employee_gender=" + selected_employee_gender
                    + "&employee_name=" + newemployee_name
                    + "&employee_mobile=" + newemployee_number
                    + "&employee_email=" + newemployee_email
                    + "&page_name=addemployee";

            Log.e("Queriesurl", newqueriesurl);

            String json = sh.makeServiceCall(newqueriesurl, ServiceHandler.POST, params);
            if (json != null) {
                addemployee_list = new ArrayList<>();
                addemployeelist = new HashMap<>();
                try {
                    JSONObject obj = new JSONObject(json);
                    for (int k = 0; k <= obj.length(); k++) {
                        resultt = obj.getString("Result");
                        message = obj.getString("message");
                        addemployeelist.put("REsult", resultt);
                        addemployeelist.put("Message", message);
                        addemployee_list.add(addemployeelist);
                    }

                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            barProgressDialog.dismiss();
            if (addemployeelist.get("REsult").equals("0")) {
                final AlertDialog alertbox = new AlertDialog.Builder(AddEmployeeActivity.this)
                        .setTitle("Error")
                        .setMessage(addemployeelist.get("message"))
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        })
                        .show();

            } else if (addemployeelist.get("REsult").equals("1")) {
                AddEmployeeActivity.this.finish();
                // Toast.makeText(AddEmployeeActivity.this, addemployeelist.get("Message"), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

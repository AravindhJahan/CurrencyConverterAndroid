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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditEmployeeActivity extends Activity {

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
    String image;
    ArrayList<String> employee_list = new ArrayList<>();
    ArrayList<String> role_list = new ArrayList<>();
    Spinner editemployee_spinner;
    ArrayAdapter<String> spinneremployeeArrayAdapter;
    String selected_employee;
    TextView edit_emply_type;
    int position_employee,position_employee_desi;
    LinearLayout get_employee_contact;
    ImageView profile_image_employee;
    EditText editemployee_owner_name, editemployee_phone_number, editemployee_email;
    Button submit_addemployee;
    List<NameValuePair> params;
    ProgressDialog barProgressDialog;
    HashMap<String, String> addemployeelist;
    ImageView editemployee_back;
    private int PICK_IMAGE_REQUEST = 1;

    String selected_employee_destination, selected_employee_gender;

    Spinner employee_desi, employee_gender;

    TextView employee_desi_text, employee_gender_text;

    ArrayList<String> destination = new ArrayList<>();
    ArrayList<String> gender = new ArrayList<>();

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

        setContentView(R.layout.activity_edit_employee);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        employee_list = getIntent().getStringArrayListExtra("employee_list");
        role_list = getIntent().getStringArrayListExtra("role_list");

        ///IDSSSS
        edit_emply_type = (TextView) findViewById(R.id.edit_emply_type);
        editemployee_spinner = (Spinner) findViewById(R.id.editemployee_spinner);
        get_employee_contact = (LinearLayout) findViewById(R.id.get_employee_contact);
        profile_image_employee = (ImageView) findViewById(R.id.profile_image_employee);
        editemployee_owner_name = (EditText) findViewById(R.id.editemployee_owner_name);
        editemployee_phone_number = (EditText) findViewById(R.id.editemployee_phone_number);
        editemployee_email = (EditText) findViewById(R.id.editemployee_email);
        submit_addemployee = (Button) findViewById(R.id.submit_addemployee);

        editemployee_back = (ImageView) findViewById(R.id.editemployee_back);

        employee_desi_text = (TextView) findViewById(R.id.employee_desi_text);
        employee_gender_text = (TextView) findViewById(R.id.employee_gender_text);

        employee_desi =  (Spinner) findViewById(R.id.employee_desi);
        employee_gender = (Spinner) findViewById(R.id.employee_gender);

        addemployeerecycleview = (RecyclerView) findViewById(R.id.editemployee_recyclerview);
        addemployeerecycleview.setHasFixedSize(true);
        addemployeerecycleview.setLayoutManager(new LinearLayoutManager(EditEmployeeActivity.this, LinearLayoutManager.HORIZONTAL, false));
       /* addemployeedata = new ArrayList<ManageFooterDataModel>();
        for (int i = 0; i < MyFooterManageData.managenameArray.length; i++) {

            addemployeedata.add(new ManageFooterDataModel(
                    MyFooterManageData.managenameArray[i],
                    MyFooterManageData.managedrawableArrayWhite41[i],
                    MyFooterManageData.manageid_[i]
            ));
        }
        adapter = new ManageFooterCustomAdapter(EditEmployeeActivity.this, addemployeedata);
        addemployeerecycleview.setAdapter(adapter);*/

        sessionManager = new SessionManager(EditEmployeeActivity.this);
        user = sessionManager.getUserDetails();


        image = employee_list.get(0);

        Glide.with(getApplicationContext())
                .load(employee_list.get(0))
                .transform(new RoundImageTransform(EditEmployeeActivity.this))
                .into(profile_image_employee);

        editemployee_owner_name.setText(employee_list.get(1));
        editemployee_phone_number.setText(employee_list.get(2));
        editemployee_email.setText(employee_list.get(3));

        get_employee_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        editemployee_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager in = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(editemployee_email.getWindowToken(), 0);

                EditEmployeeActivity.this.finish();
            }
        });

        profile_image_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, PICK_IMAGE_REQUEST);
            }
        });


        position_employee = Integer.parseInt(employee_list.get(5).toString());
        selected_employee_destination = employee_list.get(7).toString();
        selected_employee_gender = employee_list.get(6).toString();

        int list5values = Integer.parseInt(employee_list.get(5).toString());

        if (list5values == position_employee) {
            Log.e("valussss", "values");
            edit_emply_type.setText(role_list.get(position_employee));
        } else {
            edit_emply_type.setText("Wrong Datas");
        }

        if(employee_list.get(7).toString() != null) {
            employee_desi_text.setText(employee_list.get(7).toString());
            employee_desi_text.setVisibility(View.VISIBLE);
            employee_desi.setVisibility(View.GONE);
        }else{
            employee_desi_text.setVisibility(View.GONE);
            employee_desi.setVisibility(View.VISIBLE);
        }


        if(employee_list.get(6).toString() != null) {
            employee_gender_text.setText(employee_list.get(6).toString());
            employee_gender_text.setVisibility(View.VISIBLE);
            employee_gender.setVisibility(View.GONE);
        }else{
            employee_gender_text.setVisibility(View.GONE);
            employee_gender.setVisibility(View.VISIBLE);
        }

        employee_gender_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                employee_gender_text.setVisibility(View.GONE);
                employee_gender.setVisibility(View.VISIBLE);
            }
        });


        employee_desi_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                employee_desi_text.setVisibility(View.GONE);
                employee_desi.setVisibility(View.VISIBLE);
            }
        });


        edit_emply_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_emply_type.setVisibility(View.GONE);
                editemployee_spinner.setVisibility(View.VISIBLE);
            }
        });

        destination.add("Select Designation");
        destination.add("Mr");
        destination.add("Mrs");
        destination.add("Miss");
        destination.add("Dr"); 

        spinneremployeeArrayAdapter = new ArrayAdapter<String>(EditEmployeeActivity.this, R.layout.spinner_single_item, destination) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinneremployeeArrayAdapter.setDropDownViewResource(R.layout.spinner_single_item);
        employee_desi.setAdapter(spinneremployeeArrayAdapter);
        employee_desi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                //Toast.makeText(getApplicationContext(), "Selected : " + position, Toast.LENGTH_SHORT).show();
                selected_employee_destination = selectedItemText;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        gender.add("Select Gender");
        gender.add("Male");
        gender.add("Female");

        spinneremployeeArrayAdapter = new ArrayAdapter<String>(EditEmployeeActivity.this, R.layout.spinner_single_item, gender) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinneremployeeArrayAdapter.setDropDownViewResource(R.layout.spinner_single_item);
        employee_gender.setAdapter(spinneremployeeArrayAdapter);
        employee_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "Selected : " + position, Toast.LENGTH_SHORT).show();
                selected_employee_gender = selectedItemText;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        spinneremployeeArrayAdapter = new ArrayAdapter<String>(EditEmployeeActivity.this, R.layout.spinner_single_item, role_list) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinneremployeeArrayAdapter.setDropDownViewResource(R.layout.spinner_single_item);
        editemployee_spinner.setAdapter(spinneremployeeArrayAdapter);
        editemployee_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                //Toast.makeText(getApplicationContext(), "Selected : " + position, Toast.LENGTH_SHORT).show();
                selected_employee = selectedItemText;
                position_employee = position + 1;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        submit_addemployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new update_employee().execute();
            }
        });

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
                            .transform(new RoundImageTransform(EditEmployeeActivity.this))
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

        editemployee_owner_name.setText(name);
        editemployee_phone_number.setText(number);

        // Toast.makeText(this, number + name, Toast.LENGTH_LONG).show();
    }

    private class update_employee extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(EditEmployeeActivity.this, "Loading...", "Please Wait ...", true);

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            params = new ArrayList<NameValuePair>();

            if (encodedImage != null) {
                imagebase64 = encodedImage;
            } else {
                URL url = null;
                Bitmap bmp = null;
                try {
                    url = new URL(image.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                scaleDown(bmp, 200, true);
                imagebase64 = encodedImage;
            }
            params.add(new BasicNameValuePair("employee_image", imagebase64));

            String newemployee_name = editemployee_owner_name.getText().toString().trim();
            newemployee_name = newemployee_name.replace(" ", "%20");
            String newemployee_number = editemployee_phone_number.getText().toString().trim();
            newemployee_number = newemployee_number.replace(" ", "");
            String newemployee_email = editemployee_email.getText().toString().trim();
            newemployee_email = newemployee_email.replace(" ", "%20");

            selected_employee_destination = selected_employee_destination.replace(" ", "%20");

            String newqueriesurl = Constant.EDIT_EMPLOYEE +
                    "session_user_id=" + user.get("user_id") +
                    "&employee_type=" + position_employee +
                    "&employee_name=" + newemployee_name +
                    "&employee_mobile=" + newemployee_number +
                    "&employee_email=" + newemployee_email +
                    "&employee_designation=" + selected_employee_destination +
                    "&employee_gender=" + selected_employee_gender +
                    "&employee_id=" + employee_list.get(4) +
                    "&page_name=editemployee";

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
                            //Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
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
                final AlertDialog alertbox = new AlertDialog.Builder(EditEmployeeActivity.this)
                        .setTitle("Error")
                        .setMessage(addemployeelist.get("message"))
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        })
                        .show();
            } else if (addemployeelist.get("REsult").equals("1")) {
                EditEmployeeActivity.this.finish();
                // Toast.makeText(EditEmployeeActivity.this, addemployeelist.get("Message"), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

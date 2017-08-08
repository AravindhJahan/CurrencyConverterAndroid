package com.falconnect.dealermanagementsystem;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.falconnect.dealermanagementsystem.FontAdapter.RoundImageTransform;
import com.falconnect.dealermanagementsystem.Model.ManageFooterDataModel;
import com.falconnect.dealermanagementsystem.SharedPreference.AddressSavedSharedPreferences;
import com.falconnect.dealermanagementsystem.SharedPreference.SavedDetailsContact;
import com.falconnect.dealermanagementsystem.SharedPreference.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.falconnect.dealermanagementsystem.R.layout.spinner_single_item;

public class AddContactActivity extends Activity {

    public static final int PICK_CONTACT = 0;
    public static String encodedImage;
    public static String imagebase64;
    private static RecyclerView.Adapter adapter;
    private static RecyclerView addcontactrecycleview;
    private static ArrayList<ManageFooterDataModel> addcontactdata;
    public ArrayList<HashMap<String, String>> addcontact_list;
    ArrayAdapter<String> spinnercontactArrayAdapter;
    ArrayList<String> contact_list = new ArrayList<>();
    String selected_contact;
    int position_contact;
    LinearLayout getcontact;
    Spinner addcontact_spinner;
    EditText addcontact_name, addcontact_phone_number, addcontact_email, addcontact_pan_number;
    EditText addcontact_address;
    ImageView imageView;
    ImageView map;
    String selectedItemText;
    List<NameValuePair> params;
    HashMap<String, String> addcontactlist;
    String result, message, Contactid;
    SessionManager sessionManager;
    HashMap<String, String> user;
    Button submit_addcontact;
    ImageView addcontact_back;
    EditText addcontact_owner_name;
    ProgressDialog barProgressDialog;
    String image, main_name, name, number, email, pan_number;
    String picturePath;
    SavedDetailsContact savedDetailsContact;
    HashMap<String, String> user_contact;
    HashMap<String, String> user_contact_map;
    TextView spinner;
    AddressSavedSharedPreferences addressSavedSharedPreferences;
    private int PICK_IMAGE_REQUEST = 1;
    ArrayList<String> gender_list = new ArrayList<>();
    ArrayAdapter<String> spinneremployeeArrayAdapter;
    Spinner destination_spinner;
    String selected_employee_gender;
    int sms_toggle = 1;
    int email_toggle = 1;
    ImageView sms_img;
    ImageView email_img;
    RelativeLayout sms_layout, email_layout;
    Button next_addcontact, documents_addcontact;
    String s = "ABCDE1234F"; // get your editext value here
    Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");

    public static String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        encodedImage = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

        imagebase64 = encodedImage.toString();

        return encodedImage;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_contact);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();

        contact_list = intent.getStringArrayListExtra("contact_list");

        Log.e("contact_list", contact_list.toString());

//        addcontactrecycleview = (RecyclerView) findViewById(R.id.addcontact_recyclerview);
//        addcontactrecycleview.setHasFixedSize(true);
//        addcontactrecycleview.setLayoutManager(new LinearLayoutManager(AddContactActivity.this, LinearLayoutManager.HORIZONTAL, false));
        /*addcontactdata = new ArrayList<ManageFooterDataModel>();
        for (int i = 0; i < MyFooterManageData.managenameArray.length; i++) {

            addcontactdata.add(new ManageFooterDataModel(
                    MyFooterManageData.managenameArray[i],
                    MyFooterManageData.managedrawableArrayWhite3[i],
                    MyFooterManageData.manageid_[i]
            ));
        }
        adapter = new ManageFooterCustomAdapter(AddContactActivity.this, addcontactdata);
        addcontactrecycleview.setAdapter(adapter);*/


        gender_list.add("Select Gender");
        gender_list.add("Male");
        gender_list.add("Female");

        spinneremployeeArrayAdapter = new ArrayAdapter<String>(AddContactActivity.this, R.layout.spinner_single_item, gender_list) {
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


        sessionManager = new SessionManager(AddContactActivity.this);
        user = sessionManager.getUserDetails();

        savedDetailsContact = new SavedDetailsContact(AddContactActivity.this);
        addressSavedSharedPreferences = new AddressSavedSharedPreferences(this);

        //addcontact_spinner
        spinner = (TextView) findViewById(R.id.spinner);
        addcontact_back = (ImageView) findViewById(R.id.addcontact_back);
        addcontact_spinner = (Spinner) findViewById(R.id.addcontact_spinner);
        getcontact = (LinearLayout) findViewById(R.id.getcontact);
        addcontact_name = (EditText) findViewById(R.id.addcontact_name);
        addcontact_phone_number = (EditText) findViewById(R.id.addcontact_phone_number);
        imageView = (ImageView) findViewById(R.id.profile_image_contact);
        addcontact_owner_name = (EditText) findViewById(R.id.addcontact_owner_name);
        addcontact_email = (EditText) findViewById(R.id.addcontact_email);
        addcontact_address = (EditText) findViewById(R.id.addcontact_address);
        map = (ImageView) findViewById(R.id.map_buttons);
        addcontact_pan_number = (EditText) findViewById(R.id.addcontact_pan_number);
        next_addcontact = (Button) findViewById(R.id.next_addcontact);
       //documents_addcontact = (Button) findViewById(R.id.documents);
        submit_addcontact = (Button) findViewById(R.id.submit_addcontact);
        destination_spinner = (Spinner) findViewById(R.id.gender_spinner);
        sms_layout = (RelativeLayout) findViewById(R.id.sms_toogle);
        email_layout = (RelativeLayout) findViewById(R.id.email_toggle);
        sms_img = (ImageView) findViewById(R.id.sms_toggle_img);
        email_img = (ImageView) findViewById(R.id.email_toggle_img);

        addcontact_owner_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT ) {
                    // handle next button
                    return true;
                }
                return false;
            }
        });


        addcontact_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT ) {
                    // handle next button
                    return true;
                }
                return false;
            }
        });

        spinnercontactArrayAdapter = new ArrayAdapter<String>(AddContactActivity.this, spinner_single_item, contact_list) {
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
        addcontact_spinner.setAdapter(spinnercontactArrayAdapter);

        addcontact_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItemText = (String) parent.getItemAtPosition(position);
                if (position > 0) {
                    Toast.makeText(getApplicationContext(), "Selected : " + position, Toast.LENGTH_SHORT).show();
                    selected_contact = selectedItemText;
                    position_contact = position;
                    spinner.setText(selected_contact);
                    savedDetailsContact.createLoginSession(selected_contact,
                            String.valueOf(position_contact),
                            null, null, null, null, user_contact.get("contact_image"), null);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


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

        addcontact_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                savedDetailsContact.clear_contact_datas();
                addressSavedSharedPreferences.clear_address();
                AddContactActivity.this.finish();

            }
        });


        sms_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sms_toggle == 0) {
                    sms_toggle = 1;
                    Glide.with(AddContactActivity.this).load(R.drawable.toggle_off).into(sms_img);
                } else {
                    Glide.with(AddContactActivity.this).load(R.drawable.toggle_on).into(sms_img);
                    sms_toggle = 0;
                }
            }
        });

        email_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email_toggle == 0) {
                    email_toggle = 1;

                    Glide.with(AddContactActivity.this).load(R.drawable.toggle_off).into(email_img);
                } else {
                    Glide.with(AddContactActivity.this).load(R.drawable.toggle_on).into(email_img);

                    email_toggle = 0;
                }
            }
        });

        getcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, PICK_IMAGE_REQUEST);
            }
        });


        user_contact = savedDetailsContact.getUserDetails();
        user_contact_map = addressSavedSharedPreferences.getAddress_details();

        if (user_contact_map.get("map_address") == null) {
            addcontact_address.setText("");
        } else {
            addcontact_address.setText(user_contact_map.get("map_address"));
        }

        if (user_contact.get("contact_type") == null) {
            addcontact_spinner.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
        } else {
            addcontact_spinner.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
            spinner.setText(user_contact.get("contact_type"));
        }

        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addcontact_spinner.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE);
            }
        });
        if (user_contact.get("contact_main_name") == null) {
            addcontact_owner_name.setText("");
        } else {
            addcontact_owner_name.setText(user_contact.get("contact_main_name"));
        }
        if (user_contact.get("contact_name") == null) {
            addcontact_name.setText("");
        } else {
            addcontact_name.setText(user_contact.get("contact_name"));
        }

        if (user_contact.get("contact_email") == null) {
            addcontact_email.setText("");
        } else {
            addcontact_email.setText(user_contact.get("contact_email"));
        }

        if (user_contact.get("contact_number") == null) {
            addcontact_phone_number.setText("");
        } else {
            addcontact_phone_number.setText(user_contact.get("contact_number"));
        }

        if (user_contact.get("contact_pan_number") == null) {
            addcontact_pan_number.setText("");
        } else {
            addcontact_pan_number.setText(user_contact.get("contact_pan_number"));
        }

        if (user_contact.get("contact_image") == null) {
            Glide.with(getApplicationContext())
                    .load(R.drawable.default_avatar)
                    .transform(new RoundImageTransform(AddContactActivity.this))
                    .into(imageView);

        } else {
            Glide.with(getApplicationContext())
                    .load(user_contact.get("contact_image"))
                    .transform(new RoundImageTransform(AddContactActivity.this))
                    .into(imageView);
        }

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user_contact = savedDetailsContact.getUserDetails();

                image = user_contact.get("contact_image");
                main_name = addcontact_owner_name.getText().toString();
                name = addcontact_name.getText().toString();
                number = addcontact_phone_number.getText().toString();
                email = addcontact_email.getText().toString();
                pan_number = addcontact_pan_number.getText().toString();


                savedDetailsContact.createLoginSession(
                        user_contact.get("contact_type"),
                        user_contact.get("contact_type_id"),
                        main_name,
                        name,
                        number,
                        email,
                        image,
                        pan_number);

                Intent inten = new Intent(AddContactActivity.this, LocationSelection.class);
                startActivity(inten);
            }
        });

        submit_addcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //user_contact = savedDetailsContact.getUserDetails();
                String errorMessage;

                Log.e("spinner",spinner.getText().toString());

                Log.e("owner_name",addcontact_owner_name.getText().toString());

                if (spinner.getText().toString() == "") {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(AddContactActivity.this)
                            .setMessage("Please Select contact")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    alertbox.show();
                } else if (addcontact_owner_name.getText().toString() == "") {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(AddContactActivity.this)
                            .setMessage("Please enter contact owner name")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    alertbox.show();
                } else if (addcontact_name.getText().toString() == "") {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(AddContactActivity.this)
                            .setMessage("Please enter contact name")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    alertbox.show();
                } else if (addcontact_email.getText().toString() == "") {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(AddContactActivity.this)
                            .setMessage("Please enter Email-Id")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                    alertbox.show();
                } else if (addcontact_phone_number.getText().toString() == "") {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(AddContactActivity.this)
                            .setMessage("Please enter Mobile number")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    alertbox.show();

                } else if (addcontact_phone_number.length() != 10) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(AddContactActivity.this)
                            .setMessage("Please enter Validated Mobile number")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    alertbox.show();
                } else if (addcontact_pan_number.getText().toString() == "") {

                    AlertDialog.Builder alertbox = new AlertDialog.Builder(AddContactActivity.this)
                            .setMessage("Please enter PAN Number")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    alertbox.show();
                } else if (addcontact_address.getText().toString() == "") {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(AddContactActivity.this)
                            .setMessage("Please enter address")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    alertbox.show();
                } else if (selected_employee_gender == "") {

                    AlertDialog.Builder alertbox = new AlertDialog.Builder(AddContactActivity.this)
                            .setMessage("Please enter Gender")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    alertbox.show();

                } else if (addcontact_pan_number != null) {
                    s = addcontact_pan_number.getText().toString();
                    Matcher matcher = pattern.matcher(s);
                    if (matcher.matches()) {
                        new update_contact().execute();
                    } else {
                        AlertDialog.Builder alertbox = new AlertDialog.Builder(AddContactActivity.this)
                                .setMessage("Invalid PAN Number")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        addcontact_pan_number.setHint("PAN NUMBER");
                                    }
                                });
                        alertbox.show();

                    }

                } else {
                    new update_contact().execute();
                }
            }
        });


        next_addcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //user_contact = savedDetailsContact.getUserDetails();

                String errorMessage;


                Log.e("spinner",spinner.getText().toString());

                if (spinner.getText().toString() == "") {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(AddContactActivity.this)
                            .setMessage("Please Select contact")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    alertbox.show();
                } else if (addcontact_owner_name.getText().toString() == "") {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(AddContactActivity.this)
                            .setMessage("Please enter contact owner name")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    alertbox.show();
                } else if (addcontact_name.getText().toString() == "") {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(AddContactActivity.this)
                            .setMessage("Please enter contact name")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    alertbox.show();
                } else if (addcontact_email.getText().toString() == "") {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(AddContactActivity.this)
                            .setMessage("Please enter Email-Id")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    alertbox.show();
                } else if (addcontact_phone_number.getText().toString() == "") {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(AddContactActivity.this)
                            .setMessage("Please enter Mobile number")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    alertbox.show();

                } else if (addcontact_phone_number.length() != 10) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(AddContactActivity.this)
                            .setMessage("Please enter Validated Mobile number")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    alertbox.show();
                } else if (addcontact_pan_number.getText().toString() == "") {

                    AlertDialog.Builder alertbox = new AlertDialog.Builder(AddContactActivity.this)
                            .setMessage("Please enter PAN Number")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    alertbox.show();
                } else if (addcontact_address.getText().toString() == "") {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(AddContactActivity.this)
                            .setMessage("Please enter address")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                    alertbox.show();
                } else if (selected_employee_gender == "") {

                    AlertDialog.Builder alertbox = new AlertDialog.Builder(AddContactActivity.this)
                            .setMessage("Please enter Gender")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                    alertbox.show();

                } else if (addcontact_pan_number != null) {
                    s = addcontact_pan_number.getText().toString();
                    Matcher matcher = pattern.matcher(s);
                    if (matcher.matches()) {
                        new update_contacts().execute();
                    } else {
                        AlertDialog.Builder alertbox = new AlertDialog.Builder(AddContactActivity.this)
                                .setMessage("Invalid PAN Number")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        addcontact_pan_number.setHint("PAN NUMBER");
                                    }
                                });
                        alertbox.show();


                    }

                } else {
                    new update_contacts().execute();
                }
            }
        });



    }

    @Override
    public void onBackPressed() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        savedDetailsContact.clear_contact_datas();
        addressSavedSharedPreferences.clear_address();
        AddContactActivity.this.finish();

    }

    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
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

                    Uri selectedImage1 = data.getData();

                    String[] filePathColumn1 = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage1, filePathColumn1, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn1[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    Glide.with(getApplicationContext())
                            .load(selectedImage1)
                            .asBitmap()
                            .into(imageView);

                    image = picturePath.toString();

                    user_contact = savedDetailsContact.getUserDetails();
                    main_name = addcontact_owner_name.getText().toString();
                    name = addcontact_name.getText().toString();
                    number = addcontact_phone_number.getText().toString();
                    email = addcontact_email.getText().toString();
                    pan_number = addcontact_pan_number.getText().toString();


                    savedDetailsContact.createLoginSession(
                            user_contact.get("contact_type"),
                            user_contact.get("contact_type_id"),
                            main_name,
                            name,
                            number,
                            email,
                            image,
                            pan_number);

                    Bitmap bitmap = null;

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage1);
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

        main_name = addcontact_owner_name.getText().toString();

        user_contact = savedDetailsContact.getUserDetails();
        savedDetailsContact.createLoginSession(selected_contact, String.valueOf(position_contact), main_name, name, number, null, user_contact.get("contact_image"), null);

        addcontact_name.setText(name);
        addcontact_phone_number.setText(number);

        //  Toast.makeText(this, number + name, Toast.LENGTH_LONG).show();
    }

    private class update_contacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(AddContactActivity.this, "Loading...", "Please Wait ...", true);

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            params = new ArrayList<NameValuePair>();

            String newaddress = addcontact_address.getText().toString();
            user_contact = savedDetailsContact.getUserDetails();
            String ownerename = addcontact_owner_name.getText().toString();
            String name = addcontact_name.getText().toString();
            String number = addcontact_phone_number.getText().toString();
            String email = addcontact_email.getText().toString();
            try {
                name = URLEncoder.encode(name, "UTF-8");
                newaddress = newaddress.replace(" ", "");
                newaddress = URLEncoder.encode(newaddress, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (encodedImage != null) {
                imagebase64 = encodedImage;
            } else {
                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);
                scaleDown(largeIcon, 200, true);
                imagebase64 = encodedImage;
            }
            params.add(new BasicNameValuePair("contact_image", imagebase64));

            String newqueriesurl = Constant.ADDCONTACT +
                    "session_user_id=" + user.get("user_id") +
                    "&contact_type=" + user_contact.get("contact_type_id") +
                    "&contactowner=" + ownerename +
                    "&contactname=" + name +
                    "&contactnumber=" + number +
                    "&contactmailid=" + email +
                    "&contactaddress=" + newaddress +
                    "&contactpannumber=" + s +
                    "&contact_gender=" + selected_employee_gender +
                    "&contact_sms=" + sms_toggle +
                    "&contact_email=" + email_toggle +
                    "&page_name=addcontact";

            Log.e("Queriesurl", newqueriesurl);

            String json = sh.makeServiceCall(newqueriesurl, ServiceHandler.POST, params);

            Log.e("json",json.toString());

            if (json != null) {
                addcontact_list = new ArrayList<>();
                addcontactlist = new HashMap<>();
                try {
                    JSONObject obj = new JSONObject(json);
                    for (int k = 0; k <= obj.length(); k++) {
                        result = obj.getString("Result");
                        message = obj.getString("message");
                        if (result.equals("1")) {
                            Contactid = obj.getString("Contactid");
                            addcontactlist.put("Result", result);
                            addcontactlist.put("message", message);
                            addcontactlist.put("Contactid", Contactid);
                            addcontact_list.add(addcontactlist);
                        } else {
                            addcontactlist.put("Result", result);
                            addcontactlist.put("message", message);
                            addcontact_list.add(addcontactlist);
                        }
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

            if (addcontactlist.get("Result").equals("0")) {
                final AlertDialog alertbox = new AlertDialog.Builder(AddContactActivity.this)
                        .setTitle("Error")
                        .setMessage(addcontactlist.get("message"))
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        })
                        .show();
            } else {
                AddContactActivity.this.finish();
                Intent intent = new Intent(AddContactActivity.this, AddContactLeadsActivity.class);
                intent.putExtra("Contactid", Contactid);
                intent.putExtra("add_contact","1");
                startActivity(intent);

                savedDetailsContact.clear_contact_datas();
                addressSavedSharedPreferences.clear_address();
                // Toast.makeText(AddContactActivity.this, addcontactlist.get("message"), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class update_contact extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(AddContactActivity.this, "Loading...", "Please Wait ...", true);

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();
            params = new ArrayList<NameValuePair>();

            String newaddress = addcontact_address.getText().toString();
            user_contact = savedDetailsContact.getUserDetails();
            String ownerename = addcontact_owner_name.getText().toString();
            String name = addcontact_name.getText().toString();
            String number = addcontact_phone_number.getText().toString();
            String email = addcontact_email.getText().toString();
            try {
                name = URLEncoder.encode(name, "UTF-8");
                newaddress = newaddress.replace(" ", "");
                newaddress = URLEncoder.encode(newaddress, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (encodedImage != null) {
                imagebase64 = encodedImage;
            } else {
                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);
                scaleDown(largeIcon, 200, true);
                imagebase64 = encodedImage;
            }
            params.add(new BasicNameValuePair("contact_image", imagebase64));

            String newqueriesurl = Constant.ADDCONTACT +
                    "session_user_id=" + user.get("user_id") +
                    "&contact_type=" + user_contact.get("contact_type_id") +
                    "&contactowner=" + ownerename +
                    "&contactname=" + name +
                    "&contactnumber=" + number +
                    "&contactmailid=" + email +
                    "&contactaddress=" + newaddress +
                    "&contactpannumber=" + s +
                    "&contact_gender=" + selected_employee_gender +
                    "&contact_sms=" + sms_toggle +
                    "&contact_email=" + email_toggle +
                    "&page_name=addcontact";

            Log.e("Queriesurl", newqueriesurl);

            String json = sh.makeServiceCall(newqueriesurl, ServiceHandler.POST, params);

            if (json != null) {
                addcontact_list = new ArrayList<>();
                addcontactlist = new HashMap<>();
                try {
                    JSONObject obj = new JSONObject(json);
                    for (int k = 0; k <= obj.length(); k++) {
                        result = obj.getString("Result");
                        message = obj.getString("message");
                        if (result.equals("1")) {
                            addcontactlist.put("Result", result);
                            addcontactlist.put("message", message);
                            addcontact_list.add(addcontactlist);
                        } else {
                            addcontactlist.put("Result", result);
                            addcontactlist.put("message", message);
                            addcontact_list.add(addcontactlist);
                        }
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

            if (addcontactlist.get("Result").equals("0")) {
                final AlertDialog alertbox = new AlertDialog.Builder(AddContactActivity.this)
                        .setTitle("Error")
                        .setMessage(addcontactlist.get("message"))
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        })
                        .show();
            } else {
                AddContactActivity.this.finish();

                savedDetailsContact.clear_contact_datas();
                addressSavedSharedPreferences.clear_address();
                // Toast.makeText(AddContactActivity.this, addcontactlist.get("message"), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

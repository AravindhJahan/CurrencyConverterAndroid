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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EditContact extends Activity {

    public static final int PICK_CONTACT = 0;
    public static String encodedImage;
    public static String imagebase64;
    private static RecyclerView.Adapter adapter;
    private static RecyclerView editcontactrecycleview;
    private static ArrayList<ManageFooterDataModel> editcontactdata;
    public ArrayList<HashMap<String, String>> editcontact_listmain;
    ArrayAdapter<String> spinnercontactArrayAdapter;
    String selected_contact;
    int position_contact;
    LinearLayout editgetcontact;

    String contact_id;
    ProgressDialog barProgressDialog;
    ImageView edit_map;
    String image, main_name, name, number, email, pan_number;
    SavedDetailsContact savedDetailsContact;
    HashMap<String, String> user_contact;
    EditText editcontact_name, editcontact_phone_number, editcontact_email, editcontact_pan_number;
    TextView editcontact_address;
    ImageView edit_imageView;
    List<NameValuePair> params;
    String result, message, Contactid;
    String newqueriesurl;
    SessionManager sessionManager;
    HashMap<String, String> user;
    Button submit_editcontact;
    ImageView editcontact_back;
    EditText editcontact_owner_name;
    TextView spinner_contact, gender;
    Spinner editcontact_spinner, gender_spinners;
    String image_url;
    AddressSavedSharedPreferences sessionManagerAddress;
    HashMap<String, String> user_address_shared;
    ArrayList<String> list1 = new ArrayList<>();
    ArrayList<String> list2 = new ArrayList<>();
    ArrayList<String> list1_id = new ArrayList<>();
    String names = null;
    HashMap<String, String> editcontactlistmain;
    String location;
    String selected_contact_gender;
    ImageView sms_img;
    Button next_editcontact;
    ImageView email_img;
    RelativeLayout sms_layout, email_layout;
    int position_contact_gender;
    private RecyclerView.LayoutManager layoutManager;
    private int PICK_IMAGE_REQUEST = 1;
    int sms_toggle = 1 ;
    int email_toggle = 1;
    ArrayList<String> gender_spin = new ArrayList<>();
    String s = "ABCDE1234F"; // get your editext value here
    Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
    ArrayList<String> contact_list = new ArrayList<>();

    public static String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        encodedImage = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        return encodedImage;
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {

        float ratio = Math.min(maxImageSize / realImage.getWidth(), maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width, height, filter);

        convert(newBitmap);

        return newBitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_contact);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        list1 = getIntent().getStringArrayListExtra("list_spine");
        list1_id = getIntent().getStringArrayListExtra("listid");
        list2 = getIntent().getStringArrayListExtra("contact_list");
        contact_list = getIntent().getStringArrayListExtra("contact_list");
        contact_id = getIntent().getStringExtra("contact_id");
        location = getIntent().getStringExtra("Location");

        sessionManager = new SessionManager(EditContact.this);
        user = sessionManager.getUserDetails();

        //editcontact_spinner
        editcontact_back = (ImageView) findViewById(R.id.editcontact_back);

        spinner_contact = (TextView) findViewById(R.id.spinner);
        gender = (TextView) findViewById(R.id.gender);

        editgetcontact = (LinearLayout) findViewById(R.id.getcontact);
        editcontact_pan_number = (EditText) findViewById(R.id.editcontact_pan_number);
        editcontact_name = (EditText) findViewById(R.id.editcontact_name);
        editcontact_phone_number = (EditText) findViewById(R.id.editcontact_phone_number);

        edit_imageView = (ImageView) findViewById(R.id.editcontactprofile_image);

        editcontact_owner_name = (EditText) findViewById(R.id.editcontact_owner_name);
        editcontact_email = (EditText) findViewById(R.id.editcontact_email);
        editcontact_address = (TextView) findViewById(R.id.editcontact_address);
        edit_map = (ImageView) findViewById(R.id.edit_map);
        submit_editcontact = (Button) findViewById(R.id.submit_editcontact);
        sms_layout = (RelativeLayout) findViewById(R.id.sms_toogles);
        email_layout = (RelativeLayout) findViewById(R.id.email_toggles);
        sms_img = (ImageView) findViewById(R.id.sms_toggle_imgs);
        email_img = (ImageView) findViewById(R.id.email_toggle_imgs);


        image_url = list2.get(0).toString();

        gender_spin.add("Select Gender");
        gender_spin.add("Male");
        gender_spin.add("Female");

        Glide.with(getApplicationContext())
                .load(image_url)
                .transform(new RoundImageTransform(EditContact.this))
                .into(edit_imageView);


        editcontact_owner_name.setText(list2.get(1).toString());
        editcontact_name.setText(list2.get(2).toString());
        editcontact_phone_number.setText(list2.get(3).toString());

        if (location == null) {
            editcontact_address.setText(list2.get(2).toString());
        } else {
            editcontact_address.setText(location.toString());
        }

        editcontact_email.setText(list2.get(4).toString());

        sessionManagerAddress = new AddressSavedSharedPreferences(EditContact.this);
        user_address_shared = sessionManagerAddress.getAddress_details();

        if (user_address_shared.get("map_address") == null) {

            editcontact_address.setText(list2.get(5).toString());

        } else {
            editcontact_address.setText(user_address_shared.get("map_address"));
        }

        editcontact_pan_number.setText(list2.get(9).toString());

        for (int m = 0; m < list1.size(); m++) {
            String id = list2.get(7).toString();
            if (id.equals(list1_id.get(m).toString())) {
                names = list1.get(m).toString();
                position_contact = Integer.parseInt(list1_id.get(m).toString());
                Log.e("name", names);
            } else {

            }
        }
        editcontact_spinner = (Spinner) findViewById(R.id.editcontact_spinner);
        gender_spinners = (Spinner) findViewById(R.id.gender_spinners);

        spinner_contact.setText(names.toString());

        spinner_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinner_contact.setVisibility(View.GONE);
                editcontact_spinner.setVisibility(View.VISIBLE);
                spinnercontactArrayAdapter = new ArrayAdapter<String>(EditContact.this, R.layout.spinner_single_item, list1) {
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

                spinnercontactArrayAdapter.setDropDownViewResource(R.layout.spinner_single_item);
                editcontact_spinner.setAdapter(spinnercontactArrayAdapter);
                editcontact_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedItemText = (String) parent.getItemAtPosition(position);
                        Toast.makeText(getApplicationContext(), "Selected : " + position, Toast.LENGTH_SHORT).show();
                        selected_contact_gender = selectedItemText;
                        position_contact_gender = position + 1;

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        });

        selected_contact_gender = list2.get(10).toString();

        gender.setText(selected_contact_gender);

        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender_spinners.setVisibility(View.VISIBLE);
                gender.setVisibility(View.GONE);
                spinnercontactArrayAdapter = new ArrayAdapter<String>(EditContact.this, R.layout.spinner_single_item, gender_spin) {
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
                spinnercontactArrayAdapter.setDropDownViewResource(R.layout.spinner_single_item);
                gender_spinners.setAdapter(spinnercontactArrayAdapter);
                gender_spinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedItemText = (String) parent.getItemAtPosition(position);
                        Toast.makeText(getApplicationContext(), "Selected : " + position, Toast.LENGTH_SHORT).show();
                        selected_contact_gender = selectedItemText;
                        position_contact_gender = position + 1;

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        });

        if (list2.get(11).equals("0")) {
            Glide.with(EditContact.this).load(R.drawable.toggle_off).into(sms_img);
        } else {
            Glide.with(EditContact.this).load(R.drawable.toggle_on).into(sms_img);
        }

        if (list2.get(12).equals("0")) {
            Glide.with(EditContact.this).load(R.drawable.toggle_off).into(email_img);
        } else {
            Glide.with(EditContact.this).load(R.drawable.toggle_on).into(email_img);
        }

        sms_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sms_toggle == 0) {
                    sms_toggle = 1;
                    Glide.with(EditContact.this).load(R.drawable.toggle_off).into(sms_img);
                } else {
                    Glide.with(EditContact.this).load(R.drawable.toggle_on).into(sms_img);
                    sms_toggle = 0;
                }
            }
        });

        email_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email_toggle == 0) {
                    email_toggle = 1;
                    Glide.with(EditContact.this).load(R.drawable.toggle_off).into(email_img);
                } else {
                    Glide.with(EditContact.this).load(R.drawable.toggle_on).into(email_img);
                    email_toggle = 0;
                }
            }
        });


        editcontact_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                EditContact.this.finish();
            }
        });

        editgetcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        edit_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, PICK_IMAGE_REQUEST);
            }
        });

        submit_editcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String errorMessage;

                if (spinner_contact == null) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(EditContact.this)
                            .setMessage("Please Select contact")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    EditContact.this.finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                    alertbox.show();
                } else if (editcontact_owner_name == null) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(EditContact.this)
                            .setMessage("Please enter contact owner name")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    EditContact.this.finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                    alertbox.show();
                } else if (editcontact_name == null) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(EditContact.this)
                            .setMessage("Please enter contact name")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    EditContact.this.finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                    alertbox.show();
                } else if (editcontact_email == null) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(EditContact.this)
                            .setMessage("Please enter Email-Id")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    EditContact.this.finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                    alertbox.show();
                } else if (editcontact_phone_number == null) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(EditContact.this)
                            .setMessage("Please enter Mobile number")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    EditContact.this.finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                    alertbox.show();

                } else if (editcontact_phone_number.length() != 10) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(EditContact.this)
                            .setMessage("Please enter Validated Mobile number")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    EditContact.this.finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                    alertbox.show();
                } else if (editcontact_pan_number == null) {

                    AlertDialog.Builder alertbox = new AlertDialog.Builder(EditContact.this)
                            .setMessage("Please enter PAN Number")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    EditContact.this.finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                    alertbox.show();
                } else if (editcontact_address == null) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(EditContact.this)
                            .setMessage("Please enter address")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    EditContact.this.finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                    alertbox.show();
                } else if (gender == null) {

                    AlertDialog.Builder alertbox = new AlertDialog.Builder(EditContact.this)
                            .setMessage("Please enter Gender")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    EditContact.this.finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                    alertbox.show();

                } else if (editcontact_pan_number != null) {
                    s = editcontact_pan_number.getText().toString();
                    Matcher matcher = pattern.matcher(s);
                    if (matcher.matches()) {
                        new update_contact().execute();
                    } else {
                        AlertDialog.Builder alertbox = new AlertDialog.Builder(EditContact.this)
                                .setMessage("Invalid PAN Number")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        editcontact_pan_number.setHint("PAN NUMBER");
                                    }
                                });
                        alertbox.show();


                    }

                } else {
                    new update_contact().execute();
                }
            }
        });

        edit_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedDetailsContact = new SavedDetailsContact(EditContact.this);
                user_contact = savedDetailsContact.getUserDetails();

                image = user_contact.get("contact_image");
                main_name = editcontact_owner_name.getText().toString();
                name = editcontact_name.getText().toString();
                number = editcontact_phone_number.getText().toString();
                email = editcontact_email.getText().toString();
                pan_number = editcontact_pan_number.getText().toString();


                savedDetailsContact.createLoginSession(
                        user_contact.get("contact_type"),
                        user_contact.get("contact_type_id"),
                        main_name,
                        name,
                        number,
                        email,
                        image,
                        pan_number);

                Intent inten = new Intent(EditContact.this, LocationSelection.class);
                startActivity(inten);

            }
        });

        next_editcontact = (Button) findViewById(R.id.next_editcontact);
        next_editcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String errorMessage;

                if (spinner_contact == null) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(EditContact.this)
                            .setMessage("Please Select contact")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    EditContact.this.finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                    alertbox.show();
                } else if (editcontact_owner_name == null) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(EditContact.this)
                            .setMessage("Please enter contact owner name")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    EditContact.this.finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                    alertbox.show();
                } else if (editcontact_name == null) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(EditContact.this)
                            .setMessage("Please enter contact name")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    EditContact.this.finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                    alertbox.show();
                } else if (editcontact_email == null) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(EditContact.this)
                            .setMessage("Please enter Email-Id")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    EditContact.this.finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                    alertbox.show();
                } else if (editcontact_phone_number == null) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(EditContact.this)
                            .setMessage("Please enter Mobile number")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    EditContact.this.finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                    alertbox.show();

                } else if (editcontact_phone_number.length() != 10) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(EditContact.this)
                            .setMessage("Please enter Validated Mobile number")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    EditContact.this.finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                    alertbox.show();
                } else if (editcontact_pan_number == null) {

                    AlertDialog.Builder alertbox = new AlertDialog.Builder(EditContact.this)
                            .setMessage("Please enter PAN Number")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    EditContact.this.finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                    alertbox.show();
                } else if (editcontact_address == null) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(EditContact.this)
                            .setMessage("Please enter address")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    EditContact.this.finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                    alertbox.show();
                } else if (gender == null) {

                    AlertDialog.Builder alertbox = new AlertDialog.Builder(EditContact.this)
                            .setMessage("Please enter Gender")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    EditContact.this.finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                    alertbox.show();

                } else if (editcontact_pan_number != null) {
                    s = editcontact_pan_number.getText().toString();
                    Matcher matcher = pattern.matcher(s);
                    if (matcher.matches()) {
                        new update_contacts().execute();
                    } else {
                        AlertDialog.Builder alertbox = new AlertDialog.Builder(EditContact.this)
                                .setMessage("Invalid PAN Number")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        editcontact_pan_number.setHint("PAN NUMBER");
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

                    if (selectedImage.equals(null)) {

                        String[] filePathColumn = {list2.get(0).toString()};
                        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        Glide.with(getApplicationContext())
                                .load(image_url.toString())
                                .asBitmap()
                                .transform(new RoundImageTransform(EditContact.this))
                                .into(edit_imageView);

                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        scaleDown(bitmap, 200, true);

                    } else {
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        Glide.with(getApplicationContext())
                                .load(selectedImage)
                                .asBitmap()
                                .transform(new RoundImageTransform(EditContact.this))
                                .into(edit_imageView);

                        Bitmap bitmap = null;

                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        scaleDown(bitmap, 200, true);
                    }
                } else {

                }
                break;
        }
    }

    public void showSelectedNumber(String number, String name) {
        editcontact_name.setText(name);

        number = number.replace("(", "");
        number = number.replace(")", "");
        number = number.replace("-", "");
        number = number.replace(" ", "");
        number = number.replace("+91", "");

        editcontact_phone_number.setText(number);
        Toast.makeText(this, number + name, Toast.LENGTH_LONG).show();
    }

    private class update_contact extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(EditContact.this, "Loading...", "Please Wait ...", true);

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();

            String newowner_name = editcontact_owner_name.getText().toString();
            String newname = editcontact_name.getText().toString();
            String newphone_number = editcontact_phone_number.getText().toString();
            String newemail = editcontact_email.getText().toString();
            String newaddress = editcontact_address.getText().toString();
            String newpannumber = editcontact_pan_number.getText().toString();

            newowner_name = newowner_name.replace(" ", "%20");
            newname = newname.replace(" ", "%20");
            newphone_number = newphone_number.replace(" ", "%20");
            newemail = newemail.replace(" ", "%20");
            newaddress = newaddress.replace(" ", "%20");

            if (encodedImage != null) {
                imagebase64 = encodedImage;
            } else {
                URL url = null;
                Bitmap bmp = null;

                try {
                    url = new URL(image_url.toString());
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


            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("contact_image", imagebase64));

            newqueriesurl = Constant.EDITCONTACT +
                    "session_user_id=" + user.get("user_id") +
                    "&contact_type=" + position_contact +
                    "&contactowner=" + newowner_name +
                    "&contactname=" + newname +
                    "&contactnumber=" + newphone_number +
                    "&contactmailid=" + newemail +
                    "&contactaddress=" + newaddress +
                    "&contact_id=" + list2.get(6).toString() +
                    "&contactpannumber=" + newpannumber +
                    "&contact_gender=" + selected_contact_gender +
                    "&contact_sms=" + sms_toggle +
                    "&contact_email=" + email_toggle +
                    "&page_name=editcontact";

            Log.e("newqueriesurl", newqueriesurl);

            String json = sh.makeServiceCall(newqueriesurl, ServiceHandler.POST, params);

            Log.e("json", json);

            if (json != null) {
                editcontact_listmain = new ArrayList<>();
                editcontactlistmain = new HashMap<>();
                try {
                    JSONObject obj = new JSONObject(json);
                    for (int k = 0; k <= obj.length(); k++) {
                        result = obj.getString("Result");
                        message = obj.getString("message");
                        Contactid = obj.getString("Contactid");
                        editcontactlistmain.put("Result", result);
                        editcontactlistmain.put("message", message);
                        editcontactlistmain.put("Contactid", Contactid);

                        editcontact_listmain.add(editcontactlistmain);

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

            if (editcontactlistmain.get("Result").equals("0")) {
                final AlertDialog alertbox = new AlertDialog.Builder(EditContact.this)
                        .setTitle("Error")
                        .setMessage(editcontactlistmain.get("message"))
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        })
                        .show();
            } else {
                EditContact.this.finish();
                //Toast.makeText(EditContact.this, editcontactlistmain.get("message"), Toast.LENGTH_SHORT).show();
                encodedImage = null;
            }
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        startActivity(getIntent());
        finish();
    }

    private class update_contacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barProgressDialog = ProgressDialog.show(EditContact.this, "Loading...", "Please Wait ...", true);

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler sh = new ServiceHandler();

            String newowner_name = editcontact_owner_name.getText().toString();
            String newname = editcontact_name.getText().toString();
            String newphone_number = editcontact_phone_number.getText().toString();
            String newemail = editcontact_email.getText().toString();
            String newaddress = editcontact_address.getText().toString();
            String newpannumber = editcontact_pan_number.getText().toString();

            newowner_name = newowner_name.replace(" ", "%20");
            newname = newname.replace(" ", "%20");
            newphone_number = newphone_number.replace(" ", "%20");
            newemail = newemail.replace(" ", "%20");
            newaddress = newaddress.replace(" ", "%20");

            if (encodedImage != null) {
                imagebase64 = encodedImage;
            } else {
                URL url = null;
                Bitmap bmp = null;

                try {
                    url = new URL(image_url.toString());
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


            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("contact_image", imagebase64));

            Log.e("list2", list2.get(6).toString());

            newqueriesurl = Constant.EDITCONTACT +
                    "session_user_id=" + user.get("user_id") +
                    "&contact_type=" + position_contact +
                    "&contactowner=" + newowner_name +
                    "&contactname=" + newname +
                    "&contactnumber=" + newphone_number +
                    "&contactmailid=" + newemail +
                    "&contactaddress=" + newaddress +
                    "&contact_id=" + list2.get(6).toString() +
                    "&contactpannumber=" + newpannumber +
                    "&contact_gender=" + selected_contact_gender +
                    "&contact_sms=" + sms_toggle +
                    "&contact_email=" + email_toggle +
                    "&page_name=editcontact";

            Log.e("newqueriesurl", newqueriesurl);

            String json = sh.makeServiceCall(newqueriesurl, ServiceHandler.POST, params);

            Log.e("json", json);

            if (json != null) {
                editcontact_listmain = new ArrayList<>();
                editcontactlistmain = new HashMap<>();
                try {
                    JSONObject obj = new JSONObject(json);
                    for (int k = 0; k <= obj.length(); k++) {
                        result = obj.getString("Result");
                        message = obj.getString("message");
                        Contactid = obj.getString("Contactid");
                        editcontactlistmain.put("Result", result);
                        editcontactlistmain.put("message", message);
                        editcontactlistmain.put("Contactid", Contactid);
                        editcontact_listmain.add(editcontactlistmain);

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

            if (editcontactlistmain.get("Result").equals("0")) {
                final AlertDialog alertbox = new AlertDialog.Builder(EditContact.this)
                        .setTitle("Error")
                        .setMessage(editcontactlistmain.get("message"))
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        })
                        .show();
            } else {
                Intent intent = new Intent(EditContact.this, AddContactLeadsActivity.class);
                intent.putExtra("contact_list", contact_list);
                intent.putExtra("Contactid", Contactid);
                intent.putExtra("add_contact","0");

                startActivity(intent);

                encodedImage = null;
            }
        }
    }

}
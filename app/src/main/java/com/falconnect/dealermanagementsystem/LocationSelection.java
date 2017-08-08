package com.falconnect.dealermanagementsystem;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.falconnect.dealermanagementsystem.SharedPreference.AddressSavedSharedPreferences;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import static com.falconnect.dealermanagementsystem.R.id.map;

public class LocationSelection extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    public static int ACTIVITY_1_REQUEST_CODE = 0;
    public static int add_pick_drop_req = 1;
    public static Activity fa_add_pickup_drop_location;
    private static String TAG = "MAP LOCATION";
    public Button save_BTN;
    public ImageView current_location_IV;
    public ImageView back_icon_pickup_drop_location;
    public EditText address_ET;
    public Boolean IS_ADD_PICK_DROP = false;
    public boolean connectionFlag = true;
    //   public EditText door_no_ET;
    public int reqCode;
    public Snackbar snackbar;
    public Boolean IS_ADDRESS_ENTERED = false;
    public String address_entered;
    public String coming_from;
    public Button select_your_address;
    //    Toolbar mToolbar;
    //      The formatted location address.
    protected String mAddressOutput;
    protected String mAreaOutput;
    protected String mCityOutput;
    protected String mStateOutput;
    String get_address;
    ImageView back, tick;
    View mapView;
    LatLng latLong;
    AddressSavedSharedPreferences sessionManagerAddress;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LatLng mCenterLatLong;
    //      Receiver registered with this activity to get the response from FetchAddressIntentService.
    private AddressResultReceiver mResultReceiver;
    private LocationSelection ADD_PICK_DROP_CONTEXT;
    private CoordinatorLayout coordinatorLayout_add_pickup_drop_location;
    private double longitude;
    private double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ADD_PICK_DROP_CONTEXT = this;
        fa_add_pickup_drop_location = this;

        if (getIntent().getExtras() != null) {
            coming_from = getIntent().getStringExtra("coming_from");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);

        coordinatorLayout_add_pickup_drop_location = (CoordinatorLayout) findViewById(R.id.coordinatorLayout_add_pickup_drop_location);
        save_BTN = (Button) findViewById(R.id.save_BTN);
        address_ET = (EditText) findViewById(R.id.address_ET);
        //door_no_ET = (EditText) findViewById(R.id.door_no_ET);
        current_location_IV = (ImageView) findViewById(R.id.current_location_IV);
        //back_icon_pickup_drop_location = (ImageView) findViewById(R.id.back_icon_pickup_drop_location);
        select_your_address = (Button) findViewById(R.id.select_your_address);

        back = (ImageView) findViewById(R.id.map_back);

        tick = (ImageView) findViewById(R.id.map_tick);

        current_location_IV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("#33 prabhu map_ready 1");

                if (ActivityCompat.checkSelfPermission(ADD_PICK_DROP_CONTEXT, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ADD_PICK_DROP_CONTEXT, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                if (mLastLocation != null) {
                    changeMap(mLastLocation);
                    Log.d(TAG, "ON connected");

                } else
                    try {
                        LocationServices.FusedLocationApi.removeLocationUpdates(
                                mGoogleApiClient, ADD_PICK_DROP_CONTEXT);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                try {
                    LocationRequest mLocationRequest = new LocationRequest();
                    mLocationRequest.setInterval(10000);
                    mLocationRequest.setFastestInterval(5000);
                    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationServices.FusedLocationApi.requestLocationUpdates(
                            mGoogleApiClient, mLocationRequest, ADD_PICK_DROP_CONTEXT);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        address_ET.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                openAutocompleteActivity();

            }
        });


        mapFragment.getMapAsync(this);
        mResultReceiver = new AddressResultReceiver(new Handler());


        select_your_address.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //save_BTN.callOnClick();
            }
        });

       /* save_BTN.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                PreDefineFunction.hideSoftKeyboard(ADD_PICK_DROP_CONTEXT);

                if (PreDefineFunction.IsNetworkAvailable(ADD_PICK_DROP_CONTEXT)) {

                    if (address_ET.getText().toString().trim().equalsIgnoreCase("")) {

                        snackbar = Snackbar
                                .make(coordinatorLayout_add_pickup_drop_location, getString(R.string.address_should_not_be_empty), Snackbar.LENGTH_LONG);

                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setGravity(Gravity.CENTER_HORIZONTAL);
                        textView.setTextColor(Color.YELLOW);
                        snackbar.show();

                    } else {

                        reqCode = add_pick_drop_req;
                        checkConnection(reqCode);
                    }

                } else {

                    snackbar = Snackbar.make(coordinatorLayout_add_pickup_drop_location, getString(R.string.check_internet_connection), Snackbar.LENGTH_LONG);
                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setGravity(Gravity.CENTER_HORIZONTAL);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();

                }
            }
        });*/

        if (checkPlayServices()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            if (!AppUtils.isLocationEnabled(ADD_PICK_DROP_CONTEXT)) {
                // notify user
                AlertDialog.Builder dialog = new AlertDialog.Builder(ADD_PICK_DROP_CONTEXT);
                dialog.setMessage("Location not enabled!");
                dialog.setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Auto-generated method stub

                    }
                });
                dialog.show();
            }
            buildGoogleApiClient();
        } else {
            Toast.makeText(ADD_PICK_DROP_CONTEXT, "Location not supported in this device", Toast.LENGTH_SHORT).show();
        }

        tick.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManagerAddress = new AddressSavedSharedPreferences(LocationSelection.this);
                sessionManagerAddress.createAddressSession(address_ET.getText().toString());
                LocationSelection.this.finish();
            }
        });

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationSelection.this.finish();
            }
        });


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            changeMap(mLastLocation);
            Log.d(TAG, "ON connected");

        } else
            try {
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient, this);

            } catch (Exception e) {
                e.printStackTrace();
            }
        try {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            if (location != null)
                changeMap(location);
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d(TAG, "OnMapReady");
        mMap = googleMap;

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("Camera postion change" + "", cameraPosition + "");
                mCenterLatLong = cameraPosition.target;

                mMap.clear();

                try {

                    Location mLocation = new Location("");
                    mLocation.setLatitude(mCenterLatLong.latitude);
                    mLocation.setLongitude(mCenterLatLong.longitude);
                    startIntentService(mLocation);

                    latitude = mCenterLatLong.latitude;
                    longitude = mCenterLatLong.longitude;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            mGoogleApiClient.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
            }
            return false;
        }
        return true;
    }

    private void changeMap(Location location) {

        Log.d(TAG, "Reaching map" + mMap);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        if (mMap != null) {
            CameraUpdate cameraUpdate = null;
            mMap.getUiSettings().setZoomControlsEnabled(false);

            latLong = new LatLng(location.getLatitude(), location.getLongitude());

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLong));

            cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLong, 17);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLong).zoom(17f).build();

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.animateCamera(cameraUpdate);

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            startIntentService(location);


        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    protected void displayAddressOutput() {
        System.out.println("#33 prabhu map_address " + mAddressOutput);
        try {
            if (mAreaOutput != null)
                if (IS_ADDRESS_ENTERED) {
                    address_ET.setText(address_entered);
                    IS_ADDRESS_ENTERED = false;
                } else {
                    address_ET.setText(removeNewLine(mAddressOutput));
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void startIntentService(Location mLocation) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(AppUtils.LocationConstants.RECEIVER, mResultReceiver);
        intent.putExtra(AppUtils.LocationConstants.LOCATION_DATA_EXTRA, mLocation);
        startService(intent);
    }

    private void openAutocompleteActivity() {
        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(Place.TYPE_COUNTRY)
                    .setCountry("IN")
                    .build();

            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setFilter(typeFilter)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(), 0).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            String message = "Google Play Services is not available: " + GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
            Toast.makeText(ADD_PICK_DROP_CONTEXT, message, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(LocationSelection.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_CODE_AUTOCOMPLETE);
                            }
                        }).create().show();
            } else {
                ActivityCompat.requestPermissions(LocationSelection.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_AUTOCOMPLETE);
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_AUTOCOMPLETE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        buildGoogleApiClient();
                        mGoogleApiClient.connect();
                        mMap.setMyLocationEnabled(true);

                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(ADD_PICK_DROP_CONTEXT, data);
                LatLng latLong;
                latLong = place.getLatLng();
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLong).zoom(17f).build();

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                System.out.println("#33 prabhu Address Picker  " + place.getAddress().toString() + " lat " + place.getLatLng().latitude +
                        " long " + place.getLatLng().longitude);
                address_entered = place.getAddress().toString();
                address_ET.setText(address_entered);
                IS_ADDRESS_ENTERED = true;
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(ADD_PICK_DROP_CONTEXT, data);
                System.out.println("#33 prabhu Address error " + status);
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public String removeNewLine(String address) {
        String newAddress = address.replace(System.getProperty("line.separator"), " ");
        return newAddress;
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }


        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            mAddressOutput = resultData.getString(AppUtils.LocationConstants.RESULT_DATA_KEY);

            mAreaOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_AREA);

            mCityOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_CITY);
            mStateOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_STREET);

            displayAddressOutput();

            if (resultCode == AppUtils.LocationConstants.SUCCESS_RESULT) {
            }
        }
    }
}

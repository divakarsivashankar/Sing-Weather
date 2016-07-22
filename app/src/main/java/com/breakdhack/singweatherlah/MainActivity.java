package com.breakdhack.singweatherlah;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.breakdhack.singweatherlah.GeoLocation.TrackLocation;
import com.breakdhack.singweatherlah.WeatherData.NEAWeatherService;



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "";
    private AlertDialog.Builder alertDialog;
    private AlertDialog myAlertDialog;
    TextView lat, lon, address, hTemp, lTemp, tForeCast, hForeCast;
    ImageView wImage;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;
    TrackLocation mll;
    NEAWeatherService nws;
    String locality = "", newFCast;
    private boolean alreadyCalled = false, notInSingapore = false, noLocationAccess = false;
    private boolean uiDialog = false, hideDialog = false;
    String areaNames[] = {
            "Ang Mo Kio",
            "Bedok",
            "Bishan",
            "Boon Lay",
            "Bukit Batok",
            "Bukit Merah",
            "Bukit Panjang",
            "Bukit Timah",
            "Central Water Catchment",
            "Changi",
            "Choa Chu Kang",
            "Clementi",
            "City",
            "Geylang",
            "Hougang",
            "Jalan Bahar",
            "Jurong East",
            "Jurong Island",
            "Jurong West",
            "Kallang",
            "Lim Chu Kang",
            "Mandai",
            "Marine Parade",
            "Novena",
            "Pasir Ris",
            "Paya Lebar",
            "Pioneer",
            "Pulau Tekong",
            "Pulau Ubin",
            "Punggol",
            "Queenstown",
            "Seletar",
            "Sembawang",
            "Sengkang",
            "Sentosa",
            "Serangoon",
            "Southern Islands",
            "Sungei Kadut",
            "Tampines",
            "Tanglin",
            "Tengah",
            "Toa Payoh",
            "Tuas",
            "Western Islands",
            "Western Water Catchment",
            "Woodlands",
            "Yishun"};
    String a, b, c, d, e, f, g ,h, i, j;
    private int hasFineLocationPermission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lat = (TextView)findViewById(R.id.lat);
        lon = (TextView)findViewById(R.id.lon);
        address = (TextView)findViewById(R.id.address);
        hTemp = (TextView)findViewById(R.id.highTemp);
        lTemp = (TextView)findViewById(R.id.lowTemp);
        tForeCast = (TextView)findViewById(R.id.todayForecast);
        hForeCast = (TextView)findViewById(R.id.hourForecast);
        wImage = (ImageView)findViewById(R.id.weatherImage);
        a= "PS-RA-SH-SK-SR";
        b = "BR-FG-HZ-LH";
        c= "DR-LR-LS";
        d = "HG-HT-TL";
        e = "CL-OC";
        f = "FA-FW";
        g = "HR-HS";
        h = "SN-SS";
        i = "WD-WF";
        j= "WR-WS";


//
//        loadingDialog = new ProgressDialog(this);
//        loadingDialog.setMessage("Fetching Weather Information...");
//        loadingDialog.show();

        hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) + ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        }
        else {
            uiDialog = true;
            callAllFuctions();
        }
    }

    private void callAllFuctions() {

        if(uiDialog) {

            alreadyCalled = true;

            mll = new TrackLocation(MainActivity.this);

//            Location loc = mll.getLocation();

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            //gps = new GPSTracker(AndroidGPSTrackingActivity.this);

            // check if GPS enabled
            if (mll.canGetLocation()) {

                double latitude = mll.getLatitude();
                double longitude = mll.getLongitude();

                lat.append(Double.toString(latitude));
                lon.append(Double.toString(longitude));



                if (longitude != 0.0 || latitude != 0.0) {


                    try {
                        List<Address> addresses = null;

                        addresses = geocoder.getFromLocation(latitude, longitude, 1);

                        if (addresses != null || addresses.size() != 0) {

                            String address1 = addresses.get(0).getAddressLine(0);
                            String country = addresses.get(0).getCountryName();

                           // Toast.makeText(this, country, Toast.LENGTH_LONG).show();

                            if(!country.equals("Singapore")){
                                notInSingapore = true;
                                AlertDialog.Builder notSingaporeDialog = new AlertDialog.Builder(this);
                                notSingaporeDialog.setTitle("Your are not located in Singapore");
                                notSingaporeDialog.setMessage("Your Location is not in Singapore, But You can proceed to check the weather in singapore.");

                                notSingaporeDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.cancel();
                                    }
                                });

                                // Showing Alert Message
                                notSingaporeDialog.show();

                            }


                            for (String area : areaNames) {
                                if (address1.contains(area)) {

                                    locality = area;
                                }
                            }

                            if (!(locality.length() == 0)) {
                                address.append(locality);
                                new newParseMagic().execute();
                                new parseMagic().execute();
                                uiDialog = false;

                            } else {
                                address.append(address1);
                                new newParseMagic().execute();
                                uiDialog = false;

                            }

                        } else {

                            address.setText("");
                            new newParseMagic().execute();
                            new parseMagic().execute();
                            uiDialog = false;

                        }


                    } catch (IOException ioException) {
                        // Catch network or other I/O problems.
                        String errorMessage = "service_not_available";
                        Log.e(TAG, errorMessage, ioException);
                    } catch (IllegalArgumentException illegalArgumentException) {
                        // Catch invalid latitude or longitude values.
                        String errorMessage = "invalid_lat_long_used";
                        Log.e(TAG, errorMessage + ". " +
                                "Latitude = " + mll.getLatitude() +
                                ", Longitude = " +
                                mll.getLongitude(), illegalArgumentException);
                    }

                }
//            else{
//                // can't get location
//                // GPS or Network is not enabled
//                // Ask user to enable GPS/network in settings
//                mll.showSettingsAlert();
//            }
                else if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED) {

                    if(hideDialog) {
                        myAlertDialog.dismiss();
                        hideDialog = false;
                    }
                    grantPremission();
                }

                else {

                    AlertDialog.Builder newAlertDialog = new AlertDialog.Builder(this);

                    // Setting Dialog Title
                    newAlertDialog.setTitle("GPS not Recognising");

                    // Setting Dialog Message
                    newAlertDialog.setMessage("Your GPS is not returning your location. We believe you are indoors, which will have some some screening effect");

                    // On pressing Settings button
                    newAlertDialog.setPositiveButton("Close App", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                            // System.exit(1);
                            System.exit(0);
                        }
                    });

                    // Showing Alert Message
                    newAlertDialog.show();
                }


            }
        }
    }


    private void setData(String data){

        if(data != null) {
            setMyImage(data);
            String packageName = getPackageName();
            int resId = getResources().getIdentifier(data, "string", packageName);
            String newData = getString(resId);
            if(noLocationAccess) {
                hForeCast.append(locality + " will be " + newData);
                noLocationAccess = false;
            }
            //loadingDialog.hide();
        } else {
            //hForeCast.append(" it will be " +newFCast);
            setData(newFCast);
//            int resourceID = getResources().getIdentifier("drawable/" +newFCast,null,getPackageName());
//            Drawable wIcon = getResources().getDrawable(resourceID);
//            wImage.setImageDrawable(wIcon);
        }

        if(notInSingapore){
            hForeCast.setText("");
        }
    }

    private void setMyImage(String data) {

        if(a.contains(data)){

            int resourceID = getResources().getIdentifier("drawable/a",null,getPackageName());
            Drawable wIcon = getResources().getDrawable(resourceID);
            wImage.setImageDrawable(wIcon);

        } else if(b.contains(data)){

            int resourceID = getResources().getIdentifier("drawable/b",null,getPackageName());
            Drawable wIcon = getResources().getDrawable(resourceID);
            wImage.setImageDrawable(wIcon);


        }
        else if(c.contains(data)){
            int resourceID = getResources().getIdentifier("drawable/c",null,getPackageName());
            Drawable wIcon = getResources().getDrawable(resourceID);
            wImage.setImageDrawable(wIcon);
        }
        else if(d.contains(data)){
            int resourceID = getResources().getIdentifier("drawable/d",null,getPackageName());
            Drawable wIcon = getResources().getDrawable(resourceID);
            wImage.setImageDrawable(wIcon);
        }
        else if(e.contains(data)){
            int resourceID = getResources().getIdentifier("drawable/e",null,getPackageName());
            Drawable wIcon = getResources().getDrawable(resourceID);
            wImage.setImageDrawable(wIcon);
        }
        else if(f.contains(data)){
            int resourceID = getResources().getIdentifier("drawable/f",null,getPackageName());
            Drawable wIcon = getResources().getDrawable(resourceID);
            wImage.setImageDrawable(wIcon);
        }
        else if(g.contains(data)){
            int resourceID = getResources().getIdentifier("drawable/g",null,getPackageName());
            Drawable wIcon = getResources().getDrawable(resourceID);
            wImage.setImageDrawable(wIcon);
        }
        else if(h.contains(data)){
            int resourceID = getResources().getIdentifier("drawable/h",null,getPackageName());
            Drawable wIcon = getResources().getDrawable(resourceID);
            wImage.setImageDrawable(wIcon);
        }
        else if(i.contains(data)){
            int resourceID = getResources().getIdentifier("drawable/i",null,getPackageName());
            Drawable wIcon = getResources().getDrawable(resourceID);
            wImage.setImageDrawable(wIcon);
        }
        else if(j.contains(data)){
            int resourceID = getResources().getIdentifier("drawable/j",null,getPackageName());
            Drawable wIcon = getResources().getDrawable(resourceID);
            wImage.setImageDrawable(wIcon);
        }
        else
        {
            int resourceID = getResources().getIdentifier("drawable/" +data.toLowerCase(),null,getPackageName());
            Drawable wIcon = getResources().getDrawable(resourceID);
            wImage.setImageDrawable(wIcon);

        }



    }

    class parseMagic extends AsyncTask<String,String,String>{


        @Override
        protected String doInBackground(String... arg0) {
            String response = new NEAWeatherService().weatherForeCast(locality);
            //address.setText(response);

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            setData(result);
        }

    }

    class newParseMagic extends AsyncTask<String,String,String[]>{


        @Override
        protected String[] doInBackground(String... arg0) {
            String[] response1 = new String[5];
            response1 = new NEAWeatherService().dayForeCast();
            //address.setText(response);

            return response1;
        }

        @Override
        protected void onPostExecute(String[] result) {
            setTodayData(result);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 12: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    uiDialog = true;
                    if(hideDialog) {
                        myAlertDialog.dismiss();
                        hideDialog = false;
                    }
                    callAllFuctions();
                } else {
                    if(hideDialog) {
                        myAlertDialog.dismiss();
                        hideDialog = false;
                    }
                    grantPremission();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void grantPremission(){

        if(hideDialog) {
            myAlertDialog.dismiss();
            hideDialog = false;
        }

        hideDialog = true;
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Grant Permission to Access Location");
        alertDialog.setMessage("We Will not be able to provide your locality specific weather updates without Location permission. If you opt to Cancel the country's weather report for the day will be displayed. ");

        alertDialog.setPositiveButton("Grant Permission", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);


            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                address.setText(" ");
                noLocationAccess = true;
                new newParseMagic().execute();
                uiDialog = false;
                dialog.cancel();
            }
        });


        // Showing Alert Message
        //alertDialog.show();
        myAlertDialog = alertDialog.create();
        myAlertDialog.show();
    }

    private void setTodayData(String[] result) {

        hTemp.append(result[0] + " \u2103");
        lTemp.append(result[1] + " \u2103");
        tForeCast.append(result[2]);
        newFCast = result[3];
        if(locality.equals("") || locality.equals(null)) {
            setData(newFCast);
        }

    }


    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        hTemp.setText("High = ");
        lTemp.setText("Low = ");
        lat.setText("Your Latitude = ");
        tForeCast.setText("Today Forecast for the Island is ");
        lon.setText("Your Longitude = ");
        hForeCast.setText("For the Next 2 hours ");
        address.setText("Your Locality is ");

        alreadyCalled = false;
        uiDialog = true;
        //hideDialog = true;



    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        if (!alreadyCalled) {

            callAllFuctions();
        }
    }


}

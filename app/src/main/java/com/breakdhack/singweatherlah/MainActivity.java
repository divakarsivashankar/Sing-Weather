package com.breakdhack.singweatherlah;

import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.breakdhack.singweatherlah.GeoLocation.TrackLocation;
import com.breakdhack.singweatherlah.WeatherData.NEAWeatherService;



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "";
    TextView lat, lon, address, hTemp, lTemp, tForeCast, hForeCast;
    ImageView wImage;
    TrackLocation mll;
    NEAWeatherService nws;
    String locality = null, newFCast;
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


        mll = new TrackLocation(MainActivity.this);

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        //gps = new GPSTracker(AndroidGPSTrackingActivity.this);

        // check if GPS enabled
        if(mll.canGetLocation()){

            double latitude = mll.getLatitude();
            double longitude = mll.getLongitude();

            lat.append(Double.toString(latitude));
            lon.append(Double.toString(longitude));

            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(
                        mll.getLatitude(),
                        mll.getLongitude(),
                        // In this sample, get just a single address.
                        1);

                String address1 = addresses.get(0).getAddressLine(0);


                for(String area : areaNames) {
                    if(address1.contains(area)) {

                         locality = area;
                    }
                }

                if(!locality.equals(null)){
                    address.append(locality);
                    new newParseMagic().execute();
                    new parseMagic().execute();

                } else {
                    address.append(address1);
                    new newParseMagic().execute();

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

            }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            mll.showSettingsAlert();
        }


    }


    private void setData(String data){

        if(data != null) {
            setMyImage(data);
            String packageName = getPackageName();
            int resId = getResources().getIdentifier(data, "string", packageName);
            String newData = getString(resId);
            hForeCast.append(locality + " will be " + newData);
        } else {
            hForeCast.append(" it will be " +newFCast);
            int resourceID = getResources().getIdentifier("drawable/" +newFCast,null,getPackageName());
            Drawable wIcon = getResources().getDrawable(resourceID);
            wImage.setImageDrawable(wIcon);
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
            int resourceID = getResources().getIdentifier("drawable/" +data,null,getPackageName());
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

    private void setTodayData(String[] result) {

        hTemp.append(result[0]+ " \u2103");
        lTemp.append(result[1]+ " \u2103");
        tForeCast.append(result[2]);
        newFCast = result[3];

    }

}

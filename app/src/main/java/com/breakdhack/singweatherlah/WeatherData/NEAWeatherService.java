package com.breakdhack.singweatherlah.WeatherData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.json.XML;

/**
 * Created by Divakar on h/g/16.
 */
public class NEAWeatherService {
    private Exception err;

    String todayForeCast[]= new String[5];


    public String foreCast;
    String res;



    public String weatherForeCast( String locality){

                String endpoint = String.format("http://www.nea.gov.sg/api/WebAPI/?dataset=2hr_nowcast&keyref=781CF461BB6606AD907750DFD1D0766719B9623861E0B07B");

                try

                {
                    URL url = new URL(endpoint);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    con.setRequestMethod("GET");
                    InputStream inputStream = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while((inputLine = reader.readLine()) != null) {
                        response.append(inputLine);
                    }
                    reader.close();

                    String resXML =   response.toString();;
                    JSONObject jsonObj = null;

                    jsonObj = XML.toJSONObject(resXML);


                    JSONObject jObject = jsonObj.optJSONObject("channel");
                    JSONObject iObject = jObject.optJSONObject("item");
                    JSONObject wObject = iObject.optJSONObject("weatherForecast");
                    JSONArray jArray = wObject.getJSONArray("area");


                    for (int i = 0; i < jArray.length(); i++) {
                        try {
                            JSONObject oneObject = jArray.getJSONObject(i);
                            // Pulling items from the array
                            String oneObjectsItem = oneObject.getString("name");
                            if (oneObjectsItem.equals(locality)) {
                                String oneObjectsItem2 = oneObject.getString("forecast");
                                foreCast = oneObjectsItem2;
                            }
                        } catch (JSONException e) {
                            // Oops
                        }
                    }

                    return foreCast;

                }

                catch(Exception e)
                {
                    err = e;
                }


                return null;

    }




    public String[] dayForeCast(){

        String endpoint = String.format("http://www.nea.gov.sg/api/WebAPI/?dataset=24hrs_forecast&keyref=781CF461BB6606AD907750DFD1D0766719B9623861E0B07B");

        try

        {
            URL url = new URL(endpoint);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            InputStream inputStream = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            String resXML =   response.toString();;
            JSONObject jsonObj = null;

            jsonObj = XML.toJSONObject(resXML);


            JSONObject jObject = jsonObj.optJSONObject("channel");
            JSONObject iObject = jObject.optJSONObject("main");
            JSONObject wObject = iObject.optJSONObject("temperature");
            String nObject = iObject.optString("forecast");
            String fCast = iObject.optString("wxmain");
            String highTemp = wObject.optString("high");
            String lowTemp = wObject.optString("low");
            todayForeCast[0] = highTemp;
            todayForeCast[1] = lowTemp;
            todayForeCast[2] = nObject;
            todayForeCast[3] = fCast;

            return todayForeCast;

        }

        catch(Exception e)
        {
            err = e;
        }


        return null;

    }



}

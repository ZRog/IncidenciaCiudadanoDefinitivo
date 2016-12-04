package com.example.rogport.incidenciaciudadano;

import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by RogPort on 03/12/2016.
 */

public class ClaseAsincrona extends AsyncTask<String,Integer,String>{

    String Address1 = "",Address2 = "", City= "", State = "", Country = "",County = "", PIN = "",lat="",lng="";
    public ClaseAsincrona(){}
    @Override
    protected String doInBackground(String... urls) {
        String strRetorno="";
        Address1 = "";
        Address2 = "";
        City = "";
        State = "";
        Country = "";
        County = "";
        PIN = "";
        try {
            URL url = new URL(urls[0]);
            JSONObject jsonObject = JsonParser.getJSONfromURL(url.toString());
            try {
                String Status = jsonObject.getString("status");
                if(Status.equalsIgnoreCase("OK")){
                    JSONArray Results = jsonObject.getJSONArray("results");
                    JSONObject zero = Results.getJSONObject(0);
                    JSONArray address_components = zero.getJSONArray("address_components");
                    for(int i = 0; i < address_components.length(); i++){
                        JSONObject zero2 = address_components.getJSONObject(i);
                        String long_name = zero2.getString("long_name");
                        JSONArray mtypes = zero2.getJSONArray("types");
                        String Type = mtypes.getString(0);
                        if(TextUtils.isEmpty(long_name) == false || !long_name.equals(null) || long_name.length()>0 || long_name!= ""){
                            if(Type.equalsIgnoreCase("street_number")){
                                Address1 = long_name + " ";
                            }else if(Type.equalsIgnoreCase("route")){
                                Address1 = Address1 + long_name;
                            }else if(Type.equalsIgnoreCase("sublocality")){
                                Address2 = long_name;
                            }else if(Type.equalsIgnoreCase("locality")){
                                City = long_name;
                            }else if(Type.equalsIgnoreCase("administrative_area_level_2")){
                                Country = long_name;
                            }else if(Type.equalsIgnoreCase("administrative_area_level_1")){
                                State = long_name;
                            }else if(Type.equalsIgnoreCase("country")){
                                Country = long_name;
                            }else if(Type.equalsIgnoreCase("postal_code")){
                                PIN = long_name;
                            }
                        }
                    }
                    JSONObject geometry = zero.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    lat = location.getString("lat");
                    lng = location.getString("lng");
                    strRetorno = Address1 + "," + City + "," + State + "," + Country + "," + PIN+"," +lat+","+lng;
                }
                else if(Status.equalsIgnoreCase("ZERO_RESULTS")){
                    strRetorno = "fallo_fatality,caca";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("PRUEBA",Address1 + " " + Address2 + " " + City + " " + Country + " " + PIN + " " + State/*+" " +lat+" "+lng*/);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return strRetorno;


    }
}

package kr.gdb.wifi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;

import java.io.*;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import org.json.*;
import android.R;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by suhwancha on 2017. 8. 16..
 */

public class api extends AppCompatActivity {

    private double lat;
    private double longt;
    private Resources res;
    HashMap<String,String[]> location;
    HashMap<String,Boolean> arr1;
    String addr1="",addr2="";
    public api(Resources res,String a, String b){
        this.lat = lat;
        this.longt = longt;
        this.addr1 = a;
        this.addr2 = b;
        setLocation();
        this.res = res;

    }


    public void setLocation(){
//        location.put("강원도",강원도.split(","));
//        location.put("경기도",경기도.split(","));
    }

    public void setLat(double lat) {
        this.lat = lat;

    }

    public void setLongt(double longt) {
        this.longt = longt;
    }
    private Context context;

    public String getJson(){
        try {
            InputStream in_s = res.openRawResource(kr.gdb.wifi.R.raw.wifi);
            byte[] b = new byte[in_s.available()];
            in_s.read(b);
            return (new String(b));
        } catch (Exception e) {
             e.printStackTrace();
            return null;
        }



    }


    public String getHTML(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }

    public ArrayList<HashMap> parseData() throws Exception {
        ArrayList<HashMap> list = new ArrayList();

        String jsons = getJson();

        JSONArray jsonArray = new JSONArray(jsons);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = jsonArray.getJSONObject(i);
            if (jsonobject.getString("addr1").equals(addr1) && jsonobject.getString("addr2").equals(addr2)) {
                String name = jsonobject.getString("INSTL_PLC_NM");
                String addr = jsonobject.getString("addr1") + " " + jsonobject.getString("addr2") + " "
                        + jsonobject.getString("addr3");
                String logt = jsonobject.getString("longt");
                String lat = jsonobject.getString("lat");
                String tel = jsonobject.getString("telecom");
                HashMap<String, Object> map = new HashMap<>();
                map.put("name", name);
                map.put("addr", addr);
                map.put("tel",tel);
                map.put("longt", (Object)logt);
                map.put("lat", (Object)lat);
                list.add(map);
            }
        }
        return list;
    }

    public double getstance(double lat, double longt){
        double theta = longt - this.longt;
        double dist = Math.sin(deg2rad(lat)) * Math.sin(deg2rad(this.lat)) + Math.cos(deg2rad(lat))
                * Math.cos(deg2rad(this.lat)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1609.344;
        return (dist);

    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


}

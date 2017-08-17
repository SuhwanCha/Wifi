package kr.gdb.wifi;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import org.json.*;

/**
 * Created by suhwancha on 2017. 8. 16..
 */

public class api {

    private double lat;
    private double longt;

    public api(){
        this.lat = lat;
        this.longt = longt;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLongt(double longt) {
        this.longt = longt;
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

        String jsons = (getHTML("http://asdqwe2e.gdb.kr/wifi.json"));

        JSONArray jsonArray = new JSONArray(jsons);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonobject = jsonArray.getJSONObject(i);
            String name = jsonobject.getString("INSTL_PLC_NM");
            String tel = jsonobject.getString("MANAGE_INST_TELNO");
            String addr = jsonobject.getString("REFINE_ROADNM_ADDR");
            String logt = jsonobject.getString("REFINE_WGS84_LOGT");
            String lat = jsonobject.getString("REFINE_WGS84_LAT");
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", name);
            map.put("tel", tel);
            map.put("addr", addr);
            map.put("longt", (Object)logt);
            map.put("lat", (Object)lat);
            list.add(map);
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

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

    private static String getHTML(String urlToRead) throws Exception {
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

    public static ArrayList parseData() throws Exception {
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
            HashMap<String, String> map = new HashMap<>();
            map.put("name", name);
            map.put("tel", tel);
            map.put("addr", addr);
            map.put("logt", logt);
            map.put("lat", lat);
            list.add(map);
        }
        return list;
    }

}

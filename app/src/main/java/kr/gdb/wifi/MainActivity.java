package kr.gdb.wifi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ListView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.*;

public class MainActivity extends AppCompatActivity {
    api api;
    String[] LIST_MENU = {};

    ListViewAdapter adapter;

    ArrayList<HashMap> list;
    Resources res;
    String addr1, addr2;

    private void getPreferences(){
        Intent it = getIntent();
        addr1 = it.getStringExtra("addr1");
        addr2 = it.getStringExtra("addr2");
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         res = getResources();
        getPreferences();
        api = new api(res,addr1,addr2);


        adapter = new ListViewAdapter(this);
        ((ListView) findViewById(R.id.listview)).setAdapter(adapter);

        Thread thread = new Thread() {
            public void run() {
                try {
                    MainActivity.this.list = api.parseData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Pattern pattern = Pattern.compile("^[\\d.]+$");
        for (HashMap item : list) {
            if (pattern.matcher(item.get("longt").toString()).find()
                    && pattern.matcher(item.get("lat").toString()).find()) {
                adapter.addItem((Map<String, Object>) item);
            }
        }
    }

    private void printHashKey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }




}


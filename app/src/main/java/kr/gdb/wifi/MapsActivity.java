package kr.gdb.wifi;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.daum.android.map.MapActivity;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.R.id.list;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    private GoogleMap mMap;
    private api loc;
    LatLng dimigo;
    private Context context;
    int idx;
    String addr1,addr2,addr3;
    TextView tv;
    @Override
    public boolean onMarkerClick(Marker marker) {
        try{
            idx = (int)marker.getTag();
            String[] array = {"길찾기","취소"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("["+(String)list.get(idx).get("tel")+"]"+(String) list.get(idx).get("name"))
                    .setItems(array, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    System.out.println("google.navigation:q="
                                            +list.get(idx).get("lat") + ","
                                            +list.get(idx).get("longt")+"&mode=b");
                                    Uri gmmIntentUri = Uri.parse("google.navigation:q="
                                    +list.get(idx).get("lat") + ","
                                    +list.get(idx).get("longt")+"&mode=w");
                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                    mapIntent.setPackage("com.google.android.apps.maps");
                                    startActivity(mapIntent);
                                    break;
                                case 1:
//                                    Intent intent = new Intent(Intent.ACTION_DIAL);
//                                    intent.setData(Uri.parse("tel:"+(String)list.get(idx).get("tel")));
//                                    startActivity(intent);
                                    break;
                            }

                        }
                    });
            builder.show();

        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    Resources res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        res = getResources();
        getPreferences();
        tv = (TextView)findViewById(R.id.text3);
        if(addr1 == "" || addr2 == "" || addr3 == ""){
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
            alert_confirm.setMessage("지역을 선택해 주세요").setCancelable(false).setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent it = new Intent(getApplicationContext(), SettingsActivity.class);
                            startActivity(it);
                        }
                    }).setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            return;
                        }
                    });
            AlertDialog alert = alert_confirm.create();
            alert.show();
        }else{
            tv.setText(addr1 + " " + addr2);
        }


        loc = new api(res,addr1,addr2);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }
    boolean allowresume = false;
    @Override
    protected void onResume() {
        if(allowresume) {
            try {
                getPreferences();
                mMap.clear();
                loc = new api(res, addr1, addr2);
                System.out.println(addr1 + addr2);
                addMarker();
                tv.setText(addr1 + " " + addr2);
            } catch (Exception e) {

            }
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        allowresume = true;
        super.onPause();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    ArrayList<HashMap> list = null;
    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);

            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }


        mMap = googleMap;
        try {

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            double longitude = myLocation.getLongitude();
            double latitude = myLocation.getLatitude();

            dimigo = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dimigo, 16));
            mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);
        }catch(Exception e){
            dimigo = new LatLng(37.341893,126.8315179);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dimigo, 16));
        }
        addMarker();

    }


        public void addMarker(){

            new Thread() {
                public void run() {
                    try {
                        list = loc.parseData();
                        System.out.println(list.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for(int i=1; i<list.size(); i++) {
                                HashMap<String, Object> a = list.get(i);
                                System.out.println();
                                LatLng temp = new LatLng(Double.parseDouble((String)a.get("lat")),
                                        Double.parseDouble((String)a.get("longt")));
                                Marker temps = mMap.addMarker(new MarkerOptions().position(temp).title((String) list.get(i).get("name")));
                                temps.setSnippet((String)list.get(i).get("addr"));
                                temps.setTag(i);


                            }
                        }
                    });
                }
            }.start();
        }

        public void setting(View v){
            Intent it = new Intent(this, SettingsActivity.class);
            it.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(it);
        }

        public void list(View v){
            Intent it = new Intent(this, MainActivity.class);
            it.putExtra("addr1",addr1);
            it.putExtra("addr2",addr2);

            startActivity(it);
        }
        SharedPreferences pref;
        private void getPreferences(){

            addr1 = pref.getString("sync_frequency", "");
            addr2 = pref.getString("sync_frequency2", "");
        }








}


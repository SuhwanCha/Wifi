package kr.gdb.wifi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static android.R.id.list;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private api loc;
    LatLng dimigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loc = new api();
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        mMap = googleMap;
        dimigo = new LatLng(37.341893,126.8315179);
        mMap.addMarker(new MarkerOptions().position(dimigo).title("한국디지털미디어고등학교"))
        .setSnippet("경기도 안산시 단원구 사세충열로 94");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dimigo, 16));

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

    public boolean onMarkerClick (Marker marker){
        int index = (int) marker.getTag();
        return true;
    }



}


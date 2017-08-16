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
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        dimigo = new LatLng(37.341893,126.8315179);
        mMap.addMarker(new MarkerOptions().position(dimigo).title("asd"))
                .setIcon(BitmapDescriptorFactory.fromResource((R.drawable.dimigo)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dimigo, 15));
        try {
            addMarker(loc.parseData());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }


    private void addMarker(ArrayList<HashMap> list) {
        ArrayList<LatLng> markers;
        for(int i=0; i<list.size(); i++){
            LatLng temp = new LatLng((double)(list.get(i).get("lat"))
                    ,(double)list.get(i).get("longt"));
            mMap.addMarker(new MarkerOptions().position(temp).title((String)list.get(i).get("name")));
        }
    }


}

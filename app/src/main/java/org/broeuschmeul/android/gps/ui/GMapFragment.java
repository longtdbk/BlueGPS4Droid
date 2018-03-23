package org.broeuschmeul.android.gps.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.broeuschmeul.android.gps.bluetooth.provider.R;

import static org.broeuschmeul.android.gps.bluetooth.provider.R.id.map;

/**
 * Created by Admin on 09/03/2018.
 */
public class GMapFragment extends Fragment implements OnMapReadyCallback, OnMarkerClickListener,GoogleMap.OnMapClickListener {
    private Activity activity;
    private GoogleMap mMap;
    double longitude, latitude;
    private int currentInd;
    private final int[] MAP_TYPES = new int []{GoogleMap.MAP_TYPE_NORMAL,GoogleMap.MAP_TYPE_SATELLITE,GoogleMap.MAP_TYPE_HYBRID,GoogleMap.MAP_TYPE_TERRAIN};
    FloatingActionButton btnMapType;
    Marker mMarker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_view, container, false);
        activity = getActivity();
        //map = ((MapFragment)activity.getFragmentManager().findFragmentById(R.id.map)).getMap();
        MapFragment mapFragment = (MapFragment) activity.getFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        //mMarker = new Marker();
        btnMapType = (FloatingActionButton)v.findViewById(R.id.btMapType);
        return v;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        getLocation();
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);

        btnMapType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentInd ++;
                if(currentInd > MAP_TYPES.length-1){currentInd = 0;}
                mMap.setMapType(MAP_TYPES[currentInd]);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MapFragment f = (MapFragment)getActivity().getFragmentManager()
                .findFragmentById(map);
        if (f != null)
            getActivity().getFragmentManager().beginTransaction().remove(f).commit();
    }


    public void getLocation(){
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // new version must check permission :)
        try {
            Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (currentLocation == null){
                currentLocation = new Location("");
                currentLocation.setLatitude(21.0345);
                currentLocation.setLongitude(105.827);
            }
            longitude = currentLocation.getLongitude();
            latitude = currentLocation.getLatitude();
            LatLng myPos = new LatLng(latitude,longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    myPos, 16));
        }catch(SecurityException e){

        }

    }


    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
//            Toast.makeText(activity,
//                    marker.getTitle() +
//                            " has been clicked " + clickCount + " times.",
//                    Toast.LENGTH_SHORT).show();
        }

        displayAddLocNoteDialog();
        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }


    private void displayAddLocNoteDialog(){
        View messageView = activity.getLayoutInflater().inflate(R.layout.location_note_item, null, false);
        // we need this to enable html links
        TextView textViewLat = (TextView) messageView.findViewById(R.id.txtLatEdit);
        textViewLat.setText(textViewLat.getText() + "12222");
        //textView.setMovementMethod(LinkMovementMethod.getInstance());
        // When linking text, force to always use default color. This works
        // around a pressed color state bug.
        int defaultColor = textViewLat.getTextColors().getDefaultColor();
        textViewLat.setTextColor(defaultColor);
        TextView textViewLong = (TextView) messageView.findViewById(R.id.txtLongEdit);
        textViewLong.setText(textViewLong.getText() + "12334");
        textViewLong.setTextColor(defaultColor);

        AlertDialog  alertDialog = new AlertDialog.Builder(activity).create();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //builder.setTitle(R.string.about_title);
        //builder.setIcon(R.drawable.gplv3_icon);
        alertDialog.setTitle(R.string.about_title);
        alertDialog.setView(messageView);
        alertDialog.setIcon(R.drawable.gplv3_icon);
        //builder.setView(messageView);
        //builder.show();
        alertDialog.setButton( Dialog.BUTTON_POSITIVE, "Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(activity,
                        "Submit success",
                        Toast.LENGTH_SHORT).show();
            }
        });


        alertDialog.setButton( Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener()    {
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(activity,
                            "Cancel success",
                            Toast.LENGTH_SHORT).show();
                }});

        alertDialog.show();
    }

    public float getDistance(LatLng from, LatLng to) {
        float distance = 0;
        if (to != null) {
            Location loc1 = new Location("");
            loc1.setLatitude(from.latitude);
            loc1.setLongitude(from.longitude);

            Location loc2 = new Location("");
            loc2.setLatitude(to.latitude);
            loc2.setLongitude(to.longitude);

            distance = loc1.distanceTo(loc2);
        }
        return distance;
    }


    @Override
    public void onMapClick(LatLng latLng) {
        LatLng currentLatLng = new LatLng(latitude,longitude);
        // distance must in 100m around user position
        if(getDistance(currentLatLng,latLng)<=100) {
            if (mMarker!=null) {
                mMarker.remove();
            }
            mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("New Marker"));
        }
    }
}
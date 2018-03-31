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
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
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

import org.broeuschmeul.android.gps.SharedInfo;
import org.broeuschmeul.android.gps.bluetooth.provider.R;
import org.broeuschmeul.android.gps.db.util.LocNoteController;

import static org.broeuschmeul.android.gps.bluetooth.provider.R.id.map;

;

/**
 * Created by Admin on 09/03/2018.
 */
public class GMapFragment extends Fragment implements OnMapReadyCallback, OnMarkerClickListener,GoogleMap.OnMapClickListener {
    private Activity activity;
    private GoogleMap mMap;
    double longitude, latitude;
    private int currentInd;
    private final int[] MAP_TYPES = new int []{GoogleMap.MAP_TYPE_NORMAL,GoogleMap.MAP_TYPE_SATELLITE,GoogleMap.MAP_TYPE_HYBRID,GoogleMap.MAP_TYPE_TERRAIN};
    ImageButton btnCreate;
    ImageButton btnMapChange;
    ImageButton btnShowInfo;
    Marker mMarker;
    private LocNoteController locNoteController;
    private EditText editNote;
    PopupWindow mPopupWindow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_view, container, false);
        activity = getActivity();
        locNoteController = new LocNoteController(activity);
        locNoteController.open();
        MapFragment mapFragment = (MapFragment) activity.getFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        //mMarker = new Marker();
        btnCreate = (ImageButton)v.findViewById(R.id.btCreate);
        btnShowInfo = (ImageButton)v.findViewById(R.id.btShowInfo);
        btnMapChange = (ImageButton)v.findViewById(R.id.btMapChange);


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


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAddLocNoteDialog();
            }
        });

        btnShowInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp();
            }
        });

        btnMapChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentInd ++;
                if(currentInd > MAP_TYPES.length-1){currentInd = 0;}
                mMap.setMapType(MAP_TYPES[currentInd]);
            }
        });


        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    while(true) {
                        synchronized (this) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    getLocation();
                                }
                            });
                            wait(5000);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
        };
        thread.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MapFragment f = (MapFragment)getActivity().getFragmentManager()
                .findFragmentById(map);
        if (f != null)
            getActivity().getFragmentManager().beginTransaction().remove(f).commit();
        if (mPopupWindow!= null) {
            mPopupWindow.dismiss();
        }
    }


    public void getLocation(){
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // new version must check permission :)
        try {
            Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (currentLocation == null) {
                currentLocation = new Location("");
                currentLocation.setLatitude(21.0345);
                currentLocation.setLongitude(105.827);
            }
            longitude = currentLocation.getLongitude();
            latitude = currentLocation.getLatitude();
            LatLng myPos = new LatLng(latitude,longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    myPos, 18));


        }catch(SecurityException e){
            Log.d("GPSBlue","Error " + e.getMessage());
        }

    }



    public void setLocation(){
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // new version must check permission :)
        try {
            longitude = SharedInfo.getSelf().getLongitude();
            latitude = SharedInfo.getSelf().getLattitude();
            LatLng myPos = new LatLng(latitude,longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    myPos, 18));
        }catch(Exception e){
            Log.d("GPSBlue","Error " + e.getMessage());
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
        showPopUp();
        //displayAddLocNoteDialog();
        //RelativeLayout mRelativeLayout  = (RelativeLayout)activity.findViewById(R.id.rl);


        return false;
    }


    private void displayAddLocNoteDialog(){
        latitude = SharedInfo.getSelf().getLattitude();
        longitude = SharedInfo.getSelf().getLongitude();
        String txtLat = String.valueOf(latitude);
        String txtLon = String.valueOf(longitude);



        View messageView = activity.getLayoutInflater().inflate(R.layout.location_note_item, null, false);

        editNote = (EditText) messageView.findViewById(R.id.editTextNote);
        // we need this to enable html links
        TextView textViewLat = (TextView) messageView.findViewById(R.id.txtLatEdit);
        textViewLat.setText(textViewLat.getText() + txtLat);

        int defaultColor = textViewLat.getTextColors().getDefaultColor();
        textViewLat.setTextColor(defaultColor);
        TextView textViewLong = (TextView) messageView.findViewById(R.id.txtLongEdit);
        textViewLong.setText(textViewLong.getText() + txtLon);
        textViewLong.setTextColor(defaultColor);

        final AlertDialog  alertDialog = new AlertDialog.Builder(activity).create();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        alertDialog.setTitle("Add");
        alertDialog.setView(messageView);
        alertDialog.setIcon(R.drawable.gplv3_icon);

        alertDialog.setButton( Dialog.BUTTON_POSITIVE, "Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String note = editNote.getText().toString();
                locNoteController.createLocationNote(latitude,longitude, note);
                Toast.makeText(activity,
                        "Add success",
                        Toast.LENGTH_SHORT).show();
            }
        });


        alertDialog.setButton( Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener()    {
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(activity,
                            "Cancel",
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

    public void showPopUp(){
        View customView = activity.getLayoutInflater().inflate(R.layout.custom_layout,null);
        mPopupWindow = new PopupWindow(
                customView,
                ViewPager.LayoutParams.WRAP_CONTENT,
                ViewPager.LayoutParams.WRAP_CONTENT
        );
        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }

        ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);
        TextView txtView = (TextView) customView.findViewById(R.id.tv);
        latitude = SharedInfo.getSelf().getLattitude();
        longitude = SharedInfo.getSelf().getLongitude();
        double accuracy = SharedInfo.getSelf().getAccuracy();
        txtView.setText("Latitude:" + latitude + " --- Longitude:" + longitude + " -- Accuracy: " + accuracy);
        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.showAtLocation(getView(), Gravity.LEFT,-60,-200);

    }
}

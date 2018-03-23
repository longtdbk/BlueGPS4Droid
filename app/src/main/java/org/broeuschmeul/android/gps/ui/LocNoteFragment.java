package org.broeuschmeul.android.gps.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.broeuschmeul.android.gps.adapter.LocNoteAdapter;
import org.broeuschmeul.android.gps.bluetooth.provider.R;
import org.broeuschmeul.android.gps.db.util.LocNoteController;
import org.broeuschmeul.android.gps.db.util.LocationNote;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.broeuschmeul.android.gps.bluetooth.provider.BluetoothGpsProviderService.PREF_TRACK_FILE_DIR;
import static org.broeuschmeul.android.gps.bluetooth.provider.BluetoothGpsProviderService.PREF_TRACK_FILE_PREFIX;

/**
 * Created by admin on 3/23/2018.
 */

public class LocNoteFragment extends Fragment {

    private static final String LOG_TAG = "BlueGPS";


    private Activity activity;
    private LocNoteController locNoteController;
    private LocNoteAdapter adapter;
    private PrintWriter writer;
    private File trackFile;
    private boolean preludeWritten = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_loc_note_list, container, false);
        activity = getActivity();

        locNoteController = new LocNoteController(activity);
        ListView listLocNote = (ListView)v.findViewById(R.id.list_loc_note);
        adapter = new LocNoteAdapter(getActivity().getApplicationContext(), inflater, getData());
        listLocNote.setAdapter(adapter);

        listLocNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>=0) {
                    LocationNote locationNote = (LocationNote)parent.getItemAtPosition(position);
                    //long locNoteID = parent.getItemIdAtPosition(position);
                    long locNoteID = locationNote.getID();
                    double latitude = locationNote.getLatitude();
                    double longitude = locationNote.getLongitude();
                    String note = locationNote.getNote();
                    displayAddLocNoteDialog(locNoteID,latitude,longitude,note);
                }
            }
        });
        return v;
    }

    private List<LocationNote> getData(){
        LocNoteController locNoteController = new LocNoteController(getContext());
        locNoteController.open();
        List<LocationNote> locNoteList = new ArrayList<>();
        locNoteList.add(new LocationNote("111",1.3,2.5,"Test 1"));
        locNoteList.add(new LocationNote("222",1.3,2.5,"Test 2"));
        locNoteList.add(new LocationNote("333",1.3,2.5,"Test 3"));
        locNoteList.add(new LocationNote("444",1.3,2.5,"Test 4"));
        //List<LocationNote> locNoteList = locNoteController.getAllLocationNotes();
        //List<LocationNote> locationNotes = locNoteController.getAllLocationNotes();
        //locNoteList.addAll(locationNotes);
        locNoteController.close();
        return locNoteList;
    }


    private void displayAddLocNoteDialog(final long rowID, final double latitude, final double longitude, final String note){
        View messageView = activity.getLayoutInflater().inflate(R.layout.location_note_item, null, false);

        TextView textViewLat = (TextView) messageView.findViewById(R.id.txtLatEdit);
        textViewLat.setText(textViewLat.getText() + String.valueOf(latitude));

        int defaultColor = textViewLat.getTextColors().getDefaultColor();
        textViewLat.setTextColor(defaultColor);
        TextView textViewLong = (TextView) messageView.findViewById(R.id.txtLongEdit);
        textViewLong.setText(textViewLong.getText() +  String.valueOf(longitude));
        textViewLong.setTextColor(defaultColor);

        EditText editTextNote = (EditText) messageView.findViewById(R.id.editTextNote);
        editTextNote.setText(note);
        editTextNote.setTextColor(defaultColor);

        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
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
                String result = locNoteController.updateLocationNote(rowID,longitude,latitude,note);
                Toast.makeText(activity,
                        result,
                        Toast.LENGTH_SHORT).show();
            }
        });


        alertDialog.setButton( Dialog.BUTTON_NEGATIVE, "Delete", new DialogInterface.OnClickListener()    {
            public void onClick(DialogInterface dialog, int which) {
                String result =locNoteController.deleteLocationNote(rowID);
                Toast.makeText(activity,
                        result,
                        Toast.LENGTH_SHORT).show();
            }});

        alertDialog.show();
    }

    private void writeLocNoteFile(){
        SimpleDateFormat fmt = new SimpleDateFormat("_yyyy-MM-dd_HH-mm-ss'.loc'");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        String trackDirName = sharedPreferences.getString(PREF_TRACK_FILE_DIR, this.getString(R.string.defaultTrackFileDirectory));
        String trackFilePrefix = sharedPreferences.getString(PREF_TRACK_FILE_PREFIX, this.getString(R.string.defaultTrackFilePrefix));
        trackFile = new File(trackDirName,trackFilePrefix+fmt.format(new Date()));
        Log.d(LOG_TAG, "Writing the Location Note file: "+trackFile.getAbsolutePath());
        File trackDir = trackFile.getParentFile();

        List<LocationNote> locNoteList = locNoteController.getAllLocationNotes();
        try {
            if ((! trackDir.mkdirs()) && (! trackDir.isDirectory())){
                Log.e(LOG_TAG, "Error while creating parent dir of NMEA file: "+trackDir.getAbsolutePath());
            }
            writer = new PrintWriter(new BufferedWriter(new FileWriter(trackFile)));
            String dataWrite = "";
            for (int i = 0; i < locNoteList.size(); i++) {
                LocationNote locationNote = locNoteList.get(i);
                String time = locationNote.getTime();
                String longitude = String.valueOf(locationNote.getLongitude());
                String latitude = String.valueOf(locationNote.getLatitude());
                String note = String.valueOf(locationNote.getNote());
                dataWrite = time + ","+latitude + "," + longitude + "," + note +"\n";
                writer.print(dataWrite);
            }
            writer.close();
            //preludeWritten = true;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while writing the Location Note file: "+trackFile.getAbsolutePath(), e);
        }
    }
}

package org.broeuschmeul.android.gps.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.broeuschmeul.android.gps.SharedInfo;
import org.broeuschmeul.android.gps.bluetooth.provider.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatusFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatusFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView textLatLon;
    TextView textAltitude;
    TextView textSpeed;
    TextView textCourseOverGround;
    TextView textUTM;
    TextView textAccuracy;
    TextView textTime;
    private Activity activity;

    private OnFragmentInteractionListener mListener;

    public StatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatusFragment newInstance(String param1, String param2) {
        StatusFragment fragment = new StatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_status, container, false);
        activity = getActivity();
        textLatLon = (TextView)(v.findViewById(R.id.txtLatLon));
        textAltitude = (TextView)(v.findViewById(R.id.txtAltitude));
        textTime = (TextView)(v.findViewById(R.id.txtTime));
        textAccuracy = (TextView)(v.findViewById(R.id.txtAccuracy));
        textSpeed = (TextView)(v.findViewById(R.id.txtSpeed));
        textLatLon = (TextView)(v.findViewById(R.id.txtLatLon));
        Handler handler = new Handler();
//        Runnable updateViewRunable= new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    setTextTime();
//                    setTextLatLon(SharedInfo.getSelf().getLattitude(), SharedInfo.getSelf().getLongitude());
//                    setTextSpeed(SharedInfo.getSelf().getSpeed());
//                    setTextAccuracy(SharedInfo.getSelf().getAccuracy());
//                    setTextAltitude(SharedInfo.getSelf().getAltitude());
//                } catch (Exception ex) {
//                    Log.d("GPSBlue","Error in update View:" + ex.getMessage());
//                }
//            }};
//        handler.postDelayed(updateViewRunable,1000);

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    while(true) {
                        synchronized (this) {
                            wait(1000);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setTextTime();
                                    setTextLatLon(SharedInfo.getSelf().getLattitude(), SharedInfo.getSelf().getLongitude());
                                    setTextSpeed(SharedInfo.getSelf().getSpeed());
                                    setTextAccuracy(SharedInfo.getSelf().getAccuracy());
                                    setTextAltitude(SharedInfo.getSelf().getAltitude());

                                }
                            });
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            };
        };
        thread.start();


//        activity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    while(true) {
//                        Thread.sleep(1000);
//                        setTextTime();
//                        setTextLatLon(SharedInfo.getSelf().getLattitude(), SharedInfo.getSelf().getLongitude());
//                        setTextSpeed(SharedInfo.getSelf().getSpeed());
//                        setTextAccuracy(SharedInfo.getSelf().getAccuracy());
//                        setTextAltitude(SharedInfo.getSelf().getAltitude());
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    while(true) {
//                        sleep(1000);
//                        setTextTime();
//                        setTextLatLon(SharedInfo.getSelf().getLattitude(), SharedInfo.getSelf().getLongitude());
//                        setTextSpeed(SharedInfo.getSelf().getSpeed());
//                        setTextAccuracy(SharedInfo.getSelf().getAccuracy());
//                        setTextAltitude(SharedInfo.getSelf().getAltitude());
//                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        thread.start();

//        Thread updateViewThread = new Thread(updateViewRunable);
//        updateViewThread.start();


        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void setTextView(double lat, double lon, double speed, double altitude, double utm, double cog){
        textLatLon.setText(String.valueOf(lat+"/n"+lon));
        textSpeed.setText(String.valueOf(speed));
        textAltitude.setText(String.valueOf(altitude));
        textUTM.setText(String.valueOf(utm));
        textCourseOverGround.setText(String.valueOf(cog));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = sdf.format(new Date());
        textTime.setText(currentDateTime);

    }

    public void setTextLatLon(double lat, double lon){
        textLatLon.setText(String.valueOf(lat+"/"+lon));
    }

    public void setTextSpeed(double speed){
        textSpeed.setText(String.valueOf(speed));
    }

    public void setTextAltitude(double altitude){
        textAltitude.setText(String.valueOf(altitude));
    }

    public void setTextUTM(double utm){
        textUTM.setText(String.valueOf(utm));
    }

    public void setTextCOG(double cog){
        textCourseOverGround.setText(String.valueOf(cog));
    }

    public void setTextAccuracy(double accuracy){
        textAccuracy.setText(String.valueOf(accuracy));
    }

    public void setTextTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = sdf.format(new Date());
        textTime.setText(currentDateTime);
    }

}

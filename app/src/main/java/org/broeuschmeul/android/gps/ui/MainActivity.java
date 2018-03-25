package org.broeuschmeul.android.gps.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.broeuschmeul.android.gps.SharedInfo;
import org.broeuschmeul.android.gps.adapter.SectionPagerAdapter;
import org.broeuschmeul.android.gps.bluetooth.provider.BluetoothGpsActivity;
import org.broeuschmeul.android.gps.bluetooth.provider.R;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private SectionPagerAdapter mSectionsPagerAdapter;
    private StatusFragment statusFragment;
    private GMapFragment gMapFragment;
    private LocNoteFragment locNoteFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        gMapFragment = new GMapFragment();
        statusFragment = new StatusFragment();
        locNoteFragment = new LocNoteFragment();
        mSectionsPagerAdapter.addFrag(gMapFragment, "MAP");
        mSectionsPagerAdapter.addFrag(statusFragment, "STATUS");
        mSectionsPagerAdapter.addFrag(locNoteFragment, "LOCATION LIST");
        mViewPager = (ViewPager) findViewById(R.id.container);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);

        SharedInfo sharedInfo = new SharedInfo();
        sharedInfo.mainActivity = this;
    }

    public StatusFragment getStatusFragment(){
        return statusFragment;
    }

    public GMapFragment getMapFragment(){
        return gMapFragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //respond to menu item selection
        switch (item.getItemId()) {
            case R.id.setting:
                startActivity(new Intent(this, BluetoothGpsActivity.class));
                return true;
//            case R.id.help:
//                startActivity(new Intent(this, BluetoothGpsActivity.class));
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

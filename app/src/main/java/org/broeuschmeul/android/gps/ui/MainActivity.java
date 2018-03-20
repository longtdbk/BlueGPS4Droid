package org.broeuschmeul.android.gps.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.broeuschmeul.android.gps.bluetooth.provider.BluetoothGpsActivity;
import org.broeuschmeul.android.gps.bluetooth.provider.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            case R.id.about:
                startActivity(new Intent(this, BluetoothGpsActivity.class));
                return true;
            case R.id.help:
                startActivity(new Intent(this, BluetoothGpsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

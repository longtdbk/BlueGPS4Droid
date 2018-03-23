package org.broeuschmeul.android.gps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.broeuschmeul.android.gps.bluetooth.provider.R;
import org.broeuschmeul.android.gps.db.util.LocationNote;

import java.util.List;

/**
 * Created by Admin on 3/23/2018.
 */

public class LocNoteAdapter extends ArrayAdapter {
    private Context mContext;
    private LayoutInflater inflate;
    private List<LocationNote> datas;

    public LocNoteAdapter(Context context, LayoutInflater inflate, List<LocationNote> locationNotes) {
        super(context, 0, locationNotes);
        mContext=context;
        this.inflate = inflate;
        this.datas = locationNotes;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final LocationNote locationNote = (LocationNote)getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_loc_note, parent, false);
        }
        // Lookup view for data population
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTimeItem);
        TextView tvLat = (TextView) convertView.findViewById(R.id.tvLatItem);
        TextView tvLong = (TextView) convertView.findViewById(R.id.tvLongItem);
        TextView tvNote = (TextView) convertView.findViewById(R.id.tvNoteItem);

        tvLat.setText(String.valueOf(locationNote.getLatitude()));
        tvLong.setText(String.valueOf(locationNote.getLongitude()));
        tvTime.setText(locationNote.getTime());
        tvNote.setText(locationNote.getNote());
        //int temp = position%5+1;
        //imgIcon.setImageResource(mContext.getResources().getIdentifier(command.getImg(), "mipmap", mContext.getPackageName()));

        // Return the completed view to render on screen
        return convertView;
    }
    @Override
    public long getItemId(int position) {
        LocationNote locationNote = datas.get(position);
        if(locationNote!=null){
            return locationNote.getID();
        }
        return 0;
    }

}

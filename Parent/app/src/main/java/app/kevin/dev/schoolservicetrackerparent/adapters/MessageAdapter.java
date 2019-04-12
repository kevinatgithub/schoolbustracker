package app.kevin.dev.schoolservicetrackerparent.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.kevin.dev.schoolservicetrackerparent.R;
import app.kevin.dev.schoolservicetrackerparent.models.AppMessage;

public class MessageAdapter extends ArrayAdapter<AppMessage> {
    public MessageAdapter(@NonNull Context context, ArrayList<AppMessage> appMessages) {
        super(context, 0, appMessages);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AppMessage appMessage = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_row,parent,false);
        }

        CardView cvInComming = convertView.findViewById(R.id.cvInComming);
        TextView lblInCommingContent = convertView.findViewById(R.id.lblInCommingContent);
        TextView lblInCommingTimestamp = convertView.findViewById(R.id.lblInCommingTimestamp);
        CardView cvOutGoing = convertView.findViewById(R.id.cvOutGoing);
        TextView lblOutGoingContent = convertView.findViewById(R.id.lblOutGoingContent);
        TextView lblOutGoingTimestamp = convertView.findViewById(R.id.lblOutGoingTimestamp);
        if(appMessage.isInComming()){
            cvInComming.setVisibility(View.VISIBLE);
            cvOutGoing.setVisibility(View.GONE);
            lblInCommingContent.setText(appMessage.getContent());
            lblInCommingTimestamp.setText(appMessage.getTimestamp());
        }else{
            cvOutGoing.setVisibility(View.VISIBLE);
            cvInComming.setVisibility(View.GONE);
            lblOutGoingContent.setText(appMessage.getContent());
            lblOutGoingTimestamp.setText(appMessage.getTimestamp());
        }
        return convertView;
    }
}

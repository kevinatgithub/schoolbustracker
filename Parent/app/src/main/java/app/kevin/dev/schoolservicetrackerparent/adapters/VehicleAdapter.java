package app.kevin.dev.schoolservicetrackerparent.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import app.kevin.dev.schoolservicetrackerparent.R;
import app.kevin.dev.schoolservicetrackerparent.models.Vehicle;

public class VehicleAdapter extends ArrayAdapter<Vehicle> {

    public VehicleAdapter(@NonNull Context context, ArrayList<Vehicle> vehicles) {
        super(context, 0, vehicles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Vehicle vehicle = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.vehicle_row,parent,false);
        }

        ImageView imgStatus = convertView.findViewById(R.id.imgStatus);
        TextView lblStatus = convertView.findViewById(R.id.lblStatus);
        TextView lblStatusDescription = convertView.findViewById(R.id.lblStatusDescription);
        TextView lblPlateNo = convertView.findViewById(R.id.lblPlateNo);
        TextView lblDriver = convertView.findViewById(R.id.lblDriver);
        TextView lblModel = convertView.findViewById(R.id.lblModel);
        TextView lblDistance = convertView.findViewById(R.id.lblDistance);
        TextView lblTime = convertView.findViewById(R.id.lblTime);

        lblPlateNo.setText(vehicle.getPlate_no());
        lblDriver.setText(vehicle.getDriver());
        lblModel.setText(vehicle.getModel());

        if(vehicle.getStatus() != null){
            switch(vehicle.getStatus().toUpperCase()){
                case "IN TRANSIT":
                    imgStatus.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_in_transit));
                    lblStatus.setText("IN TRANSIT");
                    lblStatus.setTextColor(convertView.getResources().getColor(android.R.color.holo_green_dark));
                    lblStatusDescription.setText("This vehicle is in transit to your home or is already heading back to school.");
                    break;
                case "IN TRAFFIC":
                    imgStatus.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_in_traffic));
                    lblStatus.setText("IN TRAFFIC");
                    lblStatus.setTextColor(convertView.getResources().getColor(android.R.color.black));
                    lblStatusDescription.setText("This vehicle is in traffic and may take a while before ariving in your area.");
                    break;
                case "IN DISTRESS":
                    imgStatus.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_in_distress));
                    lblStatus.setText("IN DESTRESS");
                    lblStatus.setTextColor(convertView.getResources().getColor(android.R.color.holo_orange_light));
                    lblStatusDescription.setText("This vehicle is having a problem such as a flat tire.");
                    break;
                case "IN SCHOOL":
                    imgStatus.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_in_school));
                    lblStatus.setText("IN SCHOOL");
                    lblStatus.setTextColor(convertView.getResources().getColor(android.R.color.holo_blue_light));
                    lblStatusDescription.setText("This vehicle is in school vicinity.");
                    break;
            }
        }else{
            imgStatus.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_info));
            lblStatus.setText("NOT AVAILABLE");
            lblStatus.setTextColor(convertView.getResources().getColor(android.R.color.holo_blue_light));
            lblStatusDescription.setText("The vehicle status is not available at the moment.");
        }

        return convertView;
    }
}

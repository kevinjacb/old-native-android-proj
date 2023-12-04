package com.example.uber;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static java.lang.Math.round;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.ViewHolder> {
    //Context context;
    ArrayList<LatLng> locations;
    ArrayList<Double> distances;
    ArrayList<String> addressess;
    public DriverAdapter(ArrayList<DriverClass.LocationNode> locationNodes){
        this.distances = new ArrayList<Double>();
        this.addressess = new ArrayList<>();
        locations = new ArrayList<>();
        for (DriverClass.LocationNode locationNode : locationNodes){
            distances.add(locationNode.returnDistance());
            addressess.add(locationNode.returnAddress());
            locations.add(locationNode.returnPosition());
        }
    }



    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_view_components,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.address.setText("Address : "+addressess.get(position));
        double distance = round(distances.get(position) * 100);
        distance = distance/100;
        String distanceDis = ((distances.get(position) < 1000)?String.valueOf(distance) + " meters": String.valueOf((float)(round(distance/10)/100.0)) + " kilometers");
        holder.distance.setText("Distance : "+distanceDis);
    }

    @Override
    public int getItemCount() {
        return distances.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView distance, address;
        ImageView peek;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            peek = itemView.findViewById(R.id.peek);
            distance = itemView.findViewById(R.id.distance);
            address = itemView.findViewById(R.id.address);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), addressess.get(getAdapterPosition()), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(itemView.getContext(),MapsActivity2.class);
            intent.putExtra("latitude",locations.get(getAdapterPosition()).latitude);
            intent.putExtra("longitude",locations.get(getAdapterPosition()).longitude);
            intent.putExtra("address", "Destination: " + addressess.get(getAdapterPosition()));
            v.getContext().startActivity(intent);
        }
    }
}

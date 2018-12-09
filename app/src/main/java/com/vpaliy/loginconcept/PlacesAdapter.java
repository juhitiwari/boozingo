package com.vpaliy.loginconcept;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vpaliy.loginconcept.Interfaces.List;

/**
 * Created by slytherin on 22/10/18.
 */

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {
    private java.util.List<Places> Places;
    public static int id;
    PlacesAdapterOnClickHandler mClickHandler;
    Context context;
    public interface PlacesAdapterOnClickHandler {
        void onClick(int rate);
    }
    public PlacesAdapter(java.util.List<Places> Places,Context context) {
        this.Places = Places;this.context=context;
    }



    @Override
    public PlacesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.places_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlacesAdapter.ViewHolder holder, int position) {
         //id=Places.get(position).getMid();
        holder.mPlaces.setText(Places.get(position).getMname());
        String rate =Places.get(position).getMrating().toString();
        holder.mRating.setText(rate);
        holder.mType.setText(Places.get(position).getMtype().toString());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id2=holder.getAdapterPosition();
                id=Places.get(id2).getMid();
                Log.i("W4K","Click-"+id2);
                context.getApplicationContext().startActivity(new Intent(context.getApplicationContext(),DetailActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK));  // <--- here
            }
        });

    }

    @Override
    public int getItemCount() {
        return Places.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mPlaces,mRating,mType;
        public View view;


        public ViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            mPlaces = (TextView) itemView.findViewById(R.id.place);
            mRating = (TextView) itemView.findViewById(R.id.rating);
           mType=(TextView) itemView.findViewById(R.id.type);

        }


        @Override
        public void onClick(View v) {

        }
    }


}
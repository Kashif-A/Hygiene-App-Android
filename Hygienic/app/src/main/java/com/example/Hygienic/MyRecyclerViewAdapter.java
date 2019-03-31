package com.example.Hygienic;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.recyclerapp.R;

import java.util.List;

/**
 * RecyclerView adapter to generate views for each Restaurant in the list.
 * Inflates recyclerview_row.xml and binds restaurants to it.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<Restaurant> restaurants;
    private List<String> ratingImages;
    private LayoutInflater layoutInflater;
    private Context context;

    // Data is passed into the constructor
    MyRecyclerViewAdapter(Context context, List<Restaurant> restaurants, List<String> imageURLs) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.restaurants = restaurants;
        this.ratingImages = imageURLs;
    }

    // Inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // Binds the data to the Views in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        holder.businessTitle.setText(restaurant.getBusinessName());
        holder.businessDetails.setText(FormatRestaurantDetails.formatDetails(restaurant));

        ratingImages = GetCorrectRatingImage.generateRatingImagesForRecyclerView(restaurants);
        Glide.with(context)
                .asBitmap()
                .load(ratingImages.get(position))
                .into(holder.ratingImage);
    }

    // Total number of rows
    @Override
    public int getItemCount() {
        return restaurants.size();
    }


    // Stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView businessTitle;
        TextView businessDetails;
        ImageView ratingImage;
        ImageView divider;

        ViewHolder(View itemView) {
            super(itemView);
            businessTitle = itemView.findViewById(R.id.businessTitle);
            businessTitle.setTextSize(25);
            businessTitle.setTypeface(null, Typeface.BOLD_ITALIC);
            businessTitle.setTextColor(Color.rgb(250,250,25));

            ratingImage = itemView.findViewById(R.id.ratingImage);

            businessDetails = itemView.findViewById(R.id.businessDetails);
            businessDetails.setTextSize(14);
            businessDetails.setTypeface(null, Typeface.BOLD_ITALIC);
            businessDetails.setTextColor(Color.rgb(250,250,25));

            divider = itemView.findViewById(R.id.divider);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {}
    }
}
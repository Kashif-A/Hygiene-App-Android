package com.example.Hygienic;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.recyclerapp.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Restaurant> restaurants = new ArrayList<>();
    private MapView mapView;
    private boolean currentView = true; // To allow ImageButton to function properly
    private List<String> latLng = new ArrayList<>();

    private MyRecyclerViewAdapter adapter; // For RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_api_key));
        setContentView(R.layout.activity_main);

        // OnClickListener for 'View Toggle' button
        ImageButton viewToggler = findViewById(R.id.toggleView);
        viewToggler.setBackgroundResource(R.drawable.listview); // Empty by default to allow changes at run-time
        viewToggler.setOnClickListener(v -> {

            if(currentView){ // Check if base view active
                // Make everything invisible except RecyclerView
                findViewById(R.id.mainLayout).setVisibility(View.INVISIBLE);
                viewToggler.setBackgroundResource(R.drawable.ukmap);
                // Set up the RecyclerView
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setBackgroundColor(Color.argb(100,0,0,0));
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                adapter = new MyRecyclerViewAdapter(MainActivity.this, restaurants,
                        GetCorrectRatingImage.generateRatingImagesForRecyclerView(restaurants));
                recyclerView.setAdapter(adapter);
            } else {
                viewToggler.setBackgroundResource(R.drawable.listview);
                findViewById(R.id.recyclerView).setVisibility(View.INVISIBLE);
                findViewById(R.id.mainLayout).setVisibility(View.VISIBLE);
            }
            currentView = !currentView;
        });

        // RadioButtons (Search by ID/Name/Location)
        RadioButton postcodeRB = findViewById(R.id.postcodeRB);
        RadioButton businessnameRB = findViewById(R.id.businessnameRB);
        RadioButton locationRB = findViewById(R.id.locationRB);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(mapboxMap -> {
            // Setting and animating starting camera position to (Manchester, UK)
            CameraPosition position = new CameraPosition.Builder()
                    .target(new LatLng(53.47, -2.239))
                    .zoom(6).bearing(0).build(); // Creates a CameraPosition from the builder
            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 4000);

            // Get screen height to display LoadToast object in the centre
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int height = size.y;
            LoadToast lt = new LoadToast(MainActivity.this);
            lt.setText("Getting hygiene ratings...");
            lt.setTranslationY(height / 2);

            // OnClickListener for 'Search' button
            Button searchButton = findViewById(R.id.searchButton);
            searchButton.setOnClickListener((View v) -> {
                lt.show();
                String userInput = ((TextView) findViewById(R.id.input)).getText().toString();
                mapboxMap.clear();
                restaurants.clear();
                // Get API results based on whether user selected 'postcode', 'name' or 'location' and put results in 'restaurants'
                // AsyncTask used
                if (postcodeRB.isChecked())
                    restaurants = AccessDAO.getAPIResponse("postcode", userInput);
                else if (businessnameRB.isChecked())
                    restaurants = AccessDAO.getAPIResponse("name", userInput);
                else if (locationRB.isChecked())
                    restaurants = AccessDAO.getAPIResponse("location", userInput);
                else {
                    ToastMaker.showToast("Please select a search type.", null, this);
                }
                if (!restaurants.isEmpty()) {
                    // Separate overlapping markers
                    for (int i = 0; i < 5; i++) {
                        restaurants = PreventMarkersFromOverlapping.preventOverlapping(restaurants);
                    }
                    updateMap(mapboxMap, restaurants, lt, null, null);
                } else if (restaurants.isEmpty() && postcodeRB.isChecked() || businessnameRB.isChecked() || locationRB.isChecked()) {
                    ToastMaker.showToast("No Results Found", null, this);
                }
                lt.error();
            });

            // OnClickListener for 'Show Most Recent' button. Allows user to get most recently added Restaurants
            Button showMostRecent = findViewById(R.id.mostRecent);
            showMostRecent.setOnClickListener(v -> {
                mapboxMap.clear();
                restaurants.clear();
                restaurants = AccessDAO.getAPIResponse("mostRecent", null);
                if (!restaurants.isEmpty()) {
                    // Separate overlapping markers
                    for (int i = 0; i < 5; i++) {
                        restaurants = PreventMarkersFromOverlapping.preventOverlapping(restaurants);
                    }
                    updateMap(mapboxMap, restaurants, lt, null, null);
                }
                // If no results returned through result (i.e bad search string), show Toast to let user know no results found
                else if (locationRB.isChecked() || businessnameRB.isChecked() || locationRB.isChecked() && restaurants.isEmpty()) {
                    ToastMaker.showToast("No Results Found", null, this);
                }
            });

            // Get API response based on current location
            ImageButton currentLocationSearch = findViewById(R.id.currentLocationButton);
            currentLocationSearch.getBackground().setAlpha(0);
            currentLocationSearch.setOnClickListener((View v) -> {
                mapboxMap.clear();
                restaurants.clear();
                if (!latLng.isEmpty()){
                    addCurrentLocationMarker(mapboxMap, Double.parseDouble(latLng.get(0)), Double.parseDouble(latLng.get(1)));
                }

                // Get permission for dangerous permissions from user
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            "android.permission.ACCESS_FINE_LOCATION",
                            "android.permission.ACCESS_COARSE_LOCATION"}, 1);
                } else {
                    // Get current location and if API responds with data, populate map, else let user know something went wrong.
                    LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 20, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            Double lat = location.getLatitude();
                            Double lng = location.getLongitude();

                            addCurrentLocationMarker(mapboxMap, lat, lng);

                            latLng.clear();
                            latLng.add(String.valueOf(lat));
                            latLng.add(String.valueOf(lng));

                            lt.show();
                            // Get restaurants based on location search.
                            restaurants = AccessDAO.getAPIResponse("location", lat + "," + lng);
                            if (!restaurants.isEmpty()) {
                                // Separate overlapping markers
                                for (int i = 0; i < 5; i++) {
                                    restaurants = PreventMarkersFromOverlapping.preventOverlapping(restaurants);
                                }
                                mapboxMap.moveCamera(CameraUpdateFactory.zoomTo(10));
                                updateMap(mapboxMap, restaurants, lt, String.valueOf(lat), String.valueOf(lng));
                            } else {
                                lt.error();
                                ToastMaker.showToast("Something went wrong.Please try again!", null, MainActivity.this);
                            }
                            lm.removeUpdates(this);
                        }
                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {}
                        @Override
                        public void onProviderEnabled(String provider) {}
                        @Override
                        public void onProviderDisabled(String provider) {}
                    });
                }
            });
        });
    }

    /**
     * Get permissions for location access so user can search restaurants by current location.
     */
    // Get location permissions when activity starts
    @Override
    public void onStart() {
        super.onStart();
        // Prevent keyboard from appearing when app starts
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mapView.onStart();
    }

    /**
     * This method uses restaurant list to generate marker. One marker per restaurant.
     *
     * @param map reference to current MapBox instance.
     * @param restaurants list of restaurants generated through API response.
     * @param lt Toast to update user on current status.
     */
    private void updateMap(MapboxMap map, List<Restaurant> restaurants, LoadToast lt, String lat, String lng) {

        // Build LatLngBounds to ensure all markers fit within the screen
        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();

        // For current location search only!
        if (lat != null)
            latLngBuilder.include(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));

        for (int i = 0; i < restaurants.size(); i++) {
            String ratingValue = restaurants.get(i).getRatingValue();

            latLngBuilder.include(new LatLng(restaurants.get(i).getLatitude(),
                    restaurants.get(i).getLongitude()));

            IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
            map.addMarker(new MarkerOptions()
                    .icon(getCustomMarker(
                            Character.getNumericValue(ratingValue.charAt(ratingValue.length() - 1)), iconFactory))
                    .position(new LatLng(restaurants.get(i).getLatitude(),
                            restaurants.get(i).getLongitude()))
                    .title(restaurants.get(i).getBusinessName().toUpperCase() + "\n")
                    .snippet(FormatRestaurantDetails.formatDetails(restaurants.get(i))));
        }
        map.animateCamera(CameraUpdateFactory.zoomTo(5));
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(
                latLngBuilder.build(), 100,100,100,100));
        // Generate info window for each marker
        generateInfoWindowForMarkers(map);
        map.addOnCameraIdleListener(() -> lt.success());
    }

    /**
     * This method generates info window for each marker containing business information
     * and rating image.
     *
     * @param map current reference to the MapBox instance.
     */
    private void generateInfoWindowForMarkers(MapboxMap map) {
        map.setInfoWindowAdapter(marker -> {
            // Main layout for holding all business information & rating image
            LinearLayout infoWindow = new LinearLayout(MainActivity.this);
            infoWindow.setOrientation(LinearLayout.VERTICAL);
            infoWindow.setPadding(25, 20, 25, 40);
            infoWindow.setBackgroundColor(Color.rgb(220, 220, 220));

            // Rating image for the info window based on hygiene rating
            GetCorrectRatingImage getRatingImage = new GetCorrectRatingImage(this);
            ImageView ratingImage = null;

            if (marker.getSnippet().contains("EXEMPT")){
                ratingImage = getRatingImage.getRatingImage("G");
            } else if (marker.getSnippet().contains("RATING")){  // Extracting rating value from snippet is slightly complex.
                ratingImage = getRatingImage.getRatingImage(String.valueOf(
                        marker.getSnippet().substring(marker.getSnippet().indexOf("RATING")).charAt(13)));
            }

            // Business name
            TextView title = new TextView(this);
            title.setText(marker.getTitle());
            title.setTextColor(Color.BLACK);
            title.setTypeface(null, Typeface.BOLD_ITALIC);

            // Business details i.e address, rating value etc
            TextView businessInfo = new TextView(this);
            businessInfo.setText(marker.getSnippet());
            businessInfo.setTextColor(Color.BLACK);

            // Finalize and generate info window
            infoWindow.addView(title);
            infoWindow.addView(businessInfo);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(480, 200);
            layoutParams.gravity = Gravity.CENTER;
            if (ratingImage != null){
                ratingImage.setLayoutParams(layoutParams);
                infoWindow.addView(ratingImage);
            }

            return infoWindow;
        });
    }

    /**
     * Method to get custom marker for map depending on rating value.
     * @param rating Restaurant hygiene rating.
     * @param iconFactory helps generate Icon object that represents the image of a marker.
     * @return Icon object representing custom marker image.
     */
    private Icon getCustomMarker(int rating, IconFactory iconFactory){
        if (rating > 5){
            rating = -1;
        }
        switch (rating){
            case -1:
                return iconFactory.fromResource(R.drawable.exemptmarker);
            case 0:
                return iconFactory.fromResource(R.drawable.zeromarker);
            case 1:
                return iconFactory.fromResource(R.drawable.onemarker);
            case 2:
                return iconFactory.fromResource(R.drawable.twomarker);
            case 3:
                return iconFactory.fromResource(R.drawable.threemarker);
            case 4:
                return iconFactory.fromResource(R.drawable.fourmarker);
            case 5:
                return iconFactory.fromResource(R.drawable.fivemarker);
            default:
                return null;
        }
    }

    private void addCurrentLocationMarker(MapboxMap map, Double lat, Double lng){
        // Add marker indicating current user location
        IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
        map.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .title("CURRENT LOCATION")
                .snippet("\nLatitude: " + String.valueOf(lat).substring(0,6) +
                        "\nLongitude" + String.valueOf(lng).substring(0,6)));
    }
}
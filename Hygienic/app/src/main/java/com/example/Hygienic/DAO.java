package com.example.Hygienic;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO object to get API data from 'http://sandbox.kriswelsh.com/hygieneapi'.
 *
 * @author  Kashif Ahmed - 18061036
 * @version 1.0
 * @since   March 2019
 */
public class DAO extends AsyncTask<String, Void, List<Restaurant>> {
    private String searchType;
    private String urlString;

    // Constructor
    public DAO(String searchType){
        this.searchType = searchType;
    }

    /**
     * @exception JSONException Thrown to indicate a problem with the JSON API.
     * @return List<Restaurant> object containing all the restaurants served by the API.
     */
    @Override
    protected List<Restaurant> doInBackground (String... search) {

        // Get API response by choosing appropriate URL based on searchType
        if (searchType.equals("postcode")){

            urlString = "http://sandbox.kriswelsh.com/hygieneapi/hygiene.php?op=search_postcode&postcode="+search[0];
            return getDataToMakeRestaurants(new ArrayList<>(), urlString, false);

        } else if (searchType.equals("name")){

            urlString = "http://sandbox.kriswelsh.com/hygieneapi/hygiene.php?op=search_name&name="+search[0];
            return getDataToMakeRestaurants(new ArrayList<>(), urlString, false);

        } else if (searchType.equals("location")){

            String lat = search[0].substring(0,search[0].indexOf(",")-1);
            String lng = search[0].substring(search[0].indexOf(",")+1);
            urlString = "http://sandbox.kriswelsh.com/hygieneapi/hygiene.php?op=search_location&lat=" + lat + "&long=" + lng;
            return getDataToMakeRestaurants(new ArrayList<>(), urlString, true);

        } else if (searchType.equals("mostRecent")){

            urlString = "http://sandbox.kriswelsh.com/hygieneapi/hygiene.php?op=show_recent";
            return getDataToMakeRestaurants(new ArrayList<>(), urlString, false);

        }
        return null;
    }

    private List<Restaurant> getDataToMakeRestaurants(List<Restaurant> restaurants, String urlString, boolean searchingByLocation){
        try{
            // Get response from API based on determined URL
            URLConnection connection = new URL(urlString).openConnection();
            InputStreamReader ins = new InputStreamReader(connection.getInputStream());
            BufferedReader in = new BufferedReader(ins);

            restaurants.clear();
            // Read the input stream and get API response
            String responseBody = "";
            String line = "";
            while ((line = in.readLine()) != null) {
                responseBody = responseBody + line;
            }

            if (searchingByLocation){ // Location has DistanceKM, other searches don't
                // Turn API response into JSON object and use the created JSON object to produce Restaurant objects
                JSONArray fullResponse = new JSONArray(responseBody);
                for(int i=0; i < fullResponse.length(); i++) {
                    JSONObject restaurantAsJSON = fullResponse.getJSONObject(i);
                    JSONObject location = restaurantAsJSON.getJSONObject("Location");
                    restaurants.add(new Restaurant(restaurantAsJSON.getString("id"),
                            restaurantAsJSON.getString("BusinessName"),
                            restaurantAsJSON.getString("AddressLine1"),
                            restaurantAsJSON.getString("AddressLine2"),
                            restaurantAsJSON.getString("AddressLine3"),
                            restaurantAsJSON.getString("PostCode"),
                            restaurantAsJSON.getString("RatingValue"),
                            restaurantAsJSON.getString("RatingDate"),
                            restaurantAsJSON.getString("DistanceKM"),
                            location.getString("Latitude"),
                            location.getString("Longitude")));
                }
            } else { // No DistanceKM so different constructor needed
                // Turn API response into JSON object and use the created JSON object to produce Restaurant objects
                JSONArray fullResponse = new JSONArray(responseBody);
                for (int i = 0; i < fullResponse.length(); i++) {
                    JSONObject restaurantAsJSON = fullResponse.getJSONObject(i);
                    JSONObject location = restaurantAsJSON.getJSONObject("Location");
                    restaurants.add(new Restaurant(restaurantAsJSON.getString("id"),
                            restaurantAsJSON.getString("BusinessName"),
                            restaurantAsJSON.getString("AddressLine1"),
                            restaurantAsJSON.getString("AddressLine2"),
                            restaurantAsJSON.getString("AddressLine3"),
                            restaurantAsJSON.getString("PostCode"),
                            restaurantAsJSON.getString("RatingValue"),
                            restaurantAsJSON.getString("RatingDate"),
                            location.getString("Latitude"),
                            location.getString("Longitude")));
                }
            }
        } catch(JSONException e){
            e.printStackTrace();
        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return restaurants;
    }
}
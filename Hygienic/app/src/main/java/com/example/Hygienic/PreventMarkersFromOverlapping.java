package com.example.Hygienic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class prevents markers overlapping that are too close together or share the same
 * latitude and longitude. Ensures user has access to all the markers.
 *
 * @author  Kashif Ahmed - 18061036
 * @version 1.0
 * @since   March 2019
 */
public class PreventMarkersFromOverlapping {

    /**
     * This method takes the current list of restaurants, sorts them, then compares restaurants
     * to find ones that are too close together and separates the distance between restaurants
     * that are too close.
     *
     * @param restaurants to allow checking whether Latitude/Longitude are too close.
     * @return List<Restaurant> object containing updated Lat/Lng that have been separated if too close.
     */
    public static List<Restaurant> preventOverlapping(List<Restaurant> restaurants) {
        SortedMap<Double, Restaurant> sortedMap = new TreeMap<>();
        Double[] lat = new Double[restaurants.size()];
        List<Restaurant> separatedLatLng = new ArrayList<>();
        sortedMap.clear();
        separatedLatLng.clear();
        for (int i = 0; i < restaurants.size(); i++){
            restaurants.get(i).setLatitude(
                    restaurants.get(i).getLatitude() +
                    ThreadLocalRandom.current().nextDouble(0.000001,0.000009));

            sortedMap.put(restaurants.get(i).getLatitude(), restaurants.get(i));

            lat[i] = restaurants.get(i).getLatitude();
        }
        Arrays.sort(lat);
        for (int i=0; i < lat.length -1; i++){
            if ((lat[i+1]-lat[i]) < 0.0012){
                sortedMap.get(lat[i]).setLatitude(lat[i] +
                        ThreadLocalRandom.current().nextDouble(-0.0006,0.0002));
                sortedMap.get(lat[i]).setLongitude(sortedMap.get(lat[i]).getLongitude() +
                        ThreadLocalRandom.current().nextDouble(0.0004,0.0006));
                separatedLatLng.add(sortedMap.get(lat[i]));
            } else {
                separatedLatLng.add(sortedMap.get(lat[i]));
            }
        }
        Restaurant lastRestaurant = sortedMap.get(sortedMap.lastKey());
        lastRestaurant.setLatitude(
                lastRestaurant.getLatitude() + 0.0006
        );
        separatedLatLng.add(lastRestaurant);
        return separatedLatLng;
    }
}
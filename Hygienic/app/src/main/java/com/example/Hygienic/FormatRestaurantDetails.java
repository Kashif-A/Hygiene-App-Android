package com.example.Hygienic;

/**
 * This class allows formatting of API data to remove empty fields
 * giving a uniform and polished finish.
 *
 * @author  Kashif Ahmed - 18061036
 * @version 1.0
 * @since   March 2019
 */
public class FormatRestaurantDetails {

    /**
     * Method to create String containing business information by checking for any empty fields and removing them.
     * Results in better formatted business information and better user experience.
     *
     * @param restaurant Restaurant object.
     * @return restaurantDetails containing Address, Rating Value and Rating Date for the business.
     */
    public static String formatDetails(Restaurant restaurant){
        String stringToAppend;
        StringBuilder restaurantDetails = new StringBuilder();
        if (restaurant.getAddressLine1().length() > 1){
            stringToAppend = (restaurant.getAddressLine1()).replace(",","") + "\n";
            restaurantDetails.append(stringToAppend);
        }
        if (restaurant.getAddressLine2().length() > 1){
            stringToAppend = (restaurant.getAddressLine2()).replace(",","") + "\n";
            restaurantDetails.append(stringToAppend);
        }
        if (restaurant.getAddressLine3().length() > 1){
            stringToAppend = (restaurant.getAddressLine3()).replace(",","") + "\n";
            restaurantDetails.append(stringToAppend);
        }
        if (restaurant.getPostCode().length() > 1){
            stringToAppend = (restaurant.getPostCode()).replace(",","") + "\n\n";
            restaurantDetails.append(stringToAppend);
        }
        if (restaurant.getRatingValue().length() > 1){
            stringToAppend = (restaurant.getRatingValue().toUpperCase()).replace(",","") + "\n";
            restaurantDetails.append(stringToAppend);
        }
        if (restaurant.getRatingDate().length() > 1){
            stringToAppend = (restaurant.getRatingDate().toUpperCase()).replace(",","") + "\n\n";
            restaurantDetails.append(stringToAppend);
        }
        if (restaurant.getDistanceKM() != null) {
            stringToAppend = (restaurant.getDistanceKM().replace(",","") + "\n\n");
            restaurantDetails.append(stringToAppend);
        }
        return restaurantDetails.toString();
    }
}
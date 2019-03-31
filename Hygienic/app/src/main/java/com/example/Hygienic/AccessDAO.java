package com.example.Hygienic;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Helper class to prevent repeat or Try/Catch block because the
 * AsyncTask is called many times in the MainActivity. Reduces repeat code.
 *
 *  @author Kashif Ahmed - 18061036
 *  * @version 1.0
 *  * @since   March 2019
 */
public class AccessDAO {
    protected static List<Restaurant> getAPIResponse(String searchType, String userInput){

        try {
            return new DAO(searchType).execute(userInput).get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
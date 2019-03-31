package com.example.Hygienic;

/**
* Restaurant POJO.
*
* @author  Kashif Ahmed - 18061036
* @version 1.0
* @since   March 2019
*/
public class Restaurant {
    private String id;
    private String businessName;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String postCode;
    private String ratingValue;
    private String ratingDate;
    private String distanceKM;
    private Double latitude;
    private Double longitude;

    // Getters & setters
    public String getId() {

        return id;
    }
    public void setId(String id) {

        this.id = id;
    }
    public String getBusinessName() {

        return businessName;
    }
    public void setBusinessName(String businessName) {

        this.businessName = businessName;
    }
    public String getAddressLine1() {

        return addressLine1;
    }
    public void setAddressLine1(String addressLine1) {

        this.addressLine1 = addressLine1;
    }
    public String getAddressLine2() {

        return addressLine2;
    }
    public void setAddressLine2(String addressLine2) {

        this.addressLine2 = addressLine2;
    }
    public String getAddressLine3() {

        return addressLine3;
    }
    public void setAddressLine3(String addressLine3) {

        this.addressLine3 = addressLine3;
    }
    public String getPostCode() {

        return postCode;
    }
    public void setPostCode(String postCode) {

        this.postCode = postCode;
    }
    public String getRatingValue() {

        return ratingValue;
    }
    public void setRatingValue(String ratingValue) {

        this.ratingValue = ratingValue;
    }
    public String getRatingDate() {

        return ratingDate;
    }
    public void setRatingDate(String ratingDate) {

        this.ratingDate = ratingDate;
    }
    public String getDistanceKM() {

        return distanceKM;
    }
    public void setDistanceKM(String distanceKM) {

        this.distanceKM = distanceKM;
    }
    public Double getLatitude() {

        return latitude;
    }
    public void setLatitude(Double latitude) {

        this.latitude = latitude;
    }
    public Double getLongitude() {

        return longitude;
    }
    public void setLongitude(Double longitude) {

        this.longitude = longitude;
    }

    // Constructor without DistanceKM
    public Restaurant(String id, String businessName, String addressLine1, String addressLine2,
                      String addressLine3, String postCode, String ratingValue, String ratingDate,
                      String latitude, String longitude) {
        this.id = id;
        this.businessName = businessName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressLine3 = addressLine3;
        this.postCode = postCode;
        if (ratingValue.equals("-1")){
            this.ratingValue = "EXEMPT FROM RATING";
        } else {
            this.ratingValue =  "Rating:      " + ratingValue;
        }
        this.ratingDate = "Rating Date: " + ratingDate;
        this.latitude = Double.parseDouble(latitude);
        this.longitude = Double.parseDouble(longitude);
    }

    // Constructor with DistanceKM for Location search
    public Restaurant(String id, String businessName, String addressLine1, String addressLine2,
                      String addressLine3, String postCode, String ratingValue, String ratingDate,
                      String distanceKM, String latitude, String longitude) {
        this.id = id;
        this.businessName = businessName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressLine3 = addressLine3;
        this.postCode = postCode;
        if (ratingValue.equals("-1")){
            this.ratingValue = "EXEMPT FROM RATING";
        } else {
            this.ratingValue =  "Rating:      " + ratingValue;
        }
        this.ratingDate = "Rating Date: " + ratingDate;
        this.distanceKM = "DISTANCE FROM CURRENT LOCATION \n" + distanceKM.substring(0,4) + " KM";
        this.latitude = Double.parseDouble(latitude);
        this.longitude = Double.parseDouble(longitude);
    }

    @Override
    public String toString() {
        return "EatingEstablishment [id=" + id + ", businessName=" + businessName + ", addressLine1=" + addressLine1
                + ", addressLine2=" + addressLine2 + ", addressLine3=" + addressLine3 + ", postCode=" + postCode
                + ", ratingValue=" + ratingValue + ", ratingDate=" + ratingDate + ", distanceKM=" + distanceKM
                + ", latitude=" + latitude + ", longitude=" + longitude + "]";
    }
}
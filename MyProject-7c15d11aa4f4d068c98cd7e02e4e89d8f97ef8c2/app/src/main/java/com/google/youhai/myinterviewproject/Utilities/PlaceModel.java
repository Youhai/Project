package com.google.youhai.myinterviewproject.Utilities;


public class PlaceModel {
    // attributes
    private String description;
    private String reference;
    private String placeID;

    //constructor
    public PlaceModel(String desc, String rf){
        setDescription(desc);
        setReference(rf);
        placeID ="";
    }

    public PlaceModel(String desc, String rf, String pID){
        description =desc;
        reference = rf;
        placeID = pID;
    }




    public void setDescription(String description) {
        this.description = description;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getDescription() {
        return description;
    }

    public String getReference() {
        return reference;
    }

    public String getPlaceID() {
        return placeID;
    }
}

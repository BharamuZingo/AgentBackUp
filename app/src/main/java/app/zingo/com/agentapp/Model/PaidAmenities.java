package app.zingo.com.agentapp.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels.com on 15-11-2017.
 */

public class PaidAmenities implements Serializable {

    @SerializedName("PaidAmenitiesId")
    private int paidAmenityId;

    @SerializedName("PaidAmenitiesName")
    private String text;

    @SerializedName("Description")
    private String description;

    @SerializedName("Price")
    private int amenityRate;

    @SerializedName("Category")
    private String Category;

    @SerializedName("HotelId")
    private int hotelId;

    @SerializedName("PaidAmenitiesCategoriesId")
    private int PaidAmenitiesCategoriesId;

    @SerializedName("Availability")
    private int Availability;

    @SerializedName("Image")
    private String Image;

  /*  @SerializedName("HotelId")
    private int hotelId;*/



    //private boolean isSelected = false;

    public PaidAmenities(int paidAmenityId, String text, String description, int amenityRate, String Category, int hotelId,
                         int PaidAmenitiesCategoriesId) {
        this.paidAmenityId = paidAmenityId;
        this.text = text;
        this.description = description;
        this.amenityRate = amenityRate;
        this.hotelId = hotelId;
        this.Category = Category;
        this.PaidAmenitiesCategoriesId = PaidAmenitiesCategoriesId;
    }

    public void setPaidAmenityId(int paidAmenityId) {
        this.paidAmenityId = paidAmenityId;
    }

    public int getPaidAmenityId() {
        return paidAmenityId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getText() {
        return text;
    }

    public void setAmenityRate(int amenityRate) {
        this.amenityRate = amenityRate;
    }

    public int getAmenityRate() {
        return amenityRate;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getCategory() {
        return Category;
    }

    public void setPaidAmenitiesCategoriesId(int paidAmenitiesCategoriesId) {
        PaidAmenitiesCategoriesId = paidAmenitiesCategoriesId;
    }

    public int getPaidAmenitiesCategoriesId() {
        return PaidAmenitiesCategoriesId;
    }

    /*public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }*/


}

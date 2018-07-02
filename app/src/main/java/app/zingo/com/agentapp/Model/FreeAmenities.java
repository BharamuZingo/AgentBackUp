package app.zingo.com.agentapp.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZingoHotels.com on 3/15/2018.
 */

public class FreeAmenities {

    @SerializedName("AmenitiesId")
    private int AmenitiesId;
    @SerializedName("AmenitiesName")
    private String AmenitiesName;
    @SerializedName("Description")
    private String Description;
    @SerializedName("HotelId")
    private int HotelId;

    public int getAmenitiesId() {
        return AmenitiesId;
    }

    public void setAmenitiesId(int amenitiesId) {
        AmenitiesId = amenitiesId;
    }

    public String getAmenitiesName() {
        return AmenitiesName;
    }

    public void setAmenitiesName(String amenitiesName) {
        AmenitiesName = amenitiesName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getHotelId() {
        return HotelId;
    }

    public void setHotelId(int hotelId) {
        HotelId = hotelId;
    }
}

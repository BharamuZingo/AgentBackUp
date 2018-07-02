package app.zingo.com.agentapp.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by CSC on 11/13/2017.
 */

public class Maps implements Serializable {

    @SerializedName("MapId")
    private int MapId;

    @SerializedName("Latitude")
    private String Latitude;

    @SerializedName("Logitude")
    private String Logitude;

    @SerializedName("Location")
    private String Location;

    @SerializedName("HotelId")
    private int HotelId;

    @SerializedName("Hotels")
    private HotelDetails hotels;

    public int getMapId() {
        return MapId;
    }

    public void setMapId(int mapId) {
        MapId = mapId;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLogitude() {
        return Logitude;
    }

    public void setLogitude(String logitude) {
        Logitude = logitude;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public int getHotelId() {
        return HotelId;
    }

    public void setHotelId(int hotelId) {
        HotelId = hotelId;
    }

    public void setHotels(HotelDetails hotels) {
        this.hotels = hotels;
    }

    public HotelDetails getHotels() {
        return hotels;
    }
}

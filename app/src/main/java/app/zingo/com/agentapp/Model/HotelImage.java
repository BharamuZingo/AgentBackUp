package app.zingo.com.agentapp.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels.com on 4/18/2018.
 */

public class HotelImage implements Serializable{

    @SerializedName("HotelImageId")
    private int HotelImageId;

    @SerializedName("hotels")
    private HotelDetails hotels;

    @SerializedName("HotelId")
    private int HotelId;

    @SerializedName("Images")
    private String Images;

    @SerializedName("Image")
    private byte[] Image;

    @SerializedName("Caption")
    private String Caption;

    public int getHotelImageId() {
        return HotelImageId;
    }

    public void setHotelImageId(int hotelImageId) {
        HotelImageId = hotelImageId;
    }

    public HotelDetails getHotels() {
        return hotels;
    }

    public void setHotels(HotelDetails hotels) {
        this.hotels = hotels;
    }

    public int getHotelId() {
        return HotelId;
    }

    public void setHotelId(int hotelId) {
        HotelId = hotelId;
    }

    public String getImages() {
        return Images;
    }

    public void setImages(String images) {
        Images = images;
    }

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
        Image = image;
    }

    public String getCaption() {
        return Caption;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }
}

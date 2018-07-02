package app.zingo.com.agentapp.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZingoHotels Tech on 05-05-2018.
 */

public class HotelRoomAvailablity {

    @SerializedName("FromDate")
    private String fromdate;

    @SerializedName("ToDate")
    private String toDate;

    @SerializedName("HotelId")
    private int hotelId;

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }
}

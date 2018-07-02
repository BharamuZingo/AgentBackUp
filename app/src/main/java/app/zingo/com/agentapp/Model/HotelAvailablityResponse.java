package app.zingo.com.agentapp.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZingoHotels Tech on 05-05-2018.
 */

public class HotelAvailablityResponse {

    @SerializedName("TotalRoomSold")
    private int TotalRoomSold;

    @SerializedName("TotalRoomCount")
    private int TotalRoomCount;

    @SerializedName("TotalRoomAvailable")
    private int TotalRoomAvailable;

    public int getTotalRoomSold() {
        return TotalRoomSold;
    }

    public void setTotalRoomSold(int totalRoomSold) {
        TotalRoomSold = totalRoomSold;
    }

    public int getTotalRoomCount() {
        return TotalRoomCount;
    }

    public void setTotalRoomCount(int totalRoomCount) {
        TotalRoomCount = totalRoomCount;
    }

    public int getTotalRoomAvailable() {
        return TotalRoomAvailable;
    }

    public void setTotalRoomAvailable(int totalRoomAvailable) {
        TotalRoomAvailable = totalRoomAvailable;
    }
}

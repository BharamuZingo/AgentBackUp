package app.zingo.com.agentapp.Model;

/**
 * Created by ZingoHotels.com on 5/24/2018.
 */

public class HotelWithDistance {

    private HotelDetails hotelDetails;
    private double distance;

    public HotelWithDistance(HotelDetails hotelDetails, double distance) {
        this.hotelDetails = hotelDetails;
        this.distance = distance;
    }

    public HotelDetails getHotelDetails() {
        return hotelDetails;
    }

    public void setHotelDetails(HotelDetails hotelDetails) {
        this.hotelDetails = hotelDetails;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}

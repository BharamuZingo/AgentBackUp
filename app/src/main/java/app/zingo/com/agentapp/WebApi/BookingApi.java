package app.zingo.com.agentapp.WebApi;

import java.util.ArrayList;

import app.zingo.com.agentapp.Model.Bookings1;
import app.zingo.com.agentapp.Model.HotelAvailablityResponse;
import app.zingo.com.agentapp.Model.HotelRoomAvailablity;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels.com on 4/23/2018.
 */

public interface BookingApi {

    @POST("RoomBookings")
    Call<Bookings1> postBooking(@Header("Authorization") String authKey, @Body Bookings1 body);

    @POST("Agent/GetAvailabilityOfRoomBoookingOfParticular")
    Call<HotelAvailablityResponse> getHotelRoomAvailablitys(@Header("Authorization") String authKey, @Body HotelRoomAvailablity jsonObject);


    @GET("RoomBookings/GetRoomBookingByTravellerAgentId/{TravellerAgentId}")
    Call<ArrayList<Bookings1>> getBookingsByProfileId(@Header("Authorization") String authKey, @Path("TravellerAgentId") int TravellerAgentId);

    @GET("RoomBookings/GetRoomBookingByTravellerAgentId/{TravellerAgentId}")
    Call<ArrayList<Bookings1>> getBookingsByAgentId(@Header("Authorization") String authKey, @Path("ProfileId") int ProfileId);

    @PUT("RoomBookings/{id}")
    Call<String> updateBookingStatus(@Header("Authorization") String authKey,@Path("id") int id, @Body Bookings1 bookings1);
}

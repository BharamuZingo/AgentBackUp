package app.zingo.com.agentapp.WebApi;

import java.util.ArrayList;

/*import app.zingo.com.hotelmanagement.Model.HotelDetails;
import app.zingo.com.hotelmanagement.Util.API;*/
import app.zingo.com.agentapp.Model.AgentHotel;
import app.zingo.com.agentapp.Model.HotelDetails;
import app.zingo.com.agentapp.Utils.API;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels.com on 09-11-2017.
 */

public interface HotelOperations {

    @POST("Hotels/AddHotels")
    Call<HotelDetails> addHotel(@Header("Authorization") String authKey, @Body HotelDetails hotelDetails);

    @PUT("Hotels/UpdateHotels/{id}")
    Call<HotelDetails> updateHotel(@Header("Authorization") String authKey, @Path("id") int id, @Body HotelDetails hotelDetails);

    @GET("Profiles/GetHotelsByProfileId/{ProfileId}")
    Call<ArrayList<HotelDetails>> getHotelByProfileId(@Header("Authorization") String authKey, @Path("ProfileId") int ProfileId);

    @GET(API.HOTELS)
    Call<ArrayList<HotelDetails>> getHotel(@Header("Authorization") String authKey);

    @GET(API.HOTELS+"/{HotelId}")
    Call<HotelDetails> getHotelByHotelId(@Header("Authorization") String authKey, @Path("HotelId") int HotelId);

    @GET("Agent/GetHotelsById/{id}")
    Call<AgentHotel> getAgentHotelByHotelId(@Header("Authorization") String authKey, @Path("id") int id);
}

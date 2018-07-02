package app.zingo.com.agentapp.WebApi;

import app.zingo.com.agentapp.Model.HotelDetails;
import app.zingo.com.agentapp.Utils.API;

import java.util.ArrayList;

import app.zingo.com.agentapp.Utils.API;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by CSC on 12/8/2017.
 */

public interface HotelApi {

    //@POST("Hotels/AddHotels")
    //Call<HotelDetails> addHotel(@Body HotelDetails hotelDetails);
    //@PUT("Hotels/UpdateHotels/{id}")
    //Call<HotelDetails> updateHotel(@Path("id") int id, @Body HotelDetails hotelDetails);

    //@GET("Profiles/GetHotelsByProfileId/{ProfileId}")
    //Call<ArrayList<HotelDetails>> getHotelByProfileId(@Path("ProfileId") int ProfileId);

    @GET(API.HOTELS)
    Call<ArrayList<HotelDetails>> getHotel(@Header("Authorization") String authKey);

    @GET(API.HOTELSBYLOCALTY+"/{Localty}")
    Call<ArrayList<HotelDetails>> getHotelByLocalty(@Header("Authorization") String authKey,@Path("Localty") String Localty);

    @GET("Hotels/GetHotelsByLocaltyOrCity/{Localty}/{City}")
    Call<ArrayList<HotelDetails>> getHotelByLocaltyOrCity(@Header("Authorization") String authKey,@Path("Localty") String Localty,
                                                          @Path("City") String City);

    //@GET(API.HOTELS+"/{HotelId}")
    //Call<HotelDetails> getHotelByHotelId(@Path("HotelId") int HotelId);
}

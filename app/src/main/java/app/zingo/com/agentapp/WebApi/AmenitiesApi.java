package app.zingo.com.agentapp.WebApi;

import java.util.ArrayList;

import app.zingo.com.agentapp.Model.FreeAmenities;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels.com on 4/19/2018.
 */

public interface AmenitiesApi {

    @GET("Hotels/GetAmenitiesByHotelId/{HotelId}")
    Call<ArrayList<FreeAmenities>> getAmenitiesByHotelId(@Header("Authorization") String authKey, @Path("HotelId") int HotelId);
}

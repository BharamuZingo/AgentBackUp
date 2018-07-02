package app.zingo.com.agentapp.WebApi;

import java.util.ArrayList;

import app.zingo.com.agentapp.Model.Maps;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels.com on 10-01-2018.
 */

public interface MapApi {

    @POST("Maps/AddMaps")
    Call<Maps> addMapDetails(@Header("Authorization") String authKey, @Body Maps maps);



    @PUT("Maps/UpdateMaps/{id}")
    Call<Maps> updateMapDetails(@Header("Authorization") String authKey, @Path("id") int id, @Body Maps maps);

    @GET("Maps/GetMapsByHotelId/{HotelId}")
    Call<ArrayList<Maps>> getMapDetails(@Header("Authorization") String authKey, @Path("HotelId") int HotelId);


}

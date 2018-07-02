package app.zingo.com.agentapp.WebApi;

import java.util.ArrayList;


import app.zingo.com.agentapp.Model.HotelImage;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels.com on 4/18/2018.
 */

public interface UploadHotelImagesApi {

    @POST("HotelImages")
    Call<HotelImage> uploadImages(@Header("Authorization") String authKey, @Body HotelImage body);

    @GET("Hotels/GetHotelImagesByHotelId/{HotelId}")
    Call<ArrayList<HotelImage>> getHotelImages(@Header("Authorization") String authKey, @Path("HotelId") int HotelId);
}

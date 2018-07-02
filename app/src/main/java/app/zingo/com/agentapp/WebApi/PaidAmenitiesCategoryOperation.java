package app.zingo.com.agentapp.WebApi;

import java.util.ArrayList;

import app.zingo.com.agentapp.Model.PaidAmenitiesCategory;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels.com on 24-11-2017.
 */

public interface PaidAmenitiesCategoryOperation {

    @GET("PaidAmenitiesCategories")
    Call<ArrayList<PaidAmenitiesCategory>> getPaidAmenitiesCategories(@Header("Authorization") String authKey);

    @POST("PaidAmenitiesCategories/AddPaidAmenitiesCategory")
    Call<PaidAmenitiesCategory> addPaidAmenitiesCategories(@Header("Authorization") String authKey, @Body PaidAmenitiesCategory paidAmenitiesCategory);

    @PUT("PaidAmenitiesCategories/UpdatePaidAmenitiesCategory/{id}")
    Call<PaidAmenitiesCategory> updatePaidAmenitiesCategories(@Header("Authorization") String authKey, @Path("id") int id, @Body PaidAmenitiesCategory paidAmenitiesCategory);

    @DELETE("PaidAmenitiesCategories/DeletePaidAmenitiesCategory/{id}")
    Call<PaidAmenitiesCategory> deletePaidAmenitiesCategory(@Header("Authorization") String authKey, @Path("id") int id);

}

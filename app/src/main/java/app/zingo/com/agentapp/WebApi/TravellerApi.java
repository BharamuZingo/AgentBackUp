package app.zingo.com.agentapp.WebApi;

import java.util.ArrayList;

import app.zingo.com.agentapp.Model.Traveller;
import app.zingo.com.agentapp.Utils.API;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels Tech on 20-04-2018.
 */

public interface TravellerApi {

    @POST(API.TRAVELLER)
    Call<Traveller> addTraveler(@Header("Authorization") String authKey, @Body Traveller body);

    @GET(API.GET_TRAVELLER_BY_PHONE+"/{PhoneNumber}")
    Call<ArrayList<Traveller>> fetchTravelerByPhone(@Header("Authorization") String authKey, @Path("PhoneNumber") String PhoneNumber);

    @PUT("Travellers/{id}")
    Call<Traveller> updateTravellerDetails(@Header("Authorization") String authKey,@Path("id") int id,@Body Traveller body);

    @GET("Travellers/{id}")
    Call<Traveller> getTravellerDetails(@Header("Authorization") String authKey, @Path("id") int id);


}

package app.zingo.com.agentapp.WebApi;

import java.util.ArrayList;

import app.zingo.com.agentapp.Model.GSTDetails;
import app.zingo.com.agentapp.Utils.API;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by CSC on 11/16/2017.
 */

public interface GST {

    @GET(API.GET_GST_DETAILS_BY_HOTEL_ID+"/{HotelId}")
    Call<ArrayList<GSTDetails>> fetchGST(@Header("Authorization") String authKey, @Path("HotelId") int HotelId);

    @POST(API.GST_DETAILS)
    Call<GSTDetails> postGST(@Header("Authorization") String authKey, @Body GSTDetails body);

    @GET(API.GST_TAXES/*+"/{HotelId}"*/)
    Call<ArrayList<GSTDetails>> fetchGST_taxes(/*@Path("HotelId") int HotelId*/);

}

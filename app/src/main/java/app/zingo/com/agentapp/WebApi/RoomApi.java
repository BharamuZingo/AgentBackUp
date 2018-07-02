package app.zingo.com.agentapp.WebApi;


import app.zingo.com.agentapp.Model.Rooms;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels.com on 18-12-2017.
 */

public interface RoomApi {

    /*@PUT(API.ROOMS+"/UpdateRooms/{id}")
    Call<Rooms> updateRoom(@Header("Authorization") String authKey, @Path("id") int id, @Body Rooms rooms);*/



    @DELETE("Rooms/DeleteRooms/{id}")
    Call<Rooms> deleteRoom(@Header("Authorization") String authKey, @Path("id") int id);

    @GET("Rooms/{id}")
    Call<Rooms> getRoom(@Header("Authorization") String authKey, @Path("id") int id);
}

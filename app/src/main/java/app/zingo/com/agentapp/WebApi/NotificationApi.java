package app.zingo.com.agentapp.WebApi;

import java.util.ArrayList;

import app.zingo.com.agentapp.Model.HotelNotification;
import app.zingo.com.agentapp.Model.NotificationManager;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by ZingoHotels.com on 4/23/2018.
 */

public interface NotificationApi {

    @POST("Calculation/SendNotificationForMultipleDeviceByHotelId")
    Call<ArrayList<String>> sendnotificationToHotel(@Header("Authorization") String auth , @Body HotelNotification hotelNotification);

    @POST("Calculation/SendNotificationForAllDevice")
    Call<ArrayList<String>> sendnotificationToAll(@Header("Authorization") String auth , @Body HotelNotification hotelNotification);

    @POST("NotificationManagers")
    Call<NotificationManager> saveNotification(@Header("Authorization") String auth , @Body NotificationManager hotelNotification);

    @GET("NotificationManagers")
    Call<ArrayList<NotificationManager>> getNotification(@Header("Authorization") String authKey);

    @POST("Calculation/SendNotificationForMultipleDeviceByProfileId")
    Call<ArrayList<String>> sendnotificationToProfile(@Header("Authorization") String auth , @Body HotelNotification hotelNotification);
}

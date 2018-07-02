package app.zingo.com.agentapp.WebApi;

import java.util.ArrayList;

import app.zingo.com.agentapp.Model.ContactDetails;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels.com on 10-11-2017.
 */

public interface ContactOperations {

    @GET("Hotels/GetContactsByHotelId/{HotelId}")
    Call<ArrayList<ContactDetails>> getContactByHotelId(@Header("Authorization") String authKey, @Path("HotelId") int HotelId);

    @POST("Contacts/AddContacts")
    Call<ContactDetails> addContact(@Header("Authorization") String authKey, @Body ContactDetails contactDetails);

    @PUT("Contacts/UpdateContacts/{id}")
    Call<ContactDetails> updateContact(@Header("Authorization") String authKey, @Path("id") int id, @Body ContactDetails contactDetails);





    /*@GET("Contacts")
    Call<ArrayList<ContactDetails>> getContacts()*/
}

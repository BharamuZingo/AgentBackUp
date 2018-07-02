package app.zingo.com.agentapp.WebApi;



import java.util.ArrayList;

import app.zingo.com.agentapp.Model.UserRole;
import app.zingo.com.agentapp.Utils.API;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels.com on 08-12-2017.
 */

public interface UserRoleApi {

    @PUT(API.USER_ROLES+"/UserRoles/{id}")
    Call<UserRole> apiUpdateRole(@Header("Authorization") String authKey, @Path("id") int id, @Body UserRole userRole);

    @POST(API.USER_ROLES+"/AddUserRole")
    Call<UserRole> apiAddRole(@Header("Authorization") String authKey, @Body UserRole userRole);

    @GET(API.USER_ROLES+"/GetUserRoleByUniqueId/{UserRoleUniqueId}")
    Call<UserRole> apiGetRolesByUniqueId(@Header("Authorization") String authKey, @Path("UserRoleUniqueId") String uniqueId);

    @GET(API.USER_ROLES)
    Call<ArrayList<UserRole>> apiGetRoles(@Header("Authorization") String authKey);

    @GET(API.USER_ROLES+"/{id}")
    Call<UserRole> apiGetRolesById(@Header("Authorization") String authKey, @Path("id") int id);

    @DELETE(API.USER_ROLES+"/DeleteUserRole/{id}")
    Call<UserRole> apiDeleteUserRole(@Header("Authorization") String authKey, @Path("id") int id);
}

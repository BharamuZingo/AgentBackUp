package app.zingo.com.agentapp.WebApi;

import app.zingo.com.agentapp.Model.DeviceMapping;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by ZingoHotels Tech on 02-05-2018.
 */

public interface DeviceMappingApi {

    @POST("AgentDeviceMappings")
    Call<DeviceMapping> addProfileMap(@Body DeviceMapping body);
}

package app.zingo.com.agentapp.Utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by root on 22/6/17.
 */

public class ApiClient {

    public static final String BASE_URL = "http://zingohotels.azurewebsites.net/api/";//"http://zingowebapp.azurewebsites.net/api/"; //"http://zingohotelswebapi.azurewebsites.net/api/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

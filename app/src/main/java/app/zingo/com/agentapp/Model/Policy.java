package app.zingo.com.agentapp.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZingoHotels.com on 10-11-2017.
 */

public class Policy implements Serializable {

    @SerializedName("PolicyId")
    private int PolicyId;
    @SerializedName("HotelPolicy")
    private String HotelPolicy;
    @SerializedName("StandardCancellationPolicy")
    private String StandardCancellationPolicy;
    @SerializedName("HotelServices")
    private String HotelServices;
    @SerializedName("HotelId")
    private int HotelId;
    @SerializedName("Hotels")
    ArrayList<HotelDetails> Hotels;

    public Policy()
    {

    }

    public Policy(int PolicyId, String HotelPolicy, String StandardCancellationPolicy, String HotelServices,
                  int HotelId, ArrayList<HotelDetails> Hotels)
    {
        this.PolicyId = PolicyId;
        this.HotelPolicy = HotelPolicy;
        this.StandardCancellationPolicy = StandardCancellationPolicy;
        this.HotelServices = HotelServices;
        this.HotelId = HotelId;
        this.Hotels = Hotels;
    }

    public void setPolicyId(int PolicyId) {
        PolicyId = PolicyId;
    }

    public int getPolicyId() {
        return PolicyId;
    }

    public void setHotelPolicy(String HotelPolicy) {
        HotelPolicy = HotelPolicy;
    }

    public String getHotelPolicy() {
        return HotelPolicy;
    }

    public void setStandardCancellationPolicy(String StandardCancellationPolicy) {
        StandardCancellationPolicy = StandardCancellationPolicy;
    }

    public String getStandardCancellationPolicy() {
        return StandardCancellationPolicy;
    }

    public void setHotelServices(String HotelServices) {
        HotelServices = HotelServices;
    }

    public String getHotelServices() {
        return HotelServices;
    }

    public void setHotelId(int hotelId) {
        HotelId = hotelId;
    }

    public int getHotelId() {
        return HotelId;
    }

    public void setHotels(ArrayList<HotelDetails> hotels) {
        Hotels = hotels;
    }

    public ArrayList<HotelDetails> getHotels() {
        return Hotels;
    }
}

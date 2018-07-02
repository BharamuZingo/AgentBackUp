package app.zingo.com.agentapp.Model;

/**
 * Created by Guest User on 05/12/2017.
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//


public class City implements Serializable{
    @SerializedName("CityId")
    private int CityId;
    @SerializedName("CityName")
    private String CityName;
    @SerializedName("State")
    private String State;
    @SerializedName("Country")
    private String Country;
    @SerializedName("ProfileId")
    private int ProfileId;

    public City(int CityId,String CityName,String State,String Country,int ProfileId)
    {
        this.CityId = CityId;
        this.CityName = CityName;
        this.State = State;
        this.Country = Country;
        this.ProfileId = ProfileId;
    }
    public City(){

    }

    public void setCityId(int CityId) {
        this.CityId = CityId;
    }

    public int getCityId() {
        return CityId;
    }

    public void setCityName(String CityName) {
        this.CityName = CityName;
    }

    public String getCityName() {
        return CityName;
    }

    public void setState(String State) {
        this.State = State;
    }

    public String getState() {
        return State;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }

    public String getCityCountry() {
        return Country;
    }

    public void setProfileId(int ProfileId) {
        this.ProfileId = ProfileId;
    }

    public int getProfileId() {
        return ProfileId;
    }
}
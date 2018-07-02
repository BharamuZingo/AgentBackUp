package app.zingo.com.agentapp.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZingoHotels.com on 24-11-2017.
 */

public class PaidAmenitiesCategory implements Serializable {

    @SerializedName("PaidAmenitiesCategoryId")
    private int PaidAmenitiesCategoryId;

    @SerializedName("Description")
    private String Description;

    @SerializedName("PaidAmenitiesList")
    private ArrayList<PaidAmenities> PaidAmenitiesList;

    public PaidAmenitiesCategory()
    {

    }

    public PaidAmenitiesCategory(int PaidAmenitiesCategoryId, String Description)
    {
        this.PaidAmenitiesCategoryId = PaidAmenitiesCategoryId;
        this.Description = Description;
        //this.PaidAmenitiesList = PaidAmenitiesList;
    }

    public PaidAmenitiesCategory(int PaidAmenitiesCategoryId, String Description, ArrayList<PaidAmenities> PaidAmenitiesList)
    {
        this.PaidAmenitiesCategoryId = PaidAmenitiesCategoryId;
        this.Description = Description;
        this.PaidAmenitiesList = PaidAmenitiesList;
    }


    public void setPaidAmenitiesCategoryId(int paidAmenitiesCategoryId) {
        PaidAmenitiesCategoryId = paidAmenitiesCategoryId;
    }

    public int getPaidAmenitiesCategoryId() {
        return PaidAmenitiesCategoryId;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDescription() {
        return Description;
    }


    public void setPaidAmenitiesList(ArrayList<PaidAmenities> paidAmenitiesList) {
        PaidAmenitiesList = paidAmenitiesList;
    }

    public ArrayList<PaidAmenities> getPaidAmenitiesList() {
        return PaidAmenitiesList;
    }

}

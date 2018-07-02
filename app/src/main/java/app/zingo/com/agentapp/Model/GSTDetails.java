package app.zingo.com.agentapp.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CSC on 11/16/2017.
 */

public class GSTDetails {
    @SerializedName("GSTDetailsId")
    private int GSTDetailsId;
    @SerializedName("GSTNumber")
    private String GSTNumber;
    @SerializedName("Name")
    private String Name;

    @SerializedName("Address")
    private String Address;
    @SerializedName("State")
    private String State;
    @SerializedName("PinCode")
    private String PinCode;

    @SerializedName("ContactNo")
    private String ContactNo;
    @SerializedName("Email")
    private String Email;
    @SerializedName("Designation")
    private String Designation;

    @SerializedName("IsExtraGuestChargesToDeclaredRates")
    private boolean IsExtraGuestChargesToDeclaredRates;
    @SerializedName("HotelId")
    private int HotelId;
    @SerializedName("Hotels")
    private HotelDetails Hotels;

    public int getGSTDetailsId() {
        return GSTDetailsId;
    }

    public void setGSTDetailsId(int GSTDetailsId) {
        this.GSTDetailsId = GSTDetailsId;
    }

    public String getGSTNumber() {
        return GSTNumber;
    }

    public void setGSTNumber(String GSTNumber) {
        this.GSTNumber = GSTNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public boolean getIsExtraGuestChargesToDeclaredRates() {
        return IsExtraGuestChargesToDeclaredRates;
    }

    public void setIsExtraGuestChargesToDeclaredRates(boolean isExtraGuestChargesToDeclaredRates) {
        IsExtraGuestChargesToDeclaredRates = isExtraGuestChargesToDeclaredRates;
    }

    public int getHotelId() {
        return HotelId;
    }

    public void setHotelId(int hotelId) {
        HotelId = hotelId;
    }

    public HotelDetails getHotels() {
        return Hotels;
    }

    public void setHotels(HotelDetails hotels) {
        Hotels = hotels;
    }
}

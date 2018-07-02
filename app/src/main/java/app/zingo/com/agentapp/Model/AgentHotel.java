package app.zingo.com.agentapp.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZingoHotels Tech on 25-04-2018.
 */

public class AgentHotel implements Serializable {

    @SerializedName("HotelId")
    private int hotelId;

    @SerializedName("HotelName")
    private String hotelName;

    @SerializedName("AmenitiesList")
    private ArrayList<FreeAmenities> aminetiesList;

    @SerializedName("paidAmenityList")
    private ArrayList<PaidAmenities> paidAmenityList;

    @SerializedName("policies")
    private ArrayList<Policy> policies;

    @SerializedName("IsApproved")
    private boolean isApproved;

    @SerializedName("AccountsInfo")
    private BankDetails AccountsInfo;

    @SerializedName("PlaceName")
    private String hotelStreetAddress;

    @SerializedName("Country")
    private String country;

    @SerializedName("Localty")
    private String localty;

    @SerializedName("State")
    private String state;

    @SerializedName("City")
    private String city;

    @SerializedName("PinCode")
    private String pincode;

/*
    @SerializedName("DetailedCntacts")
    private BankDetails DetailedCntacts;
*/


   /* @SerializedName("Addresses")
    private BankDetails Addresses;*/

   /* @SerializedName("room")
    private ArrayList rooms;*/

    @SerializedName("maps")
    private ArrayList<Maps> maps;

    @SerializedName("DisplayName")
    private String hotelDisplayName;

    @SerializedName("HotelType")
    private String hotelType;

    @SerializedName("StarRating")
    private String starRating;

    @SerializedName("ChainId")
    private int chainId;

    @SerializedName("ChainName")
    private String chainName;

    @SerializedName("BuiltYear")
    private String hotelBuiltYear;

    @SerializedName("NoOfRestaurant")
    private String noofRestuarentsInHotel;

    @SerializedName("NoOfRooms")
    private String noOfRoomsInHotel;

    @SerializedName("NoOfFloors")
    private String noOfFloorsInHotel;

    @SerializedName("Currency")
    private String currencyAccepted;

    @SerializedName("VCCCurrency")
    private String vccCurrencyAccepted;

    @SerializedName("CheckInTime")
    private String standardCheckInTime;

    @SerializedName("CheckOutTime")
    private String standardCheckOutTime;

    @SerializedName("Timezone")
    private String hotelTimeZone;

    @SerializedName("Is24HourCheckIn")
    private boolean is24HourCheckIn;

    @SerializedName("document")
    private ArrayList<HotelDocuments> hotelDocuments;

    @SerializedName("ProfileId")
    private int userId;


     @SerializedName("bookingList")
     private Bookings1 bookingList;

    @SerializedName("AlloweNoOfHoursForCheckInAndCheckOut")
    private int AlloweNoOfHoursForCheckInAndCheckOut;

    @SerializedName("hotelImage")
    private ArrayList<HotelImage> hotelImage;


    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public ArrayList<Maps> getMaps() {
        return maps;
    }

    public void setMaps(ArrayList<Maps> maps) {
        this.maps = maps;
    }

    public ArrayList<FreeAmenities> getAminetiesList() {
        return aminetiesList;
    }

    public void setAminetiesList(ArrayList<FreeAmenities> aminetiesList) {
        this.aminetiesList = aminetiesList;
    }

    public ArrayList<PaidAmenities> getPaidAmenityList() {
        return paidAmenityList;
    }

    public void setPaidAmenityList(ArrayList<PaidAmenities> paidAmenityList) {
        this.paidAmenityList = paidAmenityList;
    }

    public ArrayList<Policy> getPolicies() {
        return policies;
    }

    public void setPolicies(ArrayList<Policy> policies) {
        this.policies = policies;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public BankDetails getAccountsInfo() {
        return AccountsInfo;
    }

    public void setAccountsInfo(BankDetails accountsInfo) {
        AccountsInfo = accountsInfo;
    }

    public String getHotelStreetAddress() {
        return hotelStreetAddress;
    }

    public void setHotelStreetAddress(String hotelStreetAddress) {
        this.hotelStreetAddress = hotelStreetAddress;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocalty() {
        return localty;
    }

    public void setLocalty(String localty) {
        this.localty = localty;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getHotelDisplayName() {
        return hotelDisplayName;
    }

    public void setHotelDisplayName(String hotelDisplayName) {
        this.hotelDisplayName = hotelDisplayName;
    }

    public String getHotelType() {
        return hotelType;
    }

    public void setHotelType(String hotelType) {
        this.hotelType = hotelType;
    }

    public String getStarRating() {
        return starRating;
    }

    public void setStarRating(String starRating) {
        this.starRating = starRating;
    }

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public String getHotelBuiltYear() {
        return hotelBuiltYear;
    }

    public void setHotelBuiltYear(String hotelBuiltYear) {
        this.hotelBuiltYear = hotelBuiltYear;
    }

    public String getNoofRestuarentsInHotel() {
        return noofRestuarentsInHotel;
    }

    public void setNoofRestuarentsInHotel(String noofRestuarentsInHotel) {
        this.noofRestuarentsInHotel = noofRestuarentsInHotel;
    }

    public String getNoOfRoomsInHotel() {
        return noOfRoomsInHotel;
    }

    public void setNoOfRoomsInHotel(String noOfRoomsInHotel) {
        this.noOfRoomsInHotel = noOfRoomsInHotel;
    }

    public String getNoOfFloorsInHotel() {
        return noOfFloorsInHotel;
    }

    public void setNoOfFloorsInHotel(String noOfFloorsInHotel) {
        this.noOfFloorsInHotel = noOfFloorsInHotel;
    }

    public String getCurrencyAccepted() {
        return currencyAccepted;
    }

    public void setCurrencyAccepted(String currencyAccepted) {
        this.currencyAccepted = currencyAccepted;
    }

    public String getVccCurrencyAccepted() {
        return vccCurrencyAccepted;
    }

    public void setVccCurrencyAccepted(String vccCurrencyAccepted) {
        this.vccCurrencyAccepted = vccCurrencyAccepted;
    }

    public String getStandardCheckInTime() {
        return standardCheckInTime;
    }

    public void setStandardCheckInTime(String standardCheckInTime) {
        this.standardCheckInTime = standardCheckInTime;
    }

    public String getStandardCheckOutTime() {
        return standardCheckOutTime;
    }

    public void setStandardCheckOutTime(String standardCheckOutTime) {
        this.standardCheckOutTime = standardCheckOutTime;
    }

    public String getHotelTimeZone() {
        return hotelTimeZone;
    }

    public void setHotelTimeZone(String hotelTimeZone) {
        this.hotelTimeZone = hotelTimeZone;
    }

    public boolean isIs24HourCheckIn() {
        return is24HourCheckIn;
    }

    public void setIs24HourCheckIn(boolean is24HourCheckIn) {
        this.is24HourCheckIn = is24HourCheckIn;
    }

    public ArrayList<HotelDocuments> getHotelDocuments() {
        return hotelDocuments;
    }

    public void setHotelDocuments(ArrayList<HotelDocuments> hotelDocuments) {
        this.hotelDocuments = hotelDocuments;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Bookings1 getBookingList() {
        return bookingList;
    }

    public void setBookingList(Bookings1 bookingList) {
        this.bookingList = bookingList;
    }

    public int getAlloweNoOfHoursForCheckInAndCheckOut() {
        return AlloweNoOfHoursForCheckInAndCheckOut;
    }

    public void setAlloweNoOfHoursForCheckInAndCheckOut(int alloweNoOfHoursForCheckInAndCheckOut) {
        AlloweNoOfHoursForCheckInAndCheckOut = alloweNoOfHoursForCheckInAndCheckOut;
    }

    public ArrayList<HotelImage> getHotelImage() {
        return hotelImage;
    }

    public void setHotelImage(ArrayList<HotelImage> hotelImage) {
        this.hotelImage = hotelImage;
    }
}



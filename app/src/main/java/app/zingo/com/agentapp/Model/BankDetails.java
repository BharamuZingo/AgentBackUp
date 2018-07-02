package app.zingo.com.agentapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZingoHotels.com on 04-11-2017.
 */

public class BankDetails implements Parcelable {

    @SerializedName("BankDetailsId")
    private int BankDetailsId;
    @SerializedName("AccountNo")
    private String AccountNo;
    @SerializedName("AccountHolderName")
    private String AccountHolderName;

    @SerializedName("BranchName")
    private String BranchName;
    @SerializedName("IFSCCode")
    private String IFSCCode;
    @SerializedName("BranchCode")
    private String BranchCode;

    @SerializedName("BankName")
    private String BankName;
    @SerializedName("BankCode")
    private String BankCode;
    @SerializedName("PANnumber")
    private String PANnumber;

    @SerializedName("NameOnPANCard")
    private String NameOnPANCard;
    @SerializedName("ServiceTaxNo")
    private String ServiceTaxNo;

    protected BankDetails(Parcel in) {
        BankDetailsId = in.readInt();
        AccountNo = in.readString();
        AccountHolderName = in.readString();
        BranchName = in.readString();
        IFSCCode = in.readString();
        BranchCode = in.readString();
        BankName = in.readString();
        BankCode = in.readString();
        PANnumber = in.readString();
        NameOnPANCard = in.readString();
        ServiceTaxNo = in.readString();
        HotelId = in.readInt();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.BankDetailsId);
        dest.writeString(this.AccountNo);
        dest.writeString(this.AccountHolderName);
        dest.writeString(this.BranchName);
        dest.writeString(this.IFSCCode);
        dest.writeString(this.BranchCode);
        dest.writeString(this.BankName);
        dest.writeString(this.BankCode);
        dest.writeString(this.PANnumber);
        dest.writeString(this.NameOnPANCard);
        dest.writeString(this.ServiceTaxNo);
        dest.writeInt(this.HotelId);
    }
    public BankDetails(){

    }

    public static final Creator<BankDetails> CREATOR = new Creator<BankDetails>() {
        @Override
        public BankDetails createFromParcel(Parcel in) {
            return new BankDetails(in);
        }

        @Override
        public BankDetails[] newArray(int size) {
            return new BankDetails[size];
        }
    };

    public int getBankDetailsId() {
        return BankDetailsId;
    }

    public void setBankDetailsId(int bankDetailsId) {
        BankDetailsId = bankDetailsId;
    }

    public String getAccountNo() {
        return AccountNo;
    }

    public void setAccountNo(String accountNo) {
        AccountNo = accountNo;
    }

    public String getAccountHolderName() {
        return AccountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        AccountHolderName = accountHolderName;
    }

    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }

    public String getIFSCCode() {
        return IFSCCode;
    }

    public void setIFSCCode(String IFSCCode) {
        this.IFSCCode = IFSCCode;
    }

    public String getBranchCode() {
        return BranchCode;
    }

    public void setBranchCode(String branchCode) {
        BranchCode = branchCode;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getBankCode() {
        return BankCode;
    }

    public void setBankCode(String bankCode) {
        BankCode = bankCode;
    }

    public String getPANnumber() {
        return PANnumber;
    }

    public void setPANnumber(String PANnumber) {
        this.PANnumber = PANnumber;
    }

    public String getNameOnPANCard() {
        return NameOnPANCard;
    }

    public void setNameOnPANCard(String nameOnPANCard) {
        NameOnPANCard = nameOnPANCard;
    }

    public String getServiceTaxNo() {
        return ServiceTaxNo;
    }

    public void setServiceTaxNo(String serviceTaxNo) {
        ServiceTaxNo = serviceTaxNo;
    }

    public int getHotelId() {
        return HotelId;
    }

    public void setHotelId(int hotelId) {
        HotelId = hotelId;
    }

    @SerializedName("HotelId")
    private int HotelId;


    @Override
    public int describeContents() {
        return 0;
    }


}

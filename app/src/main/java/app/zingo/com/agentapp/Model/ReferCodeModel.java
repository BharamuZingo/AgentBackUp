package app.zingo.com.agentapp.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZingoHotels Tech on 07-05-2018.
 */

public class ReferCodeModel {

    @SerializedName("ReferralCode")
    private String ReferralCode;

    @SerializedName("ReferralCodeUsed")
    private String ReferralCodeUsed;

    public String getReferralCodeUsed() {
        return ReferralCodeUsed;
    }

    public void setReferralCodeUsed(String referralCodeUsed) {
        ReferralCodeUsed = referralCodeUsed;
    }

    public String getReferralCode() {
        return ReferralCode;
    }

    public void setReferralCode(String referralCode) {
        ReferralCode = referralCode;
    }
}

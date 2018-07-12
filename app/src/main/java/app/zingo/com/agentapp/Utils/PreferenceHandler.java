package app.zingo.com.agentapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ZingoHotels.com on 10-11-2017.
 */

public class PreferenceHandler {

    private SharedPreferences sh;
    private static PreferenceHandler preferanceHandlerInstance = null;

    private PreferenceHandler() {

    }

    private PreferenceHandler(Context mContext) {
        sh = PreferenceManager.getDefaultSharedPreferences(mContext);
    }


    public static synchronized PreferenceHandler getInstance(Context mContext) {
        if (null == preferanceHandlerInstance)
        {
            preferanceHandlerInstance = new PreferenceHandler(mContext);
        }
        return preferanceHandlerInstance;
    }





    public void setUserId(int id)
    {
        sh.edit().putInt(Constants.USER_ID,id).apply();
    }

    public int getUserId()
    {
        return sh.getInt(Constants.USER_ID,0);
    }

    public void setHotelId(int id)
    {
        sh.edit().putInt(Constants.HOTEL_ID,id).apply();
    }

    public int getHotelID()
    {
        return sh.getInt(Constants.HOTEL_ID,0);
    }

    public void setUserRoleUniqueID(String approved)
    {
        sh.edit().putString(Constants.USER_ROLE_UNIQUE_ID,approved).apply();
    }

    public String getUserRoleUniqueID()
    {
        return sh.getString(Constants.USER_ROLE_UNIQUE_ID,"");
    }

    public void setUserName(String username)
    {
        sh.edit().putString(Constants.USER_NAME,username).apply();
    }

    public String getUserName()
    {
        return sh.getString(Constants.USER_NAME,"");
    }

    public void setFullName(String username)
    {
        sh.edit().putString(Constants.USER_FULL_NAME,username).apply();
    }

    public String getFullName()
    {
        return sh.getString(Constants.USER_FULL_NAME,"");
    }

    public void setEmailId(String username)
    {
        sh.edit().putString(Constants.USER_EMAIL,username).apply();
    }

    public String getEmailId()
    {
        return sh.getString(Constants.USER_EMAIL,"");
    }

    public void setCommissionAmount(long commisionAmount)
    {
        sh.edit().putLong(Constants.COMMISSION,commisionAmount).apply();
    }
    public long getCommissionAmount()
    {
        return sh.getLong(Constants.COMMISSION,0);
    }

    public void setReferalAmount(long referalAmount)
    {
        sh.edit().putLong(Constants.REFERAL,referalAmount).apply();
    }
    public long getReferalAmount()
    {
        return sh.getLong(Constants.REFERAL,0);
    }

    public void setReferedAmount(int referedAmount)
    {
        sh.edit().putInt(Constants.REFERED,referedAmount).apply();
    }
    public int getReferedAmount()
    {
        return sh.getInt(Constants.REFERED,0);
    }


    public long getCommissionPercentage()
    {
        return sh.getLong(Constants.COMMISSION_PERCENTAGE,0);
    }

    public void setCommissionPercentage(long commisionAmount)
    {
        sh.edit().putLong(Constants.COMMISSION_PERCENTAGE,commisionAmount).apply();
    }



    public void setPhoneNumber(String phonenumber)
    {
        sh.edit().putString(Constants.USER_PHONENUMER,phonenumber).apply();
    }

    public String getPhoneNumber()
    {
        return sh.getString(Constants.USER_PHONENUMER,"");
    }

    public void setProfileStatus(String status)
    {
        sh.edit().putString(Constants.USER_STATUS,status).apply();
    }

    public String getProfileStatus()
    {
        return sh.getString(Constants.USER_STATUS,"");
    }

    public void clear(){
        sh.edit().clear().apply();

    }

}

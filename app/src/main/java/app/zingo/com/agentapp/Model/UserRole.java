package app.zingo.com.agentapp.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CSC on 10/31/2017.
 */

public class UserRole {

    @SerializedName("UserRoleId")
    public int UserRoleId;
    @SerializedName("UserRoleName")
    public String UserRoleName;
    @SerializedName("UserRoleUniqueId")
    public String userRoleUniqueId;

    public int getUserRoleId() {
        return UserRoleId;
    }

    public void setUserRoleId(int userRoleId) {
        this.UserRoleId = userRoleId;
    }

    public String getUserRoleName() {
        return UserRoleName;
    }

    public void setUserRoleName(String userRoleName) {
        this.UserRoleName = userRoleName;
    }

    public void setUserRoleUniqueId(String userRoleUniqueId) {
        this.userRoleUniqueId = userRoleUniqueId;
    }

    public String getUserRoleUniqueId() {
        return userRoleUniqueId;
    }
}

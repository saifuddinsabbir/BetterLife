package com.example.dashbosrd;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    //variables
    SharedPreferences usersSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGIN = "IsLoggedIn";

    public  static final String KEY_DP = "dp";
    public static final String KEY_FULLNAME = "fullName";
    public static final String KEY_USERNAME = "userName";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONENO = "phoneNo";
    public static final String KEY_DOB = "dateOfBirth";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_BLOODGROUP = "bloodGroup";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_PASSWORD = "password";

    public SessionManager(Context _context) {
        context = _context;
        usersSession = context.getSharedPreferences("userLoginSession", Context.MODE_PRIVATE);
        editor = usersSession.edit();
    }

    public void createLoginSession(String dp, String fullName, String userName, String email, String phoneNo, String dateOfBirth, String gender, String bloodGroup, String address, String password) {

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_DP, dp);
        editor.putString(KEY_FULLNAME, fullName);
        editor.putString(KEY_USERNAME, userName);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONENO, phoneNo);
        editor.putString(KEY_DOB, dateOfBirth);
        editor.putString(KEY_GENDER, gender);
        editor.putString(KEY_BLOODGROUP, bloodGroup);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_PASSWORD, password);

        editor.commit();
    }

    public HashMap<String, String> getUsersDetailFromSession() {
        HashMap<String, String> userData = new HashMap<String, String>();

        userData.put(KEY_DP, usersSession.getString(KEY_DP, null));
        userData.put(KEY_FULLNAME, usersSession.getString(KEY_FULLNAME, null));
        userData.put(KEY_USERNAME, usersSession.getString(KEY_USERNAME, null));
        userData.put(KEY_EMAIL, usersSession.getString(KEY_EMAIL, null));
        userData.put(KEY_PHONENO, usersSession.getString(KEY_PHONENO, null));
        userData.put(KEY_DOB, usersSession.getString(KEY_DOB, null));
        userData.put(KEY_GENDER, usersSession.getString(KEY_GENDER, null));
        userData.put(KEY_BLOODGROUP, usersSession.getString(KEY_BLOODGROUP, null));
        userData.put(KEY_ADDRESS, usersSession.getString(KEY_ADDRESS, null));
        userData.put(KEY_PASSWORD, usersSession.getString(KEY_PASSWORD, null));

        return userData;
    }

    public boolean checkLogin() {
        return usersSession.getBoolean(IS_LOGIN, true);
    }

    public void logoutUserFromSession() {
        editor.clear();
        editor.commit();
    }
}

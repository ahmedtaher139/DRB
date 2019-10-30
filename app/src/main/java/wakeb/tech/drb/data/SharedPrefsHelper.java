/*
 *    Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package wakeb.tech.drb.data;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by gaura on 22-08-2017.
 */

public class SharedPrefsHelper {

    public static final String MY_PREFS = "MY_PREFS";
    public static final String NAME = "NAME";
    public static final String LANG = "LANG";
    public static final String NUMBER = "NUMBER";
    public static final String USER_ID = "USER_ID";
    public static final String TRIP_ID = "TRIP_ID";
    public static final String IMAGE = "IMAGE";
    public static final String STATUSLANG = "STATUSLANG";
    public static final String STATUSWELCOME = "STATUSWELCOME";
    public static final String TYPE = "TYPE";
    public static final String NOTIFICATION_STATUS = "NOTIFICATION_STATUS";



    SharedPreferences mSharedPreferences;

    public SharedPrefsHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(MY_PREFS, MODE_PRIVATE);
    }

    public void clear() {
        mSharedPreferences.edit().clear().apply();
    }



    public void putLang(String name) {
        mSharedPreferences.edit().putString(LANG, name).apply();
    }
    public String getLang() {
        return mSharedPreferences.getString(LANG, null);
    }

    public void putLangStatus(boolean status) {
        mSharedPreferences.edit().putBoolean(STATUSLANG, status).apply();
    }
    public boolean getLangStatus() {
        return mSharedPreferences.getBoolean(STATUSLANG, false);
    }

    public void putWelcomeStatus(boolean status) {
        mSharedPreferences.edit().putBoolean(STATUSWELCOME, status).apply();
    }
    public boolean getWelcomeStatus() {
        return mSharedPreferences.getBoolean(STATUSWELCOME, false);
    }



    public void putType(String type) {
        mSharedPreferences.edit().putString(TYPE, type).apply();
    }
    public String getType() {
        return mSharedPreferences.getString(TYPE, null);
    }

    public void putName(String name) {
        mSharedPreferences.edit().putString(NAME, name).apply();
    }
    public String getName() {
        return mSharedPreferences.getString(NAME, null);
    }



    public void putImage(String image) {
        mSharedPreferences.edit().putString(IMAGE, image).apply();
    }
    public String getImage() {
        return mSharedPreferences.getString(IMAGE, null);
    }

    public void putNumber(String email) {
        mSharedPreferences.edit().putString(NUMBER, email).apply();
    }
    public String getNumber() {
        return mSharedPreferences.getString(NUMBER, null);
    }


    public boolean getLoggedInMode() {
        return mSharedPreferences.getBoolean("IS_LOGGED_IN", false);
    }
    public void setLoggedInMode(boolean loggedIn) {
        mSharedPreferences.edit().putBoolean("IS_LOGGED_IN", loggedIn).apply();
    }
    public boolean getTripStarted() {
        return mSharedPreferences.getBoolean("TRIP_STARTED", false);
    }
    public void setTripStarted(boolean tripStarted) {
        mSharedPreferences.edit().putBoolean("TRIP_STARTED", tripStarted).apply();
    }


    public void putID(String ID) {
        mSharedPreferences.edit().putString(USER_ID, ID).apply();
    }
    public String getID() {
        return mSharedPreferences.getString(USER_ID, null);
    }



    public void setTripID(String ID) {
        mSharedPreferences.edit().putString(TRIP_ID, ID).apply();
    }
    public String getTripID() {
        return mSharedPreferences.getString(TRIP_ID, null);
    }


    public void putTurnOnNotifications(boolean ID) {
        mSharedPreferences.edit().putBoolean(NOTIFICATION_STATUS, ID).apply();
    }
    public boolean getTurnOnNotifications() {
        return mSharedPreferences.getBoolean(NOTIFICATION_STATUS, false);
    }




    public void putTurnOnRisksMarkers(boolean ID) {
        mSharedPreferences.edit().putBoolean("RisksMarkers", ID).apply();
    }
    public boolean getTurnOnRisksMarkers() {
        return mSharedPreferences.getBoolean("RisksMarkers", false);
    }

    public void putTurnOnPlacesMarkers(boolean ID) {
        mSharedPreferences.edit().putBoolean("PlacesMarkers", ID).apply();
    }
    public boolean getTurnOnPlacesMarkers() {
        return mSharedPreferences.getBoolean("PlacesMarkers", false);
    }

    public void putTurnOnStoresMarkers(boolean ID) {
        mSharedPreferences.edit().putBoolean("StoresMarkers", ID).apply();
    }
    public boolean getTurnOnStoresMarkers() {
        return mSharedPreferences.getBoolean("StoresMarkers", false);
    }


}

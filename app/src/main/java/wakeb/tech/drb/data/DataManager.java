package wakeb.tech.drb.data;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by A.taher on 10/15/2018.
 */

public class DataManager {


    SharedPrefsHelper mSharedPrefsHelper;

    public DataManager(SharedPrefsHelper sharedPrefsHelper) {
        mSharedPrefsHelper = sharedPrefsHelper;
    }

    public void clear() {
        mSharedPrefsHelper.clear();
    }

    public void saveLang(String name) {
        mSharedPrefsHelper.putLang(name);
    }

    public String getLang() {
        return mSharedPrefsHelper.getLang();
    }

    public void saveLangStatus(Boolean status) {
        mSharedPrefsHelper.putLangStatus(status);
    }

    public boolean getLangStatus() {
        return mSharedPrefsHelper.getLangStatus();
    }

    public void saveWelcomeStatus(Boolean Welcome) {
        mSharedPrefsHelper.putWelcomeStatus(Welcome);
    }

    public boolean getWelcomeStatus() {
        return mSharedPrefsHelper.getWelcomeStatus();
    }

    public void saveType(String type) {
        mSharedPrefsHelper.putType(type);
    }

    public String getType() {
        return mSharedPrefsHelper.getType();
    }

    public void saveName(String name) {
        mSharedPrefsHelper.putName(name);
    }

    public String getName() {
        return mSharedPrefsHelper.getName();
    }

    public void saveImage(String image) {
        mSharedPrefsHelper.putImage(image);
    }

    public String getImage() {
        return mSharedPrefsHelper.getImage();
    }

    public void saveNumber(String Number) {
        mSharedPrefsHelper.putNumber(Number);
    }

    public String getNumber() {
        return mSharedPrefsHelper.getNumber();
    }

    public void saveID(String ID) {
        mSharedPrefsHelper.putID(ID);
    }

    public String getID() {
        return mSharedPrefsHelper.getID();
    }

    public void setLoggedIn(boolean loggedIn) {
        mSharedPrefsHelper.setLoggedInMode(loggedIn);
    }

    public Boolean getLoggedInMode() {
        return mSharedPrefsHelper.getLoggedInMode();
    }

    public void tripStarted(boolean tripStarted) {
        mSharedPrefsHelper.setTripStarted(tripStarted);
    }

    public Boolean getTripStarted() {
        return mSharedPrefsHelper.getTripStarted();
    }


    public void tripID(String tripID) {
        mSharedPrefsHelper.setTripID(tripID);
    }

    public String getTripID() {
        return mSharedPrefsHelper.getTripID();
    }


    public void turnOnNotifications(Boolean status) {
        mSharedPrefsHelper.putTurnOnNotifications(status);
    }

    public boolean getTurnOnNotifications() {
        return mSharedPrefsHelper.getTurnOnNotifications();
    }


    public void turnOnRisksMarkers(Boolean status) {
        mSharedPrefsHelper.putTurnOnRisksMarkers(status);
    }

    public boolean getTurnOnRisksMarkers() {
        return mSharedPrefsHelper.getTurnOnRisksMarkers();
    }



    public void turnOnPlacesMarkers(Boolean status) {
        mSharedPrefsHelper.putTurnOnPlacesMarkers(status);
    }

    public boolean getTurnOnPlacesMarkers() {
        return mSharedPrefsHelper.getTurnOnPlacesMarkers();
    }




    public void turnOnStoresMarkers(Boolean status) {
        mSharedPrefsHelper.putTurnOnStoresMarkers(status);
    }

    public boolean getTurnOnStoresMarkers() {
        return mSharedPrefsHelper.getTurnOnStoresMarkers();
    }

}



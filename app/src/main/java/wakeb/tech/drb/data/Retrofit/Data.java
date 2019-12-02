package wakeb.tech.drb.data.Retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import wakeb.tech.drb.Models.AllPlaces;
import wakeb.tech.drb.Models.Comment;
import wakeb.tech.drb.Models.CurrentTrip;
import wakeb.tech.drb.Models.Journeys;
import wakeb.tech.drb.Models.LOG;
import wakeb.tech.drb.Models.MapSpots;
import wakeb.tech.drb.Models.NearPlaces;
import wakeb.tech.drb.Models.Notifications;
import wakeb.tech.drb.Models.Password;
import wakeb.tech.drb.Models.PostedTrip;
import wakeb.tech.drb.Models.Posts;
import wakeb.tech.drb.Models.Profile;
import wakeb.tech.drb.Models.Publisher;
import wakeb.tech.drb.Models.Resource;
import wakeb.tech.drb.Models.RiskType;
import wakeb.tech.drb.Models.RisksList;
import wakeb.tech.drb.Models.Settings;
import wakeb.tech.drb.Models.SpotModel;
import wakeb.tech.drb.Models.Store;
import wakeb.tech.drb.Models.StorePlace;
import wakeb.tech.drb.Models.StoresTypes;
import wakeb.tech.drb.Models.Suggest;
import wakeb.tech.drb.Models.SuggestsPlaces;
import wakeb.tech.drb.Models.Trip;
import wakeb.tech.drb.Models.User;
import wakeb.tech.drb.Models.UserProfileModel;

public class Data {
    @SerializedName("publisher")
    private User user;

    @SerializedName("settings")
    private ArrayList<Settings> settings;

    public ArrayList<Settings> getSettings() {
        return settings;
    }

    public void setSettings(ArrayList<Settings> settings) {
        this.settings = settings;
    }

    @SerializedName("trip")
    private Trip trip;

    @SerializedName("posts")
    private Posts publishing;

    public ArrayList<MapSpots> getMapSpots() {
        return mapSpots;
    }

    public void setMapSpots(ArrayList<MapSpots> mapSpots) {
        this.mapSpots = mapSpots;
    }

    @SerializedName("map_spots")
    private ArrayList<MapSpots> mapSpots;


    @SerializedName("posted-trip")
    private PostedTrip postedTrip;

    public PostedTrip getPostedTrip() {
        return postedTrip;
    }

    public void setPostedTrip(PostedTrip postedTrip) {
        this.postedTrip = postedTrip;
    }

    @SerializedName("followers")
    private  ArrayList<Publisher> followers;

    @SerializedName("nearStore")
    private  ArrayList<NearPlaces> nearStore;

    @SerializedName("Spots")
    private  ArrayList<SpotModel> spotModels;


    public ArrayList<SpotModel> getSpotModels() {
        return spotModels;
    }

    public void setSpotModels(ArrayList<SpotModel> spotModels) {
        this.spotModels = spotModels;
    }

    public ArrayList<NearPlaces> getNearStore() {
        return nearStore;
    }

    public void setNearStore(ArrayList<NearPlaces> nearStore) {
        this.nearStore = nearStore;
    }

    @SerializedName("activites")
    private  ArrayList<LOG> logs;

    @SerializedName("notifications")
    private  ArrayList<Notifications> notifications;

    public ArrayList<Notifications> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<Notifications> notifications) {
        this.notifications = notifications;
    }

    public ArrayList<LOG> getLogs() {
        return logs;
    }

    public void setLogs(ArrayList<LOG> logs) {
        this.logs = logs;
    }

    @SerializedName("suggests")
    private   SuggestsPlaces  suggests;

    public  SuggestsPlaces  getSuggests() {
        return suggests;
    }

    public void setSuggests(  SuggestsPlaces  suggests) {
        this.suggests = suggests;
    }

    @SerializedName("follows")
    private  ArrayList<Publisher> follows;

    public ArrayList<Publisher> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<Publisher> followers) {
        this.followers = followers;
    }

    public ArrayList<Publisher> getFollows() {
        return follows;
    }

    public void setFollows(ArrayList<Publisher> follows) {
        this.follows = follows;
    }

    @SerializedName("profile")
    private Profile profile;


    @SerializedName("Profile")
    private UserProfileModel userProfileModel;


    public UserProfileModel getUserProfileModel() {
        return userProfileModel;
    }

    public void setUserProfileModel(UserProfileModel userProfileModel) {
        this.userProfileModel = userProfileModel;
    }

    public CurrentTrip getCurrentTrip() {
        return currentTrip;
    }

    public void setCurrentTrip(CurrentTrip currentTrip) {
        this.currentTrip = currentTrip;
    }

    @SerializedName("currentTrip")
    private CurrentTrip currentTrip;

    @SerializedName("store-type")
    private ArrayList<StoresTypes> storesTypes;

    @SerializedName("resources")
    private ArrayList<Resource> resources;

    @SerializedName("risk-type")
    private ArrayList<RiskType> riskTypes;

    @SerializedName("Journeys")
    private ArrayList<Journeys> journeys;

    public ArrayList<Journeys> getJourneys() {
        return journeys;
    }

    public void setJourneys(ArrayList<Journeys> journeys) {
        this.journeys = journeys;
    }

    public ArrayList<Publisher> getBlocks() {
        return blocks;
    }

    public void setBlocks(ArrayList<Publisher> blocks) {
        this.blocks = blocks;
    }

    @SerializedName("blocks")
    private ArrayList<Publisher> blocks;

    public ArrayList<RiskType> getRiskTypes() {
        return riskTypes;
    }

    public void setRiskTypes(ArrayList<RiskType> riskTypes) {
        this.riskTypes = riskTypes;
    }

    public ArrayList<Publisher> getSuggested_users() {
        return suggested_users;
    }

    public void setSuggested_users(ArrayList<Publisher> publishers) {
        this.suggested_users = publishers;
    }

    @SerializedName("suggested_users")
    private ArrayList<Publisher> suggested_users;

    @SerializedName("resource")
    private Resource resource;

    @SerializedName("store")
    private Store store;

    @SerializedName("all-places")
    private AllPlaces allPlaces;

    public AllPlaces getAllPlaces() {
        return allPlaces;
    }

    public void setAllPlaces(AllPlaces allPlaces) {
        this.allPlaces = allPlaces;
    }

    @SerializedName("password")
    private Password password;

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    @SerializedName("store-places")
    private ArrayList<StorePlace> storePlaces;


    @SerializedName("risks-list")
    private RisksList risks;

    public RisksList getRisks() {
        return risks;
    }

    public void setRisks(RisksList risks) {
        this.risks = risks;
    }

    @SerializedName("Comments")
    private ArrayList<Comment> comments;


    @SerializedName("storePlace")
    private StorePlace storePlace;

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public ArrayList<StorePlace> getStorePlaces() {
        return storePlaces;
    }

    public void setStorePlaces(ArrayList<StorePlace> storePlaces) {
        this.storePlaces = storePlaces;
    }

    public StorePlace getStorePlace() {
        return storePlace;
    }

    public void setStorePlace(StorePlace storePlace) {
        this.storePlace = storePlace;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public void setResources(ArrayList<Resource> resources) {
        this.resources = resources;
    }

    public Posts getPublishing() {
        return publishing;
    }

    public void setPublishing(Posts publishing) {
        this.publishing = publishing;
    }


    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }


    public ArrayList<StoresTypes> getStoresTypes() {
        return storesTypes;
    }

    public void setStoresTypes(ArrayList<StoresTypes> storesTypes) {
        this.storesTypes = storesTypes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

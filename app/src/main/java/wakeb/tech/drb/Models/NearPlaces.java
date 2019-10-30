package wakeb.tech.drb.Models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NearPlaces {
    @SerializedName("places")
    @Expose
    private Places places;
    @SerializedName("store")
    @Expose
    private Store store;

    public Places getPlaces() {
        return places;
    }

    public void setPlaces(Places places) {
        this.places = places;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

}

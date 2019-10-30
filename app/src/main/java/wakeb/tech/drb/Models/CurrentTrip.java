package wakeb.tech.drb.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CurrentTrip {
    @SerializedName("trip")
    @Expose
    private Trip trip;
    @SerializedName("nearStore")
    @Expose
    private List<NearPlaces> nearStore = null;

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public List<NearPlaces> getNearStore() {
        return nearStore;
    }

    public void setNearStore(List<NearPlaces> nearStore) {
        this.nearStore = nearStore;
    }

}

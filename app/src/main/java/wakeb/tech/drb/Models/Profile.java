package wakeb.tech.drb.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Profile {

    @SerializedName("publisher")
    @Expose
    private User publisher;
    @SerializedName("posted-trips")
    @Expose
    private List<PostedTrip> postedTrips = null;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public User getPublisher() {
        return publisher;
    }

    public void setPublisher(User publisher) {
        this.publisher = publisher;
    }

    public List<PostedTrip> getPostedTrips() {
        return postedTrips;
    }

    public void setPostedTrips(List<PostedTrip> postedTrips) {
        this.postedTrips = postedTrips;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }


}

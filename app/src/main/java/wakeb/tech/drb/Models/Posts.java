package wakeb.tech.drb.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Posts {
    @SerializedName("posted-trips")
    @Expose
    private List<PostedTrip> pulishings = null;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public List<PostedTrip> getPulishings() {
        return pulishings;
    }

    public void setPulishings(List<PostedTrip> pulishings) {
        this.pulishings = pulishings;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

}

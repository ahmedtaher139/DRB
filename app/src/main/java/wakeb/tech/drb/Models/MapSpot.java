package wakeb.tech.drb.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MapSpot {
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

}

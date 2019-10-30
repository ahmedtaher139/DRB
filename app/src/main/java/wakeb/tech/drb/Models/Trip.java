package wakeb.tech.drb.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Trip {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("start_lat")
    @Expose
    private Double startLat;
    @SerializedName("end_lat")
    @Expose
    private Double endLat;
    @SerializedName("start_lng")
    @Expose
    private Double startLng;
    @SerializedName("end_lng")
    @Expose
    private Double endLng;
    @SerializedName("start_address")
    @Expose
    private String startAddress;
    @SerializedName("end_address")
    @Expose
    private String endAddress;
    @SerializedName("map_screen_shot")
    @Expose
    private String mapScreenShot;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("distance")
    @Expose
    private double distance;
    @SerializedName("estimated_duration")
    @Expose
    private String estimatedDuration;
    @SerializedName("publisher")
    @Expose
    private Publisher publisher;
    @SerializedName("created_at")
    @Expose
    private long createdAt;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("ended_at")
    @Expose
    private long endedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getStartLat() {
        return startLat;
    }

    public void setStartLat(Double startLat) {
        this.startLat = startLat;
    }

    public Double getEndLat() {
        return endLat;
    }

    public void setEndLat(Double endLat) {
        this.endLat = endLat;
    }

    public Double getStartLng() {
        return startLng;
    }

    public void setStartLng(Double startLng) {
        this.startLng = startLng;
    }

    public Double getEndLng() {
        return endLng;
    }

    public void setEndLng(Double endLng) {
        this.endLng = endLng;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getMapScreenShot() {
        return mapScreenShot;
    }

    public void setMapScreenShot(String mapScreenShot) {
        this.mapScreenShot = mapScreenShot;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(String estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(long endedAt) {
        this.endedAt = endedAt;
    }

}

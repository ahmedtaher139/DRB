package wakeb.tech.drb.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Suggest {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("lng")
    @Expose
    private double lng;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("comments")
    @Expose
    private Integer comments;
    @SerializedName("likes_count")
    @Expose
    private Integer likesCount;
    @SerializedName("likes_latest")
    @Expose
    private String likesLatest;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("publisher")
    @Expose
    private Publisher publisher;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public String getLikesLatest() {
        return likesLatest;
    }

    public void setLikesLatest(String likesLatest) {
        this.likesLatest = likesLatest;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

}
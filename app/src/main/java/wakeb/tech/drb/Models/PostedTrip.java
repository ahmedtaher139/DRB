package wakeb.tech.drb.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostedTrip {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("block")
    @Expose
    private Integer block;
    @SerializedName("privacy")
    @Expose
    private String privacy;
    @SerializedName("postTrip")
    @Expose
    private Trip postTrip;
    @SerializedName("isshare")
    @Expose
    private Boolean isshare;
    @SerializedName("comments")
    @Expose
    private Integer comments;
    @SerializedName("res_status")
    @Expose
    private Boolean resStatus;
    @SerializedName("res_count")
    @Expose
    private Integer resCount;
    @SerializedName("likes_count")
    @Expose
    private Integer likesCount;
    @SerializedName("likes_latest")
    @Expose
    private String likesLatest;
    @SerializedName("share_count")
    @Expose
    private Integer shareCount;
    @SerializedName("share_latest")
    @Expose
    private Object shareLatest;
    @SerializedName("sharer")
    @Expose
    private Publisher sharer;
    @SerializedName("likes_status")
    @Expose
    private Boolean likesStatus;
    @SerializedName("fav_status")
    @Expose
    private Boolean favStatus;
    @SerializedName("created_at")
    @Expose
    private long createdAt;
    @SerializedName("faves_count")
    @Expose
    private Integer FavesCount;

    public Integer getFavesCount() {
        return FavesCount;
    }

    public void setFavesCount(Integer favesCount) {
        FavesCount = favesCount;
    }

    public Boolean getResStatus() {
        return resStatus;
    }

    public void setResStatus(Boolean resStatus) {
        this.resStatus = resStatus;
    }

    public Integer getResCount() {
        return resCount;
    }

    public void setResCount(Integer resCount) {
        this.resCount = resCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBlock() {
        return block;
    }

    public void setBlock(Integer block) {
        this.block = block;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public Trip getTrip() {
        return postTrip;
    }

    public void setPostTrip(Trip postTrip) {
        this.postTrip = postTrip;
    }

    public Boolean getIsshare() {
        return isshare;
    }

    public void setIsshare(Boolean isshare) {
        this.isshare = isshare;
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

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public Object getShareLatest() {
        return shareLatest;
    }

    public void setShareLatest(Object shareLatest) {
        this.shareLatest = shareLatest;
    }

    public Publisher getSharer() {
        return sharer;
    }

    public void setSharer(Publisher sharer) {
        this.sharer = sharer;
    }

    public Boolean getLikesStatus() {
        return likesStatus;
    }

    public void setLikesStatus(Boolean likesStatus) {
        this.likesStatus = likesStatus;
    }

    public Boolean getFavStatus() {
        return favStatus;
    }

    public void setFavStatus(Boolean favStatus) {
        this.favStatus = favStatus;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

}
package wakeb.tech.drb.Models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SpotModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("place_name")
    @Expose
    private String placeName;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;
    @SerializedName("journal_id")
    @Expose
    private Integer journalId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("likes_count")
    @Expose
    private Integer likesCount;
    @SerializedName("comm_count")
    @Expose
    private Integer commCount;
    @SerializedName("files_count")
    @Expose
    private Integer filesCount;
    @SerializedName("favourites_count")
    @Expose
    private Integer favouritesCount;
    @SerializedName("is_favourite")
    @Expose
    private Boolean isFavorite;
    @SerializedName("is_liked")
    @Expose
    private Boolean isLiked;
    @SerializedName("files")
    @Expose
    private List<File> files = null;
    @SerializedName("publisher")
    @Expose
    private Publisher publisher;
    @SerializedName("Spot_journey")
    @Expose
    private SpotJourney spotJourney;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getJournalId() {
        return journalId;
    }

    public void setJournalId(Integer journalId) {
        this.journalId = journalId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Integer getCommCount() {
        return commCount;
    }

    public void setCommCount(Integer commCount) {
        this.commCount = commCount;
    }

    public Integer getFilesCount() {
        return filesCount;
    }

    public void setFilesCount(Integer filesCount) {
        this.filesCount = filesCount;
    }

    public Integer getFavouritesCount() {
        return favouritesCount;
    }

    public void setFavouritesCount(Integer favouritesCount) {
        this.favouritesCount = favouritesCount;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public Boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(Boolean isLiked) {
        this.isLiked = isLiked;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public SpotJourney getSpotJourney() {
        return spotJourney;
    }

    public void setSpotJourney(SpotJourney spotJourney) {
        this.spotJourney = spotJourney;
    }



    public static final DiffUtil.ItemCallback<SpotModel> CALLBACK = new DiffUtil.ItemCallback<SpotModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull SpotModel posts, @NonNull SpotModel t1) {
            return posts.id == t1.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull SpotModel posts, @NonNull SpotModel t1) {
            return true;
        }
    };


    @Override
    public String toString() {
        return "SpotModel{" +
                "id=" + id +
                ", placeName='" + placeName + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", journalId=" + journalId +
                ", status=" + status +
                ", createdAt='" + createdAt + '\'' +
                ", likesCount=" + likesCount +
                ", commCount=" + commCount +
                ", filesCount=" + filesCount +
                ", favouritesCount=" + favouritesCount +
                ", isFavorite=" + isFavorite +
                ", isLiked=" + isLiked +
                ", files=" + files +
                ", publisher=" + publisher +
                ", spotJourney=" + spotJourney +
                '}';
    }
}
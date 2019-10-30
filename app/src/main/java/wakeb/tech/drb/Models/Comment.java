package wakeb.tech.drb.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("user")
    @Expose
    private Publisher user;
    @SerializedName("publishing_id")
    @Expose
    private Integer publishingId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Publisher getUser() {
        return user;
    }

    public void setUser(Publisher user) {
        this.user = user;
    }

    public Integer getPublishingId() {
        return publishingId;
    }

    public void setPublishingId(Integer publishingId) {
        this.publishingId = publishingId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}

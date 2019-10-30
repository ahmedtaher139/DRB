package wakeb.tech.drb.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notifications {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("pid")
    @Expose
    private Integer pid;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("title_ar")
    @Expose
    private String titleAr;
    @SerializedName("title_en")
    @Expose
    private String titleEn;
    @SerializedName("msg_ar")
    @Expose
    private String msgAr;
    @SerializedName("msg_en")
    @Expose
    private String msgEn;
    @SerializedName("created_at")
    @Expose
    private long createdAt;
    @SerializedName("readed_at")
    @Expose
    private String readedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitleAr() {
        return titleAr;
    }

    public void setTitleAr(String titleAr) {
        this.titleAr = titleAr;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getMsgAr() {
        return msgAr;
    }

    public void setMsgAr(String msgAr) {
        this.msgAr = msgAr;
    }

    public String getMsgEn() {
        return msgEn;
    }

    public void setMsgEn(String msgEn) {
        this.msgEn = msgEn;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getReadedAt() {
        return readedAt;
    }

    public void setReadedAt(String readedAt) {
        this.readedAt = readedAt;
    }

}

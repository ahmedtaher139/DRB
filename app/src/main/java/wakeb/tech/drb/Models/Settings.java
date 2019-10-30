package wakeb.tech.drb.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Settings {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("about_ar")
    @Expose
    private String aboutAr;
    @SerializedName("about_en")
    @Expose
    private String aboutEn;
    @SerializedName("terms_ar")
    @Expose
    private String termsAr;
    @SerializedName("terms_en")
    @Expose
    private String termsEn;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("whats")
    @Expose
    private String whats;
    @SerializedName("youtube")
    @Expose
    private String youtube;
    @SerializedName("facebook")
    @Expose
    private String facebook;
    @SerializedName("twitter")
    @Expose
    private String twitter;
    @SerializedName("linked")
    @Expose
    private String linked;
    @SerializedName("created_at")
    @Expose
    private Object createdAt;
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAboutAr() {
        return aboutAr;
    }

    public void setAboutAr(String aboutAr) {
        this.aboutAr = aboutAr;
    }

    public String getAboutEn() {
        return aboutEn;
    }

    public void setAboutEn(String aboutEn) {
        this.aboutEn = aboutEn;
    }

    public String getTermsAr() {
        return termsAr;
    }

    public void setTermsAr(String termsAr) {
        this.termsAr = termsAr;
    }

    public String getTermsEn() {
        return termsEn;
    }

    public void setTermsEn(String termsEn) {
        this.termsEn = termsEn;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWhats() {
        return whats;
    }

    public void setWhats(String whats) {
        this.whats = whats;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getLinked() {
        return linked;
    }

    public void setLinked(String linked) {
        this.linked = linked;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

}

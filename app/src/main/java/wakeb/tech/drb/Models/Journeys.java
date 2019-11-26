package wakeb.tech.drb.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Journeys {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("spots")
    @Expose
    private Integer spots;
    @SerializedName("file")
    @Expose
    private Object file;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getSpots() {
        return spots;
    }

    public void setSpots(Integer spots) {
        this.spots = spots;
    }

    public Object getFile() {
        return file;
    }

    public void setFile(Object file) {
        this.file = file;
    }

}

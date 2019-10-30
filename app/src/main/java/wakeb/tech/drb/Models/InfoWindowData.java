package wakeb.tech.drb.Models;

public class InfoWindowData {

    String id , image_url , desc , type , date ;

    public InfoWindowData(String id, String image_url, String desc, String type, String date) {
        this.id = id;
        this.image_url = image_url;
        this.desc = desc;
        this.type = type;
        this.date = date;
    }

    public InfoWindowData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

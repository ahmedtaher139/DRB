package wakeb.tech.drb.Home;

public class EventBusMssg_home {

    String desc , status , privacy ;

    public EventBusMssg_home(String desc, String status, String privacy) {
        this.desc = desc;
        this.status = status;
        this.privacy = privacy;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }
}

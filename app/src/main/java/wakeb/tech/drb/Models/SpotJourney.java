package wakeb.tech.drb.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpotJourney {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("spots_count")
    @Expose
    private Integer spotsCount;
    @SerializedName("journey_spots")
    @Expose
    private List<JourneySpots> journeySpots = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSpotsCount() {
        return spotsCount;
    }

    public void setSpotsCount(Integer spotsCount) {
        this.spotsCount = spotsCount;
    }

    public List<JourneySpots> getJourneySpots() {
        return journeySpots;
    }

    public void setJourneySpots(List<JourneySpots> journeySpots) {
        this.journeySpots = journeySpots;
    }

}

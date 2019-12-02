package wakeb.tech.drb.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MapSpots {
    @SerializedName("search")
    @Expose
    private String search;
    @SerializedName("search_id")
    @Expose
    private Integer searchId;
    @SerializedName("search_type")
    @Expose
    private String searchType;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("map_spot")
    @Expose
    private MapSpot mapSpot;

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Integer getSearchId() {
        return searchId;
    }

    public void setSearchId(Integer searchId) {
        this.searchId = searchId;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public MapSpot getMapSpot() {
        return mapSpot;
    }

    public void setMapSpot(MapSpot mapSpot) {
        this.mapSpot = mapSpot;
    }

}

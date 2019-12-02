package wakeb.tech.drb.Models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyMarker implements ClusterItem {

    private final LatLng mPosition;
    private final String name;
    private final MapSpots mapSpots;

    public MyMarker(double lat, double lng, String name, MapSpots mapSpots) {
        mPosition = new LatLng(lat, lng);
        this.name = name;
        this.mapSpots = mapSpots;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    public String getName() {
        return name;
    }

    public MapSpots getMapSpots() {
        return mapSpots;
    }
}

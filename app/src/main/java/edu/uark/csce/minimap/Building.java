package edu.uark.csce.minimap;

import com.google.android.gms.maps.model.LatLng;

public class Building
{
    String buildingName;
    double latitude;
    double longitude;
    LatLng buildingLocation;
    boolean heatmapAvailable;
    LatLng[] polygon;

    public Building(String buildingName, double latitude, double longitude, boolean heatmapAvailable) {
        this.buildingName = buildingName;
        this.latitude = latitude;
        this.longitude = longitude;
        buildingLocation = new LatLng(latitude, longitude);
        this.heatmapAvailable = heatmapAvailable;
    }

    public Building(String buildingName, double latitude, double longitude, boolean heatmapAvailable, double lat1, double lon1, double lat2, double lon2, double lat3, double lon3, double lat4, double lon4) {
        this.buildingName = buildingName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.heatmapAvailable = heatmapAvailable;
        polygon = new LatLng[]{new LatLng(lat1, lon1), new LatLng(lat2, lon2), new LatLng(lat3, lon3), new LatLng(lat4, lon4)};
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isHeatmapAvailable() {
        return heatmapAvailable;
    }

    public void setHeatmapAvailable(boolean heatmapAvailable) {
        this.heatmapAvailable = heatmapAvailable;
    }

    public LatLng getBuildingLocation() {
        return buildingLocation;
    }

    public void setBuildingLocation(LatLng buildingLocation) {
        this.buildingLocation = buildingLocation;
    }

    public LatLng[] getPolygon() {
        return polygon;
    }

    public void setPolygon(LatLng[] polygon) {
        this.polygon = polygon;
    }
}
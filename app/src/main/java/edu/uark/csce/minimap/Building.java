package edu.uark.csce.minimap;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Building
{
    String buildingName;
    double latitude;
    double longitude;
    LatLng buildingLocation;
    boolean heatmapAvailable;
    public LatLng[] polygon;

    public Building(String buildingName, double latitude, double longitude, boolean heatmapAvailable) {
        this.buildingName = buildingName;
        this.latitude = latitude;
        this.longitude = longitude;
        buildingLocation = new LatLng(latitude, longitude);
        this.heatmapAvailable = heatmapAvailable;
    }

    public Building(String buildingName, double latitude, double longitude, boolean heatmapAvailable, LatLng[] coordinates) {
        this.buildingName = buildingName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.heatmapAvailable = heatmapAvailable;
        this.polygon = coordinates;//new LatLng[]{new LatLng(lat1, lon1), new LatLng(lat2, lon2), new LatLng(lat3, lon3), new LatLng(lat4, lon4)};
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
package edu.uark.csce.minimap;

public class Building
{
    String buildingName;
    double latitude;
    double longitude;
    boolean heatmapAvailable;

    public Building(String buildingName, double latitude, double longitude, boolean heatmapAvailable) {
        this.buildingName = buildingName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.heatmapAvailable = heatmapAvailable;
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


}
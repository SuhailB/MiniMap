package edu.uark.csce.minimap;
import java.util.ArrayList;

public class Heatmap
{
    int R;
    int G;
    int B;
    int deviceCount;

    public Heatmap(ArrayList<Integer> values)
    {
        R = values.get(0);
        G = values.get(1);
        B = values.get(2);
        deviceCount = values.get(3);
    }

    public int getR() {
        return R;
    }

    public void setR(int r) {
        R = r;
    }

    public int getG() {
        return G;
    }

    public void setG(int g) {
        G = g;
    }

    public int getB() {
        return B;
    }

    public void setB(int b) {
        B = b;
    }

    public int getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(int deviceCount) {
        this.deviceCount = deviceCount;
    }
}
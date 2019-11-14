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
}
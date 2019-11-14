package edu.uark.csce.minimap.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.lang.reflect.Array;
import java.util.List;

import edu.uark.csce.minimap.Heatmap;
import edu.uark.csce.minimap.MapsActivity;
import edu.uark.csce.minimap.R;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    String[] test = {
            "Mullins Library",
            "Brough Dining Hall",
            "JB-Hunt",
            "The Union",
            "Pat Walker",
            "Campus Bookstore on Dickson"
    };


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), MapsActivity.class);
                intent.putExtra("POSITION", i);
                startActivity(intent);
            }
        });
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                test
        );

        listView.setAdapter(listViewAdapter);
        return view;
    }
}
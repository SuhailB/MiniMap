package edu.uark.csce.minimap.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import edu.uark.csce.minimap.R;



public class DashboardFragment extends Fragment {



    private DashboardViewModel dashboardViewModel;
    String[] test = {
            "Mullins Library                                                                      ",
            "Brough Dining Hall                                                                   ",
            "JB-Hunt                                                                              ",
            "The Union                                                                            ",
            "Pat Walker                                                                           ",
            "Campus Bookstore on Dickson                                                           "
    };


    private void showEditDialog(int position) {
        FragmentManager fm = getFragmentManager();
        BuildingDetailsFragment buildingDetailsFragment = BuildingDetailsFragment.newInstance(String.valueOf(position));
        buildingDetailsFragment.show(fm, "fragment_edit_name");
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);


//        NavController navController = Navigation.findNavController(view);//NavHostFragment.findNavController(this);



        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//                showEditDialog(0);
                Log.e("Loc clicked:", "did not navigate");
                Bundle data = new Bundle();
                data.putInt("POS", i);
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.navigation_building, data);

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
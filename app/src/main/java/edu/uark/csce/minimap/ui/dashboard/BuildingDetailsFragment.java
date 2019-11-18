package edu.uark.csce.minimap.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import edu.uark.csce.minimap.R;

public class BuildingDetailsFragment extends DialogFragment {

    private Button bviewMap;

    public BuildingDetailsFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static BuildingDetailsFragment newInstance(String title) {
        BuildingDetailsFragment frag = new BuildingDetailsFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        return inflater.inflate(R.layout.fragment_home, container, false);
        return inflater.inflate(R.layout.building_details_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final int position = getArguments().getInt("POS");
        // Get field from view
        bviewMap = (Button) view.findViewById(R.id.mapButton);
        bviewMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Bundle data = new Bundle();
                data.putInt("POS", position);
                NavController navController = Navigation.findNavController(getParentFragment().getView());
                navController.navigate(R.id.navigation_home, data);

            }
        });


    }

}

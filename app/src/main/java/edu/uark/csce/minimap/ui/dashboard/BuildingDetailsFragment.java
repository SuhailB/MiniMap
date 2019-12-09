package edu.uark.csce.minimap.ui.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import edu.uark.csce.minimap.ColorService;
import edu.uark.csce.minimap.R;

public class BuildingDetailsFragment extends DialogFragment {

    private Button bviewMap;
    private int count;
    private TextView textView;
    int position;
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            count = intent.getIntExtra("PRIMECOUNT", 0);

            textView.setText(count +" People");
//            updateBuildingColor(map);
            Log.e("receiver", "Got message: " + count);
        }
    };

    public BroadcastReceiver axciomReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            count = intent.getIntExtra("AXCIOMCOUNT", 0);

            textView.setText(count +" People");
//            updateBuildingColor(map);
            Log.e("receiver", "Got message: " + count);
        }
    };

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
        position = getArguments().getInt("POS");
        textView = (TextView) view.findViewById(R.id.people_count);
        textView.setText("");
        Log.e("POSITION", Integer.toString(position));
        if(position == 0)
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter(ColorService.PRIME));
        else if(position == 2)
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(axciomReceiver, new IntentFilter(ColorService.AXCIOM));
        else
            textView.setText("No HeatMap for this Building");
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

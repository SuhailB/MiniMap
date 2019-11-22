package edu.uark.csce.minimap.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import edu.uark.csce.minimap.Profile;
import edu.uark.csce.minimap.ProfileAdapter;
import edu.uark.csce.minimap.R;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    static final int CREATE_NEWPOST_REQUEST = 1;  // The request code

    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<Profile> list;
    ProfileAdapter profileAdapter;
    FloatingActionButton floatingActionButton;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
//        int num = getArguments().getInt("POS");
//        Log.e("POS", String.valueOf(num));
//        notificationsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        //        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        reference = FirebaseDatabase.getInstance().getReference().child("Profiles");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<Profile>();

                for(DataSnapshot childSnapshot: dataSnapshot.getChildren())
                {
                    Profile p = childSnapshot.getValue(Profile.class);

                    list.add(p);
                }
                profileAdapter = new ProfileAdapter(NotificationsFragment.this.getActivity(), list);
                recyclerView.setAdapter(profileAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(NotificationsFragment.this.getActivity(), "something went wrong", Toast.LENGTH_SHORT);
            }
        });

        floatingActionButton = (FloatingActionButton) root.findViewById(R.id.fabtn_addition);
        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Toast toast = Toast.makeText(getActivity(),
//                        "This is a message displayed in a Toast",
//                        Toast.LENGTH_SHORT);
//
//                toast.show();
                Intent createNewPost = new Intent(getActivity(), CreateNewPost.class);
                startActivityForResult(createNewPost, CREATE_NEWPOST_REQUEST);
            }
        });





        return root;


    }

}
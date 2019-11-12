package edu.uark.csce.minimap.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import edu.uark.csce.minimap.R;
import edu.uark.csce.minimap.databinding.FragmentNotificationsBinding;
import edu.uark.csce.minimap.ui.LoginResultCallbacks;
import es.dmoral.toasty.Toasty;

public class NotificationsFragment extends Fragment implements LoginResultCallbacks {

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FragmentNotificationsBinding fragmentNotificationsBinding = DataBindingUtil.setContentView(getActivity(),R.layout.fragment_notifications);
        fragmentNotificationsBinding.setViewModel(ViewModelProviders.of(
                getActivity(), new LoginViewModelFactory(this))
                .get(NotificationsViewModel.class));

        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
//        final TextView textView = root.findViewById(R.id.text_notifications);
//        notificationsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onSuccess(String message) {
        Toasty.success(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String message) {
        Toasty.error(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
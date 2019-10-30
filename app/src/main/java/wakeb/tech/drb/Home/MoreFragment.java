package wakeb.tech.drb.Home;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.internal.Utils;
import wakeb.tech.drb.Activities.ChangePass;
import wakeb.tech.drb.Activities.ContactUs;
import wakeb.tech.drb.Activities.FavoritesList;
import wakeb.tech.drb.Activities.MyHistory;
import wakeb.tech.drb.Activities.Settings;
import wakeb.tech.drb.Activities.TextsView;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.Profile.FollowersList;
import wakeb.tech.drb.Profile.MyProfile;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Registration.LoginScreen;
import wakeb.tech.drb.Registration.SelectLanguage;
import wakeb.tech.drb.Registration.SplashScreen;
import wakeb.tech.drb.Registration.WelcomeScreen;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.data.DataManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment implements View.OnClickListener {

    private DataManager dataManager;

    LinearLayout  More_faves , More_profile, More_trips_history, More_blocked_list, Settings, More_change_password, More_contactUs, More_aboutUs, More_logOut;

    private AlertDialog languageDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataManager = ((MainApplication) getActivity().getApplication()).getDataManager();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);






        More_profile = (LinearLayout) view.findViewById(R.id.More_profile);
        More_trips_history = (LinearLayout) view.findViewById(R.id.More_trips_history);
        More_blocked_list = (LinearLayout) view.findViewById(R.id.More_blocked_list);
        Settings = (LinearLayout) view.findViewById(R.id.Settings);
        More_contactUs = (LinearLayout) view.findViewById(R.id.More_contactUs);
        More_change_password = (LinearLayout) view.findViewById(R.id.More_change_password);
        More_aboutUs = (LinearLayout) view.findViewById(R.id.More_aboutUs);
        More_logOut = (LinearLayout) view.findViewById(R.id.More_logOut);
        More_faves = (LinearLayout) view.findViewById(R.id.More_faves);


        More_profile.setOnClickListener(this);
        More_trips_history.setOnClickListener(this);
        More_blocked_list.setOnClickListener(this);
        Settings.setOnClickListener(this);
        More_contactUs.setOnClickListener(this);
        More_change_password.setOnClickListener(this);
        More_aboutUs.setOnClickListener(this);
        More_logOut.setOnClickListener(this);
        More_faves.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {



            case R.id.More_profile:

                Intent intent = new Intent(getActivity(), MyProfile.class);
                startActivity(intent);
                break;

            case R.id.More_trips_history:


                startActivity(new Intent(getActivity(), MyHistory.class));
                break;
            case R.id.More_faves:


                startActivity(new Intent(getActivity(), FavoritesList.class));
                break;
            case R.id.More_blocked_list:

                Intent intentBlocks = new Intent(getActivity(), FollowersList.class);
                intentBlocks.putExtra("FLAG", "BLOCK");
                intentBlocks.putExtra("ItemID", dataManager.getID());
                startActivity(intentBlocks);
                break;

            case R.id.Settings:


                startActivity(new Intent(getActivity(), Settings.class));

                break;

            case R.id.More_contactUs:

                startActivity(new Intent(getActivity(), ContactUs.class));
                break;

            case R.id.More_change_password:

                startActivity(new Intent(getActivity(), ChangePass.class));

                break;

            case R.id.More_aboutUs:

                Intent intent_about = new Intent(getActivity(), TextsView.class);
                intent_about.putExtra("FLAG", "ABOUT");
                startActivity(intent_about);

                break;

            case R.id.More_logOut:
                dataManager.setLoggedIn(false);
                startActivity(new Intent(getActivity(), LoginScreen.class));
                getActivity().finish();

                break;

        }
    }
}
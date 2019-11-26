package wakeb.tech.drb.Home;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import wakeb.tech.drb.Activities.ChangePass;
import wakeb.tech.drb.Activities.ContactUs;
import wakeb.tech.drb.Activities.Settings;
import wakeb.tech.drb.Activities.TextsView;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.Profile.FollowersList;
import wakeb.tech.drb.Profile.MyProfile;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Registration.LoginScreen;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.ui.favorites.FavesList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment implements View.OnClickListener {

    private DataManager dataManager;

    LinearLayout More_faves, More_profile, More_blocked_list, Settings, More_change_password, More_contactUs, More_aboutUs, More_logOut;

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
        More_blocked_list = (LinearLayout) view.findViewById(R.id.More_blocked_list);
        Settings = (LinearLayout) view.findViewById(R.id.Settings);
        More_contactUs = (LinearLayout) view.findViewById(R.id.More_contactUs);
        More_change_password = (LinearLayout) view.findViewById(R.id.More_change_password);
        More_aboutUs = (LinearLayout) view.findViewById(R.id.More_aboutUs);
        More_logOut = (LinearLayout) view.findViewById(R.id.More_logOut);
        More_faves = (LinearLayout) view.findViewById(R.id.More_faves);


        More_profile.setOnClickListener(this);
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


            case R.id.More_faves:


                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up, 0, 0)
                        .replace(R.id.Container_new, new FavesList(), FavesList.TAG)
                        .addToBackStack(FavesList.TAG).commit();




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

    @Override
    public void onResume() {
        super.onResume();
    }
}

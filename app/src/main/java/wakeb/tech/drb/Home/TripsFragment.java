package wakeb.tech.drb.Home;


import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import wakeb.tech.drb.Home.Fragments.FollowersList;
import wakeb.tech.drb.Home.Fragments.PublicList;
import wakeb.tech.drb.R;
import wakeb.tech.drb.ui.spots.FollowersSpotsFragment;
import wakeb.tech.drb.ui.spots.PublicSpotsFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class TripsFragment extends Fragment {

    TextView public_text , followers_text;
    View public_view , followers_view;
    RelativeLayout public_layout , followers_layout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trips, container, false);

        public_text = (TextView) view.findViewById(R.id.public_text);
        followers_text = (TextView) view.findViewById(R.id.followers_text);
        public_view = (View) view.findViewById(R.id.public_view);
        followers_view = (View) view.findViewById(R.id.followers_view);
        public_layout = (RelativeLayout) view.findViewById(R.id.public_layout);
        followers_layout = (RelativeLayout) view.findViewById(R.id.followers_layout);


        replaceFragment(new PublicSpotsFragment());

        public_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replaceFragment(new PublicSpotsFragment());

                if (Build.VERSION.SDK_INT < 23) {
                    public_text.setTextAppearance(getActivity(), R.style.boldText);
                    followers_text.setTextAppearance(getActivity(), R.style.normalText);
                } else {
                    public_text.setTextAppearance(R.style.boldText);
                    followers_text.setTextAppearance(R.style.normalText);
                }
                public_view.setVisibility(View.VISIBLE);
                followers_view.setVisibility(View.GONE);

            }
        });

        followers_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replaceFragment(new FollowersSpotsFragment());

                if (Build.VERSION.SDK_INT < 23) {
                    public_text.setTextAppearance(getActivity(), R.style.normalText);
                    followers_text.setTextAppearance(getActivity(), R.style.boldText);
                } else {
                    public_text.setTextAppearance(R.style.normalText);
                    followers_text.setTextAppearance(R.style.boldText);
                }
                public_view.setVisibility(View.GONE);
                followers_view.setVisibility(View.VISIBLE);

            }
        });

        return view;

    }

    private void replaceFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction =
                getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.Container,
                fragment);
        fragmentTransaction.commit();
    }


}
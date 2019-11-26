package wakeb.tech.drb.Home;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wakeb.tech.drb.Home.Fragments.RiskFragment;
import wakeb.tech.drb.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RisksFragment extends Fragment {

    public static final String TAG = RisksFragment.class.getSimpleName();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_risks, container, false);

        replaceFragment(new RiskFragment());
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

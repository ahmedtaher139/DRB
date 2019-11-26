package wakeb.tech.drb.ui.addNewSpot;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import wakeb.tech.drb.Base.BaseFragment;
import wakeb.tech.drb.Models.Journeys;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.databinding.FragmentJourneysListBinding;
import wakeb.tech.drb.ui.addNewSpot.adpters.JourneysAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class JourneysList extends BaseFragment<FragmentJourneysListBinding> {

    public static final String TAG = JourneysList.class.getSimpleName();
    private FragmentJourneysListBinding fragmentJourneysListBinding;
    AddNewViewModel addNewViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addNewViewModel = ViewModelProviders.of(getActivity()).get(AddNewViewModel.class);

        addNewViewModel.getProgressDialog().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                switch (integer) {
                    case 1:
                        CommonUtilities.showStaticDialog(getActivity(), "");
                        break;
                    case 0:
                        CommonUtilities.hideDialog();
                        break;
                }

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_journeys_list;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentJourneysListBinding = getViewDataBinding();


        fragmentJourneysListBinding.previousJourneys.setLayoutManager(new LinearLayoutManager(getActivity()));

        addNewViewModel.getLiveJourneysData().observe(this, new Observer<ArrayList<Journeys>>() {
            @Override
            public void onChanged(ArrayList<Journeys> journeys) {

                fragmentJourneysListBinding.previousJourneys.setAdapter(new JourneysAdapter(getActivity(), journeys , "CHOOSE" , (JourneysAdapter.AttachJourney) getActivity()));

            }
        });

    }


}

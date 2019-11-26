package wakeb.tech.drb.ui.addNewSpot;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import wakeb.tech.drb.Activities.NewResource;
import wakeb.tech.drb.Base.BaseFragment;
import wakeb.tech.drb.Models.Journeys;
import wakeb.tech.drb.Profile.FollowersList;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.databinding.FragmentAddNewSpotBinding;
import wakeb.tech.drb.ui.addNewSpot.adpters.JourneysAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewSpot extends BaseFragment<FragmentAddNewSpotBinding> {
    public static final String TAG = AddNewSpot.class.getSimpleName();
    private FragmentAddNewSpotBinding fragmentAddNewSpotBinding;
    AddNewViewModel addNewViewModel;
    JourneysAdapter.AttachJourney attachJourney;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_add_new_spot;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.add_new_spot));
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_36dp);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addNewViewModel = ViewModelProviders.of(getActivity()).get(AddNewViewModel.class);

        addNewViewModel.getProgressDialog().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                switch (integer)
                {
                    case 1:
                        CommonUtilities.showStaticDialog(getBaseActivity() , "");
                        break;
                    case 0:
                        CommonUtilities.hideDialog();
                        break;
                }

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentAddNewSpotBinding = getViewDataBinding();

        fragmentAddNewSpotBinding.addNewSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getBaseActivity().startActivity(new Intent(getBaseActivity() , NewResource.class));

            }
        });

        fragmentAddNewSpotBinding.startNewJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBaseActivity().startActivity(new Intent(getBaseActivity() , CreateNewJourney.class));
            }
        });

        fragmentAddNewSpotBinding.previousJourneys.setLayoutManager(new LinearLayoutManager(getBaseActivity()));

        addNewViewModel.getLiveJourneysData().observe(this, new Observer<ArrayList<Journeys>>() {
            @Override
            public void onChanged(ArrayList<Journeys> journeys) {

                fragmentAddNewSpotBinding.previousJourneys.setAdapter(new JourneysAdapter(getBaseActivity() , journeys , "ATTACH" , attachJourney));

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case android.R.id.home:
                Toast.makeText(getBaseActivity(), "", Toast.LENGTH_SHORT).show();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }

    @Override
    public void onResume() {
        super.onResume();

        addNewViewModel.getJourneys();
    }
}

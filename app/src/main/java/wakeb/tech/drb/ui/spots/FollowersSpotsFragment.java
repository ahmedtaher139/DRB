package wakeb.tech.drb.ui.spots;


import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import wakeb.tech.drb.Base.BaseFragment;
import wakeb.tech.drb.Models.SpotModel;
import wakeb.tech.drb.R;
import wakeb.tech.drb.databinding.FragmentFollowersSpotsBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowersSpotsFragment extends BaseFragment<FragmentFollowersSpotsBinding> {

    public static final String TAG = FollowersSpotsFragment.class.getSimpleName();
    private FragmentFollowersSpotsBinding followersSpotsBinding;
    SpotViewModel viewModel;


    public FollowersSpotsFragment() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_followers_spots;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(SpotViewModel.class);

        /*addNewViewModel.getProgressDialog().observe(this, new Observer<Integer>() {
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
        });*/
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        followersSpotsBinding = getViewDataBinding();
        viewModel.get_followers_spots();
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        // Create a custom SpanSizeLookup where the first item spans both columns
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {


                if(position % 5 == 0)
                {
                    return 2;
                }
                else {
                    return 1;
                }

            }
        });
        followersSpotsBinding.followersRecycler.setLayoutManager(layoutManager);
        viewModel.getFollowersListLiveData().observe(this, new Observer<PagedList<SpotModel>>() {
            @Override
            public void onChanged(PagedList<SpotModel> doctor_models) {
               SpotsAdapter spotsAdapter = new SpotsAdapter();
                spotsAdapter.submitList(doctor_models);
                followersSpotsBinding.followersRecycler.setAdapter(spotsAdapter);


                doctor_models.addWeakCallback(null, new PagedList.Callback() {
                    @Override
                    public void onChanged(int position, int count) {

                    }

                    @Override
                    public void onInserted(int position, int count) {
                        followersSpotsBinding.emptyList.setVisibility(View.GONE);

                    }

                    @Override
                    public void onRemoved(int position, int count) {

                    }
                });



            }
        });
        followersSpotsBinding.followersSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh_followers();
            }
        });

        viewModel.getFollowers_progress().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                switch (integer)
                {
                    case 1:
                        followersSpotsBinding.followersSwipeLayout.setRefreshing(true);
                        break;
                    case 0:
                        followersSpotsBinding.followersSwipeLayout.setRefreshing(false);
                        break;
                }

            }
        });
    }


}

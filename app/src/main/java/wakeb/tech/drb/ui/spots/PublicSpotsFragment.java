package wakeb.tech.drb.ui.spots;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.View;

import wakeb.tech.drb.Base.BaseFragment;
import wakeb.tech.drb.Models.SpotModel;
import wakeb.tech.drb.R;
import wakeb.tech.drb.databinding.FragmentPublicSpotsBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class PublicSpotsFragment extends BaseFragment<FragmentPublicSpotsBinding> {

    public static final String TAG = PublicSpotsFragment.class.getSimpleName();
    private FragmentPublicSpotsBinding publicSpotsBinding;
    SpotViewModel viewModel;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_public_spots;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(SpotViewModel.class);


    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        publicSpotsBinding = getViewDataBinding();
        viewModel.get_spots();
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
        publicSpotsBinding.publicRecycler.setLayoutManager(layoutManager);
        viewModel.getListLiveData().observe(this, new Observer<PagedList<SpotModel>>() {
            @Override
            public void onChanged(PagedList<SpotModel> doctor_models) {
                SpotsAdapter spotsAdapter = new SpotsAdapter();
                spotsAdapter.submitList(doctor_models);
                publicSpotsBinding.publicRecycler.setAdapter(spotsAdapter);



                doctor_models.addWeakCallback(null, new PagedList.Callback() {
                    @Override
                    public void onChanged(int position, int count) {

                    }

                    @Override
                    public void onInserted(int position, int count) {
                        publicSpotsBinding.emptyList.setVisibility(View.GONE);

                    }

                    @Override
                    public void onRemoved(int position, int count) {

                    }
                });

            }
        });


        publicSpotsBinding.publicSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh_public();
            }
        });

        viewModel.getPublic_progress().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                switch (integer)
                {
                    case 1:
                        publicSpotsBinding.publicSwipeLayout.setRefreshing(true);
                        break;
                    case 0:
                        publicSpotsBinding.publicSwipeLayout.setRefreshing(false);
                        break;
                }

            }
        });
    }


}

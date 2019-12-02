package wakeb.tech.drb.ui.favorites;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import wakeb.tech.drb.Base.BaseFragment;
import wakeb.tech.drb.Models.SpotModel;
import wakeb.tech.drb.R;
import wakeb.tech.drb.databinding.FragmentFavesListBinding;
import wakeb.tech.drb.databinding.FragmentPublicSpotsBinding;
import wakeb.tech.drb.ui.spots.SpotViewModel;
import wakeb.tech.drb.ui.spots.SpotsAdapter;


public class FavesList extends BaseFragment<FragmentFavesListBinding> {

    public static final String TAG = FavesList.class.getSimpleName();
    private FragmentFavesListBinding fragmentFavesListBinding;
    FaveViewModel viewModel;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_faves_list;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(FaveViewModel.class);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentFavesListBinding = getViewDataBinding();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getBaseActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        fragmentFavesListBinding.faveCardView.setLayoutParams(new CardView.LayoutParams( width,  height+ 40));



        viewModel.get_Faves_spots();
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

        fragmentFavesListBinding.faveRecycler.setLayoutManager(layoutManager);

        viewModel.getListLiveData().observe(this, new Observer<PagedList<SpotModel>>() {
            @Override
            public void onChanged(PagedList<SpotModel> doctor_models) {
                SpotsAdapter spotsAdapter = new SpotsAdapter();
                spotsAdapter.submitList(doctor_models);
                fragmentFavesListBinding.faveRecycler.setAdapter(spotsAdapter);
            }
        });

        fragmentFavesListBinding.faveSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh();
            }
        });

        viewModel.getFave_progress().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                switch (integer)
                {
                    case 1:
                        fragmentFavesListBinding.faveSwipeLayout.setRefreshing(true);
                        break;
                    case 0:
                        fragmentFavesListBinding.faveSwipeLayout.setRefreshing(false);
                        break;
                }

            }
        });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.my_favorites));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.more));
    }
}

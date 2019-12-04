package wakeb.tech.drb.ui.searched;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import wakeb.tech.drb.Base.BaseFragment;
import wakeb.tech.drb.Models.SpotModel;
import wakeb.tech.drb.R;
import wakeb.tech.drb.databinding.FragmentFavesListBinding;
import wakeb.tech.drb.databinding.FragmentSearchedListBinding;
import wakeb.tech.drb.ui.favorites.FaveViewModel;
import wakeb.tech.drb.ui.spots.SpotsAdapter;


public class SearchedList extends BaseFragment<FragmentSearchedListBinding> {

    public static final String TAG = SearchedList.class.getSimpleName();
    private FragmentSearchedListBinding fragmentSearchedListBinding;
    SearchedViewModel viewModel;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_searched_list;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(SearchedViewModel.class);

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
        fragmentSearchedListBinding = getViewDataBinding();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getBaseActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        fragmentSearchedListBinding.searchedCardView.setLayoutParams(new CardView.LayoutParams( width,  height+ 40));



        viewModel.get_Searched_spots(getArguments().getString("SEARCH_TYPE") , getArguments().getString("SEARCH_ID"));
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
        fragmentSearchedListBinding.searchedRecycler.setLayoutManager(layoutManager);
        viewModel.getListLiveData().observe(this, new Observer<PagedList<SpotModel>>() {
            @Override
            public void onChanged(PagedList<SpotModel> doctor_models) {
                SpotsAdapter spotsAdapter = new SpotsAdapter();
                spotsAdapter.submitList(doctor_models);
                fragmentSearchedListBinding.searchedRecycler.setAdapter(spotsAdapter);
            }
        });

        fragmentSearchedListBinding.faveSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh();
            }
        });
       /* viewModel.searchedRecycler().observe(this, new Observer<Integer>() {
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
        });*/

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getArguments().getString("SEARCH_NAME"));
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_36dp);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.world));
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }
}

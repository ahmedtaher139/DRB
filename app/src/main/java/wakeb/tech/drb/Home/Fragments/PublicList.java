package wakeb.tech.drb.Home.Fragments;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import wakeb.tech.drb.Activities.TripComments;
import wakeb.tech.drb.Adapters.TripsAdapter;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.Models.PostedTrip;
import wakeb.tech.drb.R;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class PublicList extends Fragment implements TripsAdapter.AdapterCallback {

    RecyclerView postedTrips;
    ApiServices myAPI;
    Retrofit retrofit;
    DataManager dataManager;
    int page_number = 1;
    boolean next = true;
    NestedScrollView postedTrips_NestedScrollView;
    TripsAdapter tripsAdapter;
    ArrayList<PostedTrip> postedTrips_list;
    SwipeRefreshLayout postedTrips_SwipeRefreshLayout;
    RelativeLayout empty_list;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataManager = ((MainApplication) getActivity().getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);
        postedTrips_list = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_public_list, container, false);
        postedTrips = (RecyclerView) view.findViewById(R.id.postedTrips);
        empty_list = (RelativeLayout) view.findViewById(R.id.empty_list);
        postedTrips_NestedScrollView = (NestedScrollView) view.findViewById(R.id.postedTrips_NestedScrollView);
        postedTrips_SwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.postedTrips_SwipeRefreshLayout);
        postedTrips_SwipeRefreshLayout.setRefreshing(true);
        postedTrips_SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                postedTrips_list.clear();
                page_number = 1;
                next = true;
                get_public();
            }
        });
        postedTrips.setLayoutManager(new LinearLayoutManager(getActivity()));
        tripsAdapter = new TripsAdapter(getActivity(), postedTrips_list , dataManager ,retrofit , myAPI , PublicList.this);
        postedTrips.setAdapter(tripsAdapter);
        postedTrips_NestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY == 0) {

                }
                if (scrollY > oldScrollY) {

                }
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    get_public();
                }
            }
        });

        get_public();
        return view;
    }


    void get_public() {

        if (next) {
            Map<String, String> parms = new HashMap<>();
            parms.put("user_id", dataManager.getID());
            parms.put("page", String.valueOf(page_number));
            myAPI.get_public(parms)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ApiResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ApiResponse apiResponse) {


                            if (apiResponse.getStatus()) {
                                page_number++;
                                if (apiResponse.getData().getPublishing().getMeta().getNextPageUrl().equals("")) {
                                    next = false;
                                }
                                if (apiResponse.getData().getPublishing().getPulishings().size()==0)
                                {

                                }
                                else
                                {
                                    postedTrips_list.addAll(apiResponse.getData().getPublishing().getPulishings());
                                    if(apiResponse.getData().getPublishing().getPulishings().size()==0)
                                    {
                                        empty_list.setVisibility(View.VISIBLE);
                                    }
                                    else
                                    {
                                        empty_list.setVisibility(View.GONE);

                                    }
                                }
                                try {
                                    postedTrips_SwipeRefreshLayout.setRefreshing(false);
                                } catch (Exception e) {
                                }
                            } else {
                                Toast.makeText(getActivity(), apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getActivity(), getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                            Log.i("ERROR_RETROFIT", e.getMessage());

                        }

                        @Override
                        public void onComplete() {

                            tripsAdapter.notifyDataSetChanged();
                        }
                    });
        }


    }


    @Override
    public void onItemClicked(String ID) {

        Intent intent = new Intent(getActivity() , TripComments.class);
        intent.putExtra("ItemID" , ID);
       startActivity(intent);
    }
}
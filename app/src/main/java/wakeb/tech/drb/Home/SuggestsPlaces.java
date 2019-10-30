package wakeb.tech.drb.Home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import wakeb.tech.drb.Adapters.RisksAdapter;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.Home.Fragments.RiskFragment;
import wakeb.tech.drb.Models.Risk;
import wakeb.tech.drb.Models.Suggest;
import wakeb.tech.drb.R;
import wakeb.tech.drb.SuggestionPlaces.SuggestedPlaces;
import wakeb.tech.drb.SuggestionPlaces.SuggestionAdapter;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;

public class SuggestsPlaces extends Fragment {


    RecyclerView risks;
    ApiServices myAPI;
    Retrofit retrofit;
    DataManager dataManager;

    NestedScrollView suggests_NestedScrollView;
    SwipeRefreshLayout suggests_SwipeRefreshLayout;
    RelativeLayout empty_list;
    int page_number = 1;
    boolean isLast = false;
    ArrayList<Suggest> suggests_list;
    SuggestionAdapter suggestionAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataManager = ((MainApplication) getActivity().getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_suggests_places, container, false);

        risks = (RecyclerView) view.findViewById(R.id.suggests);
        suggests_NestedScrollView = (NestedScrollView) view.findViewById(R.id.suggests_NestedScrollView);
        empty_list = (RelativeLayout) view.findViewById(R.id.empty_list);
        suggests_list = new ArrayList<>();
        suggests_SwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.suggests_SwipeRefreshLayout);
        risks.setLayoutManager(new LinearLayoutManager(getActivity()));
        suggestionAdapter = new SuggestionAdapter(getActivity(), suggests_list,  dataManager);
        risks.setAdapter(suggestionAdapter);

        suggests_SwipeRefreshLayout.setRefreshing(true);
        suggests_SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                suggests_list.clear();
                isLast = false;
                page_number = 1;
                get_suggests();
            }
        });


        suggests_NestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY == 0) {

                }
                if (scrollY > oldScrollY) {

                }
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {

                    if (!isLast)
                    {
                        get_suggests();
                    }



                }
            }
        });


        get_suggests();
        return view;
    }

    void get_suggests() {

        Map<String, String> parms = new HashMap<>();
        parms.put("user_id", dataManager.getID());
        parms.put("page", String.valueOf(page_number));

        myAPI.list_suggest(parms)
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
                            if (apiResponse.getData().getSuggests().getMeta().getNextPageUrl().equals("")) {
                                isLast = true;
                            }
                            if( apiResponse.getData().getSuggests().getSuggests().size()==0)
                            {
                                empty_list.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                empty_list.setVisibility(View.GONE);

                            }
                            suggests_list.addAll(apiResponse.getData().getSuggests().getSuggests());
                        } else {
                            Toast.makeText(getActivity(), apiResponse.getMsg() , Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        Log.i("RETROFIT_ERROR" , e.getMessage());
                       
                    }

                    @Override
                    public void onComplete() {
                        suggestionAdapter.notifyDataSetChanged();
                        try {
                            suggests_SwipeRefreshLayout.setRefreshing(false);
                        } catch (Exception e) {
                        }

                        try {

                        } catch (Exception e) {
                        }
                    }
                });
    }

}

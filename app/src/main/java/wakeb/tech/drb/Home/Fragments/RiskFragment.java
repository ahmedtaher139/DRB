package wakeb.tech.drb.Home.Fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
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
import wakeb.tech.drb.Models.Risk;
import wakeb.tech.drb.R;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class RiskFragment extends Fragment implements RisksAdapter.RiskRefresh_Interface {


    RecyclerView risks;
    ApiServices myAPI;
    Retrofit retrofit;
    DataManager dataManager;

    NestedScrollView risks_NestedScrollView;
    SwipeRefreshLayout risks_SwipeRefreshLayout;
    RelativeLayout empty_list;
    int page_number = 1;
    boolean isLast = false;
    ArrayList<Risk> risks_list;
    RisksAdapter risksAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataManager = ((MainApplication) getActivity().getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_risk, container, false);

        risks = (RecyclerView) view.findViewById(R.id.risks);
        risks_NestedScrollView = (NestedScrollView) view.findViewById(R.id.risks_NestedScrollView);
        empty_list = (RelativeLayout) view.findViewById(R.id.empty_list);
         risks_list = new ArrayList<>();
        risks_SwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.risks_SwipeRefreshLayout);
        risks.setLayoutManager(new LinearLayoutManager(getActivity()));
        risksAdapter = new RisksAdapter(getActivity(), risks_list, RiskFragment.this, dataManager, myAPI);
        risks.setAdapter(risksAdapter);

        risks_SwipeRefreshLayout.setRefreshing(true);
        risks_SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                risks_list.clear();
                isLast = false;
                page_number = 1;
                get_risks();
            }
        });


        risks_NestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY == 0) {

                }
                if (scrollY > oldScrollY) {

                }
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {

                    if (!isLast)
                    {
                        get_risks();
                    }



                }
            }
        });


        get_risks();
        return view;
    }


    void get_risks() {

        if (!isLast) {
            Map<String, String> parms = new HashMap<>();
            parms.put("publisher_id", dataManager.getID());
            parms.put("page", String.valueOf(page_number));
            myAPI.get_risks(parms)
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
                                if (apiResponse.getData().getRisks().getMeta().getNextPageUrl().equals("")) {
                                    isLast = true;
                                }
                                if (apiResponse.getData().getRisks().getRisks().size() == 0) {
                                    empty_list.setVisibility(View.VISIBLE);
                                } else {
                                    empty_list.setVisibility(View.GONE);

                                }


                                risks_list.addAll(apiResponse.getData().getRisks().getRisks());
                            } else {
                                Toast.makeText(getActivity(), apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getActivity(), getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                            Log.i("ERROR_RETROFIT", e.getMessage());

                        }

                        @Override
                        public void onComplete() {
                            risksAdapter.notifyDataSetChanged();
                            try {
                                risks_SwipeRefreshLayout.setRefreshing(false);
                            } catch (Exception e) {
                            }


                        }
                    });
        }

    }

    @Override
    public void refresh() {
        risks_SwipeRefreshLayout.setRefreshing(true);
        risks_list.clear();
        isLast = false;
        page_number = 1;
        get_risks();
    }

}

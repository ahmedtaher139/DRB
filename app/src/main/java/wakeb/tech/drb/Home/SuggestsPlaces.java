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
         risks.setLayoutManager(new LinearLayoutManager(getActivity()));
        suggestionAdapter = new SuggestionAdapter(getActivity(), suggests_list,  dataManager);
        risks.setAdapter(suggestionAdapter);
        empty_list.setVisibility(View.VISIBLE);


        return view;
    }


}

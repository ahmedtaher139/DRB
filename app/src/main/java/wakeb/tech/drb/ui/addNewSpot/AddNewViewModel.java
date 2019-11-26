package wakeb.tech.drb.ui.addNewSpot;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.Models.Journeys;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;
import wakeb.tech.drb.data.Retrofit.RetrofitClient;

public class AddNewViewModel extends AndroidViewModel {
    DataManager dataManager;
    ApiServices myAPI;
    Retrofit retrofit;

    private MutableLiveData<ArrayList<Journeys>> liveJourneysData;
    private final MutableLiveData<Integer> progressDialog;

    public AddNewViewModel(@NonNull Application application) {
        super(application);

        dataManager = ((MainApplication) getApplication()).getDataManager();
        retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(ApiServices.class);

        liveJourneysData = new MutableLiveData<>();
        progressDialog = new MutableLiveData<>();

        getJourneys();

    }


    public void showProgress() {

        progressDialog.postValue(1);

    }

    public void hideProgress() {
        progressDialog.postValue(0);


    }

    void getJourneys() {

        showProgress();
        Map<String, String> parms = new HashMap<>();
        parms.put("publisher_id", dataManager.getID());
        myAPI.get_journeys(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {

                        hideProgress();

                        if (apiResponse.getStatus()) {

                            liveJourneysData.postValue(apiResponse.getData().getJourneys());

                        } else {

                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("ERROR_RETROFIT", e.getMessage());
                        hideProgress();

                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }

    public MutableLiveData<ArrayList<Journeys>> getLiveJourneysData() {
        return liveJourneysData;
    }

    public MutableLiveData<Integer> getProgressDialog() {
        return progressDialog;
    }


}

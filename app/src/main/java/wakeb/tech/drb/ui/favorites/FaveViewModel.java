package wakeb.tech.drb.ui.favorites;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import wakeb.tech.drb.Base.MainApplication;
import wakeb.tech.drb.Models.SpotModel;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.ui.spots.SpotsDataSource;
import wakeb.tech.drb.ui.spots.SpotsDataSourceFactory;

public class FaveViewModel extends AndroidViewModel {

    Executor executor;
    FavesSpotsDataSourceFactory spotsDataSourceFactory;
    MutableLiveData<FavesSpotsDataSource> spots_modelMutableLiveData;
    LiveData<PagedList<SpotModel>> listLiveData;
    DataManager dataManager;


    public FaveViewModel(@NonNull Application application) {
        super(application);
        dataManager = ((MainApplication) getApplication()).getDataManager();

    }

    public void get_Faves_spots( ) {
        spotsDataSourceFactory = new FavesSpotsDataSourceFactory(dataManager);
        spots_modelMutableLiveData = spotsDataSourceFactory.getMutableLiveData();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(5)
                .setPageSize(10)
                .setPrefetchDistance(4)
                .build();
        executor = Executors.newFixedThreadPool(5);
        listLiveData = (new LivePagedListBuilder<Long, SpotModel>(spotsDataSourceFactory, config))
                .setFetchExecutor(executor)
                .build();

    }

    public LiveData<PagedList<SpotModel>> getListLiveData() {
        return listLiveData;
    }
}

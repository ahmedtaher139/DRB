package wakeb.tech.drb.ui.spots;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import wakeb.tech.drb.data.DataManager;


public class FollowersSpotsDataSourceFactory extends DataSource.Factory {
    FollowersSpotsDataSource doctorsDataSource;
    MutableLiveData<FollowersSpotsDataSource> mutableLiveData;
    DataManager dataManager;
    SpotViewModel viewModel;
    public FollowersSpotsDataSourceFactory(DataManager dataManager ,  SpotViewModel viewModel) {
        this.dataManager = dataManager;
        this.viewModel = viewModel;
        mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource create() {
        doctorsDataSource = new FollowersSpotsDataSource(  dataManager ,viewModel );
        mutableLiveData.postValue(doctorsDataSource);
        return doctorsDataSource;

    }

    public MutableLiveData<FollowersSpotsDataSource> getMutableLiveData() {
        return mutableLiveData;
    }

    public FollowersSpotsDataSource getDataSource() {
        return doctorsDataSource;
    }

}

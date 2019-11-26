package wakeb.tech.drb.ui.spots;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import wakeb.tech.drb.data.DataManager;


public class SpotsDataSourceFactory extends DataSource.Factory {
    SpotsDataSource doctorsDataSource;
    MutableLiveData<SpotsDataSource> mutableLiveData;
    DataManager dataManager;

    SpotViewModel viewModel;

    public SpotsDataSourceFactory(DataManager dataManager , SpotViewModel viewModel) {
        this.dataManager = dataManager;
        this.viewModel = viewModel;
        mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource create() {
        doctorsDataSource = new SpotsDataSource(  dataManager ,viewModel  );
        mutableLiveData.postValue(doctorsDataSource);
        return doctorsDataSource;

    }

    public MutableLiveData<SpotsDataSource> getMutableLiveData() {
        return mutableLiveData;
    }

    public SpotsDataSource getDataSource() {
        return doctorsDataSource;
    }

}

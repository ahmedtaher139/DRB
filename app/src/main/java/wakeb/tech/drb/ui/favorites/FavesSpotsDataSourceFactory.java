package wakeb.tech.drb.ui.favorites;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import wakeb.tech.drb.data.DataManager;


public class FavesSpotsDataSourceFactory extends DataSource.Factory {
    FavesSpotsDataSource doctorsDataSource;
    MutableLiveData<FavesSpotsDataSource> mutableLiveData;
    DataManager dataManager;

    public FavesSpotsDataSourceFactory(DataManager dataManager) {
        this.dataManager = dataManager;
        mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource create() {
        doctorsDataSource = new FavesSpotsDataSource(  dataManager );
        mutableLiveData.postValue(doctorsDataSource);
        return doctorsDataSource;

    }

    public MutableLiveData<FavesSpotsDataSource> getMutableLiveData() {
        return mutableLiveData;
    }

    public FavesSpotsDataSource getDataSource() {
        return doctorsDataSource;
    }

}

package wakeb.tech.drb.Adapters;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wakeb.tech.drb.Home.HomeInterface;
import wakeb.tech.drb.Models.Resource;
import wakeb.tech.drb.Models.StorePlace;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Stores.StoresInterface;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.data.Retrofit.ApiResponse;


public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {

    private Context context;
    private List<StorePlace> my_data;

    public PlacesAdapter(Context context, List<StorePlace> my_data) {
        this.context = context;
        this.my_data = my_data;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_place_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final StorePlace model = my_data.get(position);

        holder.AddPlace_address.setText(model.getAddress());
        holder.AddPlace_Desc.setText(model.getDesc());



        holder.AddPlace_remove_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(context instanceof StoresInterface){
                    ((StoresInterface)context).remove_place(model.getId().toString());
                }



            }
        });


    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public TextView AddPlace_address , AddPlace_Desc;
        LinearLayout AddPlace_remove_place;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            AddPlace_address = (TextView) view.findViewById(R.id.AddPlace_address);
            AddPlace_Desc = (TextView) view.findViewById(R.id.AddPlace_Desc);
            AddPlace_remove_place = (LinearLayout) view.findViewById(R.id.AddPlace_remove_place);

        }
    }


}
package wakeb.tech.drb.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import wakeb.tech.drb.Activities.SpotScreen;
import wakeb.tech.drb.Models.SpotModel;
import wakeb.tech.drb.R;


public class SpotsAdapter extends RecyclerView.Adapter<SpotsAdapter.ViewHolder> {

    private Context context;
    private List<SpotModel> my_data;

    public SpotsAdapter(Context context, List<SpotModel> my_data) {
        this.context = context;
        this.my_data = my_data;


    }


    public interface Refreshing {
        void Refresh(String FLAG);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spot_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final SpotModel model = my_data.get(position);


        holder.spot_title.setText(model.getPlaceName());
        holder.spot_files_count.setText(String.valueOf(model.getFilesCount()));
        holder.spot_spots_count.setText(String.valueOf(model.getSpotJourney().getSpotsCount()));


        if (model.getFiles().size() > 0) {
            Glide.with(context)
                    .load("http://3.17.76.229/" + model.getFiles().get(0).getFile())
                    .apply(new RequestOptions()
                            .placeholder(holder.spot_image.getDrawable())
                    )
                    .into(holder.spot_image);

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context , SpotScreen.class);
                    intent.putExtra("SPOT_ID" , String.valueOf(model.getId()));
                    context.startActivity(intent);
                }
            });




        }


    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        View view;
        ImageView spot_image;
        TextView spot_title, spot_files_count, spot_spots_count;


        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            spot_image = (ImageView) view.findViewById(R.id.spot_image);
            spot_title = (TextView) view.findViewById(R.id.spot_title);
            spot_files_count = (TextView) view.findViewById(R.id.spot_files_count);
            spot_spots_count = (TextView) view.findViewById(R.id.spot_spots_count);


        }
    }


}
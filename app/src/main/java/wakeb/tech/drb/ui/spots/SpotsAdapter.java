package wakeb.tech.drb.ui.spots;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import wakeb.tech.drb.Activities.SpotScreen;
import wakeb.tech.drb.Models.SpotModel;
import wakeb.tech.drb.Profile.MyProfile;
import wakeb.tech.drb.R;

public class SpotsAdapter extends PagedListAdapter<SpotModel, SpotsAdapter.ViewHolder> {

    public SpotsAdapter() {
        super(SpotModel.CALLBACK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.spot_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpotsAdapter.ViewHolder holder, int position) {


        final SpotModel model = getItem(position);







        holder.spot_files_count.setText(String.valueOf(model.getFilesCount()));
        holder.spot_spots_count.setText(String.valueOf(model.getSpotJourney().getSpotsCount() + ""));


        if(model.getJournalId()==null)
        {
            holder.posted_comment_layout.setVisibility(View.GONE);
            holder.spot_title.setText(model.getPlaceName());
        }
        else
        {
            holder.spot_title.setText(model.getSpotJourney().getName());

        }

        if (model.getFiles().size() > 0) {

            Glide.with(holder.itemView.getContext())
                    .load("http://3.17.76.229/" + model.getFiles().get(0).getFile())
                    .apply(new RequestOptions()
                            .placeholder(holder.spot_image.getDrawable())
                    )
                    .into(holder.spot_image);


        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), SpotScreen.class);
                intent.putExtra("SPOT_ID", String.valueOf(model.getId()));
                holder.itemView.getContext().startActivity(intent);
            }
        });


    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;

        ImageView spot_image;
        TextView spot_title, spot_files_count, spot_spots_count;
        LinearLayout posted_comment_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            spot_image = (ImageView) view.findViewById(R.id.spot_image);
            spot_title = (TextView) view.findViewById(R.id.spot_title);
            spot_files_count = (TextView) view.findViewById(R.id.spot_files_count);
            spot_spots_count = (TextView) view.findViewById(R.id.spot_spots_count);
            posted_comment_layout = (LinearLayout) view.findViewById(R.id.posted_comment_layout);

        }
    }


}
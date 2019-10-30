package wakeb.tech.drb.SuggestionPlaces;

import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import wakeb.tech.drb.Activities.ShowImage;
import wakeb.tech.drb.Activities.ShowOnMap;
import wakeb.tech.drb.Activities.ViewRecourse;
import wakeb.tech.drb.Models.Suggest;
import wakeb.tech.drb.Profile.MyProfile;
import wakeb.tech.drb.Profile.UserProfile;
import wakeb.tech.drb.R;
import wakeb.tech.drb.data.DataManager;


public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.ViewHolder> {

    private Context context;
    private List<Suggest> my_data;
    DataManager dataManager;

    public SuggestionAdapter(Context context, List<Suggest> my_data, DataManager dataManager) {
        this.context = context;
        this.my_data = my_data;
        this.dataManager = dataManager;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggest_place_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Suggest model = my_data.get(position);

        holder.suggest_item_name.setText(model.getPublisher().getDisplayName());
        holder.suggest_item_date.setText(model.getCreatedAt());
        holder.suggest_tv_startTrip.setText(model.getAddress());

        if (model.getImage().toLowerCase().contains("null".toLowerCase())) {
            holder.suggest_item_snapshot.setVisibility(View.GONE);
        }


        holder.suggest_item_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (String.valueOf(model.getPublisher().getId()).equals(dataManager.getID())) {
                    Intent intent = new Intent(context, MyProfile.class);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, UserProfile.class);
                    intent.putExtra("ItemID", String.valueOf(model.getPublisher().getId()));
                    context.startActivity(intent);
                }

            }
        });
        holder.suggest_item_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (String.valueOf(model.getPublisher().getId()).equals(dataManager.getID())) {
                    Intent intent = new Intent(context, MyProfile.class);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, UserProfile.class);
                    intent.putExtra("ItemID", String.valueOf(model.getPublisher().getId()));
                    context.startActivity(intent);
                }

            }
        });

        holder.suggest_item_snapshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ShowImage.class);
                intent.putExtra("URL", model.getImage());
                context.startActivity(intent);

            }
        });


        Glide.with(context)
                .load(model.getImage())
                .apply(new RequestOptions()
                        .placeholder(holder.suggest_item_snapshot.getDrawable())
                )
                .into(holder.suggest_item_snapshot);

        Glide.with(context)
                .load("http://3.17.76.229/uploads/publishers/"+model.getPublisher().getImage())
                .apply(new RequestOptions()
                        .placeholder(holder.suggest_item_Image.getDrawable())
                )
                .into(holder.suggest_item_Image);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, ShowOnMap.class);
                intent.putExtra("LAT", String.valueOf(model.getLat()));
                intent.putExtra("LONG", String.valueOf(model.getLng()));
                context.startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public View view;
        TextView suggest_item_name, suggest_item_date, suggest_tv_startTrip;
        ImageView suggest_item_snapshot;
        CircleImageView suggest_item_Image;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            suggest_item_name = (TextView) view.findViewById(R.id.suggest_item_name);
            suggest_item_date = (TextView) view.findViewById(R.id.suggest_item_date);
            suggest_tv_startTrip = (TextView) view.findViewById(R.id.suggest_tv_startTrip);
            suggest_item_Image = (CircleImageView) view.findViewById(R.id.suggest_item_Image);
            suggest_item_snapshot = (ImageView) view.findViewById(R.id.suggest_item_snapshot);

        }
    }


}
package wakeb.tech.drb.ui.addNewSpot.adpters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import wakeb.tech.drb.Activities.NewResource;
import wakeb.tech.drb.Models.Journeys;
import wakeb.tech.drb.R;

public class JourneysAdapter extends RecyclerView.Adapter<JourneysAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Journeys> my_data;
    private String FLAG;
    private AttachJourney attachJourney;

    public interface AttachJourney {


        void attach(String id, String name, String desc);

    }


    public JourneysAdapter(Context context, ArrayList<Journeys> my_data, String FLAG, AttachJourney attachJourney) {
        this.context = context;
        this.my_data = my_data;
        this.FLAG = FLAG;
        this.attachJourney = attachJourney;

    }


    public interface Refreshing {
        void Refresh(String FLAG);
    }

    @Override
    public JourneysAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.joureny_item, parent, false);

        return new JourneysAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final JourneysAdapter.ViewHolder holder, final int position) {

        final Journeys model = my_data.get(position);


        holder.journey_name.setText(model.getName());
        holder.journey_desc.setText(model.getDesc());
        holder.journey_spots_count.setText(String.valueOf(model.getSpots()));




        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FLAG.equals("CHOOSE")) {


                    if (context instanceof AttachJourney) {
                        ((AttachJourney) context).attach(String.valueOf(model.getId()), model.getName(), model.getDesc());
                    }


                } else {
                    Intent intent = new Intent(context, NewResource.class);
                    intent.putExtra("ID", String.valueOf(model.getId()));
                    intent.putExtra("NAME", model.getName());
                    intent.putExtra("DESC", model.getDesc());
                    context.startActivity(intent);
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        View view;
         TextView journey_spots_count, journey_name, journey_desc;


        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;

             journey_name = (TextView) view.findViewById(R.id.journey_name);
            journey_desc = (TextView) view.findViewById(R.id.journey_desc);
            journey_spots_count = (TextView) view.findViewById(R.id.journey_spots_count);


        }
    }


}
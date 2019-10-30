package wakeb.tech.drb.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import retrofit2.Retrofit;
import wakeb.tech.drb.Dialogs.ChangePrivacy;
import wakeb.tech.drb.Models.PostedTrip;
import wakeb.tech.drb.R;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiServices;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<PostedTrip> my_data;
    private DataManager dataManager;
    private Retrofit retrofit;
    private ApiServices myAPI;


    public interface HistoryAdapterCallback {
        void delete(String ID);

        void edit(String ID , String privacy);

        void post(String ID, String post);
    }

    public HistoryAdapter(Context context, List<PostedTrip> my_data, DataManager dataManager, Retrofit retrofit, ApiServices myAPI) {
        this.context = context;
        this.my_data = my_data;
        this.dataManager = dataManager;
        this.retrofit = retrofit;
        this.myAPI = myAPI;
    }


    @Override
    public int getItemCount() {
        return my_data.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        return new ViewHolder0(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_trips_item, parent, false));

    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final PostedTrip model = my_data.get(position);


        final ViewHolder0 viewHolder0 = (ViewHolder0) holder;
        viewHolder0.posted_tv_startTrip.setText(model.getTrip().getStartAddress());
        viewHolder0.posted_tv_endTrip.setText(model.getTrip().getEndAddress());

        if (String.valueOf(model.getStatus()).equals("0")) {
             viewHolder0.post_text.setText(context.getString(R.string.post));
        } else if (String.valueOf(model.getStatus()).equals("1")) {
            viewHolder0.post_text.setText(context.getString(R.string.un_post));
        }
        String upperString = model.getPrivacy().substring(0,1).toUpperCase() + model.getPrivacy().substring(1);

        viewHolder0.edit_text.setText(upperString);

        Glide.with(context)
                .load(model.getTrip().getMapScreenShot())
                .apply(new RequestOptions()
                        .placeholder(viewHolder0.posted_item_snapshot.getDrawable())
                )
                .into(viewHolder0.posted_item_snapshot);


        viewHolder0.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (context instanceof HistoryAdapterCallback) {
                    ((HistoryAdapterCallback) context).delete(String.valueOf(model.getTrip().getId()));
                }


            }
        });

        viewHolder0.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChangePrivacy cdd = new ChangePrivacy((Activity) context,String.valueOf( model.getId()));
                cdd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


                Window window = cdd.getWindow();
                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);


                WindowManager.LayoutParams lp = window.getAttributes();
                lp.dimAmount = 0.7f;
                lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                cdd.getWindow().setAttributes(lp);

                cdd.show();


            }
        });

        viewHolder0.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(model.getStatus()).equals("0")) {
                    if (context instanceof HistoryAdapterCallback) {
                        ((HistoryAdapterCallback) context).post(String.valueOf(model.getTrip().getId()) , "1");
                    };
                } else if (String.valueOf(model.getStatus()).equals("1")) {
                    if (context instanceof HistoryAdapterCallback) {
                        ((HistoryAdapterCallback) context).post(String.valueOf(model.getTrip().getId()), "0");
                    }

                }


            }
        });


    }


    class ViewHolder0 extends RecyclerView.ViewHolder {

        TextView posted_tv_startTrip,
                posted_tv_endTrip;

        ImageView posted_item_snapshot;

        RelativeLayout delete, post, edit;
        TextView delete_text, post_text, edit_text;


        View view;


        public ViewHolder0(View itemView) {
            super(itemView);
            view = itemView;

            posted_tv_startTrip = (TextView) itemView.findViewById(R.id.posted_tv_startTrip);
            posted_tv_endTrip = (TextView) itemView.findViewById(R.id.posted_tv_endTrip);
            posted_item_snapshot = (ImageView) itemView.findViewById(R.id.posted_item_snapshot);

            delete = (RelativeLayout) itemView.findViewById(R.id.delete);
            post = (RelativeLayout) itemView.findViewById(R.id.post);
            edit = (RelativeLayout) itemView.findViewById(R.id.edit);

            delete_text = (TextView) itemView.findViewById(R.id.delete_text);
            post_text = (TextView) itemView.findViewById(R.id.post_text);
            edit_text = (TextView) itemView.findViewById(R.id.edit_text);

        }
    }


}

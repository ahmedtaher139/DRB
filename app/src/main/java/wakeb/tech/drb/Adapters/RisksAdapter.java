package wakeb.tech.drb.Adapters;

import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wakeb.tech.drb.Activities.ShowImage;
import wakeb.tech.drb.Activities.ShowOnMap;
import wakeb.tech.drb.Activities.ViewRecourse;
import wakeb.tech.drb.Models.Risk;
import wakeb.tech.drb.Models.StorePlace;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Risks.RiskTypes_Interface;
import wakeb.tech.drb.Stores.StoresInterface;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;


public class RisksAdapter extends RecyclerView.Adapter<RisksAdapter.ViewHolder> {

    private Context context;
    private List<Risk> my_data;
    private RiskRefresh_Interface riskRefresh_interface;
    private DataManager dataManager;
    private ApiServices myAPI;


    public RisksAdapter(Context context, List<Risk> my_data, RiskRefresh_Interface riskRefresh_interface, DataManager dataManager, ApiServices myAPI) {
        this.context = context;
        this.my_data = my_data;
        this.riskRefresh_interface = riskRefresh_interface;
        this.dataManager = dataManager;
        this.myAPI = myAPI;
    }


    public interface RiskRefresh_Interface {
        void refresh();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.risk_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Risk model = my_data.get(position);

        holder.risk_address.setText(model.getAddress());
        holder.risk_desc.setText(model.getDesc());
        holder.risk_like_text.setText(String.valueOf(model.getYes()));
        holder.risk_dislike_textm.setText(String.valueOf(model.getNo()));
        holder.risk_type.setText(model.getRiskType().getNameAr());
        holder.risk_date.setText(DateUtils.getRelativeTimeSpanString(model.getCreatedAt(),
                System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS).toString());






        if (model.getVote()) {

            if (model.getAct().equals("yes")) {


                holder.risk_like_image.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                holder.risk_like_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.light_red));

                holder.risk_dislike_image.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN);
                holder.risk_dislike_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));

                holder.risk_like_text.setTextColor(ContextCompat.getColor(context, R.color.white));

                holder.risk_dislike_textm.setTextColor(ContextCompat.getColor(context, R.color.grey));

            } else {

                holder.risk_dislike_image.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                holder.risk_dislike_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.light_red));

                holder.risk_like_image.setColorFilter(ContextCompat.getColor(context, R.color.grey), android.graphics.PorterDuff.Mode.SRC_IN);
                holder.risk_like_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));


                holder.risk_like_text.setTextColor(ContextCompat.getColor(context, R.color.grey));

                holder.risk_dislike_textm.setTextColor(ContextCompat.getColor(context, R.color.white));

            }

        }

        holder.risk_dislike_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                add_vote(model.getId().toString(), "no");

            }
        });

        holder.risk_like_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                add_vote(model.getId().toString(), "yes");

            }
        });


        holder.risk_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =  new Intent(context , ShowImage.class);
                intent.putExtra("URL" , model.getImage());
                context.startActivity(intent);

            }
        });

        if (model.getImage().toLowerCase().contains("null".toLowerCase())) {
            holder.risk_image.setVisibility(View.GONE);
        }

        Glide.with(context)
                .load(model.getRiskType().getIcon())
                .apply(new RequestOptions()
                        .placeholder(holder.posted_item_Image.getDrawable())
                )
                .into(holder.posted_item_Image);

        Glide.with(context)
                .load(model.getImage())
                .apply(new RequestOptions()
                        .placeholder(holder.risk_image.getDrawable())
                )
                .into(holder.risk_image);

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
        TextView risk_address, risk_desc, risk_like_text, risk_dislike_textm , risk_type , risk_date;
        ImageView risk_like_image, risk_dislike_image, risk_image , posted_item_Image;
        RelativeLayout risk_dislike_layout , risk_like_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            risk_address = (TextView) view.findViewById(R.id.risk_address);
            risk_type = (TextView) view.findViewById(R.id.risk_type);
            risk_desc = (TextView) view.findViewById(R.id.risk_desc);
            risk_like_text = (TextView) view.findViewById(R.id.risk_like_text);
            risk_dislike_textm = (TextView) view.findViewById(R.id.risk_dislike_textm);
            risk_date = (TextView) view.findViewById(R.id.risk_date);
            risk_like_image = (ImageView) view.findViewById(R.id.risk_like_image);
            risk_dislike_image = (ImageView) view.findViewById(R.id.risk_dislike_image);
            risk_image = (ImageView) view.findViewById(R.id.risk_image);
            posted_item_Image = (ImageView) view.findViewById(R.id.posted_item_Image);
            risk_dislike_layout = (RelativeLayout) view.findViewById(R.id.risk_dislike_layout);
            risk_like_layout = (RelativeLayout) view.findViewById(R.id.risk_like_layout);

        }
    }

    void add_vote(String risk_id, String vote) {


        Map<String, String> parms = new HashMap<>();
        parms.put("publisher_id", dataManager.getID());
        parms.put("risk_id", risk_id);
        parms.put("vote", vote);
        myAPI.add_comment(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {


                        if (apiResponse.getStatus()) {

                            riskRefresh_interface.refresh();

                        } else {

                            Toast.makeText(context, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, context.getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }


}
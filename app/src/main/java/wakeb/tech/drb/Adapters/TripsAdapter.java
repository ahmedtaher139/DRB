package wakeb.tech.drb.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import wakeb.tech.drb.Activities.RecourseScreen;
import wakeb.tech.drb.Activities.TripComments;
import wakeb.tech.drb.Models.LOG;
import wakeb.tech.drb.Models.PostedTrip;
import wakeb.tech.drb.Profile.MyProfile;
import wakeb.tech.drb.Profile.UserProfile;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Uitils.CommonUtilities;
import wakeb.tech.drb.data.DataManager;
import wakeb.tech.drb.data.Retrofit.ApiResponse;
import wakeb.tech.drb.data.Retrofit.ApiServices;

public class TripsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<PostedTrip> my_data;
    private DataManager dataManager;
    private Retrofit retrofit;
    private ApiServices myAPI;
    AdapterCallback adapterCallback;

    public interface AdapterCallback {
        void onItemClicked(String ID);
    }

    public TripsAdapter(Context context, List<PostedTrip> my_data, DataManager dataManager, Retrofit retrofit, ApiServices myAPI, AdapterCallback adapterCallback) {
        this.context = context;
        this.my_data = my_data;
        this.dataManager = dataManager;
        this.retrofit = retrofit;
        this.myAPI = myAPI;
        this.adapterCallback = adapterCallback;
    }


    class ViewHolder0 extends RecyclerView.ViewHolder {

        TextView posted_item_name, posted_item_date, posted_tv_startTrip,
                posted_tv_endTrip, posted_tv_desc, posted_like_text,
                posted_comment_text , posted_resources_text , posted_faves_text;

        ImageView posted_like_image, posted_comment_image, posted_share_image, posted_item_snapshot, posted_fav_image ;

        CircleImageView posted_item_Image;
        LinearLayout posted_share_layout, posted_like_layout, posted_comment_layout;
        View view;


        public ViewHolder0(View itemView) {
            super(itemView);
            view = itemView;
            posted_item_name = (TextView) itemView.findViewById(R.id.posted_item_name);
            posted_item_date = (TextView) itemView.findViewById(R.id.posted_item_date);
            posted_tv_startTrip = (TextView) itemView.findViewById(R.id.posted_tv_startTrip);
            posted_tv_endTrip = (TextView) itemView.findViewById(R.id.posted_tv_endTrip);
            posted_tv_desc = (TextView) itemView.findViewById(R.id.posted_tv_desc);
            posted_like_text = (TextView) itemView.findViewById(R.id.posted_like_text);
            posted_comment_text = (TextView) itemView.findViewById(R.id.posted_comment_text);
            posted_resources_text = (TextView) itemView.findViewById(R.id.posted_resources_text);
            posted_fav_image = (ImageView) itemView.findViewById(R.id.posted_fav_image);
            posted_like_image = (ImageView) itemView.findViewById(R.id.posted_like_image);
            posted_comment_image = (ImageView) itemView.findViewById(R.id.posted_comment_image);
            posted_share_image = (ImageView) itemView.findViewById(R.id.posted_share_image);
            posted_item_snapshot = (ImageView) itemView.findViewById(R.id.posted_item_snapshot);
            posted_item_Image = (CircleImageView) itemView.findViewById(R.id.posted_item_Image);
            posted_share_layout = (LinearLayout) itemView.findViewById(R.id.posted_share_layout);
            posted_like_layout = (LinearLayout) itemView.findViewById(R.id.posted_like_layout);
            posted_comment_layout = (LinearLayout) itemView.findViewById(R.id.posted_comment_layout);
            posted_faves_text = (TextView) itemView.findViewById(R.id.posted_faves_text);

        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {


        TextView posted_item_name, posted_item_date, posted_tv_startTrip,
                posted_tv_endTrip, posted_tv_desc, posted_like_text,
                posted_comment_text, sharer_item_name , posted_resources_text , posted_faves_text;

        ImageView posted_like_image, posted_comment_image, posted_share_image, posted_item_snapshot, posted_fav_image;

        CircleImageView posted_item_Image, sharer_item_Image;

        LinearLayout posted_share_layout, posted_like_layout, posted_comment_layout;

        View view;


        public ViewHolder2(View itemView) {
            super(itemView);
            view = itemView;
            sharer_item_name = (TextView) itemView.findViewById(R.id.sharer_item_name);
            posted_item_name = (TextView) itemView.findViewById(R.id.posted_item_name);
            posted_item_date = (TextView) itemView.findViewById(R.id.posted_item_date);
            posted_tv_startTrip = (TextView) itemView.findViewById(R.id.posted_tv_startTrip);
            posted_tv_endTrip = (TextView) itemView.findViewById(R.id.posted_tv_endTrip);
            posted_tv_desc = (TextView) itemView.findViewById(R.id.posted_tv_desc);
            posted_like_text = (TextView) itemView.findViewById(R.id.posted_like_text);
            posted_comment_text = (TextView) itemView.findViewById(R.id.posted_comment_text);
            posted_resources_text = (TextView) itemView.findViewById(R.id.posted_resources_text);
            posted_fav_image = (ImageView) itemView.findViewById(R.id.posted_fav_image);
            posted_like_image = (ImageView) itemView.findViewById(R.id.posted_like_image);
            posted_comment_image = (ImageView) itemView.findViewById(R.id.posted_comment_image);
            posted_share_image = (ImageView) itemView.findViewById(R.id.posted_share_image);
            posted_item_snapshot = (ImageView) itemView.findViewById(R.id.posted_item_snapshot);
            posted_item_Image = (CircleImageView) itemView.findViewById(R.id.posted_item_Image);
            sharer_item_Image = (CircleImageView) itemView.findViewById(R.id.sharer_item_Image);
            posted_share_layout = (LinearLayout) itemView.findViewById(R.id.posted_share_layout);
            posted_like_layout = (LinearLayout) itemView.findViewById(R.id.posted_like_layout);
            posted_comment_layout = (LinearLayout) itemView.findViewById(R.id.posted_comment_layout);
            posted_faves_text = (TextView) itemView.findViewById(R.id.posted_faves_text);

        }
    }


    @Override
    public int getItemViewType(int position) {

        PostedTrip model = my_data.get(position);
        if (!model.getIsshare()) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        switch (viewType) {
            case 1:
                return new ViewHolder0(LayoutInflater.from(parent.getContext()).inflate(R.layout.posted_item, parent, false));
            case 2:
                return new ViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.shared_item, parent, false));

        }
        return null;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final PostedTrip model = my_data.get(position);
        switch (holder.getItemViewType()) {
            case 1:
                final ViewHolder0 viewHolder0 = (ViewHolder0) holder;
                viewHolder0.posted_item_name.setText(model.getTrip().getPublisher().getDisplayName());
                viewHolder0.posted_tv_startTrip.setText(model.getTrip().getStartAddress());
                viewHolder0.posted_tv_endTrip.setText(model.getTrip().getEndAddress());
                viewHolder0.posted_tv_desc.setText(model.getTrip().getDesc());
                viewHolder0.posted_like_text.setText(String.valueOf(model.getLikesCount()));
                viewHolder0.posted_comment_text.setText(String.valueOf(model.getComments()));
                viewHolder0.posted_resources_text.setText(String.valueOf(model.getResCount()));
                viewHolder0.posted_faves_text.setText(String.valueOf(model.getFavesCount()));
                viewHolder0.posted_item_date.setText(DateUtils.getRelativeTimeSpanString(model.getCreatedAt(),
                        System.currentTimeMillis(),
                        DateUtils.SECOND_IN_MILLIS).toString());

                if (!model.getTrip().getDesc().equals("")) {
                    viewHolder0.posted_tv_desc.setVisibility(View.VISIBLE);
                }

                if (model.getLikesStatus()) {
                    viewHolder0.posted_like_image.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);
                }

                if (model.getFavStatus()) {
                    viewHolder0.posted_fav_image.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);
                }

                viewHolder0.posted_comment_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, TripComments.class);
                        intent.putExtra("ItemID", model.getId().toString());
                        context.startActivity(intent);
                    }
                });

                viewHolder0.posted_like_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (viewHolder0.posted_like_image.getColorFilter() == null) {
                            CommonUtilities.save_log(myAPI, dataManager.getID(), "LIKE", "publsihing", model.getId().toString());

                            viewHolder0.posted_like_image.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);
                            viewHolder0.posted_like_text.setText(String.valueOf(Integer.parseInt(viewHolder0.posted_like_text.getText().toString()) + 1));
                        } else {
                            CommonUtilities.save_log(myAPI, dataManager.getID(), "DISLIKE", "publsihing", model.getId().toString());

                            viewHolder0.posted_like_image.setColorFilter(null);
                            viewHolder0.posted_like_text.setText(String.valueOf(Integer.parseInt(viewHolder0.posted_like_text.getText().toString()) - 1));
                        }


                        like_action(model.getId().toString());
                    }
                });

                viewHolder0.posted_fav_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Map<String, String> parms = new HashMap<>();
                        parms.put("publishing_id", model.getId().toString());
                        parms.put("user_id", dataManager.getID());
                        myAPI.fav_action(parms)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<ApiResponse>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(ApiResponse apiResponse) {


                                        if (apiResponse.getStatus()) {
                                            if (viewHolder0.posted_fav_image.getColorFilter() == null) {
                                                CommonUtilities.save_log(myAPI, dataManager.getID(), "FAVORITE", "publsihing", model.getId().toString());

                                                viewHolder0.posted_fav_image.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);
                                            } else {
                                                CommonUtilities.save_log(myAPI, dataManager.getID(), "UNFAVORITE", "publsihing", model.getId().toString());

                                                viewHolder0.posted_fav_image.setColorFilter(null);
                                            }

                                        } else {

                                            Toast.makeText(context, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                        }


                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(context, context.getString(R.string.connection_error), Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onComplete() {


                                    }
                                });
                    }
                });

                Glide.with(context)
                        .load(model.getTrip().getMapScreenShot())
                        .apply(new RequestOptions()
                                .placeholder(viewHolder0.posted_item_snapshot.getDrawable())
                        )
                        .into(viewHolder0.posted_item_snapshot);

                Glide.with(context)
                        .load(model.getTrip().getPublisher().getImage())
                        .apply(new RequestOptions()
                                .placeholder(viewHolder0.posted_item_Image.getDrawable())
                        )
                        .into(viewHolder0.posted_item_Image);


                viewHolder0.posted_item_Image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                    }
                });

                viewHolder0.posted_item_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (String.valueOf(model.getTrip().getPublisher().getId()).equals(dataManager.getID())) {
                            Intent intent = new Intent(context, MyProfile.class);
                            intent .putExtra("User_Name" , model.getTrip().getPublisher().getDisplayName());
                            intent .putExtra("User_Image" , model.getTrip().getPublisher().getImage());
                            Pair<View, String> p1 = Pair.create((View)viewHolder0.posted_item_Image, "image");
                            Pair<View, String> p2 = Pair.create((View)viewHolder0.posted_item_name, "text");

                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation((Activity) context, p1,p2);


                            context.startActivity(intent , options.toBundle());
                        } else {
                            Intent intent = new Intent(context, UserProfile.class);
                            intent.putExtra("ItemID", String.valueOf(model.getTrip().getPublisher().getId()));
                            intent .putExtra("User_Name" , model.getTrip().getPublisher().getDisplayName());
                            intent .putExtra("User_Image" , model.getTrip().getPublisher().getImage());
                            Pair<View, String> p1 = Pair.create((View)viewHolder0.posted_item_Image, "image");
                            Pair<View, String> p2 = Pair.create((View)viewHolder0.posted_item_name, "text");

                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation((Activity) context, p1,p2);



                            context.startActivity(intent ,  options.toBundle());
                        }

                    }
                });

                viewHolder0.posted_share_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        onPopupMenuClick(v, String.valueOf(model.getTrip().getId()), String.valueOf(model.getTrip().getPublisher().getId()), model.getPrivacy(), String.valueOf(model.getId()), model);
                    }
                });


                viewHolder0.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, RecourseScreen.class);
                        intent.putExtra("Publishing_id", String.valueOf(model.getId()));
                        intent.putExtra("Trip_id", String.valueOf(model.getTrip().getId()));

                        context.startActivity(intent);
                    }
                });


                break;

            case 2:
                final ViewHolder2 viewHolder2 = (ViewHolder2) holder;
                viewHolder2.sharer_item_name.setText(model.getSharer().getDisplayName());
                viewHolder2.posted_item_name.setText(model.getTrip().getPublisher().getDisplayName());
                viewHolder2.posted_tv_startTrip.setText(model.getTrip().getStartAddress());
                viewHolder2.posted_tv_endTrip.setText(model.getTrip().getEndAddress());
                viewHolder2.posted_like_text.setText(String.valueOf(model.getLikesCount()));
                viewHolder2.posted_comment_text.setText(String.valueOf(model.getComments()));
                viewHolder2.posted_resources_text.setText(String.valueOf(model.getResCount()));
                viewHolder2.posted_faves_text.setText(String.valueOf(model.getFavesCount()));

                if (model.getTrip().getDesc() != null) {
                    viewHolder2.posted_tv_desc.setVisibility(View.VISIBLE);
                    viewHolder2.posted_tv_desc.setText(model.getTrip().getDesc());

                }
                viewHolder2.posted_item_date.setText(DateUtils.getRelativeTimeSpanString(model.getCreatedAt(),
                        System.currentTimeMillis(),
                        DateUtils.SECOND_IN_MILLIS).toString());

                Glide.with(context)
                        .load(model.getTrip().getMapScreenShot())
                        .apply(new RequestOptions()
                                .placeholder(viewHolder2.posted_item_snapshot.getDrawable())
                        )
                        .into(viewHolder2.posted_item_snapshot);

                Glide.with(context)
                        .load(model.getTrip().getPublisher().getImage())
                        .apply(new RequestOptions()
                                .placeholder(viewHolder2.posted_item_Image.getDrawable())
                        )
                        .into(viewHolder2.posted_item_Image);

                Glide.with(context)
                        .load(model.getSharer().getImage())
                        .apply(new RequestOptions()
                                .placeholder(viewHolder2.sharer_item_Image.getDrawable())
                        )
                        .into(viewHolder2.sharer_item_Image);

                if (model.getLikesStatus()) {
                    viewHolder2.posted_like_image.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);
                }

                if (model.getFavStatus()) {
                    viewHolder2.posted_fav_image.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);
                }

                viewHolder2.posted_comment_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, TripComments.class);
                        intent.putExtra("ItemID", model.getId().toString());
                        context.startActivity(intent);
                    }
                });

                viewHolder2.posted_like_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (viewHolder2.posted_like_image.getColorFilter() == null) {
                            CommonUtilities.save_log(myAPI, dataManager.getID(), "LIKE", "publsihing", model.getId().toString());

                            viewHolder2.posted_like_image.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);
                            viewHolder2.posted_like_text.setText(String.valueOf(Integer.parseInt(viewHolder2.posted_like_text.getText().toString()) + 1));
                        } else {
                            CommonUtilities.save_log(myAPI, dataManager.getID(), "DISLIKE", "publsihing", model.getId().toString());

                            viewHolder2.posted_like_image.setColorFilter(null);
                            viewHolder2.posted_like_text.setText(String.valueOf(Integer.parseInt(viewHolder2.posted_like_text.getText().toString()) - 1));
                        }


                        like_action(model.getId().toString());
                    }
                });

                viewHolder2.posted_item_Image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (String.valueOf(model.getTrip().getPublisher().getId()).equals(dataManager.getID())) {
                            Intent intent = new Intent(context, MyProfile.class);
                            intent .putExtra("User_Name" , model.getTrip().getPublisher().getDisplayName());
                            intent .putExtra("User_Image" , model.getTrip().getPublisher().getImage());
                            Pair<View, String> p1 = Pair.create((View)viewHolder2.posted_item_Image, "image");
                            Pair<View, String> p2 = Pair.create((View)viewHolder2.posted_item_name, "text");

                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation((Activity) context, p1,p2);


                            context.startActivity(intent , options.toBundle());
                        } else {
                            Intent intent = new Intent(context, UserProfile.class);
                            intent.putExtra("ItemID", String.valueOf(model.getTrip().getPublisher().getId()));
                            intent .putExtra("User_Name" , model.getTrip().getPublisher().getDisplayName());
                            intent .putExtra("User_Image" , model.getTrip().getPublisher().getImage());
                            Pair<View, String> p1 = Pair.create((View)viewHolder2.posted_item_Image, "image");
                            Pair<View, String> p2 = Pair.create((View)viewHolder2.posted_item_name, "text");

                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation((Activity) context, p1,p2);



                            context.startActivity(intent ,  options.toBundle());
                        }
                    }
                });

                viewHolder2.posted_item_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (String.valueOf(model.getTrip().getPublisher().getId()).equals(dataManager.getID())) {
                            Intent intent = new Intent(context, MyProfile.class);
                            intent .putExtra("User_Name" , model.getTrip().getPublisher().getDisplayName());
                            intent .putExtra("User_Image" , model.getTrip().getPublisher().getImage());
                            Pair<View, String> p1 = Pair.create((View)viewHolder2.posted_item_Image, "image");
                            Pair<View, String> p2 = Pair.create((View)viewHolder2.posted_item_name, "text");

                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation((Activity) context, p1,p2);


                            context.startActivity(intent , options.toBundle());
                        } else {
                            Intent intent = new Intent(context, UserProfile.class);
                            intent.putExtra("ItemID", String.valueOf(model.getTrip().getPublisher().getId()));
                            intent .putExtra("User_Name" , model.getTrip().getPublisher().getDisplayName());
                            intent .putExtra("User_Image" , model.getTrip().getPublisher().getImage());
                            Pair<View, String> p1 = Pair.create((View)viewHolder2.posted_item_Image, "image");
                            Pair<View, String> p2 = Pair.create((View)viewHolder2.posted_item_name, "text");

                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation((Activity) context, p1,p2);



                            context.startActivity(intent ,  options.toBundle());
                        }
                    }
                });

                viewHolder2.sharer_item_Image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (String.valueOf(model.getSharer().getId()).equals(dataManager.getID())) {
                            Intent intent = new Intent(context, MyProfile.class);
                            intent .putExtra("User_Name" , model.getTrip().getPublisher().getDisplayName());
                            intent .putExtra("User_Image" , model.getTrip().getPublisher().getImage());
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, UserProfile.class);
                            intent.putExtra("ItemID", String.valueOf(model.getSharer().getId()));
                            intent .putExtra("User_Name" , model.getTrip().getPublisher().getDisplayName());
                            intent .putExtra("User_Image" , model.getTrip().getPublisher().getImage());
                            context.startActivity(intent );
                        }
                    }
                });

                viewHolder2.sharer_item_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (String.valueOf(model.getSharer().getId()).equals(dataManager.getID())) {
                            Intent intent = new Intent(context, MyProfile.class);
                            intent .putExtra("User_Name" , model.getTrip().getPublisher().getDisplayName());
                            intent .putExtra("User_Image" , model.getTrip().getPublisher().getImage());
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, UserProfile.class);
                            intent.putExtra("ItemID", String.valueOf(model.getSharer().getId()));
                            intent .putExtra("User_Name" , model.getTrip().getPublisher().getDisplayName());
                            intent .putExtra("User_Image" , model.getTrip().getPublisher().getImage());
                            context.startActivity(intent);
                        }
                    }
                });

                viewHolder2.posted_fav_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Map<String, String> parms = new HashMap<>();
                        parms.put("publishing_id", model.getId().toString());
                        parms.put("user_id", dataManager.getID());
                        myAPI.fav_action(parms)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<ApiResponse>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(ApiResponse apiResponse) {


                                        if (apiResponse.getStatus()) {
                                            if (viewHolder2.posted_fav_image.getColorFilter() == null) {
                                                CommonUtilities.save_log(myAPI, dataManager.getID(), "FAVE", "publsihing", model.getId().toString());

                                                viewHolder2.posted_fav_image.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark), android.graphics.PorterDuff.Mode.MULTIPLY);
                                            } else {
                                                CommonUtilities.save_log(myAPI, dataManager.getID(), "DISFAV", "publsihing", model.getId().toString());

                                                viewHolder2.posted_fav_image.setColorFilter(null);
                                            }

                                        } else {

                                            Toast.makeText(context, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                        }


                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(context, context.getString(R.string.connection_error), Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onComplete() {


                                    }
                                });
                    }
                });

                viewHolder2.posted_share_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        onPopupMenuClick(v, String.valueOf(model.getTrip().getId()), String.valueOf(model.getTrip().getPublisher().getId()), model.getPrivacy(), String.valueOf(model.getId()), model);
                    }
                });

                viewHolder2.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, RecourseScreen.class);
                        intent.putExtra("Publishing_id", String.valueOf(model.getId()));
                        intent.putExtra("Trip_id", String.valueOf(model.getTrip().getId()));
                        context.startActivity(intent);
                    }
                });


                break;
        }
    }

    void like_action(String publishing_id) {


        Map<String, String> parms = new HashMap<>();
        parms.put("publishing_id", publishing_id);
        parms.put("user_id", dataManager.getID());
        myAPI.like_action(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {


                        if (apiResponse.getStatus()) {


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

    void share_action(String trip_id, String privacy, String publisher_id) {

        CommonUtilities.showStaticDialog((Activity) context ,  "share");

        Map<String, String> parms = new HashMap<>();
        parms.put("trip_id", trip_id);
        parms.put("publisher_id", publisher_id);
        parms.put("privacy", privacy);
        parms.put("sharer_id", dataManager.getID());
        myAPI.share_action(parms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {


                        if (apiResponse.getStatus()) {


                        } else {

                            Toast.makeText(context, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, context.getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                        CommonUtilities.hideDialog();
                    }

                    @Override
                    public void onComplete() {
                        CommonUtilities.hideDialog();

                    }
                });
    }

    void fav_action(String publishing_id) {


    }


    public void onPopupMenuClick(View view, final String ItemID, final String publisher_id, final String privacy, final String publishing_id, final PostedTrip postedTrips) {


        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.share_item_settings, popup.getMenu());
        Menu menu = popup.getMenu();
        menu.findItem(R.id.share_internal).setTitle(context.getString(R.string.share_internal));
        menu.findItem(R.id.share_external).setTitle(context.getString(R.string.share_external));
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //do your things in each of the following cases
                switch (item.getItemId()) {
                    case R.id.share_internal:

                        CommonUtilities.showStaticDialog((Activity) context,  "trips");
                        Map<String, String> parms = new HashMap<>();
                        parms.put("trip_id", ItemID);
                        parms.put("publisher_id", publisher_id);
                        parms.put("privacy", privacy);
                        parms.put("sharer_id", dataManager.getID());
                        myAPI.share_trip(parms)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<ApiResponse>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(ApiResponse apiResponse) {

                                        CommonUtilities.hideDialog();

                                        if (apiResponse.getStatus()) {
                                            Toast.makeText(context, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                            CommonUtilities.save_log(myAPI, dataManager.getID(), "SHARE", "publsihing", publishing_id);

                                        } else {
                                            Toast.makeText(context, apiResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                        }


                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(context, context.getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                                        CommonUtilities.hideDialog();
                                    }

                                    @Override
                                    public void onComplete() {

                                        CommonUtilities.hideDialog();

                                    }
                                });

                        return true;

                    case R.id.share_external:

                        Toast.makeText(context, "share_external", Toast.LENGTH_SHORT).show();

                        createDeepLink(postedTrips);
                        return true;
                    default:
                        return false;
                }

            }
        });
        popup.show();


    }




    private void createDeepLink(final PostedTrip product) {
        Gson gson = new Gson();

        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                .setCanonicalIdentifier("item/" + product.getId())
                .setTitle(context.getResources().getString(R.string.app_name))
                .setContentDescription(product.getTrip().getStartAddress())
                .setContentImageUrl(product.getTrip().getMapScreenShot())
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .addContentMetadata("property1", gson.toJson(product));



        LinkProperties linkProperties = new LinkProperties()
                .setChannel("facebook")
                .setFeature("sharing");

        ShareSheetStyle shareSheetStyle = new ShareSheetStyle(context, "Check this out!", "")
                .setCopyUrlStyle(context.getResources().getDrawable(android.R.drawable.ic_menu_send), "Copy", "Added to clipboard")
                .setMoreOptionStyle(context.getResources().getDrawable(android.R.drawable.ic_menu_search), "Show more")
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.TWITTER)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.WHATS_APP)
                .setAsFullWidthStyle(true)
                .setSharingTitle("Share");

        branchUniversalObject.showShareSheet((Activity) context,
                linkProperties,
                shareSheetStyle,
                new Branch.BranchLinkShareListener() {
                    @Override
                    public void onShareLinkDialogLaunched() {
                    }

                    @Override
                    public void onShareLinkDialogDismissed() {
                    }

                    @Override
                    public void onLinkShareResponse(String sharedLink, String sharedChannel, BranchError error) {

                    }

                    @Override
                    public void onChannelSelected(String channelName) {
                    }
                });

        branchUniversalObject.generateShortUrl(context, linkProperties, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error == null) {

                 }
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("ERROORR" , error.getMessage());

            }
        });

    }
}

package wakeb.tech.drb.Risks;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import wakeb.tech.drb.Models.RiskType;
import wakeb.tech.drb.R;
import wakeb.tech.drb.Risks.RiskTypes_Interface;
import wakeb.tech.drb.data.DataManager;

public class RiskTypesDialog extends Dialog {

    private static Context c;
    private Dialog d;
    private ArrayList<RiskType> riskTypes;
    private RecyclerView risksTypes_Recycler;



    public RiskTypesDialog(Activity a, ArrayList<RiskType> riskTypes, DataManager dataManager) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.riskTypes = riskTypes;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.risk_types_dialog);

        risksTypes_Recycler = (RecyclerView) findViewById(R.id.risksTypes_Recycler);
        risksTypes_Recycler.setLayoutManager(new LinearLayoutManager(c));
        risksTypes_Recycler.setAdapter(new RisksTypesAdapter(c, riskTypes));
    }

    public class RisksTypesAdapter extends RecyclerView.Adapter<RisksTypesAdapter.ViewHolder> {

        private Context context;
        private List<RiskType> my_data;


        public RisksTypesAdapter(Context context, List<RiskType> my_data) {
            this.context = context;
            this.my_data = my_data;

        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.risk_type_item, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            final RiskType model = my_data.get(position);
            Glide.with(context)
                    .load(model.getIcon())
                    .apply(new RequestOptions()
                            .placeholder(holder.riskImage.getDrawable())
                    )
                    .into(holder.riskImage);

            holder.riskName.setText(model.getNameAr());
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof RiskTypes_Interface) {
                        ((RiskTypes_Interface) context).setRiskType(model.getNameAr(), model.getId().toString());
                    }
                    dismiss();
                }
            });


        }

        @Override
        public int getItemCount() {
            return my_data.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {


            View view;
            ImageView riskImage;
            TextView riskName;

            public ViewHolder(View itemView) {
                super(itemView);
                view = itemView;
                riskName = (TextView) view.findViewById(R.id.riskType_name);
                riskImage = (ImageView) view.findViewById(R.id.riskType_image);


            }
        }


    }

}
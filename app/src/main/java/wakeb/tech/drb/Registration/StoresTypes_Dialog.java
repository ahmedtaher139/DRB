package wakeb.tech.drb.Registration;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import wakeb.tech.drb.Models.StoresTypes;
import wakeb.tech.drb.R;
import wakeb.tech.drb.data.DataManager;

public class StoresTypes_Dialog extends Dialog {


    private static Context c;
    private Dialog d;
    private ArrayList<StoresTypes> storesTypes;
    private RecyclerView storesTypes_Recycler;
    private DataManager dataManager;


    public StoresTypes_Dialog(Activity a , ArrayList<StoresTypes> storesTypes , DataManager dataManager) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.storesTypes = storesTypes;
        this.dataManager = dataManager;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores_types__dialog);

        storesTypes_Recycler = (RecyclerView) findViewById(R.id.storesTypes_Recycler);
        storesTypes_Recycler.setLayoutManager(new LinearLayoutManager(c));
        storesTypes_Recycler.setAdapter(new StoresTypes_Adapter(c , storesTypes));
    }


    public class StoresTypes_Adapter extends RecyclerView.Adapter<StoresTypes_Adapter.ViewHolder> {

        private Context context;
        private List<StoresTypes> my_data;

        public StoresTypes_Adapter(Context context, List<StoresTypes> my_data) {
            this.context = context;
            this.my_data = my_data;
        }



        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_type_item, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            final StoresTypes model = my_data.get(position);


            if (dataManager.getLang().equals("en"))
            {
                holder.store_type_name.setText(model.getNameEn());
            }
            else
            {
                holder.store_type_name.setText(model.getNameAr());
            }


            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (dataManager.getLang().equals("en"))
                    {
                        if(context instanceof StoresType_Interface){
                            ((StoresType_Interface)context).setType(model.getId().toString() , model.getNameEn());
                        }
                        dismiss();
                    }
                    else
                    {
                        if(context instanceof StoresType_Interface){
                            ((StoresType_Interface)context).setType(model.getId().toString() , model.getNameAr());
                        }
                        dismiss();
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
            public TextView store_type_name;
            public ImageView store_type_icon;

            public ViewHolder(View itemView) {
                super(itemView);
                view = itemView;
                store_type_name = (TextView) view.findViewById(R.id.store_type_name);
                store_type_icon = (ImageView) view.findViewById(R.id.store_type_icon);

            }
        }


    }
}

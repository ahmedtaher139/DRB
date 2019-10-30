package wakeb.tech.drb.Home.Fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import wakeb.tech.drb.Adapters.ResourcesAdapter;
import wakeb.tech.drb.Models.Resource;
import wakeb.tech.drb.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResourcesList extends Fragment {


    public ResourcesList() {
        // Required empty public constructor
    }

    RecyclerView resources;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = this.getArguments();
        ArrayList<Resource> arraylist = bundle.getParcelableArrayList("arraylist");
        View view = inflater.inflate(R.layout.fragment_resources_list, container, false);

        for(Resource resource : arraylist)
        {
            Gson gson = new Gson();
            String json = gson.toJson(resource);
            Log.i("arraylist" , json);
        }

        resources = (RecyclerView) view.findViewById(R.id.resources);
        resources.setLayoutManager(new LinearLayoutManager(getActivity()));
        resources.setAdapter(new ResourcesAdapter(getActivity() ,arraylist ));
        return view;
    }

}

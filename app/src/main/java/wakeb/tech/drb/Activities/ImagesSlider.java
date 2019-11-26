package wakeb.tech.drb.Activities;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import wakeb.tech.drb.Adapters.SliderAdapter;
import wakeb.tech.drb.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImagesSlider extends Fragment {
    public static final String TAG = ImagesSlider.class.getSimpleName();

    ArrayList<String> strings;

    TextView total, current;

    RelativeLayout close;




    public ImagesSlider() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        strings = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_images_slider, container, false);


        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        close = (RelativeLayout) view.findViewById(R.id.back_button);
        total = (TextView) view.findViewById(R.id.totalImages);
        current = (TextView) view.findViewById(R.id.currentImage);


        strings =  getArguments().getStringArrayList("IMAGES");


        total.setText(String.valueOf(strings.size()));
        SliderAdapter adapter = new SliderAdapter(getActivity(), strings);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                current.setText(String.valueOf(position + 1));


            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
              }
        });
        return view;

    }

}

package wakeb.tech.drb.Uitils;

import android.os.Bundle;
import android.os.StrictMode;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import wakeb.tech.drb.R;


/**
 * Created by user on 27/3/18.
 */

public class ImagesBottomSheet extends BottomSheetDialogFragment {

    RelativeLayout camera, gallery;
    DialogListener listener;

    public ImagesBottomSheet(DialogListener listener) {

        this.listener = listener;

    }

    String mCapturedImageUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.image_picker, container,
                false);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        camera = (RelativeLayout) view.findViewById(R.id.camera);
        gallery = (RelativeLayout) view.findViewById(R.id.gallery);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.camera();
                dismiss();


            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.gallery();
                dismiss();


            }
        });

        return view;

    }

    public interface DialogListener {
        public void camera();
        public void gallery();
    }

}
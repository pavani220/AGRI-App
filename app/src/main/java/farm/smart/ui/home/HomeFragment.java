package farm.smart.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import farm.smart.R;
import farm.smart.databinding.FragmentHomeBinding;
import farm.smart.FarmerDetails;
import farm.smart.SoilHealth;
import farm.smart.CropHealth;
import farm.smart.AgriInput;
import farm.smart.Spraying;
import farm.smart.SoilTesting;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textView;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Setup image click listeners
        setupImageClickListeners(root);

        return root;
    }

    private void setupImageClickListeners(View root) {
        ImageView imageView1 = root.findViewById(R.id.imageView);
        ImageView imageView2 = root.findViewById(R.id.imageView2);
        ImageView imageView3 = root.findViewById(R.id.imageView3);
        ImageView imageView4 = root.findViewById(R.id.imageView4);
        ImageView imageView5 = root.findViewById(R.id.imageView6);
        ImageView imageView6 = root.findViewById(R.id.imageView7);

        // Set click listeners for each image
        imageView1.setOnClickListener(v -> openActivity("farmer"));
        imageView2.setOnClickListener(v -> openActivity("soilhealth"));
        imageView4.setOnClickListener(v -> openActivity("crophealth"));
        imageView3.setOnClickListener(v -> openActivity("soiltest"));
        imageView5.setOnClickListener(v -> openActivity("agriinputs"));
        imageView6.setOnClickListener(v -> openActivity("spraying"));
    }

    private void openActivity(String pageType) {
        Intent intent = null;

        // Open the correct activity based on the image clicked
        switch (pageType) {
            case "farmer":
                intent = new Intent(getActivity(), FarmerDetails.class);
                break;
            case "soilhealth":
                intent = new Intent(getActivity(), SoilHealth.class);
                break;
            case "crophealth":
                intent = new Intent(getActivity(), CropHealth.class);
                break;
            case "soiltest":
                intent =new Intent(getActivity(), SoilTesting.class);
                break;
            case "spraying":
                intent=new Intent(getActivity(), Spraying.class);
                break;
            case "agriinputs":
                intent=new Intent(getActivity(), AgriInput.class);
                break;
            default:
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

package farm.smart;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

public class CropDetails extends AppCompatActivity {

    // Firebase reference
    DatabaseReference cropRef;

    // Views
    EditText leafColorEdit, leafSizeShapeEdit, pestDiseaseInspectionEdit, rootHealthEdit,
            stemConditionEdit, flowerFruitDevelopmentEdit, plantHeightGrowthStageEdit;
    LinearLayout cropListLayout;
    LinearLayout formLayout;  // To hold the form
    Button addButton, updateButton, submitButton;  // Buttons
    TextView titleTextView;  // Title TextView

    String currentCropKey = "";  // To hold the current crop key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_health);  // Use your layout file

        // Initialize Firebase reference
        cropRef = FirebaseDatabase.getInstance().getReference("crops");

        // Initialize the views
        leafColorEdit = findViewById(R.id.editTextLeafColor);
        leafSizeShapeEdit = findViewById(R.id.editTextLeafSizeShape);
        pestDiseaseInspectionEdit = findViewById(R.id.editTextPestDiseaseInspection);
        rootHealthEdit = findViewById(R.id.editTextRootHealth);
        stemConditionEdit = findViewById(R.id.editTextStemCondition);
        flowerFruitDevelopmentEdit = findViewById(R.id.editTextFlowerFruitDevelopment);
        plantHeightGrowthStageEdit = findViewById(R.id.editTextPlantHeightGrowthStage);

        cropListLayout = findViewById(R.id.cropListLayout);
        formLayout = findViewById(R.id.formLayoutCropHealth);  // Initialize the form layout when add button is clicked
        formLayout.setVisibility(View.GONE);  // Hide the form by default to avoid the collision

        addButton = findViewById(R.id.buttonAddCrop);  // Button to show the form and add data
        updateButton = findViewById(R.id.buttonUpdateCrop);  // Button to update data
        submitButton = findViewById(R.id.buttonSubmitCropHealth);  // Submit Button to submit the form data and reflect below the form and in the page
        titleTextView = findViewById(R.id.textViewTitle);  // Reference to the Title TextView to store and display the data which is stored

        // Show the form when "Add Data" is clicked
        addButton.setOnClickListener(v -> {
            formLayout.setVisibility(View.VISIBLE);  // Show the form
            titleTextView.setVisibility(View.GONE);  // Hide the "Crop Health Details" title
            addButton.setVisibility(View.GONE);  // Hide "Add Data" button after form is visible
            updateButton.setVisibility(View.GONE);  // Hide "Update" button while adding data
        });

        // Save the data when "Submit" button is clicked
        submitButton.setOnClickListener(v -> {
            String leafColor = leafColorEdit.getText().toString();
            String leafSizeShape = leafSizeShapeEdit.getText().toString();
            String pestDiseaseInspection = pestDiseaseInspectionEdit.getText().toString();
            String rootHealth = rootHealthEdit.getText().toString();
            String stemCondition = stemConditionEdit.getText().toString();
            String flowerFruitDevelopment = flowerFruitDevelopmentEdit.getText().toString();
            String plantHeightGrowthStage = plantHeightGrowthStageEdit.getText().toString();

            // Get the userId from the session
            SessionManager sessionManager = new SessionManager(CropDetails.this);
            String userId = sessionManager.getUserId();

            // Check if we are updating or adding
            if (currentCropKey.isEmpty()) {
                // Adding new data
                String id = cropRef.push().getKey();  // Generate a new ID for the new crop
                Crop crop = new Crop(leafColor, leafSizeShape, pestDiseaseInspection, rootHealth,
                        stemCondition, flowerFruitDevelopment, plantHeightGrowthStage, userId);
                cropRef.child(id).setValue(crop).addOnSuccessListener(unused -> {
                    Toast.makeText(CropDetails.this, "Crop Health Data Added", Toast.LENGTH_SHORT).show();
                    loadCrops();  // Reload the crops' list
                    formLayout.setVisibility(View.GONE);  // Hide the form again
                    titleTextView.setVisibility(View.VISIBLE);  // Show the "Crop Health Details" title
                    addButton.setVisibility(View.VISIBLE);  // Show the "Add Data" button again
                    updateButton.setVisibility(View.VISIBLE);  // Show the "Update" button again
                });
            } else {
                // Update existing data
                Crop updatedCrop = new Crop(leafColor, leafSizeShape, pestDiseaseInspection, rootHealth,
                        stemCondition, flowerFruitDevelopment, plantHeightGrowthStage, userId);
                cropRef.child(currentCropKey).setValue(updatedCrop).addOnSuccessListener(unused -> {
                    Toast.makeText(CropDetails.this, "Crop Health Data Updated", Toast.LENGTH_SHORT).show();
                    loadCrops();  // Reload the crops' list
                    formLayout.setVisibility(View.GONE);  // Hide the form again
                    titleTextView.setVisibility(View.VISIBLE);  // Show the "Crop Health Details" title
                    addButton.setVisibility(View.VISIBLE);  // Show the "Add Data" button again
                    updateButton.setVisibility(View.VISIBLE);  // Show the "Update" button again
                    currentCropKey = "";  // Reset the current crop key
                });
            }
        });

        loadCrops();  // Load existing crops when the activity starts
    }

    // Method to load crops from Firebase and update the list on the screen
    public void loadCrops() {
        // Get the current user ID from the session
        SessionManager sessionManager = new SessionManager(CropDetails.this);
        String userId = sessionManager.getUserId();

        cropListLayout.removeAllViews();  // Remove previous data if any

        // Query Firebase to load only the data specific to the logged-in user
        cropRef.orderByChild("userId").equalTo(userId)  // Filter by userId
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Crop crop = snap.getValue(Crop.class);

                            // Create a new LinearLayout for each crop to display their details
                            LinearLayout cropDetailsLayout = new LinearLayout(CropDetails.this);
                            cropDetailsLayout.setOrientation(LinearLayout.VERTICAL);
                            cropDetailsLayout.setPadding(16, 16, 16, 16);

                            // Create TextViews for each field and add to cropDetailsLayout
                            TextView leafColorTextView = new TextView(CropDetails.this);
                            leafColorTextView.setText("Leaf Color: " + crop.leafColor);
                            leafColorTextView.setTextSize(16);
                            leafColorTextView.setPadding(0, 4, 0, 4);

                            TextView leafSizeShapeTextView = new TextView(CropDetails.this);
                            leafSizeShapeTextView.setText("Leaf Size and Shape: " + crop.leafSizeAndShape);
                            leafSizeShapeTextView.setTextSize(16);
                            leafSizeShapeTextView.setPadding(0, 4, 0, 4);

                            TextView pestDiseaseInspectionTextView = new TextView(CropDetails.this);
                            pestDiseaseInspectionTextView.setText("Pest/Disease Inspection: " + crop.pestAndDiseaseInspection);
                            pestDiseaseInspectionTextView.setTextSize(16);
                            pestDiseaseInspectionTextView.setPadding(0, 4, 0, 4);

                            TextView rootHealthTextView = new TextView(CropDetails.this);
                            rootHealthTextView.setText("Root Health: " + crop.rootHealth);
                            rootHealthTextView.setTextSize(16);
                            rootHealthTextView.setPadding(0, 4, 0, 4);

                            TextView stemConditionTextView = new TextView(CropDetails.this);
                            stemConditionTextView.setText("Stem Condition: " + crop.stemCondition);
                            stemConditionTextView.setTextSize(16);
                            stemConditionTextView.setPadding(0, 4, 0, 4);

                            TextView flowerFruitDevelopmentTextView = new TextView(CropDetails.this);
                            flowerFruitDevelopmentTextView.setText("Flower/Fruit Development: " + crop.flowerAndFruitDevelopment);
                            flowerFruitDevelopmentTextView.setTextSize(16);
                            flowerFruitDevelopmentTextView.setPadding(0, 4, 0, 4);

                            TextView plantHeightGrowthStageTextView = new TextView(CropDetails.this);
                            plantHeightGrowthStageTextView.setText("Plant Height and Growth Stage: " + crop.plantHeightAndGrowthStage);
                            plantHeightGrowthStageTextView.setTextSize(16);
                            plantHeightGrowthStageTextView.setPadding(0, 4, 0, 4);

                            // Add each TextView to the cropDetailsLayout
                            cropDetailsLayout.addView(leafColorTextView);
                            cropDetailsLayout.addView(leafSizeShapeTextView);
                            cropDetailsLayout.addView(pestDiseaseInspectionTextView);
                            cropDetailsLayout.addView(rootHealthTextView);
                            cropDetailsLayout.addView(stemConditionTextView);
                            cropDetailsLayout.addView(flowerFruitDevelopmentTextView);
                            cropDetailsLayout.addView(plantHeightGrowthStageTextView);

                            // Add "Edit" button next to each crop's details
                            Button editButton = new Button(CropDetails.this);
                            editButton.setText("Edit");
                            editButton.setOnClickListener(v -> {
                                // When "Edit" button is clicked, show the form with pre-filled data
                                currentCropKey = snap.getKey();
                                leafColorEdit.setText(crop.leafColor);
                                leafSizeShapeEdit.setText(crop.leafSizeAndShape);
                                pestDiseaseInspectionEdit.setText(crop.pestAndDiseaseInspection);
                                rootHealthEdit.setText(crop.rootHealth);
                                stemConditionEdit.setText(crop.stemCondition);
                                flowerFruitDevelopmentEdit.setText(crop.flowerAndFruitDevelopment);
                                plantHeightGrowthStageEdit.setText(crop.plantHeightAndGrowthStage);

                                formLayout.setVisibility(View.VISIBLE);  // Show the form
                                titleTextView.setVisibility(View.GONE);  // Hide the title
                                addButton.setVisibility(View.GONE);  // Hide the "Add Data" button
                                updateButton.setVisibility(View.VISIBLE);  // Show the "Update" button
                            });

                            cropDetailsLayout.addView(editButton);  // Add the "Edit" button to the layout

                            cropListLayout.addView(cropDetailsLayout);  // Add the cropDetailsLayout to the parent layout
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(CropDetails.this, "Failed to load crop data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

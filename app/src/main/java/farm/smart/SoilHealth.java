package farm.smart;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

public class SoilHealth extends AppCompatActivity {

    // Firebase reference
    DatabaseReference soilHealthRef;

    // Views
    EditText soilTypeEdit, soilColorEdit, irrigationTypeEdit, acresEdit, soilTextureEdit, soilMoistureEdit;

    LinearLayout soilListLayout;

    LinearLayout formLayout;  // To hold the form

    Button addButton, updateButton, submitButton;  // Buttons
    TextView titleTextView;  // Title TextView

    String currentSoilKey = "";  // To hold the current soil health key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_health);

        // Initialize Firebase reference
        soilHealthRef = FirebaseDatabase.getInstance().getReference("soil_health");

        // Initialize the views
        soilTypeEdit = findViewById(R.id.editTextSoilType);
        soilColorEdit = findViewById(R.id.editTextSoilColor);
        irrigationTypeEdit = findViewById(R.id.editTextIrrigationType);
        acresEdit = findViewById(R.id.editTextAcres);
        soilTextureEdit = findViewById(R.id.editTextSoilTexture);
        soilMoistureEdit = findViewById(R.id.editTextSoilMoisture);

        soilListLayout = findViewById(R.id.soilListLayout);
        formLayout = findViewById(R.id.formLayout);  // Initialize the form layout when add button is clicked
        formLayout.setVisibility(View.GONE);  // Hide the form by default to avoid collision

        addButton = findViewById(R.id.button);  // Button to show the form and add data
        updateButton = findViewById(R.id.button2);  // Button to update data
        submitButton = findViewById(R.id.buttonSubmit);  // Submit Button to submit the form data and to reflect below the form and in the page
        titleTextView = findViewById(R.id.textViewTitle);  // Reference to the Title TextView to store and display the data which is stored

        // Show the form when "Add Data" is clicked
        addButton.setOnClickListener(v -> {
            formLayout.setVisibility(View.VISIBLE);  // Show the form
            titleTextView.setVisibility(View.GONE);  // Hide the "Soil Health Details" title
            addButton.setVisibility(View.GONE);  // Hide "Add Data" button after form is visible
            updateButton.setVisibility(View.GONE);  // Hide "Update" button while adding data
        });

        // Save the data when "Submit" button is clicked
        submitButton.setOnClickListener(v -> {
            String soilType = soilTypeEdit.getText().toString();
            String soilColor = soilColorEdit.getText().toString();
            String irrigationType = irrigationTypeEdit.getText().toString();
            int acres = Integer.parseInt(acresEdit.getText().toString());
            String soilTexture = soilTextureEdit.getText().toString();
            String soilMoisture = soilMoistureEdit.getText().toString();

            // Get the userId from the session
            SessionManager sessionManager = new SessionManager(SoilHealth.this);
            String userId = sessionManager.getUserId();

            // Check if we are updating or adding
            if (currentSoilKey.isEmpty()) {
                // Adding new data
                String id = soilHealthRef.push().getKey();  // Generate a new ID for the new soil health record
                SoilHealthData soilHealthData = new SoilHealthData(soilType, soilColor, irrigationType, acres, soilTexture, soilMoisture, userId); // Pass userId here
                soilHealthRef.child(id).setValue(soilHealthData).addOnSuccessListener(unused -> {
                    Toast.makeText(SoilHealth.this, "Data Added", Toast.LENGTH_SHORT).show();
                    loadSoilHealthData();  // Reload the soil health list
                    formLayout.setVisibility(View.GONE);  // Hide the form again
                    titleTextView.setVisibility(View.VISIBLE);  // Show the "Soil Health Details" title
                    addButton.setVisibility(View.VISIBLE);  // Show the "Add Data" button again
                    updateButton.setVisibility(View.VISIBLE);  // Show the "Update" button again
                });

            } else {
                // Update existing data
                SoilHealthData updatedSoilHealthData = new SoilHealthData(soilType, soilColor, irrigationType, acres, soilTexture, soilMoisture, userId); // Pass userId here
                soilHealthRef.child(currentSoilKey).setValue(updatedSoilHealthData).addOnSuccessListener(unused -> {
                    Toast.makeText(SoilHealth.this, "Data Updated", Toast.LENGTH_SHORT).show();
                    loadSoilHealthData();  // Reload the soil health list
                    formLayout.setVisibility(View.GONE);  // Hide the form again
                    titleTextView.setVisibility(View.VISIBLE);  // Show the "Soil Health Details" title
                    addButton.setVisibility(View.VISIBLE);  // Show the "Add Data" button again
                    updateButton.setVisibility(View.VISIBLE);  // Show the "Update" button again
                    currentSoilKey = "";  // Reset the current soil key
                });
            }
        });

        loadSoilHealthData();  // Load existing soil health data when the activity starts
    }

    // Method to load soil health data from Firebase and update the list on the screen
    public void loadSoilHealthData() {
        // Get the current user ID from the session
        SessionManager sessionManager = new SessionManager(SoilHealth.this);
        String userId = sessionManager.getUserId();

        soilListLayout.removeAllViews();  // Remove previous data if any

        // Query Firebase to load only the data specific to the logged-in user
        soilHealthRef.orderByChild("userId").equalTo(userId)  // Filter by userId
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            SoilHealthData soilHealthData = snap.getValue(SoilHealthData.class);

                            // Create a new LinearLayout for each soil health data to display their details
                            LinearLayout soilHealthDetailsLayout = new LinearLayout(SoilHealth.this);
                            soilHealthDetailsLayout.setOrientation(LinearLayout.VERTICAL);
                            soilHealthDetailsLayout.setPadding(16, 16, 16, 16);

                            // Create TextViews for each field and add to soilHealthDetailsLayout
                            TextView soilTypeTextView = new TextView(SoilHealth.this);
                            soilTypeTextView.setText("Soil Type: " + soilHealthData.soilType);
                            soilTypeTextView.setTextSize(16);
                            soilTypeTextView.setPadding(0, 4, 0, 4);

                            TextView soilColorTextView = new TextView(SoilHealth.this);
                            soilColorTextView.setText("Soil Color: " + soilHealthData.soilColor);
                            soilColorTextView.setTextSize(16);
                            soilColorTextView.setPadding(0, 4, 0, 4);

                            TextView irrigationTypeTextView = new TextView(SoilHealth.this);
                            irrigationTypeTextView.setText("Irrigation Type: " + soilHealthData.irrigationType);
                            irrigationTypeTextView.setTextSize(16);
                            irrigationTypeTextView.setPadding(0, 4, 0, 4);

                            TextView acresTextView = new TextView(SoilHealth.this);
                            acresTextView.setText("Acres: " + soilHealthData.acres + " acres");
                            acresTextView.setTextSize(16);
                            acresTextView.setPadding(0, 4, 0, 4);

                            TextView soilTextureTextView = new TextView(SoilHealth.this);
                            soilTextureTextView.setText("Soil Texture: " + soilHealthData.soilTexture);
                            soilTextureTextView.setTextSize(16);
                            soilTextureTextView.setPadding(0, 4, 0, 4);

                            TextView soilMoistureTextView = new TextView(SoilHealth.this);
                            soilMoistureTextView.setText("Soil Moisture: " + soilHealthData.soilMoisture);
                            soilMoistureTextView.setTextSize(16);
                            soilMoistureTextView.setPadding(0, 4, 0, 4);

                            // Add each TextView to the soilHealthDetailsLayout
                            soilHealthDetailsLayout.addView(soilTypeTextView);
                            soilHealthDetailsLayout.addView(soilColorTextView);
                            soilHealthDetailsLayout.addView(irrigationTypeTextView);
                            soilHealthDetailsLayout.addView(acresTextView);
                            soilHealthDetailsLayout.addView(soilTextureTextView);
                            soilHealthDetailsLayout.addView(soilMoistureTextView);

                            // Add "Edit" button next to each soil health data's details
                            Button editButton = new Button(SoilHealth.this);
                            editButton.setText("Edit");
                            editButton.setOnClickListener(v -> {
                                // When "Edit" button is clicked, show the form with pre-filled data
                                currentSoilKey = snap.getKey();
                                soilTypeEdit.setText(soilHealthData.soilType);
                                soilColorEdit.setText(soilHealthData.soilColor);
                                irrigationTypeEdit.setText(soilHealthData.irrigationType);
                                acresEdit.setText(String.valueOf(soilHealthData.acres));
                                soilTextureEdit.setText(soilHealthData.soilTexture);
                                soilMoistureEdit.setText(soilHealthData.soilMoisture);
                                formLayout.setVisibility(View.VISIBLE); //show the form
                                titleTextView.setVisibility(View.GONE);  // hide the title
                                addButton.setVisibility(View.GONE);  // Hide the button named add data to add the data and to open the form
                                updateButton.setVisibility(View.VISIBLE);  // Show the "Update" button to find the old form to edit and update the form
                            });

                            // Add each field and the edit button to the soilHealthDetailsLayout
                            soilHealthDetailsLayout.addView(editButton);

                            // Add the whole soilHealthDetailsLayout to the soilListLayout
                            soilListLayout.addView(soilHealthDetailsLayout);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SoilHealth.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}



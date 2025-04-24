package farm.smart;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

public class FarmerDetails extends AppCompatActivity {

    // Firebase reference
    DatabaseReference farmerRef;

    // Views
    EditText nameEdit, mobileEdit, emailEdit, acresEdit, aadharEdit, surveyEdit, addressEdit;
    LinearLayout farmerListLayout;
    LinearLayout formLayout;  // To hold the form

    Button addButton, updateButton, submitButton;  // Buttons
    TextView titleTextView;  // Title TextView

    String currentFarmerKey = "";  // To hold the current farmer key (for update)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer);

        // Initialize Firebase reference
        farmerRef = FirebaseDatabase.getInstance().getReference("farmers");

        // Initialize the views
        nameEdit = findViewById(R.id.editTextText);
        mobileEdit = findViewById(R.id.editTextPhone);
        emailEdit = findViewById(R.id.editTextEmail);
        acresEdit = findViewById(R.id.editTextNumber3);
        aadharEdit = findViewById(R.id.editTextNumber4);
        surveyEdit = findViewById(R.id.editTextNumber5);
        addressEdit = findViewById(R.id.editTextNumber6);

        farmerListLayout = findViewById(R.id.farmerListLayout);
        formLayout = findViewById(R.id.formLayout);  // Initialize the form layout
        formLayout.setVisibility(View.GONE);  // Hide the form by default

        addButton = findViewById(R.id.button);  // Button to show the form and add data
        updateButton = findViewById(R.id.button2);  // Button to update data
        submitButton = findViewById(R.id.buttonSubmit);  // Submit Button
        titleTextView = findViewById(R.id.textViewTitle);  // Reference to the Title TextView

        // Show the form when "Add Data" is clicked
        addButton.setOnClickListener(v -> {
            formLayout.setVisibility(View.VISIBLE);  // Show the form
            titleTextView.setVisibility(View.GONE);  // Hide the "Farmer Details" title
            addButton.setVisibility(View.GONE);  // Hide "Add Data" button after form is visible
            updateButton.setVisibility(View.GONE);  // Hide "Update" button while adding data
        });

        // Save the data when "Submit" button is clicked
        submitButton.setOnClickListener(v -> {
            String name = nameEdit.getText().toString();
            String mobile = mobileEdit.getText().toString();
            String email = emailEdit.getText().toString();
            int acres = Integer.parseInt(acresEdit.getText().toString());
            String aadhar = aadharEdit.getText().toString();
            String survey = surveyEdit.getText().toString();
            String address = addressEdit.getText().toString();

            // Check if we are updating or adding
            if (currentFarmerKey.isEmpty()) {
                // Adding new data
                String id = farmerRef.push().getKey();  // Generate a new ID for the new farmer
                Farmer farmer = new Farmer(name, mobile, email, acres, aadhar, survey, address);
                farmerRef.child(id).setValue(farmer).addOnSuccessListener(unused -> {
                    Toast.makeText(FarmerDetails.this, "Data Added", Toast.LENGTH_SHORT).show();
                    loadFarmers();  // Reload the farmers' list
                    formLayout.setVisibility(View.GONE);  // Hide the form again
                    titleTextView.setVisibility(View.VISIBLE);  // Show the "Farmer Details" title
                    addButton.setVisibility(View.VISIBLE);  // Show the "Add Data" button again
                    updateButton.setVisibility(View.VISIBLE);  // Show the "Update" button again
                });
            } else {
                // Update existing data
                Farmer updatedFarmer = new Farmer(name, mobile, email, acres, aadhar, survey, address);
                farmerRef.child(currentFarmerKey).setValue(updatedFarmer).addOnSuccessListener(unused -> {
                    Toast.makeText(FarmerDetails.this, "Data Updated", Toast.LENGTH_SHORT).show();
                    loadFarmers();  // Reload the farmers' list
                    formLayout.setVisibility(View.GONE);  // Hide the form again
                    titleTextView.setVisibility(View.VISIBLE);  // Show the "Farmer Details" title
                    addButton.setVisibility(View.VISIBLE);  // Show the "Add Data" button again
                    updateButton.setVisibility(View.VISIBLE);  // Show the "Update" button again
                    currentFarmerKey = "";  // Reset the current farmer key
                });
            }
        });

        loadFarmers();  // Load existing farmers when the activity starts
    }

    // Method to load farmers from Firebase and update the list on the screen
    public void loadFarmers() {
        farmerListLayout.removeAllViews();  // Remove previous data if any
        farmerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Farmer farmer = snap.getValue(Farmer.class);

                    // Create a new TextView for each farmer to show the details
                    TextView tv = new TextView(FarmerDetails.this);
                    tv.setText(farmer.name + " | " + farmer.mobile + " | " + farmer.acres + " acres");
                    tv.setPadding(8, 8, 8, 8);
                    tv.setTextSize(16);
                    farmerListLayout.addView(tv);

                    // Add an OnClickListener to each farmer item for update functionality
                    tv.setOnClickListener(v -> {
                        // When a farmer item is clicked, pre-fill the form with their data for editing
                        nameEdit.setText(farmer.name);
                        mobileEdit.setText(farmer.mobile);
                        emailEdit.setText(farmer.email);
                        acresEdit.setText(String.valueOf(farmer.acres));
                        aadharEdit.setText(farmer.aadhar);
                        surveyEdit.setText(farmer.surveyNumber);
                        addressEdit.setText(farmer.address);

                        // Show the form
                        formLayout.setVisibility(View.VISIBLE);
                        addButton.setVisibility(View.GONE);  // Hide the "Add Data" button while updating

                        // Store the current farmer's key for update
                        currentFarmerKey = snap.getKey();
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FarmerDetails.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

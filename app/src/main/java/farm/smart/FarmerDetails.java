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

                    // Create a new LinearLayout for each farmer to display their details
                    LinearLayout farmerDetailsLayout = new LinearLayout(FarmerDetails.this);
                    farmerDetailsLayout.setOrientation(LinearLayout.VERTICAL);
                    farmerDetailsLayout.setPadding(16, 16, 16, 16);

                    // Create TextViews for each field and add to farmerDetailsLayout
                    TextView nameTextView = new TextView(FarmerDetails.this);
                    nameTextView.setText("Name: " + farmer.name);
                    nameTextView.setTextSize(16);
                    nameTextView.setPadding(0, 4, 0, 4);

                    TextView mobileTextView = new TextView(FarmerDetails.this);
                    mobileTextView.setText("Phone Number: " + farmer.mobile);
                    mobileTextView.setTextSize(16);
                    mobileTextView.setPadding(0, 4, 0, 4);

                    TextView acresTextView = new TextView(FarmerDetails.this);
                    acresTextView.setText("Acres: " + farmer.acres + " acres");
                    acresTextView.setTextSize(16);
                    acresTextView.setPadding(0, 4, 0, 4);

                    TextView emailTextView = new TextView(FarmerDetails.this);
                    emailTextView.setText("Email: " + farmer.email);
                    emailTextView.setTextSize(16);
                    emailTextView.setPadding(0, 4, 0, 4);

                    TextView aadharTextView = new TextView(FarmerDetails.this);
                    aadharTextView.setText("Aadhar Number: " + farmer.aadhar);
                    aadharTextView.setTextSize(16);
                    aadharTextView.setPadding(0, 4, 0, 4);

                    TextView surveyTextView = new TextView(FarmerDetails.this);
                    surveyTextView.setText("Survey Number: " + farmer.surveyNumber);
                    surveyTextView.setTextSize(16);
                    surveyTextView.setPadding(0, 4, 0, 4);

                    TextView addressTextView = new TextView(FarmerDetails.this);
                    addressTextView.setText("Address: " + farmer.address);
                    addressTextView.setTextSize(16);
                    addressTextView.setPadding(0, 4, 0, 4);

                    // Add each TextView to the farmerDetailsLayout
                    farmerDetailsLayout.addView(nameTextView);
                    farmerDetailsLayout.addView(mobileTextView);
                    farmerDetailsLayout.addView(acresTextView);
                    farmerDetailsLayout.addView(emailTextView);
                    farmerDetailsLayout.addView(aadharTextView);
                    farmerDetailsLayout.addView(surveyTextView);
                    farmerDetailsLayout.addView(addressTextView);

                    // Add "Edit" button next to each farmer's details
                    Button editButton = new Button(FarmerDetails.this);
                    editButton.setText("Edit");
                    editButton.setOnClickListener(v -> {
                        // When "Edit" button is clicked, show the form with pre-filled data
                        currentFarmerKey = snap.getKey();
                        nameEdit.setText(farmer.name);
                        mobileEdit.setText(farmer.mobile);
                        emailEdit.setText(farmer.email);
                        acresEdit.setText(String.valueOf(farmer.acres));
                        aadharEdit.setText(farmer.aadhar);
                        surveyEdit.setText(farmer.surveyNumber);
                        addressEdit.setText(farmer.address);

                        formLayout.setVisibility(View.VISIBLE);  // Show the form
                        titleTextView.setVisibility(View.GONE);  // Hide the title
                        addButton.setVisibility(View.GONE);  // Hide "Add Data" button
                        updateButton.setVisibility(View.VISIBLE);  // Show the "Update" button
                    });

                    // Add each field and the edit button to the farmerDetailsLayout
                    farmerDetailsLayout.addView(editButton);

                    // Add the whole farmerDetailsLayout to the farmerListLayout
                    farmerListLayout.addView(farmerDetailsLayout);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FarmerDetails.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package farm.smart;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

public class AgriInput extends AppCompatActivity {

    DatabaseReference agriRef;
    EditText inputNameEdit, companyNameEdit, inputTypeEdit, dosageEdit, usageEdit, beforeEdit, afterEdit;
    LinearLayout agriListLayout, formLayout;
    Button addButton, updateButton, submitButton;
    TextView titleTextView;
    String currentAgriKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agri_input);

        // Initialize Firebase reference
        agriRef = FirebaseDatabase.getInstance().getReference("agriInputs");

        // Initialize Views
        inputNameEdit = findViewById(R.id.editInputName);
        companyNameEdit = findViewById(R.id.editCompanyName);
        inputTypeEdit = findViewById(R.id.editInputType);
        dosageEdit = findViewById(R.id.editDosage);
        usageEdit = findViewById(R.id.editUsagePerMonth);
        beforeEdit = findViewById(R.id.editBeforeUsage);
        afterEdit = findViewById(R.id.editAfterUsage);

        agriListLayout = findViewById(R.id.agriListLayout);
        formLayout = findViewById(R.id.formLayout);
        formLayout.setVisibility(View.GONE);

        addButton = findViewById(R.id.button);
        updateButton = findViewById(R.id.button2);
        submitButton = findViewById(R.id.buttonSubmit);
        titleTextView = findViewById(R.id.textViewTitle);

        // Show form layout to add new Agri input
        addButton.setOnClickListener(v -> {
            formLayout.setVisibility(View.VISIBLE);
            titleTextView.setVisibility(View.GONE);
            addButton.setVisibility(View.GONE);
            updateButton.setVisibility(View.GONE);
        });

        // Submit Agri input data (add or update)
        submitButton.setOnClickListener(v -> {
            // Ensure all fields are not empty
            if (inputNameEdit.getText().toString().isEmpty() || companyNameEdit.getText().toString().isEmpty() ||
                    inputTypeEdit.getText().toString().isEmpty() || dosageEdit.getText().toString().isEmpty() ||
                    usageEdit.getText().toString().isEmpty() || beforeEdit.getText().toString().isEmpty() ||
                    afterEdit.getText().toString().isEmpty()) {
                Toast.makeText(AgriInput.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String inputName = inputNameEdit.getText().toString();
            String companyName = companyNameEdit.getText().toString();
            String inputType = inputTypeEdit.getText().toString();
            String dosage = dosageEdit.getText().toString();
            int usagePerMonth = Integer.parseInt(usageEdit.getText().toString());
            String beforeUsage = beforeEdit.getText().toString();
            String afterUsage = afterEdit.getText().toString();

            // SessionManager for userID retrieval
            SessionManager sessionManager = new SessionManager(AgriInput.this);
            String userId = sessionManager.getUserId();

            if (currentAgriKey.isEmpty()) {
                // Add new Agri input
                String id = agriRef.push().getKey();
                AgriData agriData = new AgriData(inputName, companyName, inputType, dosage, usagePerMonth, beforeUsage, afterUsage, userId);
                agriRef.child(id).setValue(agriData).addOnSuccessListener(unused -> {
                    Toast.makeText(AgriInput.this, "Input Added", Toast.LENGTH_SHORT).show();
                    loadInputs();
                    formLayout.setVisibility(View.GONE);
                    titleTextView.setVisibility(View.VISIBLE);
                    addButton.setVisibility(View.VISIBLE);
                    updateButton.setVisibility(View.VISIBLE);
                }).addOnFailureListener(e -> {
                    Toast.makeText(AgriInput.this, "Failed to Add Input", Toast.LENGTH_SHORT).show();
                });
            } else {
                // Update existing Agri input
                AgriData updatedData = new AgriData(inputName, companyName, inputType, dosage, usagePerMonth, beforeUsage, afterUsage, userId);
                agriRef.child(currentAgriKey).setValue(updatedData).addOnSuccessListener(unused -> {
                    Toast.makeText(AgriInput.this, "Input Updated", Toast.LENGTH_SHORT).show();
                    loadInputs();
                    formLayout.setVisibility(View.GONE);
                    titleTextView.setVisibility(View.VISIBLE);
                    addButton.setVisibility(View.VISIBLE);
                    updateButton.setVisibility(View.VISIBLE);
                    currentAgriKey = "";
                }).addOnFailureListener(e -> {
                    Toast.makeText(AgriInput.this, "Failed to Update Input", Toast.LENGTH_SHORT).show();
                });
            }
        });

        // Load existing Agri inputs from Firebase
        loadInputs();
    }

    // Method to load Agri inputs from Firebase
    public void loadInputs() {
        SessionManager sessionManager = new SessionManager(AgriInput.this);
        String userId = sessionManager.getUserId();

        agriListLayout.removeAllViews();

        agriRef.orderByChild("userId").equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            AgriData data = snap.getValue(AgriData.class);

                            // Create a layout for each Agri input
                            LinearLayout inputLayout = new LinearLayout(AgriInput.this);
                            inputLayout.setOrientation(LinearLayout.VERTICAL);
                            inputLayout.setPadding(16, 16, 16, 16);

                            // Create TextViews to display Agri data
                            TextView inputNameTV = new TextView(AgriInput.this);
                            inputNameTV.setText("Input Name: " + data.inputName);

                            TextView companyNameTV = new TextView(AgriInput.this);
                            companyNameTV.setText("Company: " + data.companyName);

                            TextView inputTypeTV = new TextView(AgriInput.this);
                            inputTypeTV.setText("Type: " + data.inputType);

                            TextView dosageTV = new TextView(AgriInput.this);
                            dosageTV.setText("Dosage: " + data.dosage);

                            TextView usageTV = new TextView(AgriInput.this);
                            usageTV.setText("Usage/Month: " + data.usagePerMonth);

                            TextView beforeTV = new TextView(AgriInput.this);
                            beforeTV.setText("Before Usage: " + data.beforeUsage);

                            TextView afterTV = new TextView(AgriInput.this);
                            afterTV.setText("After Usage: " + data.afterUsage);

                            // Add Edit button to edit Agri input
                            Button editButton = new Button(AgriInput.this);
                            editButton.setText("Edit");
                            editButton.setOnClickListener(v -> {
                                currentAgriKey = snap.getKey();
                                inputNameEdit.setText(data.inputName);
                                companyNameEdit.setText(data.companyName);
                                inputTypeEdit.setText(data.inputType);
                                dosageEdit.setText(data.dosage);
                                usageEdit.setText(String.valueOf(data.usagePerMonth));
                                beforeEdit.setText(data.beforeUsage);
                                afterEdit.setText(data.afterUsage);

                                formLayout.setVisibility(View.VISIBLE);
                                titleTextView.setVisibility(View.GONE);
                                addButton.setVisibility(View.GONE);
                                updateButton.setVisibility(View.VISIBLE);
                            });

                            // Add views to the input layout
                            inputLayout.addView(inputNameTV);
                            inputLayout.addView(companyNameTV);
                            inputLayout.addView(inputTypeTV);
                            inputLayout.addView(dosageTV);
                            inputLayout.addView(usageTV);
                            inputLayout.addView(beforeTV);
                            inputLayout.addView(afterTV);
                            inputLayout.addView(editButton);

                            agriListLayout.addView(inputLayout);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AgriInput.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

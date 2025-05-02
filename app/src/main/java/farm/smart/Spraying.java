package farm.smart;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

public class Spraying extends AppCompatActivity {

    // Firebase reference
    DatabaseReference sprayRef;

    // Views
    EditText sprayingTypeEdit, whySprayingDoneEdit, whenDoneEdit;
    LinearLayout sprayListLayout, formLayout;
    Button addButton, updateButton, submitButton;
    TextView titleTextView;

    String currentSprayKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spraying);

        // Firebase DB reference
        sprayRef = FirebaseDatabase.getInstance().getReference("sprayings");

        // Bind Views
        sprayingTypeEdit = findViewById(R.id.editSprayingType);
        whySprayingDoneEdit = findViewById(R.id.editWhySprayed);
        whenDoneEdit = findViewById(R.id.editWhenSprayed);
        sprayListLayout = findViewById(R.id.farmerListLayout);
        formLayout = findViewById(R.id.formLayout);
        formLayout.setVisibility(View.GONE);

        addButton = findViewById(R.id.button);      // "Add Data" button
        updateButton = findViewById(R.id.button2);  // "Update" button
        submitButton = findViewById(R.id.buttonSubmit);
        titleTextView = findViewById(R.id.textViewTitle);

        // Show form on Add click
        addButton.setOnClickListener(v -> {
            formLayout.setVisibility(View.VISIBLE);
            titleTextView.setVisibility(View.GONE);
            addButton.setVisibility(View.GONE);
            updateButton.setVisibility(View.GONE);
        });

        // Submit data
        submitButton.setOnClickListener(v -> {
            String type = sprayingTypeEdit.getText().toString();
            String reason = whySprayingDoneEdit.getText().toString();
            String when = whenDoneEdit.getText().toString();

            SessionManager sessionManager = new SessionManager(Spraying.this);
            String userId = sessionManager.getUserId();

            if (currentSprayKey.isEmpty()) {
                // Add new entry
                String id = sprayRef.push().getKey();
                Spray spray = new Spray(type, reason, when, userId);
                sprayRef.child(id).setValue(spray).addOnSuccessListener(unused -> {
                    Toast.makeText(Spraying.this, "Spraying data added", Toast.LENGTH_SHORT).show();
                    loadSprayings();
                    resetForm();
                });
            } else {
                // Update existing entry
                Spray updated = new Spray(type, reason, when, userId);
                sprayRef.child(currentSprayKey).setValue(updated).addOnSuccessListener(unused -> {
                    Toast.makeText(Spraying.this, "Spraying data updated", Toast.LENGTH_SHORT).show();
                    loadSprayings();
                    resetForm();
                    currentSprayKey = "";
                });
            }
        });

        loadSprayings();
    }

    private void resetForm() {
        sprayingTypeEdit.setText("");
        whySprayingDoneEdit.setText("");
        whenDoneEdit.setText("");

        formLayout.setVisibility(View.GONE);
        titleTextView.setVisibility(View.VISIBLE);
        addButton.setVisibility(View.VISIBLE);
        updateButton.setVisibility(View.VISIBLE);
    }

    private void loadSprayings() {
        SessionManager sessionManager = new SessionManager(Spraying.this);
        String userId = sessionManager.getUserId();

        sprayListLayout.removeAllViews();

        sprayRef.orderByChild("userId").equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Spray spray = snap.getValue(Spray.class);

                            LinearLayout layout = new LinearLayout(Spraying.this);
                            layout.setOrientation(LinearLayout.VERTICAL);
                            layout.setPadding(16, 16, 16, 16);

                            TextView typeView = new TextView(Spraying.this);
                            typeView.setText("Type of spraying: " + spray.sprayingType);

                            TextView reasonView = new TextView(Spraying.this);
                            reasonView.setText("Reason for Spraying: " + spray.whySprayingDone);

                            TextView whenView = new TextView(Spraying.this);
                            whenView.setText("When the Spraying Done: " + spray.whenDone);

                            Button editBtn = new Button(Spraying.this);
                            editBtn.setText("Edit");
                            editBtn.setOnClickListener(v -> {
                                currentSprayKey = snap.getKey();
                                sprayingTypeEdit.setText(spray.sprayingType);
                                whySprayingDoneEdit.setText(spray.whySprayingDone);
                                whenDoneEdit.setText(spray.whenDone);

                                formLayout.setVisibility(View.VISIBLE);
                                titleTextView.setVisibility(View.GONE);
                                addButton.setVisibility(View.GONE);
                                updateButton.setVisibility(View.VISIBLE);
                            });

                            layout.addView(typeView);
                            layout.addView(reasonView);
                            layout.addView(whenView);
                            layout.addView(editBtn);

                            sprayListLayout.addView(layout);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Spraying.this, "Error loading spraying data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

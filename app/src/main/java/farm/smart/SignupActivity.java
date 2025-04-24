package farm.smart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    EditText username, email, password;
    Button signupBtn;
    TextView toLogin;
    FirebaseAuth auth;
    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth and Realtime Database
        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance("https://farmer-fcf9c-default-rtdb.firebaseio.com/")
                .getReference("users");

        // UI Components
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signupBtn = findViewById(R.id.signupBtn);
        toLogin = findViewById(R.id.toLogin);

        // Navigate to LoginActivity
        toLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });

        // Sign Up Button Click
        signupBtn.setOnClickListener(v -> {
            String enteredUsername = username.getText().toString().trim();
            String enteredEmail = email.getText().toString().trim();
            String enteredPassword = password.getText().toString().trim();

            // Input validation
            if (enteredUsername.isEmpty()) {
                username.setError("Username is required");
                return;
            }
            if (enteredEmail.isEmpty()) {
                email.setError("Email is required");
                return;
            }
            if (enteredPassword.isEmpty()) {
                password.setError("Password is required");
                return;
            }

            // Register with Firebase Authentication
            auth.createUserWithEmailAndPassword(enteredEmail, enteredPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = auth.getCurrentUser();
                            String userId = currentUser.getUid();

                            // Store user data (excluding password)
                            UserModel newUser = new UserModel(userId, enteredUsername, enteredEmail, null);

                            // Save user data to Firebase Realtime Database
                            usersRef.child(userId).setValue(newUser)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(SignupActivity.this,
                                                "Sign up successful!",
                                                Toast.LENGTH_SHORT).show();
                                        Log.d("SignupActivity", "User created: " + userId);

                                        // Save the session
                                        SessionManager sessionManager = new SessionManager(SignupActivity.this);
                                        sessionManager.setLogin(true, userId, enteredUsername, enteredEmail);

                                        // Redirect to login or MainActivity (in case of auto-login)
                                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(SignupActivity.this,
                                                "Database Error: " + e.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    });

                        } else {
                            Toast.makeText(SignupActivity.this,
                                    "Signup failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}

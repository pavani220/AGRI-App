package farm.smart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button loginBtn;
    TextView toSignup;
    FirebaseAuth mAuth;
    DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        toSignup = findViewById(R.id.toSignup);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance("https://farmer-fcf9c-default-rtdb.firebaseio.com/")
                .getReference("users");

        toSignup.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();
        });

        loginBtn.setOnClickListener(v -> {
            String enteredEmail = email.getText().toString().trim();
            String enteredPassword = password.getText().toString().trim();

            if (enteredEmail.isEmpty()) {
                email.setError("Email is required");
                return;
            }

            if (enteredPassword.isEmpty()) {
                password.setError("Password is required");
                return;
            }

            loginUser(enteredEmail, enteredPassword);
        });
    }

    private void loginUser(String enteredEmail, String enteredPassword) {
        mAuth.signInWithEmailAndPassword(enteredEmail, enteredPassword)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user == null) {
                            Toast.makeText(this, "Login failed: user is null", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Fetch user info from database
                        usersRef.child(user.getUid()).get().addOnSuccessListener(snapshot -> {
                            if (snapshot.exists()) {
                                String username = snapshot.child("username").getValue(String.class);
                                String email = snapshot.child("email").getValue(String.class);

                                // Store session
                                SessionManager sessionManager = new SessionManager(LoginActivity.this);
                                sessionManager.setLogin(true, user.getUid(), username, email);

                                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                                // Navigate to MainActivity
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "User data not found.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(e -> {
                            Toast.makeText(LoginActivity.this, "Failed to fetch user info: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });

                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}

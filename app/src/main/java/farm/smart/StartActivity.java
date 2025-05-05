package farm.smart;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class StartActivity extends AppCompatActivity {

    Button userLoginButton, adminLoginButton;
    private static final String ADMIN_PASSWORD = "farmsmart@1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start); // your layout file name

        userLoginButton = findViewById(R.id.button3);  // User Login
        adminLoginButton = findViewById(R.id.button4); // Admin Login

        // ✅ User Login button click: Go directly to LoginActivity
        userLoginButton.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            intent.putExtra("role", "user");
            startActivity(intent);
        });

        // ✅ Admin Login button click: Show password dialog
        adminLoginButton.setOnClickListener(v -> showAdminPasswordDialog());
    }

    private void showAdminPasswordDialog() {
        EditText passwordInput = new EditText(this);
        passwordInput.setHint("Enter admin password");
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        new AlertDialog.Builder(this)
                .setTitle("Admin Access")
                .setMessage("Enter admin password to continue:")
                .setView(passwordInput)
                .setPositiveButton("Submit", (dialog, which) -> {
                    String enteredPassword = passwordInput.getText().toString().trim();
                    if (ADMIN_PASSWORD.equals(enteredPassword)) {
                        // Correct password ➜ go to SignupActivity
                        Intent intent = new Intent(StartActivity.this, SignupActivity.class);
                        intent.putExtra("role", "admin");
                        startActivity(intent);
                    } else {
                        Toast.makeText(StartActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}





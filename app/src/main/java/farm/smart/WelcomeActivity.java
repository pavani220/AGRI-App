package farm.smart;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class WelcomeActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        // Auto navigate after delay
        new Handler().postDelayed(this::navigateToNextScreen, SPLASH_TIME_OUT);
    }

    private void navigateToNextScreen() {
        SessionManager sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn()) {
            // User is already logged in
            startActivity(new Intent(this, MainActivity.class));
        } else {
            // User is not logged in — go to Signup or Login
            startActivity(new Intent(this, SignupActivity.class));
            // Or use: startActivity(new Intent(this, LoginActivity.class));
        }

        finish();
    }
}

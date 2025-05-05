package farm.smart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    private static final int SPLASH_TIME = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome); // your welcome screen layout

        // Delay then move to StartActivity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(WelcomeActivity.this, StartActivity.class);
            startActivity(intent);
            finish(); // close WelcomeActivity
        }, SPLASH_TIME);
    }
}





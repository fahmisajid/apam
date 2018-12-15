package tk.corneliusventi.apam;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class LaunchActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 3000; // 3000 = 3 second
    private final Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.LauncherTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                if (auth.getCurrentUser() == null) {
                    startActivity(new Intent(LaunchActivity.this, WelcomeActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                    finish();
                }
            }
        }, SPLASH_DELAY);

    }
}

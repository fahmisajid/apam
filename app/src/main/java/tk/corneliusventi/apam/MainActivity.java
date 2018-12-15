package tk.corneliusventi.apam;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    public FirebaseUser user;
    public Button signInOutButton;
    public Button orderButton;
    public TextView welcomeBackName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        welcomeBackName = findViewById(R.id.welcome_back_name);
        signInOutButton = findViewById(R.id.sign_inout_button);
        orderButton = findViewById(R.id.order_button);

        if (user != null){

            welcomeBackName.setText(user.getDisplayName());
            signInOutButton.setText("Sign Out");
        } else {
            signInOutButton.setText("Sign In");
        }

        signInOutButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (user != null) {
                    signOut();
                } else {
                    createSignInIntent();
                }
            }
        });
        orderButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (user != null) {
                    startActivity(new Intent(MainActivity.this, OrderActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Sign In First", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.apam)
                        .setTheme(R.style.AppTheme)
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_create_intent]
    }

    // [START auth_fui_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                user = FirebaseAuth.getInstance().getCurrentUser();
                TextView welcomeBackName = findViewById(R.id.welcome_back_name);
                welcomeBackName.setText(user.getDisplayName());
                signInOutButton.setText("Sign Out");
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
    // [END auth_fui_result]

    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(MainActivity.this, LaunchActivity.class));
                        finish();
                    }
                });
        // [END auth_fui_signout]
    }
}

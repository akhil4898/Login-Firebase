package com.example.myfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_CODE = 7717 ;
    List<AuthUI.IdpConfig> providers;
    private Button bttn;
    private TextView mail,name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bttn = (Button) findViewById(R.id.bttn);
        mail= (TextView) findViewById(R.id.mail);
        name=(TextView) findViewById(R.id.name);
        bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                bttn.setEnabled(false);

                                startActivityForResult(
                                        AuthUI.getInstance().createSignInIntentBuilder()
                                                .setAvailableProviders(providers)
                                                .setTheme(R.style.AppTheme)
                                                .build(),MY_CODE
                                );
                            }
                        });
            }
        });
        providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.AppTheme)
                        .build(),MY_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_CODE){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                name.setText(user.getDisplayName());
                mail.setText(user.getEmail());
                bttn.setEnabled(true);
            }
            else
                Toast.makeText(this,""+response.getError().getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}

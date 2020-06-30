package com.abhi.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText emailTextView;
    EditText passwordTextView;
    EditText usernameTextView;
    TextView newTextView;
    Button login;
    Button create;

    //Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailTextView = (EditText) findViewById(R.id.emailEditText);
        passwordTextView = (EditText) findViewById(R.id.passwordEditText);
        usernameTextView = (EditText) findViewById(R.id.usernameTextView);
        newTextView = (TextView) findViewById(R.id.newtextView);
        login = (Button) findViewById(R.id.loginButton);
        create = (Button) findViewById(R.id.createButton);

        //Firebase
        mAuth = FirebaseAuth.getInstance();


    }
    public void changeActivity(){
        Intent intent = new Intent(this,HomeActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            changeActivity();
        }
    }

    public void visibleItems(View view){
        login.setVisibility(View.INVISIBLE);
        newTextView.setVisibility(View.INVISIBLE);
        create.setVisibility(View.VISIBLE);
        usernameTextView.setVisibility(View.VISIBLE);
    }

    public boolean checkFields(int flag){
        //flag 1 - login , flag 2 - create
        boolean temp=true;
        if(flag==1){
            if(TextUtils.isEmpty(emailTextView.getText())){
                emailTextView.setError("Required");
                temp=false;
            }
            if(TextUtils.isEmpty(passwordTextView.getText())){
                passwordTextView.setError("Required");
                temp=false;
            }

        }
        else {
            if(TextUtils.isEmpty(emailTextView.getText())){
                emailTextView.setError("Required");
                temp=false;
            }
            if(TextUtils.isEmpty(passwordTextView.getText())){
                passwordTextView.setError("Required");
                temp=false;
            }
            if(TextUtils.isEmpty(usernameTextView.getText())){
                temp=false;
            }
        }

        return temp;
    }

    public void setLogin(View view){
        if(checkFields(1)) {

            mAuth.signInWithEmailAndPassword(emailTextView.getText().toString().trim(), passwordTextView.getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.i("message", "successfully login");
                                changeActivity();

                            } else {
                                Toast.makeText(MainActivity.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                                Log.i("message", "sign up failed");
                            }
                        }
                    });
        }
    }

    public void setCreate(View view) {
        if (checkFields(2)) {
            mAuth.createUserWithEmailAndPassword(emailTextView.getText().toString().trim(), passwordTextView.getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.i("message", "Successfully create");
                                FirebaseUser user = mAuth.getCurrentUser();
                                if(user!=null){
                                    FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("username").setValue(usernameTextView.getText().toString());
                                    changeActivity();
                                }

                            } else {
                                Toast.makeText(MainActivity.this, "Error occur", Toast.LENGTH_SHORT).show();
                                Log.i("message", "creation failed");
                            }
                        }
                    });
        }
    }

}

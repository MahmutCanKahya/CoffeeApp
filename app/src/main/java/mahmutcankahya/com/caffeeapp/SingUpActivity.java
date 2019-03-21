package mahmutcankahya.com.caffeeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SingUpActivity extends MainActivity{
    private FirebaseAnalytics mFirebaseAnalytics;
    private DatabaseReference mDatabase;
    Button btnSignup;
    EditText userPassword, userMail,userName;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singup);

        mDatabase = FirebaseDatabase.getInstance().getReference();//data base implementeedildi

        auth = FirebaseAuth.getInstance();
        userMail = (EditText) findViewById(R.id.userMail);
        userPassword = (EditText) findViewById(R.id.userPassword);
        userName=findViewById(R.id.userName);

        btnSignup = (Button) findViewById(R.id.btnSingup);

    }

    public void singup(View view){
        final String inputEmail = userMail.getText().toString().trim();
        String inputPassword = userPassword.getText().toString().trim();
        final String inputName= userName.getText().toString().trim();
        if(TextUtils.isEmpty(inputEmail)){
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(inputPassword)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
        } else if (inputPassword.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short!", Toast.LENGTH_SHORT).show();
        }
        else {
            //Create user
            auth.createUserWithEmailAndPassword(inputEmail,inputPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        /////database
                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                        DatabaseReference myRef=database.getReference();
                        mDatabase.child("users").child(auth.getUid()).child("email").setValue(inputEmail);
                        mDatabase.child("users").child(auth.getUid()).child("name").setValue(inputName);




                        startActivity(new Intent(SingUpActivity.this,QrBeginActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Authentication Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void geri(View view) {
        startActivity(new Intent(SingUpActivity.this,MainActivity.class));
    }
}

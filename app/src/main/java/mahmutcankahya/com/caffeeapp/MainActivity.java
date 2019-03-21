package mahmutcankahya.com.caffeeapp;

import android.Manifest;
import android.content.Intent;
import android.net.IpSecManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    Button btnSignin;
    EditText userPassword, userMail;

    FirebaseAuth mAut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int MY_PERMISSIONS_REQUEST_READ_CONTACTS=0;
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},MY_PERMISSIONS_REQUEST_READ_CONTACTS);

        mAut = FirebaseAuth.getInstance();
        userMail = (EditText) findViewById(R.id.userMail);
        userPassword = (EditText) findViewById(R.id.userPassword);

        btnSignin = (Button) findViewById(R.id.btnSingin);

        FirebaseUser user =mAut.getCurrentUser();
        if(user!=null){
            startActivity(new Intent(MainActivity.this,QrBeginActivity.class));
        }


    }


    public void giris(View view) {
        String inputEmail = userMail.getText().toString().trim();
        String inputPassword = userPassword.getText().toString().trim();

        if(TextUtils.isEmpty(inputEmail)){
            Toast.makeText(getApplicationContext(),"Enter a email address!", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(inputPassword)){
            Toast.makeText(getApplicationContext(),"Enter a password!", Toast.LENGTH_LONG).show();
        } else if(inputPassword.length() < 6){
            Toast.makeText(getApplicationContext(),"Password too short!", Toast.LENGTH_LONG).show();
        } else {


            //Sign in user
            mAut.signInWithEmailAndPassword(inputEmail,inputPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        startActivity(new Intent(MainActivity.this,QrBeginActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(),"Failed to Register", Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    public void kayit_ol(View view) {
        startActivity(new Intent(MainActivity.this,SingUpActivity.class));

    }


}

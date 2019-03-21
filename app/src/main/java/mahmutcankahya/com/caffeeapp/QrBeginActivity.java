package mahmutcankahya.com.caffeeapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class QrBeginActivity extends AppCompatActivity {
    FirebaseAuth aut;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_begin);
        aut=FirebaseAuth.getInstance();
    }
    public void qrCode(View view){
        startActivity(new Intent(QrBeginActivity.this,QrCodeActivity.class));
    }

    public void account_exit(View view) {
        aut.signOut();

        startActivity(new Intent(QrBeginActivity.this,MainActivity.class));
    }
}

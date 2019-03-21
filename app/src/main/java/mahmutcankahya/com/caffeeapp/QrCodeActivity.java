package mahmutcankahya.com.caffeeapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import okhttp3.internal.cache.DiskLruCache;

public class QrCodeActivity extends AppCompatActivity {
    SurfaceView surfaceView;
    CameraSource cameraSource;
    private FirebaseAnalytics mFirebaseAnalytics;
    TextView textView;
    BarcodeDetector barcodeDetector;
    FirebaseAuth auth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrcode);
        mDatabase = FirebaseDatabase.getInstance().getReference();//data base implementeedildi
        surfaceView=(SurfaceView) findViewById(R.id.camerapreview);
        textView=(TextView)findViewById(R.id.textView);
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        auth=FirebaseAuth.getInstance();

        cameraSource=new CameraSource.Builder(this,barcodeDetector).setRequestedPreviewSize(1920,1080).setAutoFocusEnabled(true).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(ActivityCompat.checkSelfPermission(QrCodeActivity.this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
                    return;
                }
                try{
                    cameraSource.start(holder);
                }catch (IOException o){
                    o.printStackTrace();
                }



            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> qrCode=detections.getDetectedItems();
                if(qrCode.size()!=0){

                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator=(Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            final String masa;
                            final String kafeAdi;

                            String text=qrCode.valueAt(0).displayValue;
                            int count=qrCode.valueAt(0).displayValue.length();
                            try {
                                Thread.sleep(2222);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            masa= qrCode.valueAt(0).displayValue.substring(count-4,count);
                            kafeAdi=qrCode.valueAt(0).displayValue.substring(0,count-4);

                            mDatabase.child("qrcode").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(kafeAdi)){
                                        cameraSource.stop();
                                        System.out.println("ASDF" + mDatabase.child("qrcode").getKey());

                                        textView.setText("Lütfen Bekleyiniz.");
                                        Intent intent=new Intent(getApplicationContext(),MenuActivity.class);
                                        intent.putExtra("send_string",kafeAdi);
                                        intent.putExtra("masa",masa);
                                        startActivity(intent);

                                    }else {
                                        textView.setText("Böyle bir kafe bulunmamaktadır");
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                startActivity(getIntent());
                                            }
                                        }, 500);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                        }
                    });

                }
            }
        });

    }
    public void geri(View view) {
        startActivity(new Intent(QrCodeActivity.this
                ,MainActivity.class));
    }
}

package mahmutcankahya.com.caffeeapp;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class BasketActivity extends AppCompatActivity {
    ArrayList<HashMap<String, String>> urun_liste;
    ArrayList<String> urunAdiB;
    ArrayList<String> urunFiyatB;
    ArrayList<String> urunAdetB;
    ArrayList<String> urunToplamB;
    PostBasketClass postBasketClass;
    ListView listView;
    Database db;
    private DatabaseReference mDatabase;
    FirebaseDatabase mfirebaseDatabase;
    //String urun_adlari[];
    //String urun_fiyatlari[];
    ///String urun_adetleri[];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basket);

    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onResume() {
        super.onResume();

        db = new Database(getApplicationContext());
        urun_liste = db.urunler();

        mfirebaseDatabase=FirebaseDatabase.getInstance();
        mDatabase=mfirebaseDatabase.getReference();

        listView = (ListView) findViewById(R.id.listView);
        urunAdiB=new ArrayList<>(urun_liste.size());
        urunFiyatB=new ArrayList<>(urun_liste.size());
        urunAdetB=new ArrayList<>(urun_liste.size());
        urunToplamB=new ArrayList<>(urun_liste.size());
        postBasketClass=new PostBasketClass(urunAdiB,urunFiyatB,urunAdetB,this);
        listView.setAdapter(postBasketClass);

        getData();

    }

    public void getData(){
        for(int i=0;i<urun_liste.size();i++){
            urunAdiB.add(urun_liste.get(i).get("urun_adi"));
            urunFiyatB.add(urun_liste.get(i).get("urun_fiyat"));
            urunAdetB.add(urun_liste.get(i).get("urun_adet"));
            urunToplamB.add(urun_liste.get(i).get("urun_toplam"));
            postBasketClass.notifyDataSetChanged();
        }
    }
    public void deleteData(Database db){
        db.resetTables();
    }



    public void basketCheck(View view) {
        Date  tarih = new Date();
        SimpleDateFormat dakika = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        UrunBilgi urunBilgi;
        String strDate = dakika.format(tarih);
        Bundle extras = getIntent().getExtras();
        final String value = extras.getString("kafe_adi");
        final String value2 = extras.getString("masa");
        for(int i=0; i<urun_liste.size(); i++){
            urunBilgi=new UrunBilgi(urunAdetB.get(i),urunToplamB.get(i));
            mDatabase.child(value).child("siparisler").child(strDate).child(value2).child(urunAdiB.get(i)).setValue(urunBilgi);

        }
        postBasketClass.clearList();

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Siparişiniz Gönderiliyor");
        progress.show();

        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                progress.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 1500);




        deleteData(db);
    }

    public void backOut(View view) {
        urunAdiB.clear();
        urunAdetB.clear();
        urun_liste.clear();
        urunFiyatB.clear();
        postBasketClass.notifyDataSetChanged();
        deleteData(db);
        //startActivity(new Intent(BasketActivity.this,MenuActivity.class));
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteData(db);
    }

}

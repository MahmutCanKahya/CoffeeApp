package mahmutcankahya.com.caffeeapp;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class BuyViewActivity extends AppCompatActivity{
    MediaPlayer mp;
    private TextView urunAdiB;
    private TextView urunAciklamaB;
    private TextView urunFiyatB;
    private ImageView urunResimB;
    EditText adet;
    Button addToBacket;
    Button increase;
    Button decrease;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.buy_view);
        setContentView(R.layout.buy_view);
        mp=MediaPlayer.create(this,R.raw.ses);
        urunAdiB=(TextView)findViewById(R.id.urun_adi_buy);
        urunAciklamaB=(TextView)findViewById(R.id.urun_aciklama_buy);
        urunFiyatB=(TextView)findViewById(R.id.urun_fiyat_buy);
        urunResimB=(ImageView)findViewById(R.id.urun_resim_buy);
        adet=(EditText)findViewById(R.id.piece);
        addToBacket=(Button)findViewById(R.id.sepete_ekle);
        increase=(Button)findViewById(R.id.increase_buy);
        decrease=(Button)findViewById(R.id.decrease_buy);
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int girilenSayi=Integer.parseInt(adet.getText().toString());
                girilenSayi++;
                String sayi=Integer.toString(girilenSayi);

                adet.setText(sayi);
            }
        });
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int girilenSayi=Integer.parseInt(adet.getText().toString());
                girilenSayi--;
                String sayi=Integer.toString(girilenSayi);
                adet.setText(sayi);
            }
        });
        adet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                int girilenSayi=Integer.parseInt(s.toString());
                try{
                    if(girilenSayi>100){
                        s.replace(0,s.length(),"100");
                    }
                    else if(girilenSayi<=0){
                        s.replace(0,s.length(),"1");
                    }
                }catch (NumberFormatException e){

                }
            }
        });

        Bundle extras = getIntent().getExtras();/////////
        final String ad = extras.getString("a");
        final String aciklama = extras.getString("b");
        final String fiyat = extras.getString("c");
        final String resim = extras.getString("d");

        urunAdiB.setText(ad);
        urunAciklamaB.setText(aciklama);
        urunFiyatB.setText(fiyat);
        Picasso.get().load(resim).into(urunResimB);


        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width=dm.widthPixels;
        int height=dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));


        show();


    }



    public void addToBasket(View view) {
        mp.start();
        Intent intent=new Intent(getApplicationContext(),BasketActivity.class);
        String u_Ad=urunAdiB.getText().toString().trim();
        String u_Fiyat=urunFiyatB.getText().toString().trim();
        String u_Adet=adet.getText().toString().trim();
        Database db = new Database(getApplicationContext());
        Float totalAdet=Float.parseFloat(u_Adet);
        Float totalFiyat=Float.parseFloat(u_Fiyat);
        Float x=totalAdet*totalFiyat;
        String sonuc=Float.toString(x);
        db.urun_ekle(u_Ad,sonuc,u_Adet,u_Fiyat);

    }

    public void show(){

    }

}

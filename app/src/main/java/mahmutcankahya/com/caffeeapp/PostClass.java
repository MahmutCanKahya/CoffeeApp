package mahmutcankahya.com.caffeeapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Intent.getIntent;

public class PostClass extends ArrayAdapter<String> {
    private final ArrayList<String> urunAdi;
    private final ArrayList<String> urunAciklama;
    private final ArrayList<String> urunFiyat;
    private final ArrayList<String> urunResim;
    private final Activity context;

    public PostClass(ArrayList<String> urunAdi, ArrayList<String> urunAciklama, ArrayList<String> urunFiyat, ArrayList<String> urunResim, Activity context){
        super(context,R.layout.custom_view,urunAdi);
        this.urunAdi=urunAdi;
        this.urunAciklama=urunAciklama;
        this.urunFiyat=urunFiyat;
        this.urunResim=urunResim;
        this.context = context;
    }
    public void clearList(){
        urunAdi.clear();
        urunResim.clear();;
        urunFiyat.clear();
        urunAciklama.clear();
        this.clear();
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater =context.getLayoutInflater();
        View customView=layoutInflater.inflate(R.layout.custom_view,null,true);
        TextView urunAdiText=customView.findViewById(R.id.menu_urun_adi);
        TextView urunAcıklamaText=customView.findViewById(R.id.menu_urun_aciklama);
        TextView urunFiyatText=customView.findViewById(R.id.menu_urun_fiyat);
        ImageView urunImage=customView.findViewById(R.id.menu_resmi);

        urunAdiText.setText(urunAdi.get(position));
        urunAcıklamaText.setText(urunAciklama.get(position));
        urunFiyatText.setText(urunFiyat.get(position));
        Picasso.get().load(urunResim.get(position)).into(urunImage);

        return customView;
    }


}

package mahmutcankahya.com.caffeeapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostBasketClass extends ArrayAdapter<String> {
    private final ArrayList<String> urunAdi;
    private final ArrayList<String> urunFiyat;
    private final ArrayList<String> urunAdet;
    private final Activity context;

    public PostBasketClass(ArrayList<String> urunAdi, ArrayList<String> urunFiyat, ArrayList<String> urunAdet, Activity context){
        super(context,R.layout.basket_custom_view,urunAdi);
        this.urunAdi=urunAdi;
        this.urunFiyat=urunFiyat;
        this.urunAdet=urunAdet;
        this.context = context;
    }
    public void clearList(){
        urunAdi.clear();
        urunAdet.clear();;
        urunFiyat.clear();
        this.clear();
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater =context.getLayoutInflater();
        View customView=layoutInflater.inflate(R.layout.basket_custom_view,null,true);
        TextView urunAdiText=customView.findViewById(R.id.urun_isim_b);
        TextView urunFiyatText=customView.findViewById(R.id.urun_fiyat_b);
        TextView urunAdetText=customView.findViewById(R.id.urun_adet_b);

        urunAdiText.setText(urunAdi.get(position));
        urunFiyatText.setText(urunFiyat.get(position));
        urunAdetText.setText(urunAdet.get(position));
        return customView;
    }
}

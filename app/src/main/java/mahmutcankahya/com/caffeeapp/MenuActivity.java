package mahmutcankahya.com.caffeeapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;

public class MenuActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    BottomNavigationView bottomNavigationView;
    FirebaseDatabase firebaseDatabase;
    SwipeMenuListView listView;
    ArrayList<String> menuUrunAdi;
    ArrayList<String> menuUrunAcıklama;
    ArrayList<String> menuUrunFiyat;
    ArrayList<String> menuUrunResim;
    PostClass postClass;
    private static final String TAG = "MenuActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);



        firebaseDatabase=FirebaseDatabase.getInstance();
        mDatabase=firebaseDatabase.getReference();

        listView =findViewById(R.id.listView);
        menuUrunAdi=new ArrayList<>();
        menuUrunAcıklama=new ArrayList<>();
        menuUrunFiyat=new ArrayList<>();
        menuUrunResim=new ArrayList<>();

        postClass=new PostClass(menuUrunAdi,menuUrunAcıklama,menuUrunFiyat,menuUrunResim,this);
        listView.setAdapter(postClass);

        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        getDataFromFirebase("sicak");
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull android.view.MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.action_sicak:
                                postClass.clearList();
                                getDataFromFirebase("sicak");
                                return true;
                            case R.id.action_soguk:
                                postClass.clearList();
                                getDataFromFirebase("soguk");
                                return true;
                            case R.id.action_yemek:
                                postClass.clearList();
                                getDataFromFirebase("atistirmalik");
                                return true;
                            case R.id.action_tatli:
                                postClass.clearList();
                                getDataFromFirebase("tatli");
                                return true;
                        }
                        return false;
                    }

                });
        swipeMenu();

    }
    private void getDataFromFirebase(final String cins) {
        DatabaseReference newReference3= firebaseDatabase.getReference();
        newReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Bundle extras = getIntent().getExtras();/////////
                final String value = extras.getString("send_string");
                for (DataSnapshot ds : dataSnapshot.child("qrcode").child(value).child("menu").child(cins).getChildren()) {
                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();

                    menuUrunAdi.add(hashMap.get("isim"));
                    menuUrunAcıklama.add(hashMap.get("aciklama"));
                    menuUrunFiyat.add(hashMap.get("fiyat"));
                    menuUrunResim.add(hashMap.get("resim"));
                    postClass.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void swipeMenu(){
        /// SWİPE MENU OLUSTURMA
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xfe, 0xe0,
                        0x61)));
                // set item width
                openItem.setWidth(270);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                openItem.setIcon(R.drawable.shopping_cart);
                menu.addMenuItem(openItem);

            }
        };

        // set creator
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        String urun_adi=menuUrunAdi.get(position);
                        String urun_aciklama=menuUrunAcıklama.get(position);
                        String urun_fiyat=menuUrunFiyat.get(position);
                        String urun_resim=menuUrunResim.get(position);
                        Intent intent=new Intent(getApplicationContext(),BuyViewActivity.class);
                        intent.putExtra("a",urun_adi);
                        intent.putExtra("b",urun_aciklama);
                        intent.putExtra("c",urun_fiyat);
                        intent.putExtra("d",urun_resim);
                        startActivity(intent);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    public void logaut(View view){

        startActivity(new Intent(MenuActivity.this,QrBeginActivity.class));
    }
    public void BasketOpen(View view) {
        Bundle extras = getIntent().getExtras();/////////
        final String value = extras.getString("send_string");
        final String value2 = extras.getString("masa");
        Intent intent=new Intent(getApplicationContext(),BasketActivity.class);
        intent.putExtra("kafe_adi",value);
        intent.putExtra("masa",value2);
        startActivity(intent);
    }
}


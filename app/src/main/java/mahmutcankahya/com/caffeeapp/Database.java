package mahmutcankahya.com.caffeeapp;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "sqllite_database";//database adı

    private static final String TABLE_NAME = "urun_liste";
    private static String URUN_ADI = "urun_adi";
    private static String URUN_FIYAT = "urun_fiyat";
    private static String URUN_ADET = "urun_adet";
    private static String URUN_TOPLAM = "urun_toplam";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {  // Databesi oluşturuyoruz.Bu methodu biz çağırmıyoruz. Databese de obje oluşturduğumuzda otamatik çağırılıyor.
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + URUN_ADI+ " TEXT ,"
                + URUN_FIYAT + " TEXT,"
                + URUN_ADET + " TEXT,"
                + URUN_TOPLAM + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    public void urun_sil(int id){ //id si belli olan row u silmek için

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, URUN_ADI + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    public void urun_ekle(String urun_adi, String toplam,String urun_adet,String urun_fiyat) {
        //kitapEkle methodu ise adı üstünde Databese veri eklemek için
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(URUN_ADI, urun_adi);
        values.put(URUN_FIYAT, urun_fiyat);
        values.put(URUN_ADET, urun_adet);
        values.put(URUN_TOPLAM, toplam);
        db.insert(TABLE_NAME, null, values);
        db.close(); //Database Bağlantısını kapattık*/
    }

    public  ArrayList<HashMap<String, String>> urunler(){

        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> urunlist = new ArrayList<HashMap<String, String>>();
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                urunlist.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        // return kitap liste
        return urunlist;
    }

    public void urun_duzenle(String urun_adi, String urun_toplam,String urun_adet,String urun_fiyat,int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Bu methodda ise var olan veriyi güncelliyoruz(update)
        ContentValues values = new ContentValues();
        values.put(URUN_ADI, urun_adi);
        values.put(URUN_FIYAT, urun_fiyat);
        values.put(URUN_ADET, urun_adet);
        values.put(URUN_TOPLAM, urun_toplam);

        // updating row
        db.update(TABLE_NAME, values, URUN_ADI + " = ?",
                new String[] { String.valueOf(id) });
    }

    public int getRowCount() {
        // Bu method bu uygulamada kullanılmıyor ama her zaman lazım olabilir.Tablodaki row sayısını geri döner.
        //Login uygulamasında kullanacağız
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }

    public void resetTables(){
        //Bunuda uygulamada kullanmıyoruz. Tüm verileri siler. tabloyu resetler.
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

}
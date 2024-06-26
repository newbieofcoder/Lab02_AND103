package fpoly.account.myapplication.ModelHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import fpoly.account.myapplication.models.Model;

public class ModelDatabaseHelper extends SQLiteOpenHelper {
    public ModelDatabaseHelper(Context mContext) {
        super(mContext, "DB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE Model(\n" +
                "\tid int PRIMARY KEY,\n" +
                "    title text,\n" +
                "    description text\n" +
                ");";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Model");
    }

    public void them(Model model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", model.getTitle());
        values.put("description", model.getDescription());
        db.insert("Model", null, values);
        db.close();
    }

    public List<Model> getAllData() {
        List<Model> list = new ArrayList<>();
        String query = "SELECT * FROM Model";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Model model = new Model();
                model.setTitle(cursor.getString(1));
                model.setDescription(cursor.getString(2));
                list.add(model);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
}

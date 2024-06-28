package fpoly.account.myapplication.ModelHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fpoly.account.myapplication.models.Model;

public class ModelDatabaseHelper extends SQLiteOpenHelper {
    public ModelDatabaseHelper(Context mContext) {
        super(mContext, "ToDoDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE Model(\n" +
                "\tid integer PRIMARY KEY AUTOINCREMENT,\n" +
                "    title text,\n" +
                "    content text,\n" +
                "    day text,\n" +
                "    type text\n" +
                ");";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Model");
        onCreate(db);
    }

    public void add(Model model) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put("day", model.getDate());
        values.put("type", model.getType());
        values.put("title", model.getTitle());
        values.put("content", model.getContent());
        db.insert("Model", null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void update(Model model, String oldTitle) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String query = "UPDATE Model SET title = '" + model.getTitle() + "', " +
                "content = '" + model.getContent() + "', " +
                "day = '" + model.getDate() + "', " +
                "type = '" + model.getType() + "' " +
                "WHERE title = '" + oldTitle + "'";
        db.execSQL(query);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public void delete(Model model) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String query = "DELETE FROM Model WHERE title = '" + model.getTitle() + "'" + " AND content = '" + model.getContent() + "'" + " AND day = '" + model.getDate() + "'" + " AND type = '" + model.getType() + "'";
        db.execSQL(query);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

    public List<Model> getAllData() {
        List<Model> list = new ArrayList<>();
        String query = "SELECT * FROM Model";
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Model model = new Model();
                model.setTitle(cursor.getString(1));
                model.setContent(cursor.getString(2));
                model.setDate(cursor.getString(3));
                model.setType(cursor.getString(4));
                list.add(model);
            } while (cursor.moveToNext());
            db.setTransactionSuccessful();
        }
        db.endTransaction();
        cursor.close();
        db.close();
        return list;
    }
}

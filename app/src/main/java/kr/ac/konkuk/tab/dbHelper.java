package kr.ac.konkuk.tab;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mytabata.db";
    private static final int DATABASE_VERSION = 2;

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //db table 생성 및 연동
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE tabata ( mtabataname TEXT PRIMARY KEY, " + "tabatatype TEXT, sets INTEGER,strList TEXT,exercisetime INTEGER,resttime INTEGER,settime INTEGER );");
    }

    //db 업데이트
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tabata");
        onCreate(db);
    }

}

package project.tfg.ecgscan.data.local;

import android.content.Context;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import project.tfg.ecgscan.data.local.model.BitmapConverter;
import project.tfg.ecgscan.data.local.model.Electro;

@Database(entities = {Electro.class}, version = 1)
@TypeConverters({BitmapConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "database";
    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (ContactsContract.Data.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME).addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        }
                    }).build();
                }
            }
        }
        return instance;
    }

    public abstract ElectroDao electroDao();



}

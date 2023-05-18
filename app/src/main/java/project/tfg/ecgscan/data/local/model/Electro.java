package project.tfg.ecgscan.data.local.model;

import android.graphics.Bitmap;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"individualId"}, unique = true)})
public class Electro {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "individualId")
    private long individualId;

    @ColumnInfo(name = "image")
    private Bitmap image;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "date")
    private String date;

    public Electro(long individualId, Bitmap image, String name, String date) {
        this.individualId = individualId;
        this.image = image;
        this.name = name;
        this.date = date;
    }

    public long getIndividualId() {
        return individualId;
    }

    public void setIndividualId(long individualId) {
        this.individualId = individualId;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

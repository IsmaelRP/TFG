package project.tfg.ecgscan.data;

import android.graphics.Bitmap;

import java.util.concurrent.atomic.AtomicLong;

public class ElectroImage implements Comparable<ElectroImage> {

    private Bitmap image;
    private String name;
    private String date;
    private long individualId;
    private static AtomicLong id = new AtomicLong(1);

    public ElectroImage(Bitmap image, String name, String date) {
        this.image = image;
        this.name = name;
        this.date = date;
        individualId = id.getAndIncrement();
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

    public long getId() {
        return this.individualId;
    }

    public void setId(long id) {
        this.individualId = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    @Override
    public int compareTo(ElectroImage electroImage) {
        return this.getName().compareTo(electroImage.getName());
    }


}

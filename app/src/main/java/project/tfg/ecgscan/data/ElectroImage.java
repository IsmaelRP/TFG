package project.tfg.ecgscan.data;

import android.graphics.Bitmap;

public class ElectroImage implements Comparable<ElectroImage>{

    private Bitmap image;
    private String name;
    private long individualId;
    private static long id = 1;

    public ElectroImage(Bitmap image, String name) {
        this.image = image;
        this.name = name;
        individualId = id;
        id ++;
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


    @Override
    public int compareTo(ElectroImage electroImage) {
        return this.getName().compareTo(electroImage.getName());
    }
}

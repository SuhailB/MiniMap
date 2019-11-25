package edu.uark.csce.minimap.ui.notifications;

import android.graphics.Bitmap;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Post {

    private String text;
    private long time;
    private String imageName;
    private String imageURL;
    private Bitmap image;
//    private boolean permission;

    public Post() {
    }

    public Post(String text, long time, String imageName, String imageURL, Bitmap image) {
        this.text = text;
        this.time = time;
        this.imageName = imageName;
        this.imageURL = imageURL;
        this.image = image;
//        this.permission = permission;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

//    public String getPostPic() {
//        return PostPic;
//    }

//    public void setPostPic(String PostPic) {
//        this.PostPic = PostPic;
//    }
//
//    public boolean getPermission() {
//        return permission;
//    }
//
//    public void setPermission(boolean permission) {
//        this.permission = permission;
//    }

}

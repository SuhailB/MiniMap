package edu.uark.csce.minimap.ui.notifications;

public class uploadinfo {

    public String imageName;
    public String imageURL;
    public uploadinfo(){}

    public uploadinfo(String name, String url) {
//        if(imageName.trim().equals(""))
//        {
//            imageName = "No Title Entered";
//        }
        this.imageName = name;
        this.imageURL = url;
    }

    public String getImageName() {
        return imageName;
    }
    public String getImageURL() {
        return imageURL;
    }
}

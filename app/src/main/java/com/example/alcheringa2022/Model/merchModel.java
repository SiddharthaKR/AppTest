package com.example.alcheringa2022.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import java.util.ArrayList;

public class merchModel implements Parcelable {
    String Name;
    String Material;
    String Price;
    String Description;
    String Image_url;
    Boolean Is_available;
    Boolean Small;
    Boolean Medium;
    Boolean Large;
    Boolean Xlarge;
    ArrayList<String> images;
    String Video_url;

    protected merchModel(Parcel in) {
        Name = in.readString();
        Material = in.readString();
        Price = in.readString();
        Description = in.readString();
        Image_url = in.readString();
        byte tmpIs_available = in.readByte();
        Is_available = tmpIs_available == 0 ? null : tmpIs_available == 1;
        byte tmpSmall = in.readByte();
        Small = tmpSmall == 0 ? null : tmpSmall == 1;
        byte tmpMedium = in.readByte();
        Medium = tmpMedium == 0 ? null : tmpMedium == 1;
        byte tmpLarge = in.readByte();
        Large = tmpLarge == 0 ? null : tmpLarge == 1;
        byte tmpXlarge = in.readByte();
        Xlarge = tmpXlarge == 0 ? null : tmpXlarge == 1;
        images = in.createStringArrayList();
        Video_url = in.readString();
    }

    public static final Creator<merchModel> CREATOR = new Creator<merchModel>() {
        @Override
        public merchModel createFromParcel(Parcel in) {
            return new merchModel(in);
        }

        @Override
        public merchModel[] newArray(int size) {
            return new merchModel[size];
        }
    };

    public void setName(String name) {
        Name = name;
    }

    public void setMaterial(String material) {
        Material = material;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setImage_url(String image_url) {
        Image_url = image_url;
    }

    public void setIs_available(Boolean is_available) {
        Is_available = is_available;
    }

    public void setSmall(Boolean small) {
        Small = small;
    }

    public void setMedium(Boolean medium) {
        Medium = medium;
    }

    public void setLarge(Boolean large) {
        Large = large;
    }

    public void setXlarge(Boolean xlarge) {
        Xlarge = xlarge;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getName() {
        return Name;
    }

    public String getMaterial() {
        return Material;
    }

    public String getPrice() {
        return Price;
    }

    public String getDescription() {
        return Description;
    }

    public String getImage_url() {
        return Image_url;
    }

    public String getVideo_url(){return Video_url;}

    public Boolean getIs_available() {
        return Is_available;
    }

    public Boolean getSmall() {
        return Small;
    }

    public Boolean getMedium() {
        return Medium;
    }

    public Boolean getLarge() {
        return Large;
    }

    public Boolean getXlarge() {
        return Xlarge;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public merchModel(String name, String material, String price, String description, String image_url, Boolean is_available, Boolean small, Boolean medium, Boolean large, Boolean xlarge, ArrayList<String> images, String video_url) {
        Name = name;
        Material = material;
        Price = price;
        Description = description;
        Image_url = image_url;
        Is_available = is_available;
        Small = small;
        Medium = medium;
        Large = large;
        Xlarge = xlarge;
        this.images = images;
        Video_url = video_url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(Material);
        dest.writeString(Price);
        dest.writeString(Description);
        dest.writeString(Image_url);
        dest.writeByte((byte) (Is_available == null ? 0 : Is_available ? 1 : 2));
        dest.writeByte((byte) (Small == null ? 0 : Small ? 1 : 2));
        dest.writeByte((byte) (Medium == null ? 0 : Medium ? 1 : 2));
        dest.writeByte((byte) (Large == null ? 0 : Large ? 1 : 2));
        dest.writeByte((byte) (Xlarge == null ? 0 : Xlarge ? 1 : 2));
        dest.writeStringList(images);
        dest.writeString(Video_url);
    }
}
package com.example.modul_2;

import android.os.Parcel;
import android.os.Parcelable;


public class Car implements Parcelable {
    private String id;
    private String brand;
    private String manufacturer;
    private int price;
    private String engineType;
    private String transmissionType;
    private String transmission;
    private String bodyType;

    private String color;
    private String imageUrl;

    public String getId(){return this.id;}

    public Car(String id,String brand, String manufacturer, int price, String engineType, String transmissionType, String transmission, String bodyType, String color, String imageUrl) {
        this.brand = brand;
        this.manufacturer = manufacturer;
        this.price = price;
        this.engineType = engineType;
        this.transmissionType = transmissionType;
        this.transmission = transmission;
        this.bodyType = bodyType;
        this.color = color;
        this.imageUrl = imageUrl;
        this.id= id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public String getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(String transmissionType) {
        this.transmissionType = transmissionType;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    protected Car(Parcel in) {
        brand = in.readString();
        manufacturer = in.readString();
        price = in.readInt();
        engineType = in.readString();
        transmissionType = in.readString();
        transmission = in.readString();
        bodyType = in.readString();
        color = in.readString();
        imageUrl = in.readString();
        id=in.readString();
    }

    public static final Parcelable.Creator<Car> CREATOR = new Parcelable.Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(brand);
        dest.writeString(manufacturer);
        dest.writeInt(price);
        dest.writeString(engineType);
        dest.writeString(transmissionType);
        dest.writeString(transmission);
        dest.writeString(bodyType);
        dest.writeString(color);
        dest.writeString(imageUrl);
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}




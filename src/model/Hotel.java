package model;

public class Hotel {

    private int hotelId;
    private String name;
    private String location;
    private String amenities;

    // Default Constructor
    public Hotel() {
    }

    // Parameterized Constructor
    public Hotel(int hotelId, String name, String location, String amenities) {
        this.hotelId = hotelId;
        this.name = name;
        this.location = location;
        this.amenities = amenities;
    }

    // Getters and Setters

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "hotelId=" + hotelId +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", amenities='" + amenities + '\'' +
                '}';
    }
}

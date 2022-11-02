package model;

import java.util.Objects;

public class Room implements IRoom {

    private final String roomNumber;
    private final RoomType roomType;
    private Double roomPrice;



    public Room(String roomNumber, Double roomPrice, RoomType enumeration) {
        this.roomNumber = roomNumber;
        this.roomPrice = roomPrice;
        this.roomType = enumeration;
    }



    public void setRoomPrice(Double roomPrice) {
        this.roomPrice = roomPrice;
    }


    @Override
    public String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public Double getRoomPrice() {
        return roomPrice;
    }

    @Override
    public RoomType getRoomType() {
        return roomType;
    }

    @Override
    public boolean isFree() {
        return false;
    }


    @Override
    public String toString() {
        return "Room{" +
                "roomNumber='" + roomNumber + '\'' +
                ", roomPrice=" + roomPrice +
                ", roomType=" + roomType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room room)) {
            return false;
        }

        return getRoomNumber().equals(room.getRoomNumber()) && getRoomPrice().equals(room.getRoomPrice()) && getRoomType() == room.getRoomType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoomNumber(), getRoomPrice(), getRoomType());
    }
}

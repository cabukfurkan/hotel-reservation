package model;

import java.util.Objects;

public class Room implements IRoom {

    private String roomNumber;
    private Double roomPrice;
    private RoomType roomType;

    private Boolean isReserved = false;

    public Room(String roomNumber, Double roomPrice, RoomType enumeration) {
        this.roomNumber = roomNumber;
        this.roomPrice = roomPrice;
        this.roomType = enumeration;
    }


    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setRoomPrice(Double roomPrice) {
        this.roomPrice = roomPrice;
    }

    public Boolean getIsReserved() {
        return isReserved;
    }

    public void setIsReserved(Boolean isReserved) {
        this.isReserved = isReserved;
    }

    public void setRoomType(RoomType enumeration) {
        this.roomType = enumeration;
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
        if (!(o instanceof Room room)) return false;
        return getRoomNumber().equals(room.getRoomNumber()) && getRoomPrice().equals(room.getRoomPrice()) && getRoomType() == room.getRoomType() && getIsReserved().equals(room.getIsReserved());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoomNumber(), getRoomPrice(), getRoomType(), getIsReserved());
    }
}

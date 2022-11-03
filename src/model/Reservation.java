package model;

import java.util.*;

public class Reservation {

    private final Customer customer;
    private Room room;
    private Date checkInDate;
    private Date checkOutDate;


    public Reservation(Customer customer, Room room, Date checkInDate, Date checkOutDate) {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public boolean collidesWith (Date newCheckInDate, Date newCheckOutDate){
            //check reservation.colliedesWith(reservation) if true continue
            //check the same thing for the checkout date if it is bw the other resevations dates
            if (newCheckInDate.equals(this.checkInDate)||newCheckInDate.equals(this.checkOutDate) ) {
                return true;
            }
            if (newCheckInDate.after(this.checkInDate) && newCheckInDate.before(this.checkOutDate)){
                return true;
            }
            if (newCheckOutDate.equals(this.checkOutDate)||newCheckOutDate.equals(this.checkInDate) ) {
                return true;
            }
            if (newCheckOutDate.after(this.checkInDate) && newCheckOutDate.before(this.checkOutDate)){
                return true;
            }
            return false;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "customer=" + customer +
                ", room=" + room +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation that)) return false;
        return getCustomer().equals(that.getCustomer()) && getRoom().equals(that.getRoom()) && getCheckInDate().equals(that.getCheckInDate()) && getCheckOutDate().equals(that.getCheckOutDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomer(), getRoom(), getCheckInDate(), getCheckOutDate());
    }
}

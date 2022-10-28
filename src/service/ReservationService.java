package service;

import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;

import java.util.*;

public class ReservationService {
    public static final ReservationService reservationService = new ReservationService();

    Collection<Room> rooms = new ArrayList<>();
    Collection<Reservation> reservations = new ArrayList<>();

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public Room getARoom(String roomId) {
        Room foundRoom = null;
        for (Room room : rooms) {
            if(room.getRoomNumber().equals(roomId)){
                foundRoom = room;
            }
        }
        return foundRoom;
    }

    public Collection<Room> getAllRooms(){
        for (Room room:rooms
             ) {
            System.out.println("room = " + room);
        }
        return rooms;
    }


    public Reservation reserveARoom(Customer customer, Room room, Date checkInDate, Date checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(reservation);
        room.setIsReserved(true);
        System.out.println("reservation is done = " + reservation) ;

        return reservation;
    }

    public Collection<Room> findRooms(Date checkInDate, Date checkOutDate) {

        return null;
    }

    public Collection<Reservation> getCustomersReservation(String customerEmail) {
        Reservation foundReservation = null;
        for (Reservation reservation:reservations
             ) {
            if (reservation.getCustomer().getEmail().equals(customerEmail)){
                return (Collection<Reservation>) foundReservation;
            }
        }
        return null;
    }
    public void printAllReservation() {

        for (Reservation reservation: reservations) {
            System.out.println(reservation);
        }
    }
}

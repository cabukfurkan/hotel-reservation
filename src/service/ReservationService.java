package service;

import api.AdminResource;
import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;
import org.w3c.dom.ls.LSOutput;

import java.util.*;

public class ReservationService {
    public static ReservationService reservationService = new ReservationService();
    Collection<Room> rooms = new ArrayList<>();

    Map<String, List<Reservation>> roomReservations = new HashMap<>();

    public void addRoom(Room room) {

        int count = 0;

        for (Room existedRoom : rooms
        ) {
            if (existedRoom.getRoomNumber().equals(room.getRoomNumber())) {
                count++;
                System.out.println("This room already exists.");
                System.out.println("==========================");
                break;
            }
        }
        if (count == 0) {
            rooms.add(room);
            System.out.println("Room " + room.getRoomNumber() + " has been added successfully.");
        }
    }

    public Room getARoom(String roomId) {
        Room foundRoom = null;
        for (Room room : rooms) {
            if (room.getRoomNumber().equals(roomId)) {
                foundRoom = room;
            }
        }
        return foundRoom;
    }

    public Collection<Room> getAllRooms() {
        return rooms;
    }


    public Reservation reserveARoom(Customer customer, Room room, Date checkInDate, Date checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        List<Reservation> reservationList = roomReservations.get(room.getRoomNumber());

        if (reservationList == null) {
            List<Reservation> newReservationList = new ArrayList<>();
            newReservationList.add(reservation);
            roomReservations.put(room.getRoomNumber(), newReservationList);
            System.out.println("You have booked room " + room.getRoomNumber() + " successfully");
            return reservation;
        }

        int collision = 0;
        for (Reservation reservation1 : reservationList) {
            if (reservation.collidesWith(reservation1.getCheckInDate(), reservation1.getCheckOutDate())) {
                collision++;
                System.out.println("This reservation is not possible");

                System.out.println("Available rooms for you as follows: ");
                Collection<Room> availableRooms = findAvailableRooms(checkInDate, checkOutDate);
                int counter = 1;
                for (Room availableRoom : availableRooms
                ) {
                    System.out.println(counter + ": " + availableRoom.getRoomNumber());
                    counter++;
                }
                break;
            }
        }

        if (collision == 0) {
            // add reservation into reservation list
            reservationList.add(reservation);
            System.out.println("You have booked room " + room.getRoomNumber() + " successfully");
            //add new reservation list into reservationS list
            roomReservations.put(room.getRoomNumber(), reservationList);
        } else {
            System.out.println(
                    "Please select only from available room list."
            );
        }
        return reservation;
    }

    public List<Room> findAvailableRooms(Date checkInDate, Date checkOutDate) {
        List<Room> availableRoomList = new ArrayList<>();
        List<Room> allRooms = (List<Room>) AdminResource.adminResource.getAllRooms();

        for (Room room : allRooms
        ) {
            boolean isRoomEverReserved = false;
            for (Map.Entry<String, List<Reservation>> roomReservationsEntry : roomReservations.entrySet()) {
                // each entry (reservation list per room)
                // values(reservations) into the list
                List<Reservation> reservationsPerRoom = roomReservationsEntry.getValue();
                // for each entry (reservation list per room) checkin dates is not within the range of an existing reservation

                if (roomReservationsEntry.getKey() == room.getRoomNumber()) {
                    isRoomEverReserved = true;
                    int collision = 0;
                    for (Reservation reservation : reservationsPerRoom
                    ) {
                        if (reservation.collidesWith(checkInDate, checkOutDate)) {
                            collision++;
                        }
                    }
                    if (collision == 0) {
                        availableRoomList.add(room);
                    }
                }

            }
            if (!isRoomEverReserved) {
                availableRoomList.add(room);
            }
        }
        return availableRoomList;
    }

    public Collection<Reservation> getCustomersReservation(String customerEmail) {
        List<Reservation> reservationsForCustomer = new ArrayList<>();
        for (Map.Entry<String, List<Reservation>> roomReservationsEntry : roomReservations.entrySet()) {
            List<Reservation> reservationsPerRoom = roomReservationsEntry.getValue();
            for (Reservation reservation : reservationsPerRoom
            ) {
                if (customerEmail.equals(reservation.getCustomer().getEmail())) {
                    reservationsForCustomer.add(reservation);
                }
            }
        }
        return reservationsForCustomer;
    }

    public void printAllReservation() {
        for (Map.Entry<String, List<Reservation>> roomReservationsEntry : roomReservations.entrySet()) {
            List<Reservation> reservationsPerRoom = roomReservationsEntry.getValue();
            for (Reservation reservation : reservationsPerRoom
            ) {
                System.out.println("Customer email: " + reservation.getCustomer().getEmail()
                        + "Customer name: " + reservation.getCustomer().getFirstName()
                        + "Customer last name: " + reservation.getCustomer().getLastName()
                        + "room number: " + reservation.getRoom()
                        + "Check in date: " + reservation.getCheckInDate()
                        + "Check out date: " + reservation.getCheckOutDate());
            }
        }
    }
}

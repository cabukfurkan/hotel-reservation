package api;


import model.Customer;
import model.Reservation;
import model.Room;
import service.CustomerService;
import service.ReservationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class HotelResource {

    public static HotelResource hotelResource = new HotelResource();


    public Customer getCustomer(String email) {
        return CustomerService.customerService.getCustomer(email);
    }

    public void createACustomer(String email, String firstName, String lastName) {
        CustomerService.customerService.addCustomer(email, firstName, lastName);
    }

    public Room getRoom(String roomNumber) {
        return ReservationService.reservationService.getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, Room room, Date checkInDate, Date checkOutDate) {
        return ReservationService.reservationService.reserveARoom(CustomerService.customerService.getCustomer(customerEmail), room, checkInDate, checkOutDate);
    }

    public Collection<Reservation> getCustomersReservations(String customerEmail) {
        return ReservationService.reservationService.getCustomersReservation(customerEmail);
    }

    public Room findARoom(Date checkIn, Date checkOut) {
        List<Room> availableRooms = new ArrayList<>();
        availableRooms = (List<Room>) ReservationService.reservationService.findAvailableRooms(checkIn, checkOut);
        return availableRooms.get(0);
    }

}

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
    private static HotelResource hotelResource = null;

    private HotelResource(){}

    public static HotelResource getInstance(){
        if(hotelResource== null){
            hotelResource = new HotelResource();
        }

        return hotelResource;
    }

    ReservationService reservationService = ReservationService.getInstance();
    CustomerService customerService = CustomerService.getInstance();

    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public void createACustomer(String email, String firstName, String lastName) {
        customerService.addCustomer(email, firstName, lastName);
    }

    public Room getRoom(String roomNumber) {
        return reservationService.getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, Room room, Date checkInDate, Date checkOutDate) {
        return reservationService.reserveARoom(customerService.getCustomer(customerEmail), room, checkInDate, checkOutDate);
    }

    public Collection<Reservation> getCustomersReservations(String customerEmail) {
        return reservationService.getCustomersReservation(customerEmail);
    }

    public Room findARoom(Date checkIn, Date checkOut) {
        List<Room> availableRooms = new ArrayList<>();
        availableRooms = (List<Room>) reservationService.findAvailableRooms(checkIn, checkOut);
        return availableRooms.get(0);
    }

}

package api;

import model.Customer;
import model.IRoom;
import model.Room;
import service.CustomerService;
import service.ReservationService;

import java.util.*;

public class AdminResource {

    public static final AdminResource adminResource = new AdminResource();

    public Customer getCustomer(String email){
        return CustomerService.customerService.getCustomer(email);
    }

    public void addRoom(Room room){
        ReservationService.reservationService.addRoom(room);
    }
    public Collection<Room> getAllRooms(){

        Collection<Room> rooms = new ArrayList<>();
        rooms = ReservationService.reservationService.getAllRooms();

        for (Room room: rooms) {
            System.out.println(room);
        }
        return rooms ;
    }
    public Collection<Customer> getAllCustomers(){
        Collection<Customer> customers = new ArrayList<>();
        customers = CustomerService.customerService.getAllCustomers();

        for (Customer customer:customers
             ) {
            System.out.println("customer = " + customer);
        }
        return customers;
    }
    public void displayAllReservations(){
        ReservationService.reservationService.printAllReservation();
    }
}

package api;

import model.Customer;
import model.IRoom;
import model.Room;
import service.CustomerService;
import service.ReservationService;

import java.util.*;

public class AdminResource {

    private static AdminResource adminResource = null;

    private  AdminResource(){}

    public static AdminResource getInstance(){
        if(adminResource== null){
            adminResource = new AdminResource();
        }
        return adminResource;
    }

    ReservationService reservationService = ReservationService.getInstance();
    CustomerService customerService = CustomerService.getInstance();

    public Customer getCustomer(String email){
        return customerService.getCustomer(email);
    }

    public void addRoom(Room room){
        reservationService.addRoom(room);
    }
    public Collection<Room> getAllRooms(){

        Collection<Room> rooms = new ArrayList<>();
        rooms = reservationService.getAllRooms();
        return rooms ;
    }
    public Collection<Customer> getAllCustomers(){
        Collection<Customer> customers = new ArrayList<>();
        customers = customerService.getAllCustomers();

        for (Customer customer:customers
             ) {
            System.out.println("customer = " + customer);
        }
        return customers;
    }
    public void displayAllReservations(){
        reservationService.printAllReservation();
    }
}

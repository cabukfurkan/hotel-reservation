import api.AdminResource;
import model.Reservation;
import model.Room;
import model.RoomType;
import service.CustomerService;
import service.ReservationService;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Tester {

    public static void main(String[] args) {

        boolean keepRunning = true;
        try (Scanner scanner = new Scanner(System.in)) {

            while (keepRunning) {
                try {
                    System.out.println("1- Find and reserve a room");
                    System.out.println("2- See my reservations");
                    System.out.println("3- Create an account");
                    System.out.println("4- Admin");
                    System.out.println("5- exit");
                    System.out.println("=====================================");
                    System.out.println("please select a NUMBER from the menu");
                    int selection = Integer.parseInt(scanner.nextLine());

                    if (selection == 1) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

                        String today = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

                        System.out.println("Enter check in date as dd-mm-yyyy from " + today);
                        String checkInDateString = scanner.nextLine();
                        Date checkInDate = formatter.parse(checkInDateString);


                        if (ReservationService.reservationService.isDateEarlier(checkInDate)) {
                            System.out.println("Check in date cannot be in the past");
                        } else {
                            System.out.println("Enter check out date dd-MM-yyyy example 03-01-2022");
                            String checkOutDateString = scanner.nextLine();
                            Date checkOutDate = formatter.parse(checkOutDateString);

                            long timeDifference = checkOutDate.getTime() - checkInDate.getTime();
                            long numberOfDays = TimeUnit.MILLISECONDS.toDays(timeDifference);

                            List<Room> availableRooms = ReservationService.reservationService.findAvailableRooms(checkInDate, checkOutDate);

                            if (availableRooms == null || availableRooms.isEmpty()) {
                                System.out.println("There is no available room at the moment");
                            } else {
                                System.out.println("Available rooms for you as follows: ");
                                System.out.println("=================================== ");
                                for (Room room : availableRooms
                                ) {
                                    System.out.println("room: " + room.getRoomNumber() + " "
                                            + room.getRoomType() + " bed" + " "
                                            + room.getRoomPrice() * numberOfDays + " "
                                            + " for " + numberOfDays + " days");
                                }

                                System.out.println("Would you like to book one of these room y/n");
                                String choice = scanner.nextLine();
                                if (choice.equals("y")) {
                                    System.out.println("Do you have account with us y/n");
                                    choice = scanner.nextLine();
                                    if (choice.equals("y")) {
                                        System.out.println("enter email format name@domain.com");
                                        String email = scanner.nextLine();
                                        if (CustomerService.customerService.getCustomer(email) == null) {
                                            System.out.println("You don't have account with this email address");

                                            System.out.println("Do you want to make an account y/n?");
                                            choice = scanner.nextLine();

                                            if (choice.equals("y")) {
                                                System.out.println("name: ");
                                                String firstName = scanner.nextLine();
                                                System.out.println("lastname: ");
                                                String lastName = scanner.nextLine();
                                                System.out.println("email: ");
                                                email = scanner.nextLine();
                                                CustomerService.customerService.addCustomer(email, firstName, lastName);

                                                System.out.println("enter the room number you would you like to reserve");
                                                String roomNumber = scanner.nextLine();
                                                ReservationService.reservationService.reserveARoom(CustomerService.customerService.getCustomer(email), ReservationService.reservationService.getARoom(roomNumber), checkInDate, checkOutDate);

                                            } else {
                                                System.out.println("returning to main menu..");
                                                System.out.println("===========================");
                                            }

                                        } else {
                                            System.out.println("what room would you like to reserve");
                                            String roomNumber = scanner.nextLine();

                                            ReservationService.reservationService.reserveARoom(CustomerService.customerService.getCustomer(email), ReservationService.reservationService.getARoom(roomNumber), checkInDate, checkOutDate);
                                        }

                                    } else {
//                                make an account
                                        System.out.println("name: ");
                                        String firstName = scanner.nextLine();
                                        System.out.println("lastname: ");
                                        String lastName = scanner.nextLine();
                                        System.out.println("email: ");
                                        String email = scanner.nextLine();
                                        CustomerService.customerService.addCustomer(email, firstName, lastName);

                                        System.out.println("enter the room number you would you like to reserve");
                                        String roomNumber = scanner.nextLine();
                                        ReservationService.reservationService.reserveARoom(CustomerService.customerService.getCustomer(email), ReservationService.reservationService.getARoom(roomNumber), checkInDate, checkOutDate);
                                    }
                                } else {
                                    System.out.println("1- Find and reserve a room");
                                    System.out.println("2- See my reservations");
                                    System.out.println("3- Create an account");
                                    System.out.println("4- Admin");
                                    System.out.println("5- Exit");
                                    System.out.println("=====================================");
                                    System.out.println("please select a NUMBER from the menu");
                                }
                            }
                        }


                    } else if (selection == 2) {
                        System.out.println("what is your email: ");
                        String email = scanner.nextLine();

                        List<Reservation> customerReservations = (List<Reservation>) ReservationService.reservationService.getCustomersReservation(email);

                        if (customerReservations == null || customerReservations.isEmpty()) {
                            System.out.println("You haven't got any reservations please make a reservation from the menu");
                            System.out.println("---------------------------");
                        }
                        for (Reservation reservation : customerReservations
                        ) {
                            System.out.println("Room number: " + reservation.getRoom().getRoomNumber() + " from " + reservation.getCheckInDate() + " to " + reservation.getCheckOutDate());
                        }
                    } else if (selection == 3) {
                        System.out.println("name: ");
                        String firstName = scanner.nextLine();
                        System.out.println("lastname: ");
                        String lastName = scanner.nextLine();
                        System.out.println("email: ");
                        String email = scanner.nextLine();
                        CustomerService.customerService.addCustomer(email, firstName, lastName);

                    } else if (selection == 4) {
                        System.out.println("1-See all Customers");
                        System.out.println("2-See all Rooms");
                        System.out.println("3-See all Reservations");
                        System.out.println("4-Add a Room");
                        System.out.println("5-Back to Main Menu");
                        System.out.println("=====================================");
                        System.out.println("please select a NUMBER from the menu");
                        selection = Integer.parseInt(scanner.nextLine());

                        if (selection == 1) {
                            AdminResource.adminResource.getAllCustomers();
                        }
                        if (selection == 2) {
                            List<Room> allRooms = (List<Room>) AdminResource.adminResource.getAllRooms();

                            for (Room room : allRooms
                            ) {
                                System.out.println("room number: " + room.getRoomNumber() + " " +
                                        "room type: " + room.getRoomType() + " " +
                                        "room price: " + room.getRoomPrice());
                            }
                        }
                        if (selection == 3) {
                            AdminResource.adminResource.displayAllReservations();
                        }
                        if (selection == 4) {
                            System.out.println("room number: ");
                            String roomNumber = scanner.nextLine();
                            System.out.println("price:");
                            Double price = Double.parseDouble(scanner.nextLine());
                            System.out.println("1 for single, 2 for double");
                            int roomTypeSelection = Integer.parseInt(scanner.nextLine());
                            RoomType roomType;
                            if (roomTypeSelection == 1) {
                                roomType = RoomType.SINGLE;
                            } else {
                                roomType = RoomType.DOUBLE;
                            }
                            Room newRoom = new Room(roomNumber, price, roomType);
                            AdminResource.adminResource.addRoom(newRoom);
                        }
                    } else if (selection == 5) {
                        keepRunning = false;
                    } else {
                        System.out.println("Please enter a valid number from the menu");
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getLocalizedMessage());
                }
            }
        }
    }
}
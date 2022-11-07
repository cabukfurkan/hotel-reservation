import api.AdminMenu;
import api.AdminResource;
import api.MainMenu;
import model.Reservation;
import model.Room;
import model.RoomType;
import service.CustomerService;
import service.ReservationService;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class HotelReservation {

    public static void main(String[] args) {
        System.out.println("Initializing hotel rooms...");
        Room room1 = new Room("1", 40.0, RoomType.SINGLE);
        ReservationService.reservationService.addRoom(room1);
        System.out.println("==============================");

        boolean keepRunning = true;
        try (Scanner scanner = new Scanner(System.in)) {

            while (keepRunning) {
                try {
                    MainMenu.mainMenu.showMenu();
                    int selection = Integer.parseInt(scanner.nextLine());

                    switch (selection) {
                        case 1 -> {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

                            String today = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

                            System.out.println("Enter check in date as dd-mm-yyyy FROM " + today);
                            String checkInDateString = scanner.nextLine();
                            Date checkInDate = formatter.parse(checkInDateString);


                            if (ReservationService.reservationService.isDateEarlier(checkInDate)) {
                                System.out.println("Check in date cannot be in the past!");
                            } else {
                                long checkInMs = checkInDate.getTime();
                                checkInMs += 24 * 60 * 60 * 1000;
                                Date oneDayLater = new Date(checkInMs);
                                String oneDayLaterStr = oneDayLater.toString();
                                oneDayLaterStr = ReservationService.reservationService.convertDate(oneDayLaterStr);

                                System.out.println("Enter check out date dd-MM-yyyy FROM " + oneDayLaterStr);
                                String checkOutDateString = scanner.nextLine();
                                Date checkOutDate = formatter.parse(checkOutDateString);

                                long timeDifference = checkOutDate.getTime() - checkInDate.getTime();
                                long numberOfDays = TimeUnit.MILLISECONDS.toDays(timeDifference);

                                if (ReservationService.reservationService.isCheckoutDateCollides(checkInDate,checkOutDate)){
                                    System.out.println("Checkout date cannot be equal to or before than checkin date");
                                    break;
                                }

                                List<Room> availableRooms = ReservationService.reservationService.findAvailableRooms(checkInDate, checkOutDate);

                                if (availableRooms == null || availableRooms.isEmpty()) {
                                    System.out.println("There is no available room at the moment");
                                    List<Room> recommendedRooms = ReservationService.reservationService.recommendedRoomsForNextWeek(checkInDate, checkOutDate);
                                    if (recommendedRooms == null || recommendedRooms.isEmpty()) {
                                        System.out.println("There is also no recommended room for the next week");
                                        System.out.println("Please try another date ");
                                    } else {
                                        System.out.println("=================================== ");
                                        for (Room room : recommendedRooms
                                        ) {
                                            System.out.println("room: " + room.getRoomNumber() + " "
                                                    + room.getRoomType() + " bed" + " "
                                                    + room.getRoomPrice() * numberOfDays + " "
                                                    + " for " + numberOfDays + " days");
                                        }
                                        System.out.println("========================");
                                        System.out.println("Would you like to book one of these for next week?");
                                        System.out.println("(y) for yes (n) for no");
                                        String choice = scanner.nextLine();
                                        if (choice.equals("y")) {
                                            System.out.println("Do you have account with us?");
                                            System.out.println("(y) for yes (n) for no");
                                            choice = scanner.nextLine();
                                            if (choice.equals("y")) {
                                                System.out.println("enter email ex. name@domain.com");
                                                String email = scanner.nextLine();
                                                if (CustomerService.customerService.getCustomer(email) == null) {
                                                    System.out.println("You don't have account with this email address");

                                                    System.out.println("Do you want to make an account?");
                                                    System.out.println("(y) for yes (n) for no");
                                                    choice = scanner.nextLine();

                                                    if (choice.equals("y")) {
                                                        System.out.println("name: ");
                                                        String firstName = scanner.nextLine();
                                                        System.out.println("lastname: ");
                                                        String lastName = scanner.nextLine();
                                                        System.out.println("email: ");
                                                        email = scanner.nextLine();
                                                        CustomerService.customerService.addCustomer(email, firstName, lastName);

                                                        System.out.println("enter the ROOM NUMBER you would you like to reserve");
                                                        String roomNumber = scanner.nextLine();

                                                        ReservationService.reservationService.reserveARoom(CustomerService.customerService.getCustomer(email), ReservationService.reservationService.getARoom(roomNumber), ReservationService.reservationService.getCheckIn7DaysLater(), ReservationService.reservationService.getCheckOut7DaysLater());

                                                    } else {
                                                        System.out.println("returning to main menu..");
                                                        System.out.println("===========================");
                                                    }

                                                } else {
                                                    System.out.println("enter the ROOM NUMBER you would you like to reserve");
                                                    String roomNumber = scanner.nextLine();

                                                    ReservationService.reservationService.reserveARoom(CustomerService.customerService.getCustomer(email), ReservationService.reservationService.getARoom(roomNumber), ReservationService.reservationService.getCheckIn7DaysLater(), ReservationService.reservationService.getCheckOut7DaysLater());
                                                }

                                            } else {
                                                System.out.println("name: ");
                                                String firstName = scanner.nextLine();
                                                System.out.println("lastname: ");
                                                String lastName = scanner.nextLine();
                                                System.out.println("email: ");
                                                String email = scanner.nextLine();
                                                CustomerService.customerService.addCustomer(email, firstName, lastName);

                                                System.out.println("enter the ROOM NUMBER you would you like to reserve");
                                                String roomNumber = scanner.nextLine();
                                                ReservationService.reservationService.reserveARoom(CustomerService.customerService.getCustomer(email), ReservationService.reservationService.getARoom(roomNumber), ReservationService.reservationService.getCheckIn7DaysLater(), ReservationService.reservationService.getCheckOut7DaysLater());
                                            }
                                        }
                                    }
                                } else {
                                    System.out.println("Available rooms for you: ");
                                    System.out.println("=================================== ");
                                    for (Room room : availableRooms
                                    ) {
                                        System.out.println("room: " + room.getRoomNumber() + " "
                                                + room.getRoomType() + " bed" + " "
                                                + room.getRoomPrice() * numberOfDays + " "
                                                + " for " + numberOfDays + " days");
                                    }
                                    System.out.println("========================================");
                                    System.out.println("Would you like to book one of these room?");
                                    System.out.println("(y) for yes (n) for no");
                                    String choice = scanner.nextLine();
                                    if (choice.equals("y")) {
                                        System.out.println("Do you have an account with us?");
                                        System.out.println("(y) for yes (n) for no");
                                        choice = scanner.nextLine();
                                        if (choice.equals("y")) {
                                            System.out.println("enter email format name@domain.com");
                                            String email = scanner.nextLine();
                                            if (CustomerService.customerService.getCustomer(email) == null) {
                                                System.out.println("You don't have an account with this email address");

                                                System.out.println("Do you want to make an account?");
                                                System.out.println("(y) for yes (n) for no");
                                                choice = scanner.nextLine();

                                                if (choice.equals("y")) {
                                                    System.out.println("name: ");
                                                    String firstName = scanner.nextLine();
                                                    System.out.println("lastname: ");
                                                    String lastName = scanner.nextLine();
                                                    System.out.println("email: ");
                                                    email = scanner.nextLine();
                                                    CustomerService.customerService.addCustomer(email, firstName, lastName);

                                                    System.out.println("enter the ROOM NUMBER you would you like to reserve");
                                                    String roomNumber = scanner.nextLine();
                                                    ReservationService.reservationService.reserveARoom(CustomerService.customerService.getCustomer(email), ReservationService.reservationService.getARoom(roomNumber), checkInDate, checkOutDate);

                                                } else {
                                                    System.out.println("returning to main menu..");
                                                    System.out.println("===========================");
                                                }

                                            } else {
                                                System.out.println("enter the ROOM NUMBER you would you like to reserve");
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

                                            System.out.println("enter the ROOM NUMBER you would you like to reserve");
                                            String roomNumber = scanner.nextLine();
                                            ReservationService.reservationService.reserveARoom(CustomerService.customerService.getCustomer(email), ReservationService.reservationService.getARoom(roomNumber), checkInDate, checkOutDate);
                                        }
                                    } else {
                                        MainMenu.mainMenu.showMenu();
                                    }
                                }
                            }
                        }
                        case 2 -> {
                            System.out.println("what is your email: ");
                            String email = scanner.nextLine();

                            List<Reservation> customerReservations = (List<Reservation>) ReservationService.reservationService.getCustomersReservation(email);

                            if (customerReservations == null || customerReservations.isEmpty()) {
                                System.out.println("You haven't got any reservations.");
                                System.out.println("please make a reservation from the menu.");
                                System.out.println("---------------------------");
                            } else {
                                for (Reservation reservation : customerReservations
                                ) {
                                    System.out.println("Room number: " + reservation.getRoom().getRoomNumber() + " from " + ReservationService.reservationService.convertDate(reservation.getCheckInDate().toString())  + " to " + ReservationService.reservationService.convertDate(reservation.getCheckOutDate().toString()));
                                }
                            }

                        }
                        case 3 -> {
                            System.out.println("name: ");
                            String firstName = scanner.nextLine();
                            System.out.println("lastname: ");
                            String lastName = scanner.nextLine();
                            System.out.println("email: ");
                            String email = scanner.nextLine();
                            CustomerService.customerService.addCustomer(email, firstName, lastName);

                        }
                        case 4 -> {
                            AdminMenu.adminMenu.showAdminMenu();
                            selection = Integer.parseInt(scanner.nextLine());

                            switch (selection) {
                                case 1 -> AdminResource.adminResource.getAllCustomers();

                                case 2 -> {
                                    List<Room> allRooms = (List<Room>) AdminResource.adminResource.getAllRooms();

                                    for (Room room : allRooms
                                    ) {
                                        System.out.println("room number: " + room.getRoomNumber() + " " +
                                                "room type: " + room.getRoomType() + " " +
                                                "room price: " + room.getRoomPrice());
                                    }
                                    System.out.println("============================");
                                }
                                case 3 ->
                                    AdminResource.adminResource.displayAllReservations();

                                case 4 -> {
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
                                default -> {
                                    System.out.println("returning to main menu");
                                    System.out.println("======================");

                                }
                            }
                        }
                        case 5 ->
                            keepRunning = false;

                        default ->
                            System.out.println("Please enter a valid number from the menu");

                    }
                } catch (Exception ex) {
                    System.out.println(ex.getLocalizedMessage());
                }
            }
        }
    }
}
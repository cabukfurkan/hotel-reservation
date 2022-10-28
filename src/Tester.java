import api.AdminMenu;
import api.AdminResource;
import api.MainMenu;
import model.IRoom;
import model.Room;
import model.RoomType;
import service.CustomerService;
import service.ReservationService;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Tester {

    public static void main(String[] args) {
        MainMenu mainMenu = new MainMenu();
        AdminMenu adminMenu = new AdminMenu();
        boolean keepRunning = true;
        try (Scanner scanner = new Scanner(System.in)) {

            while (keepRunning) {
                try {
                    System.out.println("1-" + mainMenu.getOption1());
                    System.out.println("2-" + mainMenu.getOption2());
                    System.out.println("3-" + mainMenu.getOption3());
                    System.out.println("4-" + mainMenu.getOption4());
                    System.out.println("5-" + mainMenu.getOption5());
                    System.out.println("=====================================");
                    System.out.println("please select a NUMBER from the menu");
                    int selection = Integer.parseInt(scanner.nextLine());

                    if (selection == 1) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
//                        check in date
                        System.out.println("Enter check in date dd-MM-yyyy example 02-01-2022");
                        String checkInDateString = scanner.nextLine();
                        Date checkInDate = formatter.parse(checkInDateString);
//                        checkout date
                        System.out.println("Enter check out date dd-MM-yyyy example 03-01-2022");
                        String checkOutDateString = scanner.nextLine();
                        Date checkOutDate = formatter.parse(checkOutDateString);
//                        difference bw two date
                        long timeDifference = checkOutDate.getTime() - checkInDate.getTime();
                        long numberOfDays = TimeUnit.MILLISECONDS.toDays(timeDifference);

                        Collection<Room> allRooms = new ArrayList<>();
                        allRooms = AdminResource.adminResource.getAllRooms();
                        ArrayList<Room> availableRooms = new ArrayList<>();

                        for (Room room : allRooms
                        ) {
                            if (!room.getIsReserved()) {
                                availableRooms.add(room);
                            }
                        }
                        if (availableRooms.isEmpty()) {
                            System.out.println("There is no empty room");
                        } else {
                            for (Room room : availableRooms
                            ) {
                                System.out.println("room" + room.getRoomNumber()
                                        + room.getRoomType() + " bed"
                                        + room.getRoomPrice() * numberOfDays
                                        + " for" + numberOfDays + " days");
                            }

                            System.out.println("Would you like to book this room y/n");
                            String choice = scanner.nextLine();
                            if (choice == "y") {
                                System.out.println("Do you have account with us y/n");
                                choice = scanner.nextLine();
                                if (choice == "y") {
                                    System.out.println("enter email format name@domain.com");
                                    String email = scanner.nextLine();

                                    System.out.println("what room would you like to reserve");
                                    String roomNumber = scanner.nextLine();

                                    ReservationService.reservationService.reserveARoom(CustomerService.customerService.getCustomer(email), ReservationService.reservationService.getARoom(roomNumber),checkInDate,checkOutDate );
                                    ReservationService.reservationService.getARoom(roomNumber).setIsReserved(true);
                                }
                            } else {
                                System.out.println("1-" + mainMenu.getOption1());
                                System.out.println("2-" + mainMenu.getOption2());
                                System.out.println("3-" + mainMenu.getOption3());
                                System.out.println("4-" + mainMenu.getOption4());
                                System.out.println("5-" + mainMenu.getOption5());
                                System.out.println("=====================================");
                                System.out.println("please select a NUMBER from the menu");
                                selection = Integer.parseInt(scanner.nextLine());
                            }
                        }

                    } else if (selection == 2) {

                    } else if (selection == 3) {
                        System.out.println("name: ");
                        String firstName = scanner.nextLine();
                        System.out.println("lastname: ");
                        String lastName = scanner.nextLine();
                        System.out.println("email: ");
                        String email = scanner.nextLine();
                        CustomerService.customerService.addCustomer(email, firstName, lastName);

                    } else if (selection == 4) {
                        System.out.println("1-" + adminMenu.getOption1());
                        System.out.println("2-" + adminMenu.getOption2());
                        System.out.println("3-" + adminMenu.getOption3());
                        System.out.println("4-" + adminMenu.getOption4());
                        System.out.println("5-" + adminMenu.getOption5());
                        System.out.println("=====================================");
                        System.out.println("please select a NUMBER from the menu");
                        selection = Integer.parseInt(scanner.nextLine());

                        if (selection == 1) {
                            AdminResource.adminResource.getAllCustomers();
                        }
                        if (selection == 2) {
                            AdminResource.adminResource.getAllRooms();
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
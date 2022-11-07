package service;

import api.AdminResource;
import model.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class ReservationService {
    public static ReservationService reservationService = new ReservationService();
    Collection<Room> rooms = new ArrayList<>();
    Map<String, List<Reservation>> roomReservations = new HashMap<>();

    private Date checkIn7DaysLater;
    private Date checkOut7DaysLater;

    public Date getCheckIn7DaysLater() {
        return checkIn7DaysLater;
    }

    public Date getCheckOut7DaysLater() {
        return checkOut7DaysLater;
    }

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


    public boolean isDateEarlier(Date checkInDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkInDate);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 9999);
        Date checkInDateWHours = calendar.getTime();
        Date currentTime = new Date();
        if (currentTime.after(checkInDateWHours)) {
            return true;
        }
        return false;
    }

    public Reservation reserveARoom(Customer customer, Room room, Date checkInDate, Date checkOutDate) {

        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);

        List<Reservation> reservationList = roomReservations.get(room.getRoomNumber());

        if (rooms.isEmpty()) {
            System.out.println("Hotel has no room at the moment please wait for admin");
            return reservation;
        }

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
                System.out.println("=================================== ");
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

                if (Objects.equals(roomReservationsEntry.getKey(), room.getRoomNumber())) {
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

    Date add7days(Date date) {
        long dateInMs = date.getTime();
        dateInMs += 7 * 24 * 60 * 60 * 1000;
        Date sevenDaysLater = new Date(dateInMs);

        return sevenDaysLater;
    }

    public Date add1day(Date date) {
        long dateInMs = date.getTime();
        dateInMs += 1 * 24 * 60 * 60 * 1000;
        Date oneDayLater = new Date(dateInMs);

        return oneDayLater;
    }

    public List<Room> recommendedRoomsForNextWeek(Date checkInDate, Date checkOutDate) {
        Date checkInPlus7Days = add7days(checkInDate);
        String checkInPlus7DaysStr = convertDate(checkInPlus7Days.toString());
        this.checkIn7DaysLater = checkInPlus7Days;
        Date checkOutPlus7Days = add7days(checkOutDate);
        String checkOutPlus7DaysStr = convertDate(checkOutPlus7Days.toString());
        this.checkOut7DaysLater = checkOutPlus7Days;

        List<Room> recommendedRoomList = findAvailableRooms(checkInPlus7Days, checkOutPlus7Days);
        System.out.println("Recommended rooms from " + checkInPlus7DaysStr + " to " + checkOutPlus7DaysStr);

        return recommendedRoomList;
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
                System.out.println("email: " + reservation.getCustomer().getEmail()+" "
                        + "name: " + reservation.getCustomer().getFirstName()+" "
                        + "last name: " + reservation.getCustomer().getLastName()+" "
                        + "room number: " + reservation.getRoom()+" "
                        + "Checkin date: " + convertDate(reservation.getCheckInDate().toString())+"-"
                        + "Checkout date: " + convertDate(reservation.getCheckOutDate().toString()));
            }
        }
    }

    public String convertDate(String dateToBeConverted) {
        String day = dateToBeConverted.substring(8, 10);
        String year = dateToBeConverted.substring(24);
        String month = dateToBeConverted.substring(4, 7);
        switch (month) {

            case "Jan":
                month = "01";
                break;
            case "Feb":
                month = "02";
                break;
            case "Mar":
                month = "03";
                break;
            case "Apr":
                month = "04";
                break;
            case "May":
                month = "05";
                break;
            case "Jun":
                month = "06";
                break;
            case "Jul":
                month = "07";
                break;
            case "Aug":
                month = "08";
                break;
            case "Sep":
                month = "09";
                break;
            case "Oct":
                month = "10";
                break;
            case "Nov":
                month = "11";
                break;
            case "Dec":
                month = "12";
                break;
            default:
                System.out.println("Not a month");
        }
        ;

        String convertedDate = day + "-" + month + "-" + year;
        return convertedDate;
    }
}

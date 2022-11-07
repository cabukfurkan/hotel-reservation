package api;

public class AdminMenu {

    public static AdminMenu adminMenu = new AdminMenu();

    public void showAdminMenu (){
        System.out.println("***** ADMIN MENU *****");
        System.out.println("==========================");
        System.out.println("1-See all Customers");
        System.out.println("2-See all Rooms");
        System.out.println("3-See all Reservations");
        System.out.println("4-Add a Room");
        System.out.println("5-Back to Main Menu");
        System.out.println("=====================================");
        System.out.println("please select a NUMBER from the menu");
    }
}

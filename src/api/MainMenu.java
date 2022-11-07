package api;

public class MainMenu {

    public static MainMenu mainMenu = new MainMenu();


    public void showMenu(){
        System.out.println("***** MAIN MENU *****");
        System.out.println("==========================");
        System.out.println("1- Find and reserve a room");
        System.out.println("2- See my reservations");
        System.out.println("3- Create an account");
        System.out.println("4- Admin");
        System.out.println("5- exit");
        System.out.println("=====================================");
        System.out.println("please select a NUMBER from the menu");
    }
}

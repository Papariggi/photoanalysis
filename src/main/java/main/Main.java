package main;


import menu.ConsoleMenu;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        try(Scanner sc = new Scanner(System.in)) {
            ConsoleMenu.menu(sc);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}

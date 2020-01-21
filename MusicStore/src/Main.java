import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    static WestminsterMusicStoreManager manager=new WestminsterMusicStoreManager();  //Constructor from westminster music store manager classs
    static  Scanner input=new Scanner(System.in);

    public static void main(String[] args){

//        MusicItem [] items=new MusicItem[50];



        MusicItem item = null;
        //List<MusicItem> storeItem = new ArrayList<MusicItem>();
       // List<MusicItem> boughtItem = new ArrayList<MusicItem>();
        //SortCategory c = null;

        System.out.println("=============Welcome to Westminster Music Store=============");
        int option=0; int addOption=0;
        while(option!=8) {
            option=option();
            switch (option) {
                case 1:
                    manager.add(item);  //calling add option from westminster class
                    break;
                case 2:
                    manager.delete(item); //calling delete option from westminster class
                    break;
                case 3:
                    manager.printList();  //calling printList option from westminster class
                    break;
                case 4:
                    manager.search();   //calling search option from westminster class
                    break;
                case 5:
                    manager.sort();   //calling search option fom westminster class
                    break;
                case 6:
                    manager.buy(item);  //calling buy option from westminster class
                    break;
                case 7:
                    manager.generateReport();  //calling generate option from westminster class
                    break;

            }

        }
        System.out.println("===================END==================");
    }

//Method to printing option menu
    public static int option(){
        System.out.println("Select Your Option \n" +
                "01) Add new item \n" +
                "02) Delete item \n" +
                "03) Print the list of items \n" +
                "04) Search Items\n"+
                "05) Sort the list \n" +
                "06) Buy items \n" +
                "07) Get Sales Report\n"+
                "08) Exit\n"
               );

        System.out.print("Input Your Option\t:\t");
        exceptionHandling();               //calling exception handling method
        int option=input.nextInt();
        option = invalidOption(option);
        System.out.println("---------------------------");

        return option;
    }

    //Exception handling method
    public static void exceptionHandling() {
        String message="You have entered an Invalid option\n" +
                "Input Your Option\t:\t";
        while (!input.hasNextInt()) {
            System.out.print(message);
            input.next();
        }
    }

//Method to check whether option is valid (1<=option<=8)
    public static int invalidOption(int option) {
        while (!(1 <= option && option <= 8)) {
            System.out.print("You have entered an Invalid option\n" +
                    "Input Your Option\t:\t");
            exceptionHandling();
            option = input.nextInt();
        }
        return option;
    }






}

import ConnectDB.ConnectionClass;
import javafx.application.Application;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class WestminsterMusicStoreManager implements  StoreManager {


//    Sort



    //connect to the database
    ConnectionClass connectionClass=new ConnectionClass();
    Connection connection=connectionClass.getConnection();

    private List<MusicItem> storeItem = new ArrayList<MusicItem>();
    private static final int MAX_COUNT = 1000;
    //private List<MusicItem> boughtItem = new ArrayList<MusicItem>();
    static Scanner input = new Scanner(System.in);
    //private  Map<MusicItem, LocalDateTime> salesDate=new HashMap<>();
    FileWriter fw;


////////////////////////////////////////Add option////////////////////////////////////////////////////

    public void Sort(){
        Collections.sort(storeItem,new IDComparator());
    }


    @Override
    public void notUSe() {
        throw  new UnsupportedOperationException("This is Unsupported method");
    }

    @Override
    public void add(MusicItem item) {

        if (totalCount()< MAX_COUNT) {    //check total count is less than 1000(Max item no is 1000)
            int addOption=0;
            while (addOption!=3){
                addOption=addOption();

                switch (addOption){
                    case 1:
                        addCD(item);     //calling addCd method
                        break;

                    case 2:
                        addVinyl(item);    //calling addVinyl method
                        break;
                }
            }

            System.out.println("--Exit From the Add Item Option--");

        } else {
            System.out.println("--!....Store is Full....!--");   //if there is  1000 items display this

        }
        System.out.println("\n-------------------------");
    }

    /////////////////////////////////Delete item method///////////////////////////////////////
    @Override
    public void delete(MusicItem item) {

//////////////////////////////////////////// Gugsi Sir ///////////////////////////////////////


        if (totalCount() > 0) {                 //Check if there are Items in the Store

            System.out.print("Input ID of the Music Item to delete : ");     //Input id of the Music item
            String idDelete = input.next();
            System.out.println();
            try {
                //check whether the given id is existing in cd table
                Statement statement = connection.createStatement();
                String sql = "SELECT * FROM TblCD WHERE CDID= '" + idDelete + "'";
                ResultSet resultSet = statement.executeQuery(sql);

                //Check whether the given id is exsisting in vinyl table
                System.out.println();
                Statement statement2 = connection.createStatement();
                String sql2 = "SELECT * FROM TblVinyl WHERE VinylId= '" + idDelete + "'";
                ResultSet resultSet1 = statement2.executeQuery(sql2);

                if (resultSet.next()) {
                    System.out.println("......The given Id Exists in the CD Store....."); //If item is in cd table


                    Statement statement1 = connection.createStatement();
                    String sql1 = "DELETE FROM TblCD WHERE CDID= '" + idDelete + "' ";  //delete item from database
                    statement1.executeUpdate(sql1);
                    System.out.println("You have successfully Deleted a CD From the store ");   //display item is a cd
                    // get the book count

                    System.out.println("Total Space Left In the Store : " + (MAX_COUNT - totalCount()));  //display total free spaces

                } else {

                if (resultSet1.next()) {

                    System.out.println(".........The given Id Exists in the Vinyl Store.......");      //if item is in vinyl table
                    Statement statement3 = connection.createStatement();
                    String sql3 = "DELETE FROM tblVinyl WHERE VinylId= '" + idDelete + "' ";  //delete item from vinyl table
                    statement3.executeUpdate(sql3);
                    System.out.println("You have Successfully Deleted a Vinyl From the Store");  //display item is a vinyl

                    System.out.println("Total Space Left In the Store : " + (MAX_COUNT - totalCount()));  //display total free spaces
                } else {
                    System.out.println("...This Item is Not in the Store...");  //if item not found display this
                }
                }


            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error!!!.....");
            }
        }else{
            System.out.println("\n--There Are No Music Items in the Store to Delete--");  //if there are no items in the store display this
        }

        System.out.println("\n------------------------------------");

    }

/////////////////////////////////////////////////////Print List////////////////////////////////////////
    public void printList() {
        String id,name;
        Map<String,String> cdList=new HashMap<>();   //Map for store cd id and tile
        Map<String,String> vinylList=new HashMap<>(); //MAp for store vinyl id and Name


        try {
            Statement stm = connection.createStatement();
            Statement st = connection.createStatement();
            st.executeQuery("SELECT * FROM TBLCD");  //Get all the items from the cd table
            ResultSet rs = st.getResultSet();


            while (rs.next()) {

                id = rs.getString("CDId");
                //idList.add(id);

                name = rs.getString("CDName");
                //nameList.add(name);
                cdList.put(name,id);     //Add all the cd items id and title to map

            }

            Set cdset = cdList.entrySet();
            // Get an iterator
            Iterator iteratorCd = cdset.iterator();

            // Display elements
            System.out.println("=========CD Item List=========");
            System.out.println("\nTitle\t\t\tID");
            while(iteratorCd.hasNext()) {

                Map.Entry object = (Map.Entry)iteratorCd.next();   //Print cd item list
                System.out.println(object.getKey()+"\t\t\t\t"+object.getValue());
            }

            st.executeQuery("SELECT * FROM TBLVINYL");     //select all the vinyl item details form the table
            ResultSet rs1=st.getResultSet();

            while (rs1.next()){
                id=rs1.getString("VinylId");

                name=rs1.getString("VinylTitle");

                vinylList.put(name,id);         //add all the vinyl items id and title to MAp
            }

            Set vinylSet= vinylList.entrySet();

            // Get an iterator
            Iterator iteratorVinyl=vinylSet.iterator();

            System.out.println("\n=======Vinyl Item List=======");
            System.out.println("\nTitle\t\t\tID");

            while (iteratorVinyl.hasNext()){
                Map.Entry object=(Map.Entry)iteratorVinyl.next();
                System.out.println(object.getKey()+"\t\t\t\t"+object.getValue());  //print vinyl item list
            }


        } catch (SQLException e) {
            //handle exceptions
            e.printStackTrace();
            System.out.println("Error ....!!! ");
        }
        System.out.println();
        System.out.println("-------------------------------");
    }

    @Override
    public void search() {

//Check if store is not empty
        if (totalCount() > 0) {

//Opens Search Gui
            new Thread() {
                @Override
                public void run() {
                    javafx.application.Application.launch(AllGui.class);
                }
            }.start();
        }

    else{

            System.out.println("--There Are No Music Items To Search--"); //If store is empty display this
        }

        System.out.println("-------------------------------");
        System.out.println();

    }

////////////////////////////////////////////////Sort Item//////////////////////////////////////////
    @Override
    public void sort() {

        if (totalCount() > 0) {

            String name, id;
            TreeMap<String, String> sorted = new TreeMap<>();  //Tree map to sort all the items alphabetical order

            try {
                Statement stm = connection.createStatement();
                Statement st = connection.createStatement();
                st.executeQuery("SELECT * FROM TBLCD");   //Select all the cd items from the cd table
                ResultSet rs = st.getResultSet();


                while (rs.next()) {

                    id = rs.getString("CDId");    //Add all the cd names to map
                    //idList.add(id);

                    name = rs.getString("CDName");  //Add all the cd id to map
                    //nameList.add(name);
                    sorted.put(name, id);

                }
                st.executeQuery("SELECT * FROM TBLVINYL");    //Select all the vinyl details from the vinyl table
                ResultSet rs1 = st.getResultSet();

                while (rs1.next()) {
                    id = rs1.getString("VinylId");   //Add all the vinyl names to Map

                    name = rs1.getString("VinylTitle");   //Add all the Vinyl id to map

                    sorted.put(name, id);
                }

                Set set = sorted.entrySet();

                // Get an iterator
                Iterator iterator = set.iterator();

                // Display elements
                System.out.println("=====Sorted Music Item List=====");
                System.out.println("\nTitle\t\t\tID");
                while (iterator.hasNext()) {
                    Map.Entry object = (Map.Entry) iterator.next();
                    System.out.println(object.getKey() + "\t\t\t\t" + object.getValue());  //Display sorted list using an iterator
                }

            } catch (SQLException e) {
                //handle exceptions
                e.printStackTrace();
                System.out.println("Error ....!!! ");
            }

        }else{                                            //If there are no items in the store display below
            System.out.println("-----There are no Music Items to Search-----");
        }

        System.out.println();
        System.out.println("-----------------------------");
    }

///////////////////////////////////////////////////////Buy Item////////////////////////////////////////////
    @Override
    public void buy(MusicItem item) {

        if(totalCount()>0) {                  //Check Store is Empty or not

            int option=0;
            while(option!=3){
                option=buyOption();
                switch (option){
                    case 1:
                        buyCD();        //Calling buy cd method
                        break;
                    case 2:
                        buyVinyl();   //Calling buy Vinyl method
                        break;
                }
            }
            System.out.println("Exit From the Buy Item Option");

        }else{
            System.out.println("--There Are No Music Items to Buy--");   //If store is empty display this
        }
        System.out.println("\n--------------------------------");
    }

////////////////////////////////////////////////////Generate report ////////////////////////////////////////////
    @Override
    public void generateReport() {          //Challenge Part

        if (soldCount() > 0) {         //Check if user sold some items or not

            Statement statement = null;
            //int totalCount=0;

            try {

                //1.Count total copies from each item in the store
                //2.Get each item sold month from the table
                //3.Total count for each item group by the sold month
                //4.Display month and no of copies from each item to the Month

                //Count all no of copies from each item from each item
                statement = connection.createStatement();
                String sql = "SELECT  tblSales.itemTitle, tblSales.itmId,SUM(tblSales.itemCount) as \"TotalCount\",Month(tblSales.soldDate) as \"Month\"\n" +
                        "FROM tblSales\n" +
                        "GROUP BY tblSales.itmId";
                ResultSet resultSet = statement.executeQuery(sql);
                //resultSet.next();
                System.out.println("=======Monthly Sales Report========");
                System.out.println();

                System.out.println("ItemTitle\tItemID\tTotalCount\tMonth");

                while (resultSet.next()) {   //Display the report
                    System.out.println(resultSet.getString("itemTitle") + "\t\t" + resultSet.getString("itmId") + "\t\t" + resultSet.getInt("TotalCount") + "\t\t" + resultSet.getString("Month"));
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("\n----------------------------------");

        }else{
            System.out.println("....There are no Items Sold....");
        }
    }


    /////////////////////////////////////////////////////////Add Cd Method//////////////////////////////////////////////////////////
    public void addCD(MusicItem item) {

        System.out.println("Total Space Left In the Store : "+(MAX_COUNT-totalCount()));   //Display total space left in the store

        System.out.print("\nInput CD ID : ");
        String itemId = input.next();        //Input cd id

        try {
            //check whether the given id is existing
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM TblCD WHERE CDID= '" + itemId + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {    //IF item is existing display below
                System.out.println("\n......The given Id is Already Exists in the CD Store.....");


            } else { //If item is not existing do below

                System.out.print("Input CD Name : ");  //Input name of the cd
                String itemName = input.next();

                System.out.print("Input CD Artist : ");  //Input artist if the cd
                String itemArtist = input.next();

                System.out.print("Input CD Genre : ");   //Input genre of the cd
                String itemGenre = input.next();

                System.out.print("Input CD Price : ");    //Input Price of the cd
                while(!input.hasNextBigDecimal()){       //Validating price(If Price is a Non-numeric)
                    System.out.print("\nPrice Cannot be Non Numeric..\nInput CD Price : ");
                    input.next();
                }
                BigDecimal itemPrice = input.nextBigDecimal();

                System.out.print("Input Total Duration : ");   //Input total duration
                String exception="\nTotal Duration cannot be Non Numeric...\nInput Total Duration : "; //Validating duration if it is a non numeric or not
                exceptionHandling(exception);
                int totalDuration = input.nextInt();

                Date dateObj = new Date();
                System.out.println(": : Input CD Released Date : :");
                System.out.print("Day : ");                             //Input day
                exception="\nDay cannot be Non Numeric..\nDay : ";    //Exception handling for Non Numeric
                exceptionHandling(exception);
                int day = input.nextInt();
                dateObj.setDay(day);                                 //Set date to Music Class

                System.out.print("Month : ");                             //Input Month
                exception="\nMonth Cannot be Non Numeric..\nMonth : ";
                exceptionHandling(exception);                   //Exception Handling for Non Numeric
                int month = input.nextInt();
                dateObj.setMonth(month);                      //Set month to Music Class

                System.out.print("Year : ");                    //Input year
                exception="\nYear cannot be Non Numeric...\nYear : ";
                exceptionHandling(exception);           //Exception Handling for non Numeric
                int year = input.nextInt();
                dateObj.setYear(year);             //Set day for Date class

                Date itemDate = new Date(day, month, year);  //Creating new constructor using set date,month,year

                item = new CD(itemId, itemName, itemGenre, itemDate, itemArtist, itemPrice, totalDuration);  //Creating new Music Item Constructor From CD type
                storeItem.add(item);         //Add item to StoreItem List


                //add cd details to cd table in the database
                PreparedStatement stm = null;
                try {
                    stm = connection.prepareStatement("INSERT INTO TblCD(CDId,CDName,CDGenre,CDArtist,CDPrice,CDDuration,Date)VALUES(?,?,?,?,?,?,? )");
                    stm.setString(1, itemId);          //Add cd id
                    stm.setString(2, itemName);         //Add cd Name
                    stm.setString(3, itemGenre);        //Add cd genre
                    stm.setString(4, itemArtist);       //Add cd Artist
                    stm.setString(5, String.valueOf(itemPrice));    //Add cd price
                    stm.setString(6, String.valueOf(totalDuration)); //Add cd duration
                    stm.setString(7,String.valueOf(itemDate));  //Add cd released date
                    stm.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.println("You have Successfully add a CD to the system!!");
            }

        } catch (SQLException e) {
            //handle exceptions
            e.printStackTrace();
            System.out.println("Error ....!!! ");
        }
        System.out.println("---------------------------------");
        System.out.println();
    }

    ///////////////////////////////////////////////////////////Add Vinyl Method///////////////////////////////////////////////////////////////

    public void addVinyl(MusicItem item) {


        System.out.println("Total Space Left In the Store : "+(MAX_COUNT-totalCount()));  //Display total space left in the store

        System.out.print("\nInput Vinyl Id : ");
        String itemId = input.next();              //Input Vinyl ID


        try {
            //check whether the given id is existing
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM TblVINYL WHERE VinylId= '" + itemId + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {      //If id is existing display below
                System.out.println("\n......The given Id is Already Exists in the Vinyl Store.....");


            } else {

                System.out.print("Input Vinyl Name : ");   //Input Vinyl Name
                String itemName = input.next();

                System.out.print("Input Vinyl Artist : ");  //Input Vinyl Artist
                String itemArtist = input.next();

                System.out.print("Input Vinyl Genre : ");  //Input Vinyl Genre
                String itemGenre = input.next();

                System.out.print("Input Vinyl Price : ");    //Input Vinyl Price
                while(!input.hasNextBigDecimal()){
                    System.out.print("\nPrice Cannot be Non Numeric..\nInput Vinyl Price : ");  //Validate price is a non numeric or not
                    input.next();
                }
                BigDecimal itemPrice = input.nextBigDecimal();

                System.out.print("Input Vinyl Speed : ");  //Input vinyl speed
                String exception="\nSpeed cannot be Non Numeric..\nInput Vinyl Speed : ";
                exceptionHandling(exception);  //Exception handlig for NOn numeric
                int itemSpeed = input.nextInt();

                System.out.print("Input Vinyl Diameter : ");  //input vinyl diameter
                exception="\nDiameter cannot be Non Numeric..\nInput Vinyl Diameter : ";
                exceptionHandling(exception);   //Exception handlig for NOn numeric
                int itemDiameter = input.nextInt();

                Date dateObj = new Date();
                System.out.println(": : Input Vinyl Released Date : : ");
                System.out.print("Day : ");            //Input vinyl day
                exception="\nDay cannot be Non Numeric..\nDay : ";    //Exception handlig for NOn numeric
                exceptionHandling(exception);
                int day = input.nextInt();
                dateObj.setDay(day);      //Set day for date class

                System.out.print("Month : ");
                exception="\nMonth Cannot be Non Numeric...\nMonth : ";  //Input month
                exceptionHandling(exception);   //Exception handlig for NOn numeric
                int month = input.nextInt();
                dateObj.setMonth(month);  //Set Month for date class

                System.out.print("Year : ");   //Input year
                exception="\nYear cannot be Non Numeric...\nYear : ";
                exceptionHandling(exception);   //Exception handlig for NOn numeric
                int year = input.nextInt();
                dateObj.setYear(year);   //set year for date class

                Date date = new Date(day, month, year);   //Creating new constructor using set date,month,year

               // Vinyl vinyl=new Vinyl();

                item = new Vinyl(itemId, itemName, itemGenre, date, itemArtist, itemPrice, itemSpeed, itemDiameter);  //Create new music item from vinyl type
                storeItem.add(item);  //Add vinyl item to storeItem List

                //Add Vinyl item Details to databse
                PreparedStatement stm = null;
                try {
                    stm = connection.prepareStatement("INSERT INTO TblVinyl(VinylId,VinylTitle,VinylGenre,VinylArtist,VinylPrice,VinylSpeed,VinylDiameter,Date)VALUES(?,?,?,?,?,?,?,?)");
                    stm.setString(1, itemId);  //Add vinyl id
                    stm.setString(2, itemName); //Add vinyl Name
                    stm.setString(3, itemGenre);  //Add vinyl genre
                    stm.setString(4, itemArtist);  //Add vinyl artist
                    stm.setString(5, String.valueOf(itemPrice));  //Add vinyl price
                    stm.setString(6, String.valueOf(itemSpeed));  //Add vinyl Speed
                    stm.setString(7, String.valueOf(itemDiameter));  //Add vinyl diameter
                    stm.setString(8,String.valueOf(date));  //Add vinyl date
                    stm.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.println("You have Successfully add a Vinyl to the system!!");

            }
         } catch (SQLException e) {
        //handle exceptions
        e.printStackTrace();
        System.out.println("Error ....!!! ");
    }
        System.out.println();
    }

    ///////////////////////////////////////////////////Check Availability Method/////////////////////////////////////////////////////

    public boolean contains(final List<MusicItem> list, final String itemId) {                  //Check Item is already available in the list
        return list.stream().filter(o -> o.getItemID().equals(itemId)).findFirst().isPresent();
    }


    /////////////////////Exception Handling Method//////////////////////
    public static void exceptionHandling(String message) {
        while (!input.hasNextInt()) {
            System.out.print(message);
            input.next();
        }
    }
///////////////////////////////Check Invalid option mEthod////////////////////
        public static int invalidOption(int option,int first,int last) {
       String message="Option cannot be Non Numeric\n\n" +
               "Input Your Option Number : ";
            while (!(first <= option && option <= last)) {                          //Do below while user enter a valid option
                System.out.println("You have entered a invalid option");
                System.out.print("\nInput Your Option Number : ");
                exceptionHandling(message);                                         //Calling exception handling method to enter option
                option = input.nextInt();                                           //Input the option
            }
            return option;                                                          //Return option if it is a valid one
        }

///////////////////////////////Check N of Copies///////////////////////////////
        public static int numberCopies(int copies){
        String message="Number of Copies Cannot Non Numeric\n" +
                "Enter Number of Copies : ";
            System.out.print("Input Number of Copies :  ");
            exceptionHandling(message);   //Exception Handling Method for handle non Numeric
            copies=input.nextInt();

        while(copies<0){                 //If user enters minus number for copies handle that
            System.out.println("Number of Copies Cannot be below 0");
            System.out.print("\nInput Number of Copies : ");
            exceptionHandling(message);    //exception handling method for non  numeric
            copies=input.nextInt();       //Input no of copies again
        }
        return copies;
        }

        ////////////////////////////////////////////Add Item Option Printing///////////////////////////////////////
        public static int addOption(){
        String exceptionMsg="Option cannot be Non Numeric\n\n" +
                "Input Your Add Item Option Number : ";
            System.out.println("Add Item Options==>");
            System.out.println("1) Add CD\n" +
                    "2) Add Vinyl\n" +
                    "3) Exit Add Item Option");

            System.out.print("\nInput Your Add Item Option : ");
            exceptionHandling(exceptionMsg);                                       //Calling exception handling for enter options
            int option = input.nextInt();                                          //Input Option
            option = invalidOption(option,1,3);                          //Calling invalid option method
            System.out.println("------------------------------");

            return option;                                                          //Return Option
        }
/////////////////////////////////////////Buy Item Option///////////////////////////////////////////////
        public static int buyOption(){
        String exceptionMsg="Option cannot be Non Numeric\n\n"+
                "Input Your Buy Item Option : ";

            System.out.println("Buy Item Options==>");
            System.out.println("1) Buy CD\n" +
                    "2) Buy Vinyl\n" +
                    "3) Exit Buy Item Option");

            System.out.print("\nInput Your Buy Item Option : ");
            exceptionHandling(exceptionMsg);                          //Exception handling method for non numeric validation
            int option=input.nextInt();
            option=invalidOption(option,1,3);               //invalid OPtion method calling for check valid options
            System.out.println("---------------------------------");
            return option;
        }


       /* public static int searchOption(){
        String exceptionMsg="Option cannot be Non Numeric\n\n" +
                "Input Your Search Ite Option : ";

            System.out.println("Search Item Options==>");

            System.out.println("1) Search CD\n" +
                    "2) Search Vinyl\n" +
                    "3) Exit Search Item Option");

            System.out.print("Input Your Search Item Option : ");
            exceptionHandling(exceptionMsg);
            int option=input.nextInt();
            option=invalidOption(option,1,3);
            System.out.println("-----------------------------");

            return option;

        }*/


       /* public static void searchCD(){
            new Thread() {
                @Override
                public void run() {
                    javafx.application.Application.launch(searchCdGui.class);
                }
            }.start();
        }
        public static void searchVinyl(){
            new Thread() {
                @Override
                public void run() {
                javafx.application.Application.launch(SearchVinylGui.class);
                    }
                }.start();
            }*/


//////////////////////////////////////Get Total Count of the Music Store//////////////////////////////////
    public int totalCount(){
        Statement statement = null;
        int totalCount=0;

        try {

            //Check No of Items In the Cd Table
            statement = connection.createStatement();
            String sql = "SELECT COUNT(*) FROM tblCD ";
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            int countCD = resultSet.getInt(1) ;

            //Check No of Items In the Vinyl Tabe
            String sql2 = "SELECT COUNT(*) FROM tblVinyl ";
            ResultSet resultSet2 = statement.executeQuery(sql2);
            resultSet2.next();
            int countVin = resultSet2.getInt(1);

           totalCount= countCD+countVin;   //Get Total Count

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalCount;
    }

// Get Sold Items Count
    public int soldCount(){
        int soldCount=0;
        Statement statement=null;
        try {
//Check No of Items in the Sold Items Table
            statement = connection.createStatement();
            String sql = "SELECT COUNT(*) FROM tblSales ";
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            soldCount = resultSet.getInt(1) ;


        } catch (SQLException e) {
            e.printStackTrace();
        }
    return soldCount;
    }

//////////////////////////////////////////////////Buy CD///////////////////////////////////////////
    public void buyCD(){
        System.out.print("Input CD ID : ");
        String cdId=input.next();

        try {
            //check whether the given id is existing
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM TblCD WHERE CDId= '" + cdId + "'";
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                String name=resultSet.getString("CDName");              //Get Name of the cd
                String id=resultSet.getString("CDId");                  //Get Id of the CD
                int price=resultSet.getInt("CDPrice");                  //Get Price of the Cd
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  //Get Sold Timer(Current Time)
                LocalDateTime now = LocalDateTime.now();


                int copies=0;
                copies=numberCopies(copies);           //Get Number of copies

                if(copies>0){
                    //Display Invoice report of the Sold Item
                    System.out.println("===Invoice Report===");
                    System.out.println("Cd Name : "+name);               //Display Name of the Cd
                    System.out.println("Cd Id : "+id);                   //Display Id of the Cd
                    System.out.println("Number of Copies : "+copies);    //Display No of Copies that Buy
                    System.out.println("Total Cost : "+(price*copies));  //Display Total Price


                    PreparedStatement stm = null;

                    try {
                        //Add sold item details to Sold Item table in database
                        stm = connection.prepareStatement("INSERT INTO tblSales(itmId,itemTitle,itemPrice,itemCount,soldDate)VALUES(?,?,?,?,?)");
                        stm.setString(1, id);               //Add cdId
                        stm.setString(2, name);             //Add CD Name
                        stm.setInt(3, price);               //Add cd Price
                        stm.setInt(4, copies);              //Add No of Copies
                        stm.setString(5,dtf.format(now));   //Add Sold Date
                        stm.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


            try {

                //Write Sold Details In a Text Document

                FileWriter fw = new FileWriter("D:\\IIT\\Progarmming Principles\\MusicStore\\FileWriter.txt",true); //Text Document Path

                fw.write("\n====Invoice Report====\n");
                fw.write("CD Name : "+name+"\n");             //Write Name
                fw.write("CD ID : "+id+"\n");                 //Write id
                fw.write("Number of Copies : "+copies+"\n");  //Write No of Copies
                fw.write("Total Cost : "+(price*copies+"\n"));  //Write total price
                fw.write("Sold Date/Time : "+dtf.format(now));   //Write Sold date
                fw.write("\n------------------------------------");
                fw.close();
            } catch(Exception e){System.out.println(e);}
            }

            } else {
                System.out.println("--Sorry This Item Is Not Available in the Store--");  //If id is not available display this
            }
        }catch (SQLException e) {
            //handle exceptions
            e.printStackTrace();
            System.out.println("Error ....!!! ");
        }
        System.out.println("\n--------------------------------");

    }

    public void buyVinyl(){
        System.out.print("Input Vinyl ID : ");                   //Input Vinyl Id that need to buy
        String vinylId=input.next();

        try {
            //check whether the given id is existing
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM TblVINYL WHERE VinylId= '" + vinylId + "'";
            ResultSet resultSet = statement.executeQuery(sql);          //Check id is in the store

            if (resultSet.next()) {                                         //If id is available
                String name=resultSet.getString("VinylTitle");   //Get relevant vinyl name
                String id=resultSet.getString("VinylId");       //get id
                int price=resultSet.getInt("VinylPrice");   //get price
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  //get sold time
                LocalDateTime now = LocalDateTime.now();

                int copies=0;
                copies=numberCopies(copies);     //Get No of Copies

                if(copies>0){
                    System.out.println("===Invoice Report===");       //Display details of sold vinyl
                    System.out.println("Vinyl Name : "+name);        //Display vinyl NAme
                    System.out.println("Vinyl Id : "+id);            //Display vinyl ID
                    System.out.println("Number of Copies : "+copies);   //Display no of copies
                    System.out.println("Total Cost : "+(price*copies));  //Display total price

                    PreparedStatement stm = null;

                    try {
                        //Add sold Vinyl details to sold item table
                        stm = connection.prepareStatement("INSERT INTO tblSales(itmId,itemTitle,itemPrice,itemCount,soldDate)VALUES(?,?,?,?,?)");
                        stm.setString(1, id);          //add vinyl id
                        stm.setString(2, name);         //Add vinyl Name
                        stm.setInt(3, price);           //Add vinyl Price
                        stm.setInt(4, copies);          //add no of copies
                        stm.setString(5,dtf.format(now));  //add sold date
                        stm.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                    try {
                        //Write sold vinyl details in a text document

                        FileWriter fw = new FileWriter("D:\\IIT\\Progarmming Principles\\MusicStore\\FileWriter.txt",true);//Text document path
                        fw.write("\n====Invoice Report====\n");
                        fw.write("Vinyl Name : "+name+"\n");  //Write name
                        fw.write("Vinyl ID : "+id+"\n");     //Write id
                        fw.write("Number of Copies : "+copies+"\n");    //Write no of copies
                        fw.write("Total Cost : "+(price*copies+"\n")); //Write total price
                        fw.write("Sold Date/Time : "+dtf.format(now));  //Write sold date
                        fw.write("\n------------------------------------");
                        fw.close();
                    } catch(Exception e){System.out.println(e);}
                }

            } else {
                System.out.println("--Sorry This Item Is Not Available in the Store--");  //If vinyl item is not available display this
            }
        }catch (SQLException e) {
            //handle exceptions
            e.printStackTrace();
            System.out.println("Error ....!!! ");
        }
        System.out.println("\n--------------------------------");

    }



}







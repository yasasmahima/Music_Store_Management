import ConnectDB.ConnectionClass;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AllGui extends Application {
    Stage window;
    Scene searchCD;          //Scene Search cd
    Scene searchVinyl;      //Scene Search Vinyl

    private ObservableList<ObservableList> data;   //Observable List to put database data to tables

    ConnectionClass connectionClass=new ConnectionClass();
    Connection connection=connectionClass.getConnection();
    TableView tableCD = new TableView();                        //Table cd
    TableView tableVinyl = new TableView();                     //Table Vinyl


    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        window=primaryStage;
        window.setTitle("Search Items");

///////////////////////////////////////////////////////////////Main Menu Window//////////////////////////////////////////

        GridPane grid=new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(10);

        grid.setPadding(new Insets(0,1,5,1));
        Image img3=new Image(getClass().getResourceAsStream("back.jpg"));  //Background Image of the Grid

        BackgroundImage backgroundimage = new BackgroundImage(img3,                //Set Background Image
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);

        // create Background
        Background background = new Background(backgroundimage);               //Set Background
        grid.setBackground(background);

        Scene scene=new Scene(grid,1070,460);                    //Set Scene Size
        window.setScene(scene);                                              //Set Scene to window
        window.show();

        Image quit = new Image(getClass().getResourceAsStream("quit.png"));    //Image for quit Button
        Button btnQuit=new Button("",new ImageView(quit));
        btnQuit.setFont(Font.font(20));
        btnQuit.setTextAlignment(TextAlignment.LEFT);
        grid.add(btnQuit,5,1);
        btnQuit.setOnAction(event -> window.close());                        //Quite Button Action Event (Close the window/GUI)


        Image imgAdd=new Image(getClass().getResourceAsStream("searchi.png"));    //Main Header Image
        Label welcome=new Label("",new ImageView(imgAdd));
        grid.add(welcome,3,2);

        Image imgAddCd = new Image(getClass().getResourceAsStream("cdi.png"));     //Image for search cd Button
        Button addVinyl=new Button("",new ImageView(imgAddCd));
        addVinyl.setFont(Font.font(20));
        grid.add(addVinyl,2,9);
        addVinyl.setOnAction(event -> window.setScene(searchCD));      //Action event for the search cd Button (Opens the Search CD GUI)

        Image imgRemoveVinyl = new Image(getClass().getResourceAsStream("vinyli.png"));   //Image for Search cd Button
        Button addCd=new Button("",new ImageView(imgRemoveVinyl));
        addCd.setFont(Font.font(20));
        grid.add(addCd,5,9);
        addCd.setOnAction(event -> window.setScene(searchVinyl));   //Action event for the search vinyl Button (OPens the search Vinyl GUI)

        //////////////////////////////////////////////////////////Search CD GUI///////////////////////////////////////////////////////


        ConnectionClass connectionClass=new ConnectionClass();            ///Database Connection
        Connection connection=connectionClass.getConnection();
        //window =primaryStage;
        //window.setTitle("Search CD");


        GridPane gridBuyCD=new GridPane();
        gridBuyCD.setAlignment(Pos.TOP_CENTER);
        gridBuyCD.setVgap(10);

        gridBuyCD.setPadding(new Insets(0,1,5,1));
        Image backgroundImage=new Image(getClass().getResourceAsStream("back.jpg"));     //Background Image for the grid
        backgroundimage = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);

        // create Background
        background = new Background(backgroundimage);    //Set background and the Image
        gridBuyCD.setBackground(background);

        searchCD=new Scene(gridBuyCD,1100,800);        //Set Search cd Scene

        Image imgBack = new Image(getClass().getResourceAsStream("Back.png"));  //Image for back Button
        Button back=new Button("",new ImageView(imgBack));
        back.setFont(Font.font(20));
        back.setTextAlignment(TextAlignment.LEFT);
        gridBuyCD.add(back,5,1);
        back.setOnAction(event -> window.setScene(scene));     //Action event for back Button(Go to Main gui)


        Image imgBuyCD=new Image(getClass().getResourceAsStream("Cds.png"));     //Header Image
        welcome=new Label("",new ImageView(imgBuyCD));
        gridBuyCD.add(welcome,3,2);

        tableCD.setMaxWidth(560);             //Set table Cd Width
        tableCD.setEditable(true);


        //tableCD.getColumns().addAll(cdNameCol,cdIdCol,cdArtistCol,cdGenreCol,cdDateCol,cdPriceCol,cdDurationCol);*/
        gridBuyCD.getColumnConstraints().add(new ColumnConstraints(50));
        gridBuyCD.add(tableCD,3,5);             //Set Table cd To Grid

        Label lblCDName=new Label("Name of the CD\t:  ");
        lblCDName.setFont(Font.font(20));
        gridBuyCD.add(lblCDName,1,8);

        TextField nameC=new TextField();                 //Name input text area for search
        nameC.setPromptText("\t\t\t\t\tCD Name");
        nameC.setMaxWidth(500);
        gridBuyCD.add(nameC,3,8);


        Label lblCdAvailability=new Label("");         //Result displayed label after search
        lblCdAvailability.setFont(Font.font(20));
        gridBuyCD.add(lblCdAvailability,3,10);

        /*TextField cdQuantity=new TextField("");
        cdQuantity.setPromptText("\t\t\t\t\tQuantity");
        gridBuyCD.add(cdQuantity,3,10);*/

        Image imgOrder = new Image(getClass().getResourceAsStream("search.png"));  //Image for search Button
        Button order=new Button("",new ImageView(imgOrder));
        order.setFont(Font.font(20));
        order.setTextAlignment(TextAlignment.LEFT);
        gridBuyCD.add(order,5,8);
        order.setOnAction(new EventHandler<javafx.event.ActionEvent>() {

            //Action event to serach button
            @Override
            public void handle(javafx.event.ActionEvent event) {

                try {
                    String name=nameC.getText();                         //Get name from the text box
                    //check whether the given name is existing
                    Statement statement = connection.createStatement();
                    String sql = "SELECT * FROM TblCD WHERE CDName= '" + name + "'";
                    ResultSet resultSet = statement.executeQuery(sql);
                    System.out.println(name);

                    if (resultSet.next()) {
                        //If name is existing  display id name artist and the price in the below label
                        lblCdAvailability.setText("CD Id : "+(resultSet.getString("cdId"))+"  CD Artist : "+(resultSet.getString("cdArtist")+"  CD Price : "+(resultSet.getString("cdPrice"))));

                    } else {
                        lblCdAvailability.setText("This is Not in the Music Store"); //if name is not available display this
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Error!!!.....");
                }

            }
        });

        buildDataCD();  //Add date to cd table from database

///////////////////////////////////////////////////////////Search Vinyl GUI//////////////////////////////////////////////////////////

        GridPane gridSearchVinyl = new GridPane();
        gridSearchVinyl.setAlignment(Pos.TOP_CENTER);
        gridSearchVinyl.setVgap(10);

        gridSearchVinyl.setPadding(new Insets(0, 1, 5, 1));
        backgroundImage = new Image(getClass().getResourceAsStream("back.jpg"));       //Set background image

        backgroundimage = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);

        // create Background
        background = new Background(backgroundimage);     //Set background image to grid
        gridSearchVinyl.setBackground(background);

        searchVinyl = new Scene(gridSearchVinyl, 1300, 800);      //Set scene size

        imgBack = new Image(getClass().getResourceAsStream("Back.png"));    //Image for back button
        back=new Button("",new ImageView(imgBack));
        back.setFont(Font.font(20));
        back.setTextAlignment(TextAlignment.LEFT);
        gridSearchVinyl.add(back,5,1);
        back.setOnAction(event -> window.setScene(scene));              //Action event for the back button (Go back to main GUI)


        Image imgVinyl = new Image(getClass().getResourceAsStream("vinyl.png"));     //Header image
        welcome = new Label("", new ImageView(imgVinyl));
        gridSearchVinyl.add(welcome, 3, 2);
        // welcome.setMinWidth();

        tableVinyl.setMaxWidth(720);               //Set table Vinyl Size
        tableVinyl.setEditable(true);


        //tableVinyl.getColumns().addAll(vinylNameCol,vinylIdCol,vinylArtistCol,vinylGenreCol,vinylDateCol,vinylPriceCol,vinylDurationCol,vinylDiameterCol);*/
        gridSearchVinyl.getColumnConstraints().add(new ColumnConstraints(50));
        gridSearchVinyl.add(tableVinyl, 3, 5);             //Add table Vinyl to Grid

        Label lblVinylName = new Label("Name of the Vinyl\t:  ");
        lblVinylName.setFont(Font.font(20));
        gridSearchVinyl.add(lblVinylName, 1, 8);

        TextField nameV = new TextField();
        nameV.setMaxWidth(500);
        nameV.setPromptText("\t\t\t\t\tVinyl Name");                 //Text field to enter name to search
        gridSearchVinyl.add(nameV, 3, 8);


        Label lblVinylAvailability = new Label("");
        lblVinylAvailability.setFont(Font.font(20));
        gridSearchVinyl.add(lblVinylAvailability, 3, 10);  //Label which displays search items details


        imgOrder = new Image(getClass().getResourceAsStream("search.png"));    //Image for search Button
        order = new Button("", new ImageView(imgOrder));
        order.setFont(Font.font(20));
        order.setTextAlignment(TextAlignment.LEFT);
        gridSearchVinyl.add(order, 5, 8);
        order.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                //Action event for the search Button
                try {
                    String name = nameV.getText();  //Get name from the text box

                    //check whether the given id is existing
                    Statement statement = connection.createStatement();
                    String sql = "SELECT * FROM TblVinyl WHERE VinylTitle= '" + name + "'";
                    ResultSet resultSet = statement.executeQuery(sql);
                    System.out.println(name);

                    if (resultSet.next()) {
                        //If name is available display id artist and the price
                        lblVinylAvailability.setText("Vinyl Id : " + (resultSet.getString("VinylId")) + "  Vinyl Artist : " + (resultSet.getString("VinylArtist") + "  Vinyl Price : " + (resultSet.getString("VinylPrice"))));

                    } else {
                        lblVinylAvailability.setText("This is Not in the Music Store");  //If vinyl is not available Display this
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Error!!!.....");
                }

            }
        });

        buildDataVinyl();  //Add data to Vinyl Table


    }


//////////////////////////////////////////////Method to add date to cd table//////////////////////////////////////
    public void buildDataCD(){
        // Connection c ;
        data = FXCollections.observableArrayList();
        try{
            //connection = DBConnect.connect();

            String SQL = "SELECT * from tblCD";
            //ResultSet
            ResultSet rs = connection.createStatement().executeQuery(SQL);


            //Table column added dynamically
            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                //Dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));

                //Non Property Style
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>, ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> cdListDetails) {
                        return new SimpleStringProperty(cdListDetails.getValue().get(j).toString());
                    }
                });

                tableCD.getColumns().addAll(col);
                //System.out.println("Column ["+i+"] ");
            }

            //Data added to observable list
            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));   //Add each row data to the rows in the table
                }

                data.add(row);

            }

            //Added data to table View
            tableCD.setItems(data);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }

////////////////////////////////////////////////////////////Method to add Details to the Vinyl Table/////////////////////////////////////////
    public void buildDataVinyl() {
        // Connection c ;
        data = FXCollections.observableArrayList();
        try {
            //connection = DBConnect.connect();

            String SQL = "SELECT * from tblVinyl";
            //ResultSet
            ResultSet rs = connection.createStatement().executeQuery(SQL);


            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {

                //Dynamic Table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));

                //Non Property Style

                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> vinylListDetails) {
                        return new SimpleStringProperty(vinylListDetails.getValue().get(j).toString());   //Get data from each column in the table database
                    }
                });

                tableVinyl.getColumns().addAll(col);  //Add columns to the table vinyl

            }


            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                //System.out.println("Row [1] added " + row);
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            tableVinyl.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }









}

import ConnectDB.ConnectionClass;
import com.sun.javafx.scene.layout.region.Margins;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class searchCdGui extends Application {


    Stage windowCD;

    private ObservableList<ObservableList> data;

    ConnectionClass connectionClass=new ConnectionClass();
    Connection connection=connectionClass.getConnection();
    TableView tableCD = new TableView();


    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {




        //////////////////////////////////////////////////////////////////////////////


        ConnectionClass connectionClass=new ConnectionClass();
        Connection connection=connectionClass.getConnection();
        windowCD =primaryStage;
        windowCD.setTitle("Search CD");


        GridPane gridBuyCD=new GridPane();
        gridBuyCD.setAlignment(Pos.TOP_CENTER);
        gridBuyCD.setVgap(10);

        gridBuyCD.setPadding(new Insets(0,1,5,1));
        Image backgroundImage=new Image(getClass().getResourceAsStream("back.jpg"));
        BackgroundImage backgroundimage = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);

        // create Background
        Background background = new Background(backgroundimage);
        gridBuyCD.setBackground(background);

        Scene searchCD=new Scene(gridBuyCD,1100,800);

        Image quit = new Image(getClass().getResourceAsStream("quit.png"));
        Button btnQuit=new Button("",new ImageView(quit));
        btnQuit.setFont(Font.font(20));
        btnQuit.setTextAlignment(TextAlignment.LEFT);
        gridBuyCD.add(btnQuit,5,1);
        btnQuit.setOnAction(event -> windowCD.close());


        Image imgBuyCD=new Image(getClass().getResourceAsStream("Cds.png"));
        Label welcome=new Label("",new ImageView(imgBuyCD));
        gridBuyCD.add(welcome,3,2);

        tableCD.setMaxWidth(560);
        tableCD.setEditable(true);


     /* TableColumn cdNameCol = new TableColumn("Cd Name");
        cdNameCol.prefWidthProperty().bind(searchCD.widthProperty().divide(10).subtract(2.1/3));
        cdNameCol.maxWidthProperty().bind(cdNameCol.prefWidthProperty());
        cdNameCol.setCellValueFactory(new PropertyValueFactory<>("CD Name"));
        cdNameCol.setText("");

        TableColumn cdIdCol = new TableColumn("Cd ID");
        cdIdCol.prefWidthProperty().bind(searchCD.widthProperty().divide(12).subtract(2.1/3));
        cdIdCol.maxWidthProperty().bind(cdIdCol.prefWidthProperty());
        cdIdCol.setCellValueFactory(new PropertyValueFactory<>("CD ID"));

        TableColumn cdArtistCol = new TableColumn("Cd Artist");
        cdArtistCol.prefWidthProperty().bind(searchCD.widthProperty().divide(10).subtract(2.1/3));
        cdArtistCol.maxWidthProperty().bind(cdArtistCol.prefWidthProperty());
        cdArtistCol.setCellValueFactory(new PropertyValueFactory<>("CD Artist"));

        TableColumn cdGenreCol = new TableColumn("Cd Genre");
        cdGenreCol.prefWidthProperty().bind(searchCD.widthProperty().divide(12).subtract(2.1/3));
        cdGenreCol.maxWidthProperty().bind(cdGenreCol.prefWidthProperty());
        cdGenreCol.setCellValueFactory(new PropertyValueFactory<>("CD Genre"));

       TableColumn cdDateCol = new TableColumn("Cd Date");
        cdDateCol.prefWidthProperty().bind(searchCD.widthProperty().divide(12).subtract(2.1/3));
        cdDateCol.maxWidthProperty().bind(cdDateCol.prefWidthProperty());
        cdDateCol.setCellValueFactory(new PropertyValueFactory<>("CD Date"));

        TableColumn cdPriceCol = new TableColumn("Cd Price");
        cdPriceCol.prefWidthProperty().bind(searchCD.widthProperty().divide(12).subtract(2.1/3));
        cdPriceCol.maxWidthProperty().bind(cdPriceCol.prefWidthProperty());
        cdPriceCol.setCellValueFactory(new PropertyValueFactory<>("Cd Price"));

        TableColumn cdDurationCol = new TableColumn("Duration");
        cdDurationCol.prefWidthProperty().bind(searchCD.widthProperty().divide(12).subtract(2.1/3));
        cdDurationCol.maxWidthProperty().bind(cdDurationCol.prefWidthProperty());
        cdDurationCol.setCellValueFactory(new PropertyValueFactory<>("Duration"));

        tableCD.getColumns().addAll(cdNameCol,cdIdCol,cdArtistCol,cdGenreCol,cdDateCol,cdPriceCol,cdDurationCol);*/
        gridBuyCD.getColumnConstraints().add(new ColumnConstraints(50));
        gridBuyCD.add(tableCD,3,5);

        Label lblCDName=new Label("Name of the CD\t:  ");
        lblCDName.setFont(Font.font(20));
        gridBuyCD.add(lblCDName,1,8);

        TextField nameC=new TextField();
        nameC.setPromptText("\t\t\t\t\tCD Name");
        nameC.setMaxWidth(500);
        gridBuyCD.add(nameC,3,8);


        Label lblCdAvailability=new Label("");
        lblCdAvailability.setFont(Font.font(20));
        gridBuyCD.add(lblCdAvailability,3,10);

        /*TextField cdQuantity=new TextField("");
        cdQuantity.setPromptText("\t\t\t\t\tQuantity");
        gridBuyCD.add(cdQuantity,3,10);*/

        Image imgOrder = new Image(getClass().getResourceAsStream("search.png"));
        Button order=new Button("",new ImageView(imgOrder));
        order.setFont(Font.font(20));
        order.setTextAlignment(TextAlignment.LEFT);
        gridBuyCD.add(order,5,8);
        order.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {

                try {
                    String name=nameC.getText();
                    //check whether the given id is existing
                    Statement statement = connection.createStatement();
                    String sql = "SELECT * FROM TblCD WHERE CDName= '" + name + "'";
                    ResultSet resultSet = statement.executeQuery(sql);
                    System.out.println(name);

                    if (resultSet.next()) {
                        lblCdAvailability.setText("CD Id : "+(resultSet.getString("cdId"))+"  CD Artist : "+(resultSet.getString("cdArtist")+"  CD Price : "+(resultSet.getString("cdPrice"))));

                    } else {
                        lblCdAvailability.setText("This is Not in the Music Store");
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Error!!!.....");
                }

            }
        });

        buildData();


        windowCD.setScene(searchCD);
        windowCD.show();

    }


    public void buildData(){
        // Connection c ;
        data = FXCollections.observableArrayList();
        try{
            //connection = DBConnect.connect();
            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT * from tblCD";
            //ResultSet
            ResultSet rs = connection.createStatement().executeQuery(SQL);


            //Table column added dynamically
            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>, ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
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
                    row.add(rs.getString(i));
                }
                //System.out.println("Row [1] added "+row );
                data.add(row);

            }

            //Added data to table View
            tableCD.setItems(data);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }









}

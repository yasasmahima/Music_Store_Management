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

public class SearchVinylGui extends Application {
    private ObservableList<ObservableList> data;

    ConnectionClass connectionClass = new ConnectionClass();
    Connection connection = connectionClass.getConnection();
    TableView tableVinyl = new TableView();

    Stage windowVinyl;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        windowVinyl = primaryStage;
        windowVinyl.setTitle("Search Vinyl");


        GridPane gridSearchVinyl = new GridPane();
        gridSearchVinyl.setAlignment(Pos.TOP_CENTER);
        gridSearchVinyl.setVgap(10);

        gridSearchVinyl.setPadding(new Insets(0, 1, 5, 1));
        Image backgroundImage = new Image(getClass().getResourceAsStream("back.jpg"));

        BackgroundImage backgroundimage = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);

        // create Background
        Background background = new Background(backgroundimage);
        gridSearchVinyl.setBackground(background);

        Scene searchVinyl = new Scene(gridSearchVinyl, 1300, 800);

        Image quit = new Image(getClass().getResourceAsStream("quit.png"));
        Button btnQuit = new Button("", new ImageView(quit));
        btnQuit.setFont(Font.font(20));
        btnQuit.setTextAlignment(TextAlignment.LEFT);
        gridSearchVinyl.add(btnQuit, 5, 1);
        btnQuit.setOnAction(event -> windowVinyl.close());


        Image imgBuyCD = new Image(getClass().getResourceAsStream("vinyl.png"));
        Label welcome = new Label("", new ImageView(imgBuyCD));
        gridSearchVinyl.add(welcome, 3, 2);
       // welcome.setMinWidth();

        tableVinyl.setMaxWidth(720);
        tableVinyl.setEditable(true);


        /*TableColumn vinylNameCol = new TableColumn("Vinyl Name");
        vinylNameCol.prefWidthProperty().bind(searchVinyl.widthProperty().divide(14).subtract(2.1/3));
        vinylNameCol.maxWidthProperty().bind(vinylNameCol.prefWidthProperty());

        TableColumn vinylIdCol = new TableColumn("Vinyl ID");
        vinylIdCol.prefWidthProperty().bind(searchVinyl.widthProperty().divide(14).subtract(2.1/3));
        vinylIdCol.maxWidthProperty().bind(vinylIdCol.prefWidthProperty());

        TableColumn vinylArtistCol = new TableColumn("Vinyl Artist");
        vinylArtistCol.prefWidthProperty().bind(searchVinyl.widthProperty().divide(14).subtract(2.1/3));
        vinylArtistCol.maxWidthProperty().bind(vinylArtistCol.prefWidthProperty());

        TableColumn vinylGenreCol = new TableColumn("Vinyl Genre");
        vinylGenreCol.prefWidthProperty().bind(searchVinyl.widthProperty().divide(14).subtract(2.1/3));
        vinylGenreCol.maxWidthProperty().bind(vinylGenreCol.prefWidthProperty());

        TableColumn vinylDateCol = new TableColumn("Vinyl Date");
        vinylDateCol.prefWidthProperty().bind(searchVinyl.widthProperty().divide(14).subtract(2.1/3));
        vinylDateCol.maxWidthProperty().bind(vinylDateCol.prefWidthProperty());

        TableColumn vinylPriceCol = new TableColumn("Vinyl Price");
        vinylPriceCol.prefWidthProperty().bind(searchVinyl.widthProperty().divide(14).subtract(2.1/3));
        vinylPriceCol.maxWidthProperty().bind(vinylPriceCol.prefWidthProperty());

        TableColumn vinylDurationCol = new TableColumn("Speed");
        vinylDurationCol.prefWidthProperty().bind(searchVinyl.widthProperty().divide(14).subtract(2.1/3));
        vinylDurationCol.maxWidthProperty().bind(vinylDurationCol.prefWidthProperty());

        TableColumn vinylDiameterCol = new TableColumn("Diameter");
        vinylDiameterCol.prefWidthProperty().bind(searchVinyl.widthProperty().divide(14).subtract(2.1/3));
        vinylDiameterCol.maxWidthProperty().bind(vinylDiameterCol.prefWidthProperty());


        tableVinyl.getColumns().addAll(vinylNameCol,vinylIdCol,vinylArtistCol,vinylGenreCol,vinylDateCol,vinylPriceCol,vinylDurationCol,vinylDiameterCol);*/
        gridSearchVinyl.getColumnConstraints().add(new ColumnConstraints(50));
        gridSearchVinyl.add(tableVinyl, 3, 5);

        Label lblVinylName = new Label("Name of the Vinyl\t:  ");
        lblVinylName.setFont(Font.font(20));
        gridSearchVinyl.add(lblVinylName, 1, 8);

        TextField nameV = new TextField();
        nameV.setMaxWidth(500);
        nameV.setPromptText("\t\t\t\t\tVinyl Name");
        gridSearchVinyl.add(nameV, 3, 8);


        Label lblVinylAvailability = new Label("");
        lblVinylAvailability.setFont(Font.font(20));
        gridSearchVinyl.add(lblVinylAvailability, 3, 10);


        Image imgOrder = new Image(getClass().getResourceAsStream("search.png"));
        Button order = new Button("", new ImageView(imgOrder));
        order.setFont(Font.font(20));
        order.setTextAlignment(TextAlignment.LEFT);
        gridSearchVinyl.add(order, 5, 8);
        order.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String name = nameV.getText();

                    //check whether the given id is existing
                    Statement statement = connection.createStatement();
                    String sql = "SELECT * FROM TblVinyl WHERE VinylTitle= '" + name + "'";
                    ResultSet resultSet = statement.executeQuery(sql);
                    System.out.println(name);

                    if (resultSet.next()) {
                        lblVinylAvailability.setText("Vinyl Id : " + (resultSet.getString("VinylId")) + "  Vinyl Artist : " + (resultSet.getString("VinylArtist") + "  Vinyl Price : " + (resultSet.getString("VinylPrice"))));

                    } else {
                        lblVinylAvailability.setText("This is Not in the Music Store");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Error!!!.....");
                }

            }
        });

        buildData();
        windowVinyl.setScene(searchVinyl);
        windowVinyl.show();

    }

    public void buildData() {
        // Connection c ;
        data = FXCollections.observableArrayList();
        try {
            //connection = DBConnect.connect();
            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT * from tblVinyl";
            //ResultSet
            ResultSet rs = connection.createStatement().executeQuery(SQL);


            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tableVinyl.getColumns().addAll(col);
                //System.out.println("Column [" + i + "] ");
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

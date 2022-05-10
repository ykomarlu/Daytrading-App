package cs1302.omega;

import javafx.scene.layout.Priority;
import cs1302.api.CryptoApi;
import cs1302.api.Currency;
import cs1302.api.CurrencyResult;
import cs1302.api.Money;
import cs1302.api.CurrencyList;
import javafx.scene.text.Font;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;
import java.io.IOException;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import java.net.URI;
import javafx.scene.text.Text;
import java.lang.Thread;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import java.lang.IndexOutOfBoundsException;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import java.util.ArrayList;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import javafx.scene.layout.TilePane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.event.ActionEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URLEncoder;
import javafx.geometry.Pos;
/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */

public class OmegaApp extends Application {
    //Copy and Paste as they are
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    //Put in new OmegaApp
    private CryptoApi cryptoApi;
    private Stage stage;
    private Scene scene;
    private VBox vbox;
    private HBox blankTopBox;
    private HBox blankBottomBox;
    private HBox blankMiddleBox;
    private Text title;
    private VBox tradeBox;
    private HBox tradeButtonHBox;
    private HBox convertFields;
    private HBox suggCurrencyBox;
    private VBox convertBox;
    private HBox tradeButtonBox;
    private ComboBox<String> dropdownThree;
    private HBox convertTextBox;
    private Button convertButton;
    private Button tradeButton;
    private TextField numField;
    private Text convertAmount;
    private Text suggestedCurrencies;
    private HttpRequest request;
    private ComboBox<String> dropdownOne;
    private ComboBox<String> dropdownTwo;
//    private HBox d1HBox;
    //  private HBox d2HBox;
    //  private HBox convertButtonBox;
    private String jsonString;
    private String symbol;
    private int index;

    /**
     * The constructor of the {@code OmegeApp} class which initializes all the class-level
     * and instance variables of the class.
     */
    public OmegaApp() {

        this.stage = new Stage();
        this.title = new Text("Day-To-Day Crypto App");
        title.setFont(new Font(40));
        this.vbox = new VBox(10);
        this.blankTopBox = new HBox();
        this.blankBottomBox = new HBox();
        this.blankMiddleBox = new HBox();
        this.tradeButtonHBox = new HBox();
        this.cryptoApi = new CryptoApi();
        this.vbox.setPrefWidth(1000);
        this.vbox.setPrefHeight(1000);
        this.convertFields = new HBox(370);
        this.convertFields.setPrefHeight(130);
        this.convertFields.setPrefWidth(1000);
        this.blankTopBox.setPrefHeight(250);
        this.blankBottomBox.setPrefHeight(250);
        this.blankTopBox.setPrefWidth(1000);
        this.blankBottomBox.setPrefWidth(1000);
        this.blankMiddleBox.setPrefHeight(150);
        this.suggCurrencyBox = new HBox();
        suggCurrencyBox.setPrefHeight(30);
        suggCurrencyBox.setPrefWidth(1000);
        this.tradeBox = new VBox(10);
        this.convertBox = new VBox(10);
        this.tradeBox.setPrefWidth(1000);
        this.convertBox.setPrefWidth(1000);
        this.tradeBox.setPrefHeight(250);
        this.convertBox.setPrefHeight(250);
        this.convertButton = new Button("Convert");
        this.tradeButton = new Button("Should I Buy This?");
        this.tradeButtonBox = new HBox(10);
        this.numField = new TextField("1.0");
        this.dropdownOne = new ComboBox();
        this.dropdownTwo = new ComboBox();
        this.dropdownThree = new ComboBox();
        this.convertAmount = new Text("this is convertAmount");
        this.convertTextBox = new HBox(220);
        this.suggestedCurrencies = new Text("This is suggested");
        suggestedCurrencies.setStyle("-fx-border-color: black;");
        convertAmount.setStyle("-fx-border-color: black;");
        this.tradeButtonHBox.getChildren().add(tradeButton);
        tradeButtonHBox.setPrefHeight(100);
        tradeButtonHBox.setPrefWidth(1000);
        appInit();
    }

    /**
     * A method that continues the beginnning FX initialization.
     */
    public void appInit() {

        tradeButtonHBox.setPrefHeight(100);
        tradeButtonHBox.setPrefWidth(1000);
        tradeButtonHBox.setAlignment(Pos.CENTER);
        convertTextBox.setPrefWidth(1000);
        convertTextBox.setPrefHeight(130);
        convertTextBox.setAlignment(Pos.CENTER);
        dropdownOne.setMaxWidth(200);
        dropdownOne.setValue("United States Dollar");
        dropdownTwo.setMaxWidth(200);
        dropdownTwo.setValue("bitcoin");
        convertTextBox.setAlignment(Pos.CENTER);
        convertTextBox.getChildren().addAll(dropdownOne, convertButton, dropdownTwo);
        convertFields.setAlignment(Pos.CENTER);
        convertFields.getChildren().addAll(numField, convertAmount);
        convertBox.setAlignment(Pos.CENTER);
        convertBox.getChildren().addAll(convertFields, convertTextBox);
        convertBox.setPrefWidth(1000);
        convertBox.setPrefHeight(20);
        tradeButtonBox.setAlignment(Pos.CENTER);
        tradeButtonBox.getChildren().addAll(cryptoApi.getTextField());
        tradeButtonBox.setPrefWidth(1000);
        tradeButtonBox.setPrefHeight(30);
        suggCurrencyBox.setAlignment(Pos.CENTER);
        suggCurrencyBox.setPrefWidth(1000);
        suggCurrencyBox.setPrefHeight(30);
        suggCurrencyBox.getChildren().add(suggestedCurrencies);
        tradeBox.getChildren().addAll(tradeButtonBox, tradeButtonHBox, suggCurrencyBox);
        blankTopBox.getChildren().add(title);
        blankTopBox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(blankTopBox, tradeBox, blankMiddleBox,
            convertBox, blankBottomBox);
        this.scene = new Scene(this.vbox);
        this.stage.setOnCloseRequest(event -> Platform.exit());
        this.stage.setTitle("Day-Trading Crypto Site");
        this.stage.setScene(this.scene);
        this.stage.sizeToScene();
        this.stage.show();
        Platform.runLater(() -> this.stage.setResizable(false));

    }

    /**
     * A method that initializes the FX Application.
     */
    public void init() {
        System.out.println("init() is called");
    }

    /**
     * A method that executes all the functionalities on the page.
     * @param stage A reference to the current Stage object of the application
     */
    public void start(Stage stage) {
        // build request
        cryptoApi.dropDownInit();
        for (int i = 0; i < cryptoApi.getList().length; i++) {
            dropdownOne.getItems().add(cryptoApi.getList()[i].description);
            // symbols.add(cryptoApi.getList()[i].currency);
        }
        cryptoApi.dropDownInitTwo();
        for (int i = 0; i < cryptoApi.getListCurrency().length; i++) {
            dropdownTwo.getItems().add(cryptoApi.getListCurrency()[i].id);
        }

        EventHandler<ActionEvent> load = (e) -> {
            cryptoApi.buttonExecute();
            suggestedCurrencies.setText(isDecreasing());
        };

        tradeButton.setOnAction(load);

        EventHandler<ActionEvent> convert = (e) -> {

            for (int i = 0; i < cryptoApi.getList().length; i++) {
                if ((dropdownOne.getValue().toString())
                     .equals(cryptoApi.getList()[i].description)) {
                    index = i;
                }
            }

            symbol = cryptoApi.getList()[index].currency;

            System.out.println("NumField Text: " + numField.getText());

            cryptoApi.convert(symbol, dropdownTwo.getValue()
                .toString(), numField.getText());

            System.out.println("Conv Amount: "
                + (cryptoApi.getConversionAmnt()));

            System.out.println("Price: " +
                (cryptoApi.getPrice()));

            convertAmount.setText("$" + (cryptoApi.getConversionAmnt() * cryptoApi.getPrice()));
        };

        convertButton.setOnAction(convert);

    }

    /**
     * A method that returns a String based on how the value of changePercent24Hr compares to 0.
     * @return String
     */
    public String isDecreasing() {
        if (cryptoApi.getChange() < 0) {
            return "This is a favorable currency to purchase! Average Change: "
                + cryptoApi.getChange();
        }

        return "This is not the best currency to purchase. Average Change: "
            + cryptoApi.getChange();
    }

    /**
     * A method that ends the FX application when the window is closed.
     */
    public void stop() {
        System.out.println("stop() called");
    }
}

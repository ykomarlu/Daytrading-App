package cs1302.api;

import java.io.IOException;
import javafx.scene.control.TextField;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Example using Open Library Search API.
 *
 * <p>
 * To run this example on Odin, use the following commands:
 *
 * <pre>
 * $ mvn clean compile
 * $ mvn exec:java -Dexec.mainClass=cs1302.api.OpenLibrarySearchApi
 * </pre>
 */
public class CryptoApi {

    /** HTTP client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)           // uses HTTP protocol version 2 where possible
        .followRedirects(HttpClient.Redirect.NORMAL)  // always redirects, except from HTTPS to HTTP
        .build();                                     // builds and returns a HttpClient object

    /** Google {@code Gson} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()                          // enable nice output when printing
        .create();                                    // builds and returns a Gson object

    private double averageChange;
    private TextField suggCurrenciesTextField = new TextField("bitcoin");
    private double changeNum;
    private Money[] mList;
    private Currency[] cList;
    private double convAmnt;
    private double d2USDPrice;
    /**
     * An example of some things you can do with a response.
     */

    /**
     * A getter method that retrieves a reference to a
     * {@code TextField} called suggCurrenciesTextField.
     * @return TextField
     */
    public TextField getTextField() {
        return suggCurrenciesTextField;
    }

    /**
     * A getter method that retrieves a reference to a {@code double} value called
     * changeNum.
     * @return double
     */
    public double getChange() {
        return changeNum;
    }

    /**
     * A getter method that retrieves a reference to a {@code Money[]} called
     * mList.
     * @return Money[]
     */
    public Money[] getList() {
        return mList;
    }

    /**
     * A getter method that retrieves a reference to a {@code double} called
     * d2USDPrice.
     * @return double
     */
    public double getPrice() {
        return d2USDPrice;
    }

    /**
     * A getter method that retrieves a reference to a {@code double} called
     * convAmnt.
     * @return double
     */
    public double getConversionAmnt() {
        return convAmnt;
    }

    /**
     * A getter method that retrieves a reference to a {@code Currency[]} called
     * cList.
     * @return Currency[]
     */
    public Currency[] getListCurrency() {
        return cList;
    }

    /**
     * A method that makes an api call to the Amdoren API to get the initial
     * values of the first dropdown list.
     *
     */
    public void dropDownInit() {
        try {
            String api_key = URLEncoder.encode("rjd6vdwiQWPHXGv2xGy6H9sUre4Q7v"
                , StandardCharsets.UTF_8);
            String uri = String.format("https://www.amdoren.com/api/currency_list.php?" +
                "api_key=%s", api_key);
            HttpResponse<String> json = fetchString(uri);
            CurrencyList result = GSON.fromJson(json.body().trim(), CurrencyList.class);
            System.out.println(result.currencies != null);
            mList = result.currencies;
            return;
        } catch (IllegalArgumentException | IOException | InterruptedException e) {
            System.err.println(e.getMessage());
        } // try
    } // search

    /**
     * A method that makes an api call to the Coincap API to get the initial values
     * of the second dropdown list.
     *
     */
    public void dropDownInitTwo() {
        try {
            String uri = String.format("https://api.coincap.io/v2/assets");
            HttpResponse<String> json = fetchString(uri);
            CurrencyArray result = GSON.fromJson(json.body().trim(), CurrencyArray.class);
            System.out.println(result.data != null);
            cList = result.data;
            return;
        } catch (IllegalArgumentException | IOException | InterruptedException e) {
            System.err.println(e.getMessage());
        } // try
    }

    /**
     * A method that retrieves the value for a property called changePercent24Hr
     * for a particular currency.
     *
     */
    public void buttonExecute() {
        System.out.println("This may take some time to download...");
        try {
            String id = URLEncoder.encode(suggCurrenciesTextField.getText().toLowerCase()
                , StandardCharsets.UTF_8);
            System.out.println("id: " + id);
            String uri = String.format("https://api.coincap.io/v2/assets/%s", id);
            HttpResponse<String> json = fetchString(uri);
            CurrencyResult result = GSON.fromJson(json.body().trim(), CurrencyResult.class);
            System.out.println(result.data != null);
            System.out.println(result.data.changePercent24Hr);
            changeNum = Double.parseDouble(result.data.changePercent24Hr);
            return;
        } catch (IllegalArgumentException | IOException | InterruptedException e) {
            System.err.println(e.getMessage());
        } // try
    } // search

    /**
     * A method that retrieves double values called d2USDPrice and convAmnt.
     * @param d1 A string reference variable to the selected value of the first
     * dropdown list
     * @param d2 A string reference variable to the selected values of the second
     * dropdown list
     * @param val A string reference variable to the amount of currency wanted to convert
     * @throws IOException if an I/O error occurs when sending or receiving
     * @throws InterruptedException if the HTTP client's {@code send} method is
     * interrupted
     * @throws IllegalArgumentException an exception that gets returns if an invalid argument
     * type is passed in
     */
    public void convert(String d1, String d2, String val) {
        try {
            String id = URLEncoder.encode(d2.toLowerCase()
                , StandardCharsets.UTF_8);
            System.out.println("id: " + id);
            String uri = String.format("https://api.coincap.io/v2/assets/%s", id);
            HttpResponse<String> json = fetchString(uri);
            CurrencyResult result = GSON.fromJson(json.body().trim(), CurrencyResult.class);
            System.out.println(result.data != null);
            System.out.println(result.data.priceUsd);
            d2USDPrice = Double.parseDouble(result.data.priceUsd);

            String api_key = URLEncoder.encode("rjd6vdwiQWPHXGv2xGy6H9sUre4Q7v"
                , StandardCharsets.UTF_8);
            String to = "USD";
            String from = d1;
            String amount = val;
            uri = String.format("https://www.amdoren.com/api/currency.php?api_key=%s&from=%s"
            + "&to=%s&amount=%s", api_key, to, from, amount);
            json = fetchString(uri);
            ConvertTemplate resultTwo = GSON.fromJson(json.body().trim(), ConvertTemplate.class);
            System.out.println(resultTwo != null);
            System.out.println(resultTwo.amount != 0.0);
            convAmnt = resultTwo.amount;
            return;
        } catch (IllegalArgumentException | IOException | InterruptedException e) {
            System.err.println(e.getMessage());
        } // try
    }

    /**
     * Returns the response body string data from a URI.
     * @param uri location of desired content
     * @return HttpResponse A reference to a HttpResponse object
     */
    private HttpResponse<String> fetchString(String uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .build();
        HttpResponse<String> response = HTTP_CLIENT
            .send(request, BodyHandlers.ofString());
        final int statusCode = response.statusCode();
        if (statusCode != 200) {
            throw new IOException("response status code not 200:" + statusCode);
        } // if
        System.out.println(response.body().trim());
        return response;
    } // fetchString

} // OpenLibrarySearchApi

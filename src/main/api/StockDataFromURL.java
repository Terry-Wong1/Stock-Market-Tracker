package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;


/* Obtains stock data by making an http request to AlphaVantage. Data returned by AlphaVantage is in JSON format
* which is saved as a String*/
public abstract class StockDataFromURL {
    private static final String GLOBAL_QUOTES_URL = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=";
    private static final String API_KEY = "TZC9YNYQWA4DPF84";
    private static int timeOut = 5000;

    public static String getStockData(String ticker) {
        BufferedReader bufferedReader;
        String line;
        StringBuilder responseContent = new StringBuilder();
        try {
            URL request = new URL(GLOBAL_QUOTES_URL + ticker + "&apikey=" + API_KEY);
            URLConnection connection = request.openConnection();
            connection.setConnectTimeout(timeOut);
            connection.setReadTimeout(timeOut);
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = bufferedReader.readLine()) != null) {
                responseContent.append(line);
            }
            bufferedReader.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseContent.toString();
    }

}

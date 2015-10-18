package com.wallerstein.timeseries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;

/**
 * Constructs TimeSeries using Yahoo finance as a source.
 */
final class YahooTimeSeriesFactory {

    private static final int DEFAULT_TIME_PERIOD = 4;
    private static final Map<String, TimeSeries> TIME_SERIES_MAP =
            new HashMap<String, TimeSeries>();

    private YahooTimeSeriesFactory() {
        throw new UnsupportedOperationException(
                "YahooTimeSeriesFactory is a utility class and " +
                        "cannot be instantiated.");
    }

    /**
     * Get a time series of closing prices for the given symbol.
     * @param symbol valid trade symbol
     * @return TimeSeries
     */
    public static TimeSeries getClosingPricesTS(final String symbol) {
        return getClosingPricesTS(symbol, DEFAULT_TIME_PERIOD);
    }

    public static TimeSeries getClosingPricesTS(final String symbol,
                                                 final int timePeriod) {

        if (TIME_SERIES_MAP.containsKey(symbol)) {
            return TIME_SERIES_MAP.get(symbol);
        }

        TimeSeries retVal = new TimeSeries(symbol);

        try {
            URL url = new URL(getURL(symbol, timePeriod));

            URLConnection urlConn = url.openConnection();
            InputStreamReader inStream = new InputStreamReader(
                    urlConn.getInputStream());
            BufferedReader buff = new BufferedReader(inStream);
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            // ignore first line
            buff.readLine();
            // get the quote as a csv string
            String csvString;
            while ((csvString = buff.readLine()) != null) {

                // parse the csv string
                StringTokenizer tokenizer = new StringTokenizer(csvString, ",");
                String tradeDate = tokenizer.nextToken();
                tokenizer.nextToken();
                tokenizer.nextToken();
                tokenizer.nextToken();
                tokenizer.nextToken();
                tokenizer.nextToken();
                String adjustedClose = tokenizer.nextToken();

                // System.out.println("trade date: " + tradeDate + " close: "
                // + adjustedClose);

                retVal.add(new Day(formatter.parse(tradeDate)),
                        Double.valueOf(adjustedClose));
            }
            TIME_SERIES_MAP.put(symbol, retVal);
        } catch (MalformedURLException e) {
            System.out.println("Please check the spelling of the URL:"
                    + e.toString());
        } catch (IOException e1) {
            System.out
                    .println("Can't read from the Internet: " + e1.toString());
        } catch (ParseException e2) {
            System.out.println("Can't parse " + e2.toString());
        }

        // System.out.println("result: " + retVal.);
        return retVal;
    }

    private static String getURL(final String symbol, final int timePeriod) {
        Calendar rightNow = Calendar.getInstance();
        int currentYear = rightNow.get(Calendar.YEAR);
        int month = rightNow.get(Calendar.MONTH);
        int day = rightNow.get(Calendar.DAY_OF_MONTH);
        int targetYear = currentYear - timePeriod;

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("http://ichart.finance.yahoo.com/table.csv?s=");
        urlBuilder.append(symbol);
        urlBuilder.append("&a=");
        urlBuilder.append(month);
        urlBuilder.append("&b=");
        urlBuilder.append(day);
        urlBuilder.append("&c=");
        urlBuilder.append(targetYear);
        urlBuilder.append("&d=");
        urlBuilder.append(month);
        urlBuilder.append("&e=");
        urlBuilder.append(day);
        urlBuilder.append("&f=");
        urlBuilder.append(currentYear);
        urlBuilder.append("&g=");
        urlBuilder.append("d"); // daily returns
        return urlBuilder.toString();
    }

}

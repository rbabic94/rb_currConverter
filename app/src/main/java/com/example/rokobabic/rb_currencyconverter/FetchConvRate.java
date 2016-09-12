package com.example.rokobabic.rb_currencyconverter;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FetchConvRate extends AsyncTask<Void, Void, String> {

    public Float conversionRate;
    String s1, s2;

    public String res;

    public FetchConvRate(String s1, String s2) {
        conversionRate = 0.0f;
        this.s1 = s1;
        this.s2 = s2;
    }

    @Override
    protected String doInBackground(Void... params) {
        OkHttpClient client = new OkHttpClient();
        /*Uri uri = Uri.parse("https://query.yahooapis.com/v1/public/yql").buildUpon()
                .appendQueryParameter("q", "select Name, Rate from yahoo.finance.xchange where pair in (" +
                        s1 + s2 + ")")
                .appendQueryParameter("format", "json")
                .appendQueryParameter("env", "store://datatables.org/alltableswithkeys")
                .appendQueryParameter("callback", "")
                .build();*/

        try {
            URL url = new URL("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22"+s1+s2+"%22)&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys");

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            ResponseBody responseBody = client.newCall(request)
                    .execute()
                    .body();
            String response = responseBody.string();

            //str = response;
            conversionRate = parseConversionRates(response);

            responseBody.close();
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
}
    public Float getRate()
    {
        return conversionRate;
    }
    public String getRateS()
    {
        return res;
    }

    private Float parseConversionRates(String responseBody) {

        //Float conversionRate = 0.0f;
        String rate = "<Rate>";
        String _rate = "</Rate>";
        //int start = 0;
        String result;
        //String a;

        //Pattern word = Pattern.compile(rate);
        //Matcher match = word.matcher(responseBody);

        //start = responseBody.indexOf(rate);
        //a = Integer.toString(start);

        //result = responseBody.substring(responseBody.indexOf(rate)+6, responseBody.indexOf(_rate)-1);
        responseBody = responseBody.replaceAll("\"", "'");
        result = responseBody.substring(responseBody.indexOf(rate)+6, responseBody.indexOf(_rate));
        res = result;
        /*JSONObject responseJson = new JSONObject(responseBody);
        JSONArray rates = responseJson.getJSONObject("query")
                .getJSONObject("results")
                .getJSONArray("rate");

        //String conversionRates = new String();

        //for (int i = 0; i < rates.length(); i++) {
            JSONObject rate = rates.getJSONObject(0);
            String[] currencies = rate.getString("Name").split("/");
            conversionRate = Float.valueOf(rate.getString("Rate"));
            //conversionRates.add(new ConversionRate(currencies[0], currencies[1], conversionRate));
        //}*/
        return 0.0f;//Float.valueOf(result);
    }
}
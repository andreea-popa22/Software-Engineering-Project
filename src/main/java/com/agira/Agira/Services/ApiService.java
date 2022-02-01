package com.agira.Agira.Services;

import com.agira.Agira.SensitiveInformation;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import org.springframework.boot.configurationprocessor.json.*;
//import org.springframework.boot.configurationprocessor.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;

import java.net.URL;
import java.util.Scanner;

public class ApiService {
    public static String ReadData(URL url) {
        try {
            StringBuilder informationString = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                informationString.append(scanner.nextLine());
            }
            scanner.close();
            return informationString.toString();
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return "error";
    }

    public static JSONObject PrettyPrintJson(String informationString) {
        Object obj = null;
        try {
            JSONObject dataObject = new JSONObject(informationString);
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataObject));
            return dataObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String GetRouteForCity(String city)
    {
        return SensitiveInformation.apiRoute + city + SensitiveInformation.apiToken;
    }

    public static String GetParameter(String city, String parameter){
        try {
            URL url = new URL(ApiService.GetRouteForCity(city));
            String city_info = ApiService.ReadData(url);

            JSONObject dataObject = new JSONObject(city_info);

            String value = dataObject.getJSONObject("data").getJSONObject("iaqi").getJSONObject(parameter.toLowerCase()).getString("v");
            System.out.println(parameter.toUpperCase() + ":  " + value);
            return value;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

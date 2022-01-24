package com.agira.Agira;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.configurationprocessor.json.*;
//import org.springframework.boot.configurationprocessor.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;

import java.io.IOException;
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

//    public static JSONObject StringToJson(String informationString) {
//        Object obj = null;
//        try {
//            JSONObject dataObject = new JSONObject(informationString);
//
//            //ObjectMapper mapper = new ObjectMapper();
//            //System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataObject));
//            return dataObject;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public static String GetRouteForCity(String city)
    {
        return SensitiveInformation.apiRoute + city + SensitiveInformation.apiToken;
    }

    public static String GetCO(String city){
        try {
            URL url = new URL(ApiService.GetRouteForCity(city));
            String city_info = ApiService.ReadData(url);

            JSONObject dataObject = new JSONObject(city_info);

           String co = dataObject.getJSONObject("data").getJSONObject("iaqi").getJSONObject("no2").getString("v");
           System.out.println("CO:  " + co);
           return co;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}

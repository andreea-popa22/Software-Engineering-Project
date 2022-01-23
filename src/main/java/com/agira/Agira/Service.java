package com.agira.Agira;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class Service {
    public static String ReadData(URL url) throws IOException {
        StringBuilder informationString = new StringBuilder();
        Scanner scanner = new Scanner(url.openStream());

        while (scanner.hasNext()) {
            informationString.append(scanner.nextLine());
        }
        scanner.close();
        return informationString.toString();
    }

    public static JSONObject StringToJson(String informationString) throws ParseException, JsonProcessingException {
        Object obj = new JSONParser().parse(informationString);

        JSONObject dataObject = (JSONObject) obj;

        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataObject));
        return dataObject;
    }
}

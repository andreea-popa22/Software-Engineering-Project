package com.agira.Agira.Services;

import com.agira.Agira.SensitiveInformation;

import java.sql.*;

public class DatabaseService {

    public static String GetPurifierLocation(int id) {
        try {
            String selectSql = "SELECT location_name FROM PURIFIER WHERE purifier_id=?";
            Connection connection = DriverManager.getConnection(SensitiveInformation.databaseJDBC, SensitiveInformation.databaseUser, SensitiveInformation.databasePassword);

            PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                return resultSet.getString(1).toLowerCase();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
package BackEnd.Controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    private final String URL = "jdbc:mysql://localhost:3306/testdb?useSSL=false";
    private final String USER = "testuser";
    private final String PASSWORD = "test623";

    public static void main(String[] args){
        try (
                Connection con = DriverManager.getConnection(URL, USER, PASSWORD);


    }
}

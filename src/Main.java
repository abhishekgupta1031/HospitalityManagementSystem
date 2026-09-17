import java.sql.Connection;
import util.DatabaseConnector;

public class Main {

    public static void main(String[] args) {

        try (Connection connection = DatabaseConnector.getConnection()) {

            System.out.println("Database connected successfully!");

        } catch (Exception e) {

            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
    }
}
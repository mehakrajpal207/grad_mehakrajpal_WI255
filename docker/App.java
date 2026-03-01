import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class App {

    public static void main(String[] args) {

        String url = "jdbc:postgresql://postgres-container:5432/testdb";
        String user = "postgres";
        String password = "postgres";

        try {
            Class.forName("org.postgresql.Driver");

            Connection con = DriverManager.getConnection(url, user, password);

            String sql = "INSERT INTO users(name, email) VALUES(?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "Avrut");
            ps.setString(2, "avrut@gmail.com");

            int rows = ps.executeUpdate();

            System.out.println("Inserted rows: " + rows);

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
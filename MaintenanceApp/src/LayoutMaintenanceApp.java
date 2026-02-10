import java.sql.*;
import java.util.Scanner;

public class LayoutMaintenanceApp {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/layout_maintenance";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "Root";

    private static final String ADMIN_PASSWORD = "admin123";

    /* ================= JDBC ================= */
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /* ================= MAINTENANCE ================= */
    private static double calculateMaintenance(int length, int width, boolean occupied) {
        int area = length * width;
        return occupied ? area * 9 : area * 6;
    }

    /* ================= MAIN ================= */
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Layout Maintenance System =====");
            System.out.println("1. Admin");
            System.out.println("2. Site Owner");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> adminLogin(sc);
                case 2 -> ownerMenu(sc);
                case 3 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option");
            }
        }
    }

    /* ================= ADMIN LOGIN ================= */
    private static void adminLogin(Scanner sc) {
        System.out.print("Enter Admin Password: ");
        String pwd = sc.nextLine();

        if (!ADMIN_PASSWORD.equals(pwd)) {
            System.out.println("Wrong password. Returning to main menu.");
            return;
        }
        adminMenu(sc);
    }

    /* ================= ADMIN MENU ================= */
    private static void adminMenu(Scanner sc) {

        while (true) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. Add Owner");
            System.out.println("2. Add Site");
            System.out.println("3. View Pending Maintenance");
            System.out.println("4. Approve Owner Updates");
            System.out.println("5. Back");
            System.out.print("Choice: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1 -> addOwner(sc);
                case 2 -> addSite(sc);
                case 3 -> viewPendingMaintenance(sc);
                case 4 -> approveUpdates();
                case 5 -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    /* ================= ADD OWNER ================= */
    private static void addOwner(Scanner sc) {
        try (Connection con = getConnection()) {

            System.out.print("Owner Name: ");
            String name = sc.nextLine();

            System.out.print("Phone: ");
            String phone = sc.nextLine();

            System.out.print("Email: ");
            String email = sc.nextLine();

            String sql = "INSERT INTO owner(name, phone, email) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.executeUpdate();

            System.out.println("Owner added successfully");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* ================= ADD SITE ================= */
    private static void addSite(Scanner sc) {
        try (Connection con = getConnection()) {

            System.out.print("Site Type (VILLA/APARTMENT/INDEPENDENT/OPEN): ");
            String type = sc.nextLine().toUpperCase();

            boolean occupied = !type.equals("OPEN");

            int siteCount = getSiteCount(con);
            if (siteCount >= 35) {
                System.out.println("All 35 sites already allocated.");
                return;
            }

            int length, width;
            if (siteCount < 10) { length = 40; width = 60; }
            else if (siteCount < 20) { length = 30; width = 50; }
            else { length = 30; width = 40; }

            System.out.print("Owner ID: ");
            int ownerId = sc.nextInt();

            double maintenance = calculateMaintenance(length, width, occupied);

            String sql =
                "INSERT INTO site(site_type,length,width,occupied,maintenance_amount," +
                "maintenance_paid,owner_id,update_status) VALUES (?,?,?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, type);
            ps.setInt(2, length);
            ps.setInt(3, width);
            ps.setBoolean(4, occupied);
            ps.setDouble(5, maintenance);
            ps.setBoolean(6, false);
            ps.setInt(7, ownerId);
            ps.setString(8, "APPROVED");

            ps.executeUpdate();

            System.out.println("Site added successfully");
            System.out.println("Maintenance = ₹" + maintenance);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int getSiteCount(Connection con) throws SQLException {
        ResultSet rs = con.createStatement()
                .executeQuery("SELECT COUNT(*) FROM site");
        rs.next();
        return rs.getInt(1);
    }

    /* ================= PENDING MAINTENANCE ================= */
    private static void viewPendingMaintenance(Scanner sc) {
        try (Connection con = getConnection()) {

            String sql =
                "SELECT site_id, owner_id, maintenance_amount " +
                "FROM site WHERE maintenance_paid = false";

            ResultSet rs = con.createStatement().executeQuery(sql);

            while (rs.next()) {
                System.out.println(
                    "Site ID: " + rs.getInt(1) +
                    ", Owner ID: " + rs.getInt(2) +
                    ", Amount: ₹" + rs.getDouble(3)
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* ================= APPROVE UPDATES ================= */
    private static void approveUpdates() {
        try (Connection con = getConnection()) {

            String sql =
                "UPDATE site SET update_status='APPROVED' WHERE update_status='PENDING'";
            int count = con.createStatement().executeUpdate(sql);

            System.out.println(count + " updates approved");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* ================= OWNER MENU ================= */
    private static void ownerMenu(Scanner sc) {
        System.out.print("Enter Owner ID: ");
        int ownerId = sc.nextInt();

        try (Connection con = getConnection()) {

            String sql =
                "SELECT site_id, site_type, length, width, maintenance_amount, maintenance_paid, update_status " +
                "FROM site WHERE owner_id = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, ownerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(
                    "Site ID: " + rs.getInt(1) +
                    ", Type: " + rs.getString(2) +
                    ", Size: " + rs.getInt(3) + "x" + rs.getInt(4) +
                    ", Amount: ₹" + rs.getDouble(5) +
                    ", Paid: " + rs.getBoolean(6) +
                    ", Status: " + rs.getString(7)
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

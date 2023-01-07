package database;

import controller.FarmerPageController;
import functions.functions;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextInputDialog;
import javax.swing.JOptionPane;

/**
 * @author Martin Maina
 */
public class DatabaseFunctions {

    private static final String host = "localhost";
    private static final String port = "3306";
    private static final String url = "jdbc:mysql://" + host + ":" + port + "/digitalfarmers";
    private static final String pass = "";
    private static final String user = "root";
    private static Connection con = null;
    private static Statement stmt = null;

    /**
     * Constructor
     */
    public static void Connection() {

    }

    /**
     * Returns the resultset of a query
     *
     * @param query
     * @return
     */
    public static ResultSet execQuery(String query) {
        ResultSet result;
        try {
            con = DriverManager.getConnection(url, user, pass);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseFunctions.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            stmt = con.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Exception at execQuery:database " + e.getLocalizedMessage());
            return null;
        }
        return result;
    }

    /**
     * execAtion method Returbs true of the query was executed uscceffuly in the
     * databae,
     *
     * @param query
     * @return
     */
    public static boolean execAction(String query) {

        try {
            con = DriverManager.getConnection(url, user, pass);
            stmt = con.createStatement();
            stmt.execute(query);
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execAction:database " + e.getLocalizedMessage());
            return false;
        }
    }

    /**
     *
     * @param username
     * @return
     */
    public static boolean CheckUserExists(String username) {
        boolean exists = false;
        String query = "SELECT * FROM users WHERE username='" + username + "'";

        ResultSet rs = execQuery(query);
        try {
            while (rs.next()) {
                exists = true;
            }
        } catch (SQLException e) {
        }
        return exists;
    }

    public static int getId(String username) {
        int id = 0;
        String sql = "SELECT userid FROM users WHERE username='" + username + "'";
        try {
            con = DriverManager.getConnection(url, user, pass);
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(sql);

            while (rs.next()) {
                id = rs.getInt("userid");
            }
        } catch (SQLException e) {
            System.out.println("GET ID " + e.getLocalizedMessage());
        }
        return id;
    }

    public static String getRole(String username) {
        String role = "";
        String sql = "SELECT role FROM users where username='" + username + "'";

        try {
            con = DriverManager.getConnection(url, user, pass);
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(sql);

            while (rs.next()) {
                role = rs.getString("password");
            }
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return role;
    }

    public static String getRole(int id) {
        String role = "";
        String sql = "SELECT role FROM users where userid='" + id + "'";
        try {
            con = DriverManager.getConnection(url, user, pass);
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery(sql);

            while (rs.next()) {
                role = rs.getString("role");
            }
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
        }

        return role;
    }

    public static void deleteLoggedInUsers() {
        String sql = "DELETE FROM loggedin";
        try {
            con = DriverManager.getConnection(url, user, pass);
            PreparedStatement ps = con.prepareStatement(sql);
            ps.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void loggedIn(String USER, int id) {
        String sql1 = "DELETE  FROM loggedin";
        String sql = USER.equals("CUSTOMER") ? "INSERT INTO loggedin VALUES('CUSTOMER', '" + id + "')" : "INSERT INTO loggedin VALUES('FARMER', '" + id + "')";

        try {
            con = DriverManager.getConnection(url, user, pass);
            PreparedStatement ps = con.prepareStatement(sql);
            PreparedStatement ps1 = con.prepareStatement(sql1);
            ps1.execute();
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean login(String username, String password) {

        boolean isAuthenticated;

        String hashedPassword = functions.generateHash(password);
        isAuthenticated = hashedPassword.equals(verifyPassword(username, password));

        return isAuthenticated;

    }

    public static String verifyPassword(String username, String password) {

        String storedHashedPassword = null;

        try {

            con = DriverManager.getConnection(url, user, pass);

            String query = "select * from users where username ='" + username + "' ";

            PreparedStatement ps = con.prepareStatement(query);

            ResultSet rs = ps.executeQuery(query);

            while (rs.next()) {

                storedHashedPassword = rs.getString("password");

            }

        } catch (SQLException e) {
            functions.dialogPopUp("Error!", e.getMessage());
        }

        return storedHashedPassword;
    }

    public static boolean loginMain(String username, String password) {
        boolean isAuthenticated = false;

        String passw = "";

        try {
            con = DriverManager.getConnection(url, user, pass);
            String query = "SELECT * FROM users WHERE username= '" + username + "'";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery(query);
            while (rs.next()) {
                passw = rs.getString("password");
            }
        } catch (SQLException e) {
            System.out.println("Login Main " + e.getMessage());
        }

        if (passw.equals(functions.generateHash(password))) {
            isAuthenticated = true;
        }
        return isAuthenticated;
    }

    public static boolean addNewCollectionPoint(String name, String address) {
        String query = "INSERT INTO collectionpoints (name, id, address, commision) VALUES('" + name + "', NULL, '" + address + "', 0.0)";
        return execAction(query);
    }

    public static boolean addNewProduct(String name, String price, String quantity, String username) {
        int farmerId = getId(username);
        String query = "INSERT INTO products  VALUES('" + farmerId + "','" + name + "', NULL, '" + price + "', '" + quantity + "')";

        return execAction(query);
    }

    public static int getLastMysqlAutoIncrement() {
        int aI = 0;
        try {
            String query = "SELECT userid AS AI FROM users ORDER BY userid DESC LIMIT 1";
            ResultSet rs = execQuery(query);

            while (rs.next()) {

                aI = rs.getInt("AI");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return aI;
    }

    public static boolean addNewUser(Object role, String password, String username, String name, String tel, String email, String address) {
        int id = getLastMysqlAutoIncrement() + 1;
        String query = "INSERT INTO users (userid, username, role, password) VALUES('" + id + "','" + username + "','" + role.toString() + "', '" + password + "')";
        String query2 = (role.equals("CUSTOMER")) ? "INSERT INTO customers (id, name, phonenumber, address, email) VALUES('" + id + "','" + name + "', '" + tel + "', '" + address + "', '" + email + "') " : "INSERT INTO farmers (id, name, phonenumber, address, email) VALUES('" + id + "','" + name + "', '" + tel + "', '" + address + "', '" + email + "')";
        return (execAction(query) && execAction(query2));
    }

    public static String getPurchasedProductsByProductId(String productId) {
        String purchasedProducts = "";
        String query = "SELECT SUM(quantity) FROM orders WHERE productid='" + productId + "'";
        ResultSet rs = database.DatabaseFunctions.execQuery(query);

        try {
            while (rs.next()) {
                purchasedProducts = rs.getString(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return purchasedProducts;
    }

    public static String checkLoggedIn() {

        String UserLoggedIn = "";
        try {

            String query = "SELECT userid FROM loggedin LIMIT 1";
            ResultSet rs = DatabaseFunctions.execQuery(query);
            while (rs.next()) {
                UserLoggedIn = rs.getString("userid");
            }
            return UserLoggedIn;

        } catch (SQLException ex) {
            Logger.getLogger(FarmerPageController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return UserLoggedIn;
    }

    public static String getFarmerNameByProductId(String string) {
        String farmerName = "N/A";
        String query = "SELECT name FROM farmers WHERE id='" + string + "'";
        ResultSet rs = execQuery(query);
        try {
            while (rs.next()) {
                farmerName = rs.getString(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return farmerName;
    }

    public static String getCustomerNameById(String id) {
        String customerName = "N/A";
        String query = "SELECT name FROM customers WHERE id='" + id + "'";
        ResultSet rs = execQuery(query);
        try {
            while (rs.next()) {
                customerName = rs.getString("name");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return customerName;
    }

    public static String getProductNameById(String id) {
        String productName = "";
        String query = "SELECT productname FROM products WHERE id='" + id + "'";
        ResultSet rs = execQuery(query);
        try {
            while (rs.next()) {
                productName = rs.getString("productname");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return productName;
    }

    public static int getProductPriceById(String string) {
        int price = 0;
        String query = "SELECT price FROM products WHERE id='" + string + "'";
        ResultSet rs = execQuery(query);
        try {
            while (rs.next()) {
                price = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return price;
    }

    public static String getCollectionIdByName(String collectionPoint) {
        String collectionPointId = "";
        String query = "SELECT id FROM collectionpoints WHERE name='" + collectionPoint + "'";

        ResultSet rs = execQuery(query);
        try {
            while (rs.next()) {
                collectionPointId = rs.getString(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return collectionPointId;
    }

    public static void forgetPasswordReset(String username) {
        if (CheckUserExists(username)) {
            int userId = getId(username);
            TextInputDialog tid = new TextInputDialog();
            tid.setHeaderText("Enter new password");
            tid.showAndWait();
            String password1 = tid.getEditor().getText();
            tid.setHeaderText("Repeat the password");
            tid.showAndWait();
            String password2 = tid.getEditor().getText();

            if (password1.equals(password2)) {
                String sql = "UPDATE users SET password='" + functions.generateHash(password2) + "' WHERE userid = '" + userId + "'";
                if (execAction(sql)) {
                    functions.dialogPopUp("Password Reset successfully", "INFORMATION");
                } else {
                    System.out.println("Error could noy update");
                }
            } else {
                System.out.println("Passwords do not match");
            }
        } else {
            System.out.println("User does not exist");
        }
    }

    public static double getTotalSalesByOrderId(String orderId) {
        double totalcost = 0.0;
        String query = "SELECT totalcost FROM orders WHERE orderid='" + orderId + "'";
        ResultSet rs = execQuery(query);
        try {
            while (rs.next()) {
                totalcost = rs.getDouble("totalcost");
            }
        } catch (SQLException e) {
            System.out.println("Error : Method GetTotalSalesByOrderId : " + e.getMessage());
        }
        return totalcost;
    }

    public static String getCollectionIdByOrder(String string) {
        String collectionId = "";
        String query = "SELECT collectionpointid FROM orders WHERE orderid='" + string + "'";
        ResultSet rs = execQuery(query);
        try {
            while (rs.next()) {
                collectionId = rs.getString(1);
            }
        } catch (SQLException e) {
            System.out.println("Error : Method GetCollectionIdByOrder : " + e.getMessage());
        }
        return collectionId;
    }

    /**
     * Function to get the number of items sold for a particular product
     *
     * @param orderId
     * @return
     */
    public static String getTotalProductsSoldByOrderId(String orderId) {
        String productsSold = "";
        String query = "SELECT quantitypurchased FROM orders WHERE orderid='" + orderId + "'";
        ResultSet rs = execQuery(query);
        try {
            while (rs.next()) {
                productsSold = rs.getString(1);
            }
        } catch (SQLException e) {
            System.out.println("Error : Method getTotalProductsSoldByOrderId : " + e.getMessage());
        }
        return productsSold;
    }

    /**
     * Function to get the product id given the order id
     *
     * @param string
     * @return
     */
    public static String getProductIdByOrderId(String string) {
        String productId = "";
        String query = "SELECT productid FROM orders WHERE orderid='" + string + "'";
        ResultSet rs = execQuery(query);
        try {
            while (rs.next()) {
                productId = rs.getString(1);
            }
        } catch (SQLException e) {
            System.out.println("Error : Method getProductIdByOrderId : " + e.getMessage());
        }
        return productId;
    }

    /**
     * Function to get the Farmer Name
     *
     * @param id
     * @return
     */
    public static String getFamerNameById(String id) {
        String farmerName = "N/A";
        String query = "SELECT name FROM farmers WHERE id='" + id + "'";
        ResultSet rs = execQuery(query);
        try {
            while (rs.next()) {
                farmerName = rs.getString("name");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return farmerName;
    }

    /**
     * Method to dump data into database tables from a file (txt)
     *
     * @param table
     * @param file
     */
    public static void insertDataIntoDatabase(String table, FileInputStream file) {
        try {
            DataInputStream in = new DataInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            ArrayList list = new ArrayList();

            while ((strLine = br.readLine()) != null) {
                list.add(strLine);
            }
            Iterator itr;
            for (itr = list.iterator(); itr.hasNext();) {
                String str = itr.next().toString();
                String[] splitSt = str.split(",");
                if (table.equalsIgnoreCase("customers")) {
                    String name = "", id = "", phonenumber = "", address = "", email = "";
                    for (int i = 0; i < splitSt.length; i++) {
                        name = splitSt[0];
                        id = splitSt[1];
                        phonenumber = splitSt[2];
                        address = splitSt[3];
                        email = splitSt[4];
                    }

                    stmt.executeUpdate("INSERT INTO customers VALUES('" + name + "', '" + id + "', '" + phonenumber + "', '" + address + "', '" + email + "')");

                } else if (table.equalsIgnoreCase("farmers")) {
                    String name = "", id = "", phonenumber = "", rating = "", address = "", email = "";
                    for (int i = 0; i < splitSt.length; i++) {
                        name = splitSt[0];
                        address = splitSt[1];
                        id = splitSt[2];
                        rating = splitSt[3];
                        phonenumber = splitSt[4];
                        email = splitSt[5];
                    }

                    stmt.executeUpdate("INSERT INTO farmers VALUES('" + name + "', '" + address + "', '" + id + "', '" + rating + "', '" + phonenumber + "', '" + email + "')");

                } else if (table.equalsIgnoreCase("collectionpoints")) {
                    String name = "", id = "", address = "", commision = "";
                    for (int i = 0; i < splitSt.length; i++) {
                        name = splitSt[0];
                        id = splitSt[1];
                        address = splitSt[3];
                        commision = splitSt[4];
                    }

                    stmt.executeUpdate("INSERT INTO collectionpoints VALUES('" + name + "', '" + id + "', '" + address + "', '" + commision + "')");

                } else if (table.equalsIgnoreCase("products")) {
                    String name = "", id = "", price = "", quantity = "", purchase = "";
                    for (int i = 0; i < splitSt.length; i++) {
                        name = splitSt[0];
                        price = splitSt[1];
                        quantity = splitSt[2];
                        purchase = splitSt[3];
                    }

                    stmt.executeUpdate("INSERT INTO products VALUES('" + getRandomFarmerId() + "', '" + name + "', NULL', '" + price + "', '" + quantity + "', '" + purchase + "')");

                }
            }
        } catch (IOException e) {
            System.out.println("Exception at Method: insertDataIntoDatabase : " + e.getMessage());
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseFunctions.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static int getRandomFarmerId() {
        int rs;
        List<Integer> farmersId = Arrays.asList();
        String query = "SELECT id FROM farmers";
        ResultSet rs1 = execQuery(query);
        try {
            while (rs1.next()) {
                farmersId.add(rs1.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println("Error: getRandomFarmerId : " + e.getMessage());
        }
        Random rand = new Random();
        rs = farmersId.get(rand.nextInt(farmersId.size()));
        return rs;
    }

    public static int getNumberOfOrdersByCollectionPointId(String id) {
        int result = 0;
        String query = "SELECT COUNT() FROM orders WHERE collectionpointid = '" + id + "'";
        ResultSet rs = execQuery(query);

        try {

        } catch (Exception e) {
        }
        return result;
    }
}

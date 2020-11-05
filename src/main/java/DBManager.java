import java.sql.*;

public class DBManager {

    Config cfg = new Config();

    static String DB_URL = "jdbc:mysql://localhost:3306/";
    static String DB_NAME = "seddit";
    static String DB_USER = "admin";
    static String DB_PASSWORD = "admin";

//    String DB_URL = cfg.getProperty("DB_URL");
//    String DB_NAME = cfg.getProperty("DB_NAME");
//    String DB_USER = cfg.getProperty("DB_USER");
//    String DB_PASSWORD = cfg.getProperty("DB_PASSWORD");

    static Connection conn = null;

    DBManager(){
        try {
            conn = getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(DB_URL+DB_NAME, DB_USER, DB_PASSWORD);
        return conn;
    }

    public void closeConnection() throws SQLException {
        if(conn!=null) conn.close();
    }

    public boolean validateLogin(String username, String password) throws SQLException {
        Statement st = conn.createStatement();
        String validateSQL = String.format("SELECT * FROM login WHERE USERNAME='%s' AND PASSWORD='%s';", username, password);
//        String validateSQL = "SELECT * FROM login WHERE USERNAME='a' AND PASSWORD='a';";
        ResultSet resultSet = st.executeQuery(validateSQL);
        return resultSet.next();
    }




}

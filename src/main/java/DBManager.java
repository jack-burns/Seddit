import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBManager {

//    Config cfg = new Config();

    static String DB_URL = "jdbc:mysql://localhost:3306/";
    static String DB_NAME = "seddit";
    static String DB_USER = "admin";
    static String DB_PASSWORD = "admin";

//    String DB_URL = cfg.getProperty("DB_URL");
//    String DB_NAME = cfg.getProperty("DB_NAME");
//    String DB_USER = cfg.getProperty("DB_USER");
//    String DB_PASSWORD = cfg.getProperty("DB_PASSWORD");

    static Connection conn = null;




    public void getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e){
            System.err.println(e);
        }
    }

    public void closeConnection() throws SQLException {

        if(conn!=null) {
            try{
                conn.close();
            } catch (SQLException e) {
                System.out.println("Connection already closed");
            }
        }
    }

    public boolean validateLogin(String username, String password) {
        try {
            Statement st = conn.createStatement();
            String validateSQL = String.format("SELECT * FROM users WHERE USERNAME='%s' AND PASSWORD='%s';", username, password);
//        String validateSQL = "SELECT * FROM login WHERE USERNAME='a' AND PASSWORD='a';";
            ResultSet resultSet = st.executeQuery(validateSQL);
            return resultSet.next();
        } catch (SQLException e){
            return false;
        }
    }

    public boolean postMessage(String title, String content, int userID){
        try{
            Statement st = conn.createStatement();
            //INSERT INTO table_name (column1, column2, column3, ...)
            //VALUES (value1, value2, value3, ...);
            //"SELECT * FROM users WHERE USERNAME='%s' AND PASSWORD='%s';", username, password
            String postMessage = String.format("INSERT INTO posts (title, content, from_user_id, create_timestamp, modified_timestamp) VALUES('%s','%s',%s,'%s',NULL);", title, content, userID, getDate());
            System.out.println("insertion query: " + postMessage);
            st.executeUpdate(postMessage);
            return true;
        } catch (SQLException e){
            return false;
        }
    }

    public int getUerID(String userName){//this is working properly
        try{
            Statement st = conn.createStatement();
            String idQuerying = String.format("SELECT id FROM users WHERE username = '%s';", userName);
            System.out.println(idQuerying);
            ResultSet resultSet = st.executeQuery(idQuerying);
            resultSet.next();
            //System.out.println("result is: " + resultSet.getInt(1));
            return  resultSet.getInt(1);
        } catch (SQLException e){
            return -1;
        }
    }

    public String getDate(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }


}

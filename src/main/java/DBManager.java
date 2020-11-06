import dao.UserPost;

import java.sql.*;
import java.util.ArrayList;

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

    public ArrayList<UserPost> getUserPosts(){
        ArrayList<UserPost> userPostArrayList = new ArrayList<>();
        try{
            Statement st = conn.createStatement();
            String getUserPostsSQL = "SELECT * FROM posts;";
            ResultSet resultSet = st.executeQuery(getUserPostsSQL);
            while(resultSet.next()){
                UserPost userPost = new UserPost(resultSet.getString("title"),
                        resultSet.getString("content"),
                        String.valueOf(resultSet.getInt("from_user_id")),
                        resultSet.getString("create_timestamp"),
                        resultSet.getString("modified_timestamp"));

                userPostArrayList.add(userPost);
            }

        } catch (SQLException e){
        }
        return userPostArrayList;
    }




}

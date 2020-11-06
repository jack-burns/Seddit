import dao.UserPost;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    public ArrayList<UserPost> getUserPosts(int viewCount){
        ArrayList<UserPost> userPostArrayList = new ArrayList<>();
        try{
            Statement st = conn.createStatement();
            String getUserPostsSQL = "SELECT * FROM posts;";
            ResultSet resultSet = st.executeQuery(getUserPostsSQL);
            if(viewCount!=-1) {
                int i = 0;
                while (resultSet.next() && i < viewCount) {
                    UserPost userPost = new UserPost(resultSet.getString("title"),
                            resultSet.getString("content"),
                            String.valueOf(resultSet.getInt("from_user_id")),
                            resultSet.getString("create_timestamp"),
                            resultSet.getString("modified_timestamp"));

                    userPostArrayList.add(userPost);
                    i++;
                }
            } else {
                while (resultSet.next()) {
                    UserPost userPost = new UserPost(resultSet.getString("title"),
                            resultSet.getString("content"),
                            String.valueOf(resultSet.getInt("from_user_id")),
                            resultSet.getString("create_timestamp"),
                            resultSet.getString("modified_timestamp"));

                    userPostArrayList.add(userPost);
                }
            }

            } catch (SQLException e){
        }
        return userPostArrayList;
    }

    public boolean postMessage(String title, String content, int userID){
        try{
            Statement st = conn.createStatement();
            String postMessage = String.format("INSERT INTO posts (title, content, from_user_id, create_timestamp, modified_timestamp) VALUES('%s','%s',%s,'%s',NULL);", title, content, userID, getDate());
            st.executeUpdate(postMessage);// need to use executeUpdate for insertion and deletion
            return true;
        } catch (SQLException e){
            return false;
        }
    }

    public int getUserID(String userName){
        try{
            Statement st = conn.createStatement();
            String idQuerying = String.format("SELECT id FROM users WHERE username = '%s';", userName);
            System.out.println(idQuerying);
            ResultSet resultSet = st.executeQuery(idQuerying);
            resultSet.next(); //.next() because cursor starts before result row 1
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

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import dao.UserPost;

import javax.servlet.http.Part;
import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            String getUserPostsSQL = "SELECT * FROM posts INNER JOIN users ON posts.from_user_id=users.id ORDER BY posts.id DESC;";
            ResultSet resultSet = st.executeQuery(getUserPostsSQL);
            if(viewCount!=-1) {
                int i = 0;
                while (resultSet.next() && i < viewCount) {
                    UserPost userPost = new UserPost(resultSet.getString("title"),
                            resultSet.getString("content"),
                            resultSet.getString("username"),
//                            getUserName(resultSet.getInt("from_user_id")),
//                            String.valueOf(resultSet.getInt("from_user_id")),
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

                    userPostArrayList.add(0,userPost);
                }
            }

            } catch (SQLException e){
        }
        return userPostArrayList;
    }

    public void postMessage(String title, String content, String username, Part filePart){
        UserPost userPost = new UserPost(title, content, username);
        try{
            Statement st = conn.createStatement();
            String postMessage = String.format("INSERT INTO posts (title, content, from_user_id, create_timestamp, modified_timestamp) VALUES ('%s','%s',%s,'%s','%s');",
                    userPost.getTitle(), userPost.getContent(), getUserID(userPost.getUsername()), formatDate(userPost.getCreate_timestamp()), formatDate(userPost.getModified_timestamp()));
            // need to use executeUpdate for insertion and deletion
            st.executeUpdate(postMessage, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()){
                int post_id = generatedKeys.getInt(1);
                postFile(filePart, post_id);
                insertHashtags(content, post_id);
            }
        } catch (SQLException e){}
    }

    public void postFile(Part filePart, int post_id){
        InputStream inputStream = null;
        try {
            inputStream = filePart.getInputStream();
            Statement st = conn.createStatement();
            String postFileSQL = String.format("INSERT INTO uploads (description, data, filename, filesize, filetype, to_post_id) VALUES ('%s', '%s', '%s', '%s', '%s', '%d');",
                    filePart.getName(), inputStream, filePart.getName(), filePart.getSize(), filePart.getContentType(), post_id);
            st.executeUpdate(postFileSQL);// need to use executeUpdate for insertion and deletion
        } catch (IOException | SQLException e){}
    }

    public String formatDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public int getUserID(String userName){
        int userId = -1;
        try{
            Statement st = conn.createStatement();
            String idQuerying = String.format("SELECT id FROM users WHERE username = '%s';", userName);
            System.out.println(idQuerying);
            ResultSet resultSet = st.executeQuery(idQuerying);
            resultSet.next(); //.next() because cursor starts before result row 1
            userId = resultSet.getInt("id");
        } catch (SQLException e){}
        return userId;
    }


    // not used, but ill leave it here for the moment
    public String getUserName(int userId){
        String username = "anonymous";
        try {
            Statement st = conn.createStatement();
            String usernameSQL = String.format("SELECT * FROM users WHERE id= '%d';", userId);
            System.out.println(usernameSQL);
            ResultSet resultSet = st.executeQuery(usernameSQL);
            resultSet.next(); //.next() because cursor starts before result row 1
            username= resultSet.getString("username");
        } catch (SQLException e){}

        return username;


    }

    public void insertHashtags(String content, int post_id){
        List<String> tags = contentHashtagParsing(content);
        if(tags.size() != 0){
            try {
                Statement st = conn.createStatement();
                String hashtagSQL = "INSERT INTO hashtags (tag, to_post_id) VALUES ";
                for(String tag : tags){
                    hashtagSQL = hashtagSQL + String.format("('%s',%d), ", tag, post_id);
                }
                hashtagSQL = hashtagSQL + ";";
                st.executeUpdate(hashtagSQL);
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    private List<String> contentHashtagParsing(String content){
        Pattern pattern = Pattern.compile("#\\w+");
        List<String>  allMatches = new ArrayList<String>();
        Matcher hashtagMatcher = pattern.matcher(content);
        while(hashtagMatcher.find()){
            allMatches.add(hashtagMatcher.group());
        }
        return allMatches;
    }


}

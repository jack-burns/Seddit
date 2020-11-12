//import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import com.google.common.base.Converter;
import com.google.common.hash.Hashing;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import dao.FileAttachment;
import dao.UserPost;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.Part;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
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
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e);
        }
    }

    public void closeConnection() throws SQLException {

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Connection already closed");
            }
        }
    }

    private String hashPassword(String password) {
        return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
    }


    public boolean validateLogin(String username, String password) {
        /*try {
            Statement st = conn.createStatement();
            String validateSQL = String.format("SELECT * FROM users WHERE USERNAME='%s' AND PASSWORD='%s';", username, password);
//        String validateSQL = "SELECT * FROM login WHERE USERNAME='a' AND PASSWORD='a';";
            ResultSet resultSet = st.executeQuery(validateSQL);
            return resultSet.next();
        } catch (SQLException e) {
            return false;
        }*/
        if(username == null || password == null)
            return false;
        // hash password
        String hash = hashPassword(password);


        try {
            // Parse JSON object
            InputStream input = Converter.class.getResourceAsStream("/users.json");

            Scanner sc = new Scanner(input);
            StringBuffer sb = new StringBuffer();
            while(sc.hasNext())
                sb.append(sc.nextLine());

            Object obj = new JSONParser().parse(sb.toString());
            // typecast obj to JSONObject
            JSONObject jo = (JSONObject) obj;
            // get users
            JSONArray array = (JSONArray) jo.get("users");

            // Iterate over users
            Iterator it1 = array.iterator();
            Iterator<Map.Entry> it2;

            boolean userValid = false;
            boolean name = false;
            boolean pass = false;
            while(it1.hasNext()) {
                name = false;
                pass = false;
                it2 = ((Map) it1.next()).entrySet().iterator();
                while(it2.hasNext()){
                    Map.Entry pair = it2.next();
                    if(pair.getKey().equals("username") && pair.getValue().equals(username))
                        name = true;
                    if(pair.getKey().equals("password") && pair.getValue().equals(hash))
                        pass = true;
                    if(name && pass) {
                        userValid = true;
                        break;
                    }
                }
                if(userValid)
                    break;
            }
            return userValid;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<UserPost> getUserPosts(int viewCount) {
        ArrayList<UserPost> userPostArrayList = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
//            String getUserPostsSQL = "SELECT * FROM posts INNER JOIN users ON posts.from_user_id=users.id ORDER BY posts.id DESC;";
            String getUserPostsWithAttachmentSQL = "SELECT * FROM posts RIGHT OUTER JOIN users ON posts.from_user_id=users.id LEFT OUTER JOIN uploads ON posts.id=uploads.to_post_id ORDER BY posts.id DESC;";
            ResultSet resultSet = st.executeQuery(getUserPostsWithAttachmentSQL);
            int i = 0;
            while (resultSet.next()) {
                if (viewCount != -1 && i >= viewCount) {
                    break;
                }
                FileAttachment file = new FileAttachment(resultSet.getInt("uploads.id"),
                        resultSet.getString("filename"),
                        resultSet.getString("description"),
                        resultSet.getString("filesize"),
                        resultSet.getString("filetype"),
                        resultSet.getBlob("data"));
                UserPost userPost = new UserPost(resultSet.getInt("users.id"),
                        resultSet.getString("title"),
                        resultSet.getString("content"),
                        resultSet.getString("username"),
                        resultSet.getString("create_timestamp"),
                        resultSet.getString("modified_timestamp"),
                        file,
                        resultSet.getInt("posts.id"));

                userPostArrayList.add(userPost);
                i++;
            }

        } catch (SQLException e) {}
        return userPostArrayList;
    }

    public UserPost getUserPost(int postId){
        UserPost userPost = new UserPost();
        try{
            Statement st = conn.createStatement();
            String getUserPostsWithAttachmentSQL = "SELECT * FROM posts RIGHT OUTER JOIN users ON posts.from_user_id=users.id LEFT OUTER JOIN uploads ON posts.id=uploads.to_post_id WHERE posts.id="+postId+" ORDER BY posts.id DESC;";
            ResultSet resultSet = st.executeQuery(getUserPostsWithAttachmentSQL);
            resultSet.next();
            FileAttachment file = new FileAttachment(resultSet.getInt("uploads.id"),
                    resultSet.getString("filename"),
                    resultSet.getString("description"),
                    resultSet.getString("filesize"),
                    resultSet.getString("filetype"),
                    resultSet.getBlob("data"));
            userPost = new UserPost(resultSet.getInt("users.id"),
                    resultSet.getString("title"),
                    resultSet.getString("content"),
                    resultSet.getString("username"),
                    resultSet.getString("create_timestamp"),
                    resultSet.getString("modified_timestamp"),
                    file,
                    resultSet.getInt("posts.id"));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return userPost;
    }
    public void postMessage(String title, String content, String username, Part filePart) {
        UserPost userPost = new UserPost(title, content, username);
        try {
            Statement st = conn.createStatement();
            String postMessage = String.format("INSERT INTO posts (title, content, from_user_id, create_timestamp, modified_timestamp) VALUES ('%s','%s',%s,'%s','%s');",
                    userPost.getTitle(), userPost.getContent(), getUserID(userPost.getUsername()), formatDate(userPost.getCreate_timestamp()), formatDate(userPost.getModified_timestamp()));
            // need to use executeUpdate for insertion and deletion
            st.executeUpdate(postMessage, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                int post_id = generatedKeys.getInt(1);
                postFile(filePart, post_id);
                insertHashtags(content, post_id);//added for hashtag parsing
            }
        } catch (SQLException e) {
        }
    }

    public void postFile(Part filePart, int post_id) {
        InputStream inputStream = null;
        try {
            inputStream = filePart.getInputStream();
            Statement st = conn.createStatement();
            String postFileSQL = String.format("INSERT INTO uploads (description, data, filename, filesize, filetype, to_post_id) VALUES ('%s', '%s', '%s', '%s', '%s', '%d');",
                    filePart.getName(), inputStream, filePart.getSubmittedFileName(), filePart.getSize(), filePart.getContentType(), post_id);
            st.executeUpdate(postFileSQL);// need to use executeUpdate for insertion and deletion
        } catch (IOException | SQLException e) {
        }
    }

    public String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public int getUserID(String userName) {
        int userId = -1;
        try {
            Statement st = conn.createStatement();
            String idQuerying = String.format("SELECT id FROM users WHERE username = '%s';", userName);
            //System.out.println(idQuerying);
            ResultSet resultSet = st.executeQuery(idQuerying);
            resultSet.next(); //.next() because cursor starts before result row 1
            userId = resultSet.getInt("id");
        } catch (SQLException e) {
        }
        return userId;
    }


    // not used, but ill leave it here for the moment
    public String getUserName(int userId) {
        String username = "anonymous";
        try {
            Statement st = conn.createStatement();
            String usernameSQL = String.format("SELECT * FROM users WHERE id= '%d';", userId);
            System.out.println(usernameSQL);
            ResultSet resultSet = st.executeQuery(usernameSQL);
            resultSet.next(); //.next() because cursor starts before result row 1
            username = resultSet.getString("username");
        } catch (SQLException e) {
        }

        return username;
    }

    public boolean modifyPost(int postID, String title, String content){
        try
        {
            PreparedStatement statement = conn.prepareStatement("UPDATE posts SET title = ?, content = ?, modified_timestamp =? WHERE id =?"); //there might be a more efficient way to query this
            statement.setString(1, title);
            statement.setString(2, content);
            statement.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
            statement.setInt(4, postID);
            statement.executeUpdate();
            return true;

        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePost(int postID){
        try
        {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM posts WHERE id=?"); //there might be a more efficient way to query this
            statement.setInt(1, postID);
            statement.executeUpdate();
            return true;
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
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
                hashtagSQL = hashtagSQL.substring(0, hashtagSQL.length()-2) + ";";//this is a hacky way of doing it, have to get rid of the last ", " in sql string there is probably a better way
                st.executeUpdate(hashtagSQL);
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    private List<String> contentHashtagParsing(String content){
        Pattern pattern = Pattern.compile("#\\w+");//somehow underscore is readily recognized as alphanumerical
        List<String>  allMatches = new ArrayList<String>();
        Matcher hashtagMatcher = pattern.matcher(content);
        while(hashtagMatcher.find()){
            allMatches.add(hashtagMatcher.group());
        }
        return allMatches;
    }


}

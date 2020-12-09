//import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import com.google.common.base.Converter;
import com.google.common.hash.Hashing;
import dao.FileAttachment;
import dao.UserPost;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBManager {

    Config cfg = new Config();

//    static String DB_URL = "jdbc:mysql://localhost:3306/";
//    static String DB_NAME = "seddit";
//    static String DB_USER = "admin";
//    static String DB_PASSWORD = "admin";

    String DB_URL = cfg.getProperty("DB_URL");
    String DB_NAME = cfg.getProperty("DB_NAME");
    String DB_USER = cfg.getProperty("DB_USER");
    String DB_PASSWORD = cfg.getProperty("DB_PASSWORD");

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

    public String getUserGroup(String username){
        String group = "";

        try {
            // Parse JSON object
            InputStream input = Converter.class.getResourceAsStream("/users.json");

            Scanner sc = new Scanner(input);
            StringBuffer sb = new StringBuffer();
            while (sc.hasNext())
                sb.append(sc.nextLine());

            Object obj = new JSONParser().parse(sb.toString());
            // typecast obj to JSONObject
            JSONObject jo = (JSONObject) obj;
            // get users
            JSONArray array = (JSONArray) jo.get("users");
            for(int i = 0; i<array.size(); i++) {
                JSONObject user = (JSONObject) array.get(i);
                if(username.equals(user.get("username"))) {
                    group = (String) user.get("visibility");
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

            return group;
    }

    public ArrayList<String> getAllGroups(String membership){
        ArrayList<String> group = new ArrayList<>();
        ArrayList<String> group2 = new ArrayList<>();
        group.add(membership);

        try {
            // Parse JSON object
            InputStream input = Converter.class.getResourceAsStream("/membership.json");

            Scanner sc = new Scanner(input);
            StringBuffer sb = new StringBuffer();
            while (sc.hasNext())
                sb.append(sc.nextLine());

            Object obj = new JSONParser().parse(sb.toString());
            // typecast obj to JSONObject
            JSONObject jo = (JSONObject) obj;
            // get users
            JSONArray array = (JSONArray) jo.get("memberships");
            for (Object o : array) {
                JSONObject user = (JSONObject) o;
                if (membership.equals(user.get("name"))) {
                    JSONArray children = (JSONArray) user.get("children");
                    for (Object child : children) {
                        group.add((String) child);
                    }
                }
                else if (group.contains(user.get("name"))) {
                    // removes groups that are children to a group, but have no parent
                    String parent = (String) user.get("parent");
                    if(parent.isEmpty())
                        group.remove(user.get("name"));
                }
            }

            // remove undefined groups
            for(Object o : array) {
                JSONObject user = (JSONObject) o;
                if(group.contains(user.get("name")))
                    group2.add((String) user.get("name"));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return group2;

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
        if (username == null || password == null)
            return false;
        // hash password
        String hash = hashPassword(password);


        try {
            // Parse JSON object
            InputStream input = Converter.class.getResourceAsStream("/users.json");

            Scanner sc = new Scanner(input);
            StringBuffer sb = new StringBuffer();
            while (sc.hasNext())
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
            while (it1.hasNext()) {
                name = false;
                pass = false;
                it2 = ((Map) it1.next()).entrySet().iterator();
                while (it2.hasNext()) {
                    Map.Entry pair = it2.next();
                    if (pair.getKey().equals("username") && pair.getValue().equals(username))
                        name = true;
                    if (pair.getKey().equals("password") && pair.getValue().equals(hash))
                        pass = true;
                    if (name && pass) {
                        userValid = true;
                        break;
                    }
                }
                if (userValid)
                    break;
            }
            return userValid;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getGroupCondition(ArrayList<String> groups){
        StringBuilder condition = new StringBuilder();
        if(groups.size()>0){
            condition.append(" WHERE");
            for(int i = 0; i<groups.size();i++){
                condition.append(" posts.visibility='").append(groups.get(i)).append("'");
                if(i<groups.size()-1){
                    condition.append(" OR");
                }
            }
        }
        return condition.toString();
    }

    public ArrayList<UserPost> getUserPosts(int viewCount, String group) {
        ArrayList<String> visibilities = getAllGroups(group);
        ArrayList<UserPost> userPostArrayList = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
//            String getUserPostsSQL = "SELECT * FROM posts INNER JOIN users ON posts.from_user_id=users.id ORDER BY posts.id DESC;";
            String selectClause = "SELECT * FROM posts INNER JOIN users ON posts.from_user_id=users.id LEFT OUTER JOIN uploads ON posts.id=uploads.to_post_id";
            String whereClause = getGroupCondition(visibilities) + " OR posts.visibility='Public'";
            String orderClause = " ORDER BY posts.id DESC;";
            String getUserPostsWithAttachmentSQL = selectClause+whereClause+orderClause;
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
                        resultSet.getInt("posts.id"),
                        resultSet.getString("visibility"));

                userPostArrayList.add(userPost);
                i++;
            }

        } catch (SQLException e) {
        }
        return userPostArrayList;
    }

    public UserPost getUserPost(int postId) {
        UserPost userPost = new UserPost();
        try {
            Statement st = conn.createStatement();
            String getUserPostsWithAttachmentSQL = "SELECT * FROM posts RIGHT OUTER JOIN users ON posts.from_user_id=users.id LEFT OUTER JOIN uploads ON posts.id=uploads.to_post_id WHERE posts.id=" + postId + " ORDER BY posts.id DESC;";
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
                    resultSet.getInt("posts.id"),
                    resultSet.getString("visibility"));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return userPost;
    }

    public ArrayList<UserPost> searchPost(String username, String usernameToSearchFor, String hashtag, String fromDate, String toDate, String group) {//will search as long as one field is valid
        ArrayList<UserPost> searchResults = new ArrayList<>();

        try {
            Statement searchQuery = conn.createStatement();
            //LEFT JOIN users, posts, hashtags and uploads tables together to accommodate for any combination of search between date range, username(singular) and hashtag(s)
            String selectClause = "SELECT DISTINCT users.id, posts.id, title, content, visibility, username, create_timestamp, modified_timestamp, uploads.id, filename, description, filesize, filetype, data";
            //it seems like any keys with repeated names from tables that are join together need to be in the select clause otherwise the java sql library will see it as syntax error although the workbench works fine
            String fromClause = "FROM (((users LEFT JOIN posts ON users.id = posts.from_user_id) LEFT JOIN hashtags ON posts.id = hashtags.to_post_id) LEFT JOIN uploads ON posts.id = uploads.to_post_id)";
            String whereClause = "WHERE ";

            //composing WHERE clause
            String usernameWhereClause = "";
            String hashtagWhereClause = "";
            String fromDateWhereClause = "";
            String toDateWhereClause = "";
            String groupWhereClause = "";

            // check user is actually in group they want to see
            if(!userPartOfGroup(username, group)) {
                if(!group.equals("Public"))
                    group = "";
            }

            if(!group.equals("")) {
                groupWhereClause = "posts.visibility='" + group + "'";
            }

            if (!usernameToSearchFor.isEmpty()) {
                usernameWhereClause = "username = '" + usernameToSearchFor + "'";
            }

            List<String> hashtags = new ArrayList<>();
            hashtags = contentHashtagParsing(hashtag);
            if (!hashtags.isEmpty()) {
                hashtagWhereClause = "(";
                for (String tag : hashtags) {
                    hashtagWhereClause = hashtagWhereClause + "tag = '" + tag + "' OR ";
                }
                hashtagWhereClause = hashtagWhereClause.substring(0, hashtagWhereClause.length() - 4) + ")";
            }


            if (!fromDate.isEmpty()) {
                fromDateWhereClause = "modified_timestamp >= '" + fromDate + "'"; //maybe we need to handle if user gets from and to mixed up
            }
            if (!toDate.isEmpty()) {
                System.out.println("toDate is empty: " + false + " " + toDate);
                toDateWhereClause = "modified_timestamp <= '" + toDate + "'";
            }

            String[] searchTerms = {usernameWhereClause, hashtagWhereClause, fromDateWhereClause, toDateWhereClause, groupWhereClause};
            boolean atLeastOneWhereTerm = false;

            for (String term : searchTerms) {
                if (!term.isEmpty()) {
                    atLeastOneWhereTerm = true;
                    whereClause = whereClause + term + " AND ";
                }
            }

            //query DB and process results if there is at least a single term in WHERE clause
            if (atLeastOneWhereTerm) {
                whereClause = whereClause.substring(0, whereClause.length() - 5) + ";";
                System.out.println("the where clause is: " + whereClause);
                String sqlQuery = selectClause + "\n" + fromClause + "\n" + whereClause;
                System.out.println(sqlQuery);
                ResultSet resultSet = searchQuery.executeQuery(sqlQuery);//okay, you cannot compose strings inside the parameter
                while (resultSet.next()) {
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
                            resultSet.getInt("posts.id"),
                            resultSet.getString("visibility"));
                    searchResults.add(userPost);
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return searchResults;
    }

    public FileAttachment getFileAttachment(int fileId) {
        FileAttachment fileAttachment = new FileAttachment();

        try {
            PreparedStatement st = conn.prepareStatement("SELECT * FROM uploads WHERE id =?"); //there might be a more efficient way to query this
            st.setInt(1, fileId);
            ResultSet resultSet = st.executeQuery();

            resultSet.next();

            fileAttachment = new FileAttachment(resultSet.getInt("uploads.id"),
                    resultSet.getString("filename"),
                    resultSet.getString("description"),
                    resultSet.getString("filesize"),
                    resultSet.getString("filetype"),
                    resultSet.getBlob("data"));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return fileAttachment;


    }

    public boolean userPartOfGroup(String username, String groupToPostTo) {
        // all groups available from that group
        // get users group
        String userGroup = getUserGroup(username);
        // get all groups under it
        ArrayList<String> userGroups = getAllGroups(userGroup);
        // check if group user wants to post to is in users list of groups
        return userGroups.contains(groupToPostTo);
    }


    public int postMessage(String title, String content, String username, Part filePart, String group) {

        // check user is part of the group
        if(!userPartOfGroup(username, group) && !group.equals("Public"))
            return -1;


        UserPost userPost = new UserPost(title, content, username, group);
        try {
            Statement st = conn.createStatement();
            String postMessage = String.format("INSERT INTO posts (title, content, from_user_id, create_timestamp, modified_timestamp, visibility) VALUES ('%s','%s',%s,'%s','%s','%s');",
                    userPost.getTitle(), userPost.getContent(), getUserID(userPost.getUsername()), formatDate(userPost.getCreate_timestamp()), formatDate(userPost.getModified_timestamp()), userPost.getGroup());
            // need to use executeUpdate for insertion and deletion
            st.executeUpdate(postMessage, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = st.getGeneratedKeys();
            if (generatedKeys.next()) {
                int post_id = generatedKeys.getInt(1);
                if(filePart != null)
                    postFile(filePart, post_id);
                insertHashtags(content, post_id);//added for hashtag parsing
                return post_id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean modifyPost(int postID, String title, String content, String group) { //we need appropriate hashtags updating mechanism here as well

        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE posts SET title = ?, content = ?, modified_timestamp =? , visibility = ? WHERE id =?"); //there might be a more efficient way to query this
            statement.setString(1, title);
            statement.setString(2, content);
            statement.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
            statement.setString(4, group);
            statement.setInt(5, postID);
            statement.executeUpdate();

            statement = conn.prepareStatement("DELETE FROM hashtags WHERE to_post_id=?"); //there might be a more efficient way to query this
            statement.setInt(1, postID);
            statement.executeUpdate();

            insertHashtags(content, postID);

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePost(int postID) {
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM hashtags WHERE to_post_id=?"); //there might be a more efficient way to query this
            statement.setInt(1, postID);
            statement.executeUpdate();
            statement = conn.prepareStatement("DELETE FROM uploads WHERE to_post_id=?");
            statement.setInt(1, postID);
            statement.executeUpdate();
            statement = conn.prepareStatement("DELETE FROM posts WHERE id=?"); //there might be a more efficient way to query this
            statement.setInt(1, postID);
            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean postFile(Part filePart, int post_id) {
        InputStream inputStream = null;
        try {

            inputStream = filePart.getInputStream();
//            byte[] bytes = new byte[1024];
//            for (int i = inputStream.read(); i!=-1; i= inputStream.read()){
//                bytes[i] = (byte) inputStream.read();
//
//            }
            String postFileSQL = "INSERT INTO uploads (description, data, filename, filesize, filetype, to_post_id) VALUES (?,?,?,?,?,?);";
            PreparedStatement st = conn.prepareStatement(postFileSQL);
            st.setString(1, filePart.getName());
            st.setBlob(2, inputStream);
            st.setString(3, filePart.getSubmittedFileName());
            st.setLong(4, filePart.getSize());
            st.setString(5, filePart.getContentType());
            st.setInt(6, post_id);
            st.executeUpdate();// need to use executeUpdate for insertion and deletion
            return true;
        } catch (IOException | SQLException e) {
            return false;
        }
    }

    public boolean modifyFile(Part filePart, int fileID) {
        try {
            PreparedStatement st = conn.prepareStatement("UPDATE uploads SET description = ?, data = ?, filename = ?, filesize = ?, filetype = ?  WHERE id = ?");
            st.setString(1, filePart.getName());
            st.setString(2, String.valueOf(filePart.getInputStream()));
            st.setString(3, filePart.getSubmittedFileName());
            st.setString(4, String.valueOf(filePart.getSize()));
            st.setString(5, filePart.getContentType());
            st.setInt(6, fileID);
            st.executeUpdate();// need to use executeUpdate for insertion and deletion
            return true;
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteFile(int fileID) {
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM uploads WHERE id=?"); //there might be a more efficient way to query this
            statement.setInt(1, fileID);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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

    public void insertHashtags(String content, int post_id) {
        List<String> tags = contentHashtagParsing(content);
        if (tags.size() != 0) {
            try {
                Statement st = conn.createStatement();
                String hashtagSQL = "INSERT INTO hashtags (tag, to_post_id) VALUES ";
                for (String tag : tags) {
                    hashtagSQL = hashtagSQL + String.format("('%s',%d), ", tag, post_id);
                }
                hashtagSQL = hashtagSQL.substring(0, hashtagSQL.length() - 2) + ";";//this is a hacky way of doing it, have to get rid of the last ", " in sql string there is probably a better way
                st.executeUpdate(hashtagSQL);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    //we need to add a method there, one that returns the joint of user, hashtag and posts, then in accordance to which field is empty, we need to add AND to our sql statement

    private List<String> contentHashtagParsing(String content) {
        Pattern pattern = Pattern.compile("#\\w+");//somehow underscore is readily recognized as alphanumerical
        List<String> allMatches = new ArrayList<String>();
        Matcher hashtagMatcher = pattern.matcher(content);
        while (hashtagMatcher.find()) {
            allMatches.add(hashtagMatcher.group());
        }
        return allMatches;
    }


}

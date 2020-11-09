package dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserPost {

    String username;
    String title;
    String content;
    Date create_timestamp;
    Date modified_timestamp;
    int postID;//added to facilitate post modification and deletion

    public UserPost(String title, String content, String username){
        this.username = username;
        this.title = title;
        this.content = content;
        this.create_timestamp = new Date();
        this.modified_timestamp = new Date();
    }

    public UserPost(String title, String content, String username, String create_timestamp, String modified_timestamp, int postID) {
        this.username = username;
        this.title = title;
        this.content = content;
        try {
            this.create_timestamp = new SimpleDateFormat("yyyy-mm-dd").parse(create_timestamp);
            this.modified_timestamp = new SimpleDateFormat("yyyy-mm-dd").parse(modified_timestamp);
        } catch (ParseException e){
            this.create_timestamp = new Date();
            this.modified_timestamp = new Date();
        }
        this.postID = postID;
    }




    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Date getCreate_timestamp() {
        return create_timestamp;
    }

    public Date getModified_timestamp() {
        return modified_timestamp;
    }

    public int getPostID(){return postID;};
}

package dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserPost {

    int userId;
    String username;
    String title;
    String content;
    Date create_timestamp;
    Date modified_timestamp;
    FileAttachment fileAttachment;
    String group;
    int postID;//added to facilitate post modification and deletion

    public UserPost() {
    }


    public UserPost(String title, String content, String username, String group) {
        this.username = username;
        this.title = title;
        this.content = content;
        this.create_timestamp = new Date();
        this.modified_timestamp = new Date();
        this.group = group;
    }


    public UserPost(int userId, String title, String content, String username, String create_timestamp, String modified_timestamp, FileAttachment fileAttachment, int postID, String group) {

//    public UserPost(String title, String content, String username, String create_timestamp, String modified_timestamp, int postID) {
        this.userId = userId;
        this.username = username;
        this.title = title;
        this.content = content;
        this.group = group;
        try {
            this.create_timestamp = new SimpleDateFormat("yyyy-mm-dd").parse(create_timestamp);
            this.modified_timestamp = new SimpleDateFormat("yyyy-mm-dd").parse(modified_timestamp);
        } catch (ParseException e) {
            this.create_timestamp = new Date();
            this.modified_timestamp = new Date();
        }
        this.postID = postID;
        this.fileAttachment = fileAttachment;
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

    public FileAttachment getFileAttachment() {
        return fileAttachment;
    }

    public int getPostID() {
        return postID;
    }

    public String getGroup() {
        return group;
    }

    ;
}

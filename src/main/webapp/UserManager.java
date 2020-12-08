import java.util.ArrayList;

//contains method required for user authentication and provides session data needed for visibility, loaded dynamically
public class UserManager implements userManagerInterface {
    private DBManager db;
    private String username;
    private String membership;

    public UserManager(){
        db = new DBManager();
        db.getConnection();
    }

    @Override
    public boolean authenticateUser(String username, String password) {
        this.username = username;
        return db.validateLogin(username, password);
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getUserVisibility() {
        membership = db.getUserVisibility(username);
        return membership;
    }

    @Override
    public ArrayList<String> getAllVisibilities() {
        return db.getAllVisibilities(membership);
    }
}

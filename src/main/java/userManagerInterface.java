import java.util.ArrayList;

public interface userManagerInterface {
    public boolean authenticateUser(String username, String password);

    public String getUsername();

    public String getUserGroup();

    public ArrayList<String> getAllGroups();
}

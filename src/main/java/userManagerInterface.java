import java.util.ArrayList;

public interface userManagerInterface {
    public boolean authenticateUser(String username, String password);

    public String getUsername();

    public String getUserVisibility();

    public ArrayList<String> getAllVisibilities();
}

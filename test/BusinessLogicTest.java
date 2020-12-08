import dao.UserPost;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class BusinessLogicTest {
    public static DBManager dbManager;

    @BeforeEach
    public void setUp() {
        dbManager = new DBManager();
        dbManager.getConnection();
    }

    // test login
    @Test
    public void testLogin() {
        // test login with valid user
        String username = "a";
        String password = "a";
        boolean result = dbManager.validateLogin(username, password);
        assertTrue(result);

        // test login with invalid user
        username = "invalid";
        password = "invalid";
        result = dbManager.validateLogin(username, password);
        assertFalse(result);
    }

    // test creating posts
    @Test
    public void createPost() {
        // can only create a post if the user is valid
        // a, a is a valid user
        String username = "a";
        String password = "a";
        boolean loginResult = dbManager.validateLogin(username, password);
        int result;
        if(loginResult) {
            result = dbManager.postMessage("testCase", "testCase", "a", null, "admin");
            // check post was successfully created
            if(result != -1) {
                UserPost post = dbManager.getUserPost(result);
                assertEquals("testCase", post.getTitle());
                assertEquals("testCase", post.getContent());
                assertEquals("a", post.getUsername());
                assertEquals("admin", post.getGroup());
            }
        } else{
            assertFalse(loginResult);
        }
    }

    @Test
    public void createPostInvalidGroup() {
        String username = "b";
        int result;
        result = dbManager.postMessage("testGroup", "testGroup", username, null, "admin");
        // check post wasn't created
        if(result == -1) {
            assertTrue(true);
        } else {
            assertFalse(true);
        }
    }

    // updating posts
    // deleting posts

    // Tests needed from assignment specs

    // 1 - a group membership that has undefined group or user
    @Test
    public void undefinedGroup() {

    }


    // 2 - a group definition with a non-existing parent
    @Test
    public void groupWithNoParent() {
        ArrayList<String> groups = dbManager.getAllGroups("admin");
        if(groups.contains("bad"))
            assertFalse(true);
        else
            assertTrue(true);
    }


}

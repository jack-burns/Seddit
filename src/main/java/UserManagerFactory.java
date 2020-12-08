import java.io.*;

public class UserManagerFactory extends Factory{
    private static UserManagerFactory instance;
    final String classNamePath = "/ClassName.txt";
    String className;

    private UserManagerFactory(){
        //getting UserManager's classname from file
        try{
            InputStream reader = UserManagerFactory.class.getClassLoader().getResourceAsStream(classNamePath);
            StringBuilder sb = new StringBuilder();
            int next;
            while((next = reader.read()) != -1){
                sb.append((char)next);
            }
            className = sb.toString();
            System.out.println("class name from file: " + className);
            reader.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    //ensure only one instance of the class is created, for singleton pattern
    public static UserManagerFactory getInstance(){
        if(instance == null){
            synchronized (UserManagerFactory.class){
                if(instance == null)
                    instance = new UserManagerFactory();
            }
        }
        return instance;
    }

    @Override
    public userManagerInterface createUserManager() {
        if(className != null){
            //runtime class loading (not visible to business layer and IDE before then), creating and return an instance of the class
            try{
                ClassLoader myLoader = UserManagerFactory.class.getClassLoader();
                myLoader.loadClass(className);
                Class classRef = Class.forName(className);
                userManagerInterface classInstance = (userManagerInterface) classRef.newInstance();
                return classInstance;
            }
            catch(ClassNotFoundException e){
                e.printStackTrace();
            }
            catch(IllegalAccessException e){
                e.printStackTrace();
            }
            catch(InstantiationException e ){
                e.printStackTrace();
            }

        }
        return null;
    }
}

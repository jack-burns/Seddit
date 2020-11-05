import java.util.Properties;


//DOESNT WORK FOR THE MOMENT, PATH IS NOT PROPERLY SPECIFIED

public class Config {

    Properties configFile;
    public Config()
    {
        configFile = new Properties();
        try {
            configFile.load(this.getClass().getClassLoader().
                    getResourceAsStream("WEB-INF/config.properties"));
        }catch(Exception eta){
            eta.printStackTrace();
        }
    }

    public String getProperty(String key)
    {
        String value = this.configFile.getProperty(key);
        return value;
    }
}

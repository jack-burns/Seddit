package dao;

import java.io.InputStream;
import java.sql.Blob;

public class FileAttachment {

    int id;
    String name;
    String desc;
    String size;
    String type;
    Blob data;

    public FileAttachment(int id, String name, String desc, String size, String type, Blob data){
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.size = size;
        this.type = type;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public Blob getData() {
        return data;
    }
}

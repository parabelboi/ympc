package eu.jmpd.service.impl;

import java.util.WeakHashMap;

import org.apache.olingo.odata2.api.annotation.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.annotation.edm.EdmEntityType;
import org.apache.olingo.odata2.api.annotation.edm.EdmKey;
import org.apache.olingo.odata2.api.annotation.edm.EdmProperty;

@EdmEntityType(namespace = "ServiceRegistry")
@EdmEntitySet(name = "Providers")
public class Provider extends Entity {
    
    public Provider(String id, String name, String description,
        String url)
    {
        super(new WeakHashMap<String, Object>());
        
        setId(id);
        setName(name);
        setDescription(description);
        setUrl(url);
    }
    
    private String              id;
    
    @EdmKey
    @EdmProperty
    private final static String Id          = "Id";
    @EdmProperty
    private final static String Name        = "Name";
    @EdmProperty
    private final static String Description = "Description";
    @EdmProperty
    private final static String Url         = "Url";
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public void setName(String name) {
        put(Name, name);
    }
    
    public void setDescription(String description) {
        put(Description, description);
    }
    
    public void setUrl(String url) {
        put(Url, url);
    }
    
    public String getName() {
        return (String) get(Name);
    }
    
    public String getDescription() {
        return (String) get(Description);
    }
    
    public String getUrl() {
        return (String) get(Url);
    }
    
    @Override
    public String toString() {
        return "Provider{" + "Id=" + getId() + ", Name=" + getName() + ", Description="
                + getDescription() + ", Url=" + getUrl() + '}';
    }
    
}

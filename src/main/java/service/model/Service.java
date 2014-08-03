package service.model;

import org.apache.olingo.odata2.api.annotation.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.annotation.edm.EdmEntityType;
import org.apache.olingo.odata2.api.annotation.edm.EdmKey;
import org.apache.olingo.odata2.api.annotation.edm.EdmProperty;
import org.apache.olingo.odata2.api.annotation.edm.EdmType;

@EdmEntityType(name = "Service")
@EdmEntitySet(name = "Services")
public class Service {
    @EdmKey
    @EdmProperty(name = "Id", type = EdmType.STRING)
    private String Id;
    @EdmProperty(name = "Name")
    private String Name;
    @EdmProperty(name = "Url")
    private String Url;
    
    public Service() {
        super();
    }
    
    public String getId() {
        return Id;
    }
    
    public void setId(final String id) {
        Id = id;
    }
    
    public String getName() {
        return Name;
    }
    
    public void setName(String name) {
        Name = name;
    }
    
    public String getUrl() {
        return Url;
    }
    
    public void setUrl(String url) {
        Url = url;
    }
}

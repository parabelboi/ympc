package service.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.olingo.odata2.api.annotation.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.annotation.edm.EdmEntityType;
import org.apache.olingo.odata2.api.annotation.edm.EdmKey;
import org.apache.olingo.odata2.api.annotation.edm.EdmNavigationProperty;
import org.apache.olingo.odata2.api.annotation.edm.EdmProperty;

@EdmEntityType
@EdmEntitySet
public class Provider extends Entity {
    @EdmKey
    @EdmProperty
    private String              id;
    @EdmProperty
    private String              name;
    @EdmNavigationProperty
    private List<Service>       services = new ArrayList<Service>();
    
    /** optional getter and setter */
    private final static String Id       = "Id";
    private final static String Name     = "Name";
    private final static String Services = "Services";
    
    public Provider(final String id, final String name, final List<Service> services)
    {
        super(new HashMap<String, Object>());
        this.id = id;
        put(Id, id);
        put(Name, name);
        put(Services, services);
    }
}

package service.model;

import java.util.HashMap;

import org.apache.olingo.odata2.api.annotation.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.annotation.edm.EdmEntityType;
import org.apache.olingo.odata2.api.annotation.edm.EdmKey;
import org.apache.olingo.odata2.api.annotation.edm.EdmNavigationProperty;
import org.apache.olingo.odata2.api.annotation.edm.EdmProperty;

@EdmEntityType
@EdmEntitySet
public class Service extends Entity {
    @EdmKey
    @EdmProperty
    private String              id;
    @EdmProperty
    private String              name;
    @EdmNavigationProperty
    private Provider            provider;
    
    private final static String Id         = "Id";
    private final static String Name       = "Name";
    private final static String Provider   = "Provider";
    private final static String ServiceUrl = "ServiceUrl";
    
    /** optional getter and setter */
    
    public Service(
        final String id,
        final String name,
        final String provider,
        final String serviceUrl)
    {
        super(new HashMap<String, Object>());
        this.id = id;
        put(Id, id);
        put(Name, name);
        put(Provider, provider);
        put(ServiceUrl, name);
    }
    
}

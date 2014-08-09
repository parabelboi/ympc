package service.model;

import org.apache.olingo.odata2.api.annotation.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.annotation.edm.EdmEntityType;
import org.apache.olingo.odata2.api.annotation.edm.EdmNavigationProperty;
import org.apache.olingo.odata2.api.annotation.edm.EdmProperty;

@EdmEntityType(name = "Service")
@EdmEntitySet(name = "Services")
public class Service extends Base {
    @EdmProperty(name = "Url")
    private String Url;
    @EdmNavigationProperty(name = "Provider", association = "ProviderOfService")
    private Provider Provider;
    
    public Service() {
        super();
    }
}

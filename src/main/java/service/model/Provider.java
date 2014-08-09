package service.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.olingo.odata2.api.annotation.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.annotation.edm.EdmEntityType;
import org.apache.olingo.odata2.api.annotation.edm.EdmNavigationProperty;

@EdmEntityType(name = "Provider")
@EdmEntitySet(name = "Providers")
public class Provider extends Base {
    @EdmNavigationProperty(name = "Services", toType = Service.class, association = "ServicesOfProvider")
    private List<Service> services = new ArrayList<Service>();
    
    public Provider() {
        super();
    }
}

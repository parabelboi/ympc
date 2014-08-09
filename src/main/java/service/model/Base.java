package service.model;

import org.apache.olingo.odata2.api.annotation.edm.EdmEntityType;
import org.apache.olingo.odata2.api.annotation.edm.EdmKey;
import org.apache.olingo.odata2.api.annotation.edm.EdmProperty;
import org.apache.olingo.odata2.api.annotation.edm.EdmType;

@EdmEntityType(name = "Base")
public class Base {
    @EdmKey
    @EdmProperty(name = "Id", type = EdmType.STRING)
    private String Id;
    @EdmProperty(name = "Name")
    private String Name;
    
    @Override
    public int hashCode() {
        return Id.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass()
                && (Id == ((Base) obj).Id || Id.equals(((Base) obj).Id));
    }
}

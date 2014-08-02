package eu.jmpd.service.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties.ODataEntityProviderPropertiesBuilder;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.exception.ODataNotFoundException;
import org.apache.olingo.odata2.api.exception.ODataNotImplementedException;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.processor.ODataSingleProcessor;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetEntityUriInfo;

public class JmpdODataSingleProcessor extends ODataSingleProcessor {
    private final JmpdDataStore dataStore = new JmpdDataStore();
    
    @Override
    public ODataResponse readEntity(
        final GetEntityUriInfo uriInfo,
        final String contentType)
        throws ODataException
    {
        
        if (uriInfo.getNavigationSegments().size() == 0) {
            EdmEntitySet entitySet = uriInfo.getStartEntitySet();
            
            if (JmpdEdmProvider.ENTITY_SET_NAME_SERVICES.equals(entitySet
                .getName()))
            {
                final String id = uriInfo.getKeyPredicates().get(0).getLiteral();
                Map<String, Object> data = dataStore.getService(id);
                
                if (data != null) {
                    URI serviceRoot = getContext().getPathInfo()
                        .getServiceRoot();
                    ODataEntityProviderPropertiesBuilder propertiesBuilder = EntityProviderWriteProperties
                        .serviceRoot(serviceRoot);
                    
                    return EntityProvider.writeEntry(contentType, entitySet,
                        data, propertiesBuilder.build());
                }
            } else
                if (JmpdEdmProvider.ENTITY_SET_NAME_PROVIDERS
                    .equals(entitySet.getName()))
                {
                    final String id = uriInfo.getKeyPredicates().get(0).getLiteral();
                    Provider data = dataStore.getProvider(id);
                    
                    if (data != null) {
                        URI serviceRoot = getContext().getPathInfo()
                            .getServiceRoot();
                        ODataEntityProviderPropertiesBuilder propertiesBuilder = EntityProviderWriteProperties
                            .serviceRoot(serviceRoot);
                        
                        return EntityProvider.writeEntry(contentType, entitySet,
                            data, propertiesBuilder.build());
                    }
                }
            
            throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
            
        } else
            if (uriInfo.getNavigationSegments().size() == 1) {
                // navigation first level, simplified example for illustration
                // purposes only
                EdmEntitySet entitySet = uriInfo.getTargetEntitySet();
                if (JmpdEdmProvider.ENTITY_SET_NAME_PROVIDERS.equals(entitySet
                    .getName()))
                {
                    final String providerId = uriInfo.getKeyPredicates().get(0).getLiteral();
                    return EntityProvider.writeEntry(
                        contentType,
                        uriInfo.getTargetEntitySet(),
                        dataStore.getProvider(providerId),
                        EntityProviderWriteProperties.serviceRoot(
                            getContext().getPathInfo().getServiceRoot())
                            .build());
                }
                
                throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
            }
        
        throw new ODataNotImplementedException();
    }
    
    public ODataResponse readEntitySet(GetEntitySetUriInfo uriInfo,
        String contentType) throws ODataException
    {
        
        EdmEntitySet entitySet;
        
        if (uriInfo.getNavigationSegments().size() == 0) {
            entitySet = uriInfo.getStartEntitySet();
            
            if (JmpdEdmProvider.ENTITY_SET_NAME_SERVICES.equals(entitySet
                .getName()))
                return EntityProvider.writeFeed(
                    contentType,
                    entitySet,
                    new ArrayList<Map<String, Object>>(dataStore.getServices()),
                    EntityProviderWriteProperties.serviceRoot(
                        getContext().getPathInfo().getServiceRoot())
                        .build());
            else
                if (JmpdEdmProvider.ENTITY_SET_NAME_PROVIDERS
                    .equals(entitySet.getName()))
                    return EntityProvider.writeFeed(
                        contentType,
                        entitySet,
                        new ArrayList<Map<String, Object>>(dataStore.getProviders()),
                        EntityProviderWriteProperties.serviceRoot(
                            getContext().getPathInfo().getServiceRoot())
                            .build());
            
            throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
        } else
            // TODO: implement this for other sizes as well
            if (uriInfo.getNavigationSegments().size() == 1) {
                // navigation first level, simplified example for illustration
                // purposes only
                entitySet = uriInfo.getTargetEntitySet();
                
                if (JmpdEdmProvider.ENTITY_SET_NAME_SERVICES.equals(entitySet
                    .getName()))
                {
                    final String serviceId = uriInfo.getKeyPredicates().get(0).getLiteral();
                    
                    List<Map<String, Object>> services = new ArrayList<Map<String, Object>>();
                    services.add(dataStore.getService(serviceId));
                    
                    return EntityProvider.writeFeed(
                        contentType,
                        entitySet,
                        services,
                        EntityProviderWriteProperties.serviceRoot(
                            getContext().getPathInfo().getServiceRoot())
                            .build());
                }
                
                throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
            }
        
        throw new ODataNotImplementedException();
    }
    
}

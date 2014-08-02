package eu.jmpd.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.olingo.odata2.annotation.processor.core.DataSourceProcessor;
import org.apache.olingo.odata2.annotation.processor.core.datasource.DataSource;
import org.apache.olingo.odata2.annotation.processor.core.datasource.ValueAccess;

import service.model.Provider;
import service.model.Service;

public class JmpdDataStore extends DataSourceProcessor {
    
    public JmpdDataStore(final DataSource dataSource, final ValueAccess valueAccess) {
        super(dataSource, valueAccess);
        cache_service = new WeakHashMap<String, Service>();
        cache_provider = new WeakHashMap<String, Provider>();
        cache_service_per_provider = new WeakHashMap<Provider, Map<String, Service>>();
        cache_provider_of_service = new WeakHashMap<Service, Provider>();
        backend_service = null;
        backend_provider = null;
        backend_service_per_provider = null;
        backend_provider_of_service = null;
        init();
    }
    
    private final Map<String, Service>                cache_service;
    private final Map<String, Provider>               cache_provider;
    private final Map<Service, Provider>              cache_provider_of_service;
    private final Map<Provider, Map<String, Service>> cache_service_per_provider;
    private final Map<String, Service>                backend_service;
    private final Map<String, Provider>               backend_provider;
    private final Map<Service, Provider>              backend_provider_of_service;
    private final Map<Provider, Map<String, Service>> backend_service_per_provider;
    
    // Data accessors
    public Service getService(final String id) {
        final Service cachedService = cache_service.get(id);
        if (cachedService != null)
            return cachedService;
        if (backend_service != null)
            return backend_service.get(id);
        return null;
    }
    
    // TODO: read from backend
    public Collection<Service> getServices() {
        return cache_service.values();
    }
    
    // TODO: read from backend
    public Collection<Provider> getProviders() {
        return cache_provider.values();
    }
    
    public Provider getProvider(final String id) {
        final Provider cachedProvider = cache_provider.get(id);
        if (cachedProvider != null)
            return cachedProvider;
        if (backend_provider != null)
            return backend_provider.get(id);
        return null;
    }
    
    public Collection<Service> getServicesFrom(final String providerId) {
        final Provider provider = getProvider(providerId);
        if (provider != null) {
            final Map<String, Service> services = cache_service_per_provider
                .get(provider);
            if (services != null)
                return services.values();
            if (backend_service_per_provider != null) {
                final Map<String, Service> services2 = backend_service_per_provider
                    .get(provider);
                if (services2 != null)
                    return services2.values();
            }
        }
        return null;
    }
    
    public Provider getProviderFor(final String serviceId) {
        final Service service = getService(serviceId);
        if (service != null) {
            final Provider provider = cache_provider_of_service.get(service);
            if (provider != null)
                return provider;
            if (backend_provider_of_service != null)
                return backend_provider_of_service.get(service);
        }
        return null;
    }
    
    // TODO: read data from backing store
    // for now add an entry
    private void init() {
        
        final String serviceid = "serviceid://1";
        final String providerid = "providerid://1";
        
        final Service service = new Service(serviceid, "jmpd service", providerid, "http://127.0.0.1:8080/");
        
        final List<Service> servicelist = new ArrayList<Service>();
        servicelist.add(service);
        
        final Provider provider = new Provider(providerid, "jmpd provider", servicelist);
        
        final Map<String, Service> services = new WeakHashMap<String, Service>();
        services.put(serviceid, service);
        
        cache_service.put(serviceid, service);
        cache_provider.put(providerid, provider);
        cache_service_per_provider.put(provider, services);
        cache_provider_of_service.put(service, provider);
    }
}

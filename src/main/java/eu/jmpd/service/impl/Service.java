package eu.jmpd.service.impl;

import java.util.WeakHashMap;

import org.apache.olingo.odata2.api.annotation.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.annotation.edm.EdmEntityType;
import org.apache.olingo.odata2.api.annotation.edm.EdmKey;
import org.apache.olingo.odata2.api.annotation.edm.EdmProperty;

@EdmEntityType(namespace = "ServiceRegistry")
@EdmEntitySet(name = "Services")
public class Service extends Entity {

	public Service(final String id, final String name,
			final String description, final String providerId,
			final String serviceUrl, final String resourceUrl) {
		
		super(new WeakHashMap<String, Object>());

		setId(id);
		setName(name);
		setDescription(description);
		setProviderId(providerId);
		setServiceUrl(serviceUrl);
		setResourceUrl(resourceUrl);
	}

	private String id;

	@EdmKey
	@EdmProperty
	private final static String Id = "Id";
	@EdmProperty
	private final static String Name = "Name";
	@EdmProperty
	private final static String Description = "Description";
	@EdmProperty
	private final static String ProviderId = "ProviderId";
	@EdmProperty
	private final static String ServiceUrl = "ServiceUrl";
	@EdmProperty
	private final static String ResourceUrl = "ResourceUrl";

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

	public void setProviderId(String providerId) {
		put(ProviderId, providerId);
	}

	public void setServiceUrl(String serviceUrl) {
		put(ServiceUrl, serviceUrl);
	}

	public void setResourceUrl(String resourceUrl) {
		put(ResourceUrl, resourceUrl);
	}

	public String getName() {
		return (String) get(Name);
	}

	public String getDescription() {
		return (String) get(Description);
	}

	public String getProviderId() {
		return (String) get(ProviderId);
	}

	public String getServiceUrl() {
		return (String) get(ServiceUrl);
	}

	public String getResourceUrl() {
		return (String) get(ResourceUrl);
	}

	@Override
	public String toString() {
		return "Service{" +
					"Id=" + getId() +
					", Name=" + getName() +
					", Description=" + getDescription() +
					", ProviderId="	+ getProviderId() +
					", ServiceUrl=" + getServiceUrl() +
					", ResourceUrl=" + getResourceUrl() +
				'}';
	}

}

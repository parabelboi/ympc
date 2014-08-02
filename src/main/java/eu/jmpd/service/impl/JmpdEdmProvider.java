package eu.jmpd.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.olingo.odata2.api.edm.EdmConcurrencyMode;
import org.apache.olingo.odata2.api.edm.EdmMultiplicity;
import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.EdmTargetPath;
import org.apache.olingo.odata2.api.edm.FullQualifiedName;
import org.apache.olingo.odata2.api.edm.provider.Association;
import org.apache.olingo.odata2.api.edm.provider.AssociationEnd;
import org.apache.olingo.odata2.api.edm.provider.AssociationSet;
import org.apache.olingo.odata2.api.edm.provider.AssociationSetEnd;
import org.apache.olingo.odata2.api.edm.provider.ComplexProperty;
import org.apache.olingo.odata2.api.edm.provider.CustomizableFeedMappings;
import org.apache.olingo.odata2.api.edm.provider.EdmProvider;
import org.apache.olingo.odata2.api.edm.provider.EntityContainer;
import org.apache.olingo.odata2.api.edm.provider.EntityContainerInfo;
import org.apache.olingo.odata2.api.edm.provider.EntitySet;
import org.apache.olingo.odata2.api.edm.provider.EntityType;
import org.apache.olingo.odata2.api.edm.provider.Facets;
import org.apache.olingo.odata2.api.edm.provider.Key;
import org.apache.olingo.odata2.api.edm.provider.NavigationProperty;
import org.apache.olingo.odata2.api.edm.provider.Property;
import org.apache.olingo.odata2.api.edm.provider.PropertyRef;
import org.apache.olingo.odata2.api.edm.provider.Schema;
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty;
import org.apache.olingo.odata2.api.exception.ODataException;

/**
 * here way to much noise in this file
 * another approach is needed
 * @author ckrafft
 *
 */
public class JmpdEdmProvider extends EdmProvider {

	static final String ENTITY_SET_NAME_PROVIDERS = "Providers";
	static final String ENTITY_NAME_PROVIDER = "Provider";

	static final String ENTITY_SET_NAME_SERVICES = "Services";
	static final String ENTITY_NAME_SERVICE = "Service";

	private static final String NAMESPACE = "service.model";

	private static final FullQualifiedName ENTITY_TYPE_1_1 = new FullQualifiedName(
			NAMESPACE, ENTITY_NAME_SERVICE);
	private static final FullQualifiedName ENTITY_TYPE_1_2 = new FullQualifiedName(
			NAMESPACE, ENTITY_NAME_PROVIDER);

	private static final FullQualifiedName ASSOCIATION_SERVICE_PROVIDER = new FullQualifiedName(
			NAMESPACE, "Service_Provider_Provider_Services");

	private static final String ROLE_1_1 = "Service_Provider";
	private static final String ROLE_1_2 = "Provider_Services";

	private static final String ENTITY_CONTAINER = "ODataServicesEntityContainer";

	private static final String ASSOCIATION_SET = "Services_Providers";

	@Override
	public List<Schema> getSchemas() throws ODataException {
		final List<Schema> schemas = new ArrayList<Schema>();

		final Schema schema = new Schema();
		schema.setNamespace(NAMESPACE);

		final List<EntityType> entityTypes = new ArrayList<EntityType>();
		entityTypes.add(getEntityType(ENTITY_TYPE_1_1));
		entityTypes.add(getEntityType(ENTITY_TYPE_1_2));
		schema.setEntityTypes(entityTypes);

		/*
		final List<ComplexType> complexTypes = new ArrayList<ComplexType>();
		complexTypes.add(getComplexType(COMPLEX_TYPE));
		schema.setComplexTypes(complexTypes);
		*/

		final List<Association> associations = new ArrayList<Association>();
		associations.add(getAssociation(ASSOCIATION_SERVICE_PROVIDER));
		schema.setAssociations(associations);

		final List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
		final EntityContainer entityContainer = new EntityContainer();
		entityContainer.setName(ENTITY_CONTAINER).setDefaultEntityContainer(
				true);

		final List<EntitySet> entitySets = new ArrayList<EntitySet>();
		entitySets
				.add(getEntitySet(ENTITY_CONTAINER, ENTITY_SET_NAME_SERVICES));
		entitySets
				.add(getEntitySet(ENTITY_CONTAINER, ENTITY_SET_NAME_PROVIDERS));
		entityContainer.setEntitySets(entitySets);

		final List<AssociationSet> associationSets = new ArrayList<AssociationSet>();
		associationSets.add(getAssociationSet(ENTITY_CONTAINER,
				ASSOCIATION_SERVICE_PROVIDER, ENTITY_SET_NAME_PROVIDERS,
				ROLE_1_2));
		entityContainer.setAssociationSets(associationSets);

		entityContainers.add(entityContainer);
		schema.setEntityContainers(entityContainers);

		schemas.add(schema);

		return schemas;
	}

	@Override
	public EntityContainerInfo getEntityContainerInfo(String name)
			throws ODataException {
		if (name == null || "ODataServicesEntityContainer".equals(name)) {
			return new EntityContainerInfo().setName(
					"ODataServicesEntityContainer").setDefaultEntityContainer(
					true);
		}

		return null;
	}

	@Override
	public EntityType getEntityType(final FullQualifiedName edmFQName)
			throws ODataException {
		if (NAMESPACE.equals(edmFQName.getNamespace())) {

			if (ENTITY_TYPE_1_1.getName().equals(edmFQName.getName())) {

				// Properties
				final List<Property> properties = new ArrayList<Property>();
				properties.add(new SimpleProperty().setName("Id")
						.setType(EdmSimpleTypeKind.Int32)
						.setFacets(new Facets().setNullable(false)));
				properties
						.add(new SimpleProperty()
								.setName("Name")
								.setType(EdmSimpleTypeKind.String)
								.setFacets(
										new Facets().setNullable(false)
												.setMaxLength(100))
								.setCustomizableFeedMappings(
										new CustomizableFeedMappings()
												.setFcTargetPath(EdmTargetPath.SYNDICATION_TITLE)));
				properties.add(new SimpleProperty().setName("ProviderId")
						.setType(EdmSimpleTypeKind.Int32));
				properties.add(new SimpleProperty().setName("ResourceUrl")
						.setType(EdmSimpleTypeKind.String));
				properties.add(new SimpleProperty().setName("ServiceUrl")
						.setType(EdmSimpleTypeKind.String));
				properties.add(new SimpleProperty().setName("Description")
						.setType(EdmSimpleTypeKind.String));
				properties
						.add(new SimpleProperty()
								.setName("Updated")
								.setType(EdmSimpleTypeKind.DateTime)
								.setFacets(
										new Facets()
												.setNullable(false)
												.setConcurrencyMode(
														EdmConcurrencyMode.Fixed))
								.setCustomizableFeedMappings(
										new CustomizableFeedMappings()
												.setFcTargetPath(EdmTargetPath.SYNDICATION_UPDATED)));

				// Navigation Properties
				final List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
				navigationProperties.add(new NavigationProperty()
						.setName("Provider")
						.setRelationship(ASSOCIATION_SERVICE_PROVIDER)
						.setFromRole(ROLE_1_1).setToRole(ROLE_1_2));

				// Key
				final List<PropertyRef> keyProperties = new ArrayList<PropertyRef>();
				keyProperties.add(new PropertyRef().setName("Id"));
				Key key = new Key().setKeys(keyProperties);

				return new EntityType().setName(ENTITY_TYPE_1_1.getName())
						.setProperties(properties).setKey(key)
						.setNavigationProperties(navigationProperties);

			} else if (ENTITY_TYPE_1_2.getName().equals(edmFQName.getName())) {

				// Properties
				final List<Property> properties = new ArrayList<Property>();
				properties.add(new SimpleProperty().setName("Id")
						.setType(EdmSimpleTypeKind.Int32)
						.setFacets(new Facets().setNullable(false)));
				properties
						.add(new SimpleProperty()
								.setName("Name")
								.setType(EdmSimpleTypeKind.String)
								.setFacets(
										new Facets().setNullable(false)
												.setMaxLength(100))
								.setCustomizableFeedMappings(
										new CustomizableFeedMappings()
												.setFcTargetPath(EdmTargetPath.SYNDICATION_TITLE)));
				properties.add(new ComplexProperty().setName("Url").setType(
						new FullQualifiedName(NAMESPACE, "Url")));
				properties
						.add(new ComplexProperty().setName("Description")
								.setType(
										new FullQualifiedName(NAMESPACE,
												"Description")));
				properties
						.add(new SimpleProperty()
								.setName("Updated")
								.setType(EdmSimpleTypeKind.DateTime)
								.setFacets(
										new Facets()
												.setNullable(false)
												.setConcurrencyMode(
														EdmConcurrencyMode.Fixed))
								.setCustomizableFeedMappings(
										new CustomizableFeedMappings()
												.setFcTargetPath(EdmTargetPath.SYNDICATION_UPDATED)));

				// Navigation Properties
				final List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
				navigationProperties.add(new NavigationProperty()
						.setName("Services")
						.setRelationship(ASSOCIATION_SERVICE_PROVIDER)
						.setFromRole(ROLE_1_2).setToRole(ROLE_1_1));

				// Key
				final List<PropertyRef> keyProperties = new ArrayList<PropertyRef>();
				keyProperties.add(new PropertyRef().setName("Id"));
				final Key key = new Key().setKeys(keyProperties);

				return new EntityType().setName(ENTITY_TYPE_1_2.getName())
						.setProperties(properties).setHasStream(true)
						.setKey(key)
						.setNavigationProperties(navigationProperties);
			}
		}

		return null;
	}

	@Override
	public Association getAssociation(final FullQualifiedName edmFQName)
			throws ODataException {
		if (NAMESPACE.equals(edmFQName.getNamespace())) {
			if (ASSOCIATION_SERVICE_PROVIDER.getName().equals(
					edmFQName.getName())) {
				return new Association()
						.setName(ASSOCIATION_SERVICE_PROVIDER.getName())
						.setEnd1(
								new AssociationEnd().setType(ENTITY_TYPE_1_1)
										.setRole(ROLE_1_1)
										.setMultiplicity(EdmMultiplicity.MANY))
						.setEnd2(
								new AssociationEnd().setType(ENTITY_TYPE_1_2)
										.setRole(ROLE_1_2)
										.setMultiplicity(EdmMultiplicity.ONE));
			}
		}
		return null;
	}

	@Override
	public EntitySet getEntitySet(final String entityContainer,
			final String name) throws ODataException {
		if (ENTITY_CONTAINER.equals(entityContainer)) {
			if (ENTITY_SET_NAME_SERVICES.equals(name)) {
				return new EntitySet().setName(name).setEntityType(
						ENTITY_TYPE_1_1);
			} else if (ENTITY_SET_NAME_PROVIDERS.equals(name)) {
				return new EntitySet().setName(name).setEntityType(
						ENTITY_TYPE_1_2);
			}
		}
		return null;
	}

	@Override
	public AssociationSet getAssociationSet(final String entityContainer,
			final FullQualifiedName association,
			final String sourceEntitySetName, final String sourceEntitySetRole)
			throws ODataException {
		if (ENTITY_CONTAINER.equals(entityContainer)) {
			if (ASSOCIATION_SERVICE_PROVIDER.equals(association)) {
				return new AssociationSet()
						.setName(ASSOCIATION_SET)
						.setAssociation(ASSOCIATION_SERVICE_PROVIDER)
						.setEnd1(
								new AssociationSetEnd()
										.setRole(ROLE_1_2)
										.setEntitySet(ENTITY_SET_NAME_PROVIDERS))
						.setEnd2(
								new AssociationSetEnd().setRole(ROLE_1_1)
										.setEntitySet(ENTITY_SET_NAME_SERVICES));
			}
		}
		return null;
	}

}

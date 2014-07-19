package net.jmpd.service.registry;

import net.jmpd.service.auth.AuthService;

import org.apache.olingo.odata2.annotation.processor.api.AnnotationServiceFactory;
import org.apache.olingo.odata2.api.ODataCallback;
import org.apache.olingo.odata2.api.ODataDebugCallback;
import org.apache.olingo.odata2.api.ODataService;
import org.apache.olingo.odata2.api.ODataServiceFactory;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.processor.ODataErrorCallback;
import org.apache.olingo.odata2.api.processor.ODataErrorContext;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceRegistry extends ODataServiceFactory {

	/**
	 * Instance holder for all annotation relevant instances which should be
	 * used as singleton instances within the ODataApplication (ODataService)
	 */
	private static class AnnotationInstances {
		final static String MODEL_PACKAGE = "net.jmpd.service.model";
		final static ODataService ODATA_SERVICE;
		final static AuthService AUTH_SERVICE;

		static {
			AUTH_SERVICE = new AuthService();
			try {
				ODATA_SERVICE = AnnotationServiceFactory
						.createAnnotationService(MODEL_PACKAGE);
			} catch (ODataApplicationException ex) {
				throw new RuntimeException(
						"Exception during sample data generation.", ex);
			} catch (ODataException ex) {
				throw new RuntimeException(
						"Exception during data source initialization generation.",
						ex);
			}
		}
	}

	@Override
	public ODataService createService(final ODataContext context)
			throws ODataException {
		// Edm via Annotations and ListProcessor via AnnotationDS with
		// AnnotationsValueAccess
		return AnnotationInstances.ODATA_SERVICE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ODataCallback> T getCallback(
			final Class<? extends ODataCallback> callbackInterface) {
		return (T) (callbackInterface
				.isAssignableFrom(ScenarioErrorCallback.class) ? new ScenarioErrorCallback()
				: callbackInterface.isAssignableFrom(ODataDebugCallback.class) ? new ScenarioDebugCallback()
						: super.getCallback(callbackInterface));
	}

	/*
	 * Helper classes and methods
	 */
	private final class ScenarioDebugCallback implements ODataDebugCallback {

		@Override
		public boolean isDebugEnabled() {
			return true;
		}
	}

	private class ScenarioErrorCallback implements ODataErrorCallback {
		private final Logger LOG = LoggerFactory
				.getLogger(ScenarioErrorCallback.class);

		@Override
		public ODataResponse handleError(final ODataErrorContext context)
				throws ODataApplicationException {
			if (context.getHttpStatus() == HttpStatusCodes.INTERNAL_SERVER_ERROR) {
				LOG.error("Internal Server Error", context.getException());
			}

			return EntityProvider.writeErrorDocument(context);
		}
	}

}

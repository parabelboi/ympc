package eu.jmpd.service.impl;

import org.apache.olingo.odata2.annotation.processor.api.AnnotationServiceFactory;
import org.apache.olingo.odata2.api.ODataService;
import org.apache.olingo.odata2.api.ODataServiceFactory;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataContext;

public class JmpdServiceRegistry extends ODataServiceFactory {
    private static class AnnotationInstances {
        final static String MODEL_PACKAGE = "service.model";
        final static ODataService ANNOTATION_ODATA_SERVICE;

        static {
          try {
            ANNOTATION_ODATA_SERVICE = AnnotationServiceFactory.createAnnotationService(MODEL_PACKAGE);            
          } catch (ODataApplicationException ex) {
            throw new RuntimeException("Exception during sample data generation.", ex);
          } catch (ODataException ex) {
            throw new RuntimeException("Exception during data source initialization generation.", ex);
          }
        }
      }
    
    @Override
    public ODataService createService(ODataContext ctx) throws ODataException {
        return AnnotationInstances.ANNOTATION_ODATA_SERVICE;
    }
}

package eu.jmpd.service;

import java.util.List;

import org.apache.olingo.odata2.api.ODataService;
import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataProcessor;
import org.apache.olingo.odata2.api.processor.part.BatchProcessor;
import org.apache.olingo.odata2.api.processor.part.EntityComplexPropertyProcessor;
import org.apache.olingo.odata2.api.processor.part.EntityLinkProcessor;
import org.apache.olingo.odata2.api.processor.part.EntityLinksProcessor;
import org.apache.olingo.odata2.api.processor.part.EntityMediaProcessor;
import org.apache.olingo.odata2.api.processor.part.EntityProcessor;
import org.apache.olingo.odata2.api.processor.part.EntitySetProcessor;
import org.apache.olingo.odata2.api.processor.part.EntitySimplePropertyProcessor;
import org.apache.olingo.odata2.api.processor.part.EntitySimplePropertyValueProcessor;
import org.apache.olingo.odata2.api.processor.part.FunctionImportProcessor;
import org.apache.olingo.odata2.api.processor.part.FunctionImportValueProcessor;
import org.apache.olingo.odata2.api.processor.part.MetadataProcessor;
import org.apache.olingo.odata2.api.processor.part.ServiceDocumentProcessor;

public class ODataRegistryService implements ODataService {

	@Override
	public String getVersion() throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Edm getEntityDataModel() throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MetadataProcessor getMetadataProcessor() throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceDocumentProcessor getServiceDocumentProcessor()
			throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityProcessor getEntityProcessor() throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntitySetProcessor getEntitySetProcessor() throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityComplexPropertyProcessor getEntityComplexPropertyProcessor()
			throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityLinkProcessor getEntityLinkProcessor() throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityLinksProcessor getEntityLinksProcessor() throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityMediaProcessor getEntityMediaProcessor() throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntitySimplePropertyProcessor getEntitySimplePropertyProcessor()
			throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntitySimplePropertyValueProcessor getEntitySimplePropertyValueProcessor()
			throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FunctionImportProcessor getFunctionImportProcessor()
			throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FunctionImportValueProcessor getFunctionImportValueProcessor()
			throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BatchProcessor getBatchProcessor() throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ODataProcessor getProcessor() throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getSupportedContentTypes(
			Class<? extends ODataProcessor> processorFeature)
			throws ODataException {
		// TODO Auto-generated method stub
		return null;
	}

}

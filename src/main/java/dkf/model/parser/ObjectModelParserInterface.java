package dkf.model.parser;

import java.util.Map;

import dkf.coder.Coder;

public interface ObjectModelParserInterface {
	
	public void retrieveStructure();
	
	public String getClassHandleName();
	
	@SuppressWarnings("rawtypes")
	public Map<String, Coder> getFieldCoderMap();
	
	public Map<String, byte[]> getEncodedFieldMap();
	
	public Map<String, byte[]> encode(Object element);

}

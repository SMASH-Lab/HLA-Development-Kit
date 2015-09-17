package dkf.model.object.parser;


import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleValueMap;

import java.util.Map;

import dkf.model.parser.AbstractObjectModelParser;

public abstract class AbstractObjectClassParser extends AbstractObjectModelParser {
	
	public AbstractObjectClassParser() {
		super();
	}

	public abstract void decode(Object element, Map<String, AttributeHandle> mapFieldNameAttributeHandle, AttributeHandleValueMap arg1);
}

package dkf.model.interaction.parser;

import hla.rti1516e.ParameterHandle;
import hla.rti1516e.ParameterHandleValueMap;

import java.util.Map;

import dkf.model.parser.AbstractObjectModelParser;

public abstract class AbstractInteractionParser extends AbstractObjectModelParser {
	
	
	public AbstractInteractionParser() {
		super();
	}
	
	public abstract void decode(Object interaction, Map<String, ParameterHandle> mapFieldNameParameterHandle, ParameterHandleValueMap arg1);	
}

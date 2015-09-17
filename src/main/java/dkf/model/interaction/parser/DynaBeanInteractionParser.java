package dkf.model.interaction.parser;

import hla.rti1516e.ParameterHandle;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.encoding.DecoderException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.LazyDynaClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.JDOMException;

import dkf.coder.Coder;
import dkf.exception.NoResultException;
import dkf.utility.access.FOMDataInspector;
import dkf.utility.dataType.HLADataTypeUtil;


public class DynaBeanInteractionParser extends AbstractInteractionParser {
	
	private static final Logger logger = LogManager.getLogger(DynaBeanInteractionParser.class);
	
	private FOMDataInspector inspector = null;
	private LazyDynaClass dynaClass = null;

	public DynaBeanInteractionParser(LazyDynaClass dynaClass, FOMDataInspector inspector) {
		this.inspector = inspector;
		this.dynaClass  = dynaClass;
		retrieveStructure();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void retrieveStructure() {
		
		this.classHandleName = dynaClass.getName();
		List<Parameter> parameterList = null;
		try {
			parameterList = inspector.retrieveInteractionStructure(dynaClass.getName());
		} catch (JDOMException | IOException e) {
			logger.error("Error in retrieving the structure of the InteractionClass: "+classHandleName, e);
		}

		Map<String, Coder> tmpMapCoder = new HashMap<String, Coder>();
		Coder coderTmp = null;
		try {
			for(Parameter par : parameterList){
				coderTmp = tmpMapCoder.get(par.getDataType());
				if(coderTmp == null){
					coderTmp = HLADataTypeUtil.getClassCoder(par.getDataType()).newInstance();
					if(coderTmp == null)
						throw new NoResultException("Invalid Coder: "+par.getDataType());
					tmpMapCoder.put(par.getDataType(), coderTmp);
				}
				mapFieldCoder.put(par.getName(), coderTmp);
				
				//add DynaProperty
				dynaClass.add(par.getName(), HLADataTypeUtil.getJavaType(par.getDataType()));
				logger.debug("Parameter: "+par.getName()+" type: "+HLADataTypeUtil.getJavaType(par.getDataType()).getName());
			}
			
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error("Error in retrieving the Coder for the interaction: "+classHandleName, e);
		} finally{
			//clean resources
			tmpMapCoder = null;
			coderTmp = null;
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, byte[]> encode(Object element) {
		
		if(element == null || !(element instanceof DynaBean)){
			logger.error("The argument is null");
			throw new IllegalArgumentException("The argument is null");
		}

		logger.debug("Encoding: "+element.toString());

		DynaProperty[] properties = ((DynaBean)element).getDynaClass().getDynaProperties();
		Coder coder = null;

		for(DynaProperty p : properties){
			coder = mapFieldCoder.get(p.getName());
			if(coder != null){
				logger.trace(element.getClass().getName()+ " Update [ Field "+p.getName()+", value: "+((DynaBean)element).get(p.getName())+" ]");
				encoderMap.put(p.getName(), coder.encode(((DynaBean)element).get(p.getName())));
			}
			else{
				logger.error("The field ' "+p.getName()+" ' has not a Coder");
				throw new NullPointerException("The field ' "+p.getName()+" ' has not a Coder");
			}
		}
		return encoderMap;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void decode(Object interaction, Map<String, ParameterHandle> mapFieldNameParameterHandle, ParameterHandleValueMap arg1) {
		
		if(interaction == null || arg1 == null || mapFieldNameParameterHandle == null){
			logger.error("Some arguments are null");
			throw new IllegalArgumentException("Some arguments are null");
		}

		if(!(interaction instanceof DynaBean)){
			logger.error("Interaction must be an instance of the DynaBean interface");
			throw new IllegalArgumentException("Interaction must be an instance of the DynaBean interface");
		}

		DynaProperty[] properties = ((DynaBean)interaction).getDynaClass().getDynaProperties();
		Coder coder = null;
		byte[] currValue = null;

		try {
			for(DynaProperty p : properties){
				coder = mapFieldCoder.get(p.getName());
				if(coder != null){
					currValue = arg1.get(mapFieldNameParameterHandle.get(p.getName()));
					logger.debug("Parameter decoded [ Field "+p.getName()+", value: "+coder.decode(currValue)+" ]");
					((DynaBean)interaction).set(p.getName(), coder.decode(currValue));
				}
				else{
					logger.error("The field ' "+p.getName()+" ' has not a Coder");
					throw new NullPointerException("The field ' "+p.getName()+" ' has not a Coder");
				}
			}
		} catch (DecoderException e) {
			logger.error("Decode Exception", e);
		}
		
	}

}

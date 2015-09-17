package dkf.model.object.parser;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleValueMap;
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


public class DynaBeanObjectParser extends AbstractObjectClassParser {

	private static final Logger logger = LogManager.getLogger(DynaBeanObjectParser.class);

	private LazyDynaClass dynaClass = null;
	private FOMDataInspector inspector = null;

	public DynaBeanObjectParser(LazyDynaClass dynaClass, FOMDataInspector inspector) {
		this.inspector = inspector;
		this.dynaClass = dynaClass;
		retrieveStructure();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void retrieveStructure() {

		this.classHandleName = dynaClass.getName();
		List<Attribute> attributeList = null;
		try {
			attributeList = inspector.retrieveObjectStructure(dynaClass.getName());
		} catch (JDOMException | IOException e) {
			logger.error("Error in retrieving the structure of the CbjectClass: "+classHandleName, e);
		}

		Map<String, Coder> tmpMapCoder = new HashMap<String, Coder>();
		Coder coderTmp = null;
		try {
			for(Attribute attr : attributeList){
				coderTmp = tmpMapCoder.get(attr.getDataType());
				if(coderTmp == null){
					coderTmp = HLADataTypeUtil.getClassCoder(attr.getDataType()).newInstance();
					if(coderTmp == null)
						throw new NoResultException("Invalid Coder: "+attr.getDataType());
					tmpMapCoder.put(attr.getDataType(), coderTmp);
				}
				mapFieldCoder.put(attr.getName(), coderTmp);
				
				//add DynaProperty
				dynaClass.add(attr.getName(), HLADataTypeUtil.getJavaType(attr.getDataType()));
			}
			
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error("Error in retrieving the Coder for the object: "+classHandleName, e);
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
	public void decode(Object element, Map<String, AttributeHandle> mapFieldNameAttributeHandle, AttributeHandleValueMap arg1) {

		if(element == null || arg1 == null || mapFieldNameAttributeHandle == null){
			logger.error("Some arguments are null");
			throw new IllegalArgumentException("Some arguments are null");
		}

		if(!(element instanceof DynaBean)){
			logger.error("Object must be an instance of the DynaBean interface");
			throw new IllegalArgumentException("Object must be an instance of the DynaBean interface");
		}

		DynaProperty[] properties = ((DynaBean)element).getDynaClass().getDynaProperties();
		Coder coder = null;
		byte[] currValue = null;

		try {
			for(DynaProperty p : properties){
				coder = mapFieldCoder.get(p.getName());
				if(coder != null){
					currValue = arg1.get(mapFieldNameAttributeHandle.get(p.getName()));
					logger.debug("Attribute decoded [ Field "+p.getName()+", value: "+coder.decode(currValue)+" ]");
					((DynaBean)element).set(p.getName(), coder.decode(currValue));
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

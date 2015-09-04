/*****************************************************************
HLADevelopmentKit -  A Java framework to develop HLA Federates.
Copyright (c) 2015, SMASH Lab - University of Calabria (Italy), 
All rights reserved.

GNU Lesser General Public License (GNU LGPL).

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 3.0 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library. 
If not, see http://http://www.gnu.org/licenses/
*****************************************************************/

package dkf.model.interaction;

import hla.rti1516e.ParameterHandle;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.encoding.DecoderException;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dkf.coder.Coder;
import dkf.model.interaction.annotations.InteractionClass;
import dkf.model.interaction.annotations.Parameter;
import dkf.model.parser.AbstractObjectModelParser;



public class InteractionClassModelParser extends AbstractObjectModelParser {

	private static final Logger logger = LogManager.getLogger(InteractionClassModelParser.class);

	protected Class<? extends InteractionClass> interactionClassModel = null;


	public InteractionClassModelParser(Class<? extends InteractionClass> interactionClassModel) {
		super();
		this.interactionClassModel = interactionClassModel;
		retrieveClassModelStructure();
	}

	@SuppressWarnings({ "rawtypes" })
	protected void retrieveClassModelStructure() {

		this.classHandleName = interactionClassModel.getAnnotation(InteractionClass.class).name();
		fields = interactionClassModel.getDeclaredFields();
		Map<Class, Coder> tmpMapCoder = new HashMap<Class, Coder>();
		Coder coderTmp = null;

		try {
			for(Field f : fields){
				if(f.isAnnotationPresent(Parameter.class)){
					coderTmp = tmpMapCoder.get(f.getAnnotation(Parameter.class).coder());
					if(coderTmp == null){
						coderTmp = f.getAnnotation(Parameter.class).coder().newInstance();
						tmpMapCoder.put(f.getAnnotation(Parameter.class).coder(), coderTmp);
					}
					matchingObjectCoderIsValid(f, coderTmp);
					mapFieldCoder.put(f.getAnnotation(Parameter.class).name(), coderTmp);
				}
			}
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error("Error in retreving the annotations of the fields");
			e.printStackTrace();
		} finally {
			tmpMapCoder = null;
			coderTmp = null;
		}

	}

	@SuppressWarnings("unchecked")
	public Map<String, byte[]> encode(Object interaction) {

		if(interaction == null){
			logger.error("The argument is null");
			throw new IllegalArgumentException("The parameter value cannot be null");
		}

		logger.debug("Encoding: "+interaction.toString());

		PropertyDescriptor pd = null;
		Object fieldValue = null;
		Coder<Object> fieldCoder = null;

		try {
			for(Field f : fields)
				if(f.isAnnotationPresent(Parameter.class)){
					pd = new PropertyDescriptor(f.getName(), interaction.getClass());
					fieldValue = pd.getReadMethod().invoke(interaction);
					if(fieldValue != null){
						fieldCoder = mapFieldCoder.get(f.getAnnotation(Parameter.class).name());
						logger.trace(interaction.getClass().getName()+ " Update [ Field "+f.getName()+", value: "+fieldValue+" ]");
						encoderMap.put(f.getAnnotation(Parameter.class).name(), fieldCoder.encode(fieldValue));
					}
					else{
						logger.error("The parameter ' "+f.getName()+" ' is null");
						throw new NullPointerException("The parameter ' "+f.getName()+" ' is null");
					}
				}
		} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logger.error("Error in retreving the value of the fields");
			e.printStackTrace();
		}

		return encoderMap;
	}

	@SuppressWarnings("unchecked")
	public void decode(Object interaction, Map<String, ParameterHandle> mapFieldNameParameterHandle, ParameterHandleValueMap arg1) {

		if(interaction == null || arg1 == null || mapFieldNameParameterHandle == null){
			logger.error("Some arguments are null");
			throw new IllegalArgumentException("Some arguments are null");
		}

		PropertyDescriptor pd = null;
		Coder<Object> fieldCoder = null;
		byte[] currValue = null;
		try {
			for(Field f : fields)
				if(f.isAnnotationPresent(Parameter.class)){
					pd = new PropertyDescriptor(f.getName(), interaction.getClass());
					fieldCoder = mapFieldCoder.get(f.getAnnotation(Parameter.class).name());
					currValue = arg1.get(mapFieldNameParameterHandle.get(f.getAnnotation(Parameter.class).name()));
					pd.getWriteMethod().invoke(interaction, fieldCoder.decode(currValue));
					logger.debug("Parameter decoded [ Field "+f.getName()+", value: "+fieldCoder.decode(currValue)+" ]");
				}
		} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | DecoderException e) {
			e.printStackTrace();
		}
	}
}

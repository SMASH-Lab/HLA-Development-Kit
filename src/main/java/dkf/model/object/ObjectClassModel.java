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
package dkf.model.object;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.exceptions.AttributeNotDefined;
import hla.rti1516e.exceptions.AttributeNotOwned;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.IllegalName;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.ObjectClassNotDefined;
import hla.rti1516e.exceptions.ObjectClassNotPublished;
import hla.rti1516e.exceptions.ObjectInstanceNameInUse;
import hla.rti1516e.exceptions.ObjectInstanceNameNotReserved;
import hla.rti1516e.exceptions.ObjectInstanceNotKnown;
import hla.rti1516e.exceptions.OwnershipAcquisitionPending;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dkf.core.DKFRTIAmbassador;
import dkf.exception.UpdateException;

@SuppressWarnings("rawtypes")
public class ObjectClassModel {

	private static Logger logger = LogManager.getLogger(ObjectClassModel.class);

	private ObjectModelStatus status = ObjectModelStatus.UNKNOWN;
	private ObjectClassHandle  objectClassHandle = null;

	private Map<String, AttributeHandle> mapFieldNameAttributeHandle = null;

	private RTIambassador rti_ambassador = null;

	private Map<Object, ObjectClassEntity> entityMap = null;
	private Map<ObjectInstanceHandle, ObjectClassEntity> mapObjectInstanceHandleObjectClassEntity = null;
	
	private ObjectClassModelParser parser = null;

	private AttributeHandleValueMap attribute_values = null;

	@SuppressWarnings("unchecked")
	public ObjectClassModel(Class objectClass) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, NotConnected, InvalidObjectClassHandle, InstantiationException, IllegalAccessException {
		this.rti_ambassador = DKFRTIAmbassador.getInstance();
		this.parser = new ObjectClassModelParser(objectClass);
		
		this.entityMap = new HashMap<Object, ObjectClassEntity>();
		this.mapObjectInstanceHandleObjectClassEntity = new HashMap<ObjectInstanceHandle, ObjectClassEntity>();
		initialize();
	}

	private void initialize() throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError, InvalidObjectClassHandle, 
	InstantiationException, IllegalAccessException  {

		if(status == ObjectModelStatus.UNKNOWN){
			
			// Get a handle to the class.
			this.objectClassHandle = rti_ambassador.getObjectClassHandle(parser.getClassHandleName());
			this.mapFieldNameAttributeHandle = new HashMap<String, AttributeHandle>();

			// Get handles to all the attributes.
			AttributeHandle tmp = null;
			for(String str : parser.getMapFieldCoder().keySet()){
				tmp = rti_ambassador.getAttributeHandle(objectClassHandle, str);
				mapFieldNameAttributeHandle.put(str, tmp);
			}

			this.attribute_values  = rti_ambassador.getAttributeHandleValueMapFactory().create(mapFieldNameAttributeHandle.size());

			status = ObjectModelStatus.INITIALIZED;
		}

	}// initialize

	public void subscribe() throws AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {

		if(status == ObjectModelStatus.INITIALIZED){

			AttributeHandleSet attributeSet = rti_ambassador.getAttributeHandleSetFactory().create();
			for(AttributeHandle val : mapFieldNameAttributeHandle.values())
				attributeSet.add(val);

			rti_ambassador.subscribeObjectClassAttributes(objectClassHandle, attributeSet);
			status = ObjectModelStatus.SUBSCRIBED;
			
		}
		else{
			logger.error("You can't subscribe an uninitialized element!");
			throw new IllegalStateException("You can't subscribe an uninitialized element!");
		}

	}//subscribe

	public void unsubscribe() throws ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {

		if(status == ObjectModelStatus.SUBSCRIBED){
			rti_ambassador.unsubscribeObjectClass(this.objectClassHandle);
			status = ObjectModelStatus.UNKNOWN;
		}
		else{
			logger.error("You can't unsubscribe "+this.objectClassHandle+" because it is not subscribed");
			throw new IllegalStateException("You can't unsubscribe "+this.objectClassHandle+" because it is not subscribed");
		}

	}//subscribe

	public void publish() throws FederateNotExecutionMember, NotConnected, AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, 
	RestoreInProgress, RTIinternalError{

		if(status == ObjectModelStatus.INITIALIZED){

			AttributeHandleSet attributeSet = rti_ambassador.getAttributeHandleSetFactory().create();
			
			for(AttributeHandle element: mapFieldNameAttributeHandle.values())
				attributeSet.add(element);
			
			rti_ambassador.publishObjectClassAttributes(objectClassHandle, attributeSet);
						
			status = ObjectModelStatus.PUBLISHED;
		}
		else{
			logger.error("You can't publish an uninitialized or an already published ObjectClassModel!");
			throw new IllegalStateException("You can't publish an uninitialized or an already published ObjectClassModel!");
		}

	}

	public void unpublish() throws FederateNotExecutionMember, NotConnected, OwnershipAcquisitionPending, AttributeNotDefined, 
	ObjectClassNotDefined, SaveInProgress, RestoreInProgress, RTIinternalError {

		if(status == ObjectModelStatus.PUBLISHED){

			AttributeHandleSet attributeSet = rti_ambassador.getAttributeHandleSetFactory().create();
			attributeSet.addAll(mapFieldNameAttributeHandle.values());
			rti_ambassador.unpublishObjectClassAttributes(objectClassHandle, attributeSet);

			status = ObjectModelStatus.UNKNOWN;
		}
		else{
			logger.error("You can't unpublish an uninitialized or an unpublished ObjectClassModel!");
			throw new IllegalStateException("You can't unpublish an uninitialized or an unpublished ObjectClassModel!");
		}

	}

	public void addEntity(ObjectClassEntity element){
		entityMap.put(element.getElement(), element);
		mapObjectInstanceHandleObjectClassEntity.put(element.getObjectInstanceHandle(), element);
	}

	public void updatePublishedObject(ObjectClassEntity element) throws UpdateException, IllegalName, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, 
	NotConnected, RTIinternalError, ObjectInstanceNameInUse, ObjectInstanceNameNotReserved, ObjectClassNotPublished, 
	ObjectClassNotDefined, AttributeNotOwned, AttributeNotDefined, ObjectInstanceNotKnown {

		ObjectClassEntity entity = entityMap.get(element.getElement());

		if(entity == null){
			logger.error("Can't find the ' "+element+" ' !");
			throw new IllegalStateException("Can't find the ' "+element+" ' !");
		}

		if(status == ObjectModelStatus.PUBLISHED) {
			if(entity.getObjectInstanceHandle() == null)
				entity.setObjectInstanceHandle(reserveObjectInstanceName(entity));
			
			Map<String, byte[]> ris = parser.encode(element.getElement());
			for(Entry<String, AttributeHandle> entry : mapFieldNameAttributeHandle.entrySet())
				attribute_values.put(entry.getValue(), ris.get(entry.getKey()));
			
			rti_ambassador.updateAttributeValues(entity.getObjectInstanceHandle(), attribute_values , null);
		}
		else{
			logger.error("Can't update the Object ' "+element+" ', because it is not published!");
			throw new IllegalStateException("Can't update the Object ' "+element+" ', because it is not published!");
		}

	}
	

	public void updateSubscribedObject(ObjectClassEntity element, AttributeHandleValueMap arg1){ 
		
		ObjectClassEntity entity = entityMap.get(element.getElement());

		if(entity == null){
			logger.error("Can't find the ' "+element+" ' !");
			throw new IllegalStateException("Can't find the ' "+element+" ' !");
		}
		
		if(status == ObjectModelStatus.SUBSCRIBED)
			parser.decode(element.getElement(), mapFieldNameAttributeHandle, arg1);
		
		else{
			logger.error("Can't update the ' "+element+" ', because it is not subscribed !");
			throw new IllegalStateException("Can't update the ' "+element+" ', because it is not subscribed !");
		}
	}

	private ObjectInstanceHandle reserveObjectInstanceName(ObjectClassEntity entity) throws IllegalName, SaveInProgress, RestoreInProgress, 
	FederateNotExecutionMember, NotConnected, RTIinternalError, 
	ObjectInstanceNameInUse, ObjectInstanceNameNotReserved, 
	ObjectClassNotPublished, ObjectClassNotDefined {

		// Reserve the name.
		rti_ambassador.reserveObjectInstanceName(entity.getInstanceName());

		// Need to wait here until name reservation callback is recieved.
		while (entity.getStatus() == NameReservationStatus.UNKNOWN)
			Thread.yield();

		if (entity.getStatus() == NameReservationStatus.FAILED) {
			logger.error("Name Resevation Failed [name= "+entity+" ]");
			throw new IllegalName("Name Resevation Failed [name= "+entity+" ]");
		}

		// If name is reserved then register the instance.
		if (entity.getStatus() == NameReservationStatus.SUCCEDED)
			return  rti_ambassador.registerObjectInstance(objectClassHandle,entity.getInstanceName());

		return null;
	}
	
	public Map<Object, ObjectClassEntity> getEntities() {
		return this.entityMap;
	}
	
	public ObjectClassHandle getObjectClassHandle() {
		return this.objectClassHandle;
	}

	public ObjectClassEntity getObjectClassEntity(ObjectInstanceHandle instanceHandle) {
		return mapObjectInstanceHandleObjectClassEntity.get(instanceHandle);
	}

}


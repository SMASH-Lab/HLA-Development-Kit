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

import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
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
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.LazyDynaClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dkf.model.object.annotations.ObjectClass;
import dkf.utility.access.FOMDataInspector;
import dkf.exception.UpdateException;

@SuppressWarnings("rawtypes")
public class ObjectClassModelManager {

	private static final Logger logger = LogManager.getLogger(ObjectClassModelManager.class);

	//maps published element
	private Map<String, ObjectClassModel> published = null;
	private Map<String, ObjectClassEntity> mapInstanceNameObjectClassEntity = null;

	//maps subscribed element
	private Map<String, ObjectClassModel> subscribed = null;
	private Map<ObjectClassHandle, Class> mapHandleClassObjectClass = null;
	private Map<ObjectInstanceHandle, ObjectClassHandleEntity> objectInstanceHandleObjectClassHandle = null;

	//maps DynaElement
	private Map<ObjectClassHandle, DynaClass> mapHandleClassDynaObjectClass = null;

	public ObjectClassModelManager() {
		this.published = new HashMap<String, ObjectClassModel>();
		this.mapInstanceNameObjectClassEntity = new HashMap<String, ObjectClassEntity>();

		this.subscribed = new HashMap<String, ObjectClassModel>();
		this.mapHandleClassObjectClass = new HashMap<ObjectClassHandle, Class>();
		this.objectInstanceHandleObjectClassHandle = new HashMap<ObjectInstanceHandle, ObjectClassHandleEntity>();

		this.mapHandleClassDynaObjectClass = new HashMap<ObjectClassHandle, DynaClass>();

	}

	public Map<String, ObjectClassModel> getPublishedMap() {
		return this.published;
	}

	public Map<String, ObjectClassModel> getSubscribedMap() {
		return this.subscribed;
	}

	@SuppressWarnings("unchecked")
	public void subscribe(Class objectClass) throws RTIinternalError, InstantiationException, IllegalAccessException, NameNotFound, 
	FederateNotExecutionMember, NotConnected, InvalidObjectClassHandle, AttributeNotDefined,
	ObjectClassNotDefined, SaveInProgress, RestoreInProgress {

		ObjectClassModel ocm = new ObjectClassModel(objectClass);
		this.subscribed.put(((Class<ObjectClass>)objectClass).getAnnotation(ObjectClass.class).name(), ocm);
		this.mapHandleClassObjectClass.put(ocm.getObjectClassHandle(), objectClass);
		ocm.subscribe();
		logger.debug("subscribed: "+ objectClass);
	}

	public void subscribe(String objectEndPoint, FOMDataInspector inspector) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, NotConnected, InvalidObjectClassHandle, InstantiationException, IllegalAccessException, AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress {

		LazyDynaClass dynaClass = new LazyDynaClass(objectEndPoint);
		ObjectClassModel ocm = new ObjectClassModel(dynaClass, inspector);
		this.subscribed.put(objectEndPoint, ocm);
		mapHandleClassDynaObjectClass.put(ocm.getObjectClassHandle(), dynaClass.newInstance().getDynaClass());
		ocm.subscribe();
		logger.debug("subscribed: "+ objectEndPoint);
	}

	public void unsubscribe(String objectEndPoint) throws ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		
		ObjectClassModel ocm = subscribed.get(objectEndPoint);
		
		if(!ocm.isDynaClass())
			this.mapHandleClassObjectClass.remove(ocm.getObjectClassHandle());
		else
			this.mapHandleClassDynaObjectClass.remove(ocm.getObjectClassHandle());

		/*
		 * for(Entry<Object, ObjectClassEntity> entry : ocm.getEntities().entrySet())
			this.objectInstanceHandleObjectClassHandle.remove(entry.getValue().getObjectInstanceHandle());*/

		this.subscribed.remove(objectEndPoint);
		ocm.unsubscribe();

	}

	public void publish(Object element, String name) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, NotConnected, InvalidObjectClassHandle, InstantiationException, IllegalAccessException, AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, IllegalName, ObjectInstanceNameInUse, ObjectInstanceNameNotReserved, ObjectClassNotPublished, AttributeNotOwned, ObjectInstanceNotKnown, UpdateException {

		ObjectClassModel ocm = published.get(element.getClass().getAnnotation(ObjectClass.class).name());

		if(ocm == null){
			ocm = new ObjectClassModel(element.getClass());
			ocm.publish();
			this.published.put(element.getClass().getAnnotation(ObjectClass.class).name(), ocm);
		}

		ObjectClassEntity entity = null;
		if(name != null)
			entity = new ObjectClassEntity(name, element);
		else
			entity = new ObjectClassEntity(element.getClass().getName()+element.hashCode(), element);

		ocm.addEntity(entity);
		this.mapInstanceNameObjectClassEntity.put(entity.getInstanceName(), entity);

	}

	public ObjectClassEntity getObjectClassEntityByIstanceName(String instance_name) {
		return this.mapInstanceNameObjectClassEntity.get(instance_name);
	}

	public void addDiscoverObjectInstance(ObjectInstanceHandle instanceHandle, ObjectClassHandle objectClassHandle, String instanceName) {
		this.objectInstanceHandleObjectClassHandle.put(instanceHandle, new ObjectClassHandleEntity(objectClassHandle, instanceName));
	}

	public Object reflectAttributeValues(ObjectInstanceHandle instanceHandle, AttributeHandleValueMap attributes) throws InstantiationException, IllegalAccessException {

		ObjectClassHandleEntity oche = objectInstanceHandleObjectClassHandle.get(instanceHandle);

		if(mapHandleClassObjectClass.get(oche.getObjectClassHandle()) != null){
			// process a Class
			return reflectingObjectClassAttributeValues(oche, instanceHandle, attributes);
		}
		else if(mapHandleClassDynaObjectClass.get(oche.getObjectClassHandle()) != null){
			// process a LazyDynaClass
			return reflectingObjectDynaClassAttributeValues(oche, instanceHandle, attributes);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private Object reflectingObjectClassAttributeValues(ObjectClassHandleEntity oche, ObjectInstanceHandle instanceHandle, AttributeHandleValueMap attributes) throws InstantiationException, IllegalAccessException {

		ObjectClassHandle och = oche.getObjectClassHandle();
		ObjectClassModel ocm = subscribed.get(((Class<ObjectClass>)mapHandleClassObjectClass.get(och)).getAnnotation(ObjectClass.class).name());

		ObjectClassEntity entity = ocm.getObjectClassEntity(instanceHandle);
		if(entity == null){
			Object element = mapHandleClassObjectClass.get(och).newInstance();
			entity = new ObjectClassEntity(instanceHandle, element);
			entity.setInstanceName(oche.getInstanceName());
			entity.setStatus(NameReservationStatus.SUCCEDED);
			ocm.addEntity(entity);
		}
		ocm.updateSubscribedObject(entity, attributes);

		return entity.getElement();
	}

	private Object reflectingObjectDynaClassAttributeValues(ObjectClassHandleEntity oche, ObjectInstanceHandle instanceHandle, AttributeHandleValueMap attributes) throws InstantiationException, IllegalAccessException {

		ObjectClassHandle och = oche.getObjectClassHandle();
		ObjectClassModel ocm = subscribed.get(mapHandleClassDynaObjectClass.get(och).getName());

		ObjectClassEntity entity = ocm.getObjectClassEntity(instanceHandle);
		if(entity == null){
			Object element = mapHandleClassDynaObjectClass.get(och).newInstance();
			entity = new ObjectClassEntity(instanceHandle, element);
			entity.setInstanceName(oche.getInstanceName());
			entity.setStatus(NameReservationStatus.SUCCEDED);
			ocm.addEntity(entity);
		}
		ocm.updateSubscribedObject(entity, attributes);

		return entity.getElement();

	}

	public boolean objectClassIsSubscribed(ObjectClassHandle arg1) {
		return this.mapHandleClassObjectClass.get(arg1) != null || this.mapHandleClassDynaObjectClass.get(arg1) != null;
	}

	public boolean objectInstanceHandleIsSubscribed(ObjectInstanceHandle arg0) {
		return this.objectInstanceHandleObjectClassHandle.get(arg0) != null;
	}

}

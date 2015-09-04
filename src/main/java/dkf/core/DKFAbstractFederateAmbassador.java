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
package dkf.core;

import java.util.Observer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dkf.core.observer.Subject;
import dkf.exception.UpdateException;
import dkf.model.interaction.InteractionClassModel;
import dkf.model.interaction.InteractionClassModelManager;
import dkf.model.interaction.annotations.InteractionClass;
import dkf.model.object.NameReservationStatus;
import dkf.model.object.ObjectClassEntity;
import dkf.model.object.ObjectClassModel;
import dkf.model.object.ObjectClassModelManager;
import dkf.model.object.annotations.ObjectClass;
import dkf.time.TimeInterface;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.FederateHandle;
import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.LogicalTime;
import hla.rti1516e.MessageRetractionHandle;
import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.OrderType;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.TransportationTypeHandle;
import hla.rti1516e.exceptions.AttributeNotDefined;
import hla.rti1516e.exceptions.AttributeNotOwned;
import hla.rti1516e.exceptions.FederateInternalError;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederateServiceInvocationsAreBeingReportedViaMOM;
import hla.rti1516e.exceptions.IllegalName;
import hla.rti1516e.exceptions.InteractionClassNotDefined;
import hla.rti1516e.exceptions.InteractionClassNotPublished;
import hla.rti1516e.exceptions.InteractionParameterNotDefined;
import hla.rti1516e.exceptions.InvalidInteractionClassHandle;
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

public abstract class DKFAbstractFederateAmbassador extends NullFederateAmbassador {

	private static Logger logger = LogManager.getLogger(DKFAbstractFederateAmbassador.class);

	private Subject subject = null;

	private ObjectClassModelManager objectManager = null;
	private InteractionClassModelManager interactionManager = null;

	//To manage concurrent access to the variable 'logical_time'
	private boolean isRegulating = false;
	private boolean isConstrained = false;
	private boolean isAdvancing = false;

	private TimeInterface time = null;

	public DKFAbstractFederateAmbassador() {
		this.subject = new Subject();

		this.objectManager = new ObjectClassModelManager();
		this.interactionManager = new InteractionClassModelManager();

	}

	protected void setTime(TimeInterface time) {
		this.time  = time;
	}

	public boolean isRegulating() {
		return isRegulating;
	}

	public void setRegulating(boolean isRegulating) {
		this.isRegulating = isRegulating;
	}

	public boolean isConstrained() {
		return isConstrained;
	}

	public void setConstrained(boolean isConstrained) {
		this.isConstrained = isConstrained;
	}

	public boolean isAdvancing() {
		return isAdvancing;
	}

	public void setAdvancing(boolean isAdvancing) {
		this.isAdvancing = isAdvancing;
	}

	public void addObserverToSubject(Observer observer) {
		subject.addObserver(observer);
	}

	public void deleteObserverFromSubject(Observer observer) {
		subject.deleteObserver(observer);
	}

	@Override
	public void discoverObjectInstance(ObjectInstanceHandle arg0, ObjectClassHandle arg1, String arg2, FederateHandle arg3) throws FederateInternalError {
		try {
			discObjectInstance(arg0, arg1, arg2, arg3);
		} catch (RTIinternalError e) {
			e.printStackTrace();
		}
	}

	@Override
	public void discoverObjectInstance(ObjectInstanceHandle arg0, ObjectClassHandle arg1, String arg2) throws FederateInternalError {
		try {
			discObjectInstance(arg0, arg1, arg2, null);
		} catch (RTIinternalError e) {
			e.printStackTrace();
		}
	}

	private void discObjectInstance(ObjectInstanceHandle arg0, ObjectClassHandle arg1, String arg2, FederateHandle arg3) throws RTIinternalError {

		logger.trace("ObjectInstanceHandle: "+arg0+", ObjectClassHandle: "+ arg1+", Name: "+arg2+", FederateHandle: "+arg3);

		if(objectManager.objectClassIsSubscribed(arg1))
			objectManager.addDiscoverObjectInstance(arg0, arg1, arg2);

	}

	@Override
	@SuppressWarnings("rawtypes")
	public void reflectAttributeValues(ObjectInstanceHandle arg0,
			AttributeHandleValueMap arg1, byte[] arg2, OrderType arg3,
			TransportationTypeHandle arg4, LogicalTime arg5, OrderType arg6,
			MessageRetractionHandle arg7, SupplementalReflectInfo arg8) throws FederateInternalError {

		this.reflAttributeValues(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);


	}

	@Override
	@SuppressWarnings("rawtypes")
	public void reflectAttributeValues(ObjectInstanceHandle arg0,
			AttributeHandleValueMap arg1, byte[] arg2, OrderType arg3,
			TransportationTypeHandle arg4, LogicalTime arg5, OrderType arg6,
			SupplementalReflectInfo arg7) throws FederateInternalError {
		this.reflAttributeValues(arg0, arg1, arg2, arg3, arg4, arg5, arg6, null, arg7);
	}

	@Override
	public void reflectAttributeValues(ObjectInstanceHandle arg0,
			AttributeHandleValueMap arg1, byte[] arg2, OrderType arg3,
			TransportationTypeHandle arg4, SupplementalReflectInfo arg5) throws FederateInternalError {

		this.reflAttributeValues(arg0, arg1, arg2, arg3, arg4, null, null, null, arg5);

	}
	
	@SuppressWarnings("rawtypes")
	private void reflAttributeValues(ObjectInstanceHandle arg0, AttributeHandleValueMap arg1, byte[] arg2, OrderType arg3,
			TransportationTypeHandle arg4, LogicalTime arg5, OrderType arg6,
			MessageRetractionHandle arg7, SupplementalReflectInfo arg8) throws FederateInternalError {

		logger.trace("ObjectInstanceHandle "+ arg0+",AttributeHandleValueMap "+ arg1+",byte[] "+ arg2+",OrderType "+arg3+", TransportationTypeHandle"+arg4+",LogicalTime"+ arg5+",OrderType"+arg6+"MessageRetractionHandle"+arg7+" ,SupplementalReflectInfo"+arg8);

		if(objectManager.objectInstanceHandleIsSubscribed(arg0)){
			try {
				Object ris = objectManager.reflectAttributeValues(arg0, arg1);
				if(ris != null)
					this.subject.notifyUpdate(ris);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				logger.error("Error during the decoding operation of the Object with ObjectInstanceHandle="+arg0);
			}
		}

	}

	@Override
	public void receiveInteraction(InteractionClassHandle interactionClass,
			ParameterHandleValueMap theParameters, byte[] userSuppliedTag,
			OrderType sentOrdering, TransportationTypeHandle theTransport,
			SupplementalReceiveInfo receiveInfo) throws FederateInternalError {

		this.recInteraction(interactionClass, theParameters, userSuppliedTag,
				sentOrdering, theTransport, null, null, null, receiveInfo);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void receiveInteraction(InteractionClassHandle interactionClass,
			ParameterHandleValueMap theParameters, byte[] userSuppliedTag,
			OrderType sentOrdering, TransportationTypeHandle theTransport,
			LogicalTime theTime, OrderType receivedOrdering,
			SupplementalReceiveInfo receiveInfo) throws FederateInternalError {

		this.recInteraction(interactionClass, theParameters, userSuppliedTag,
				sentOrdering, theTransport, theTime, receivedOrdering,
				null, receiveInfo);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void receiveInteraction(InteractionClassHandle interactionClass,
			ParameterHandleValueMap theParameters, byte[] userSuppliedTag,
			OrderType sentOrdering, TransportationTypeHandle theTransport,
			LogicalTime theTime, OrderType receivedOrdering,
			MessageRetractionHandle retractionHandle,
			SupplementalReceiveInfo receiveInfo) throws FederateInternalError {

		this.recInteraction(interactionClass, theParameters, userSuppliedTag,
				sentOrdering, theTransport, theTime, receivedOrdering,
				retractionHandle, receiveInfo);
	}
	
	@SuppressWarnings("rawtypes")
	private void recInteraction(InteractionClassHandle arg0, ParameterHandleValueMap arg1, byte[] arg2,
			OrderType arg3, TransportationTypeHandle arg4, LogicalTime arg5, OrderType arg6, MessageRetractionHandle arg7, SupplementalReceiveInfo arg8) {

		logger.trace("InteractionClassHandle "+ arg0+",ParameterHandleValueMap "+ arg1+",byte[] "+ arg2+
				",OrderType "+arg3+", TransportationTypeHandle"+arg4+",LogicalTime"+ arg5+",OrderType"+arg6+"MessageRetractionHandle"+arg7+" ,SupplementalReflectInfo"+arg8);


		if(interactionManager.interactionInstanceHandleIsSubscribed(arg0)){
			Object ris = interactionManager.receiveInteraction(arg0, arg1);
			if(ris != null)
				this.subject.notifyUpdate(ris);
		}

	}

	@Override
	@SuppressWarnings("rawtypes")
	public void timeConstrainedEnabled(LogicalTime time) throws FederateInternalError {

		this.time.setFederateTime(time);
		this.isConstrained = true;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void timeRegulationEnabled(LogicalTime time) throws FederateInternalError {

		this.time.setFederateTime(time);
		this.isRegulating = true;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void timeAdvanceGrant(LogicalTime time) throws FederateInternalError {

		if (time.compareTo(this.time.getLogicalTime()) >= 0 ){
			this.time.setFederateTime(time);
			this.isAdvancing = true;
		}
	}

	@Override
	public void objectInstanceNameReservationSucceeded(String instance_name) throws FederateInternalError {

		ObjectClassEntity oce = objectManager.getObjectClassEntityByIstanceName(instance_name);

		if (oce != null)
			oce.setStatus(NameReservationStatus.SUCCEDED);
		else 
			logger.error("ElementObject "+instance_name+" not found!");

	}


	@Override
	public void objectInstanceNameReservationFailed(String instance_name) throws FederateInternalError {

		ObjectClassEntity oce = objectManager.getObjectClassEntityByIstanceName(instance_name);

		if (oce != null)
			oce.setStatus(NameReservationStatus.FAILED);
		else 
			logger.error("ElementObject "+instance_name+" not found!");

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean objectClassModelIsAlreadySubscribed(Class objectClass) {
		if(((Class<ObjectClass>)objectClass).getAnnotation(ObjectClass.class) != null &&
				objectManager.getSubscribedMap().containsKey(((Class<ObjectClass>)objectClass).getAnnotation(ObjectClass.class).name()))
			return true;
		return false;
	}

	@SuppressWarnings("rawtypes")
	public void subscribeObjectClassModel(Class objectClass) throws RTIinternalError, InstantiationException, IllegalAccessException, 
	NameNotFound, FederateNotExecutionMember, NotConnected, InvalidObjectClassHandle, 
	AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress {

		objectManager.subscribe(objectClass);

	}

	@SuppressWarnings("rawtypes")
	public void unsubscribeObjectClassModel(Class objectClass) throws ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, 
	NotConnected, RTIinternalError {

		objectManager.unsubscribe(objectClass);

	}

	public boolean objectClassEntityIsAlreadyPublished(Object element) {

		if(element.getClass().getAnnotation(ObjectClass.class) != null &&
				element.getClass().getAnnotation(ObjectClass.class).name() != null){

			ObjectClassModel ocm = objectManager.getPublishedMap().get(element.getClass().getAnnotation(ObjectClass.class).name());
			if(ocm != null)
				return ocm.getEntities().containsKey(element);
		}
		return false;
	}

	public void publishObjectClassEntity(Object element, String name) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, NotConnected, InvalidObjectClassHandle, InstantiationException, IllegalAccessException, AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, IllegalName, ObjectInstanceNameInUse, ObjectInstanceNameNotReserved, ObjectClassNotPublished, AttributeNotOwned, ObjectInstanceNotKnown, UpdateException {
		objectManager.publish(element, name);

	}

	public void updateObjectClassEntityOnRTI(Object element) throws IllegalName, SaveInProgress, RestoreInProgress, FederateNotExecutionMember,
	NotConnected, RTIinternalError, ObjectInstanceNameInUse, ObjectInstanceNameNotReserved, 
	ObjectClassNotPublished, ObjectClassNotDefined, AttributeNotOwned, AttributeNotDefined, 
	ObjectInstanceNotKnown, UpdateException {


		ObjectClassModel ocm = objectManager.getPublishedMap().get(element.getClass().getAnnotation(ObjectClass.class).name());
		ocm.updatePublishedObject(ocm.getEntities().get(element));
	}

	public boolean interactionClassEntityIsAlreadyPublished(Object interaction) {

		if(interaction.getClass().getAnnotation(InteractionClass.class) != null &&
				interaction.getClass().getAnnotation(InteractionClass.class).name() != null){

			InteractionClassModel icm = interactionManager.getPublishedMap().get(interaction.getClass().getAnnotation(InteractionClass.class).name());
			if(icm != null)
				return icm.getEntity() != null;
		}

		return false;

	}

	public void publishInteractionClassEntity(Object interaction) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, NotConnected, InvalidInteractionClassHandle, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, InteractionClassNotPublished, InteractionParameterNotDefined {
		interactionManager.publish(interaction);

	}

	public void updateInteractionClassEntityOnRTI(Object interaction) throws InteractionClassNotPublished, InteractionParameterNotDefined, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {

		InteractionClassModel icm = interactionManager.getPublishedMap().get(interaction.getClass().getAnnotation(InteractionClass.class).name());
		icm.updatePublishedInteraction();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean interactionClassModelIsAlreadySubscribed(Class interactionClass) {

		if(((Class<InteractionClass>)interactionClass).getAnnotation(InteractionClass.class) != null &&
				interactionManager.getSubscribedMap().containsKey(((Class<InteractionClass>)interactionClass).getAnnotation(InteractionClass.class).name()))
			return true;
		return false;

	}

	@SuppressWarnings("rawtypes")
	public void subscribeInteractionClassModel(Class interactionClass) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, NotConnected, InvalidInteractionClassHandle, FederateServiceInvocationsAreBeingReportedViaMOM, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, InstantiationException, IllegalAccessException {
		interactionManager.subscribe(interactionClass);
	}

	@SuppressWarnings("rawtypes")
	public void unsubscribeInteractionClassModel(Class interactionClass) throws InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		interactionManager.unsubscribe(interactionClass);
	}
}

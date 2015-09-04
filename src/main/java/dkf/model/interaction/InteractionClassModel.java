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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.ParameterHandle;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederateServiceInvocationsAreBeingReportedViaMOM;
import hla.rti1516e.exceptions.InteractionClassNotDefined;
import hla.rti1516e.exceptions.InteractionClassNotPublished;
import hla.rti1516e.exceptions.InteractionParameterNotDefined;
import hla.rti1516e.exceptions.InvalidInteractionClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dkf.core.DKFRTIAmbassador;
import dkf.model.object.ObjectModelStatus;

public class InteractionClassModel {

	private static Logger logger = LogManager.getLogger(InteractionClassModel.class);

	private ObjectModelStatus status = ObjectModelStatus.UNKNOWN;
	private InteractionClassHandle  interactionClassHandle = null;

	private Map<String, ParameterHandle> mapFieldNameParameterHandle = null;

	private RTIambassador rti_ambassador = null;

	private InteractionClassEntity interactionEntity = null;
	private InteractionClassModelParser parser = null;

	private ParameterHandleValueMap parameter_values = null;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InteractionClassModel(Class interactionClass) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, NotConnected, InvalidInteractionClassHandle {
		this.rti_ambassador = DKFRTIAmbassador.getInstance();
		this.parser = new InteractionClassModelParser(interactionClass);
		initialize();
	}

	private void initialize() throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError, InvalidInteractionClassHandle {

		if(status == ObjectModelStatus.UNKNOWN){

			// Get a handle to the class.
			this.interactionClassHandle = rti_ambassador.getInteractionClassHandle(parser.getClassHandleName());
			this.mapFieldNameParameterHandle = new HashMap<String, ParameterHandle>();

			// Get handles to all the attributes.
			ParameterHandle tmp = null;
			for(String str : parser.getMapFieldCoder().keySet()){
				tmp = rti_ambassador.getParameterHandle(interactionClassHandle, str);
				mapFieldNameParameterHandle.put(str, tmp);
			}

			this.parameter_values = rti_ambassador.getParameterHandleValueMapFactory().create(mapFieldNameParameterHandle.size());
			status = ObjectModelStatus.INITIALIZED;

		}
	}

	public void subscribe() throws FederateServiceInvocationsAreBeingReportedViaMOM, InteractionClassNotDefined, SaveInProgress,
	RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {

		if(status == ObjectModelStatus.INITIALIZED){

			rti_ambassador.subscribeInteractionClass(this.interactionClassHandle);
			status = ObjectModelStatus.SUBSCRIBED;
		}
		else{
			logger.error("You can't subscribe an uninitialized interaction!");
			throw new IllegalStateException("You can't subscribe an uninitialized interaction!");
		}
	}

	public void unsubscribe() throws InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		if(status == ObjectModelStatus.SUBSCRIBED){

			rti_ambassador.unsubscribeInteractionClass(this.interactionClassHandle);
			status = ObjectModelStatus.UNKNOWN;
		}
		else{
			logger.error("You can't unsubscribe "+this.interactionClassHandle+" because it is not subscribed");
			throw new IllegalStateException("You can't unsubscribe "+this.interactionClassHandle+" because it is not subscribed");
		}
	}

	public void publish() throws InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {

		if(status == ObjectModelStatus.INITIALIZED){
			rti_ambassador.publishInteractionClass(interactionClassHandle);
			status = ObjectModelStatus.PUBLISHED;
		}
		else{
			logger.error("You can't publish an uninitialized or an already published InteractionClassModel!");
			throw new IllegalStateException("You can't publish an uninitialized or an already published InteractionClassModel!");
		}
	}

	public void unpublish() throws InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {

		if(status == ObjectModelStatus.PUBLISHED){
			rti_ambassador.unpublishInteractionClass(interactionClassHandle);
			status = ObjectModelStatus.UNKNOWN;
		}
		else{
			logger.error("You can't unpublish an uninitialized or an unpublished InteractionClassModel!");
			throw new IllegalStateException("You can't unpublish an uninitialized or an unpublished InteractionClassModel!");
		}
	}

	public void updatePublishedInteraction() throws InteractionClassNotPublished, InteractionParameterNotDefined, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {

		
		if(status == ObjectModelStatus.PUBLISHED) {
			Map<String, byte[]> ris = parser.encode(interactionEntity.getElement());
			for(Entry<String, ParameterHandle> entry : mapFieldNameParameterHandle.entrySet())
				parameter_values.put(entry.getValue(), ris.get(entry.getKey()));

			rti_ambassador.sendInteraction(interactionClassHandle, parameter_values, null);
		}
		else{
			logger.error("Can't update the ' "+interactionEntity+" ', because it is not published!");
			throw new IllegalStateException("Can't update the ' "+interactionEntity+" ', because it is not published!");
		}

	}

	public void updateSubscribedInteraction(ParameterHandleValueMap arg1) {

		if(status == ObjectModelStatus.SUBSCRIBED)
			parser.decode(interactionEntity.getElement(), mapFieldNameParameterHandle, arg1);
		else{
			logger.error("Can't update the ' "+interactionEntity+" ', because it is not subscribed !");
			throw new IllegalStateException("Can't update the ' "+interactionEntity+" ', because it is not subscribed !");
		}
	}

	public InteractionClassHandle getInteractionClassHandle() {
		return this.interactionClassHandle;
	}
	
	public void addEntity(Object interaction){
		this.interactionEntity = new InteractionClassEntity(interaction.getClass().getName()+interaction.hashCode(), interaction);
	}
	
	public InteractionClassEntity getEntity() {
		return this.interactionEntity;
	}

}

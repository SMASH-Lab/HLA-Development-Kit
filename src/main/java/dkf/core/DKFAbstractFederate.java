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

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Observer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.JDOMException;

import hla.rti1516e.exceptions.AsynchronousDeliveryAlreadyEnabled;
import hla.rti1516e.exceptions.AttributeNotDefined;
import hla.rti1516e.exceptions.AttributeNotOwned;
import hla.rti1516e.exceptions.CallNotAllowedFromWithinCallback;
import hla.rti1516e.exceptions.ConnectionFailed;
import hla.rti1516e.exceptions.CouldNotCreateLogicalTimeFactory;
import hla.rti1516e.exceptions.CouldNotOpenFDD;
import hla.rti1516e.exceptions.ErrorReadingFDD;
import hla.rti1516e.exceptions.FederateIsExecutionMember;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederateOwnsAttributes;
import hla.rti1516e.exceptions.FederateServiceInvocationsAreBeingReportedViaMOM;
import hla.rti1516e.exceptions.FederationExecutionDoesNotExist;
import hla.rti1516e.exceptions.IllegalName;
import hla.rti1516e.exceptions.InTimeAdvancingState;
import hla.rti1516e.exceptions.InconsistentFDD;
import hla.rti1516e.exceptions.InteractionClassNotDefined;
import hla.rti1516e.exceptions.InteractionClassNotPublished;
import hla.rti1516e.exceptions.InteractionParameterNotDefined;
import hla.rti1516e.exceptions.InvalidInteractionClassHandle;
import hla.rti1516e.exceptions.InvalidLocalSettingsDesignator;
import hla.rti1516e.exceptions.InvalidLookahead;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.InvalidResignAction;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.ObjectClassNotDefined;
import hla.rti1516e.exceptions.ObjectClassNotPublished;
import hla.rti1516e.exceptions.ObjectInstanceNameInUse;
import hla.rti1516e.exceptions.ObjectInstanceNameNotReserved;
import hla.rti1516e.exceptions.ObjectInstanceNotKnown;
import hla.rti1516e.exceptions.OwnershipAcquisitionPending;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RequestForTimeConstrainedPending;
import hla.rti1516e.exceptions.RequestForTimeRegulationPending;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;
import hla.rti1516e.exceptions.TimeConstrainedAlreadyEnabled;
import hla.rti1516e.exceptions.TimeRegulationAlreadyEnabled;
import hla.rti1516e.exceptions.UnsupportedCallbackModel;
import dkf.config.Configuration;
import dkf.core.DKFFederateInterface;
import dkf.exception.FirewallExeption;
import dkf.exception.PublishException;
import dkf.exception.SubscribeException;
import dkf.exception.TimeRepresentationException;
import dkf.exception.UnsubscribeException;
import dkf.exception.UpdateException;
import dkf.model.interaction.annotations.InteractionClass;
import dkf.model.object.annotations.ObjectClass;
import dkf.time.TimeFactory;
import dkf.time.TimeInterface;
import dkf.utility.SystemUtility;
import dkf.utility.access.FOMDataInspectoryFactory;


public abstract class DKFAbstractFederate implements DKFFederateInterface {

	private static final Logger logger = LogManager.getLogger(DKFAbstractFederate.class);

	private DKFHLAModule hlamodule= null;
	private Configuration config = null;
	private ExecutionThread executionThread = null;

	private TimeInterface time = null;

	public DKFAbstractFederate(DKFAbstractFederateAmbassador seefedamb) {
		this.hlamodule = new DKFHLAModule(this, seefedamb);

	}

	public void configure(Configuration config) {
		this.config = config;
	}

	public void connectToRTI(String local_settings_designator) throws ConnectionFailed, InvalidLocalSettingsDesignator, UnsupportedCallbackModel, 
	CallNotAllowedFromWithinCallback, RTIinternalError {

		this.hlamodule.connect(local_settings_designator);

	}

	public void joinFederationExecution() throws CouldNotCreateLogicalTimeFactory, InconsistentFDD, ErrorReadingFDD, CouldNotOpenFDD, SaveInProgress, 
	RestoreInProgress, NotConnected, CallNotAllowedFromWithinCallback, RTIinternalError, MalformedURLException, FederateNotExecutionMember {

		// check the status of windows firewall
		if(System.getProperty("os.name").toUpperCase().contains("WINDOWS") && SystemUtility.windowsFirewallIsEnabled()){
			logger.error("Windows Firewall is enabled. Please disable it before you ran your SEEFederate.");
			throw new FirewallExeption("Windows Firewall is enabled. Please disable it before you ran your SEEFederate.");
		}

		boolean joined = false;
		while(!joined){
			try {
				this.hlamodule.joinFederationExecution();
				joined = true;
			} catch (FederationExecutionDoesNotExist e1) {
				logger.info("Federation execution does not exist!");
				//create federation execution
				logger.info("Creating "+ config.getFederationName() +" federation execution ...");
				try {
					FOMDataInspectoryFactory inspector = new FOMDataInspectoryFactory(config.getFomDirectory());
					this.hlamodule.createFederationExecution(config.getFederationName(), inspector.getFOMsURL() , inspector.getTimestamp());
				} catch (JDOMException | IOException| TimeRepresentationException e) {
					e.printStackTrace();
				}
			}
		}

		try {
			this.time = new TimeFactory(config).createTimeInstance();
		} catch (JDOMException | IOException  e1) {
			logger.error("Error during the retrieval of the time representation");
		} catch (TimeRepresentationException e) {
			logger.error("Error during the retrieval of the time representation. It must be HLAInteger64Time or HLAfloat64Time");
		}
		this.hlamodule.setTime(time);

		try {
			this.hlamodule.configureTimeManager();
		} catch (InvalidLookahead | InTimeAdvancingState | RequestForTimeRegulationPending
				| FederateNotExecutionMember | RequestForTimeConstrainedPending  e) {
			e.printStackTrace();
		} catch (TimeRegulationAlreadyEnabled | TimeConstrainedAlreadyEnabled | AsynchronousDeliveryAlreadyEnabled e) {
			// ignored
			e.printStackTrace();
		}

		this.executionThread = new ExecutionThread(hlamodule, time);

	}

	public void diconnectFromRTI() throws InvalidResignAction, OwnershipAcquisitionPending, 
	FederateOwnsAttributes, FederateNotExecutionMember, NotConnected, 
	RTIinternalError, FederateIsExecutionMember, CallNotAllowedFromWithinCallback, 
	SaveInProgress, RestoreInProgress {

		this.executionThread.shutdown();
		this.hlamodule.disconnect();

	}

	public void subscribeSubject(Observer observer) {
		this.hlamodule.subscribeToSubject(observer);
	}

	public void unsubscribeSubject(Observer observer) {
		this.hlamodule.unsubscribeFromSubject(observer);
	}

	protected abstract void doAction();

	public Configuration getConfig() {
		return config;
	}

	public void startExecution() {
		this.executionThread.start();
	}

	public void publishElement(Object element) throws NameNotFound, FederateNotExecutionMember, NotConnected, 
	RTIinternalError, InvalidObjectClassHandle, AttributeNotDefined, 
	ObjectClassNotDefined, SaveInProgress, RestoreInProgress, 
	InstantiationException, IllegalAccessException, IllegalName, 
	ObjectInstanceNameInUse, ObjectInstanceNameNotReserved, ObjectClassNotPublished, 
	AttributeNotOwned, ObjectInstanceNotKnown, PublishException, UpdateException {

		publishElement(element, null);
	}

	public void publishElement(Object element, String name) throws NameNotFound, FederateNotExecutionMember, NotConnected, 
	RTIinternalError, InvalidObjectClassHandle, AttributeNotDefined,
	ObjectClassNotDefined, SaveInProgress, RestoreInProgress, PublishException, 
	InstantiationException, IllegalAccessException, IllegalName, ObjectInstanceNameInUse, 
	ObjectInstanceNameNotReserved, ObjectClassNotPublished, AttributeNotOwned, 
	ObjectInstanceNotKnown, UpdateException {

		if(objectClassIsValid(element.getClass()))
			this.hlamodule.publishElement(element, name);
		else{
			logger.error("ObjectElement: '"+ element +"' is not valid!");
			throw new PublishException("ObjectElement: '"+ element +"' is not valid!");
		}

	}


	public void publishInteraction(Object interaction) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, 
	NotConnected, InvalidInteractionClassHandle, InteractionClassNotDefined, 
	SaveInProgress, RestoreInProgress, InteractionClassNotPublished, 
	InteractionParameterNotDefined, PublishException {

		if(interactionClassIsValid(interaction.getClass()))
			this.hlamodule.publishInteraction(interaction);
		else{
			logger.error("Interaction: '"+ interaction +"' is not valid!");
			throw new PublishException("Interaction: '"+ interaction +"' is not valid!");
		}
	}


	public void updateElement(Object element) throws FederateNotExecutionMember, NotConnected, AttributeNotOwned, AttributeNotDefined, 
	ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, RTIinternalError, UpdateException, 
	IllegalName, ObjectInstanceNameInUse, ObjectInstanceNameNotReserved, ObjectClassNotPublished, 
	ObjectClassNotDefined {

		if(objectClassIsValid(element.getClass()))
			this.hlamodule.updateElementObject(element);
		else{
			logger.error("ObjectElement: '"+ element +"' is not valid!");
			throw new UpdateException("ObjectElement: '"+ element +"' is not valid!");
		}
	}

	public void updateInteraction(Object interaction) throws InteractionClassNotPublished, InteractionParameterNotDefined, 
	InteractionClassNotDefined, SaveInProgress, RestoreInProgress, 
	FederateNotExecutionMember, NotConnected, RTIinternalError, UpdateException {

		if(interactionClassIsValid(interaction.getClass()))
			this.hlamodule.updateInteraction(interaction);
		else{
			logger.error("Interaction: '"+ interaction +"' is not valid!");
			throw new UpdateException("Interaction: '"+ interaction +"' is not valid!");
		}
	}

	@SuppressWarnings("rawtypes")
	public void subscribeElement(Class objectClass) throws InstantiationException, IllegalAccessException, NameNotFound, 
	FederateNotExecutionMember, NotConnected, RTIinternalError, 
	InvalidObjectClassHandle, AttributeNotDefined, ObjectClassNotDefined, 
	SaveInProgress, RestoreInProgress, SubscribeException {

		if(objectClassIsValid(objectClass))
			this.hlamodule.subscribeElementObject(objectClass);
		else{
			logger.error("ObjectClass: '"+ objectClass +"' is not valid!");
			throw new SubscribeException("ObjectClass: '"+ objectClass +"' is not valid!");
		}
	}


	@SuppressWarnings("rawtypes")
	public void subscribeInteraction(Class interactionClass) throws RTIinternalError, InstantiationException, IllegalAccessException, 
	NameNotFound, FederateNotExecutionMember, NotConnected, InvalidInteractionClassHandle, 
	FederateServiceInvocationsAreBeingReportedViaMOM, InteractionClassNotDefined, SaveInProgress, 
	RestoreInProgress, SubscribeException {

		if(interactionClassIsValid(interactionClass))
			this.hlamodule.subscribeInteractionObject(interactionClass);
		else{
			logger.error("Interaction: '"+ interactionClass +"' is not valid!");
			throw new SubscribeException("Interaction: '"+ interactionClass +"' is not valid!");
		}

	}


	@SuppressWarnings("rawtypes")
	public void unsubscribeElement(Class objectClass) throws ObjectClassNotDefined, SaveInProgress, RestoreInProgress, 
	FederateNotExecutionMember, NotConnected, RTIinternalError, UnsubscribeException {

		if(objectClassIsValid(objectClass))
			this.hlamodule.unsubscribeObjectClass(objectClass);
		else{
			logger.error("ObjectClass: '"+ objectClass +"' is not valid!");
			throw new UnsubscribeException("ObjectClass: '"+ objectClass +"' is not valid!");
		}

	}

	@SuppressWarnings("rawtypes")
	public void unsubscribeInteraction(Class interactionClass) throws InteractionClassNotDefined, SaveInProgress, RestoreInProgress, 
	FederateNotExecutionMember, NotConnected, RTIinternalError, UnsubscribeException {

		if(interactionClassIsValid(interactionClass))
			this.hlamodule.unsubscribeInteractionObject(interactionClass);
		else{
			logger.error("Interaction: '"+ interactionClass +"' is not valid!");
			throw new UnsubscribeException("Interaction: '"+ interactionClass +"' is not valid!");
		}
	}

	public TimeInterface getTime() {
		return time;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean objectClassIsValid(Class objectClass) {

		if(((Class<? extends ObjectClass>)objectClass).getAnnotation(ObjectClass.class) != null &&
				((Class<? extends ObjectClass>)objectClass).getAnnotation(ObjectClass.class).name() != null)
			return true;
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean interactionClassIsValid(Class interactionClass) {
		if(((Class<? extends InteractionClass>)interactionClass).getAnnotation(InteractionClass.class) != null &&
				((Class<? extends InteractionClass>)interactionClass).getAnnotation(InteractionClass.class).name() != null)
			return true;
		return false;
	}

}

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

import java.net.URL;
import java.util.Observer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dkf.config.Configuration;
import dkf.exception.PublishException;
import dkf.exception.UnsubscribeException;
import dkf.exception.UpdateException;
import dkf.time.TimeInterface;
import dkf.utility.access.FOMDataInspectoryFactory;
import dkf.utility.access.TimeType;
import hla.rti1516e.CallbackModel;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.ResignAction;
import hla.rti1516e.TimeQueryReturn;
import hla.rti1516e.exceptions.AlreadyConnected;
import hla.rti1516e.exceptions.AsynchronousDeliveryAlreadyDisabled;
import hla.rti1516e.exceptions.AsynchronousDeliveryAlreadyEnabled;
import hla.rti1516e.exceptions.AttributeNotDefined;
import hla.rti1516e.exceptions.AttributeNotOwned;
import hla.rti1516e.exceptions.CallNotAllowedFromWithinCallback;
import hla.rti1516e.exceptions.ConnectionFailed;
import hla.rti1516e.exceptions.CouldNotCreateLogicalTimeFactory;
import hla.rti1516e.exceptions.CouldNotOpenFDD;
import hla.rti1516e.exceptions.ErrorReadingFDD;
import hla.rti1516e.exceptions.FederateAlreadyExecutionMember;
import hla.rti1516e.exceptions.FederateIsExecutionMember;
import hla.rti1516e.exceptions.FederateNameAlreadyInUse;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederateOwnsAttributes;
import hla.rti1516e.exceptions.FederateServiceInvocationsAreBeingReportedViaMOM;
import hla.rti1516e.exceptions.FederatesCurrentlyJoined;
import hla.rti1516e.exceptions.FederationExecutionAlreadyExists;
import hla.rti1516e.exceptions.FederationExecutionDoesNotExist;
import hla.rti1516e.exceptions.IllegalName;
import hla.rti1516e.exceptions.IllegalTimeArithmetic;
import hla.rti1516e.exceptions.InTimeAdvancingState;
import hla.rti1516e.exceptions.InconsistentFDD;
import hla.rti1516e.exceptions.InteractionClassNotDefined;
import hla.rti1516e.exceptions.InteractionClassNotPublished;
import hla.rti1516e.exceptions.InteractionParameterNotDefined;
import hla.rti1516e.exceptions.InvalidInteractionClassHandle;
import hla.rti1516e.exceptions.InvalidLocalSettingsDesignator;
import hla.rti1516e.exceptions.InvalidLogicalTime;
import hla.rti1516e.exceptions.InvalidLogicalTimeInterval;
import hla.rti1516e.exceptions.InvalidLookahead;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.InvalidResignAction;
import hla.rti1516e.exceptions.LogicalTimeAlreadyPassed;
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
import hla.rti1516e.exceptions.TimeConstrainedIsNotEnabled;
import hla.rti1516e.exceptions.TimeRegulationAlreadyEnabled;
import hla.rti1516e.exceptions.TimeRegulationIsNotEnabled;
import hla.rti1516e.exceptions.UnsupportedCallbackModel;
import hla.rti1516e.time.HLAinteger64Time;


public class DKFHLAModule {

	private static final Logger logger = LogManager.getLogger(DKFHLAModule.class);

	private RTIambassador rtiamb = null;
	private DKFAbstractFederate federate = null;
	private DKFAbstractFederateAmbassador fedamb = null;

	//Handle simulation time
	private TimeQueryReturn startingGALT;
	private TimeInterface time = null;

	protected DKFHLAModule(DKFAbstractFederate federate, DKFAbstractFederateAmbassador fedamb) {
		this.federate = federate;
		this.fedamb = fedamb;
	}

	protected void setTime(TimeInterface time) {
		this.time = time;
		this.fedamb.setTime(time);
	}

	protected void connect(String local_settings_designator) throws RTIinternalError, ConnectionFailed, 
															 InvalidLocalSettingsDesignator, UnsupportedCallbackModel, CallNotAllowedFromWithinCallback {

		logger.info("Connecting to HLA/RTI.");
		// Create the RTIambassador and Connect
		this.rtiamb = DKFRTIAmbassador.getInstance();

		// connect	
		try {
			rtiamb.connect(this.fedamb, CallbackModel.HLA_IMMEDIATE, local_settings_designator);
		} catch (AlreadyConnected e) {
			//ignore
		}

	}

	protected void joinFederationExecution() throws InconsistentFDD, ErrorReadingFDD, CouldNotOpenFDD, NotConnected, 
												RTIinternalError, CouldNotCreateLogicalTimeFactory, SaveInProgress, RestoreInProgress, CallNotAllowedFromWithinCallback, FederationExecutionDoesNotExist {

		Configuration config = federate.getConfig();
		
		logger.info("Loading FOMs modules...");
		FOMDataInspectoryFactory fdi = new FOMDataInspectoryFactory(config.getFomDirectory());
		URL foms[] = fdi.getFOMsURL();
		
		//join the federate into the Federation execution
		logger.info("Join the federate '"+config.getFederateName()+"' in the Federation execution '"+config.getFederationName()+"'");
		try {
			boolean joined = false;
			String federateNameSuffix = "";
			int federateNameIndex = 1;
			while (!joined) {
				try {
					if(foms.length != 0)
						rtiamb.joinFederationExecution(config.getFederateName()+federateNameSuffix, config.getFederateType(), config.getFederationName(), foms);
					else
						rtiamb.joinFederationExecution(config.getFederateName()+federateNameSuffix, config.getFederateType(), config.getFederationName());
					joined = true;
					logger.info("Federate '"+config.getFederateName()+"' in the Federation execution '"+config.getFederationName()+"'");
				} catch (FederateNameAlreadyInUse e) {
					federateNameSuffix = "-" + federateNameIndex++;
				} 
			}//while
		} catch (FederateAlreadyExecutionMember ignored) {
			//ignore
		}

	}


	protected void configureTimeManager() throws InvalidLookahead, InTimeAdvancingState, RequestForTimeRegulationPending, TimeRegulationAlreadyEnabled, 
	SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError, AsynchronousDeliveryAlreadyEnabled, 
	RequestForTimeConstrainedPending, TimeConstrainedAlreadyEnabled {
		logger.info("Set up time management.");
		//enable asynchronous delivey
		if(federate.getConfig().isAsynchronousDelivery()){
			logger.info("Asynchronous delivery enabled.");
			rtiamb.enableAsynchronousDelivery();
		}

		// Make the local logical time object.
		time.initializeLogicalTime();
		
		// Make the local logical time interval.
		time.initializeLookaheadInterval();

		// Make this federate time constrained.
		// Enable time constraint.
		if(federate.getConfig().isTimeConstrained()){
			logger.info("Time constrained enabled.");
			rtiamb.enableTimeConstrained();
			// Wait for time constraint to take affect.
			while(!fedamb.isConstrained()){
				try {
					Thread.sleep(10);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}// IF-TIME CONSTRAINED

		// Advance time to the current federation execution time.
		advanceToCurrentHLAtime();

		// Make this federate time regulating.
		// Enable time regulation.
		if(federate.getConfig().isTimeRegulating()){
			logger.debug("Time regulating enabled.");
			rtiamb.enableTimeRegulation(time.getLookaheadInterval());
			// Wait for time regulation to take affect.
			while(!fedamb.isRegulating()){
				try {
					Thread.sleep(10);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}// IF-TIME REGULATION

		// At initialization, time can advance.
		fedamb.setAdvancing(true);

	}

	private boolean advanceToCurrentHLAtime() {
		// For late joining federates, advance time to GALT.
		try {
			startingGALT = rtiamb.queryGALT();
		} catch (Exception e) {
			return false;
		}

		if (startingGALT.timeIsValid) {
			try {
				time.setLogicalTime((HLAinteger64Time)startingGALT.time);
				rtiamb.timeAdvanceRequest(startingGALT.time);
			} catch (Exception e) {
				return true ;
			}
		} 
		else
			return false;

		// Wait here for time advance grant.
		while(!fedamb.isAdvancing()){
			try {
				Thread.sleep(10);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	protected void disconnect() throws InvalidResignAction, OwnershipAcquisitionPending, 
	FederateOwnsAttributes, FederateNotExecutionMember, 
	NotConnected, CallNotAllowedFromWithinCallback, 
	RTIinternalError, FederateIsExecutionMember, 
	SaveInProgress, RestoreInProgress {

		logger.info("Disconnecting the federate from the federation execution.");
		// Disable time management.
		if (federate.getConfig().isTimeConstrained())
			try {
				rtiamb.disableTimeConstrained();
				logger.debug("Time constrained disabled.");
			} catch (TimeConstrainedIsNotEnabled e) {
				//ignored
			}

		if (federate.getConfig().isTimeRegulating())
			try {
				rtiamb.disableTimeRegulation();
				logger.debug("Time regulating disabled.");
			} catch (TimeRegulationIsNotEnabled e) {
				//ignored
			}

		if (federate.getConfig().isAsynchronousDelivery())
			try {
				rtiamb.disableAsynchronousDelivery();
				logger.debug("Asynchronous delivery disabled.");
			} catch (AsynchronousDeliveryAlreadyDisabled e) {
				//ignored
			}

		// Clean up connectivity to Federation Execution.
		rtiamb.resignFederationExecution(ResignAction.DELETE_OBJECTS_THEN_DIVEST);
		logger.debug("Resign from the federation execution");
		
		//Try to destroy the federation execution
		try {
			rtiamb.destroyFederationExecution(federate.getConfig().getFederationName());
			logger.error("Federatione execution '"+federate.getConfig().getFederationName()+"' has been destroyed");
		} catch (FederationExecutionDoesNotExist e) {
			logger.error("Federatione execution '"+federate.getConfig().getFederationName()+"' doesn't exist");
		} catch (FederatesCurrentlyJoined e) {
			logger.debug("Didn't destroi the federatione execution '"+federate.getConfig().getFederationName()+"', federates still joined");
		}
		
		// Disconnect from the RTI.
		rtiamb.disconnect();
		logger.info("The federate has been disconnected from the federation execution.");
		rtiamb = null;

	}

	public DKFAbstractFederate getFederate() {
		return this.federate;
	}

	public DKFAbstractFederateAmbassador getAmbassador() {
		return this.fedamb;
	}

	protected void makeTARequest() throws LogicalTimeAlreadyPassed, InvalidLogicalTime, 
	InTimeAdvancingState, RequestForTimeRegulationPending, 
	RequestForTimeConstrainedPending, SaveInProgress, RestoreInProgress, 
	FederateNotExecutionMember, NotConnected, RTIinternalError, IllegalTimeArithmetic {

		fedamb.setAdvancing(false);
		try {
			rtiamb.timeAdvanceRequest(time.nextTimeStep());
		} catch (InvalidLogicalTimeInterval e) {
			logger.error("Invalid LogicalTimeInterval.");
			e.printStackTrace();
		}

	}

	public void subscribeToSubject(Observer observer) {
		fedamb.addObserverToSubject(observer);
		logger.info("Subscribed to the Subject");
	}

	public void unsubscribeFromSubject(Observer observer) {
		fedamb.deleteObserverFromSubject(observer);
		logger.info("Unsubscribed from the Subject");
	}


	public void publishElement(Object element, String name) throws NameNotFound, FederateNotExecutionMember, NotConnected, 
															RTIinternalError, InvalidObjectClassHandle, PublishException, InstantiationException, 
															IllegalAccessException, AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, 
															RestoreInProgress, IllegalName, ObjectInstanceNameInUse, ObjectInstanceNameNotReserved, 
															ObjectClassNotPublished, AttributeNotOwned, ObjectInstanceNotKnown, UpdateException  {

		if(!fedamb.objectClassEntityIsAlreadyPublished(element)){
			fedamb.publishObjectClassEntity(element, name);
			logger.info("The Object '"+element+"' has been published.");
		}
		else{
			logger.warn("Object: "+element+", is already published");
			throw new PublishException("Object: "+element+", is already published.");
		}

	}

	public void publishInteraction(Object interaction) throws PublishException, RTIinternalError, NameNotFound, FederateNotExecutionMember, NotConnected, InvalidInteractionClassHandle, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, InteractionClassNotPublished, InteractionParameterNotDefined {

		if(!fedamb.interactionClassEntityIsAlreadyPublished(interaction)){
			fedamb.publishInteractionClassEntity(interaction);
			logger.info("The Interaction '"+interaction+"' has been published.");
		}
		else{
			logger.warn("Interaction: "+interaction+", is already published");
			throw new PublishException("Interaction: "+interaction+", is already published.");
		}
	}

	public void updateElementObject(Object element) throws UpdateException, FederateNotExecutionMember, NotConnected, AttributeNotOwned, 
															AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, RTIinternalError, 
															IllegalName, ObjectInstanceNameInUse, ObjectInstanceNameNotReserved, ObjectClassNotPublished, ObjectClassNotDefined {

		if(fedamb.objectClassEntityIsAlreadyPublished(element)){
			fedamb.updateObjectClassEntityOnRTI(element);
			logger.info("The Object '"+element+"' has been updated on the HLA/RTI platform.");
		}
		else{
			logger.warn("Object: "+element+", is not published. The equal() and hascode() methods must be consistent.");
			throw new UpdateException("Object: "+element+", is not published.The equal() and hashcode() methods must be consistent.");
		}
	}

	public void updateInteraction(Object interaction) throws InteractionClassNotPublished, InteractionParameterNotDefined, InteractionClassNotDefined, 
															SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError, 
															UpdateException {


		if(fedamb.interactionClassEntityIsAlreadyPublished(interaction)){
			fedamb.updateInteractionClassEntityOnRTI(interaction);
			logger.info("The Interaction '"+interaction+"' has been updated on the HLA/RTI platform.");
		}
		else{
			logger.warn("Interaction: "+interaction+", not published.");
			throw new UpdateException("You can't update an unpublished interaction.");
		}

	}
	
	@SuppressWarnings("rawtypes")
	public void subscribeElementObject(Class objectClass) throws InstantiationException, IllegalAccessException, NameNotFound, FederateNotExecutionMember, NotConnected, 
															RTIinternalError, InvalidObjectClassHandle, AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress {

		if(!fedamb.objectClassModelIsAlreadySubscribed(objectClass)){
			fedamb.subscribeObjectClassModel(objectClass);
			logger.info("The ObjectClass '"+objectClass+"' has been subscribed.");
		}
		else
			logger.warn("The ObjectClass '"+objectClass+"' is already subscribed.");
	}

	@SuppressWarnings("rawtypes")
	public void subscribeInteractionObject(Class interactionClass) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, 
																		NotConnected, InvalidInteractionClassHandle, FederateServiceInvocationsAreBeingReportedViaMOM, 
																		InteractionClassNotDefined, SaveInProgress, RestoreInProgress, InstantiationException, 
																		IllegalAccessException  {

		if(!fedamb.interactionClassModelIsAlreadySubscribed(interactionClass)){
			fedamb.subscribeInteractionClassModel(interactionClass);
			logger.info("The InteractionClass '"+interactionClass+"' has been subscribed.");
		}
		else
			logger.warn("The InteractionClass '"+interactionClass+"' is already subscribed.");
	}
	
	@SuppressWarnings("rawtypes")
	public void unsubscribeObjectClass(Class objectClass) throws ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, 
															NotConnected, RTIinternalError, UnsubscribeException {

		if(fedamb.objectClassModelIsAlreadySubscribed(objectClass)){
			fedamb.unsubscribeObjectClassModel(objectClass);
			logger.info("The ObjectClass '"+objectClass+" ' has been unsubscribed.");
		}
		else{	
			logger.error("Error during unsubscribe the "+objectClass);
			throw new UnsubscribeException("Error during unsubscribe the '"+objectClass+"'");
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void unsubscribeInteractionObject(Class interactionClass) throws InteractionClassNotDefined, SaveInProgress, RestoreInProgress, 
																			FederateNotExecutionMember, NotConnected, RTIinternalError, UnsubscribeException {

		if(fedamb.interactionClassModelIsAlreadySubscribed(interactionClass)){
			fedamb.unsubscribeInteractionClassModel(interactionClass);
			logger.info("The InteractionClass '"+interactionClass+" ' has been unsubscribed.");
		}
		else{	
			logger.error("Error during unsubscribe the "+interactionClass);
			throw new UnsubscribeException("Error during unsubscribe the "+interactionClass);
		}
	}

	protected void createFederationExecution(String federationExecutionName, URL[] fomModules,  TimeType timeType) {
		try {
			this.rtiamb.createFederationExecution(federationExecutionName, fomModules, timeType.toString());
		} catch (CouldNotCreateLogicalTimeFactory | InconsistentFDD
				| ErrorReadingFDD | CouldNotOpenFDD
				| FederationExecutionAlreadyExists | NotConnected
				| RTIinternalError e) {
			logger.error("Didn't create federation execution, it already existed");
			e.printStackTrace();
		}
		
	}
}

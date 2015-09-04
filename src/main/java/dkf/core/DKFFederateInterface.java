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

import java.net.MalformedURLException;
import java.util.Observer;

import dkf.config.Configuration;
import dkf.exception.PublishException;
import dkf.exception.SubscribeException;
import dkf.exception.UnsubscribeException;
import dkf.exception.UpdateException;
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
import hla.rti1516e.exceptions.IllegalName;
import hla.rti1516e.exceptions.InconsistentFDD;
import hla.rti1516e.exceptions.InteractionClassNotDefined;
import hla.rti1516e.exceptions.InteractionClassNotPublished;
import hla.rti1516e.exceptions.InteractionParameterNotDefined;
import hla.rti1516e.exceptions.InvalidInteractionClassHandle;
import hla.rti1516e.exceptions.InvalidLocalSettingsDesignator;
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
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;
import hla.rti1516e.exceptions.UnsupportedCallbackModel;

public interface DKFFederateInterface {
	
	/**
	 * Configures the HLA federate on the HLA Federation
	 * @param config
	 */
	public void configure(Configuration config);
	
	/**
	 * Connects the HLA Federate on the HLA Federation
	 * 
	 * @param local_settings_designator represents the "local settings designator" string related to the specific
	 * RTI vendor
	 * <p>
	 * For PITCH RTI the local_settings_designator parameter MUST be "crcHost=" + {crc_host} + "\ncrcPort=" + {crc_port}<br>
	 * e.g. "crcHost=localhost\ncrcPort=8989"
	 * <p>
	 * For MAK RTI the local_settings_designator parameter MUST be an empty string<br>
	 * e.g. ""
	 * @throws RTIinternalError
	 * @throws CallNotAllowedFromWithinCallback 
	 * @throws UnsupportedCallbackModel 
	 * @throws InvalidLocalSettingsDesignator 
	 * @throws ConnectionFailed 
	 * @throws NotConnected 
	 * @throws FederateNotExecutionMember 
	 */
	public void connectToRTI(String local_settings_designator) throws RTIinternalError, ConnectionFailed, InvalidLocalSettingsDesignator, UnsupportedCallbackModel, CallNotAllowedFromWithinCallback;
	
	/**
	 * Joins the Federate to the specified Federation Execution.
	 * @throws MalformedURLException 
	 * @throws RTIinternalError 
	 * @throws CallNotAllowedFromWithinCallback 
	 * @throws NotConnected 
	 * @throws RestoreInProgress 
	 * @throws SaveInProgress 
	 * @throws CouldNotOpenFDD 
	 * @throws ErrorReadingFDD 
	 * @throws InconsistentFDD 
	 * @throws CouldNotCreateLogicalTimeFactory 
	 * @throws FederateNotExecutionMember 
	 */
	public void joinFederationExecution() throws CouldNotCreateLogicalTimeFactory, InconsistentFDD, ErrorReadingFDD, CouldNotOpenFDD, SaveInProgress, RestoreInProgress, NotConnected, CallNotAllowedFromWithinCallback, RTIinternalError, MalformedURLException, FederateNotExecutionMember;
	
	/**
	 * Disconnects the HLA Federate from the HLA Federation
	 * @throws RTIinternalError 
	 * @throws CallNotAllowedFromWithinCallback 
	 * @throws NotConnected 
	 * @throws FederateNotExecutionMember 
	 * @throws FederateOwnsAttributes 
	 * @throws OwnershipAcquisitionPending 
	 * @throws InvalidResignAction 
	 * @throws FederateIsExecutionMember 
	 * @throws RestoreInProgress 
	 * @throws SaveInProgress 
	 */
	public void diconnectFromRTI() throws InvalidResignAction, OwnershipAcquisitionPending, FederateOwnsAttributes, FederateNotExecutionMember, NotConnected, CallNotAllowedFromWithinCallback, RTIinternalError, FederateIsExecutionMember, SaveInProgress, RestoreInProgress;
	
	/**
	 * Starts Simulation Execution
	 */
	public void startExecution();
	
	/**
	 * Subscribes the HLA Federate to the Subject, in order to be notified about updates.
	 * @param observer
	 */
	public void subscribeSubject(Observer observer);
	
	/**
	 * Unsubscribes the HLA Federate from the Subject.
	 * @param observer
	 */
	public void unsubscribeSubject(Observer observer);
	
	/**
	 * Publishes the Element on HLA/RTI platform
	 * @param element
	 * @throws NameNotFound
	 * @throws FederateNotExecutionMember
	 * @throws NotConnected
	 * @throws RTIinternalError
	 * @throws InvalidObjectClassHandle
	 * @throws AttributeNotDefined
	 * @throws ObjectClassNotDefined
	 * @throws SaveInProgress
	 * @throws RestoreInProgress
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalName
	 * @throws ObjectInstanceNameInUse
	 * @throws ObjectInstanceNameNotReserved
	 * @throws ObjectClassNotPublished
	 * @throws AttributeNotOwned
	 * @throws ObjectInstanceNotKnown
	 * @throws PublishException
	 * @throws UpdateException
	 */
	public void publishElement(Object element) throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError, InvalidObjectClassHandle, AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, InstantiationException, IllegalAccessException, IllegalName, ObjectInstanceNameInUse, ObjectInstanceNameNotReserved, ObjectClassNotPublished, AttributeNotOwned, ObjectInstanceNotKnown, PublishException, UpdateException;
	
	/**
	 * Publishes the Element on HLA/RTI platform with the specified name
	 * @param element
	 * @param name
	 * @throws NameNotFound
	 * @throws FederateNotExecutionMember
	 * @throws NotConnected
	 * @throws RTIinternalError
	 * @throws InvalidObjectClassHandle
	 * @throws AttributeNotDefined
	 * @throws ObjectClassNotDefined
	 * @throws SaveInProgress
	 * @throws RestoreInProgress
	 * @throws PublishException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalName
	 * @throws ObjectInstanceNameInUse
	 * @throws ObjectInstanceNameNotReserved
	 * @throws ObjectClassNotPublished
	 * @throws AttributeNotOwned
	 * @throws ObjectInstanceNotKnown
	 * @throws UpdateException
	 */
	public void publishElement(Object element, String name) throws NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError, InvalidObjectClassHandle, AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, PublishException, InstantiationException, IllegalAccessException, IllegalName, ObjectInstanceNameInUse, ObjectInstanceNameNotReserved, ObjectClassNotPublished, AttributeNotOwned, ObjectInstanceNotKnown, UpdateException;
	
	/**
	 * Publishes the Interaction on HLA/RTI platform
	 * @param element
	 * @throws RTIinternalError
	 * @throws NameNotFound
	 * @throws FederateNotExecutionMember
	 * @throws NotConnected
	 * @throws InvalidInteractionClassHandle
	 * @throws PublishException
	 * @throws InteractionParameterNotDefined 
	 * @throws InteractionClassNotPublished 
	 * @throws RestoreInProgress 
	 * @throws SaveInProgress 
	 * @throws InteractionClassNotDefined 
	 */
	public void publishInteraction(Object element) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, NotConnected, InvalidInteractionClassHandle, PublishException, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, InteractionClassNotPublished, InteractionParameterNotDefined;
	
	
	/**
	 * Updates the Element on HLA/RTI platform
	 * @param element
	 * @throws FederateNotExecutionMember
	 * @throws NotConnected
	 * @throws AttributeNotOwned
	 * @throws AttributeNotDefined
	 * @throws ObjectInstanceNotKnown
	 * @throws SaveInProgress
	 * @throws RestoreInProgress
	 * @throws RTIinternalError
	 * @throws UpdateException
	 * @throws ObjectClassNotDefined 
	 * @throws ObjectClassNotPublished 
	 * @throws ObjectInstanceNameNotReserved 
	 * @throws ObjectInstanceNameInUse 
	 * @throws IllegalName 
	 */
	public void updateElement(Object element) throws FederateNotExecutionMember, NotConnected, AttributeNotOwned, AttributeNotDefined, ObjectInstanceNotKnown, SaveInProgress, RestoreInProgress, RTIinternalError, UpdateException, IllegalName, ObjectInstanceNameInUse, ObjectInstanceNameNotReserved, ObjectClassNotPublished, ObjectClassNotDefined;
	
	/**
	 * Updates the Interaction on HLA/RTI platform
	 * @param interaction
	 * @throws InteractionClassNotPublished
	 * @throws InteractionParameterNotDefined
	 * @throws InteractionClassNotDefined
	 * @throws SaveInProgress
	 * @throws RestoreInProgress
	 * @throws FederateNotExecutionMember
	 * @throws NotConnected
	 * @throws RTIinternalError
	 * @throws UpdateException
	 */
	public void updateInteraction(Object interaction) throws InteractionClassNotPublished, InteractionParameterNotDefined, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError, UpdateException;
	
	/**
	 * Subscribes an ElementObject
	 * @param objectClass
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NameNotFound
	 * @throws FederateNotExecutionMember
	 * @throws NotConnected
	 * @throws RTIinternalError
	 * @throws InvalidObjectClassHandle
	 * @throws AttributeNotDefined
	 * @throws ObjectClassNotDefined
	 * @throws SaveInProgress
	 * @throws RestoreInProgress
	 * @throws SubscribeException
	 */
	@SuppressWarnings("rawtypes")
	public void subscribeElement(Class objectClass) throws InstantiationException, IllegalAccessException, NameNotFound, FederateNotExecutionMember, NotConnected, RTIinternalError, InvalidObjectClassHandle, AttributeNotDefined, ObjectClassNotDefined, SaveInProgress, RestoreInProgress, SubscribeException;
	
	/**
	 * Subscribes an InteractionObject
	 * @param interactionClass
	 * @throws RTIinternalError
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NameNotFound
	 * @throws FederateNotExecutionMember
	 * @throws NotConnected
	 * @throws InvalidInteractionClassHandle
	 * @throws FederateServiceInvocationsAreBeingReportedViaMOM
	 * @throws InteractionClassNotDefined
	 * @throws SaveInProgress
	 * @throws RestoreInProgress
	 * @throws SubscribeException 
	 */
	@SuppressWarnings("rawtypes")
	public void subscribeInteraction(Class interactionClass) throws RTIinternalError, InstantiationException, IllegalAccessException, NameNotFound, FederateNotExecutionMember, NotConnected, InvalidInteractionClassHandle, FederateServiceInvocationsAreBeingReportedViaMOM, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, SubscribeException;
	
	/**
	 * Unsubscribes an ElementObject
	 * @param objectClass
	 * @throws ObjectClassNotDefined
	 * @throws SaveInProgress
	 * @throws RestoreInProgress
	 * @throws FederateNotExecutionMember
	 * @throws NotConnected
	 * @throws RTIinternalError
	 * @throws UnsubscribeException
	 */
	@SuppressWarnings("rawtypes")
	public void unsubscribeElement(Class objectClass) throws ObjectClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError, UnsubscribeException;
	
	/**
	 * Unsubscribes an InteractionObject
	 * @param objectClass
	 * @throws InteractionClassNotDefined
	 * @throws SaveInProgress
	 * @throws RestoreInProgress
	 * @throws FederateNotExecutionMember
	 * @throws NotConnected
	 * @throws RTIinternalError
	 * @throws UnsubscribeException
	 */
	@SuppressWarnings("rawtypes")
	public void unsubscribeInteraction(Class objectClass) throws InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError, UnsubscribeException;

}

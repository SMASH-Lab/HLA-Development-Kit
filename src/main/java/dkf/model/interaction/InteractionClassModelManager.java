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

import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.ParameterHandleValueMap;
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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import dkf.model.interaction.annotations.InteractionClass;

@SuppressWarnings("rawtypes")
public class InteractionClassModelManager {

	//maps for published element
	private Map<String, InteractionClassModel> published = null;
	private Map<String, InteractionClassEntity> mapInstanceNameInteractionClassEntity = null;
	
	//maps for subscribed element
	private Map<String, InteractionClassModel> subscribed = null;
	private BidiMap<InteractionClassHandle, Class> mapInteractionClassHandleClass = null;


	public InteractionClassModelManager() {
		this.published = new HashMap<String, InteractionClassModel>();
		this.mapInstanceNameInteractionClassEntity = new HashMap<String, InteractionClassEntity>();
		
		this.subscribed = new HashMap<String, InteractionClassModel>();
		this.mapInteractionClassHandleClass = new DualHashBidiMap<InteractionClassHandle, Class>();
	}

	public Map<String, InteractionClassModel> getPublishedMap() {
		return this.published;
	}
	
	public Map<String, InteractionClassModel> getSubscribedMap() {
		return this.subscribed;
	}

	public void publish(Object interaction) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, NotConnected, InvalidInteractionClassHandle, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, InteractionClassNotPublished, InteractionParameterNotDefined {

		InteractionClassModel icm = published.get(interaction.getClass().getAnnotation(InteractionClass.class).name());

		if(icm == null){
			icm = new InteractionClassModel(interaction.getClass());
			icm.addEntity(interaction);
			icm.publish();
			this.published.put(interaction.getClass().getAnnotation(InteractionClass.class).name(), icm);
			this.mapInstanceNameInteractionClassEntity.put(icm.getEntity().getInstanceName(), icm.getEntity());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void subscribe(Class interactionClass) throws RTIinternalError, NameNotFound, FederateNotExecutionMember, NotConnected, InvalidInteractionClassHandle, FederateServiceInvocationsAreBeingReportedViaMOM, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, InstantiationException, IllegalAccessException {
		
		InteractionClassModel icm = new InteractionClassModel(interactionClass);
		icm.addEntity(interactionClass.newInstance());
		icm.subscribe();
		this.subscribed.put(((Class<? extends InteractionClass>)interactionClass).getAnnotation(InteractionClass.class).name(), icm);
		this.mapInteractionClassHandleClass.put(icm.getInteractionClassHandle(), interactionClass);
	}
	
	@SuppressWarnings("unchecked")
	public void unsubscribe(Class interactionClass) throws InteractionClassNotDefined, SaveInProgress, RestoreInProgress, FederateNotExecutionMember, NotConnected, RTIinternalError {
		this.subscribed.remove(((Class<? extends InteractionClass>)interactionClass).getAnnotation(InteractionClass.class).name()).unsubscribe();
		this.mapInteractionClassHandleClass.inverseBidiMap().remove(interactionClass);
	}

	public boolean interactionInstanceHandleIsSubscribed(InteractionClassHandle arg0) {
		return this.mapInteractionClassHandleClass.get(arg0) != null;
	}

	@SuppressWarnings("unchecked")
	public Object receiveInteraction(InteractionClassHandle arg0, ParameterHandleValueMap arg1) {
			
		InteractionClassModel icm = subscribed.get(((Class<InteractionClass>)mapInteractionClassHandleClass.get(arg0)).getAnnotation(InteractionClass.class).name());
		if(icm != null){
			icm.updateSubscribedInteraction(arg1);
			return icm.getEntity().getElement();
		}
		return null;
	}

}

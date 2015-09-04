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

import hla.rti1516e.ObjectInstanceHandle;

public class ObjectClassEntity extends AbstractClassEntity {
	
	private ObjectInstanceHandle instanceHandle = null;
	private NameReservationStatus status = NameReservationStatus.UNKNOWN;

	
	public ObjectClassEntity(String instanceName, Object element) {
		super(element);
		this.instanceName = instanceName;
	}


	public ObjectClassEntity(ObjectInstanceHandle instanceHandle, Object element) {
		super(element);
		this.instanceHandle = instanceHandle;
	}


	public ObjectInstanceHandle getObjectInstanceHandle() {
		return this.instanceHandle;
	}
	
	public void setObjectInstanceHandle(ObjectInstanceHandle instanceHandle) {
		this.instanceHandle = instanceHandle;
	}
	
	public void setStatus(NameReservationStatus status){
		this.status = status;
	}
	
	public NameReservationStatus getStatus(){
		return this.status;
	}
	
}

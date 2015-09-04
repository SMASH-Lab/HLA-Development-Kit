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

public abstract class AbstractClassEntity {
	
	protected String instanceName = null;
	protected Object element = null;
	
	public AbstractClassEntity(Object element) {
		this.element = element;
	}
	
	public String getInstanceName() {
		return this.instanceName;
	}
	
	public Object getElement() {
		return element;
	}
	
	public void setElement(Object element) {
		this.element = element;
		
	}
	
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

}

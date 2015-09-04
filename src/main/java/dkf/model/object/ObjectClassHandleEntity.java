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

import hla.rti1516e.ObjectClassHandle;

public class ObjectClassHandleEntity {
	
	private ObjectClassHandle objectClassHandle = null;
	
	private String instanceName = null;

	public ObjectClassHandleEntity(ObjectClassHandle objectClassHandle, String instanceName) {
		this.objectClassHandle = objectClassHandle;
		this.instanceName = instanceName;
	}

	public ObjectClassHandle getObjectClassHandle() {
		return objectClassHandle;
	}

	public void setObjectClassHandle(ObjectClassHandle objectClassHandle) {
		this.objectClassHandle = objectClassHandle;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((objectClassHandle == null) ? 0 : objectClassHandle
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObjectClassHandleEntity other = (ObjectClassHandleEntity) obj;
		if (objectClassHandle == null) {
			if (other.objectClassHandle != null)
				return false;
		} else if (!objectClassHandle.equals(other.objectClassHandle))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ObjectClassHandleEntity [objectClassHandle="
				+ objectClassHandle + ", instanceName=" + instanceName + "]";
	}
	
}

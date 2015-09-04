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
package dkf.config;

import java.io.File;

import org.joda.time.DateTime;

/**
 *
 * The root interface in the Configuration hierarchy.
 * A configuration is a set of parameters, which are used by the DKF framework
 * to set up a Federate on the specific RTI infrastructure. 
 * 
 * @author SMASH-Lab University of Calabria
 * @version 0.1
 * 
 */
public interface ConfigurationInterface {
	
	/**
	 * @return The IP address of the host
	 */
	public String getCrcHost();

	/**
	 * @return The CRC port
	 */
	public int getCrcPort();

	/**
	 * @return The name of the SEE Federation
	 */
	public String getFederationName();
	
	/**
	 * @return The name of the SEE Federate
	 */
	public String getFederateName();
	
	/**
	 * @return The type of the SEE Federate
	 */
	public String getFederateType();
	
	/**
	 * @return The directory that contains the FOMs module
	 */
	public File getFomDirectory();
	
	/**
	 * @return true: if the Asynchronous Delivery is enabled
	 */
	public boolean isAsynchronousDelivery();
	
	/**
	 * @return true: if the Realtime Clock is enabled
	 */
	public boolean isRealtime();
	
	/**
	 * @return true: if the Time Constrained is enabled
	 */
	public boolean isTimeConstrained();

	/**
	 * @return true: if the Time Regulating is enabled
	 */
	public boolean isTimeRegulating();
	
	/**
	 * Sets the CRC host to the specified value passed as parameter
	 * @param crcHost the new value for the CRC host
	 */
	public void setCrcHost(String crcHost);
	
	/**
	 * Sets the CRC port to the specified value passed as parameter
	 * @param crcHost the new value for the CRC port
	 */
	public void setCrcPort(int crcHost);
	
	/**
	 * Sets the Asynchronous delivery to the specified value passed as parameter
	 * @param bool the new value for the Asynchronous delivery
	 */
	public void setAsynchronousDelivery(boolean bool);
	
	/**
	 * Sets the federation name to the specified value passed as parameter
	 * @param federationName the new value for the federation name
	 */
	public void setFederationName(String federationName);
	
	/**
	 * Sets the name of the federate to the specified value passed as parameter
	 * @param federateName the new name
	 */
	public void setFederateName(String federateName);
	
	/**
	 * Sets the type of the federate to the specified value passed as parameter
	 * @param federateType the new type
	 */
	public void setFederateType(String federateType);
	
	/**
	 * Sets the FOMs directory to the specified value passed as parameter
	 * @param dir the FOMs directory
	 */
	public void setFomDirectory(File dir);
	
	/**
	 * Sets the realtime clock to the specified value passed as parameter
	 * @param bool the new value for the realtime clock 
	 */
	public void setRealtime(boolean bool);
	
	/**
	 * Sets the time regulating to the specified value passed as parameter
	 * @param bool the new value for the time regulating 
	 */
	public void setTimeRegulating(boolean bool);
	
	/**
	 * Sets the time constrained to the specified value passed as parameter
	 * @param bool the new value for the time constrained
	 */
	public void setTimeConstrained(boolean bool);
	
	/**
	 * 
	 * @return The simulation start ephoc
	 */
	public DateTime getSimulationEphoc();
	
	/**
	 * Sets the simulation start ephoc
	 * @param simulationEphoc
	 */
	public void setSimulationEphoc(DateTime simulationEphoc);
	
}

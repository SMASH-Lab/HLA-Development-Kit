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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;

/**
 * 
 * @author SMASH-Lab University of Calabria
 * @version 0.1
 * 
 */
public class Configuration implements ConfigurationInterface {
	
	
	private static final String IP_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
									        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
									        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
									        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	private boolean isAsynchronousDelivery;
	private String crcHost;
	private int crcPort;
	private String federationName;
	private String federateName;
	private String federateType;
	private File fomDirectory;
	private boolean isRealtime;
	private boolean isTimeConstrained;
	private boolean isTimeRegulating;
	private DateTime simulationEphoc;
	
	protected Configuration() {}

	public boolean isAsynchronousDelivery() {
		return isAsynchronousDelivery;
	}

	public void setAsynchronousDelivery(boolean isAsynchronousDelivery) {
		this.isAsynchronousDelivery = isAsynchronousDelivery;
	}

	public String getCrcHost() {
		return crcHost;
	}

	public void setCrcHost(String crcHost) {
		if( crcHost == null || (!crcHost.equalsIgnoreCase("localhost") && !ipIsValid(crcHost)) )
			throw new IllegalArgumentException("Invalid IP Address");
		this.crcHost = crcHost;
	}
	
	public int getCrcPort() {
		return crcPort;
	}
	
	public void setCrcPort(int crcPort) {
		if(crcPort < 1024 || crcPort > 49151)
			throw new IllegalArgumentException("Invalid CRC Port, The CRC Port must be in the range [1024 - 49151]");
		this.crcPort = crcPort;
	}

	public String getFederationName() {
		return federationName;
	}
	
	public void setFederationName(String federationName) {
		this.federationName = federationName;
	}
	
	public String getFederateName() {
		return federateName;
	}
	
	public void setFederateName(String federateName) {
		this.federateName = federateName;
	}

	public String getFederateType() {
		return federateType;
	}

	public void setFederateType(String federateType) {
		this.federateType = federateType;
	}
	
	public File getFomDirectory() {
		return fomDirectory;
	}

	public void setFomDirectory(File fomDirectory) {
		if(fomDirectory == null || !fomDirectory.isDirectory())
			throw new IllegalArgumentException("The parameter must be a directory");
		this.fomDirectory = fomDirectory;
	}

	public boolean isRealtime() {
		return isRealtime;
	}

	public void setRealtime(boolean isRealtime) {
		this.isRealtime = isRealtime;
	}

	public boolean isTimeConstrained() {
		return isTimeConstrained;
	}

	public void setTimeConstrained(boolean isTimeConstrained) {
		this.isTimeConstrained = isTimeConstrained;
	}

	public boolean isTimeRegulating() {
		return isTimeRegulating;
	}

	public void setTimeRegulating(boolean isTimeRegulating) {
		this.isTimeRegulating = isTimeRegulating;
	}
	
	private boolean ipIsValid(String ip){          

	      Pattern pattern = Pattern.compile(IP_PATTERN);
	      Matcher matcher = pattern.matcher(ip);
	      return matcher.matches();             
	}
	
	public DateTime getSimulationEphoc() {
		return simulationEphoc;
	}
	
	public void setSimulationEphoc(DateTime simulationEphoc) {
		this.simulationEphoc = simulationEphoc;
	}
	
}

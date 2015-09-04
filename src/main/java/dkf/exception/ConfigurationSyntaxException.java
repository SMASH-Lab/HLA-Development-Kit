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
package dkf.exception;

/**
* 
* @author SMASH-Lab University of Calabria
* @version 0.1
* 
*/
public class ConfigurationSyntaxException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ConfigurationSyntaxException(String message){
		super(message);
	}
	
	public ConfigurationSyntaxException(String message, Throwable cause){
		super(message, cause);
	}
    
	public ConfigurationSyntaxException(Throwable cause){
		super(cause);
	} 

}

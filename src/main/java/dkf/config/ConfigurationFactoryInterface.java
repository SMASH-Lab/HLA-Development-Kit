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
import java.io.FileNotFoundException;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
* 
* @author SMASH-Lab University of Calabria
* @version 0.1
* 
*/
public interface ConfigurationFactoryInterface {
		
	/**
	 * Imports the configuration parameters from a given file
	 * @param file configuration file
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public Configuration importConfiguration(File file) throws FileNotFoundException, JsonParseException, JsonMappingException, IOException;
	
	/**
	 * Export the configuration parameters into a given directory
	 * @param config AbstractConfiguration object
	 * @param outputFile output file
	 * @throws IOException 
	 */
	public void exportConfiguration(Configuration config, File outputFile) throws IOException;

	
	/**
	 * Create an empty Configuration object
	 * @return an empty Configuration object
	 */
	public Configuration createConfiguration();
	
}
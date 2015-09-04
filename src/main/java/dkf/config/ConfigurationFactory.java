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
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class ConfigurationFactory implements ConfigurationFactoryInterface {

	Logger logger = LogManager.getLogger(ConfigurationFactory.class);

	private ObjectMapper mapper = null;

	public Configuration importConfiguration(File file) throws JsonParseException, JsonMappingException, IOException {
		logger.info("Importing the DKF configuration file");
		return getObjectMapper().readValue(file, Configuration.class);
		
	}


	public void exportConfiguration(Configuration config, File outputFile) throws JsonGenerationException, JsonMappingException, IOException {
		logger.info("Exporting the DKF configuration file");
		getObjectMapper().writerWithDefaultPrettyPrinter().writeValue(outputFile, config);

	}

	public Configuration createConfiguration() {
		return new Configuration();
	}
	
	private ObjectMapper getObjectMapper() {
		if(mapper == null){
			 mapper = new ObjectMapper();
			 mapper.registerModule(new JodaModule());
			 mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
			 mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		}
		return mapper;
	}
	
}

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

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ConfigurationFactoryTest {

	private ConfigurationFactory factory = null;
	private File confiuration = null;
	private Configuration conf = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

	}

	@Before
	public void setUp() throws Exception {
		
		factory = new ConfigurationFactory();
		confiuration = new File(getClass().getResource("/configuration.json").toURI());
		conf = factory.importConfiguration(confiuration);
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test(expected = NullPointerException.class)
	public void invalidConfigurationFile() throws Exception {
		
		factory = new ConfigurationFactory();
		conf = factory.importConfiguration(new File(getClass().getResource("/configuration1.json").toURI()));
		
	}

	@Test
	public void testImportConfiguration() {
		assertNotNull(conf);
		assertTrue(conf.isAsynchronousDelivery());
		assertTrue(conf.getCrcHost().equals("localhost"));
		assertTrue(conf.getCrcPort() == 8989);
		assertTrue(conf.getFederateName().equals("Consumer"));
		assertTrue(conf.getFederationName().equals("SEE 2015"));
		assertNotNull(conf.getFomDirectory());
		assertFalse(conf.isRealtime()); 
		assertNotNull(conf.getSimulationEphoc());
		assertTrue(conf.isTimeConstrained());
		assertFalse(conf.isTimeRegulating());
	}

	@Test
	public void testExportConfiguration() throws JsonGenerationException, JsonMappingException, IOException, URISyntaxException {
		Configuration tmp = factory.createConfiguration();
		tmp.setAsynchronousDelivery(true);
		tmp.setCrcHost("localhost");
		tmp.setCrcPort(8989);
		tmp.setFederateName("Consumer");
		tmp.setFederationName("SEE 2015");
		tmp.setFomDirectory(new File("C:/Users/SEI01/Desktop/StarterKit project/out"));
		tmp.setRealtime(false);
		tmp.setTimeConstrained(true);
		tmp.setTimeRegulating(false);
		tmp.setSimulationEphoc(new DateTime(10, 10, 10, 10, 10));
		factory.exportConfiguration(tmp, new File("C:/Users/SEI01/Desktop/StarterKit project/out/export.json"));
	}

	@Test
	public void testCreateConfiguration() throws URISyntaxException {
		Configuration tmp = factory.createConfiguration();
		tmp.setAsynchronousDelivery(true);
		tmp.setCrcHost("localhost");
		tmp.setCrcPort(8989);
		tmp.setFederateName("Consumer");
		tmp.setFederationName("SEE 2015");
		tmp.setFomDirectory(new File("C:/Users/SEI01/Desktop/StarterKit project/out"));
		tmp.setRealtime(false);
		tmp.setTimeConstrained(true);
		tmp.setTimeRegulating(false);
		tmp.setSimulationEphoc(new DateTime(10, 10, 10, 10, 10));
		
		assertNotNull(tmp);
		assertTrue(tmp.isAsynchronousDelivery());
		assertTrue(tmp.getCrcHost().equals("localhost"));
		assertTrue(tmp.getCrcPort() == 8989);
		assertTrue(tmp.getFederateName().equals("Consumer"));
		assertTrue(tmp.getFederationName().equals("SEE 2015"));
		assertTrue(tmp.getFomDirectory().exists());
		assertFalse(tmp.isRealtime()); 
		assertNotNull(tmp.getSimulationEphoc());
		assertTrue(tmp.isTimeConstrained());
		assertFalse(tmp.isTimeRegulating());
		
	}

}

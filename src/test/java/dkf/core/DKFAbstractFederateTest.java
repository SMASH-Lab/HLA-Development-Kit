package dkf.core;

import hla.rti1516e.exceptions.CallNotAllowedFromWithinCallback;
import hla.rti1516e.exceptions.ConnectionFailed;
import hla.rti1516e.exceptions.CouldNotCreateLogicalTimeFactory;
import hla.rti1516e.exceptions.CouldNotOpenFDD;
import hla.rti1516e.exceptions.ErrorReadingFDD;
import hla.rti1516e.exceptions.FederateIsExecutionMember;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederateOwnsAttributes;
import hla.rti1516e.exceptions.InconsistentFDD;
import hla.rti1516e.exceptions.InvalidLocalSettingsDesignator;
import hla.rti1516e.exceptions.InvalidResignAction;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.OwnershipAcquisitionPending;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;
import hla.rti1516e.exceptions.UnsupportedCallbackModel;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dkf.config.Configuration;
import dkf.config.ConfigurationFactory;

public class DKFAbstractFederateTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testJoinIntoFederationExecution() throws URISyntaxException, JsonParseException, JsonMappingException, IOException, ConnectionFailed, InvalidLocalSettingsDesignator, UnsupportedCallbackModel, CallNotAllowedFromWithinCallback, RTIinternalError, CouldNotCreateLogicalTimeFactory, InconsistentFDD, ErrorReadingFDD, CouldNotOpenFDD, SaveInProgress, RestoreInProgress, NotConnected, FederateNotExecutionMember, InvalidResignAction, OwnershipAcquisitionPending, FederateOwnsAttributes, FederateIsExecutionMember {

		ConfigurationFactory factory = new ConfigurationFactory();
		File confiuration = new File(getClass().getResource("/configuration.json").toURI());
		Configuration conf = factory.importConfiguration(confiuration);
		
		DKFAbstractFederateAmbassador ambassador = new DKFAbstractFederateAmbassador() {
			
		};
		
		DKFAbstractFederate federate = new DKFAbstractFederate(ambassador) {

			@Override
			protected void doAction() {
				// TODO Auto-generated method stub
				
			}
			
		};
		
		federate.configure(conf);
		federate.connectToRTI("crcHost=" + conf.getCrcHost() + "\ncrcPort=" +conf.getCrcPort());
		federate.joinFederationExecution();
		federate.diconnectFromRTI();
		
	}

}

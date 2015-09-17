package dkf.core.subscriber;

import hla.rti1516e.exceptions.AttributeNotDefined;
import hla.rti1516e.exceptions.AttributeNotOwned;
import hla.rti1516e.exceptions.CallNotAllowedFromWithinCallback;
import hla.rti1516e.exceptions.ConnectionFailed;
import hla.rti1516e.exceptions.CouldNotCreateLogicalTimeFactory;
import hla.rti1516e.exceptions.CouldNotOpenFDD;
import hla.rti1516e.exceptions.ErrorReadingFDD;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederationExecutionDoesNotExist;
import hla.rti1516e.exceptions.IllegalName;
import hla.rti1516e.exceptions.InconsistentFDD;
import hla.rti1516e.exceptions.InvalidLocalSettingsDesignator;
import hla.rti1516e.exceptions.InvalidObjectClassHandle;
import hla.rti1516e.exceptions.NameNotFound;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.ObjectClassNotDefined;
import hla.rti1516e.exceptions.ObjectClassNotPublished;
import hla.rti1516e.exceptions.ObjectInstanceNameInUse;
import hla.rti1516e.exceptions.ObjectInstanceNameNotReserved;
import hla.rti1516e.exceptions.ObjectInstanceNotKnown;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;
import hla.rti1516e.exceptions.UnsupportedCallbackModel;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dkf.config.ConfigurationFactory;
import dkf.exception.PublishException;
import dkf.exception.UpdateException;

public class RoverTest {

	private File conf = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		conf = new File(getClass().getResource("/configuration.json").toURI());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws JsonParseException, JsonMappingException, ConnectionFailed, InvalidLocalSettingsDesignator, UnsupportedCallbackModel, CallNotAllowedFromWithinCallback, RTIinternalError, CouldNotCreateLogicalTimeFactory, FederationExecutionDoesNotExist, InconsistentFDD, ErrorReadingFDD, CouldNotOpenFDD, SaveInProgress, RestoreInProgress, NotConnected, MalformedURLException, FederateNotExecutionMember, NameNotFound, InvalidObjectClassHandle, AttributeNotDefined, ObjectClassNotDefined, InstantiationException, IllegalAccessException, IllegalName, ObjectInstanceNameInUse, ObjectInstanceNameNotReserved, ObjectClassNotPublished, AttributeNotOwned, ObjectInstanceNotKnown, PublishException, UpdateException, IOException {

		RoverFederateAmbassador ambassador = new RoverFederateAmbassador();
		RoverFederate federate = new RoverFederate(ambassador);

		// start execution
		federate.configureAndStart(new ConfigurationFactory().importConfiguration(conf));
	}

}

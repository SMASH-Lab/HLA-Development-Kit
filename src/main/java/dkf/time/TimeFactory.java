package dkf.time;

import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.JDOMException;

import dkf.config.Configuration;
import dkf.exception.TimeRepresentationException;
import dkf.utility.access.FOMDataInspectoryFactory;


public class TimeFactory implements TimeFactoryInterface {

	Logger logger = LogManager.getLogger(TimeFactory.class);

	private FOMDataInspectoryFactory inspector = null;
	private Configuration config = null;

	public TimeFactory(Configuration config) {
		this.config  = config;
	}

	@Override
	public TimeInterface createTimeInstance() throws FederateNotExecutionMember, NotConnected, RTIinternalError, JDOMException, IOException, TimeRepresentationException {
		inspector = new FOMDataInspectoryFactory(config.getFomDirectory());
		
		switch (inspector.getTimestamp()) {
		case HLAinteger64Time:
			return new Integer64Time(config.getSimulationEphoc());
		case HLAfloat64Time:
			return new Float64Time(config.getSimulationEphoc());
		default:
			logger.error("Invalid Time type");
			return null;
		}
	}

}

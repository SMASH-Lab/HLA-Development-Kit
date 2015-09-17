package dkf.time;

import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import dkf.utility.dataType.TimeType;


public class TimeFactory implements TimeFactoryInterface {

	private static final Logger logger = LogManager.getLogger(TimeFactory.class);
	
	private TimeType timestampType = null;
	private DateTime simulationEphoc = null;

	public TimeFactory(TimeType timestampType, DateTime simulationEphoc) {
		this.timestampType = timestampType;
		this.simulationEphoc  = simulationEphoc;
	}

	@Override
	public TimeInterface createTimeInstance() throws FederateNotExecutionMember, NotConnected, RTIinternalError {
		
		switch (timestampType) {
		case HLAinteger64Time:
			return new Integer64Time(simulationEphoc);
		case HLAfloat64Time:
			return new Float64Time(simulationEphoc);
		default:
			logger.error("Invalid Time type");
			return null;
		}
	}

}

package dkf.time;

import hla.rti1516e.LogicalTime;
import hla.rti1516e.LogicalTimeInterval;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.IllegalTimeArithmetic;
import hla.rti1516e.exceptions.InvalidLogicalTimeInterval;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.time.HLAinteger64Interval;
import hla.rti1516e.time.HLAinteger64Time;
import hla.rti1516e.time.HLAinteger64TimeFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import dkf.core.DKFRTIAmbassador;
import dkf.utility.JulianDateType;

@SuppressWarnings("rawtypes")
public class Integer64Time extends TimeAbstract {
	
	private final long LOOKAHEAD_INTEGER64TIME_STEP = 1000000;
	
	private static final Logger logger = LogManager.getLogger(Integer64Time.class);
	
	public Integer64Time(DateTime simulationEphoc) throws FederateNotExecutionMember, NotConnected, RTIinternalError {
		super(simulationEphoc);
		this.time_factory = (HLAinteger64TimeFactory) DKFRTIAmbassador.getInstance().getTimeFactory();
	}

	@Override
	@SuppressWarnings("unchecked")
	public HLAinteger64Time nextTimeStep() throws IllegalTimeArithmetic, InvalidLogicalTimeInterval {
		super.setupNextTimeStep();
		return (HLAinteger64Time) logical_time;
	}

	@Override
	public void setLogicalTime(LogicalTime logicaltime) {
		if(!(logicaltime instanceof HLAinteger64Time)){
			logger.error("LogicalTime is not valid. It must be a HLAinteger64Time");
			throw new IllegalArgumentException("LogicalTime is not valid. It must be a HLAinteger64Time");
			}
		else
			super.setupLogicalTime(logicaltime);
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public HLAinteger64Time getLogicalTime() {
		return (HLAinteger64Time)logical_time;
	}

	@Override
	public void initializeLookaheadInterval() {
		this.lookaheadInterval = ((HLAinteger64TimeFactory)time_factory).makeInterval(LOOKAHEAD_INTEGER64TIME_STEP);
	}

	@Override
	public void setLookaheadInterval(LogicalTimeInterval logicaltimeinterval) {
		if(!(logicaltimeinterval instanceof HLAinteger64Interval)){
			logger.error("LogicalTimeInterval is not valid. It must be a HLAinteger64Interval");
			throw new IllegalArgumentException("LogicalTimeInterval is not valid. It must be a HLAinteger64Interval");
			}
		else
			super.setupLookaheadInterval(logicaltimeinterval);
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public HLAinteger64Interval getLookaheadInterval() {
		return (HLAinteger64Interval)lookaheadInterval;
	}
	
	@Override
	public double getFederationExecutionTimeCycle() {
		return ((HLAinteger64Time)logical_time).getValue();
	}

	@Override
	public DateTime getFederationExecutionTime() {
		return super.calculateFederationExecutionTime(((HLAinteger64Time)logical_time).getValue());
	}

	@Override
	public double getFederationExecutionTimeInJulianDate(JulianDateType juliantype) {
		return super.calculateFederationExecutionTimeInJulianDate(((HLAinteger64Time)logical_time).getValue(), juliantype);
	}

	@Override
	public double getTimeClycle() {
		return this.LOOKAHEAD_INTEGER64TIME_STEP;
	}
	
}

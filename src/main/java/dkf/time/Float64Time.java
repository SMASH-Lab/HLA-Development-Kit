package dkf.time;

import hla.rti1516e.LogicalTime;
import hla.rti1516e.LogicalTimeInterval;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.IllegalTimeArithmetic;
import hla.rti1516e.exceptions.InvalidLogicalTimeInterval;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.time.HLAfloat64Interval;
import hla.rti1516e.time.HLAfloat64Time;
import hla.rti1516e.time.HLAfloat64TimeFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import dkf.core.DKFRTIAmbassador;
import dkf.utility.JulianDateType;

@SuppressWarnings("rawtypes")
public class Float64Time extends TimeAbstract {

	private double LOOKAHEAD_FLOAT64TIME_STEP = 1000000;

	private static final Logger logger = LogManager.getLogger(Float64Time.class);

	public Float64Time(DateTime simulationEphoc) throws FederateNotExecutionMember, NotConnected, RTIinternalError {
		super(simulationEphoc);
		this.time_factory = (HLAfloat64TimeFactory) DKFRTIAmbassador.getInstance().getTimeFactory();
	}

	@Override
	@SuppressWarnings("unchecked")
	public HLAfloat64Time nextTimeStep() throws IllegalTimeArithmetic, InvalidLogicalTimeInterval {
		super.setupNextTimeStep();
		return (HLAfloat64Time) logical_time;
	}

	@Override
	public void setLogicalTime(LogicalTime logicaltime) {
		if(!(logicaltime instanceof HLAfloat64Time)){
			logger.error("LogicalTime is not valid. It must be a HLAfloat64Time");
			throw new IllegalArgumentException("LogicalTime is not valid. It must be a HLAfloat64Time");
		}
		else
			super.setupLogicalTime(logicaltime);

	}

	@Override
	@SuppressWarnings("unchecked")
	public HLAfloat64Time getLogicalTime() {
		return (HLAfloat64Time)logical_time;
	}

	@Override
	public void initializeLookaheadInterval() {
		this.lookaheadInterval = ((HLAfloat64TimeFactory)time_factory).makeInterval(LOOKAHEAD_FLOAT64TIME_STEP);
	}

	@Override
	public void setLookaheadInterval(LogicalTimeInterval logicaltimeinterval) {
		if(!(logicaltimeinterval instanceof HLAfloat64Interval)){
			logger.error("LogicalTimeInterval is not valid. It must be a HLAfloat64Interval");
			throw new IllegalArgumentException("LogicalTimeInterval is not valid. It must be a HLAfloat64Interval");
		}
		else
			super.setupLookaheadInterval(logicaltimeinterval);

	}

	@Override
	@SuppressWarnings("unchecked")
	public HLAfloat64Interval getLookaheadInterval() {
		return (HLAfloat64Interval)lookaheadInterval;
	}

	@Override
	public double getFederationExecutionTimeCycle() {
		return ((HLAfloat64Time)logical_time).getValue();
	}

	@Override
	public DateTime getFederationExecutionTime() {
		return super.calculateFederationExecutionTime(((HLAfloat64Time)logical_time).getValue());
	}

	@Override
	public double getFederationExecutionTimeInJulianDate(JulianDateType juliantype) {
		return super.calculateFederationExecutionTimeInJulianDate(((HLAfloat64Time)logical_time).getValue(), juliantype);
	}

	@Override
	public double getTimeClycle() {
		return LOOKAHEAD_FLOAT64TIME_STEP;
	}

}

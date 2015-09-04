package dkf.time;


import jodd.datetime.JDateTime;
import hla.rti1516e.LogicalTime;
import hla.rti1516e.LogicalTimeFactory;
import hla.rti1516e.LogicalTimeInterval;
import hla.rti1516e.exceptions.IllegalTimeArithmetic;
import hla.rti1516e.exceptions.InvalidLogicalTimeInterval;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import dkf.utility.JulianDateType;
import dkf.utility.TimeUnit;
import dkf.utility.TimeUtility;

@SuppressWarnings("rawtypes")
public abstract class TimeAbstract implements TimeInterface {
	
	private static final Logger logger = LogManager.getLogger(TimeAbstract.class);
	
	protected DateTime simulationEphoc = null;
	
	protected LogicalTimeFactory time_factory = null;
	protected LogicalTime federateLogicalTime = null;
	protected LogicalTime logical_time = null;
	protected LogicalTimeInterval lookaheadInterval = null;
	
	protected JDateTime julianDate = null;
	protected double executionCounter;

	
	protected TimeAbstract(DateTime simulationEphoc) {
		this.simulationEphoc = new DateTime(simulationEphoc);
	}
	
	@Override
	public void setFederateTime(LogicalTime time) {
		this.federateLogicalTime = time;
		
	}
	
	@SuppressWarnings("unchecked")
	public void setupNextTimeStep() throws IllegalTimeArithmetic, InvalidLogicalTimeInterval {
		this.logical_time = this.logical_time.add(lookaheadInterval);
	}

	public void setupLogicalTime(LogicalTime logicaltime) {
		this.logical_time = logicaltime;
	}

	public void setupLookaheadInterval(LogicalTimeInterval logicaltimeinterval) {
		this.lookaheadInterval = logicaltimeinterval;
	}
	
	@Override
	public double getFederateExecutionTimeCycle() {
		return executionCounter;
	}
	
	@Override
	public void setFederateExecutionTimeCycle(double executiontime) {
		this.executionCounter = executiontime;
		
	}

	protected DateTime calculateFederationExecutionTime(double value) {
		return simulationEphoc.plusMillis((int) Math.round((TimeUtility.convert(value, TimeUnit.MICROSECONDS, TimeUnit.MILLISECONDS))));
		
	}

	protected double calculateFederationExecutionTimeInJulianDate(double value, JulianDateType juliantype) {
		if(julianDate == null)
			julianDate = new JDateTime();

		DateTime currTime = simulationEphoc.plusMillis((int) Math.round((TimeUtility.convert(value, TimeUnit.MICROSECONDS, TimeUnit.MILLISECONDS))));
		julianDate.set(currTime.getYear(), currTime.getMonthOfYear(), currTime.getDayOfMonth(), currTime.getHourOfDay(), currTime.getMinuteOfHour(), currTime.getSecondOfMinute(), currTime.getMillisOfSecond());

		switch (juliantype) {
			case DATE:
				return julianDate.getJulianDateDouble();
			case MODIFIED:
				return julianDate.getJulianDate().getModifiedJulianDate().doubleValue();
			case REDUCED:
				return julianDate.getJulianDate().getReducedJulianDate().doubleValue();
			case TRUNCATED:
				return julianDate.getJulianDate().getTruncatedJulianDate().doubleValue();
			default:
				logger.error("Illegal JulianDateType: "+juliantype);
				throw new IllegalArgumentException("Illegal JulianDateType: "+juliantype);
		}
	}
	
	
	public void initializeLogicalTime() {
		this.logical_time = time_factory.makeInitial();
	}
	
}

package dkf.time;

import org.joda.time.DateTime;

import dkf.utility.JulianDateType;
import hla.rti1516e.LogicalTime;
import hla.rti1516e.LogicalTimeInterval;
import hla.rti1516e.exceptions.IllegalTimeArithmetic;
import hla.rti1516e.exceptions.InvalidLogicalTimeInterval;

@SuppressWarnings("rawtypes")
public interface TimeInterface {
	
	public void setFederateTime(LogicalTime time);
	
	public <T extends LogicalTime> T nextTimeStep() throws IllegalTimeArithmetic, InvalidLogicalTimeInterval;
	
	//logicaltime
	public void initializeLogicalTime();
	public void setLogicalTime(LogicalTime logicaltime);
	public <T extends LogicalTime> T getLogicalTime();

	//lookaheadinterval
	public void initializeLookaheadInterval();
	public void setLookaheadInterval(LogicalTimeInterval logicaltimeinterval);
	public <T extends LogicalTimeInterval> T getLookaheadInterval();
	
	public double getFederationExecutionTimeCycle();
	public DateTime getFederationExecutionTime();
	public double getFederationExecutionTimeInJulianDate(JulianDateType juliantype);
	
	public double getFederateExecutionTimeCycle();
	public void setFederateExecutionTimeCycle(double executiontime);

	public double getTimeClycle();

}

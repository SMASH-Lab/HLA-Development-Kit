package dkf.time;

import java.io.IOException;

import org.jdom2.JDOMException;

import dkf.exception.TimeRepresentationException;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;

public interface TimeFactoryInterface {
	
	/**
	 * Creates a Time instance starting from the FOM
	 * @return Time
	 * @throws RTIinternalError 
	 * @throws NotConnected 
	 * @throws FederateNotExecutionMember 
	 * @throws TimeRepresentationException 
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public TimeInterface createTimeInstance() throws FederateNotExecutionMember, NotConnected, RTIinternalError, JDOMException, IOException, TimeRepresentationException;

}

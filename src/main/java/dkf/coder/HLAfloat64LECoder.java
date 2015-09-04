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
package dkf.coder;

import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfloat64LE;
import hla.rti1516e.exceptions.RTIinternalError;

public class HLAfloat64LECoder implements Coder<Double>{

	private HLAfloat64LE coder = null;
	private EncoderFactory factory = null;

	public HLAfloat64LECoder() throws RTIinternalError {
		this.factory = RtiFactoryFactory.getRtiFactory().getEncoderFactory();
		this.coder = factory.createHLAfloat64LE();
	}
	
	public Double decode(byte[] code) throws DecoderException {
		coder.decode(code);
		return coder.getValue();
	}

	public byte[] encode(Double element) {
		coder.setValue(element);
		return coder.toByteArray();
	}
	
	public Class<Double> getAllowedType() {
		return Double.class;
	}
}
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
package dkf.utility;

public class TimeUtility {
	
	private static final TimeUnit[] units = new TimeUnit[] {
		
		TimeUnit.MINUTES, 
		TimeUnit.SECONDS,
		TimeUnit.MILLISECONDS,
		TimeUnit.MICROSECONDS,
		TimeUnit.NANOSECONDS,
		
	};
	
	private static final float[] conversionFactors = new float[] {
		60,
		1000,
		1000,
		1000,
		1000,
	};
	
	
	public static double convert(double time, TimeUnit from, TimeUnit to) {
		
		if(time < 0)
			throw new IllegalArgumentException("Time must be greater than zero.");
		
		int start_index = getTimeUnitIndex(from);
		int stop_index = getTimeUnitIndex(to);
		double returnValue = time;
		
		if(start_index < stop_index){
			for(int i = start_index+1; i <= stop_index; i++)
				returnValue = returnValue * conversionFactors[i];
		}
		else if(start_index > stop_index){
			for(int i = start_index-1; i >= stop_index; i--){
				returnValue = returnValue / conversionFactors[i];
			}
		}
		return returnValue;
		
	}


	private static int getTimeUnitIndex(TimeUnit from) {
		
		for(int i=0; i < units.length; i++)
			if(units[i] == from)
				return i;
		return -1;
	}
	
}

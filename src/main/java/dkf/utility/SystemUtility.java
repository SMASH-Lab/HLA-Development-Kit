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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 * @author SMASH-Lab University of Calabria
 * @version 0.1
 * 
 */
public class SystemUtility {

	/**
	 * Checks the state of the 'CurrentProfile' profile in Windows Firewall
	 * @return true if it is set to 'ON'
	 */
	public static boolean windowsFirewallIsEnabled(){

		Process process = null;
		String line = null;
		BufferedReader reader = null;
		Boolean status = false;

		try {
			process = Runtime.getRuntime().exec("netsh advfirewall show currentprofile state");
			process.waitFor();
			reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			while ((line = reader.readLine()) != null) 
				if(line.contains("ON"))
					status = true;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally{
			try {
				process.destroy();
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return status;
	}

}

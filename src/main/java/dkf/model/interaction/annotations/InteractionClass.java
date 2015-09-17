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
package dkf.model.interaction.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dkf.model.Order;
import dkf.model.Sharing;
import dkf.model.Transportation;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InteractionClass {
	
	/**
	 * The name of the InteractionClass defined in the FOM file
	 * @return The name of the InteractionClass
	 */
	public String name();
	
	/**
	 * The transportation type of the InteractionClass defined in the FOM file
	 * @return The transportation type of the InteractionClass
	 */
	public Transportation transportation() default Transportation.Unknown;
	
	/**
	 * The Sharing type of the InteractionClass defined in the FOM file
	 * @return The type of the Sharing
	 */
	public Sharing sharing() default Sharing.Unknown;
	
	/**
	 * The Order type of the InteractionClass defined in the FOM file
	 * @return The type of the Order
	 */
	public Order order() default Order.Unknown;
	
	/**
	 * The semantic of the InteractionClass defined in the FOM file
	 * @return The semantic of the InteractionClass
	 */
	public String semantic() default "";

}

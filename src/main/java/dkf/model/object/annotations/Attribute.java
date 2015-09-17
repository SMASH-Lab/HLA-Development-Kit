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
package dkf.model.object.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dkf.coder.Coder;
import dkf.model.Order;
import dkf.model.Sharing;
import dkf.model.Transportation;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Attribute {
	
	/**
	 * the name of the attribute defined in the FOM file
	 * @return the filed's name
	 */
	public String name();
	
	/**
	 * the coder of the attribute defined in the FOM file
	 * @return the filed's coder
	 */
	@SuppressWarnings("rawtypes")
	public Class<? extends Coder> coder();
	
	/**
	 * The Sharing type of the attribute defined in the FOM file
	 * @return The type of the Sharing
	 */
	public Sharing sharing() default Sharing.Unknown;
	
	/**
	 * The Ownership type of the attribute defined in the FOM file
	 * @return The type of ownership
	 */
	public Ownership Ownership() default Ownership.Unknown;
	
	/**
	 * The UpdateType of the attribute defined in the FOM file
	 * @return The type of Update
	 */
	public UpdateType updateType() default UpdateType.Unknown;
	
	/**
	 * The update condition of the attribute
	 * @return The update condition
	 */
	public String updateCondition() default "";
	
	/**
	 * The Order type of the attribute defined in the FOM file
	 * @return The type of the Sharing
	 */
	public Order order() default Order.Unknown;
	
	/**
	 * The transportation type of the attribute defined in the FOM file
	 * @return The transportation type of the attribute
	 */
	public Transportation transportation() default Transportation.Unknown;
	
	/**
	 * The semantic of the parameter defined in the FOM file
	 * @return The semantic of the attribute
	 */
	public String semantic() default "";
}

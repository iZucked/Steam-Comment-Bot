/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.migration;


/**
 * A single migration unit to convert between two versions of a scenario version.
 * 
 * @author Simon Goodall
 * 
 */
public interface IClientMigrationUnit extends IMigrationUnit {

	/**
	 * The client migration context this unit applies to.
	 * 
	 * @return
	 */
	String getClientContext();

	/**
	 * Version number to upgrade the client model from
	 * 
	 * @return
	 */
	int getClientSourceVersion();

	/**
	 * Version number the client model will be upgraded to
	 * 
	 * @return
	 */
	int getClientDestinationVersion();
}

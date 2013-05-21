/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.migration;

/**
 * A single migration unit to convert between two versions of a scenario version.
 * 
 * @author Simon Goodall
 * @since 3.0
 * 
 */
public interface IMigrationUnitExtension extends IMigrationUnit {

	void setMigrationUnit(IMigrationUnit unit);

	IMigrationUnit getMigrationUnit();
}

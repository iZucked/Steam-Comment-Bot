package com.mmxlabs.models.migration;

import java.util.List;

public interface IMigrationChain {

	/**
	 * An ordered list of migration units
	 * @return
	 */
	List<IMigrationUnit> getMigrationUnits();
}

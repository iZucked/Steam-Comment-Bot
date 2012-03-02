/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.importer.importers;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * A warning listener which collects unique warnings
 * 
 * @author hinton
 * 
 */
public class WarningCollector implements IImportWarningListener {
	/**
	 * The collected warnings
	 */
	private final HashSet<ImportWarning> warnings = new HashSet<ImportWarning>();

	@Override
	public void importWarning(final ImportWarning iw) {
		warnings.add(iw);
	}

	public Collection<ImportWarning> getWarnings() {
		return Collections.unmodifiableSet(warnings);
	}
}

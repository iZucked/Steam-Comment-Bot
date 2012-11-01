/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.transformer;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.lng.analytics.UnitCostMatrix;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Transformer for setting up analytics problem instances.
 * 
 * @author hinton
 *
 */
public interface IAnalyticsTransformer {
	/**
	 * Single method which evaluates a cost matrix. This shouldn't be a very expensive operation,
	 * so it doesn't create a job.
	 * 
	 * 
	 * @param root
	 * @param spec
	 * @return
	 */
	public List<UnitCostLine> createCostLines(final MMXRootObject root, final UnitCostMatrix spec, final IProgressMonitor monitor);

	UnitCostLine createCostLine(MMXRootObject root, UnitCostMatrix spec, Port from, Port to);
}

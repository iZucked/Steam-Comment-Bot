/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.econs;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lingo.reports.views.standard.econs.StandardEconsRowFactory.EconsOptions;

public interface IEconsRowFactory {

	Collection<CargoEconsReportRow> createRows(@NonNull EconsOptions options);
}

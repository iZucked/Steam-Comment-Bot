/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.pnlcalcs;

import com.mmxlabs.rcp.common.application.IInjectableE4ComponentFactory;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class PNLCalcsReportComponentFactory implements IInjectableE4ComponentFactory {

	@Override
	public Class<?> getComponentClass() {
		return PNLCalcsReportComponent.class;
	}

}

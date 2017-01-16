/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import com.mmxlabs.rcp.common.application.IInjectableE4ComponentFactory;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class PNLDetailsReportComponentFactory implements IInjectableE4ComponentFactory {

	@Override
	public Class<?> getComponentClass() {
		return PNLDetailsReportComponent.class;
	}

}

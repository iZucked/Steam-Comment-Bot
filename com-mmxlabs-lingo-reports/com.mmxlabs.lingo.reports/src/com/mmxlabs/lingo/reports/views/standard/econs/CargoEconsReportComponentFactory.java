/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.econs;

import com.mmxlabs.rcp.common.application.IInjectableE4ComponentFactory;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class CargoEconsReportComponentFactory implements IInjectableE4ComponentFactory {

	@Override
	public Class<?> getComponentClass() {
		return CargoEconsReportComponent.class;
	}

}

/**
 * Copyrightom (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers.impl;

import java.util.HashMap;

import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.scheduler.optimiser.providers.IDiscountCurveProviderEditor;

/**
 * @author Tom Hinton
 *
 */
public class HashMapDiscountCurveEditor implements IDiscountCurveProviderEditor {
	private final HashMap<String, ICurve> map = new HashMap<String, ICurve>();
	private final ICurve unit = new ConstantValueCurve(1);
	final String name;
	
	
	public HashMapDiscountCurveEditor(String name) {
		super();
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.providers.IDiscountCurveProvider#getDiscountCurve(java.lang.String)
	 */
	@Override
	public ICurve getDiscountCurve(String componentName) {
		final ICurve c = map.get(componentName);
		return c == null ? unit : c;
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.optimiser.core.scenario.IDataComponentProvider#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.optimiser.core.scenario.IDataComponentProvider#dispose()
	 */
	@Override
	public void dispose() {
		map.clear();

	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.providers.IDiscountCurveProviderEditor#setDiscountCurve(java.lang.String, com.mmxlabs.common.curves.ICurve)
	 */
	@Override
	public void setDiscountCurve(String componentName, ICurve discountCurve) {
		map.put(componentName, discountCurve == null ? unit : discountCurve);
	}

}

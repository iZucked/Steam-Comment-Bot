/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.optimiser.components;

import com.mmxlabs.common.detailtree.DetailTree;
import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.common.detailtree.impl.CurrencyDetailElement;
import com.mmxlabs.scheduler.optimiser.Calculator;

/**
 * An {@link IDetailTree} special case for recording a transfer of LNG between entities.
 * 
 * Adds leaf node for volume, price/m3, /mmbtu, total mmbtu and cv value.
 * 
 * @author hinton
 * 
 */
public class LNGTransferDetailTree extends DetailTree {
	public LNGTransferDetailTree(final String key, final long lngVolume, final int lngPricePerM3, final int cvValue) {
		super(key, new CurrencyDetailElement(Calculator.multiply(lngVolume, lngPricePerM3)));
		addChild("Volume", lngVolume);
		addChild("Price/m3", new CurrencyDetailElement(lngPricePerM3));
		addChild("CV Value", cvValue);
		addChild("MMBTu", Calculator.multiply(cvValue, lngVolume));
		addChild("Price/MMBTu", new CurrencyDetailElement(Calculator.divide(lngPricePerM3, cvValue)));
	}
}

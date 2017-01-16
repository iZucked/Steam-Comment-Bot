/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.annotations.impl;

import com.mmxlabs.common.detailtree.DetailTree;
import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.common.detailtree.impl.UnitPriceDetailElement;
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
		// FIXME: We divide by 1000 as TradingExporterExtension does not know which values are in normal scale (currently x1000) or high scale (currentl x1000000)
		super(key, new UnitPriceDetailElement(Calculator.convertM3ToM3Price(lngVolume, lngPricePerM3) / 1000L));
		addChild("Volume", lngVolume);
		addChild("Price/m3", new UnitPriceDetailElement(lngPricePerM3 / 1000L));
		addChild("CV Value", cvValue / 1000L);
		addChild("MMBTu", Calculator.convertM3ToMMBTu(lngVolume, cvValue));
		addChild("Price/MMBTu", new UnitPriceDetailElement(Calculator.costPerMMBTuFromM3(lngPricePerM3, cvValue) / 1000L));
	}
}

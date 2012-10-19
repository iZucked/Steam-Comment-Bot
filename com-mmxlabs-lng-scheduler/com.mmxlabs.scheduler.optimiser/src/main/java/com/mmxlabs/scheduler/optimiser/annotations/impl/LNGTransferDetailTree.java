package com.mmxlabs.scheduler.optimiser.annotations.impl;

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
 * @since 2.0
 * 
 */
public class LNGTransferDetailTree extends DetailTree {
	public LNGTransferDetailTree(final String key, final long lngVolume, final int lngPricePerM3, final int cvValue) {
		super(key, new CurrencyDetailElement(Calculator.convertM3ToM3Price(lngVolume, lngPricePerM3)));
		addChild("Volume", lngVolume);
		addChild("Price/m3", new CurrencyDetailElement(lngPricePerM3));
		addChild("CV Value", cvValue);
		addChild("MMBTu", Calculator.convertM3ToMMBTu(lngVolume, cvValue));
		addChild("Price/MMBTu", new CurrencyDetailElement(Calculator.costPerMMBTuFromM3(lngPricePerM3, cvValue)));
	}
}

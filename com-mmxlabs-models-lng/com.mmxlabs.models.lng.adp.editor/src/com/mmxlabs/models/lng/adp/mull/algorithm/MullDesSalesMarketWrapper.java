package com.mmxlabs.models.lng.adp.mull.algorithm;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;

@NonNullByDefault
public class MullDesSalesMarketWrapper implements IMullDischargeWrapper {

	private final DESSalesMarket desSalesMarket;

	public MullDesSalesMarketWrapper(final DESSalesMarket desSalesMarket) {
		this.desSalesMarket = desSalesMarket;
	}

	public DESSalesMarket getDesSalesMarket() {
		return desSalesMarket;
	}
	
	@Override
	public boolean equals(@Nullable Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof final MullDesSalesMarketWrapper mullDesSalesMarketWrapper) {
			return this.desSalesMarket == mullDesSalesMarketWrapper.getDesSalesMarket();
		}
		return false;
	};
	
	public int hashCode() {
		return this.desSalesMarket.hashCode();
	};
}

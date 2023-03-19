/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;
import com.mmxlabs.models.lng.analytics.ui.views.evaluators.IMapperClass;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadOption;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.ChangeablePriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

@NonNullByDefault
public class SwapValueMatrixElementProvider {

	private final ISequenceElement baseLoadSequenceElement;

	private final ISequenceElement baseDischargeSequenceElement;
	private final ChangeablePriceCalculator baseDischargePriceCalculator;

	private final LoadSlot backfillDesPurchaseMarketSlot;
	private final ISequenceElement backfillDesPurchaseMarketSequenceElement;
	private final ChangeablePriceCalculator backfillDesPurchaseMarketPriceCalculator;

	private final DischargeSlot diversionDesSaleMarketSlot;
	private final ISequenceElement diversionDesSaleMarketSequenceElement;
	private final ChangeablePriceCalculator diversionDesSaleMarketPriceCalculator;

	private SwapValueMatrixElementProvider(final ISequenceElement baseLoadSequenceElement, final ISequenceElement baseDischargeSequenceElement,
			final ChangeablePriceCalculator baseDischargePriceCalculator, final LoadSlot backfillDesPurchaseMarketSlot, final ISequenceElement backfillDesPurchaseMarketSequenceElement,
			final ChangeablePriceCalculator backfillDesPurchaseMarketPriceCalculator, final DischargeSlot diversionDesSaleMarketSlot, final ISequenceElement diversionDesSaleMarketSequenceElement,
			final ChangeablePriceCalculator diversionDesSaleMarketPriceCalculator) {
		this.baseLoadSequenceElement = baseLoadSequenceElement;

		this.baseDischargeSequenceElement = baseDischargeSequenceElement;
		this.baseDischargePriceCalculator = baseDischargePriceCalculator;

		this.backfillDesPurchaseMarketSlot = backfillDesPurchaseMarketSlot;
		this.backfillDesPurchaseMarketSequenceElement = backfillDesPurchaseMarketSequenceElement;
		this.backfillDesPurchaseMarketPriceCalculator = backfillDesPurchaseMarketPriceCalculator;

		this.diversionDesSaleMarketSlot = diversionDesSaleMarketSlot;
		this.diversionDesSaleMarketSequenceElement = diversionDesSaleMarketSequenceElement;
		this.diversionDesSaleMarketPriceCalculator = diversionDesSaleMarketPriceCalculator;
	}

	public ISequenceElement getBaseLoadSequenceElement() {
		return baseLoadSequenceElement;
	}

	public ISequenceElement getBaseDischargeSequenceElement() {
		return baseDischargeSequenceElement;
	}

	public ChangeablePriceCalculator getBaseDischargePriceCalculator() {
		return baseDischargePriceCalculator;
	}

	public LoadSlot getBackfillDesPurchaseMarketSlot() {
		return backfillDesPurchaseMarketSlot;
	}

	public ISequenceElement getBackfillDesPurchaseMarketSequenceElement() {
		return backfillDesPurchaseMarketSequenceElement;
	}

	public ChangeablePriceCalculator getBackfillDesPurchaseMarketPriceCalculator() {
		return backfillDesPurchaseMarketPriceCalculator;
	}

	public DischargeSlot getDiversionDesSaleMarketSlot() {
		return diversionDesSaleMarketSlot;
	}

	public ISequenceElement getDiversionDesSaleMarketSequenceElement() {
		return diversionDesSaleMarketSequenceElement;
	}

	public ChangeablePriceCalculator getDiversionDesSaleMarketPriceCalculator() {
		return diversionDesSaleMarketPriceCalculator;
	}

	public static SwapValueMatrixElementProvider buildElementProvider(final ModelEntityMap modelEntityMap, final IPortSlotProvider portSlotProvider, final Pair<LoadSlot, DischargeSlot> baseCargo,
			final SwapValueMatrixModel model, final IMapperClass mapper) {
		final ISequenceElement baseLoadSequenceElement;
		{
			final IPortSlot loadOption = modelEntityMap.getOptimiserObjectNullChecked(baseCargo.getFirst(), IPortSlot.class);
			baseLoadSequenceElement = portSlotProvider.getElement(loadOption);
		}

		final ChangeablePriceCalculator baseDischargePriceCalculator;
		final ISequenceElement baseDischargeSequenceElement;
		{
			final IPortSlot dischargeOption = modelEntityMap.getOptimiserObjectNullChecked(baseCargo.getSecond(), IPortSlot.class);
			if (dischargeOption instanceof final com.mmxlabs.scheduler.optimiser.components.impl.DischargeSlot oBaseDischargeSlot) {
				baseDischargePriceCalculator = new ChangeablePriceCalculator();
				oBaseDischargeSlot.setDischargePriceCalculator(baseDischargePriceCalculator);
				baseDischargeSequenceElement = portSlotProvider.getElement(dischargeOption);
			} else {
				throw new IllegalStateException("Value matrix runner could not set discharge price");
			}
		}
		final LoadSlot backfillLoadSlot;
		final ChangeablePriceCalculator backfillLoadPriceCalculator;
		final ISequenceElement backfillLoadSequenceElement;
		{
			backfillLoadSlot = mapper.getPurchaseMarketOriginal(model.getParameters().getSwapLoadMarket().getMarket(), model.getParameters().getSwapLoadMarket().getMonth());
			final IPortSlot loadPortSlot = modelEntityMap.getOptimiserObject(backfillLoadSlot, IPortSlot.class);
			if (loadPortSlot instanceof LoadOption oBackfillLoadSlot) {
				final ILoadPriceCalculator loadPriceCalculator = oBackfillLoadSlot.getLoadPriceCalculator();
				if (loadPriceCalculator instanceof ChangeablePriceCalculator changeableLoadPriceCalculator) {
					if (model.getParameters().getSwapFee() != 0.0) {
						final int internalSwapFee = OptimiserUnitConvertor.convertToInternalPrice(model.getParameters().getSwapFee());
						final ChangeablePriceCalculator newLoadPriceCalculator = new ChangeablePriceCalculator(internalSwapFee);
						oBackfillLoadSlot.setLoadPriceCalculator(newLoadPriceCalculator);
						backfillLoadPriceCalculator = newLoadPriceCalculator;
					} else {
						backfillLoadPriceCalculator = changeableLoadPriceCalculator;
					}
					backfillLoadSequenceElement = portSlotProvider.getElement(loadPortSlot);
				} else {
					throw new IllegalStateException("Value matrix runner could not set market load price");
				}
			} else {
				throw new IllegalStateException("Value matrix runner could not set market load price");
			}
		}
		final DischargeSlot diversionDesMarketDischargeSlot;
		final ISequenceElement diversionDesSaleMarketSequenceElement;
		final ChangeablePriceCalculator diversionDesSaleMarketPriceCalculator;
		{
			diversionDesMarketDischargeSlot = mapper.getSalesMarketOriginal(model.getParameters().getSwapDischargeMarket().getMarket(), model.getParameters().getSwapDischargeMarket().getMonth());
			final IPortSlot dischargePortSlot = modelEntityMap.getOptimiserObject(diversionDesMarketDischargeSlot, IPortSlot.class);
			if (dischargePortSlot instanceof com.mmxlabs.scheduler.optimiser.components.impl.DischargeOption oSwapMarketDischargeSlot) {
				final ISalesPriceCalculator salesPriceCalculator = oSwapMarketDischargeSlot.getDischargePriceCalculator();
				if (salesPriceCalculator instanceof ChangeablePriceCalculator changeableDischargePriceCalculator) {
					diversionDesSaleMarketPriceCalculator = changeableDischargePriceCalculator;
					diversionDesSaleMarketSequenceElement = portSlotProvider.getElement(dischargePortSlot);
				} else {
					throw new IllegalStateException("Swap sandbox could not set market discharge price");
				}
			} else {
				throw new IllegalStateException("Swap sandbox could not set market discharge price");

			}
		}
		return new SwapValueMatrixElementProvider(baseLoadSequenceElement, baseDischargeSequenceElement, baseDischargePriceCalculator, backfillLoadSlot, backfillLoadSequenceElement,
				backfillLoadPriceCalculator, diversionDesMarketDischargeSlot, diversionDesSaleMarketSequenceElement, diversionDesSaleMarketPriceCalculator);
	}
}

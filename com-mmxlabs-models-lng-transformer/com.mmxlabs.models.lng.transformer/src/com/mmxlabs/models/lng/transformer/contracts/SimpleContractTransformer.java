/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.contracts;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.FixedPriceContract;
import com.mmxlabs.models.lng.commercial.IndexPriceContract;
import com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.AXPlusBCurve;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator2;
import com.mmxlabs.scheduler.optimiser.contracts.IShippingPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.SimpleContract;

/**
 * 
 * 
 * @author hinton
 * 
 */
public class SimpleContractTransformer implements IContractTransformer {
	private ISchedulerBuilder builder;
	private SimpleContractBuilder contractBuilder;
	private ModelEntityMap map;
	private MMXRootObject rootObject;
	private final Collection<EClass> handledClasses = Arrays.asList(CommercialPackage.eINSTANCE.getFixedPriceContract(), CommercialPackage.eINSTANCE.getIndexPriceContract(),
			CommercialPackage.eINSTANCE.getProfitSharePurchaseContract(), CommercialPackage.eINSTANCE.getNetbackPurchaseContract());

	@Override
	public void startTransforming(final MMXRootObject rootObject, final ModelEntityMap map, final ISchedulerBuilder builder) {
		this.rootObject = rootObject;
		this.map = map;
		this.builder = builder;
		this.contractBuilder = new SimpleContractBuilder();
		builder.addBuilderExtension(contractBuilder);
	}

	@Override
	public void finishTransforming() {
		this.rootObject = null;
		this.map = null;
		this.builder = null;
		this.contractBuilder = null;
	}

	private ILoadPriceCalculator2 instantiatePC(final Contract c) {
		if (c instanceof ProfitSharePurchaseContract) {
			final ProfitSharePurchaseContract contract = (ProfitSharePurchaseContract) c;
			final Set<IPort> baseMarketPorts = new HashSet<IPort>();
			for (APortSet portSet : contract.getBaseMarketPorts()) {
				final Set<APort> ports = SetUtils.getPorts(portSet);
				for (APort p : ports) {
					baseMarketPorts.add(map.getOptimiserObject(p, IPort.class));
				}
			}
			ICurve baseMarket = new AXPlusBCurve(Calculator.scaleToInt(contract.getBaseMarketMultiplier()), Calculator.scaleToInt(contract.getBaseMarketConstant()), map.getOptimiserObject(
					contract.getBaseMarketIndex(), ICurve.class));
			ICurve referenceMarket = new AXPlusBCurve(Calculator.scaleToInt(contract.getRefMarketMultiplier()), Calculator.scaleToInt(contract.getRefMarketConstant()), map.getOptimiserObject(
					contract.getRefMarketIndex(), ICurve.class));

			return contractBuilder.createProfitSharingContract(baseMarket, referenceMarket, Calculator.scaleToInt(contract.getMargin()), Calculator.scaleToInt(contract.getShare()), baseMarketPorts);
			// } else if (c instanceof IndexPriceContract) {
			// return contractBuilder.createNetbackContract(buyersMargin)map.getOptimiserObject(((IndexPriceContract) c).getIndex(), ICurve.class),
			// Calculator.scaleToInt(((IndexPriceContract) c).getConstant()), Calculator.scaleToInt(((IndexPriceContract) c).getMultiplier()));
		}
		return instantiate(c);
	}

	private SimpleContract instantiate(final Contract c) {
		if (c instanceof FixedPriceContract) {
			return contractBuilder.createFixedPriceContract(Calculator.scaleToInt(((FixedPriceContract) c).getPricePerMMBTU()));
		} else if (c instanceof IndexPriceContract) {
			return contractBuilder.createMarketPriceContract(map.getOptimiserObject(((IndexPriceContract) c).getIndex(), ICurve.class), Calculator.scaleToInt(((IndexPriceContract) c).getConstant()),
					Calculator.scaleToInt(((IndexPriceContract) c).getMultiplier()));
		}
		return null;
	}

	@Override
	public IShippingPriceCalculator transformSalesContract(final SalesContract sc) {
		return instantiate(sc);
	}

	@Override
	public ILoadPriceCalculator2 transformPurchaseContract(final PurchaseContract pc) {
		return instantiatePC(pc);
	}

	@Override
	public void slotTransformed(final Slot modelSlot, final IPortSlot optimiserSlot) {

	}

	/**
	 * @return
	 */
	public Collection<EClass> getContractEClasses() {
		return handledClasses;
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.trading.integration;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.NetbackPurchaseContract;
import com.mmxlabs.models.lng.commercial.NotionalBallastParameters;
import com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.IContractTransformer;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVesselClass;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.AXPlusBCurve;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator2;
import com.mmxlabs.scheduler.optimiser.contracts.IShippingPriceCalculator;

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
	private final Collection<EClass> handledClasses = Arrays.asList(CommercialPackage.eINSTANCE.getProfitSharePurchaseContract(), CommercialPackage.eINSTANCE.getNetbackPurchaseContract());

	@Override
	public void startTransforming(final MMXRootObject rootObject, final ModelEntityMap map, final ISchedulerBuilder builder) {
		this.rootObject = rootObject;
		this.map = map;
		this.builder = builder;
		this.contractBuilder = new SimpleContractBuilder(map);
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
			for (final APortSet portSet : contract.getBaseMarketPorts()) {
				final Set<APort> ports = SetUtils.getPorts(portSet);
				for (final APort p : ports) {
					baseMarketPorts.add(map.getOptimiserObject(p, IPort.class));
				}
			}
			final ICurve baseMarket = new AXPlusBCurve(Calculator.scaleToInt(contract.getBaseMarketMultiplier()), Calculator.scaleToInt(contract.getBaseMarketConstant()), map.getOptimiserObject(
					contract.getBaseMarketIndex(), ICurve.class));
			final ICurve referenceMarket = new AXPlusBCurve(Calculator.scaleToInt(contract.getRefMarketMultiplier()), Calculator.scaleToInt(contract.getRefMarketConstant()), map.getOptimiserObject(
					contract.getRefMarketIndex(), ICurve.class));

			return contractBuilder.createProfitSharingContract(baseMarket, referenceMarket, Calculator.scaleToInt(contract.getMargin()), Calculator.scaleToInt(contract.getShare()), baseMarketPorts);
		} else if (c instanceof NetbackPurchaseContract) {
			final NetbackPurchaseContract netbackPurchaseContract = (NetbackPurchaseContract) c;
			final Map<AVesselClass, NotionalBallastParameters> notionalBallastParameterMap = new HashMap<AVesselClass, NotionalBallastParameters>();

			for (final NotionalBallastParameters p : netbackPurchaseContract.getNotionalBallastParameters()) {

				for (final AVesselClass vc : p.getVesselClasses()) {

					notionalBallastParameterMap.put(vc, p);
				}
			}

			return contractBuilder.createNetbackContract(Calculator.scaleToInt(netbackPurchaseContract.getMargin()), notionalBallastParameterMap);
		}
		return null;
	}

	@Override
	public IShippingPriceCalculator transformSalesContract(final SalesContract sc) {
		return null;
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

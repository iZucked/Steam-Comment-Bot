/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.trading.integration.extensions;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;

import com.google.inject.Inject;
import com.google.inject.Injector;
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
import com.mmxlabs.models.lng.transformer.ResourcelessModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.IContractTransformer;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVesselClass;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.AXPlusBCurve;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;

/**
 * * Contract Transformer and Builder - this is the {@link IContractTransformer} portion of the extension. See {@link StandardContractBuilderExtension} for the builder / internal model side. This
 * class deals directly with the EMF model and uses the {@link StandardContractBuilderExtension} to create internal instances.
 * 
 * @author hinton
 * 
 * @since 2.0
 */
public class StandardContractTransformerExtension implements IContractTransformer {

	private StandardContractBuilderExtension contractBuilder;

	private ModelEntityMap map;

	private final Collection<EClass> handledClasses = Arrays.asList(CommercialPackage.eINSTANCE.getProfitSharePurchaseContract(), CommercialPackage.eINSTANCE.getNetbackPurchaseContract());

	@Inject
	private Injector injector;

	@Override
	public void startTransforming(final MMXRootObject rootObject, final ResourcelessModelEntityMap map, final ISchedulerBuilder builder) {
		this.map = map;
		contractBuilder = new StandardContractBuilderExtension();
		injector.injectMembers(contractBuilder);
		builder.addBuilderExtension(contractBuilder);
	}

	@Override
	public void finishTransforming() {
		this.map = null;
		this.contractBuilder = null;
	}

	private ILoadPriceCalculator instantiatePC(final Contract c) {
		if (c instanceof ProfitSharePurchaseContract) {
			final ProfitSharePurchaseContract contract = (ProfitSharePurchaseContract) c;
			final Set<IPort> baseMarketPorts = new HashSet<IPort>();
			for (final APortSet portSet : contract.getBaseMarketPorts()) {
				final Set<APort> ports = SetUtils.getPorts(portSet);
				for (final APort p : ports) {
					baseMarketPorts.add(map.getOptimiserObject(p, IPort.class));
				}
			}
			final ICurve baseMarket = new AXPlusBCurve(OptimiserUnitConvertor.convertToInternalConversionFactor(contract.getBaseMarketMultiplier()),
					OptimiserUnitConvertor.convertToInternalPrice(contract.getBaseMarketConstant()), map.getOptimiserObject(contract.getBaseMarketIndex(), ICurve.class));
			final ICurve referenceMarket = new AXPlusBCurve(OptimiserUnitConvertor.convertToInternalConversionFactor(contract.getRefMarketMultiplier()),
					OptimiserUnitConvertor.convertToInternalPrice(contract.getRefMarketConstant()), map.getOptimiserObject(contract.getRefMarketIndex(), ICurve.class));

			final int salesPriceMultiplier = OptimiserUnitConvertor.convertToInternalConversionFactor(contract.getSalesMultiplier());

			return contractBuilder.createProfitSharingContract(baseMarket, referenceMarket, OptimiserUnitConvertor.convertToInternalPrice(contract.getMargin()),
					OptimiserUnitConvertor.convertToInternalConversionFactor(contract.getShare()), baseMarketPorts, salesPriceMultiplier);
		} else if (c instanceof NetbackPurchaseContract) {
			final NetbackPurchaseContract netbackPurchaseContract = (NetbackPurchaseContract) c;
			final Map<AVesselClass, NotionalBallastParameters> notionalBallastParameterMap = new HashMap<AVesselClass, NotionalBallastParameters>();

			for (final NotionalBallastParameters p : netbackPurchaseContract.getNotionalBallastParameters()) {

				for (final AVesselClass vc : p.getVesselClasses()) {

					notionalBallastParameterMap.put(vc, p);
				}
			}

			return contractBuilder.createNetbackContract(OptimiserUnitConvertor.convertToInternalConversionFactor(netbackPurchaseContract.getMargin()),
					OptimiserUnitConvertor.convertToInternalPrice(netbackPurchaseContract.getFloorPrice()), notionalBallastParameterMap);
		}
		return null;
	}

	@Override
	public ISalesPriceCalculator transformSalesContract(final SalesContract sc) {
		return null;
	}

	@Override
	public ILoadPriceCalculator transformPurchaseContract(final PurchaseContract pc) {
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

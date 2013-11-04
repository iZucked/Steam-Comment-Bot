/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.mmxlabs.common.curves.StepwiseIntegerCurve;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.LNGPriceCalculatorParameters;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.IContractTransformer;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.IAlternativeElementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

/**
 * An abstract contract transformer for redirection contracts which can switch between FOB and DES purchase within the optimisation.
 * TODO: 
 * 
 * @author Simon Goodall
 * 
 */
public abstract class RedirectionContractTransformer implements IContractTransformer {

	private ModelEntityMap map;

	@Inject
	private DateAndCurveHelper dateHelper;

	@Inject
	private IAlternativeElementProviderEditor alternativeSequenceElementProviderEditor;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private Injector injector;

	@Inject
	private SeriesParser indices;

	private final Set<IPortSlot> generatedOptions = new HashSet<IPortSlot>();

	private ISchedulerBuilder builder;

	private transient List<ITransformerExtension> transformerExtensions = null;

	@Inject
	private RedirectionGroupProvider redirectionGroupProvider;

	private final Class<? extends LNGPriceCalculatorParameters> redirectionPriceParamtersClass;

//	@Inject
//	private IRedirectionContractDetailsProvider redirectionContractDetailsProvider;
	
	protected RedirectionContractTransformer(final Class<? extends LNGPriceCalculatorParameters> redirectionPriceParamtersClass) {
		this.redirectionPriceParamtersClass = redirectionPriceParamtersClass;
	}
	
	@Override
	public void startTransforming(final LNGScenarioModel rootObject, final ModelEntityMap map, final ISchedulerBuilder builder) {
		this.map = map;
		this.builder = builder;
	}

	@Override
	public void finishTransforming() {
		this.map = null;
		this.builder = null;
	}

	@Override
	public ISalesPriceCalculator transformSalesPriceParameters(@Nullable SalesContract salesContract, final LNGPriceCalculatorParameters sc) {
		return null;
	}

	@Override
	public abstract ILoadPriceCalculator transformPurchasePriceParameters(@Nullable PurchaseContract purchaseContract, final LNGPriceCalculatorParameters pc);
//	{
//		if (redirectionPriceParamtersClass.isInstance(pc)) {
//			return createRedirectionContract(redirectionPriceParamtersClass.cast(pc));
//		}
//		return null;
//	}

//	private <T extends LNGPriceCalculatorParameters> RedirectionContract createRedirectionContract(final T  contract) {
//		final IPort baseSalesMarketPort = map.getOptimiserObject(contract.getBaseSalesMarketPort(), IPort.class);
//		final IPort sourcePurchasePort = map.getOptimiserObject(contract.getSourcePurchasePort(), IPort.class);
//
//		final ICurve purchasePriceCurve = generateExpressionCurve(contract.getBasePurchasePriceExpression());
//		final ICurve salesPriceCurve = generateExpressionCurve(contract.getBaseSalesPriceExpression());
//
//		final int notionalSpeed = OptimiserUnitConvertor.convertToInternalSpeed(contract.getNotionalSpeed());
//
//		final IVesselClass vesselClass = map.getOptimiserObject(contract.getVesselClass(), IVesselClass.class);
//		final ICurve hireCurve = new ConstantValueCurve(OptimiserUnitConvertor.convertToInternalHourlyRate(contract.getHireCost()));
//		final RedirectionContract redirectionContract = new RedirectionContract(purchasePriceCurve, salesPriceCurve, notionalSpeed, baseSalesMarketPort, sourcePurchasePort, vesselClass, hireCurve);
//		injector.injectMembers(redirectionContract);
//		return redirectionContract;
//	}

	@Override
	public void slotTransformed(final Slot modelSlot, final IPortSlot optimiserSlot) {
		// Avoid recursion with generated slots
		if (generatedOptions.contains(optimiserSlot)) {
			return;
		}
		if (modelSlot instanceof LoadSlot) {

			final LoadSlot loadSlot = (LoadSlot) modelSlot;

			if (loadSlot.getContract() instanceof PurchaseContract) {
				final PurchaseContract purchaseContract = (PurchaseContract) loadSlot.getContract();

				if (redirectionPriceParamtersClass.isInstance(purchaseContract.getPriceInfo() )) {
//					final RedirectionPriceParameters redirectionPriceParameters = (RedirectionPriceParameters) purchaseContract.getPriceInfo();

//					Date originalDate = redirectionContractDetailsProvider.getOriginalDate(loadSlot);
//					for (final EObject obj : modelSlot.getExtensions()) {
//						if (obj instanceof RedirectionContractOriginalDate) {
//							final RedirectionContractOriginalDate redirectionContractOriginalDate = (RedirectionContractOriginalDate) obj;
//							originalDate = redirectionContractOriginalDate.getDate();
//							break;
//						}
//					}

//					final int originalLoadTime = originalDate == null ? optimiserSlot.getTimeWindow().getStart() : map.getHoursFromDate(originalDate);
					// TODO: Obtain directly or derive from slot data
//					final int shippingHours = 60 * 24; // redirectionContractDetailsProvider.getShippingHours....
					// TODO: Pass into
					// Get from contract
					boolean swappable = true;

					final ISequenceElement elementA = portSlotProvider.getElement(optimiserSlot);

					final Set<Slot> redirectionGroup = new HashSet<>();
					redirectionGroup.add(modelSlot);
					// If DES/FOB Swappable
					if (swappable) {
						// FOB - DES Conversion code.

						final ILoadOption loadOption = (ILoadOption) optimiserSlot;

						final boolean slotIsOptional = modelSlot.isOptional();
						final ILoadPriceCalculator priceCalculator = loadOption.getLoadPriceCalculator();
						final int cargoCVValue = loadOption.getCargoCVValue();
						final long minVolume = loadOption.getMinLoadVolume();
						final long maxVolume = loadOption.getMaxLoadVolume();

						ILoadOption alternativeSlot;
						final ITimeWindow currentWindow = loadOption.getTimeWindow();
						final String id = loadOption.getId() + "-alt";
						if (loadSlot.isDESPurchase()) {
							// Convert to FOB Purchase slot
							final ITimeWindow window = builder.createTimeWindow(originalLoadTime, originalLoadTime + 24);
							alternativeSlot = builder.createLoadSlot(id, loadOption.getPort(), window, minVolume, maxVolume, priceCalculator, cargoCVValue, 24, false, true, IPortSlot.NO_PRICING_DATE, slotIsOptional);
							generatedOptions.add(alternativeSlot);

							// Create a fake model object to add in here;
							final LoadSlot fobPurchaseSlot = CargoFactory.eINSTANCE.createLoadSlot();
							fobPurchaseSlot.setDESPurchase(false);
							fobPurchaseSlot.setName(loadOption.getId());
							fobPurchaseSlot.setArriveCold(true);
							if (loadSlot.isSetCargoCV()) {
								fobPurchaseSlot.setCargoCV(loadSlot.getCargoCV());
							}
							fobPurchaseSlot.setPort(loadSlot.getPort());
							fobPurchaseSlot.setWindowStart(map.getDateFromHours(window.getStart()));
							fobPurchaseSlot.setContract(loadSlot.getContract());
							fobPurchaseSlot.setOptional(loadSlot.isOptional());
							fobPurchaseSlot.setWindowSize((int) 24);
							if (loadSlot.isSetMaxQuantity()) {
								fobPurchaseSlot.setMaxQuantity(loadSlot.getMaxQuantity());
							}
							if (loadSlot.isSetMinQuantity()) {
								fobPurchaseSlot.setMinQuantity(loadSlot.getMinQuantity());
							}
							// Key piece of information
							map.addModelObject(fobPurchaseSlot, alternativeSlot);

							redirectionGroup.add(fobPurchaseSlot);

							// Pass slot to other contract transformers
							transformSlot(fobPurchaseSlot, alternativeSlot);
						} else {
							// Convert to DES Purchase
							final ITimeWindow window = builder.createTimeWindow(currentWindow.getStart(), currentWindow.getEnd() + shippingHours);
							alternativeSlot = builder.createDESPurchaseLoadSlot(id, loadOption.getPort(), window, minVolume, maxVolume, priceCalculator, cargoCVValue, IPortSlot.NO_PRICING_DATE, slotIsOptional);

							generatedOptions.add(alternativeSlot);

							// Create a fake model object to add in here;
							final LoadSlot desSlot = CargoFactory.eINSTANCE.createLoadSlot();
							desSlot.setDESPurchase(true);
							desSlot.setName(loadOption.getId());
							desSlot.setArriveCold(true);
							// Always set CV
							desSlot.setCargoCV(loadSlot.getSlotOrPortCV());
							desSlot.setPort(loadSlot.getPort());
							desSlot.setWindowStart(map.getDateFromHours(window.getStart()));
							desSlot.setContract(loadSlot.getContract());
							desSlot.setOptional(loadSlot.isOptional());
							desSlot.setWindowSize((int) 24);
							if (loadSlot.isSetMaxQuantity()) {
								desSlot.setMaxQuantity(loadSlot.getMaxQuantity());
							}
							if (loadSlot.isSetMinQuantity()) {
								desSlot.setMinQuantity(loadSlot.getMinQuantity());
							}
							// Key piece of information
							map.addModelObject(desSlot, alternativeSlot);

							// Reuse RedirectionDESPurchaseBindingsGenerator
							desPurchaseSlotBindingsGenerator.bindDischargeSlotsToDESPurchase(builder, desSlot, alternativeSlot);

							redirectionGroup.add(desSlot);

							// Pass slot to other contract transformers
							transformSlot(desSlot, alternativeSlot);
						}

						// Bind the current and generated slots together and pass into the redirection group provider. The post export processor and constraint checker need this information.
						redirectionGroupProvider.createRedirectionGroup(redirectionGroup);

						// final ISequenceElement elementA = portSlotProvider.getElement(optimiserSlot);
						final ISequenceElement elementB = portSlotProvider.getElement(alternativeSlot);
						alternativeSequenceElementProviderEditor.setAlternativeElements(elementA, elementB);
					}
				}
			}
		}
	}

	private void transformSlot(final Slot modelSlot, final IPortSlot optimiserSlot) {

		if (transformerExtensions == null) {
			transformerExtensions = injector.getInstance(Key.get(new TypeLiteral<List<ITransformerExtension>>() {
			}));
		}

		for (final ITransformerExtension e : transformerExtensions) {
			if (e == this) {
				continue;
			}
			if (e instanceof IContractTransformer) {
				final IContractTransformer ct = (IContractTransformer) e;
				ct.slotTransformed(modelSlot, optimiserSlot);
			}
		}
	}

	@Override
	public abstract Collection<EClass> getContractEClasses();

	private StepwiseIntegerCurve generateExpressionCurve(final String priceExpression) {

		if (priceExpression == null || priceExpression.isEmpty()) {
			return new StepwiseIntegerCurve();
		}

		final IExpression<ISeries> expression = indices.parse(priceExpression);
		final ISeries parsed = expression.evaluate();

		final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();
		if (parsed.getChangePoints().length == 0) {
			curve.setDefaultValue(OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(0).doubleValue()));
		} else {

			curve.setDefaultValue(0);
			for (final int i : parsed.getChangePoints()) {
				curve.setValueAfter(i - 1, OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(i).doubleValue()));
			}
		}
		return curve;
	}

}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.redirection;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jdt.annotation.NonNull;
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
import com.mmxlabs.models.lng.cargo.SpotSlot;
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
import com.mmxlabs.scheduler.optimiser.builder.impl.TimeWindowMaker;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.IAlternativeElementProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IShippingHoursRestrictionProvider;

/**
 * An abstract contract transformer for redirection contracts which can switch between FOB and DES purchase within the optimisation. TODO:
 * 
 * @author Simon Goodall
 * 
 * 
 *         TODO: Roll this directly in to the transformer!
 */
public abstract class RedirectionContractTransformer implements IContractTransformer {

	private ModelEntityMap modelEntityMap;

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

	@Inject
	private IShippingHoursRestrictionProvider shippingHoursRestrictionProvider;

	protected RedirectionContractTransformer(final Class<? extends LNGPriceCalculatorParameters> redirectionPriceParamtersClass) {
		this.redirectionPriceParamtersClass = redirectionPriceParamtersClass;
	}

	@Override
	public void startTransforming(final LNGScenarioModel rootObject, final ModelEntityMap modelEntityMap, final ISchedulerBuilder builder) {
		this.modelEntityMap = modelEntityMap;
		this.builder = builder;
	}

	@Override
	public void finishTransforming() {
		this.modelEntityMap = null;
		this.builder = null;
	}

	@Override
	public ISalesPriceCalculator transformSalesPriceParameters(@Nullable SalesContract salesContract, @NonNull final LNGPriceCalculatorParameters sc) {
		return null;
	}

	@Override
	public abstract ILoadPriceCalculator transformPurchasePriceParameters(@Nullable PurchaseContract purchaseContract, @NonNull final LNGPriceCalculatorParameters pc);

	@Override
	public void slotTransformed(@NonNull final Slot modelSlot, @NonNull final IPortSlot optimiserSlot) {
		// Avoid recursion with generated slots
		if (generatedOptions.contains(optimiserSlot)) {
			return;
		}
		if (modelSlot instanceof LoadSlot) {

			final LoadSlot loadSlot = (LoadSlot) modelSlot;

			if (loadSlot.getContract() instanceof PurchaseContract) {
				final PurchaseContract purchaseContract = (PurchaseContract) loadSlot.getContract();

				if (redirectionPriceParamtersClass.isInstance(purchaseContract.getPriceInfo())) {
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
						final boolean slotIsLocked = modelSlot.isLocked();
						final boolean isSpotSlot = (modelSlot instanceof SpotSlot);
						final ILoadPriceCalculator priceCalculator = loadOption.getLoadPriceCalculator();
						final int cargoCVValue = loadOption.getCargoCVValue();
						final long minVolume = loadOption.getMinLoadVolume();
						final long maxVolume = loadOption.getMaxLoadVolume();

						ILoadOption alternativeSlot;
						final ITimeWindow currentWindow = loadOption.getTimeWindow();
						assert currentWindow != null;

						final String id = loadOption.getId() + "-alt";
						if (loadSlot.isDESPurchase()) {
							final ITimeWindow baseTimeWindow = shippingHoursRestrictionProvider.getBaseTime(elementA);

							// Convert to FOB Purchase slot
							alternativeSlot = builder.createLoadSlot(id, loadOption.getPort(), baseTimeWindow, minVolume, maxVolume, priceCalculator, cargoCVValue, loadSlot.getSlotOrPortDuration(),
									false, true, IPortSlot.NO_PRICING_DATE, loadOption.getPricingEvent(), slotIsOptional, slotIsLocked, isSpotSlot, loadOption.isVolumeSetInM3());
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
							fobPurchaseSlot.setWindowStart(modelEntityMap.getDateFromHours(baseTimeWindow.getInclusiveStart(), loadSlot.getPort()).toLocalDate());
							fobPurchaseSlot.setContract(loadSlot.getContract());
							fobPurchaseSlot.setOptional(loadSlot.isOptional());
							fobPurchaseSlot.setWindowSize((int) 23);
							if (loadSlot.isSetMaxQuantity()) {
								fobPurchaseSlot.setMaxQuantity(loadSlot.getMaxQuantity());
							}
							if (loadSlot.isSetMinQuantity()) {
								fobPurchaseSlot.setMinQuantity(loadSlot.getMinQuantity());
							}
							if (loadSlot.isSetPricingEvent()) {
								fobPurchaseSlot.setPricingEvent(loadSlot.getPricingEvent());
							}
							// Key piece of information
							modelEntityMap.addModelObject(fobPurchaseSlot, alternativeSlot);

							redirectionGroup.add(fobPurchaseSlot);

							// Pass slot to other contract transformers
							transformSlot(fobPurchaseSlot, alternativeSlot);
						} else {
							// Convert to DES Purchase
							final int shippingHours = shippingHoursRestrictionProvider.getShippingHoursRestriction(elementA);

							final ITimeWindow window = TimeWindowMaker.createInclusiveExclusive(currentWindow.getInclusiveStart(), currentWindow.getExclusiveEnd() + shippingHours, 0, false);
							alternativeSlot = builder.createDESPurchaseLoadSlot(id, loadOption.getPort(), window, minVolume, maxVolume, priceCalculator, cargoCVValue, loadSlot.getSlotOrPortDuration(),
									IPortSlot.NO_PRICING_DATE, loadOption.getPricingEvent(), slotIsOptional, slotIsLocked, isSpotSlot, loadOption.isVolumeSetInM3());

							generatedOptions.add(alternativeSlot);

							// Create a fake model object to add in here;
							final LoadSlot desSlot = CargoFactory.eINSTANCE.createLoadSlot();
							desSlot.setDESPurchase(true);
							desSlot.setName(loadOption.getId());
							desSlot.setArriveCold(true);
							// Always set CV
							desSlot.setCargoCV(loadSlot.getSlotOrDelegatedCV());
							desSlot.setPort(loadSlot.getPort());
							desSlot.setWindowStart(modelEntityMap.getDateFromHours(window.getInclusiveStart(), loadSlot.getPort()).toLocalDate());
							desSlot.setContract(loadSlot.getContract());
							desSlot.setOptional(loadSlot.isOptional());
							desSlot.setWindowSize((int) 23);
							if (loadSlot.isSetMaxQuantity()) {
								desSlot.setMaxQuantity(loadSlot.getMaxQuantity());
							}
							if (loadSlot.isSetMinQuantity()) {
								desSlot.setMinQuantity(loadSlot.getMinQuantity());
							}
							if (loadSlot.isSetPricingEvent()) {
								desSlot.setPricingEvent(loadSlot.getPricingEvent());
							}
							// Key piece of information
							modelEntityMap.addModelObject(desSlot, alternativeSlot);

							// TODO Reuse RedirectionDESPurchaseBindingsGenerator
							// desPurchaseSlotBindingsGenerator.bindDischargeSlotsToDESPurchase(builder, desSlot, alternativeSlot);

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
				curve.setValueAfter(i, OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(i).doubleValue()));
			}
		}
		return curve;
	}

}

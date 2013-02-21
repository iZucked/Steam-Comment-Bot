/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.transformer.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.model.Category;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.IConstraintDescriptor;
import org.eclipse.emf.validation.service.IConstraintFilter;
import org.eclipse.emf.validation.service.ModelValidationService;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mmxlabs.common.Association;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.StepwiseIntegerCurve;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.CostComponent;
import com.mmxlabs.models.lng.analytics.ProvisionalCargo;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.lng.analytics.Visit;
import com.mmxlabs.models.lng.analytics.Voyage;
import com.mmxlabs.models.lng.analytics.transformer.ICargoSandboxTransformer;
import com.mmxlabs.models.lng.analytics.transformer.internal.TmpDefaultBreakEvenEvaluator;
import com.mmxlabs.models.lng.analytics.transformer.internal.TmpVanillaEntityValueCalculator;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.transformer.ResourcelessModelEntityMap;
import com.mmxlabs.models.lng.transformer.TransformerHelper;
import com.mmxlabs.models.lng.transformer.inject.modules.ScheduleBuilderModule;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.models.lng.types.ExtraData;
import com.mmxlabs.models.lng.types.ExtraDataFormatType;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.DefaultExtraValidationContext;
import com.mmxlabs.models.ui.validation.ValidationHelper;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.impl.AnnotatedSolution;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossAnnotation;
import com.mmxlabs.scheduler.optimiser.annotations.IProfitAndLossEntry;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.BreakEvenLoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.BreakEvenSalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.FixedPriceContract;
import com.mmxlabs.scheduler.optimiser.contracts.impl.PriceExpressionContract;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.UnconstrainedCargoAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.AbstractSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.ScheduleEvaluator;
import com.mmxlabs.scheduler.optimiser.manipulators.SequencesManipulatorModule;
import com.mmxlabs.scheduler.optimiser.providers.IEntityProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.guice.DataComponentProviderModule;
import com.mmxlabs.scheduler.optimiser.providers.impl.HashMapEntityProviderEditor;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @since 3.0
 */
public class CargoSandboxTransformer implements ICargoSandboxTransformer {

	private static final Logger log = LoggerFactory.getLogger(CargoSandboxTransformer.class);

	@Override
	public @Nullable
	UnitCostLine createCostLine(final MMXRootObject root, final ProvisionalCargo provisionalCargo) {
		final IBatchValidator validator = (IBatchValidator) ModelValidationService.getInstance().newValidator(EvaluationMode.BATCH);
		validator.setOption(IBatchValidator.OPTION_INCLUDE_LIVE_CONSTRAINTS, true);

		validator.addConstraintFilter(new IConstraintFilter() {

			@Override
			public boolean accept(final IConstraintDescriptor constraint, final EObject target) {

				for (final Category cat : constraint.getCategories()) {
					if (cat.getId().equals("com.mmxlabs.models.lng.analytics.validation.constraints.cargosandbox")) {
						return true;
					} else if (cat.getId().endsWith(".base")) {
						return true;
					}
				}
				return false;
			}
		});

		final ValidationHelper helper = new ValidationHelper();
		final IStatus validationStatus = helper.runValidation(validator, new DefaultExtraValidationContext(root), Collections.singleton(provisionalCargo));
		if (!validationStatus.isOK()) {
			return null;
		}

		// try {
		final PortModel portModel = root.getSubModel(PortModel.class);
		final PricingModel pricing = root.getSubModel(PricingModel.class);

		// Check for valid plan
		if (provisionalCargo == null || provisionalCargo.getBuy() == null || provisionalCargo.getSell() == null) {
			// monitor.setCanceled(true);
			return null;
		}
		// Check for a valid vessel.
		final Vessel modelVessel = provisionalCargo.getVessel();
		if (modelVessel == null) {
			return null;
		}

		BuyOpportunity buy = provisionalCargo.getBuy();
		SellOpportunity sell = provisionalCargo.getSell();

		final Injector injector = Guice.createInjector(new DataComponentProviderModule(), new ScheduleBuilderModule(), new SequencesManipulatorModule(), createShippingCostModule());
		final ISchedulerBuilder builder = injector.getInstance(ISchedulerBuilder.class);

		final ResourcelessModelEntityMap entities = injector.getInstance(ResourcelessModelEntityMap.class);

		final SeriesParser indices = injector.getInstance(SeriesParser.class);
		final DateAndCurveHelper dateHelper = injector.getInstance(DateAndCurveHelper.class);

		// Earliest date, record!
		builder.setEarliestDate(buy.getDate());
		entities.setEarliestDate(buy.getDate());

		/*
		 * Create ports and distances
		 */
		final Association<Port, IPort> ports = new Association<Port, IPort>();
		final ICooldownPriceCalculator nullCalculator = new FixedPriceContract(0);
		for (final Port port : portModel.getPorts()) {
			final IPort optPort = builder.createPort(port.getName(), !port.isAllowCooldown(), nullCalculator);
			ports.add(port, optPort);
			entities.addModelObject(portModel, optPort);
		}
		for (final Route route : portModel.getRoutes()) {
			for (final RouteLine line : route.getLines()) {
				builder.setPortToPortDistance(ports.lookup(line.getFrom()), ports.lookup(line.getTo()), route.getName(), line.getDistance());
			}
		}

		/**
		 * create all the market curves (should these come through the builder?)
		 */
		final Association<Index<?>, ICurve> indexAssociation = new Association<Index<?>, ICurve>();

		final PricingModel pricingModel = root.getSubModel(PricingModel.class);
		for (final Index<Double> index : pricingModel.getCommodityIndices()) {
			if (index instanceof DataIndex) {
				final DataIndex<Double> di = (DataIndex<Double>) index;
				final SortedSet<Pair<Date, Number>> vals = new TreeSet<Pair<Date, Number>>(new Comparator<Pair<Date, ?>>() {
					@Override
					public int compare(final Pair<Date, ?> o1, final Pair<Date, ?> o2) {
						return o1.getFirst().compareTo(o2.getFirst());
					}
				});
				for (final IndexPoint<Double> pt : di.getPoints()) {
					vals.add(new Pair<Date, Number>(pt.getDate(), pt.getValue()));
				}
				final int[] times = new int[vals.size()];
				final Number[] nums = new Number[vals.size()];
				int k = 0;
				for (final Pair<Date, Number> e : vals) {
					times[k] = dateHelper.convertTime(buy.getDate(), e.getFirst());
					nums[k++] = e.getSecond();
				}
				indices.addSeriesData(index.getName(), times, nums);
			} else if (index instanceof DerivedIndex) {
				indices.addSeriesExpression(index.getName(), ((DerivedIndex) index).getExpression());
			}
		}

		for (final Index<Double> index : pricingModel.getCommodityIndices()) {
			try {
				final ISeries parsed = indices.getSeries(index.getName());
				final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();
				curve.setDefaultValue(0);
				for (final int i : parsed.getChangePoints()) {
					curve.setValueAfter(i - 1, OptimiserUnitConvertor.convertToInternalPrice(parsed.evaluate(i).doubleValue()));
				}
				entities.addModelObject(index, curve);
				indexAssociation.add(index, curve);
			} catch (final Exception exception) {
				log.warn("Error evaluating series " + index.getName(), exception);
			}
		}

		final VesselClass eVc = modelVessel.getVesselClass();

		double baseFuelPrice = 0;
		for (final BaseFuelCost baseFuelCost : pricingModel.getFleetCost().getBaseFuelPrices()) {
			if (baseFuelCost.getFuel() == eVc.getBaseFuel()) {
				baseFuelPrice = baseFuelCost.getPrice();
				break;
			}
		}
		final IVesselClass vesselClass = TransformerHelper.buildIVesselClass(builder, eVc, baseFuelPrice);

		/**
		 * Set up vessel class route parameters
		 */
		for (final Route route : portModel.getRoutes()) {
			VesselClassRouteParameters parametersForRoute = null;
			RouteCost costForRoute = null;
			for (final VesselClassRouteParameters parameters : modelVessel.getVesselClass().getRouteParameters()) {
				if (parameters.getRoute() == route) {
					parametersForRoute = parameters;
					break;
				}
			}
			for (final RouteCost routeCost : pricing.getRouteCosts()) {
				if (routeCost.getRoute() == route && routeCost.getVesselClass() == modelVessel.getVesselClass()) {
					costForRoute = routeCost;
					break;
				}
			}
			for (final RouteLine line : route.getLines()) {
				builder.setPortToPortDistance(ports.lookup(line.getFrom()), ports.lookup(line.getTo()), route.getName(), line.getDistance());
				if (parametersForRoute != null) {
					builder.setVesselClassRouteTransitTime(route.getName(), vesselClass, parametersForRoute.getExtraTransitTime());
					builder.setVesselClassRouteFuel(route.getName(), vesselClass, VesselState.Laden, OptimiserUnitConvertor.convertToInternalHourlyRate(parametersForRoute.getLadenConsumptionRate()),
							OptimiserUnitConvertor.convertToInternalHourlyRate(parametersForRoute.getLadenNBORate()));
					builder.setVesselClassRouteFuel(route.getName(), vesselClass, VesselState.Ballast,
							OptimiserUnitConvertor.convertToInternalHourlyRate(parametersForRoute.getBallastConsumptionRate()),
							OptimiserUnitConvertor.convertToInternalHourlyRate(parametersForRoute.getBallastNBORate()));
				}
				if (costForRoute != null) {
					builder.setVesselClassRouteCost(route.getName(), vesselClass, VesselState.Laden, OptimiserUnitConvertor.convertToInternalFixedCost(costForRoute.getLadenCost()));
					builder.setVesselClassRouteCost(route.getName(), vesselClass, VesselState.Ballast, OptimiserUnitConvertor.convertToInternalFixedCost(costForRoute.getBallastCost()));
				}
			}
		}

		IEntity entity = new IEntity() {

			@Override
			public int getUpstreamTransferPrice(int loadPricePerM3, int cvValue) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public long getTaxedProfit(long downstreamTotalPretaxProfit, int time) {
				// TODO Auto-generated method stub
				return downstreamTotalPretaxProfit;
			}

			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getDownstreamTransferPrice(int dischargePricePerM3, int cvValue) {
				// TODO Auto-generated method stub
				return 0;
			}
		};

		HashMapEntityProviderEditor entityProvider = (HashMapEntityProviderEditor) injector.getInstance(IEntityProvider.class);

		entityProvider.setShippingEntity(entity);

		// / Build up list of elements to schedule
		final List<IPortSlot> elements = new LinkedList<IPortSlot>();
		IStartEndRequirement startConstraint = null;
		IStartEndRequirement endConstraint = null;
		int startHeelPrice = 0;
		int startHeelCV = 0;
		int startHeelVolume = 0;

		IPort buyPort = ports.lookup(buy.getPort());
		IPort sellPort = ports.lookup(sell.getPort());
		// Start Event
		final int buyTime = entities.getHoursFromDate(buy.getDate());
		final int sellTime = entities.getHoursFromDate(sell.getDate());
		{
			final ITimeWindow timeWindow = builder.createTimeWindow(buyTime, buyTime);
			startConstraint = builder.createStartEndRequirement(buyPort, timeWindow);
		}

		// Buy Opportunity
		{
			final String id = "buy";
			final ITimeWindow timeWindow = builder.createTimeWindow(buyTime, buyTime + 24);

			final int cargoCVValue = OptimiserUnitConvertor.convertToInternalConversionFactor(buy.getPort().getCvValue());
			final int gasVolume = Integer.MAX_VALUE;// OptimiserUnitConvertor.convertToInternalConversionFactor(row.getHeelVolume());

			final ILoadPriceCalculator priceCalculator;

			final String priceExpression = buy.getPriceExpression();
			if (IBreakEvenEvaluator.MARKER.equals(priceExpression)) {
				priceCalculator = new BreakEvenLoadPriceCalculator();
			} else {
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
				priceCalculator = new PriceExpressionContract(curve);
			}

			IPortSlot slot = builder.createLoadSlot(id, buyPort, timeWindow, 0, gasVolume, priceCalculator, cargoCVValue, buy.getPort().getLoadDuration(), false, true, false);
			entityProvider.setEntityForSlot(entity, slot);

			elements.add(slot);

		}
		// Sell Opportunity
		{
			final String id = "sell";
			final ITimeWindow timeWindow = builder.createTimeWindow(sellTime, sellTime + 24);

			final int gasVolume = Integer.MAX_VALUE;
			;// OptimiserUnitConvertor.convertToInternalConversionFactor(row.getHeelVolume());

			final ISalesPriceCalculator priceCalculator;
			final String priceExpression = sell.getPriceExpression();
			if (IBreakEvenEvaluator.MARKER.equals(priceExpression)) {
				priceCalculator = new BreakEvenSalesPriceCalculator();
			} else {
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
				priceCalculator = new PriceExpressionContract(curve);
			}

			final long minCv = 0;
			final long maxCv = Long.MAX_VALUE;
			IPortSlot slot = builder.createDischargeSlot(id, sellPort, timeWindow, 0, gasVolume, minCv, maxCv, priceCalculator, sell.getPort().getDischargeDuration(), false);
			elements.add(slot);
			entityProvider.setEntityForSlot(entity, slot);
		}

		{
			endConstraint = builder.createStartEndRequirement(buyPort, builder.createTimeWindow(2 * sellTime, 2 * sellTime + 24));

		}

		int notionalDayRate = 0;
		// Create the vessel now we have the start/end requirements
		final IVessel vessel = builder.createVessel(modelVessel.getName(), vesselClass, new ConstantValueCurve(notionalDayRate / 24), startConstraint, endConstraint, startHeelVolume, startHeelCV,
				startHeelPrice);
		/*
		 * Create a sequences assigning each cargo to the right vessel
		 */
		final IOptimisationData data = builder.getOptimisationData();
		final IVesselProvider vesselProvider = data.getDataComponentProvider(SchedulerConstants.DCP_vesselProvider, IVesselProvider.class);
		final IModifiableSequences sequences = new ModifiableSequences(data.getResources());
		// final Iterator<IVessel> vesselIterator = vessels.iterator();
		final IStartEndRequirementProvider startEndProvider = data.getDataComponentProvider(SchedulerConstants.DCP_startEndRequirementProvider, IStartEndRequirementProvider.class);
		final IPortSlotProvider slotProvider = data.getDataComponentProvider(SchedulerConstants.DCP_portSlotsProvider, IPortSlotProvider.class);

		final int[][] arrivalTimes = new int[1][elements.size() + 2];
		int index = 0;
		final IResource resource = vesselProvider.getResource(vessel);
		{
			final IModifiableSequence sequence = sequences.getModifiableSequence(resource);
			// set up sequence and arrival times
			sequence.add(startEndProvider.getStartElement(resource));

			arrivalTimes[0][index++] = startEndProvider.getStartRequirement(resource).getTimeWindow().getStart();
			for (final IPortSlot slot : elements) {
				sequence.add(slotProvider.getElement(slot));
				arrivalTimes[0][index++] = slot.getTimeWindow().getStart();
			}
			sequence.add(startEndProvider.getEndElement(resource));

			IStartEndRequirement endRequirement = startEndProvider.getEndRequirement(resource);
			arrivalTimes[0][index++] = endRequirement.getTimeWindow().getStart();
		}
		// }
		/*
		 * Create a fitness core and evaluate+annotate the sequences
		 */
		final ISequencesManipulator manipulator = injector.getInstance(ISequencesManipulator.class);
		manipulator.init(data);
		manipulator.manipulate(sequences); // this will set the return elements to the right places, and remove the start elements.

		// final DirectRandomSequenceScheduler scheduler = new DirectRandomSequenceScheduler();
		final AbstractSequenceScheduler scheduler = new AbstractSequenceScheduler() {

			@Override
			public ScheduledSequences schedule(final ISequences sequences, final boolean forExport) {
				return null;
			}

			@Override
			public ScheduledSequences schedule(final ISequences sequences, final Collection<IResource> affectedResources, final boolean forExport) {
				return null;
			}

			@Override
			public void acceptLastSchedule() {

			}
		};
		injector.injectMembers(scheduler);
		// scheduler.setScheduleEvaluator(injector.getInstance(ScheduleEvaluator.class));
		//
		// ScheduleEvaluator evaluator = injector.getInstance(ScheduleEvaluator.class);
		// evaluator.setCargoAllocator(injector.getInstance(ICargoAllocator.class));
		// evaluator.setFitnessComponents(Collections.<ICargoSchedulerFitnessComponent>emptySet(), Collections.<ICargoAllocationFitnessComponent>singleton(new
		// TmpProfitAndLossAllocationComponent("")));
		// evaluator.setLoadPriceCalculators(buy)
		// // injector.injectMembers(scheduler);
		// SchedulerUtils.setDataComponentProviders(data, scheduler);
		// scheduler.setVoyagePlanOptimiser(optimiser);
		// scheduler.init();

		// run the scheduler on the sequences
		final ScheduledSequences result = scheduler.schedule(sequences, arrivalTimes);

		if (result == null) {
			// Error scheduling
			return null;

		}
		TmpDefaultBreakEvenEvaluator beCalc = new TmpDefaultBreakEvenEvaluator();
		injector.injectMembers(beCalc);
		beCalc.processSchedule(result);

		final UnconstrainedCargoAllocator aca = injector.getInstance(UnconstrainedCargoAllocator.class);
		aca.setVesselProvider(vesselProvider);

		final Collection<IAllocationAnnotation> allocations = aca.allocate(result);
		final Iterator<IAllocationAnnotation> allocationIterator = allocations.iterator();

		/*
		 * Unpack the annotated solution and create output lines
		 */

		IEntityValueCalculator entityValueCalculator = new TmpVanillaEntityValueCalculator();
		injector.injectMembers(entityValueCalculator);

		AnnotatedSolution solution = new AnnotatedSolution();
		solution.setSequences(sequences);
		// AnnotatedSolutionExporter exporter = new AnnotatedSolutionExporter();
		// exporter.exportAnnotatedSolution(root, entities, solution);

		for (final ScheduledSequence sequence : result) {
			final IAllocationAnnotation allocation = allocationIterator.next();
			final VoyagePlan plan = sequence.getVoyagePlans().get(1);
			// create line for plan
			long evaluate = entityValueCalculator.evaluate(plan, allocation, vessel, 0, solution);

			// VisitEventExporter visitExporter = new VisitEventExporter();

			ISequenceElement buyElement = slotProvider.getElement(elements.get(0));
			// visitExporter.export(buyElement, solution.getElementAnnotations());
			final IProfitAndLossAnnotation generatedCharterOutProfitAndLoss = solution.getElementAnnotations().getAnnotation(buyElement, "element-profit-and-loss", IProfitAndLossAnnotation.class);

			long totalPNL = 0;
			for (IProfitAndLossEntry entry : generatedCharterOutProfitAndLoss.getEntries()) {
				totalPNL += entry.getFinalGroupValue();
			}

			final UnitCostLine line = AnalyticsFactory.eINSTANCE.createUnitCostLine();

			line.setFrom(ports.reverseLookup(((PortDetails) plan.getSequence()[0]).getOptions().getPortSlot().getPort()));
			line.setTo(ports.reverseLookup(((PortDetails) plan.getSequence()[2]).getOptions().getPortSlot().getPort()));

			final Pair<Port, Port> key = new Pair<Port, Port>(line.getFrom(), line.getTo());

			final Map<FuelComponent, Integer[]> voyageSums = new EnumMap<FuelComponent, Integer[]>(FuelComponent.class);

			// unpack costs from plan
			createPortCostComponent(line.addExtraData("loading", "1 Loading"), ports, pricing, (PortDetails) plan.getSequence()[0], notionalDayRate, modelVessel.getVesselClass());
			createVoyageCostComponent(line.addExtraData("laden", "2 Laden Journey"), (VoyageDetails) plan.getSequence()[1], notionalDayRate);
			sumVoyageCostComponent((VoyageDetails) plan.getSequence()[1], voyageSums);
			createPortCostComponent(line.addExtraData("discharging", "3 Discharging"), ports, pricing, (PortDetails) plan.getSequence()[2], notionalDayRate, modelVessel.getVesselClass());
			createVoyageCostComponent(line.addExtraData("ballast", "4 Ballast Journey"), (VoyageDetails) plan.getSequence()[3], notionalDayRate);
			sumVoyageCostComponent((VoyageDetails) plan.getSequence()[3], voyageSums);

			int totalDuration = 0;
			int totalFuelCost = 0;
			int totalRouteCost = 0;
			int totalPortCost = 0;

			for (final ExtraData d : line.getExtraData()) {
				final ExtraData duration = d.getDataWithKey("duration");
				if (duration != null) {
					totalDuration += duration.getValueAs(Integer.class);
				}
				final ExtraData fuelCost = d.getDataWithKey("fuelcost");
				if (fuelCost != null) {
					totalFuelCost += fuelCost.getValueAs(Integer.class);
				}
				final ExtraData routeCost = d.getDataWithKey("routecost");
				if (routeCost != null) {
					totalRouteCost += routeCost.getValueAs(Integer.class);
				}
				final ExtraData portcost = d.getDataWithKey("portcost");
				if (portcost != null) {
					totalPortCost += portcost.getValueAs(Integer.class);
				}
			}

			for (final CostComponent cc : line.getCostComponents()) {
				totalDuration += cc.getDuration();
				totalFuelCost += cc.getFuelCost();
				if (cc instanceof Voyage)
					totalRouteCost += ((Voyage) cc).getRouteCost();
				if (cc instanceof Visit)
					totalPortCost += ((Visit) cc).getPortCost();
			}

			line.setDuration(totalDuration);
			line.setFuelCost(totalFuelCost);
			line.setCanalCost(totalRouteCost);
			line.setHireCost((notionalDayRate * totalDuration) / 24);
			line.setPortCost(totalPortCost);

			line.setProfit((int) (totalPNL / 1000l));

			line.setVolumeLoaded(OptimiserUnitConvertor.convertToExternalVolume(allocation.getDischargeVolume() + allocation.getFuelVolume()));
			line.setVolumeDischarged(OptimiserUnitConvertor.convertToExternalVolume(allocation.getDischargeVolume()));

			final double cv = line.getFrom().getCvValue();

			line.setMmbtuDelivered((int) (line.getVolumeDischarged() * cv));
			line.setUnitCost(line.getTotalCost() / (double) line.getMmbtuDelivered());

			{
				final ExtraData summary = line.addExtraData("summary", "Summary", line.getTotalCost() / (double) line.getMmbtuDelivered(), ExtraDataFormatType.STRING_FORMAT);
				summary.setFormat("$%,f");
				summary.addExtraData("duration", "Duration", totalDuration, ExtraDataFormatType.DURATION);
				final ExtraData fuelCostData = summary.addExtraData("fuelcost", "Fuel Cost", totalFuelCost, ExtraDataFormatType.CURRENCY);

				{
					for (final FuelComponent component : FuelComponent.values()) {

						if (voyageSums.containsKey(component)) {
							final Integer[] sums = voyageSums.get(component);
							// totalFuelCost += componentCost;
							final ExtraData componentData = fuelCostData.addExtraData(component.name(), component.name(), sums[1], ExtraDataFormatType.CURRENCY);
							componentData.addExtraData("quantity", "Usage (" + component.getDefaultFuelUnit().name() + ")", sums[0], ExtraDataFormatType.INTEGER);
						}
					}
				}

				summary.addExtraData("routecost", "Route Cost", totalRouteCost, ExtraDataFormatType.CURRENCY);
				summary.addExtraData("portcost", "Port Cost", totalPortCost, ExtraDataFormatType.CURRENCY);
				summary.addExtraData("profit", "Profit", line.getProfit(), ExtraDataFormatType.CURRENCY);
				summary.addExtraData("ssalesprice", "Sales Price", allocation.getDischargeM3Price() / cv / 1000000.0, ExtraDataFormatType.STRING_FORMAT).setFormat("$%,.02f");
				summary.addExtraData("purchaseprice", "Purchase Price", allocation.getLoadM3Price() / cv / 1000000.0, ExtraDataFormatType.STRING_FORMAT).setFormat("$%,.02f");
				// summary.addExtraData("hirecost", "Hire Cost", (spec.getNotionalDayRate() * totalDuration) / 24, ExtraDataFormatType.CURRENCY);

				final ExtraData dischargeData = summary.addExtraData("discharged", "MMBTU Discharged", (int) (line.getVolumeDischarged() * cv), ExtraDataFormatType.INTEGER);
				dischargeData.addExtraData("m3", "Volume", line.getVolumeDischarged(), ExtraDataFormatType.INTEGER);

				final ExtraData loadData = summary.addExtraData("loaded", "MMBTU Loaded", (int) (line.getVolumeLoaded() * cv), ExtraDataFormatType.INTEGER);
				loadData.addExtraData("m3", "Volume", line.getVolumeLoaded(), ExtraDataFormatType.INTEGER);

				summary.addExtraData("unitcost", "Unit Cost", line.getTotalCost() / (double) line.getMmbtuDelivered(), ExtraDataFormatType.STRING_FORMAT).setFormat("$%,f");
			}

			return line;
		}
		return null;
	}

	private Module createShippingCostModule() {
		return new AbstractModule() {

			@Override
			protected void configure() {
				bind(IVoyagePlanOptimiser.class).to(VoyagePlanOptimiser.class);
				bind(ILNGVoyageCalculator.class).to(LNGVoyageCalculator.class);
				bind(ScheduleEvaluator.class);
				bind(ICargoAllocator.class).to(UnconstrainedCargoAllocator.class);
				bind(IEntityValueCalculator.class).to(TmpVanillaEntityValueCalculator.class);

			}
		};
	}

	private void createVoyageCostComponent(final ExtraData d, final VoyageDetails voyageDetails, int notionalDayRate) {
		final int duration = voyageDetails.getIdleTime() + voyageDetails.getTravelTime();
		int totalCost = 0;
		d.addExtraData("distance", "Distance", voyageDetails.getOptions().getDistance(), ExtraDataFormatType.INTEGER);
		d.addExtraData("duration", "Duration", duration, ExtraDataFormatType.DURATION);
		d.addExtraData("idletime", "Idle Time", voyageDetails.getIdleTime(), ExtraDataFormatType.DURATION);
		d.addExtraData("traveltime", "Travel Time", voyageDetails.getTravelTime(), ExtraDataFormatType.DURATION);
		d.addExtraData("speed", "Speed", OptimiserUnitConvertor.convertToExternalSpeed(voyageDetails.getSpeed()), ExtraDataFormatType.STRING_FORMAT).setFormat("%,f");
		final int routeCost = OptimiserUnitConvertor.convertToExternalFixedCost(voyageDetails.getRouteCost());
		totalCost += routeCost;
		d.addExtraData("routecost", "Route Cost", routeCost, ExtraDataFormatType.CURRENCY).addExtraData("route", "Route", voyageDetails.getOptions().getRoute(), ExtraDataFormatType.AUTO);

		final ExtraData fuelData = d.addExtraData("fuelcost", "Fuel Cost");
		int totalFuelCost = 0;

		for (final FuelComponent component : FuelComponent.values()) {
			final long consumption = voyageDetails.getFuelConsumption(component, component.getDefaultFuelUnit());
			final int unitPrice = voyageDetails.getFuelUnitPrice(component);

			if (consumption == 0) {
				continue;
			}
			final int componentCost = OptimiserUnitConvertor.convertToExternalFixedCost(Calculator.costFromConsumption(consumption, unitPrice));
			totalFuelCost += componentCost;
			final ExtraData componentData = fuelData.addExtraData(component.name(), component.name(), componentCost, ExtraDataFormatType.CURRENCY);
			componentData.addExtraData("quantity", "Usage (" + component.getDefaultFuelUnit().name() + ")", OptimiserUnitConvertor.convertToExternalVolume(consumption), ExtraDataFormatType.INTEGER);
			componentData.addExtraData("unitprice", "Cost/" + component.getDefaultFuelUnit().name(), OptimiserUnitConvertor.convertToExternalPrice(unitPrice), ExtraDataFormatType.STRING_FORMAT)
					.setFormat("$%,f");

		}
		totalCost += totalFuelCost;
		fuelData.setValue(totalFuelCost);
		fuelData.setFormatType(ExtraDataFormatType.CURRENCY);

		final int hireCost = (notionalDayRate * duration) / 24;
		totalCost += hireCost;
		d.setValue(totalCost);
		d.setFormatType(ExtraDataFormatType.CURRENCY);
		d.addExtraData("hirecost", "Hire Cost", hireCost, ExtraDataFormatType.CURRENCY);
	}

	private void sumVoyageCostComponent(final VoyageDetails voyageDetails, final Map<FuelComponent, Integer[]> sums) {

		for (final FuelComponent component : FuelComponent.values()) {
			final long consumption = voyageDetails.getFuelConsumption(component, component.getDefaultFuelUnit());
			final int unitPrice = voyageDetails.getFuelUnitPrice(component);

			if (consumption == 0) {
				continue;
			}
			Integer[] data;
			if (sums.containsKey(component)) {
				data = sums.get(component);
			} else {
				data = new Integer[2];
				data[0] = new Integer(0);
				data[1] = new Integer(0);
				sums.put(component, data);
			}
			data[0] = data[0].intValue() + OptimiserUnitConvertor.convertToExternalVolume(consumption);
			final int componentCost = OptimiserUnitConvertor.convertToExternalFixedCost(Calculator.costFromConsumption(consumption, unitPrice));
			data[1] = data[1].intValue() + componentCost;
		}
	}

	private void createPortCostComponent(final ExtraData result, final Association<Port, IPort> ports, final PricingModel pricing, final PortDetails portDetails, int notionalDayRate,
			VesselClass vesselClass) {
		result.addExtraData("duration", "Duration", portDetails.getOptions().getVisitDuration(), ExtraDataFormatType.DURATION);
		final Port port = ports.reverseLookup(portDetails.getOptions().getPortSlot().getPort());
		result.addExtraData("location", "Location", port.getName(), ExtraDataFormatType.AUTO);
		int total = 0;
		for (final PortCost cost : pricing.getPortCosts()) {
			if (SetUtils.getPorts(cost.getPorts()).contains(port)) {
				// this is the cost for the given port
				total += cost.getPortCost(vesselClass, portDetails.getOptions().getPortSlot() instanceof ILoadSlot ? PortCapability.LOAD : PortCapability.DISCHARGE);
				result.addExtraData("portcost", "Port Cost", total, ExtraDataFormatType.CURRENCY);

				break;
			}
		}

		total += (notionalDayRate * portDetails.getOptions().getVisitDuration()) / 24;
		result.addExtraData("hirecost", "Hire Cost", (notionalDayRate * portDetails.getOptions().getVisitDuration()) / 24, ExtraDataFormatType.CURRENCY);

		result.setValue(total);
		result.setFormatType(ExtraDataFormatType.CURRENCY);
	}

}

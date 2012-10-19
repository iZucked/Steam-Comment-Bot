/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.management.timer.Timer;

import org.eclipse.emf.ecore.EClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.common.Association;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.curves.ConstantValueCurve;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.curves.StepwiseIntegerCurve;
import com.mmxlabs.common.parser.IExpression;
import com.mmxlabs.common.parser.series.ISeries;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotLoadSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.DryDockEvent;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.fleet.HeelOptions;
import com.mmxlabs.models.lng.fleet.MaintenanceEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselClassRouteParameters;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.fleet.VesselStateAttributes;
import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.optimiser.OptimisationRange;
import com.mmxlabs.models.lng.optimiser.OptimiserModel;
import com.mmxlabs.models.lng.optimiser.OptimiserSettings;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.pricing.BaseFuelCost;
import com.mmxlabs.models.lng.pricing.CharterCostModel;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.DESPurchaseMarket;
import com.mmxlabs.models.lng.pricing.DESSalesMarket;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.DerivedIndex;
import com.mmxlabs.models.lng.pricing.FOBPurchasesMarket;
import com.mmxlabs.models.lng.pricing.FOBSalesMarket;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.pricing.SpotAvailability;
import com.mmxlabs.models.lng.pricing.SpotMarket;
import com.mmxlabs.models.lng.pricing.SpotMarketGroup;
import com.mmxlabs.models.lng.pricing.SpotType;
import com.mmxlabs.models.lng.transformer.contracts.IContractTransformer;
import com.mmxlabs.models.lng.transformer.inject.extensions.ContractTransformer;
import com.mmxlabs.models.lng.transformer.inject.extensions.ContractTransformer.ModelClass;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.ASpotMarket;
import com.mmxlabs.models.lng.types.AVessel;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.ICargo;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IStartEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.IVesselEventPortSlot;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.components.impl.InterpolatingConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.components.impl.LookupTableConsumptionRateCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ICooldownPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.BreakEvenLoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.BreakEvenSalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.MarketPriceContract;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IBreakEvenEvaluator;

/**
 * Wrapper for an EMF LNG Scheduling {@link MMXRootObject}, providing utility methods to convert it into an optimisation job. Typical usage is to construct an LNGScenarioTransformer with a given
 * Scenario, and then call the {@link createOptimisationData} method. It is only expected that an instance will be used once. I.e. a single call to {@link #createOptimisationData(ModelEntityMap)}
 * 
 * @author hinton
 * 
 */
public class LNGScenarioTransformer {
	private static final Logger log = LoggerFactory.getLogger(LNGScenarioTransformer.class);

	private MMXRootObject rootObject;
	private TimeZone timezone;
	private Date earliestTime;
	private Date latestTime;

	private final SeriesParser indices = new SeriesParser();

	@Inject(optional = true)
	private Iterable<ContractTransformer> transformerExtensions;

	@Inject
	private ISchedulerBuilder builder;

	@Inject
	private Injector injector;

	/**
	 * Contains the contract transformers for each known contract type, by the EClass of the contract they transform.
	 */
	final Map<EClass, IContractTransformer> contractTransformersByEClass = new LinkedHashMap<EClass, IContractTransformer>();

	/**
	 * A set of all contract transformers being used; these should be mapped to in {@link #contractTransformersByEClass}
	 */
	final Set<IContractTransformer> contractTransformers = new LinkedHashSet<IContractTransformer>();

	/**
	 * A set of all transformer extensions being used (should contain {@link #contractTransformers})
	 */
	final Set<ITransformerExtension> allTransformerExtensions = new LinkedHashSet<ITransformerExtension>();

	private final Map<VesselClass, List<IVessel>> spotVesselsByClass = new HashMap<VesselClass, List<IVessel>>();

	private final ArrayList<IVessel> allVessels = new ArrayList<IVessel>();

	private OptimiserSettings defaultSettings = null;

	/**
	 * The {@link Set} of ID strings already used. The UI should restrict user entered data items from clashing, but generated ID's may well clash with user ones.
	 */
	private final Set<String> usedIDStrings = new HashSet<String>();

	/**
	 * A {@link Map} of existing spot market slots by ID. This map is used later when building the spot market options.
	 */
	private final Map<String, Slot> marketSlotsByID = new HashMap<String, Slot>();

	private final EnumMap<SpotType, TreeMap<String, Collection<Slot>>> existingSpotCount = new EnumMap<SpotType, TreeMap<String, Collection<Slot>>>(SpotType.class);

	/**
	 * Create a transformer for the given scenario; the class holds a reference, so changes made to the scenario after construction will be reflected in calls to the various helper methods.
	 * 
	 * @param scenario
	 */
	@Inject
	public LNGScenarioTransformer(final MMXRootObject rootObject) {

		init(rootObject);
	}

	protected void init(final MMXRootObject rootObject) {

		this.rootObject = rootObject;
		this.timezone = TimeZone.getTimeZone("UTC");
	}

	/**
	 * Get any {@link ITransformerExtension} and {@link IContractTransformer}s from the platform's registry.
	 */
	@Inject
	public boolean addPlatformTransformerExtensions() {

		if (transformerExtensions == null) {
			return false;
		}

		final Map<String, EClass> contracts = new HashMap<String, EClass>();

		final CommercialModel commercialModel = rootObject.getSubModel(CommercialModel.class);
		for (final Contract c : commercialModel.getSalesContracts()) {
			contracts.put(c.eClass().getInstanceClass().getCanonicalName(), c.eClass());
		}

		for (final Contract c : commercialModel.getPurchaseContracts()) {
			contracts.put(c.eClass().getInstanceClass().getCanonicalName(), c.eClass());
		}

		for (final ContractTransformer t : transformerExtensions) {

			final ModelClass[] modelClass = t.getModelClass();

			final HashSet<String> modelClasses = new HashSet<String>();
			for (final ModelClass mc : modelClass) {
				modelClasses.add(mc.getTransformer());
			}
			modelClasses.retainAll(contracts.keySet());

			final ITransformerExtension transformer = t.createTransformer();
			addTransformerExtension(transformer);

			if (transformer instanceof IContractTransformer) {
				final List<EClass> classes = new LinkedList<EClass>();
				for (final String mc : modelClasses) {
					classes.add(contracts.get(mc));
				}
				addContractTransformer((IContractTransformer) transformer, classes);
			}
		}

		return true;
	}

	public void addTransformerExtension(final ITransformerExtension extension) {
		log.debug(extension.getClass().getCanonicalName() + " added to transformer extensions");
		injector.injectMembers(extension);
		allTransformerExtensions.add(extension);
	}

	public void addContractTransformer(final IContractTransformer transformer, final Collection<EClass> forContracts) {
		contractTransformers.add(transformer);
		for (final EClass ec : forContracts) {
			log.debug(transformer.getClass().getCanonicalName() + " handling contracts with eClass " + ec.getName());
			contractTransformersByEClass.put(ec, transformer);
		}
	}

	/**
	 * Instantiates and returns an {@link IOptimisationData} isomorphic to the contained scenario.
	 * 
	 * @return
	 * @throws IncompleteScenarioException
	 */
	public IOptimisationData createOptimisationData(final ResourcelessModelEntityMap entities) throws IncompleteScenarioException {
		/*
		 * Set reference for hour 0
		 */
		findEarliestAndLatestTimes();

		// set earliest and latest times into entities
		entities.setEarliestDate(earliestTime);

		/**
		 * First, create all the market curves (should these come through the builder?)
		 */

		final Association<Index<?>, ICurve> indexAssociation = new Association<Index<?>, ICurve>();

		final PricingModel pricingModel = rootObject.getSubModel(PricingModel.class);
		for (final Index<Double> index : pricingModel.getCommodityIndices()) {
			if (index instanceof DataIndex) {
				final DataIndex<Double> di = (DataIndex) index;
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
					times[k] = convertTime(e.getFirst());
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

		// set up the contract transformers
		for (final ITransformerExtension extension : allTransformerExtensions) {
			extension.startTransforming(rootObject, entities, builder);
		}

		final CommercialModel commercialModel = rootObject.getSubModel(CommercialModel.class);

		for (final PurchaseContract c : commercialModel.getPurchaseContracts()) {
			final IContractTransformer transformer = contractTransformersByEClass.get(c.eClass());
			final ILoadPriceCalculator calculator = transformer.transformPurchaseContract(c);
			entities.addModelObject(c, calculator);
		}

		for (final SalesContract c : commercialModel.getSalesContracts()) {
			final IContractTransformer transformer = contractTransformersByEClass.get(c.eClass());
			final ISalesPriceCalculator calculator = transformer.transformSalesContract(c);
			entities.addModelObject(c, calculator);
		}

		/**
		 * Bidirectionally maps EMF {@link Port} Models to {@link IPort}s in the builder.
		 */
		final Association<Port, IPort> portAssociation = new Association<Port, IPort>();
		/**
		 * Lists all the {@link IPort}s created for this scenario
		 */
		final List<IPort> allPorts = new ArrayList<IPort>();
		/**
		 * A reverse-lookup for the {@link allPorts} array.
		 */
		final Map<IPort, Integer> portIndices = new HashMap<IPort, Integer>();

		/*
		 * Construct ports for each port in the scenario port model, and keep them in a two-way lookup table (the two-way lookup is needed to do things like setting distances later).
		 */
		final PortModel portModel = rootObject.getSubModel(PortModel.class);

		final Map<APort, ICooldownPriceCalculator> cooldownCalculators = new HashMap<APort, ICooldownPriceCalculator>();
		for (final CooldownPrice price : pricingModel.getCooldownPrices()) {
			final ICooldownPriceCalculator cooldownCalculator = new MarketPriceContract(indexAssociation.lookup(price.getIndex()), 0, OptimiserUnitConvertor.convertToInternalConversionFactor(1));

			for (final APort port : SetUtils.getPorts(price.getPorts())) {
				cooldownCalculators.put(port, cooldownCalculator);
			}
		}

		for (final Port ePort : portModel.getPorts()) {
			final IPort port;
			if (ePort.getLocation() != null) {
				final Location loc = ePort.getLocation();
				port = builder.createPort(ePort.getName(), !ePort.isAllowCooldown(), cooldownCalculators.get(ePort), (float) loc.getLat(), (float) loc.getLon());
			} else {
				port = builder.createPort(ePort.getName(), !ePort.isAllowCooldown(), cooldownCalculators.get(ePort));
			}
			portAssociation.add(ePort, port);
			portIndices.put(port, allPorts.size());
			allPorts.add(port);
			entities.addModelObject(ePort, port);
		}

		final Pair<Association<VesselClass, IVesselClass>, Association<Vessel, IVessel>> vesselAssociations = buildFleet(builder, portAssociation, entities);

		// process port costs
		final PricingModel pricing = rootObject.getSubModel(PricingModel.class);
		if (pricing != null) {
			for (final PortCost cost : pricing.getPortCosts()) {
				for (final APort port : SetUtils.getPorts(cost.getPorts())) {
					for (final PortCostEntry entry : cost.getEntries()) {
						PortType type = null;
						switch (entry.getActivity()) {
						case LOAD:
							type = PortType.Load;
							break;
						case DISCHARGE:
							type = PortType.Discharge;
							break;
						case DRYDOCK:
							type = PortType.DryDock;
							break;
						case MAINTENANCE:
							type = PortType.Maintenance;
							break;
						}

						if (type != null) {
							for (final IVessel v : allVessels) {
								// TODO should the builder handle the application of costs to vessel classes?
								final long activityCost = OptimiserUnitConvertor.convertToInternalFixedCost(cost.getPortCost(vesselAssociations.getFirst().reverseLookup(v.getVesselClass()),
										entry.getActivity()));
								builder.setPortCost(portAssociation.lookup((Port) port), v, type, activityCost);
							}
						}
					}
				}

			}
		}

		buildDistances(builder, portAssociation, allPorts, portIndices, vesselAssociations.getFirst());

		buildCargoes(builder, portAssociation, indexAssociation, vesselAssociations.getSecond(), contractTransformers, entities, getOptimisationSettings().isRewire());

		buildVesselEvents(builder, portAssociation, vesselAssociations.getFirst(), vesselAssociations.getSecond(), entities);

		buildSpotCargoMarkets(builder, portAssociation, indexAssociation, contractTransformers, entities);
		// buildDiscountCurves(builder);

		// freezeStartSequences(builder, entities);

		// freeze any frozen assignments
		freezeInputModel(builder, entities);

		for (final ITransformerExtension extension : allTransformerExtensions) {
			extension.finishTransforming();
		}

		builder.setEarliestDate(earliestTime);

		return builder.getOptimisationData();
	}

	private void freezeInputModel(final ISchedulerBuilder builder, final ModelEntityMap entities) {

		Date freezeDate = null;
		final OptimiserModel optimiserModel = rootObject.getSubModel(OptimiserModel.class);
		if (optimiserModel != null) {
			final OptimiserSettings settings = optimiserModel.getActiveSetting();
			if (settings != null) {
				final OptimisationRange range = settings.getRange();
				if (range != null) {
					freezeDate = range.getOptimiseAfter();
				}
			}
		}

		final InputModel input = rootObject.getSubModel(InputModel.class);
		if (input != null) {

			for (final ElementAssignment assignment : input.getElementAssignments()) {
				final UUIDObject o = assignment.getAssignedObject();

				boolean freeze = assignment.isLocked();

				if (!freeze && freezeDate != null) {
					if (o instanceof Cargo) {
						final Cargo cargo = (Cargo) o;
						if (cargo.getLoadSlot().getWindowStart().before(freezeDate)) {
							freeze = true;
						}
					} else if (o instanceof VesselEvent) {
						final VesselEvent vesselEvent = (VesselEvent) o;
						if (vesselEvent.getStartBy().before(freezeDate)) {
							freeze = true;
						}
					}
				}

				if (!freeze) {
					continue;
				}

				final AVesselSet vesselSet = assignment.getAssignment();
				final IVessel vessel;
				if (vesselSet instanceof VesselClass) {
					final List<IVessel> spots = spotVesselsByClass.get(vesselSet);
					if (spots != null && spots.isEmpty() == false) {
						vessel = spots.get(0);
						spots.remove(0);
					} else {
						vessel = null;
					}

				} else if (vesselSet instanceof Vessel) {
					vessel = entities.getOptimiserObject(vesselSet, IVessel.class);
				} else {
					vessel = null;
				}

				if (vessel == null) {
					continue;
				}

				if (o instanceof Cargo) {
					final ICargo cargo = entities.getOptimiserObject(o, ICargo.class);
					if (cargo != null) {
						// bind slots to vessel
						builder.constrainSlotToVessels(cargo.getLoadOption(), Collections.singleton(vessel));
						builder.constrainSlotToVessels(cargo.getDischargeOption(), Collections.singleton(vessel));
						// bind sequencing as well - this forces
						// previousSlot to come before currentSlot.
						builder.constrainSlotAdjacency(cargo.getLoadOption(), cargo.getDischargeOption());
					}
				} else if (o instanceof VesselEvent) {
					final IVesselEventPortSlot slot = entities.getOptimiserObject(o, IVesselEventPortSlot.class);
					if (slot != null) {
						builder.constrainSlotToVessels(slot, Collections.singleton(vessel));
					}
				}
			}
		}
	}

	// /**
	// * Create and associate discount curves
	// *
	// * This ought to be in {@link OptimisationTransformer} but that doesn't have access to set up DCPs.
	// *
	// * @param builder
	// */
	// private void buildDiscountCurves(final ISchedulerBuilder builder) {
	// // set up DCP
	//
	// final DiscountCurve defaultCurve = scenario.getOptimisation().getCurrentSettings().isSetDefaultDiscountCurve() ? scenario.getOptimisation().getCurrentSettings().getDefaultDiscountCurve()
	// : null;
	//
	// final ICurve realDefaultCurve;
	// if (defaultCurve == null) {
	// realDefaultCurve = new ConstantValueCurve(1);
	// } else {
	// realDefaultCurve = buildDiscountCurve(defaultCurve);
	// }
	//
	// for (final Objective objective : scenario.getOptimisation().getCurrentSettings().getObjectives()) {
	// if (objective.isSetDiscountCurve()) {
	// builder.setFitnessComponentDiscountCurve(objective.getName(), buildDiscountCurve(objective.getDiscountCurve()));
	// } else {
	// builder.setFitnessComponentDiscountCurve(objective.getName(), realDefaultCurve);
	// }
	// }
	// }

	// /**
	// * @param defaultCurve
	// * @return
	// */
	// private ICurve buildDiscountCurve(final DiscountCurve curve) {
	// final InterpolatingDiscountCurve realCurve = new InterpolatingDiscountCurve();
	//
	// final int baseTime = curve.isSetStartDate() ? convertTime(curve.getStartDate()) : 0;
	// if (baseTime > 0) {
	// realCurve.setValueAtPoint(0, 1);
	// realCurve.setValueAtPoint(baseTime - 1, 1);
	// }
	//
	// for (final Discount d : curve.getDiscounts()) {
	// realCurve.setValueAtPoint(baseTime + d.getTime(), d.getDiscountFactor());
	// }
	// return realCurve;
	// }

	private StepwiseIntegerCurve createCurveForDoubleIndex(final Index<Double> index, final double scale) {
		final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();

		curve.setDefaultValue(0);

		boolean gotOneEarlyDate = false;
		for (final Date date : index.getDates()) {
			final double value = index.getValueForMonth(date);
			final int hours = convertTime(date);
			if (hours < 0) {
				if (gotOneEarlyDate)
					continue;
				gotOneEarlyDate = true;
			}
			curve.setValueAfter(hours, OptimiserUnitConvertor.convertToInternalPrice(scale * value));
		}

		return curve;
	}

	private StepwiseIntegerCurve createCurveForIntegerIndex(final Index<Integer> index, final double scale) {
		final StepwiseIntegerCurve curve = new StepwiseIntegerCurve();

		curve.setDefaultValue(0);

		boolean gotOneEarlyDate = false;
		for (final Date date : index.getDates()) {
			final int value = index.getValueForMonth(date);
			final int hours = convertTime(date);
			if (hours < 0) {
				if (gotOneEarlyDate) {
					// TODO: While we should skip all the early stuff, we need to keep the latest, this currently however takes the earliest value!
					// continue;
				}
				gotOneEarlyDate = true;
			}
			curve.setValueAfter(hours, OptimiserUnitConvertor.convertToInternalPrice((double) value * scale));
		}

		return curve;
	}

	/**
	 * Find the earliest and latest times set by events in the model. This takes into account:
	 * 
	 * <ul>
	 * <li>Slot window dates</li>
	 * <li>Vessel event dates</li>
	 * <li>Vessel availability dates</li>
	 * </ul>
	 */
	private void findEarliestAndLatestTimes() {
		earliestTime = null;
		latestTime = null;
		final FleetModel fleet = rootObject.getSubModel(FleetModel.class);
		final CargoModel cargo = rootObject.getSubModel(CargoModel.class);

		final HashSet<Date> allDates = new HashSet<Date>();

		for (final VesselEvent event : fleet.getVesselEvents()) {
			allDates.add(event.getStartBy());
			allDates.add(event.getStartAfter());
		}
		for (final Vessel vessel : fleet.getVessels()) {
			if (vessel.getAvailability().isSetStartBy())
				allDates.add(vessel.getAvailability().getStartBy());
			if (vessel.getAvailability().isSetStartAfter())
				allDates.add(vessel.getAvailability().getStartAfter());

			if (vessel.getAvailability().isSetEndBy())
				allDates.add(vessel.getAvailability().getEndBy());
			if (vessel.getAvailability().isSetEndAfter())
				allDates.add(vessel.getAvailability().getEndAfter());
		}
		for (final Slot s : cargo.getLoadSlots()) {
			allDates.add(s.getWindowStartWithSlotOrPortTime());
			allDates.add(s.getWindowEndWithSlotOrPortTime());
		}
		for (final Slot s : cargo.getDischargeSlots()) {
			allDates.add(s.getWindowStartWithSlotOrPortTime());
			allDates.add(s.getWindowEndWithSlotOrPortTime());
		}

		earliestTime = Collections.min(allDates);
		latestTime = Collections.max(allDates);
	}

	private void buildVesselEvents(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Association<VesselClass, IVesselClass> classes,
			final Association<Vessel, IVessel> vessels, final ModelEntityMap entities) {

		final Date latestDate = getOptimisationSettings().getRange().isSetOptimiseBefore() ? getOptimisationSettings().getRange().getOptimiseBefore() : latestTime;

		final FleetModel fleetModel = rootObject.getSubModel(FleetModel.class);

		for (final VesselEvent event : fleetModel.getVesselEvents()) {

			if (event.getStartAfter().after(latestDate)) {
				continue;
			}

			final ITimeWindow window = builder.createTimeWindow(convertTime(event.getStartAfter()), convertTime(event.getStartBy()));
			final IPort port = portAssociation.lookup(event.getPort());
			final int durationHours = event.getDurationInDays() * 24;
			final IVesselEventPortSlot builderSlot;
			if (event instanceof CharterOutEvent) {
				final CharterOutEvent charterOut = (CharterOutEvent) event;
				final IPort endPort = portAssociation.lookup(charterOut.isSetRelocateTo() ? charterOut.getRelocateTo() : charterOut.getPort());
				final HeelOptions heelOptions = charterOut.getHeelOptions();
				final long maxHeel = heelOptions.isSetVolumeAvailable() ? OptimiserUnitConvertor.convertToInternalVolume(heelOptions.getVolumeAvailable()) : Long.MAX_VALUE;
				final long totalHireCost = OptimiserUnitConvertor.convertToInternalDailyCost(charterOut.getHireRate()) * (long) charterOut.getDurationInDays();
				final long repositioning = OptimiserUnitConvertor.convertToInternalFixedCost(charterOut.getRepositioningFee());
				builderSlot = builder.createCharterOutEvent(event.getName(), window, port, endPort, durationHours, maxHeel,
						OptimiserUnitConvertor.convertToInternalConversionFactor(heelOptions.getCvValue()), OptimiserUnitConvertor.convertToInternalPrice(heelOptions.getPricePerMMBTU()),
						totalHireCost, repositioning);
			} else if (event instanceof DryDockEvent) {
				builderSlot = builder.createDrydockEvent(event.getName(), window, port, durationHours);
			} else if (event instanceof MaintenanceEvent) {
				builderSlot = builder.createMaintenanceEvent(event.getName(), window, port, durationHours);
			} else {
				throw new RuntimeException("Unknown event type: " + event.getClass().getName());
			}

			for (final AVessel v : SetUtils.getVessels(event.getAllowedVessels())) {
				builder.addVesselEventVessel(builderSlot, vessels.lookup((Vessel) v));
			}

			entities.addModelObject(event, builderSlot);
		}
	}

	/**
	 * Extract the cargoes from the scenario and add them to the given builder.
	 * 
	 * @param builder
	 *            current builder. should already have ports/distances/vessels built
	 * @param indexAssociation
	 * @param contractTransformers
	 * @param entities
	 * @param defaultRewiring
	 */
	private void buildCargoes(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Association<Index<?>, ICurve> indexAssociation,
			final Association<Vessel, IVessel> vesselAssociation, final Collection<IContractTransformer> contractTransformers, final ModelEntityMap entities, final boolean defaultRewiring) {

		final Date latestDate = getOptimisationSettings().getRange().isSetOptimiseBefore() ? getOptimisationSettings().getRange().getOptimiseBefore() : latestTime;

		final Set<LoadSlot> usedLoadSlots = new HashSet<LoadSlot>();
		final Set<DischargeSlot> usedDischargeSlots = new HashSet<DischargeSlot>();

		// TODO: Refactor into Load and Discharge slot creation before cargo paring
		final CargoModel cargoModel = rootObject.getSubModel(CargoModel.class);
		for (final Cargo eCargo : cargoModel.getCargoes()) {
			if (eCargo.getLoadSlot().getWindowStartWithSlotOrPortTime().after(latestDate)) {
				continue;
			}
			final ILoadOption load;
			final LoadSlot loadSlot = eCargo.getLoadSlot();
			{

				load = createLoadOption(builder, portAssociation, contractTransformers, entities, loadSlot);
				usedLoadSlots.add(loadSlot);
			}
			final DischargeSlot dischargeSlot = eCargo.getDischargeSlot();
			final IDischargeOption discharge;
			{
				discharge = createDischargeOption(builder, portAssociation, contractTransformers, entities, dischargeSlot);
				usedDischargeSlots.add(dischargeSlot);
			}
			// Bind FOB/DES slots to resource
			if (loadSlot.isDESPurchase()) {
				if (loadSlot instanceof SpotLoadSlot) {
					final SpotLoadSlot spotLoadSlot = (SpotLoadSlot) loadSlot;
					final Set<IPort> marketPorts = new HashSet<IPort>();
					{
						final ASpotMarket market = spotLoadSlot.getMarket();
						if (market instanceof DESPurchaseMarket) {
							final DESPurchaseMarket desPurchaseMarket = (DESPurchaseMarket) market;
							final Set<APort> portSet = SetUtils.getPorts(desPurchaseMarket.getDestinationPorts());
							for (final APort ap : portSet) {
								if (ap instanceof Port) {
									final IPort ip = portAssociation.lookup((Port) ap);
									if (ip != null) {
										marketPorts.add(ip);
									}
								}
							}
						}

					}
					builder.bindDischargeSlotsToDESPurchase(load, marketPorts);
				} else {
					// Bind to this port -- TODO: Fix to discharge?
					builder.bindDischargeSlotsToDESPurchase(load, Collections.singleton(discharge.getPort()));
				}
			}
			if (dischargeSlot.isFOBSale()) {
				builder.bindLoadSlotsToFOBSale(discharge, load.getPort());
			}

			final ICargo cargo = builder.createCargo(eCargo.getName(), load, discharge, eCargo.isSetAllowRewiring() ? eCargo.isAllowRewiring() : defaultRewiring);

			entities.addModelObject(eCargo, cargo);
			if (eCargo.getCargoType() == CargoType.FLEET) {

				final Set<AVessel> allowedVessels = SetUtils.getVessels(eCargo.getAllowedVessels());
				if (!allowedVessels.isEmpty()) {
					final Set<IVessel> vesselsForCargo = new HashSet<IVessel>();
					for (final AVessel v : allowedVessels) {
						vesselsForCargo.add(vesselAssociation.lookup((Vessel) v));
					}
					builder.setCargoVesselRestriction(cargo, vesselsForCargo);
				}
			}
		}

		for (final LoadSlot loadSlot : cargoModel.getLoadSlots()) {

			// TODO: Filter on date

			// if (eCargo.getLoadSlot().getWindowStartWithSlotOrPortTime().after(latestDate)) {
			// continue;
			// }
			if (usedLoadSlots.contains(loadSlot)) {
				continue;
			}

			final ILoadOption load;
			{

				// Make optional
				load = createLoadOption(builder, portAssociation, contractTransformers, entities, loadSlot);
				if (!loadSlot.isOptional()) {
					builder.setSoftRequired(load);
				}
			}
			// Bind FOB/DES slots to resource
			if (loadSlot.isDESPurchase()) {
				if (loadSlot instanceof SpotLoadSlot) {
					final SpotLoadSlot spotLoadSlot = (SpotLoadSlot) loadSlot;
					final Set<IPort> marketPorts = new HashSet<IPort>();
					{
						final ASpotMarket market = spotLoadSlot.getMarket();
						if (market instanceof DESPurchaseMarket) {
							final DESPurchaseMarket desPurchaseMarket = (DESPurchaseMarket) market;
							final Set<APort> portSet = SetUtils.getPorts(desPurchaseMarket.getDestinationPorts());
							for (final APort ap : portSet) {
								if (ap instanceof Port) {
									final IPort ip = portAssociation.lookup((Port) ap);
									if (ip != null) {
										marketPorts.add(ip);
									}
								}
							}
						}

					}
					builder.bindDischargeSlotsToDESPurchase(load, marketPorts);
				} else {
					// Bind to this port -- TODO: Fix to discharge?
					builder.bindDischargeSlotsToDESPurchase(load, Collections.singleton(load.getPort()));
				}
			}
		}

		for (final DischargeSlot dischargeSlot : cargoModel.getDischargeSlots()) {

			// TODO: Filter on date
			// if (eCargo.getLoadSlot().getWindowStartWithSlotOrPortTime().after(latestDate)) {
			// continue;
			// }
			if (usedDischargeSlots.contains(dischargeSlot)) {
				continue;
			}

			final IDischargeOption discharge;
			{
				discharge = createDischargeOption(builder, portAssociation, contractTransformers, entities, dischargeSlot);
				if (!dischargeSlot.isOptional()) {
					builder.setSoftRequired(discharge);
				}
			}
			// Bind FOB/DES slots to resource
			if (dischargeSlot.isFOBSale()) {
				builder.bindLoadSlotsToFOBSale(discharge, discharge.getPort());
			}

		}
	}

	private IDischargeOption createDischargeOption(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap entities, final DischargeSlot dischargeSlot) {
		final IDischargeOption discharge;
		usedIDStrings.add(dischargeSlot.getName());

		final ITimeWindow dischargeWindow = builder.createTimeWindow(convertTime(earliestTime, dischargeSlot.getWindowStartWithSlotOrPortTime()),
				convertTime(earliestTime, dischargeSlot.getWindowEndWithSlotOrPortTime()));

		final ISalesPriceCalculator dischargePriceCalculator;

		if (dischargeSlot.isSetPriceExpression()) {

			final String priceExpression = dischargeSlot.getPriceExpression();
			if (IBreakEvenEvaluator.MARKER.equals(priceExpression)) {
				dischargePriceCalculator = new BreakEvenSalesPriceCalculator();
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
				dischargePriceCalculator = new MarketPriceContract(curve, 0, 1000);
			}

		} else {
			dischargePriceCalculator = entities.getOptimiserObject(dischargeSlot.getContract(), ISalesPriceCalculator.class);
		}

		if (dischargeSlot.isFOBSale()) {
			discharge = builder.createFOBSaleDischargeSlot(dischargeSlot.getName(), portAssociation.lookup(dischargeSlot.getPort()), dischargeWindow,
					OptimiserUnitConvertor.convertToInternalVolume(dischargeSlot.getSlotOrContractMinQuantity()),
					OptimiserUnitConvertor.convertToInternalVolume(dischargeSlot.getSlotOrContractMaxQuantity()), dischargePriceCalculator, dischargeSlot.isOptional());
		} else {
			discharge = builder.createDischargeSlot(dischargeSlot.getName(), portAssociation.lookup(dischargeSlot.getPort()), dischargeWindow,
					OptimiserUnitConvertor.convertToInternalVolume(dischargeSlot.getSlotOrContractMinQuantity()),
					OptimiserUnitConvertor.convertToInternalVolume(dischargeSlot.getSlotOrContractMaxQuantity()), dischargePriceCalculator, dischargeSlot.getSlotOrPortDuration(),
					dischargeSlot.isOptional());
		}
		if (dischargeSlot instanceof SpotSlot) {
			marketSlotsByID.put(dischargeSlot.getName(), dischargeSlot);
			addSpotSlotToCount((SpotSlot) dischargeSlot);
		}

		entities.addModelObject(dischargeSlot, discharge);
		for (final IContractTransformer contractTransformer : contractTransformers) {
			contractTransformer.slotTransformed(dischargeSlot, discharge);
		}
		return discharge;
	}

	private ILoadOption createLoadOption(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap entities, final LoadSlot loadSlot) {
		final ILoadOption load;
		usedIDStrings.add(loadSlot.getName());

		final ITimeWindow loadWindow = builder.createTimeWindow(convertTime(earliestTime, loadSlot.getWindowStartWithSlotOrPortTime()),
				convertTime(earliestTime, loadSlot.getWindowEndWithSlotOrPortTime()));

		final ILoadPriceCalculator loadPriceCalculator;

		if (loadSlot.isSetPriceExpression()) {

			final String priceExpression = loadSlot.getPriceExpression();
			if (IBreakEvenEvaluator.MARKER.equals(priceExpression)) {
				loadPriceCalculator = new BreakEvenLoadPriceCalculator();
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
				loadPriceCalculator = new MarketPriceContract(curve, 0, 1000);
			}
		} else {
			final PurchaseContract purchaseContract = (PurchaseContract) (loadSlot.getContract());
			loadPriceCalculator = entities.getOptimiserObject(purchaseContract, ILoadPriceCalculator.class);
		}

		if (loadSlot.isDESPurchase()) {
			load = builder.createDESPurchaseLoadSlot(loadSlot.getName(), portAssociation.lookup(loadSlot.getPort()), loadWindow,
					OptimiserUnitConvertor.convertToInternalVolume(loadSlot.getSlotOrContractMinQuantity()), OptimiserUnitConvertor.convertToInternalVolume(loadSlot.getSlotOrContractMaxQuantity()),
					loadPriceCalculator, OptimiserUnitConvertor.convertToInternalConversionFactor(loadSlot.getSlotOrPortCV()), loadSlot.isOptional());
		} else {
			load = builder.createLoadSlot(loadSlot.getName(), portAssociation.lookup(loadSlot.getPort()), loadWindow,
					OptimiserUnitConvertor.convertToInternalVolume(loadSlot.getSlotOrContractMinQuantity()), OptimiserUnitConvertor.convertToInternalVolume(loadSlot.getSlotOrContractMaxQuantity()),
					loadPriceCalculator, OptimiserUnitConvertor.convertToInternalConversionFactor(loadSlot.getSlotOrPortCV()), loadSlot.getSlotOrPortDuration(), loadSlot.isSetArriveCold(),
					loadSlot.isArriveCold(), loadSlot.isOptional());
		}
		// Store market slots for lookup when building spot markets.
		entities.addModelObject(loadSlot, load);

		for (final IContractTransformer contractTransformer : contractTransformers) {
			contractTransformer.slotTransformed(loadSlot, load);
		}
		if (loadSlot instanceof SpotSlot) {
			marketSlotsByID.put(loadSlot.getName(), loadSlot);
			addSpotSlotToCount((SpotSlot) loadSlot);
		}
		return load;
	}

	private void buildSpotCargoMarkets(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Association<Index<?>, ICurve> indexAssociation,
			final Collection<IContractTransformer> contractTransformers, final ModelEntityMap entities) {

		final PricingModel pricingModel = rootObject.getSubModel(PricingModel.class);
		if (pricingModel == null) {
			return;
		}
		final Date latestDate = getOptimisationSettings().getRange().isSetOptimiseBefore() ? getOptimisationSettings().getRange().getOptimiseBefore() : latestTime;

		buildDESPurchaseSpotMarket(builder, portAssociation, contractTransformers, entities, latestDate, pricingModel.getDesPurchaseSpotMarket());
		buildDESSalesSpotMarket(builder, portAssociation, contractTransformers, entities, latestDate, pricingModel.getDesSalesSpotMarket());
		buildFOBPurchaseSpotMarket(builder, portAssociation, contractTransformers, entities, latestDate, pricingModel.getFobPurchasesSpotMarket());
		buildFOBSalesSpotMarket(builder, portAssociation, contractTransformers, entities, latestDate, pricingModel.getFobSalesSpotMarket());

	}

	private void buildDESPurchaseSpotMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap entities, final Date latestDate, final SpotMarketGroup desPurchaseSpotMarket) {
		if (desPurchaseSpotMarket != null) {

			final SpotAvailability groupAvailability = desPurchaseSpotMarket.getAvailability();

			// Loop over the date range in the optimisation generating market slots
			final Calendar cal = Calendar.getInstance();
			cal.setTime(earliestTime);

			// Set back to start of month
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			Date startTime = cal.getTime();
			while (startTime.before(latestDate)) {

				final String yearMonthString = getKeyForDate(cal.getTime());

				// Roll forward
				cal.add(Calendar.MONTH, 1);
				final Date endTime = cal.getTime();

				final List<IPortSlot> marketGroupSlots = new ArrayList<IPortSlot>();

				for (final SpotMarket market : desPurchaseSpotMarket.getMarkets()) {
					assert market instanceof DESPurchaseMarket;
					if (market instanceof DESPurchaseMarket) {
						final DESPurchaseMarket desPurchaseMarket = (DESPurchaseMarket) market;
						final Set<APort> portSet = SetUtils.getPorts(desPurchaseMarket.getDestinationPorts());
						final Set<IPort> marketPorts = new HashSet<IPort>();
						for (final APort ap : portSet) {
							if (ap instanceof Port) {
								final IPort ip = portAssociation.lookup((Port) ap);
								if (ip != null) {
									marketPorts.add(ip);
								}
							}
						}

						final Collection<Slot> existing = getSpotSlots(SpotType.DES_PURCHASE, getKeyForDate(startTime));
						final int count = getAvailabilityForDate(market.getAvailability(), startTime);

						final List<IPortSlot> marketSlots = new ArrayList<IPortSlot>(count);
						for (final Slot slot : existing) {
							final IPortSlot portSlot = entities.getOptimiserObject(slot, IPortSlot.class);
							marketSlots.add(portSlot);
							marketGroupSlots.add(portSlot);
						}

						final int remaining = count - existing.size();
						if (remaining > 0) {
							int offset = 0;
							for (int i = 0; i < remaining; ++i) {

								final ITimeWindow tw = builder.createTimeWindow(convertTime(earliestTime, startTime), convertTime(earliestTime, endTime));

								final int cargoCVValue = OptimiserUnitConvertor.convertToInternalConversionFactor(desPurchaseMarket.getCv());

								final String idPrefix = market.getName() + "-" + yearMonthString + "-";

								// Avoid ID clash
								String id = idPrefix + (i + offset);
								while (usedIDStrings.contains(id)) {
									id = idPrefix + (i + ++offset);
								}
								usedIDStrings.add(id);

								final ILoadPriceCalculator priceCalculator = entities.getOptimiserObject(market.getContract(), ILoadPriceCalculator.class);

								final ILoadOption desPurchaseSlot = builder.createDESPurchaseLoadSlot(id, null, tw, OptimiserUnitConvertor.convertToInternalVolume(market.getMinQuantity()),
										OptimiserUnitConvertor.convertToInternalVolume(market.getMaxQuantity()), priceCalculator, cargoCVValue, true);

								// Create a fake model object to add in here;
								final SpotLoadSlot desSlot = CargoFactory.eINSTANCE.createSpotLoadSlot();
								desSlot.setDESPurchase(true);
								desSlot.setName(id);
								desSlot.setArriveCold(false);
								desSlot.setCargoCV(desPurchaseMarket.getCv());
								desSlot.setWindowStart(new Date(startTime.getTime()));
								desSlot.setContract(desPurchaseMarket.getContract());
								desSlot.setOptional(true);
								final long duration = (endTime.getTime() - startTime.getTime()) / 1000 / 60 / 60;
								desSlot.setWindowSize((int) duration);
								// Key piece of information
								desSlot.setMarket(desPurchaseMarket);
								entities.addModelObject(desSlot, desPurchaseSlot);

								for (final IContractTransformer contractTransformer : contractTransformers) {
									contractTransformer.slotTransformed(desSlot, desPurchaseSlot);
								}

								builder.bindDischargeSlotsToDESPurchase(desPurchaseSlot, marketPorts);

								marketSlots.add(desPurchaseSlot);
								marketGroupSlots.add(desPurchaseSlot);
							}
						}
						if (marketSlots.size() > count) {
							// Take this count and add into a constraint.
							builder.createSlotGroupCount(marketSlots, count);
						}
					}
				}

				// Take group availability curve and add into a constraint.
				if (groupAvailability != null) {
					final int count = getAvailabilityForDate(groupAvailability, startTime);
					if (marketGroupSlots.size() > count) {
						builder.createSlotGroupCount(marketGroupSlots, count);
					}
				}

				startTime = cal.getTime();
			}
		}
	}

	private void buildFOBSalesSpotMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap entities, final Date latestDate, final SpotMarketGroup fobSalesSpotMarket) {
		if (fobSalesSpotMarket != null) {

			final SpotAvailability groupAvailability = fobSalesSpotMarket.getAvailability();

			// Loop over the date range in the optimisation generating market slots
			final Calendar cal = Calendar.getInstance();
			cal.setTime(earliestTime);

			// Set back to start of month
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			Date startTime = cal.getTime();
			while (startTime.before(latestDate)) {

				final String yearMonthString = getKeyForDate(cal.getTime());
				// Roll forward
				cal.add(Calendar.MONTH, 1);
				final Date endTime = cal.getTime();

				final List<IPortSlot> marketGroupSlots = new ArrayList<IPortSlot>();

				for (final SpotMarket market : fobSalesSpotMarket.getMarkets()) {
					assert market instanceof FOBSalesMarket;
					if (market instanceof FOBSalesMarket) {
						final FOBSalesMarket fobSaleMarket = (FOBSalesMarket) market;
						final Port loadPort = (Port) fobSaleMarket.getLoadPort();
						final IPort loadIPort = portAssociation.lookup(loadPort);

						final Collection<Slot> existing = getSpotSlots(SpotType.FOB_SALE, getKeyForDate(startTime));
						final int count = getAvailabilityForDate(market.getAvailability(), startTime);

						final List<IPortSlot> marketSlots = new ArrayList<IPortSlot>(count);
						for (final Slot slot : existing) {
							final IPortSlot portSlot = entities.getOptimiserObject(slot, IPortSlot.class);
							marketSlots.add(portSlot);
							marketGroupSlots.add(portSlot);
						}

						final int remaining = count - existing.size();
						if (remaining > 0) {
							int offset = 0;
							for (int i = 0; i < remaining; ++i) {

								final ITimeWindow tw = builder.createTimeWindow(convertTime(earliestTime, startTime), convertTime(earliestTime, endTime));

								final String idPrefix = market.getName() + "-" + yearMonthString + "-";

								String id = idPrefix + (i + offset);
								while (usedIDStrings.contains(id)) {
									id = idPrefix + (i + ++offset);
								}
								usedIDStrings.add(id);

								final ISalesPriceCalculator priceCalculator = entities.getOptimiserObject(market.getContract(), ISalesPriceCalculator.class);

								final IDischargeOption fobSaleSlot = builder.createFOBSaleDischargeSlot(id, loadIPort, tw, OptimiserUnitConvertor.convertToInternalVolume(market.getMinQuantity()),
										OptimiserUnitConvertor.convertToInternalVolume(market.getMaxQuantity()), priceCalculator, true);

								// Create a fake model object to add in here;
								final SpotDischargeSlot fobSlot = CargoFactory.eINSTANCE.createSpotDischargeSlot();
								fobSlot.setFOBSale(true);
								fobSlot.setName(id);
								fobSlot.setPort(loadPort);
								fobSlot.setWindowStart(new Date(startTime.getTime()));
								fobSlot.setContract(fobSaleMarket.getContract());
								fobSlot.setOptional(true);
								final long duration = (endTime.getTime() - startTime.getTime()) / 1000 / 60 / 60;
								fobSlot.setWindowSize((int) duration);
								// Key piece of information
								fobSlot.setMarket(fobSaleMarket);
								entities.addModelObject(fobSlot, fobSaleSlot);

								for (final IContractTransformer contractTransformer : contractTransformers) {
									contractTransformer.slotTransformed(fobSlot, fobSaleSlot);
								}

								builder.bindLoadSlotsToFOBSale(fobSaleSlot, loadIPort);

								marketSlots.add(fobSaleSlot);
								marketGroupSlots.add(fobSaleSlot);
							}
						}

						if (marketSlots.size() > count) {
							// Take this count and add into a constraint.
							builder.createSlotGroupCount(marketSlots, count);
						}
					}
				}

				// Take group availability curve and add into a constraint.
				if (groupAvailability != null) {
					final int count = getAvailabilityForDate(groupAvailability, startTime);
					if (marketGroupSlots.size() > count) {
						builder.createSlotGroupCount(marketGroupSlots, count);
					}
				}

				startTime = cal.getTime();
			}
		}
	}

	private void buildDESSalesSpotMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap entities, final Date latestDate, final SpotMarketGroup desSalesSpotMarket) {
		if (desSalesSpotMarket != null) {

			final SpotAvailability groupAvailability = desSalesSpotMarket.getAvailability();

			// Loop over the date range in the optimisation generating market slots
			final Calendar cal = Calendar.getInstance();
			cal.setTime(earliestTime);

			// Set back to start of month
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			Date startTime = cal.getTime();
			while (startTime.before(latestDate)) {

				final String yearMonthString = getKeyForDate(cal.getTime());
				// Roll forward
				cal.add(Calendar.MONTH, 1);
				final Date endTime = cal.getTime();
				final List<IPortSlot> marketGroupSlots = new ArrayList<IPortSlot>();

				for (final SpotMarket market : desSalesSpotMarket.getMarkets()) {
					assert market instanceof DESSalesMarket;
					if (market instanceof DESSalesMarket) {
						final DESSalesMarket desSalesMarket = (DESSalesMarket) market;
						final APort notionalAPort = desSalesMarket.getNotionalPort();
						final IPort notionalIPort = portAssociation.lookup((Port) notionalAPort);

						final Collection<Slot> existing = getSpotSlots(SpotType.DES_SALE, getKeyForDate(startTime));
						final int count = getAvailabilityForDate(market.getAvailability(), startTime);

						final List<IPortSlot> marketSlots = new ArrayList<IPortSlot>(count);
						for (final Slot slot : existing) {
							final IPortSlot portSlot = entities.getOptimiserObject(slot, IPortSlot.class);
							marketSlots.add(portSlot);
							marketGroupSlots.add(portSlot);
						}

						final int remaining = count - existing.size();
						if (remaining > 0) {
							int offset = 0;
							for (int i = 0; i < remaining; ++i) {

								final ITimeWindow tw = builder.createTimeWindow(convertTime(earliestTime, startTime), convertTime(earliestTime, endTime));

								final String idPrefix = market.getName() + "-" + yearMonthString + "-";

								String id = idPrefix + (i + offset);
								while (usedIDStrings.contains(id)) {
									id = idPrefix + (i + ++offset);
								}
								usedIDStrings.add(id);

								final ISalesPriceCalculator priceCalculator = entities.getOptimiserObject(market.getContract(), ISalesPriceCalculator.class);

								// Create a fake model object to add in here;
								final SpotDischargeSlot desSlot = CargoFactory.eINSTANCE.createSpotDischargeSlot();
								desSlot.setName(id);
								desSlot.setWindowStart(new Date(startTime.getTime()));
								desSlot.setContract(desSalesMarket.getContract());
								desSlot.setOptional(true);
								desSlot.setPort((Port) notionalAPort);
								final long duration = (endTime.getTime() - startTime.getTime()) / 1000l / 60l / 60l;
								desSlot.setWindowSize((int) duration);

								final IDischargeOption desSalesSlot = builder.createDischargeSlot(id, notionalIPort, tw, OptimiserUnitConvertor.convertToInternalVolume(market.getMinQuantity()),
										OptimiserUnitConvertor.convertToInternalVolume(market.getMaxQuantity()), priceCalculator, desSlot.getSlotOrPortDuration(), true);

								// Key piece of information
								desSlot.setMarket(desSalesMarket);
								entities.addModelObject(desSlot, desSalesSlot);

								for (final IContractTransformer contractTransformer : contractTransformers) {
									contractTransformer.slotTransformed(desSlot, desSalesSlot);
								}

								marketSlots.add(desSalesSlot);
								marketGroupSlots.add(desSalesSlot);
							}
						}
						if (marketSlots.size() > count) {
							// Take this count and add into a constraint.
							builder.createSlotGroupCount(marketSlots, count);
						}
					}
				}

				// Take group availability curve and add into a constraint.
				if (groupAvailability != null) {
					final int count = getAvailabilityForDate(groupAvailability, startTime);
					if (marketGroupSlots.size() > count) {
						builder.createSlotGroupCount(marketGroupSlots, count);
					}
				}

				startTime = cal.getTime();
			}
		}
	}

	private void buildFOBPurchaseSpotMarket(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Collection<IContractTransformer> contractTransformers,
			final ModelEntityMap entities, final Date latestDate, final SpotMarketGroup fobPurchaseSpotMarket) {
		if (fobPurchaseSpotMarket != null) {

			final SpotAvailability groupAvailability = fobPurchaseSpotMarket.getAvailability();

			// Loop over the date range in the optimisation generating market slots
			final Calendar cal = Calendar.getInstance();
			cal.setTime(earliestTime);

			// Set back to start of month
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			Date startTime = cal.getTime();
			while (startTime.before(latestDate)) {

				final String yearMonthString = getKeyForDate(cal.getTime());

				// Roll forward
				cal.add(Calendar.MONTH, 1);
				final Date endTime = cal.getTime();

				final List<IPortSlot> marketGroupSlots = new ArrayList<IPortSlot>();

				for (final SpotMarket market : fobPurchaseSpotMarket.getMarkets()) {
					assert market instanceof FOBPurchasesMarket;
					if (market instanceof FOBPurchasesMarket) {
						final FOBPurchasesMarket fobPurchaseMarket = (FOBPurchasesMarket) market;
						final APort notionalAPort = fobPurchaseMarket.getNotionalPort();
						final IPort notionalIPort = portAssociation.lookup((Port) notionalAPort);

						final int cargoCVValue = OptimiserUnitConvertor.convertToInternalConversionFactor(fobPurchaseMarket.getCv());

						final Collection<Slot> existing = getSpotSlots(SpotType.FOB_PURCHASE, getKeyForDate(startTime));
						final int count = getAvailabilityForDate(market.getAvailability(), startTime);

						final List<IPortSlot> marketSlots = new ArrayList<IPortSlot>(count);
						for (final Slot slot : existing) {
							final IPortSlot portSlot = entities.getOptimiserObject(slot, IPortSlot.class);
							marketSlots.add(portSlot);
							marketGroupSlots.add(portSlot);
						}

						final int remaining = count - existing.size();
						if (remaining > 0) {
							int offset = 0;
							for (int i = 0; i < remaining; ++i) {

								final ITimeWindow tw = builder.createTimeWindow(convertTime(earliestTime, startTime), convertTime(earliestTime, endTime));

								final String idPrefix = market.getName() + "-" + yearMonthString + "-";

								String id = idPrefix + (i + offset);
								while (usedIDStrings.contains(id)) {

									id = idPrefix + (i + ++offset);
								}
								usedIDStrings.add(id);

								final ILoadPriceCalculator priceCalculator = entities.getOptimiserObject(market.getContract(), ILoadPriceCalculator.class);

								// Create a fake model object to add in here;
								final SpotLoadSlot fobSlot = CargoFactory.eINSTANCE.createSpotLoadSlot();
								fobSlot.setName(id);
								fobSlot.setWindowStart(new Date(startTime.getTime()));
								fobSlot.setContract(fobPurchaseMarket.getContract());
								fobSlot.setOptional(true);
								fobSlot.setArriveCold(true);
								fobSlot.setCargoCV(fobPurchaseMarket.getCv());
								fobSlot.setPort((Port) notionalAPort);
								final long duration = (endTime.getTime() - startTime.getTime()) / 1000l / 60l / 60l;
								fobSlot.setWindowSize((int) duration);

								final ILoadOption fobPurchaseSlot = builder.createLoadSlot(id, notionalIPort, tw, OptimiserUnitConvertor.convertToInternalVolume(market.getMinQuantity()),
										OptimiserUnitConvertor.convertToInternalVolume(market.getMaxQuantity()), priceCalculator, cargoCVValue, fobSlot.getSlotOrPortDuration(), true, true, true);

								// Key piece of information
								fobSlot.setMarket(fobPurchaseMarket);
								entities.addModelObject(fobSlot, fobPurchaseSlot);

								for (final IContractTransformer contractTransformer : contractTransformers) {
									contractTransformer.slotTransformed(fobSlot, fobPurchaseSlot);
								}

								marketSlots.add(fobPurchaseSlot);
								marketGroupSlots.add(fobPurchaseSlot);
							}
						}
						if (marketSlots.size() > count) {
							// Take this count and add into a constraint.
							builder.createSlotGroupCount(marketSlots, count);
						}
					}
				}

				// Take group availability curve and add into a constraint.
				if (groupAvailability != null) {
					final int count = getAvailabilityForDate(groupAvailability, startTime);
					if (marketGroupSlots.size() > count) {
						builder.createSlotGroupCount(marketGroupSlots, count);
					}
				}

				startTime = cal.getTime();
			}
		}
	}

	private int getAvailabilityForDate(final SpotAvailability availability, final Date startTime) {
		boolean valueSet = false;
		int count = 0;
		if (availability != null) {
			if (availability.isSetCurve()) {
				final Index<Integer> curve = availability.getCurve();

				final Integer value = curve.getValueForMonth(startTime);
				if (value != null) {
					count = value;
					valueSet = true;
				}

			}
			if (!valueSet && availability.isSetConstant()) {
				count = availability.getConstant();
				valueSet = true;

			}
		}
		return count;
	}

	/**
	 * Convert a date into relative hours; returns the number of hours between windowStart and earliest.
	 * 
	 * @param earliest
	 * @param windowStart
	 * @return number of hours between earliest and windowStart
	 */
	private int convertTime(final Date earliest, final Date windowStart) {
		// I am using two calendars, because the java date objects are all
		// deprecated; however, timezones should not be a problem because
		// every Date in the EMF representation is in UTC.
		final Calendar a = Calendar.getInstance(timezone);
		a.setTime(earliest);
		final Calendar b = Calendar.getInstance(timezone);
		b.setTime(windowStart);
		final long difference = b.getTimeInMillis() - a.getTimeInMillis();
		return (int) (difference / Timer.ONE_HOUR);
	}

	/**
	 * Create the distance matrix for the given builder.
	 * 
	 * @param builder
	 *            the builder we are working with
	 * @param portAssociation
	 *            an association between ports in the EMF model and IPorts in the builder
	 * @param allPorts
	 *            the list of all IPorts constructed so far
	 * @param portIndices
	 *            a reverse-lookup for the ports in allPorts
	 * @param vesselAssociation
	 * @throws IncompleteScenarioException
	 */
	private void buildDistances(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final List<IPort> allPorts, final Map<IPort, Integer> portIndices,
			final Association<VesselClass, IVesselClass> vesselAssociation) throws IncompleteScenarioException {

		/*
		 * Now fill out the distances from the distance model. Firstly we need to create the default distance matrix.
		 */
		final PortModel portModel = rootObject.getSubModel(PortModel.class);
		for (final Route r : portModel.getRoutes()) {
			for (final RouteLine dl : r.getLines()) {
				IPort from, to;
				from = portAssociation.lookup(dl.getFrom());
				to = portAssociation.lookup(dl.getTo());

				final int distance = dl.getDistance();

				builder.setPortToPortDistance(from, to, r.getName(), distance);
			}

			// Set extra time and fuel consumption
			final FleetModel fleetModel = rootObject.getSubModel(FleetModel.class);
			for (final VesselClass evc : fleetModel.getVesselClasses()) {
				for (final VesselClassRouteParameters routeParameters : evc.getRouteParameters()) {
					builder.setVesselClassRouteTransitTime(routeParameters.getRoute().getName(), vesselAssociation.lookup(evc), routeParameters.getExtraTransitTime());

					builder.setVesselClassRouteFuel(routeParameters.getRoute().getName(), vesselAssociation.lookup(evc), VesselState.Laden,
							OptimiserUnitConvertor.convertToInternalHourlyRate(routeParameters.getLadenConsumptionRate()),
							OptimiserUnitConvertor.convertToInternalHourlyRate(routeParameters.getLadenNBORate()));

					builder.setVesselClassRouteFuel(routeParameters.getRoute().getName(), vesselAssociation.lookup(evc), VesselState.Ballast,
							OptimiserUnitConvertor.convertToInternalHourlyRate(routeParameters.getBallastConsumptionRate()),
							OptimiserUnitConvertor.convertToInternalHourlyRate(routeParameters.getBallastNBORate()));

				}
			}

			// set tolls
			final PricingModel pm = rootObject.getSubModel(PricingModel.class);
			for (final RouteCost routeCost : pm.getRouteCosts()) {
				final IVesselClass vesselClass = vesselAssociation.lookup(routeCost.getVesselClass());

				builder.setVesselClassRouteCost(routeCost.getRoute().getName(), vesselClass, VesselState.Laden, OptimiserUnitConvertor.convertToInternalFixedCost(routeCost.getLadenCost()));
				builder.setVesselClassRouteCost(routeCost.getRoute().getName(), vesselClass, VesselState.Ballast, OptimiserUnitConvertor.convertToInternalFixedCost(routeCost.getBallastCost()));
			}
		}
	}

	/**
	 * Construct the fleet model for the scenario
	 * 
	 * @param builder
	 *            a builder which has had its ports and distances instantiated
	 * @param portAssociation
	 *            the Port <-> IPort association to connect EMF Ports with builder IPorts
	 * @param entities
	 * @return
	 */
	private Pair<Association<VesselClass, IVesselClass>, Association<Vessel, IVessel>> buildFleet(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation,
			final ModelEntityMap entities) {

		/*
		 * Build the fleet model - first we must create the vessel classes from the model
		 */
		final Association<VesselClass, IVesselClass> vesselClassAssociation = new Association<VesselClass, IVesselClass>();
		final Association<Vessel, IVessel> vesselAssociation = new Association<Vessel, IVessel>();
		// TODO: Check that we are mutliplying/dividing correctly to maintain
		// precision

		final FleetModel fleetModel = rootObject.getSubModel(FleetModel.class);
		final PricingModel pricingModel = rootObject.getSubModel(PricingModel.class);

		// look up prices

		for (final VesselClass eVc : fleetModel.getVesselClasses()) {
			int baseFuelPrice = 0;
			for (final BaseFuelCost baseFuelCost : pricingModel.getFleetCost().getBaseFuelPrices()) {
				if (baseFuelCost.getFuel() == eVc.getBaseFuel()) {
					baseFuelPrice = OptimiserUnitConvertor.convertToInternalPrice(baseFuelCost.getPrice());
					break;
				}
			}

			final IVesselClass vc = builder.createVesselClass(eVc.getName(), OptimiserUnitConvertor.convertToInternalSpeed(eVc.getMinSpeed()),
					OptimiserUnitConvertor.convertToInternalSpeed(eVc.getMaxSpeed()), OptimiserUnitConvertor.convertToInternalVolume((int) (eVc.getFillCapacity() * (double) eVc.getCapacity())),
					OptimiserUnitConvertor.convertToInternalVolume(eVc.getMinHeel()), baseFuelPrice,
					OptimiserUnitConvertor.convertToInternalConversionFactor(eVc.getBaseFuel().getEquivalenceFactor()), OptimiserUnitConvertor.convertToInternalHourlyRate(eVc.getPilotLightRate()),
					eVc.getWarmingTime(), eVc.getCoolingTime(), OptimiserUnitConvertor.convertToInternalVolume(eVc.getCoolingVolume()));

			vesselClassAssociation.add(eVc, vc);

			/*
			 * Set state-specific attributes
			 */
			buildVesselStateAttributes(builder, vc, com.mmxlabs.scheduler.optimiser.components.VesselState.Laden, eVc.getLadenAttributes());
			buildVesselStateAttributes(builder, vc, com.mmxlabs.scheduler.optimiser.components.VesselState.Ballast, eVc.getBallastAttributes());

			/*
			 * set up inaccessible ports by applying resource allocation constraints
			 */
			final Set<IPort> inaccessiblePorts = new HashSet<IPort>();
			for (final APort ePort : SetUtils.getPorts(eVc.getInaccessiblePorts())) {
				inaccessiblePorts.add(portAssociation.lookup((Port) ePort));
			}

			if (inaccessiblePorts.isEmpty() == false) {
				builder.setVesselClassInaccessiblePorts(vc, inaccessiblePorts);
			}

			entities.addModelObject(eVc, vc);
		}

		/*
		 * Now create each vessel
		 */
		for (final Vessel eV : fleetModel.getVessels()) {
			final IStartEndRequirement startRequirement = createRequirement(builder, portAssociation, eV.getAvailability().isSetStartAfter() ? eV.getAvailability().getStartAfter() : null, eV
					.getAvailability().isSetStartBy() ? eV.getAvailability().getStartBy() : null, SetUtils.getPorts(eV.getAvailability().getStartAt()));

			final IStartEndRequirement endRequirement = createRequirement(builder, portAssociation, eV.getAvailability().isSetEndAfter() ? eV.getAvailability().getEndAfter() : null, eV
					.getAvailability().isSetEndBy() ? eV.getAvailability().getEndBy() : null, SetUtils.getPorts(eV.getAvailability().getEndAt()));

			// TODO: Hook up once charter out opt implemented
			final int dailyCharterInPrice = eV.isSetTimeCharterRate() ? eV.getTimeCharterRate() : 0;// vesselAssociation.lookup(eV).getHourlyCharterInPrice() * 24;

			final long heelLimit = eV.getStartHeel().isSetVolumeAvailable() ? OptimiserUnitConvertor.convertToInternalVolume(eV.getStartHeel().getVolumeAvailable()) : 0;

			final int hourlyCharterInRate = (int) OptimiserUnitConvertor.convertToInternalHourlyCost(dailyCharterInPrice);
			final ICurve hourlyCharterInCurve = new ConstantValueCurve(hourlyCharterInRate);

			final IVessel vessel = builder.createVessel(eV.getName(), vesselClassAssociation.lookup(eV.getVesselClass()), hourlyCharterInCurve,

			eV.isSetTimeCharterRate() ? VesselInstanceType.TIME_CHARTER : VesselInstanceType.FLEET, startRequirement, endRequirement, heelLimit,
					OptimiserUnitConvertor.convertToInternalConversionFactor(eV.getStartHeel().getCvValue()), OptimiserUnitConvertor.convertToInternalPrice(eV.getStartHeel().getPricePerMMBTU()));
			vesselAssociation.add(eV, vessel);

			entities.addModelObject(eV, vessel);
			allVessels.add(vessel);
		}

		{
			int charterCount = 0;
			for (final CharterCostModel charterCost : pricingModel.getFleetCost().getCharterCosts()) {

				for (final VesselClass eVc : charterCost.getVesselClasses()) {
					final ICurve charterInCurve;
					if (charterCost.getCharterInPrice() == null) {
						charterInCurve = new ConstantValueCurve(0);
					} else {
						charterInCurve = createCurveForIntegerIndex(charterCost.getCharterInPrice(), 1.0f / 24.0f);
					}

					charterCount = charterCost.getSpotCharterCount();
					if (charterCount > 0) {
						final List<IVessel> spots = builder.createSpotVessels("SPOT-" + eVc.getName(), vesselClassAssociation.lookup(eVc), charterCount, charterInCurve);
						spotVesselsByClass.put(eVc, spots);
						allVessels.addAll(spots);
					}

					if (charterCost.getCharterOutPrice() != null) {
						final ICurve charterOutCurve = createCurveForIntegerIndex(charterCost.getCharterOutPrice(), 1.0f / 24.0f);
						final int minDuration = 24 * charterCost.getMinCharterOutDuration();
						builder.createCharterOutCurve(vesselClassAssociation.lookup(eVc), charterOutCurve, minDuration);
					}
				}
			}

		}
		//
		// /*
		// * Create spot charter vessels with no start/end requirements
		// */
		// for (final VesselClass eVc : fleetModel.getVesselClasses()) {
		// if (eVc.getSpotCharterCount() > 0) {
		// final List<IVessel> spots = builder.createSpotVessels("SPOT-" + eVc.getName(), vesselClassAssociation.lookup(eVc), eVc.getSpotCharterCount());
		// // TODO this is not necessarily ideal; if there is an initial
		// // solution set we associate all the spot vessels with ones in
		// // that solution.
		// int vesselIndex = 0;
		// if ((scenario.getOptimisation() != null) && (scenario.getOptimisation().getCurrentSettings() != null)) {
		// final Schedule initialSchedule = scenario.getOptimisation().getCurrentSettings().getInitialSchedule();
		// if (initialSchedule != null) {
		// for (final AllocatedVessel allocatedVessel : initialSchedule.getFleet()) {
		// if ((allocatedVessel instanceof SpotVessel) && (((SpotVessel) allocatedVessel).getVesselClass() == eVc)) {
		// // map it to one of the ones we have just made
		// assert vesselIndex < spots.size() : "Initial schedule should not have more spot vessels than fleet suggests";
		// entities.addModelObject(allocatedVessel, spots.get(vesselIndex));
		// vesselIndex++;
		// }
		// }
		// }
		// }
		// }
		// }

		return new Pair<Association<VesselClass, IVesselClass>, Association<Vessel, IVessel>>(vesselClassAssociation, vesselAssociation);
	}

	/**
	 * Convert a PortAndTime from the EMF to an IStartEndRequirement for internal use; may be subject to change later.
	 * 
	 * @param builder
	 * @param portAssociation
	 * @param pat
	 * @return
	 */
	private IStartEndRequirement createRequirement(final ISchedulerBuilder builder, final Association<Port, IPort> portAssociation, final Date from, final Date to, final Set<APort> ports) {
		ITimeWindow window = null;

		if (from == null && to != null) {
			window = builder.createTimeWindow(convertTime(earliestTime), convertTime(to));
		} else if (from != null && to == null) {
			window = builder.createTimeWindow(convertTime(from), convertTime(latestTime));
		} else if (from != null && to != null) {
			window = builder.createTimeWindow(convertTime(from), convertTime(to));
		}

		if (ports.isEmpty()) {
			if (window != null) {
				return builder.createStartEndRequirement(window);
			}
		} else {
			if (ports.size() > 1) {
				final Set<IPort> portSet = new HashSet<IPort>();
				for (final APort p : ports) {
					portSet.add(portAssociation.lookup((Port) p));
				}
				if (window == null) {
					return builder.createStartEndRequirement(portSet);
				} else {
					return builder.createStartEndRequirement(portSet, window);
				}
			} else {
				final IPort port = portAssociation.lookup((Port) ports.iterator().next());
				if (window == null) {
					return builder.createStartEndRequirement(port);
				} else {
					return builder.createStartEndRequirement(port, window);
				}
			}
		}

		return builder.createStartEndRequirement();
	}

	private int convertTime(final Date startTime) {
		return convertTime(earliestTime, startTime);
	}

	/**
	 * Tell the builder to set up the given vessel state from the EMF fleet model
	 * 
	 * @param builder
	 *            the builder which is currently in use
	 * @param vc
	 *            the {@link IVesselClass} which the builder has constructed whose attributes we are setting
	 * @param laden
	 *            the {@link com.mmxlabs.scheduler.optimiser.components.VesselState} we are setting attributes for
	 * @param ladenAttributes
	 *            the {@link VesselStateAttributes} from the EMF model
	 */
	private void buildVesselStateAttributes(final ISchedulerBuilder builder, final IVesselClass vc, final com.mmxlabs.scheduler.optimiser.components.VesselState state,
			final VesselStateAttributes attrs) {

		// create consumption rate calculator for the curve
		final TreeMap<Integer, Long> keypoints = new TreeMap<Integer, Long>();

		for (final FuelConsumption line : attrs.getFuelConsumption()) {
			keypoints.put(OptimiserUnitConvertor.convertToInternalSpeed(line.getSpeed()), (long) OptimiserUnitConvertor.convertToInternalHourlyRate(line.getConsumption()));
		}

		final InterpolatingConsumptionRateCalculator consumptionCalculator = new InterpolatingConsumptionRateCalculator(keypoints);

		final LookupTableConsumptionRateCalculator cc = new LookupTableConsumptionRateCalculator(vc.getMinSpeed(), vc.getMaxSpeed(), consumptionCalculator);

		builder.setVesselClassStateParamaters(vc, state, OptimiserUnitConvertor.convertToInternalHourlyRate(attrs.getNboRate()),
				OptimiserUnitConvertor.convertToInternalHourlyRate(attrs.getIdleNBORate()), OptimiserUnitConvertor.convertToInternalHourlyRate(attrs.getIdleBaseRate()),
				OptimiserUnitConvertor.convertToInternalHourlyRate(attrs.getInPortBaseRate()), cc);
	}

	/**
	 * Utility method for getting the current optimisation settings from this scenario. TODO maybe put this in another file/model somewhere else.
	 * 
	 * @return
	 */
	public OptimiserSettings getOptimisationSettings() {
		final OptimiserModel om = rootObject.getSubModel(OptimiserModel.class);
		if (om != null) {
			// select settings
			final OptimiserSettings x = om.getActiveSetting();
			if (x != null)
				return x;
		}
		if (defaultSettings == null) {
			defaultSettings = ScenarioUtils.createDefaultSettings();
			if (om != null) {
				om.getSettings().add(defaultSettings);
				om.setActiveSetting(defaultSettings);
			}
		}
		return defaultSettings;
	}

	/**
	 * Add the spot slot to the existing market spot slot counter.
	 * 
	 * @param spotSlot
	 */
	private void addSpotSlotToCount(final SpotSlot spotSlot) {
		final ASpotMarket market = spotSlot.getMarket();
		final SpotType spotType;
		if (market instanceof DESPurchaseMarket) {
			spotType = SpotType.DES_PURCHASE;
		} else if (market instanceof DESSalesMarket) {
			spotType = SpotType.DES_SALE;
		} else if (market instanceof FOBPurchasesMarket) {
			spotType = SpotType.FOB_PURCHASE;
		} else if (market instanceof FOBSalesMarket) {
			spotType = SpotType.FOB_SALE;
		} else {
			log.warn("Spot slot with an invalid market found");
			return;
		}

		final TreeMap<String, Collection<Slot>> curve;
		if (existingSpotCount.containsKey(spotType)) {
			curve = existingSpotCount.get(spotType);
		} else {
			curve = new TreeMap<String, Collection<Slot>>();
			existingSpotCount.put(spotType, curve);
		}
		if (spotSlot instanceof Slot) {
			final Slot slot = (Slot) spotSlot;
			final String key = getKeyForDate(slot.getWindowStart());
			final Collection<Slot> slots;
			if (curve.containsKey(key)) {
				slots = curve.get(key);
			} else {
				slots = new LinkedList<Slot>();
				curve.put(key, slots);
			}
			slots.add(slot);
		}
	}

	private Collection<Slot> getSpotSlots(final SpotType spotType, final String key) {
		if (existingSpotCount.containsKey(spotType)) {
			final TreeMap<String, Collection<Slot>> curve = existingSpotCount.get(spotType);
			if (curve.containsKey(key)) {
				final Collection<Slot> slots = curve.get(key);
				if (slots != null) {
					return slots;
				}
			}
		}
		return Collections.emptyList();
	}

	private String getKeyForDate(final Date date) {
		final String key = new SimpleDateFormat("yyyy-MM").format(date);
		return key;
	}
}

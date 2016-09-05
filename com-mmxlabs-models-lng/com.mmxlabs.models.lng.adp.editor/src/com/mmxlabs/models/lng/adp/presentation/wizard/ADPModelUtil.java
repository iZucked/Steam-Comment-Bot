package com.mmxlabs.models.lng.adp.presentation.wizard;

import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.BindingRule;
import com.mmxlabs.models.lng.adp.FlowType;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.SupplyFromProfileFlow;
import com.mmxlabs.models.lng.adp.SupplyFromSpotFlow;
import com.mmxlabs.models.lng.adp.ext.IADPBindingRuleProvider;
import com.mmxlabs.models.lng.adp.ext.IADPProfileProvider;
import com.mmxlabs.models.lng.adp.ext.IProfileGenerator;
import com.mmxlabs.models.lng.adp.ext.impl.DistributionModelGeneratorUtil;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.scenario.model.LNGReferenceModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;

public class ADPModelUtil {

	public static LNGScenarioModel prepareModel(final LNGScenarioModel scenarioModel) {
		final LNGScenarioModel copy = LNGScenarioFactory.eINSTANCE.createLNGScenarioModel();

		final EcoreUtil.Copier copier = new EcoreUtil.Copier();
		final LNGReferenceModel referenceModel = (LNGReferenceModel) copier.copy(ScenarioModelUtil.findReferenceModel(scenarioModel));
		copy.setReferenceModel(referenceModel);

		copy.setCargoModel(CargoFactory.eINSTANCE.createCargoModel());

		copy.getCargoModel().getVesselAvailabilities().addAll(copier.copyAll(scenarioModel.getCargoModel().getVesselAvailabilities()));

		copy.setScheduleModel(ScheduleFactory.eINSTANCE.createScheduleModel());
		// Copy existing model?
		// for (EObject eObject : scenarioModel.getExtensions()) {
		// if (eObject instanceof ADPModel) {
		// ADPModel adpModel = (ADPModel) eObject;
		// copy.getExtensions().add(copier.copy(adpModel));
		// break;
		// }
		// }

		// TODO: Run post process hooks e.g. create custom model

		copier.copyReferences();

		return copy;
	}

	public static ADPModel createADPModel(final LNGScenarioModel scenarioModel) {
		final ADPModel adpModel = ADPFactory.eINSTANCE.createADPModel();

		// Set default parameters
		final YearMonth startOfGasYear;
		if (YearMonth.now().getMonthValue() <= Month.OCTOBER.getValue()) {
			startOfGasYear = YearMonth.now().withMonth(Month.OCTOBER.getValue());
		} else {
			startOfGasYear = YearMonth.now().withMonth(Month.OCTOBER.getValue()).plusYears(1);
		}
		adpModel.setYearStart(startOfGasYear);
		// TODO: Other Params;

		final Bundle bundle = FrameworkUtil.getBundle(ADPModelUtil.class);
		final BundleContext bundleContext = bundle.getBundleContext();
		final ServiceReference<IADPProfileProvider> serviceReference = bundleContext.getServiceReference(IADPProfileProvider.class);

		final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(scenarioModel);
		try {
			final IADPProfileProvider profileProvider = bundleContext.getService(serviceReference);
			profileProvider.createProfiles(commercialModel, adpModel);

		} finally {
			bundleContext.ungetService(serviceReference);
		}
		return adpModel;
	}

	public static void populateModel(final LNGScenarioModel scenarioModel, final ADPModel adpModel) {
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);

		final Bundle bundle = FrameworkUtil.getBundle(ADPModelUtil.class);
		final BundleContext bundleContext = bundle.getBundleContext();
		Collection<ServiceReference<IProfileGenerator>> serviceReferences;
		try {
			serviceReferences = bundleContext.getServiceReferences(IProfileGenerator.class, null);

			final List<IProfileGenerator> generators = new LinkedList<>();

			for (final ServiceReference<IProfileGenerator> ref : serviceReferences) {
				generators.add(bundleContext.getService(ref));
			}
			try {
				for (final PurchaseContractProfile profile : adpModel.getPurchaseContractProfiles()) {
					if (profile.isEnabled()) {
						for (final SubContractProfile<LoadSlot> subProfile : profile.getSubProfiles()) {
							for (final IProfileGenerator g : generators) {
								if (g.canGenerate(profile, subProfile)) {
									final List<LoadSlot> slots = DistributionModelGeneratorUtil.generateProfile(factory -> g.generateSlots(adpModel, profile, subProfile, factory));
									subProfile.getSlots().addAll(slots);
									cargoModel.getLoadSlots().addAll(slots);

									break;
								}
							}
						}
					}
				}
				for (final SalesContractProfile profile : adpModel.getSalesContractProfiles()) {
					if (profile.isEnabled()) {

						for (final SubContractProfile<DischargeSlot> subProfile : profile.getSubProfiles()) {
							for (final IProfileGenerator g : generators) {
								if (g.canGenerate(profile, subProfile)) {
									final List<DischargeSlot> slots = DistributionModelGeneratorUtil.generateProfile(factory -> g.generateSlots(adpModel, profile, subProfile, factory));
									subProfile.getSlots().addAll(slots);
									cargoModel.getDischargeSlots().addAll(slots);

									break;
								}
							}
						}
					}
				}
			} finally {
				for (final ServiceReference<IProfileGenerator> ref : serviceReferences) {
					bundleContext.ungetService(ref);
				}
			}
		} catch (final InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ComposedAdapterFactory createAdapterFactory() {
		final List<AdapterFactory> factories = new ArrayList<>();
		factories.add(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
		factories.add(new ReflectiveItemProviderAdapterFactory());
		return new ComposedAdapterFactory(factories);
	}

	public static void createBindings(final LNGScenarioModel scenarioModel, final ADPModel adpModel) {

		final Bundle bundle = FrameworkUtil.getBundle(ADPModelUtil.class);
		final BundleContext bundleContext = bundle.getBundleContext();
		final ServiceReference<IADPBindingRuleProvider> serviceReference = bundleContext.getServiceReference(IADPBindingRuleProvider.class);
		if (serviceReference == null) {
			return;
		}
		try {
			final IADPBindingRuleProvider provider = bundleContext.getService(serviceReference);
			provider.generateBindingRules(adpModel);
		} finally {
			bundleContext.ungetService(serviceReference);
		}
	}

	public static void makeBindings(final LNGScenarioModel scenarioModel, final ADPModel adpModel) {

		final Set<Slot> usedSlots = new HashSet<>();
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);

		for (final BindingRule rule : adpModel.getBindingRules()) {
			final FlowType flowType = rule.getFlowType();

			if (flowType instanceof SupplyFromProfileFlow) {
				final SupplyFromProfileFlow flow = (SupplyFromProfileFlow) flowType;

				final Iterator<DischargeSlot> dischargeItr = new SeenCargoIterator<>((Iterator<DischargeSlot>) rule.getSubProfile().getSlots().iterator(), usedSlots);
				while (dischargeItr.hasNext()) {
					final DischargeSlot discharge = dischargeItr.next();

					LoadSlot bestOption = null;
					final Iterator<LoadSlot> loadItr = new SeenCargoIterator<>(flow.getSubProfile().getSlots().iterator(), usedSlots);
					while (loadItr.hasNext()) {
						final LoadSlot load = loadItr.next();
						if (load == null) {
							continue;
						}

						// Simple year/month match
						if (load.getWindowStart().getYear() != discharge.getWindowStart().getYear()) {
							continue;
						}
						if (load.getWindowStart().getMonthValue() != discharge.getWindowStart().getMonthValue()) {
							continue;
						}

						if (bestOption == null) {
							bestOption = load;
						} else {
							// TODO: Find better option
							break;
						}
					}

					createCargo(usedSlots, cargoModel, rule, discharge, bestOption);
				}
			}
			// if (flowType instanceof SupplyFromSpotFlow) {
			// final SupplyFromSpotFlow flow = (SupplyFromSpotFlow) flowType;
			//
			// final Iterator<DischargeSlot> dischargeItr = new SeenCargoIterator<>((Iterator<DischargeSlot>) rule.getSubProfile().getSlots().iterator(), usedSlots);
			// while (dischargeItr.hasNext()) {
			// final DischargeSlot discharge = dischargeItr.next();
			//
			// LoadSlot bestOption = null;
			// final Iterator<LoadSlot> loadItr = new SeenCargoIterator<>(flow.getSubProfile().getSlots().iterator(), usedSlots);
			// while (loadItr.hasNext()) {
			// final LoadSlot load = loadItr.next();
			// if (load == null) {
			// continue;
			// }
			//
			// // Simple year/month match
			// if (load.getWindowStart().getYear() != discharge.getWindowStart().getYear()) {
			// continue;
			// }
			// if (load.getWindowStart().getMonthValue() != discharge.getWindowStart().getMonthValue()) {
			// continue;
			// }
			//
			// if (bestOption == null) {
			// bestOption = load;
			// } else {
			// // TODO: Find better option
			// break;
			// }
			// }
			//
			// createCargo(usedSlots, cargoModel, rule, discharge, bestOption);
			// }
			// }
		}

	}

	private static void createCargo(final Set<Slot> usedSlots, final CargoModel cargoModel, final BindingRule rule, final DischargeSlot discharge, LoadSlot bestOption) {
		if (bestOption != null) {
			final Cargo cargo = CargoFactory.eINSTANCE.createCargo();
			cargo.getSlots().add(bestOption);
			cargo.getSlots().add(discharge);

			if (bestOption.isDESPurchase()) {
				if (rule.getShippingOption().getVessel() != null) {
					bestOption.setNominatedVessel(rule.getShippingOption().getVessel());
				}
			}
			if (discharge.isFOBSale()) {
				if (rule.getShippingOption().getVessel() != null) {
					discharge.setNominatedVessel(rule.getShippingOption().getVessel());
				}
			}

			if (rule.getShippingOption().getVesselAssignmentType() != null) {
				cargo.setVesselAssignmentType(rule.getShippingOption().getVesselAssignmentType());
				cargo.setSpotIndex(rule.getShippingOption().getSpotIndex());
			}

			cargoModel.getCargoes().add(cargo);

			usedSlots.add(bestOption);
			usedSlots.add(discharge);
		}
	}

	private static class SeenCargoIterator<T extends Slot> implements Iterator<T> {

		Iterator<T> itr;

		Set<Slot> seenSlots;

		public SeenCargoIterator(final Iterator<T> itr, final Set<Slot> seenSlots) {
			this.itr = itr;
			this.seenSlots = seenSlots;

		}

		@Override
		public boolean hasNext() {
			return itr.hasNext();
		}

		@Override
		public T next() {

			T s = itr.next();
			while (s != null && seenSlots.contains(s)) {
				if (itr.hasNext()) {
					s = itr.next();
				} else {
					s = (T) null;
				}
			}

			return s;
		}

	}
}

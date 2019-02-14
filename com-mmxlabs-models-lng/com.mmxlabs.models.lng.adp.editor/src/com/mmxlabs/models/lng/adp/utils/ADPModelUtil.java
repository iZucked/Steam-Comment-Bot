/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.utils;

import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.FleetProfile;
import com.mmxlabs.models.lng.adp.LNGVolumeUnit;
import com.mmxlabs.models.lng.adp.ProfileVesselRestriction;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.SubProfileConstraint;
import com.mmxlabs.models.lng.adp.ext.IADPProfileProvider;
import com.mmxlabs.models.lng.adp.ext.IProfileGenerator;
import com.mmxlabs.models.lng.adp.ext.impl.DistributionModelGeneratorUtil;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.VolumeUnits;

public class ADPModelUtil {

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
		adpModel.setYearEnd(startOfGasYear.plusYears(1));
		// TODO: Other Params;
		final FleetProfile fleetProfile = ADPFactory.eINSTANCE.createFleetProfile();
		adpModel.setFleetProfile(fleetProfile);

		final Bundle bundle = FrameworkUtil.getBundle(ADPModelUtil.class);
		final BundleContext bundleContext = bundle.getBundleContext();
		final ServiceReference<IADPProfileProvider> serviceReference = bundleContext.getServiceReference(IADPProfileProvider.class);

		final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(scenarioModel);
		try {
			final IADPProfileProvider profileProvider = bundleContext.getService(serviceReference);
			profileProvider.createProfiles(scenarioModel, commercialModel, adpModel);
		} finally {
			bundleContext.ungetService(serviceReference);
		}
		return adpModel;
	}

	/**
	 * Generate default profiles, without EMF command
	 * 
	 * @param scenarioModel
	 * @param adpModel
	 */
	public static void createDefaultProfiles(LNGScenarioModel scenarioModel, ADPModel adpModel) {
		final Bundle bundle = FrameworkUtil.getBundle(ADPModelUtil.class);
		final BundleContext bundleContext = bundle.getBundleContext();
		final ServiceReference<IADPProfileProvider> serviceReference = bundleContext.getServiceReference(IADPProfileProvider.class);

		final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(scenarioModel);
		try {
			final IADPProfileProvider profileProvider = bundleContext.getService(serviceReference);
			profileProvider.createProfiles(scenarioModel, commercialModel, adpModel);
		} finally {
			bundleContext.ungetService(serviceReference);
		}
	}

	public static @Nullable Command populateModel(final EditingDomain editingDomain, final LNGScenarioModel scenarioModel, final ADPModel adpModel, final PurchaseContractProfile profile) {
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);

		final CompoundCommand cmd = new CompoundCommand("Generate ADP slots");

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
				if (profile.isEnabled()) {
					List<Slot> slotsToRemove = new LinkedList<>();
					List<Cargo> cargoesToRemove = new LinkedList<>();
					for (LoadSlot slot : cargoModel.getLoadSlots()) {
						if (slot.getContract() == profile.getContract()) {
							slotsToRemove.add(slot);
							if (slot.getCargo() != null) {
								cargoesToRemove.add(slot.getCargo());
								for (Slot s : slot.getCargo().getSlots()) {
									if (s instanceof SpotSlot) {
										slotsToRemove.add(s);
									}
								}
							}
						}
					}
					if (!slotsToRemove.isEmpty()) {
						cmd.append(DeleteCommand.create(editingDomain, slotsToRemove));
					}
					if (!cargoesToRemove.isEmpty()) {
						cmd.append(DeleteCommand.create(editingDomain, cargoesToRemove));
					}

					for (final SubContractProfile<LoadSlot, PurchaseContract> subProfile : profile.getSubProfiles()) {
						for (final IProfileGenerator g : generators) {
							if (g.canGenerate(profile, subProfile)) {
								final List<LoadSlot> slots = DistributionModelGeneratorUtil.generateProfile(factory -> g.generateSlots(adpModel, profile, subProfile, factory));
								for (SubProfileConstraint subProfileConstraint : subProfile.getConstraints()) {
									if (subProfileConstraint instanceof ProfileVesselRestriction) {
										slots.stream().forEach(s -> {
											s.getAllowedVessels().clear();
											s.getAllowedVessels().addAll(((ProfileVesselRestriction) subProfileConstraint).getVessels());
										});
									}
								}
								if (!slots.isEmpty()) {
									cmd.append(AddCommand.create(editingDomain, cargoModel, CargoPackage.Literals.CARGO_MODEL__LOAD_SLOTS, slots));
								}
								break;
							}
						}
					}
				}
			} finally {
				for (final ServiceReference<IProfileGenerator> ref : serviceReferences) {
					bundleContext.ungetService(ref);
				}
			}
			if (cmd.isEmpty()) {
				return IdentityCommand.INSTANCE;
			}
			return cmd;
		} catch (final InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Command populateModel(final EditingDomain editingDomain, final LNGScenarioModel scenarioModel, final ADPModel adpModel, final SalesContractProfile profile) {
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);
		final CompoundCommand cmd = new CompoundCommand("Generate ADP slots");

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
				if (profile.isEnabled()) {
					List<Slot> slotsToRemove = new LinkedList<>();
					List<Cargo> cargoesToRemove = new LinkedList<>();
					for (DischargeSlot slot : cargoModel.getDischargeSlots()) {
						if (slot.getContract() == profile.getContract()) {
							slotsToRemove.add(slot);
							if (slot.getCargo() != null) {
								cargoesToRemove.add(slot.getCargo());
								for (Slot s : slot.getCargo().getSlots()) {
									if (s instanceof SpotSlot) {
										slotsToRemove.add(s);
									}
								}
							}
						}
					}
					if (!slotsToRemove.isEmpty()) {
						cmd.append(DeleteCommand.create(editingDomain, slotsToRemove));
					}
					if (!cargoesToRemove.isEmpty()) {
						cmd.append(DeleteCommand.create(editingDomain, cargoesToRemove));
					}
					for (final SubContractProfile<DischargeSlot, SalesContract> subProfile : profile.getSubProfiles()) {
						for (final IProfileGenerator g : generators) {
							if (g.canGenerate(profile, subProfile)) {
								final List<DischargeSlot> slots = DistributionModelGeneratorUtil.generateProfile(factory -> g.generateSlots(adpModel, profile, subProfile, factory));
								for (SubProfileConstraint subProfileConstraint : subProfile.getConstraints()) {
									if (subProfileConstraint instanceof ProfileVesselRestriction) {
										slots.stream().forEach(s -> {
											s.getAllowedVessels().clear();
											s.getAllowedVessels().addAll(((ProfileVesselRestriction) subProfileConstraint).getVessels());
										});
									}
								}
								if (!slots.isEmpty()) {
									cmd.append(AddCommand.create(editingDomain, cargoModel, CargoPackage.Literals.CARGO_MODEL__DISCHARGE_SLOTS, slots));
								}
								break;
							}
						}
					}
				}
			} finally {
				for (final ServiceReference<IProfileGenerator> ref : serviceReferences) {
					bundleContext.ungetService(ref);
				}
			}
			if (cmd.isEmpty()) {
				return IdentityCommand.INSTANCE;
			}
			return cmd;
		} catch (final InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static ComposedAdapterFactory createAdapterFactory() {
		final List<AdapterFactory> factories = new ArrayList<>();
		factories.add(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
		factories.add(new ReflectiveItemProviderAdapterFactory());
		return new ComposedAdapterFactory(factories);
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

	public static @Nullable PurchaseContractProfile createProfile(final LNGScenarioModel scenarioModel, final ADPModel adpModel, final PurchaseContract contract) {
		final Bundle bundle = FrameworkUtil.getBundle(ADPModelUtil.class);
		final BundleContext bundleContext = bundle.getBundleContext();
		final ServiceReference<IADPProfileProvider> serviceReference = bundleContext.getServiceReference(IADPProfileProvider.class);

		final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(scenarioModel);
		try {
			final IADPProfileProvider profileProvider = bundleContext.getService(serviceReference);
			return profileProvider.createProfile(scenarioModel, commercialModel, adpModel, contract);

		} finally {
			bundleContext.ungetService(serviceReference);
		}
	}

	public static @Nullable SalesContractProfile createProfile(final LNGScenarioModel scenarioModel, final ADPModel adpModel, final SalesContract contract) {
		final Bundle bundle = FrameworkUtil.getBundle(ADPModelUtil.class);
		final BundleContext bundleContext = bundle.getBundleContext();
		final ServiceReference<IADPProfileProvider> serviceReference = bundleContext.getServiceReference(IADPProfileProvider.class);

		final CommercialModel commercialModel = ScenarioModelUtil.getCommercialModel(scenarioModel);
		try {
			final IADPProfileProvider profileProvider = bundleContext.getService(serviceReference);
			return profileProvider.createProfile(scenarioModel, commercialModel, adpModel, contract);

		} finally {
			bundleContext.ungetService(serviceReference);
		}
	}

	public static int convertMTtoM3(double volumeInMT) {
		int volumeInM3 = (int) Math.round(volumeInMT * 1380.0 / 600.0 * 1_000_000.0);
		return volumeInM3;
	}

	public static void setSlotVolumeFrom(double volume, LNGVolumeUnit volumeUnit, Slot slot) {
		switch (volumeUnit) {
		case M3:
			slot.setVolumeLimitsUnit(VolumeUnits.M3);
			slot.setMinQuantity((int) Math.round(0));
			slot.setMaxQuantity((int) Math.round(volume));
			break;
		case MMBTU:
			slot.setVolumeLimitsUnit(VolumeUnits.MMBTU);
			slot.setMinQuantity((int) Math.round(0));
			slot.setMaxQuantity((int) Math.round(volume));
			break;
		case MT:
			slot.setVolumeLimitsUnit(VolumeUnits.M3);
			// Rough MT to m3 conversion
			int volumeInM3 = convertMTtoM3(volume);
			slot.setMinQuantity(0);
			slot.setMaxQuantity(volumeInM3);
			break;
		default:
			throw new IllegalArgumentException();

		}
	}

	public static void generateModelSlots(@NonNull LNGScenarioModel scenarioModel, @NonNull ADPModel adpModel) {
		for (PurchaseContractProfile profile : adpModel.getPurchaseContractProfiles()) {
			populateModel(scenarioModel, adpModel, profile);
		}
		for (SalesContractProfile profile : adpModel.getSalesContractProfiles()) {
			populateModel(scenarioModel, adpModel, profile);
		}

	}

	public static void populateModel(final LNGScenarioModel scenarioModel, final ADPModel adpModel, final PurchaseContractProfile profile) {
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
				if (profile.isEnabled()) {
					for (final SubContractProfile<LoadSlot, PurchaseContract> subProfile : profile.getSubProfiles()) {
						for (final IProfileGenerator g : generators) {
							if (g.canGenerate(profile, subProfile)) {
								final List<LoadSlot> slots = DistributionModelGeneratorUtil.generateProfile(factory -> g.generateSlots(adpModel, profile, subProfile, factory));
								for (SubProfileConstraint subProfileConstraint : subProfile.getConstraints()) {
									if (subProfileConstraint instanceof ProfileVesselRestriction) {
										slots.stream().forEach(s -> {
											s.getAllowedVessels().clear();
											s.getAllowedVessels().addAll(((ProfileVesselRestriction) subProfileConstraint).getVessels());
										});
									}
								}
								cargoModel.getLoadSlots().addAll(slots);
								break;
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
			e.printStackTrace();
		}
	}

	public static void populateModel(final LNGScenarioModel scenarioModel, final ADPModel adpModel, final SalesContractProfile profile) {
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
				if (profile.isEnabled()) {
					for (final SubContractProfile<DischargeSlot, SalesContract> subProfile : profile.getSubProfiles()) {
						for (final IProfileGenerator g : generators) {
							if (g.canGenerate(profile, subProfile)) {
								final List<DischargeSlot> slots = DistributionModelGeneratorUtil.generateProfile(factory -> g.generateSlots(adpModel, profile, subProfile, factory));
								for (SubProfileConstraint subProfileConstraint : subProfile.getConstraints()) {
									if (subProfileConstraint instanceof ProfileVesselRestriction) {
										slots.stream().forEach(s -> {
											s.getAllowedVessels().clear();
											s.getAllowedVessels().addAll(((ProfileVesselRestriction) subProfileConstraint).getVessels());
										});
									}
								}
								cargoModel.getDischargeSlots().addAll(slots);
								break;
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
			e.printStackTrace();
		}
	}

}

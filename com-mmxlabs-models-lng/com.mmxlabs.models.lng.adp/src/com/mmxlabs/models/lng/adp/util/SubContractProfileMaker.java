/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.util;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.CargoByQuarterDistributionModel;
import com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel;
import com.mmxlabs.models.lng.adp.CargoNumberDistributionModel;
import com.mmxlabs.models.lng.adp.CargoSizeDistributionModel;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.IntervalType;
import com.mmxlabs.models.lng.adp.LNGVolumeUnit;
import com.mmxlabs.models.lng.adp.PreDefinedDate;
import com.mmxlabs.models.lng.adp.PreDefinedDistributionModel;
import com.mmxlabs.models.lng.adp.ProfileVesselRestriction;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.TimePeriod;

public class SubContractProfileMaker<T extends AbstractContractProfileMaker<T, U, V>, U extends ContractProfile<V>, V extends Slot> {

	protected final @NonNull ADPModelBuilder adpModelBuilder;

	protected final @NonNull U contractProfile;

	private final @NonNull SubContractProfile<V> subContractProfile;

	private final T parent;

	public SubContractProfileMaker(final @NonNull ADPModelBuilder adpModelBuilder, final @NonNull U contractProfile, @NonNull final T parent) {
		this.adpModelBuilder = adpModelBuilder;
		this.contractProfile = contractProfile;
		this.parent = parent;
		this.subContractProfile = ADPFactory.eINSTANCE.createSubContractProfile();
	}

	@NonNull
	public T build() {
		contractProfile.getSubProfiles().add(subContractProfile);
		return parent;
	}

	@NonNull
	public SubContractProfileMaker<T, U, V> withName(final String name) {
		subContractProfile.setName(name);
		return this;
	}

	@NonNull
	public SubContractProfileMaker<T, U, V> withContractType(final ContractType contractType) {
		subContractProfile.setContractType(contractType);
		return this;
	}

	@NonNull
	public SubContractProfileMaker<T, U, V> withSlotTemplate(final String slotTemplate) {
		subContractProfile.setSlotTemplateId(slotTemplate);
		return this;
	}

	@NonNull
	public SubContractProfileMaker<T, U, V> addProfileVesselRestriction(final Vessel... vessels) {

		final ProfileVesselRestriction constraint = ADPFactory.eINSTANCE.createProfileVesselRestriction();
		for (final Vessel vessel : vessels) {
			constraint.getVessels().add(vessel);
		}
		subContractProfile.getConstraints().add(constraint);
		return this;
	}

	/**
	 * Generic modifier call
	 * 
	 * @param action
	 * @return
	 */
	@NonNull
	public SubContractProfileMaker<T, U, V> with(@NonNull final Consumer<SubContractProfile<V>> action) {
		action.accept(subContractProfile);
		return this;
	}

	@NonNull
	public SubContractProfileMaker<T, U, V> withPreDefinedDistributionModel(final double volumne, final LNGVolumeUnit volumeUnit, final int windowSize, final TimePeriod windowSizeUnit,
			final List<LocalDate> dates) {

		final PreDefinedDistributionModel model = ADPFactory.eINSTANCE.createPreDefinedDistributionModel();
		model.setVolumePerCargo(volumne);
		model.setVolumeUnit(volumeUnit);
		model.setWindowSize(windowSize);
		model.setWindowSizeUnits(windowSizeUnit);
		dates.forEach(d -> {
			final PreDefinedDate date = ADPFactory.eINSTANCE.createPreDefinedDate();
			date.setDate(d);
			model.getDates().add(date);
		});
		subContractProfile.setDistributionModel(model);
		return this;
	}

	@NonNull
	public SubContractProfileMaker<T, U, V> withCargoSizeDistributionModel(final double volume, final LNGVolumeUnit volumeUnit, final boolean exact) {

		final CargoSizeDistributionModel model = ADPFactory.eINSTANCE.createCargoSizeDistributionModel();
		model.setVolumePerCargo(volume);
		model.setVolumeUnit(volumeUnit);
		model.setExact(exact);
		subContractProfile.setDistributionModel(model);
		return this;
	}

	@NonNull
	public SubContractProfileMaker<T, U, V> withCargoByQuarterDistributionModel(final double volume, final LNGVolumeUnit volumeUnit, final int q1, final int q2, final int q3, final int q4) {

		final CargoByQuarterDistributionModel model = ADPFactory.eINSTANCE.createCargoByQuarterDistributionModel();
		model.setVolumePerCargo(volume);
		model.setVolumeUnit(volumeUnit);
		model.setQ1(q1);
		model.setQ2(q2);
		model.setQ3(q3);
		model.setQ4(q4);
		subContractProfile.setDistributionModel(model);
		return this;
	}

	@NonNull
	public SubContractProfileMaker<T, U, V> withCargoIntervalDistributionModel(final double volume, final LNGVolumeUnit volumeUnit, final int quantity, final IntervalType intervalType,
			final int spacing) {

		final CargoIntervalDistributionModel model = ADPFactory.eINSTANCE.createCargoIntervalDistributionModel();
		model.setVolumePerCargo(volume);
		model.setVolumeUnit(volumeUnit);

		model.setSpacing(spacing);
		model.setQuantity(quantity);
		model.setIntervalType(intervalType);
		subContractProfile.setDistributionModel(model);
		return this;
	}

	@NonNull
	public SubContractProfileMaker<T, U, V> withCargoNumberDistributionModel(final double volume, final LNGVolumeUnit volumeUnit, final int numberOfCargoes) {

		final CargoNumberDistributionModel model = ADPFactory.eINSTANCE.createCargoNumberDistributionModel();
		model.setVolumePerCargo(volume);
		model.setVolumeUnit(volumeUnit);
		model.setNumberOfCargoes(numberOfCargoes);
		subContractProfile.setDistributionModel(model);
		return this;
	}

}

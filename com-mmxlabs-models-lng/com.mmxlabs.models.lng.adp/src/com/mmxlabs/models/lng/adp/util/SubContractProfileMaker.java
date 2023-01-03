/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import com.mmxlabs.models.lng.adp.PreDefinedDate;
import com.mmxlabs.models.lng.adp.PreDefinedDistributionModel;
import com.mmxlabs.models.lng.adp.ProfileVesselRestriction;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.TimePeriod;

public class SubContractProfileMaker<T extends AbstractContractProfileMaker<T, U, V, W>, U extends ContractProfile<V, W>, V extends Slot<W>, W extends Contract> {

	protected final @NonNull ADPModelBuilder adpModelBuilder;

	protected final @NonNull U contractProfile;

	private final @NonNull SubContractProfile<V, W> subContractProfile;

	private final T parent;

	public SubContractProfileMaker(final @NonNull ADPModelBuilder adpModelBuilder, final @NonNull U contractProfile, @NonNull final T parent) {
		this.adpModelBuilder = adpModelBuilder;
		this.contractProfile = contractProfile;
		this.parent = parent;
		this.subContractProfile = ADPFactory.eINSTANCE.createSubContractProfile();
		this.subContractProfile.setPort(contractProfile.getContract().getPreferredPort());
	}

	@NonNull
	public T build() {
		contractProfile.getSubProfiles().add(subContractProfile);
		return parent;
	}

	@NonNull
	public SubContractProfileMaker<T, U, V, W> withName(final String name) {
		subContractProfile.setName(name);
		return this;
	}

	@NonNull
	public SubContractProfileMaker<T, U, V, W> withContractType(final ContractType contractType) {
		subContractProfile.setContractType(contractType);
		return this;
	}

	@NonNull
	public SubContractProfileMaker<T, U, V, W> withSlotTemplate(final String slotTemplate) {
		subContractProfile.setSlotTemplateId(slotTemplate);
		return this;
	}
	
	@NonNull
	public SubContractProfileMaker<T, U, V, W> withNominatedVessel(final Vessel vessel) {
		subContractProfile.setNominatedVessel(vessel);
		return this;
	}

	@NonNull
	public SubContractProfileMaker<T, U, V, W> addProfileVesselRestriction(final Vessel... vessels) {

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
	public SubContractProfileMaker<T, U, V, W> with(@NonNull final Consumer<SubContractProfile<V, W>> action) {
		action.accept(subContractProfile);
		return this;
	}

	@NonNull
	public SubContractProfileMaker<T, U, V, W> withPreDefinedDistributionModel(final int windowSize, final TimePeriod windowSizeUnit, final List<LocalDate> dates) {

		final PreDefinedDistributionModel model = ADPFactory.eINSTANCE.createPreDefinedDistributionModel();
		dates.forEach(d -> {
			final PreDefinedDate date = ADPFactory.eINSTANCE.createPreDefinedDate();
			date.setDate(d);
			model.getDates().add(date);
		});
		subContractProfile.setDistributionModel(model);
		return this;
	}

	@NonNull
	public SubContractProfileMaker<T, U, V, W> withCargoSizeDistributionModel(final boolean exact) {

		final CargoSizeDistributionModel model = ADPFactory.eINSTANCE.createCargoSizeDistributionModel();
		model.setExact(exact);
		subContractProfile.setDistributionModel(model);
		return this;
	}

	@NonNull
	public SubContractProfileMaker<T, U, V, W> withCargoByQuarterDistributionModel(final int q1, final int q2, final int q3, final int q4) {

		final CargoByQuarterDistributionModel model = ADPFactory.eINSTANCE.createCargoByQuarterDistributionModel();
		model.setQ1(q1);
		model.setQ2(q2);
		model.setQ3(q3);
		model.setQ4(q4);
		subContractProfile.setDistributionModel(model);
		return this;
	}

	@NonNull
	public SubContractProfileMaker<T, U, V, W> withCargoIntervalDistributionModel(final int quantity, final IntervalType intervalType, final int spacing) {

		final CargoIntervalDistributionModel model = ADPFactory.eINSTANCE.createCargoIntervalDistributionModel();

		model.setSpacing(spacing);
		model.setQuantity(quantity);
		model.setIntervalType(intervalType);
		subContractProfile.setDistributionModel(model);
		return this;
	}

	@NonNull
	public SubContractProfileMaker<T, U, V, W> withCargoNumberDistributionModel(final int numberOfCargoes) {

		final CargoNumberDistributionModel model = ADPFactory.eINSTANCE.createCargoNumberDistributionModel();
		model.setNumberOfCargoes(numberOfCargoes);
		subContractProfile.setDistributionModel(model);
		return this;
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.util;

import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.IntervalType;
import com.mmxlabs.models.lng.adp.LNGVolumeUnit;
import com.mmxlabs.models.lng.adp.MaxCargoConstraint;
import com.mmxlabs.models.lng.adp.MinCargoConstraint;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.cargo.Slot;

public class AbstractContractProfileMaker<T extends AbstractContractProfileMaker<T, U, V>, U extends ContractProfile<V>, V extends Slot> {

	protected final @NonNull ADPModelBuilder adpModelBuilder;

	protected final @NonNull U contractProfile;

	public AbstractContractProfileMaker(final @NonNull ADPModelBuilder adpModelBuilder, final @NonNull U contractProfile) {
		this.adpModelBuilder = adpModelBuilder;
		this.contractProfile = contractProfile;
	}

	@NonNull
	public U build() {

		if (contractProfile instanceof PurchaseContractProfile) {
			PurchaseContractProfile purchaseContractProfile = (PurchaseContractProfile) contractProfile;
			adpModelBuilder.getADPModel().getPurchaseContractProfiles().add(purchaseContractProfile);
		} else if (contractProfile instanceof SalesContractProfile) {
			SalesContractProfile salesContractProfile = (SalesContractProfile) contractProfile;
			adpModelBuilder.getADPModel().getSalesContractProfiles().add(salesContractProfile);
		} else {
			assert false;
		}

		return contractProfile;
	}

	@NonNull
	public T withEnabled(boolean enabled) {
		contractProfile.setEnabled(enabled);
		return (T) this;
	}

	@NonNull
	public T withCustom(boolean custom) {
		contractProfile.setCustom(custom);
		return (T) this;
	}

	@NonNull
	public T withVolume(double volume, LNGVolumeUnit volumeUnit) {
		contractProfile.setTotalVolume(volume);
		contractProfile.setVolumeUnit(volumeUnit);
		return (T) this;
	}

	@NonNull
	public T addMaxCargoConstraint(int count, IntervalType intervalType) {

		MaxCargoConstraint constraint = ADPFactory.eINSTANCE.createMaxCargoConstraint();
		constraint.setMaxCargoes(count);
		constraint.setIntervalType(intervalType);
		contractProfile.getConstraints().add(constraint);
		return (T) this;
	}

	@NonNull
	public T addMinCargoConstraint(int count, IntervalType intervalType) {

		MinCargoConstraint constraint = ADPFactory.eINSTANCE.createMinCargoConstraint();
		constraint.setMinCargoes(count);
		constraint.setIntervalType(intervalType);
		contractProfile.getConstraints().add(constraint);
		return (T) this;
	}

	/**
	 * Generic modifier call
	 * 
	 * @param action
	 * @return
	 */
	@NonNull
	public T with(@NonNull Consumer<U> action) {
		action.accept(contractProfile);
		return (T) this;
	}

	@NonNull
	public SubContractProfileMaker<T, U, V> withSubContractProfile(String name) {

		SubContractProfileMaker<T, U, V> maker = new SubContractProfileMaker<T, U, V>(adpModelBuilder, contractProfile, (T) this);
		maker.withName(name);
		return maker;
	}

}

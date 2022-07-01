/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class ThirdPartySlotEntityConstraint extends AbstractModelMultiConstraint {

	public static final String KEY_SLOT_KEEP_OPEN = "ThirdPartySlotEntityConstraint.slot.keep-open";
	public static final String KEY_CARGO_WIRING = "ThirdPartySlotEntityConstraint.cargo.locked-wiring";
	public static final String KEY_CARGO_SAME_ENTITY = "ThirdPartySlotEntityConstraint.cargo.same.entity";
	public static final String KEY_CARGO_NON_SHIPPED = "ThirdPartySlotEntityConstraint.cargo.non-shipped";

	@Override
	protected void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof final Slot<?> slot) {

			final DetailConstraintStatusFactory baseFactory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName(ScenarioElementNameHelper.getTypeName(slot), ScenarioElementNameHelper.getNonNullString(slot.getName()));

			final BaseLegalEntity entity = slot.getSlotOrDelegateEntity();
			if (entity != null && entity.isThirdParty()) {
				final Cargo cargo = slot.getCargo();
				if (cargo == null && !slot.isLocked()) {
					baseFactory.copyName() //
							.withFormattedMessage("Open positions for third-party entities should be paired or marked as keep open.") //
							.withObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Locked()) //
							.withConstraintKey(KEY_SLOT_KEEP_OPEN) //
							.make(ctx, statuses);
				}
			}
		}
		if (target instanceof final Cargo cargo) {

			final DetailConstraintStatusFactory baseFactory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName(ScenarioElementNameHelper.getTypeName(cargo), ScenarioElementNameHelper.getName(cargo));

			int thirdPartyCount = 0;
			final Set<BaseLegalEntity> entities = new HashSet<>();
			for (final Slot<?> slot : cargo.getSlots()) {
				final BaseLegalEntity entity = slot.getSlotOrDelegateEntity();
				if (entity != null && entity.isThirdParty()) {
					++thirdPartyCount;
				}
				entities.add(entity);
			}
			if (thirdPartyCount > 0) {

				if (cargo.isAllowRewiring()) {
					baseFactory.copyName() //
							.withFormattedMessage("Cargoes for third-party entities should disallow rewiring") //
//							.withObjectAndFeature(slot, MMXCorePackage.eINSTANCE.getNamedObject_Name()) //
							.withObjectAndFeature(cargo, CargoPackage.eINSTANCE.getCargo_AllowRewiring()) //
							.withConstraintKey(KEY_CARGO_WIRING) //
							.make(ctx, statuses);
				}

				if (entities.size() > 1 || thirdPartyCount != cargo.getSlots().size()) {
					final DetailConstraintStatusFactory f = baseFactory.copyName() //
							.withFormattedMessage("All slots in a third-party cargo should use the same third-party entity");
					cargo.getSlots().forEach(s -> f.withObjectAndFeature(s, CargoPackage.eINSTANCE.getSlot_Entity()));
					f.withObjectAndFeature(cargo, CargoPackage.eINSTANCE.getCargo_Slots());
					f.withConstraintKey(KEY_CARGO_SAME_ENTITY);

					f.make(ctx, statuses);
				}

				if (cargo.getCargoType() == CargoType.FLEET) {
					final DetailConstraintStatusFactory f = baseFactory.copyName() //
							.withFormattedMessage("Cargoes for a third-party should be non-shipped");
					cargo.getSlots().forEach(s -> f.withObjectAndFeature(cargo, MMXCorePackage.eINSTANCE.getNamedObject_Name()));
					cargo.getSlots().forEach(s -> f.withObjectAndFeature(cargo, CargoPackage.eINSTANCE.getCargo_Slots()));
					f.withConstraintKey(KEY_CARGO_NON_SHIPPED);
					f.make(ctx, statuses);
				}
			}
		}
	}
}
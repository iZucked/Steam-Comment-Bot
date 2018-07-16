package com.mmxlabs.models.lng.adp.validation;

import java.awt.Desktop.Action;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class ADPSlotConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof Slot) {
			final Slot slot = (Slot) target;

			if (slot.eContainer() instanceof SubContractProfile<?> || extraContext.getContainer(slot) instanceof SubContractProfile<?>) {
				final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
						.withTag("ADP") //
						.withTypedName("ADP Slot", slot.getName());

				final Port port = slot.getPort();
				if (port == null) {
					factory.copyName() //
							.withMessage("No port specified - is the notional port missing on the contract?") //
							.withObjectAndFeature(slot, CargoPackage.Literals.SLOT__PORT) //
							.make(ctx, statuses);
				}
				if (slot instanceof LoadSlot) {
					LoadSlot loadSlot = (LoadSlot) slot;

					if (loadSlot.getSlotOrDelegateCV() == 0.0) {
						factory.copyName() //
								.withMessage("No CV specified") //
								.withObjectAndFeature(slot, CargoPackage.Literals.LOAD_SLOT__CARGO_CV) //
								.make(ctx, statuses);
					}
				}
			}
		}
	}

}

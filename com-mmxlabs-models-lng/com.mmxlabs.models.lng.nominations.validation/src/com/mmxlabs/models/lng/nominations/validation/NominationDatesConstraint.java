package com.mmxlabs.models.lng.nominations.validation;

import java.time.LocalDate;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.SlotNomination;
import com.mmxlabs.models.lng.nominations.utils.NominationsModelUtils;
import com.mmxlabs.models.lng.nominations.validation.internal.Activator;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Validate dueDate + alertDate are prior to load/discharge date of associated slot.
 */
public class NominationDatesConstraint extends AbstractModelMultiConstraint  {
	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (target instanceof SlotNomination) {
			final AbstractNomination nomination = (AbstractNomination)target;
			final String nomineeId = nomination.getNomineeId();
			if (nomineeId != null && !"".equals(nomineeId)) {
				final LNGScenarioModel scenarioModel = (LNGScenarioModel)extraContext.getRootObject();
				if (scenarioModel != null) {
					final Slot<?> slot = NominationsModelUtils.findSlot(scenarioModel, nomination);
					if (slot != null) {
						LocalDate date = slot.getWindowStart();

						//Check due date is sensible.
						if (date != null && nomination.getDueDate() != null && date.isBefore(nomination.getDueDate())) {
							final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus)
									ctx.createFailureStatus(String.format(
									"Nomination due date %s is after window start date (%s) of slot %s", nomination.getDueDate(),
									date, slot.getName())));
							status.addEObjectAndFeature(nomination, NominationsPackage.eINSTANCE.getAbstractNomination_DueDate());
							statuses.add(status);							
						}
						
						//Check alert date is sensible.
						if (date != null && nomination.getAlertDate() != null && date.isBefore(nomination.getAlertDate())) {
							final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus)
									ctx.createFailureStatus(String.format(
									"Nomination alert date %s is after window start date (%s) of slot %s", nomination.getAlertDate(),
									date, slot.getName())));
							status.addEObjectAndFeature(nomination, NominationsPackage.eINSTANCE.getAbstractNomination_AlertDate());
							statuses.add(status);	
						}
					}
				}
			}
		}
		return Activator.PLUGIN_ID;
	}
}
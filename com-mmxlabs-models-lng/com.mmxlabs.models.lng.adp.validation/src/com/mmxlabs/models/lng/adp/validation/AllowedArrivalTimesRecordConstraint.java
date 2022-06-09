package com.mmxlabs.models.lng.adp.validation;

import java.time.LocalDate;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.AllowedArrivalTimeRecord;
import com.mmxlabs.models.lng.adp.presentation.customisation.IInventoryBasedGenerationPresentationCustomiser;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.rcp.common.ServiceHelper;

public class AllowedArrivalTimesRecordConstraint extends AbstractModelMultiConstraint {

	private final String typedName;

	public AllowedArrivalTimesRecordConstraint() {
		final IInventoryBasedGenerationPresentationCustomiser[] customiserArr = new IInventoryBasedGenerationPresentationCustomiser[1];
		ServiceHelper.withOptionalServiceConsumer(IInventoryBasedGenerationPresentationCustomiser.class, v -> customiserArr[0] = v);
		typedName = customiserArr[0] != null ? customiserArr[0].getDropDownMenuLabel() : "Inventory-based generation";
	}

	@Override
	protected void doValidate(@NonNull IValidationContext ctx, @NonNull IExtraValidationContext extraContext, @NonNull List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (target instanceof @NonNull final AllowedArrivalTimeRecord allowedArrivalTimeRecord) {
			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName("ADP profile", typedName) //
					.withTag(ValidationConstants.TAG_ADP);
			if (allowedArrivalTimeRecord.getPeriodStart() == null) {
				factory.copyName() //
						.withObjectAndFeature(allowedArrivalTimeRecord, ADPPackage.Literals.ALLOWED_ARRIVAL_TIME_RECORD__PERIOD_START) //
						.withMessage("No start date provided") //
						.make(ctx, statuses);
			} else {
				final ADPModel adpModel = ScenarioModelUtil.getADPModel((LNGScenarioModel) extraContext.getRootObject());
				final LocalDate adpEnd = adpModel.getYearEnd().atDay(1);
				if (!allowedArrivalTimeRecord.getPeriodStart().isBefore(adpEnd)) {
					factory.copyName() //
							.withObjectAndFeature(allowedArrivalTimeRecord, ADPPackage.Literals.ALLOWED_ARRIVAL_TIME_RECORD__PERIOD_START) //
							.withMessage("Start date must be before ADP year end") //
							.make(ctx, statuses);
				}
			}
		}
	}
}

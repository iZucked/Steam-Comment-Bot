package com.mmxlabs.models.lng.adp.validation;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.MullEntityRow;
import com.mmxlabs.models.lng.adp.MullSubprofile;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class MullSubprofileConstraint extends AbstractModelMultiConstraint {
	
	private static final double EPSILON_DOUBLE = 0.00001;
	
	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof MullSubprofile) {
			final MullSubprofile mullSubprofile = (MullSubprofile) target;
			
			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName("ADP profile", "MULL Generation") //
					.withTag(ValidationConstants.TAG_ADP);
			if (mullSubprofile.getInventory() == null) {
				factory.copyName() //
						.withObjectAndFeature(mullSubprofile, ADPPackage.Literals.MULL_SUBPROFILE__INVENTORY) //
						.withMessage("No inventory selected") //
						.make(ctx, statuses);
			} else {
				final Inventory inventory = mullSubprofile.getInventory();
				
				if (inventory.getCapacities().isEmpty()) {
					factory.copyName() //
							.withObjectAndFeature(mullSubprofile, ADPPackage.Literals.MULL_SUBPROFILE__INVENTORY) //
							.withMessage(String.format("%s capacity data missing", inventory.getName())) //
							.make(ctx, statuses);
				} else {
					final LocalDate earliestCapacityDate = inventory.getCapacities().stream().map(r -> r.getDate()).min((d1, d2) -> d1.compareTo(d2)).get();
					final ADPModel adpModel = (ADPModel) mullSubprofile.eContainer().eContainer();
					final LocalDate adpStart = adpModel.getYearStart().atDay(1);
					if (earliestCapacityDate.isAfter(adpStart)) {
						factory.copyName() //
						.withObjectAndFeature(mullSubprofile, ADPPackage.Literals.MULL_SUBPROFILE__INVENTORY) //
						.withMessage(String.format("%s capacity information starts after ADP start", inventory.getName())) //
						.make(ctx, statuses);
					}
				}
			}
			
			if (mullSubprofile.getEntityTable().isEmpty()) {
				factory.copyName() //
						.withObjectAndFeature(mullSubprofile, ADPPackage.Literals.MULL_SUBPROFILE__ENTITY_TABLE) //
						.withMessage("No Entity entitlement data present.")
						.make(ctx, statuses);
			} else {
				final double sumReferenceEntitlement = mullSubprofile.getEntityTable().stream().mapToDouble(MullEntityRow::getRelativeEntitlement).sum();
				if (Math.abs(sumReferenceEntitlement - 1.0) > EPSILON_DOUBLE) {
					factory.copyName() //
						.withObjectAndFeature(mullSubprofile, ADPPackage.Literals.MULL_SUBPROFILE__ENTITY_TABLE) //
						.withMessage("Sum of reference entitlements should equal 1.") //
						.make(ctx, statuses);
				}
				validateDuplicateEntities(factory, mullSubprofile, ctx, statuses);
			}
		}
	}
	
	private void validateDuplicateEntities(@NonNull final DetailConstraintStatusFactory factory, final MullSubprofile mullSubprofile, final IValidationContext ctx, final List<IStatus> statuses) {
		final Map<BaseLegalEntity, List<MullEntityRow>> entityRows = new HashMap<>();
		for (final MullEntityRow entityRow : mullSubprofile.getEntityTable()) {
			if (entityRow.getEntity() == null) {
				continue;
			}
			List<MullEntityRow> entityRowList = entityRows.get(entityRow.getEntity());
			if (entityRowList == null) {
				entityRowList = new LinkedList<>();
				entityRows.put(entityRow.getEntity(), entityRowList);
			}
			entityRowList.add(entityRow);
		}
		entityRows.entrySet().stream() //
				.forEach(entry -> {
					if (entry.getValue().size() > 1) {
						factory.copyName() //
								.withObjectAndFeatures(entry.getValue().stream().map(row -> Pair.of(row, ADPPackage.Literals.MULL_ENTITY_ROW__ENTITY)).toArray(Pair[]::new)) //
								.withMessage(String.format("Duplicate entry for %s", entry.getKey().getName())) //
								.make(ctx, statuses);
					}
				});
	}
}

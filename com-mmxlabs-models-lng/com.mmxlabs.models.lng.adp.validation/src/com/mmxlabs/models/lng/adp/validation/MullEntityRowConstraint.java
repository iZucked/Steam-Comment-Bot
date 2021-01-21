package com.mmxlabs.models.lng.adp.validation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.DESSalesMarketAllocationRow;
import com.mmxlabs.models.lng.adp.MullEntityRow;
import com.mmxlabs.models.lng.adp.SalesContractAllocationRow;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class MullEntityRowConstraint extends AbstractModelMultiConstraint {
	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof MullEntityRow) {
			final MullEntityRow mullEntityRow = (MullEntityRow) target;
			
			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName("ADP profile", "MULL Generation") //
					.withTag(ValidationConstants.TAG_ADP);
			
			if (mullEntityRow.getEntity() == null) {
				factory.copyName() //
				.withObjectAndFeature(mullEntityRow, ADPPackage.Literals.MULL_ENTITY_ROW__ENTITY) //
				.withMessage("No entity selected") //
				.make(ctx, statuses);
			}
			
			if (mullEntityRow.getInitialAllocation() == null) {
				factory.copyName() //
				.withObjectAndFeature(mullEntityRow, ADPPackage.Literals.MULL_ENTITY_ROW__INITIAL_ALLOCATION) //
				.withMessage("A value must be provided for the initial allocation") //
				.make(ctx, statuses);
			} else {
				if (!mullEntityRow.getInitialAllocation().matches("-?\\d+")) {
					factory.copyName() //
					.withObjectAndFeature(mullEntityRow, ADPPackage.Literals.MULL_ENTITY_ROW__INITIAL_ALLOCATION) //
					.withMessage("The initial allocation must be a whole number") //
					.make(ctx, statuses);
				}
			}
			
			if (mullEntityRow.getRelativeEntitlement() <= 0) {
				factory.copyName() //
				.withObjectAndFeature(mullEntityRow, ADPPackage.Literals.MULL_ENTITY_ROW__RELATIVE_ENTITLEMENT) //
				.withMessage("Reference entitlement must be greater than zero") //
				.make(ctx, statuses);
			}
			
			if (mullEntityRow.getSalesContractAllocationRows().isEmpty() && mullEntityRow.getDesSalesMarketAllocationRows().isEmpty()) {
				factory.copyName() //
				.withObjectAndFeature(mullEntityRow, ADPPackage.Literals.MULL_ENTITY_ROW__SALES_CONTRACT_ALLOCATION_ROWS) //
				.withObjectAndFeature(mullEntityRow, ADPPackage.Literals.MULL_ENTITY_ROW__DES_SALES_MARKET_ALLOCATION_ROWS) //
				.withMessage("Entity allocation must have sales contract or DES sales market allocations") //
				.make(ctx, statuses);
			} else {
				if (!mullEntityRow.getSalesContractAllocationRows().isEmpty()) {
					validateDuplicateSalesAllocationRows(factory, mullEntityRow, ctx, statuses);
				}
				if (!mullEntityRow.getDesSalesMarketAllocationRows().isEmpty()) {
					validateDuplicateDESMarketAllocationRows(factory, mullEntityRow, ctx, statuses);
				}
			}
		}
	}
	
	private void validateDuplicateSalesAllocationRows(@NonNull final DetailConstraintStatusFactory factory, final MullEntityRow mullEntityRow, final IValidationContext ctx, final List<IStatus> statuses) {
		final Map<SalesContract, List<SalesContractAllocationRow>> salesContractAllocationRows = new HashMap<>();
		for (final SalesContractAllocationRow salesContractAllocationRow : mullEntityRow.getSalesContractAllocationRows()) {
			if (salesContractAllocationRow.getContract() == null) {
				continue;
			}
			List<SalesContractAllocationRow> contractAllocationRowList = salesContractAllocationRows.get(salesContractAllocationRow.getContract());
			if (contractAllocationRowList == null) {
				contractAllocationRowList = new LinkedList<>();
				salesContractAllocationRows.put(salesContractAllocationRow.getContract(), contractAllocationRowList);
			}
			contractAllocationRowList.add(salesContractAllocationRow);
		}
		salesContractAllocationRows.entrySet().stream() //
				.forEach(entry -> {
					if (entry.getValue().size() > 1) {
						factory.copyName() //
								.withObjectAndFeatures(entry.getValue().stream().map(row -> Pair.of(row, ADPPackage.Literals.SALES_CONTRACT_ALLOCATION_ROW__CONTRACT)).toArray(Pair[]::new)) //
								.withMessage(String.format("Duplicate entry for %s", entry.getKey().getName())) //
								.make(ctx, statuses);
					}
				});
	}
	
	private void validateDuplicateDESMarketAllocationRows(@NonNull final DetailConstraintStatusFactory factory, final MullEntityRow mullEntityRow, final IValidationContext ctx, final List<IStatus> statuses) {
		final Map<DESSalesMarket, List<DESSalesMarketAllocationRow>> desSalesMarketAllocationRows = new HashMap<>();
		for (final DESSalesMarketAllocationRow desSalesMarketAllocationRow : mullEntityRow.getDesSalesMarketAllocationRows()) {
			if (desSalesMarketAllocationRow.getDesSalesMarket() == null) {
				continue;
			}
			List<DESSalesMarketAllocationRow> marketAllocationRowList = desSalesMarketAllocationRows.get(desSalesMarketAllocationRow.getDesSalesMarket());
			if (marketAllocationRowList == null) {
				marketAllocationRowList = new LinkedList<>();
				desSalesMarketAllocationRows.put(desSalesMarketAllocationRow.getDesSalesMarket(), marketAllocationRowList);
			}
			marketAllocationRowList.add(desSalesMarketAllocationRow);
		}
		desSalesMarketAllocationRows.entrySet().stream() //
				.forEach(entry -> {
					if (entry.getValue().size() > 1) {
						factory.copyName() //
								.withObjectAndFeatures(entry.getValue().stream().map(row -> Pair.of(row, ADPPackage.Literals.DES_SALES_MARKET_ALLOCATION_ROW__DES_SALES_MARKET)).toArray(Pair[]::new)) //
								.withMessage(String.format("Duplicate entry for %s", entry.getKey().getName())) //
								.make(ctx, statuses);
					}
				});
	}
}
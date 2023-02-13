/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.validation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.DESSalesMarketAllocationRow;
import com.mmxlabs.models.lng.adp.MullAllocationRow;
import com.mmxlabs.models.lng.adp.MullEntityRow;
import com.mmxlabs.models.lng.adp.SalesContractAllocationRow;
import com.mmxlabs.models.lng.adp.presentation.customisation.IInventoryBasedGenerationPresentationCustomiser;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.rcp.common.ServiceHelper;

public class MullEntityRowConstraint extends AbstractModelMultiConstraint {
	private static final String REGEXP_INTEGER = "-?\\d+";
	private final String typedName;
	
	public MullEntityRowConstraint() {
		final IInventoryBasedGenerationPresentationCustomiser[] customiserArr = new IInventoryBasedGenerationPresentationCustomiser[1];
		ServiceHelper.withOptionalServiceConsumer(IInventoryBasedGenerationPresentationCustomiser.class, v -> customiserArr[0] = v);
		typedName = customiserArr[0] != null ? customiserArr[0].getDropDownMenuLabel() : "Inventory-based generation";
	}
	
	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof final MullEntityRow mullEntityRow) {
			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withTypedName("ADP profile", typedName) //
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
				if (!mullEntityRow.getInitialAllocation().matches(REGEXP_INTEGER)) {
					factory.copyName() //
							.withObjectAndFeature(mullEntityRow, ADPPackage.Literals.MULL_ENTITY_ROW__INITIAL_ALLOCATION) //
							.withMessage("The initial allocation must be a whole number") //
							.make(ctx, statuses);
				}
			}

			if (mullEntityRow.getRelativeEntitlement() < 0) {
				factory.copyName() //
						.withObjectAndFeature(mullEntityRow, ADPPackage.Literals.MULL_ENTITY_ROW__RELATIVE_ENTITLEMENT) //
						.withMessage("Reference entitlement must be nonnegative") //
						.make(ctx, statuses);
			} else if (mullEntityRow.getRelativeEntitlement() == 0.0) {
				final List<MullAllocationRow> nonzeroAACQMullAllocations = Stream.concat(mullEntityRow.getSalesContractAllocationRows().stream().map(MullAllocationRow.class::cast),
						mullEntityRow.getDesSalesMarketAllocationRows().stream().map(MullAllocationRow.class::cast)).filter(row -> row.getWeight() > 0).collect(Collectors.toList());
				if (!nonzeroAACQMullAllocations.isEmpty()) {
					factory.copyName().withObjectAndFeature(mullEntityRow, ADPPackage.Literals.MULL_ENTITY_ROW__RELATIVE_ENTITLEMENT);
					for (final MullAllocationRow mullAllocationRow : nonzeroAACQMullAllocations) {
						factory.withObjectAndFeature(mullAllocationRow, ADPPackage.Literals.MULL_ALLOCATION_ROW__WEIGHT);
					}
					factory.withMessage("Entity with relative entitlement equal to zero must not have allocations with non-zero AACQ") //
							.make(ctx, statuses);
				}
			} else {
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
	}

	private void validateDuplicateSalesAllocationRows(@NonNull final DetailConstraintStatusFactory factory, final MullEntityRow mullEntityRow, final IValidationContext ctx,
			final List<IStatus> statuses) {
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

	private void validateDuplicateDESMarketAllocationRows(@NonNull final DetailConstraintStatusFactory factory, final MullEntityRow mullEntityRow, final IValidationContext ctx,
			final List<IStatus> statuses) {
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
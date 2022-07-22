/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.transfers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transfers.TransferAgreement;
import com.mmxlabs.models.lng.transfers.TransferModel;
import com.mmxlabs.models.lng.transfers.TransferRecord;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.contracts.ISlotTransformer;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.entities.IEntity;
import com.mmxlabs.scheduler.optimiser.providers.ITransferModelDataProviderEditor;
import com.mmxlabs.scheduler.optimiser.transfers.BasicTransferAgreement;
import com.mmxlabs.scheduler.optimiser.transfers.BasicTransferRecord;
import com.mmxlabs.scheduler.optimiser.transfers.TransfersLookupData;

/**
 */
public class TransfersDataTransformer implements ISlotTransformer {
	
	@Inject
	private ITransferModelDataProviderEditor transferModelDataProviderEditor;
	
	@Inject
	@Named(SchedulerConstants.Parser_Commodity)
	private SeriesParser commodityParser;
	
	@Inject
	private DateAndCurveHelper dateAndCurveHelper;
	

	@Override
	public void slotTransformed(@NonNull Slot<?> modelSlot, @NonNull IPortSlot optimiserSlot) {
		transferModelDataProviderEditor.reconsileIPortSlotWithLookupData(optimiserSlot);
	}
	
	@Override
	public void startTransforming(LNGScenarioModel lngScenarioModel, ModelEntityMap modelEntityMap, ISchedulerBuilder builder) {
		if (true) {
			if (lngScenarioModel != null) {
				final TransferModel transferModel = lngScenarioModel.getTransferModel();
				if (transferModel != null) {					
					transferModelDataProviderEditor.addLookupData(createLookUpData(transferModel, modelEntityMap));
				}
			}
		}
	}

	private TransfersLookupData createLookUpData(final @NonNull TransferModel transferModel, //
			final @NonNull ModelEntityMap modelEntityMap) {
		final List<BasicTransferAgreement> agreements = createBasicAgreements(transferModel, modelEntityMap);
		final List<BasicTransferRecord> records = createBasicRecords(transferModel, modelEntityMap);
		return new TransfersLookupData(agreements, records);
	}

	private List<BasicTransferAgreement> createBasicAgreements(final @NonNull TransferModel transfersModel, //
			final @NonNull ModelEntityMap modelEntityMap) {
		final List<BasicTransferAgreement> agreements = new ArrayList<>(transfersModel.getTransferAgreements().size());
		for (final TransferAgreement ta : transfersModel.getTransferAgreements()) {
			final String priceExpression = ta.getPriceExpression();
			if (priceExpression != null && !priceExpression.isBlank()) {
				final ICurve curve = dateAndCurveHelper.generateExpressionCurve(priceExpression, commodityParser);
				final IEntity fromEntity = modelEntityMap.getOptimiserObjectNullChecked(ta.getFromEntity(), IEntity.class);
				final IEntity toEntity = modelEntityMap.getOptimiserObjectNullChecked(ta.getToEntity(), IEntity.class);
				final BasicTransferAgreement foo = new BasicTransferAgreement(ta.getName(), fromEntity, //
						toEntity, curve, priceExpression);
				agreements.add(foo);
				modelEntityMap.addModelObject(ta, foo);
			}
		}
		return agreements;
	}

	private List<BasicTransferRecord> createBasicRecords(final @NonNull TransferModel transfersModel, //
			final @NonNull ModelEntityMap modelEntityMap){
		final List<BasicTransferRecord> records = new ArrayList<>(transfersModel.getTransferRecords().size());
		for (final TransferRecord tr : transfersModel.getTransferRecords()) {
			final TransferAgreement ta = tr.getTransferAgreement();
			if (ta != null) {

				final BasicTransferAgreement fooTA = modelEntityMap.getOptimiserObjectNullChecked(ta, BasicTransferAgreement.class);
				final Slot<?> slot = tr.getLhs();
				if (slot != null) {
					String prefix = "FP-";
					if (slot instanceof LoadSlot) {
						prefix = "FP-";
						if (((LoadSlot) slot).isDESPurchase()) {
							prefix = "DP-";
						}
					} else {
						prefix = "DS-";
						if (((DischargeSlot) slot).isFOBSale()) {
							prefix = "FS-";
						}
					}
					ICurve curve = null;
					final String priceExpression = tr.getPriceExpression();
					if (priceExpression != null && !priceExpression.isBlank()) {
						curve = dateAndCurveHelper.generateExpressionCurve(tr.getPriceExpression(), commodityParser);
					}
					final BasicTransferRecord barTR = new BasicTransferRecord(tr.getName(), fooTA,
							tr.isSetPriceExpression(), curve, tr.getPriceExpression(), dateAndCurveHelper.convertTime(tr.getPricingDate()), //
							prefix + slot.getName(), tr.getRhs() == null ? "" : tr.getRhs().getName());
					records.add(barTR);
					modelEntityMap.addModelObject(tr, barTR);
				}
			}
		}
		return records;
	}
}

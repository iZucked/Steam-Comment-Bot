/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.transfers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.parser.series.SeriesParser;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transfers.TransferAgreement;
import com.mmxlabs.models.lng.transfers.TransferModel;
import com.mmxlabs.models.lng.transfers.TransferRecord;
import com.mmxlabs.models.lng.transfers.TransferStatus;
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

	@Inject
	@Named(SchedulerConstants.PROCESS_TRANSFER_MODEL)
	private boolean processTransferModel;

	LNGScenarioModel lngScenarioModel;
	ModelEntityMap modelEntityMap;
	final List<IPortSlot> seenSlots = new LinkedList<>();
	final Map<Slot<?>, IPortSlot> modelToOptimiserSlots = new HashMap<>();

	@Override
	public void slotTransformed(@NonNull Slot<?> modelSlot, @NonNull IPortSlot optimiserSlot) {
		if (processTransferModel) {
			seenSlots.add(optimiserSlot);
			modelToOptimiserSlots.put(modelSlot, optimiserSlot);
		}
	}

	@Override
	public void startTransforming(LNGScenarioModel lngScenarioModel, ModelEntityMap modelEntityMap, ISchedulerBuilder builder) {
		if (processTransferModel) {
			this.lngScenarioModel = lngScenarioModel;
			this.modelEntityMap = modelEntityMap;
		}
	}

	@Override
	public void finishTransforming() {
		if (processTransferModel) {
			transferModelDataProviderEditor.setSeriesParsers(commodityParser);
			if (lngScenarioModel != null) {
				final TransferModel transferModel = lngScenarioModel.getTransferModel();
				if (transferModel != null) {					
					transferModelDataProviderEditor.setLookupData(createLookUpData(transferModel, modelEntityMap));
					seenSlots.forEach(transferModelDataProviderEditor::reconsileIPortSlotWithLookupData);
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

	/**
	 * Does not add transfer records where:
	 * 1. Source and destination entities are equal
	 * 2. Price expression is missing or empty
	 * @param transfersModel
	 * @param modelEntityMap
	 * @return
	 */
	private List<BasicTransferAgreement> createBasicAgreements(final @NonNull TransferModel transfersModel, //
			final @NonNull ModelEntityMap modelEntityMap) {
		final List<BasicTransferAgreement> agreements = new ArrayList<>(transfersModel.getTransferAgreements().size());
		for (final TransferAgreement ta : transfersModel.getTransferAgreements()) {
			final String priceExpression;
			if (ta.getPriceExpression() != null && !ta.getPriceExpression().isBlank()) {
				priceExpression = ta.getPriceExpression();
			} else {
				throw new IllegalStateException(String.format("Transfer agreement %s is missing the price data", ta.getName()));
			}
			final IEntity fromEntity = modelEntityMap.getOptimiserObjectNullChecked(ta.getFromEntity(), IEntity.class);
			final IEntity toEntity = modelEntityMap.getOptimiserObjectNullChecked(ta.getToEntity(), IEntity.class);
			final BasicTransferAgreement basicTA = new BasicTransferAgreement(ta.getName(), fromEntity, //
					toEntity, priceExpression);
			agreements.add(basicTA);
			modelEntityMap.addModelObject(ta, basicTA);
		}
		return agreements;
	}

	/** Does not process transfer records where:
	 * 0. Cancelled transfer records
	 * 1. Transfer agreement is not set
	 * 2. LHS is not referred
	 * 3. Price expression is missing or empty
	 * 4. Pricing date is not set
	 * @param transfersModel
	 * @param modelEntityMap
	 * @return
	 */
	private List<BasicTransferRecord> createBasicRecords(final @NonNull TransferModel transfersModel, //
			final @NonNull ModelEntityMap modelEntityMap){
		final List<BasicTransferRecord> records = new ArrayList<>(transfersModel.getTransferRecords().size());
		for (final TransferRecord tr : transfersModel.getTransferRecords()) {
			if (tr.getStatus() != TransferStatus.CANCELLED) {
				final TransferAgreement ta = tr.getTransferAgreement();
				final boolean pricingOverride;
				String priceExpression = "";
				if (tr.getPriceExpression() != null && !tr.getPriceExpression().isBlank()) {
					pricingOverride = true;
					priceExpression = tr.getPriceExpression();
				} else if (ta.getPriceExpression() != null && !ta.getPriceExpression().isBlank()) {
					pricingOverride = false;
					priceExpression = ta.getPriceExpression();
				} else {
					throw new IllegalStateException(String.format("Transfer record %s is missing the price data", tr.getName()));
				}

				if (ta != null && tr.getLhs() != null && tr.getRecordOrDelegatePricingDate() != null) {

					final BasicTransferAgreement basicTA = modelEntityMap.getOptimiserObjectNullChecked(ta, BasicTransferAgreement.class);
					final Slot<?> slot = tr.getLhs();
					final IPortSlot portSlot = modelToOptimiserSlots.get(slot);
					
					final int pricingDate = dateAndCurveHelper.convertTime(tr.getRecordOrDelegatePricingDate());
					final String nextTransferName = tr.getRhs() == null ? "" : tr.getRhs().getName();
					final BasicTransferRecord basicTR;
					
					if (pricingOverride) {
						basicTR = new BasicTransferRecord(tr.getName(), basicTA, priceExpression, pricingDate, portSlot, nextTransferName);
					} else {
						basicTR = new BasicTransferRecord(tr.getName(), basicTA, pricingDate, portSlot, nextTransferName);
					}
					records.add(basicTR);
					modelEntityMap.addModelObject(tr, basicTR);

				}
			}
		}
		return records;
	}
}

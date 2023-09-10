/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.io.paper;

import java.time.YearMonth;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.lngdataserver.integration.paper.model.DatahubPaperDeal;
import com.mmxlabs.lngdataserver.integration.paper.model.PaperVersion;
import com.mmxlabs.lngdataserver.integration.paper.model.DatahubPaperDeal.Kind;
import com.mmxlabs.lngdataserver.integration.paper.model.DatahubPaperDeal.PricingType;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.PaperPricingType;
import com.mmxlabs.models.lng.cargo.SellPaperDeal;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.VersionRecord;

public class PaperToScenarioCopier {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaperToScenarioCopier.class);

	private PaperToScenarioCopier() {

	}

	public static Command getUpdateCommand(@NonNull final EditingDomain editingDomain, @NonNull final CargoModel cargoModel, final PaperVersion version) {
		final CompoundCommand cmd = new CompoundCommand("Update paper deals");
		Set<PaperDeal> existing = new HashSet<PaperDeal>();
		cargoModel.getPaperDeals().forEach(existing::add);

		if (!existing.isEmpty()) {
			cmd.append(DeleteCommand.create(editingDomain, existing));
		}

		for (final DatahubPaperDeal paper : version.getPapersList()) {
			final PaperDeal newPaper;
			switch (paper.getKind()) {
				case BUY_PAPER_DEAL:
					newPaper = CargoFactory.eINSTANCE.createBuyPaperDeal();
					break;
				case SELL_PAPER_DEAL:
					newPaper = CargoFactory.eINSTANCE.createSellPaperDeal();
					break;
				default:
					throw new IllegalArgumentException();
			}
			newPaper.setName(paper.getName());
			newPaper.setComment(paper.getComment());
			newPaper.setIndex(paper.getIndex());
			newPaper.setPrice(paper.getPrice());
			newPaper.setQuantity(paper.getQuantity());
			newPaper.setPricingMonth(YearMonth.from(paper.getPricingMonth()));
			newPaper.setPricingPeriodStart(paper.getPricingPeriodStart());
			newPaper.setPricingPeriodEnd(paper.getPricingPeriodEnd());
			newPaper.setHedgingPeriodStart(paper.getHedgingPeriodStart());
			newPaper.setHedgingPeriodEnd(paper.getHedgingPeriodEnd());
			newPaper.setYear(paper.getYear());
			newPaper.setPricingType(pricingTypeMap.get(paper.getPricingType()));
		
			cmd.append(AddCommand.create(
				editingDomain,
				cargoModel,
				CargoPackage.Literals.CARGO_MODEL__PAPER_DEALS,
				newPaper
			));
		}		
		
		/*
		// Gather existing curves
		final Set<PaperDeal> existing = new HashSet<>();
		cargoModel.getPaperDeals().forEach(existing::add);;
		
		Collection<DatahubPaperDeal> newPapers = version.getPaperDeals();
		for (final DatahubPaperDeal deal : newPapers) {
			final String name = deal.getName();
		}
		*/
		
		// Update version records
		VersionRecord record = cargoModel.getPaperDealsVersionRecord();
		cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__CREATED_BY, version.getCreatedBy()));
		cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__CREATED_AT, version.getCreatedAt()));
		cmd.append(SetCommand.create(editingDomain, record, MMXCorePackage.Literals.VERSION_RECORD__VERSION, version.getIdentifier()));
		return cmd;
	}
	private static Map<PricingType, PaperPricingType> pricingTypeMap = new HashMap<>();
	static {
		pricingTypeMap.put(PricingType.CALENDAR, PaperPricingType.CALENDAR);
		pricingTypeMap.put(PricingType.PERIOD_AVG, PaperPricingType.PERIOD_AVG);
		pricingTypeMap.put(PricingType.INSTRUMENT, PaperPricingType.INSTRUMENT);
	}

}

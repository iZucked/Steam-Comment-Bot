/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.nominations;

import java.time.LocalDate;

import org.eclipse.jdt.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.utils.NominationsModelUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

public class Nominations {
	String slotId;
	String buyer;
	String seller;
	String cn;
	String type;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonSerialize(using = LocalDateSerializer.class)
	LocalDate date;
	String nominationType;
	String nominationComment;
	String nomination;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonSerialize(using = LocalDateSerializer.class)
	LocalDate nominateBy;
	boolean warn;

	public Nominations() {
		this.slotId = "";
		this.buyer = "";
		this.seller = "";
		this.cn = "";
		this.type = "";
		this.date = null;
		this.nomination = "";
		this.nominateBy = null;
		this.nominationType = "";
		this.nominationComment = "";
		this.warn = false;
	}

	public Nominations(@NonNull final LNGScenarioModel scenarioModel, @NonNull final AbstractNomination sn) {
		this.slotId = sn.getNomineeId();
		this.buyer = NominationsModelUtils.getTo(scenarioModel, sn);
		this.seller = NominationsModelUtils.getFrom(scenarioModel, sn);
		this.cn = NominationsModelUtils.getCN(scenarioModel, sn);
		this.type = NominationsModelUtils.getSide(sn);
		this.date = NominationsModelUtils.getDate(scenarioModel, sn);
		this.nominationType = sn.getType();
		this.nominateBy = NominationsModelUtils.getDueDate(scenarioModel, sn);
		this.nominationComment = NominationsModelUtils.getRemark(scenarioModel, sn);
		this.nomination = ""; //Not used currently.
		
		if (this.nominateBy != null) {
			final LocalDate today = LocalDate.now();
			if (!today.isBefore(this.nominateBy)) {
				this.warn = true;
			} else {
				final LocalDate alertDate = NominationsModelUtils.getAlertDate(scenarioModel, sn);
				if (alertDate != null && !today.isBefore(alertDate)) {
					this.warn = true;
				}
			}
		}
	}
	
	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(final String buyer) {
		this.buyer = buyer;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(final String seller) {
		this.seller = seller;
	}

	public String getNomination() {
		return nomination;
	}

	public void setNomination(final String nomination) {
		this.nomination = nomination;
	}

	public LocalDate getNominateBy() {
		return nominateBy;
	}

	public void setNominateBy(final LocalDate nominateBy) {
		this.nominateBy = nominateBy;
	}

	public boolean isWarn() {
		return warn;
	}

	public void setWarn(final boolean warn) {
		this.warn = warn;
	}

	public String getSlotId() {
		return slotId;
	}

	public String getCn() {
		return cn;
	}

	public String getType() {
		return type;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getNominationType() {
		return nominationType;
	}

	public String getNominationComment() {
		return nominationComment;
	}
}

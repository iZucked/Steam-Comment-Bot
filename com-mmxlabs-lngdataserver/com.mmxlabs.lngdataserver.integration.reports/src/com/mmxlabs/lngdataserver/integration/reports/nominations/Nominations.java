/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.reports.nominations;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import org.eclipse.jdt.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.google.common.io.Files;
import com.mmxlabs.lingo.reports.modelbased.SchemaGenerator;
import com.mmxlabs.lingo.reports.modelbased.SchemaGenerator.Mode;
import com.mmxlabs.lingo.reports.modelbased.annotations.ColumnName;
import com.mmxlabs.lingo.reports.modelbased.annotations.HubColumnStyle;
import com.mmxlabs.lingo.reports.modelbased.annotations.HubFormat;
import com.mmxlabs.lingo.reports.modelbased.annotations.SchemaVersion;
import com.mmxlabs.lngdataserver.integration.ui.scenarios.extensions.TodayProvider;
import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.utils.NominationsModelUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;

@SchemaVersion(1)
public class Nominations {
	@ColumnName("Cargo")
	public String slotId;
	
	@ColumnName("Type")
	public String type;

	@ColumnName("From")
	public String seller;

	@ColumnName("To")
	public String buyer;

	@ColumnName("CN")
	public String cn;

	@ColumnName("Date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonSerialize(using = LocalDateSerializer.class)
	@HubFormat("DD/MM/YYYY hh:mm")
	public LocalDate date;

	@ColumnName("Nomination type")
	public String nominationType;

	@ColumnName("Nominate by")
	@HubColumnStyle("""
			{
				"row": [ {
					"condition":"<",
					"value":"now",
					"styling": [ {
						"property":"background-color",
						"value":"rgb(255, 234, 158)"
					} ]
				} ]
			}""")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonSerialize(using = LocalDateSerializer.class)
	@HubFormat("DD/MM/YYYY hh:mm")
	public LocalDate nominateBy;

	@ColumnName("Remark")
	public String nominationComment;

	public String nomination;
	public boolean warn;

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
		this.nomination = ""; // Not used currently.

		if (this.nominateBy != null) {
			final LocalDate today = TodayProvider.getInstance().getToday().toLocalDate();
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
	
	public static void main(String[] args) throws Exception {
		String schema = new SchemaGenerator().generateHubSchema(Nominations.class, Mode.SUMMARY);
		System.out.println(schema);
		Files.write(schema, new File("target/noms.json"), StandardCharsets.UTF_8);
	}
}

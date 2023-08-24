/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.paper.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.mmxlabs.rcp.common.json.CreatedAtInstantDeserializer;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({ "published", "current", "metaInformation", "version" })
public class PaperVersion {

	@JsonDeserialize(using = CreatedAtInstantDeserializer.class)
	@JsonSerialize(using = InstantSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS]", timezone = "UTC")
	private Instant createdAt;

	private String identifier;

	private String createdBy;
	
	List<DatahubPaperDeal> paperDeals = new ArrayList<>();

	public PaperVersion() {
		// jackson
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public List<DatahubPaperDeal> getPaperDeals() {
		return paperDeals;
	}

	public void setPaperDeals(List<DatahubPaperDeal> paperDeals) {
		this.paperDeals = paperDeals;
	}
}

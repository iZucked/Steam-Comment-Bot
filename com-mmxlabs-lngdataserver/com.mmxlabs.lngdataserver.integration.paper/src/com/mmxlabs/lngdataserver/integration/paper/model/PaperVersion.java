/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.paper.model;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.mmxlabs.lngdataserver.commons.model.CreatedAtInstantDeserializer;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaperVersion {

	@JsonDeserialize(using = CreatedAtInstantDeserializer.class)
	@JsonSerialize(using = InstantSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS]", timezone = "UTC")
	private Instant createdAt;

	private String createdBy;

	private String identifier;
	
	private List<DatahubPaperDeal> papersList = new LinkedList<>();

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
	
	public List<DatahubPaperDeal> getPapersList() {
		return papersList;
	}
	
	public void setPaperDealsList(List<DatahubPaperDeal> deals) {
		this.papersList = deals;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}

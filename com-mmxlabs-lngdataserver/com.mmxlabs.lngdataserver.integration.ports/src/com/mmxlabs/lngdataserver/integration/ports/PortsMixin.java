/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ports;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mmxlabs.lngdataserver.integration.repo.general.GeneralDataRecord;
import com.mmxlabs.rcp.common.json.CreatedAtInstantDeserializer;

/**
 * Jackson Mixin class to map upstream version data object to {@link GeneralDataRecord} fields.
 * 
 * @author Simon Goodall
 *
 */
@JsonIgnoreProperties({ "creator", "type", "published", "current", "ports" })
public class PortsMixin {

	@JsonAlias("identifier")
	public String uuid;

	@JsonAlias("createdAt")
	@JsonDeserialize(using = CreatedAtInstantDeserializer.class)
	public Instant creationDate;

}

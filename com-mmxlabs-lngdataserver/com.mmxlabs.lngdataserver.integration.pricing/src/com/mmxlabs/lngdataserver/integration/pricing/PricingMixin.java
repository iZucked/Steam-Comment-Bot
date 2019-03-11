/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.pricing;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mmxlabs.lngdataserver.commons.model.CreatedAtInstantDeserializer;
import com.mmxlabs.lngdataserver.integration.repo.GeneralDataRecord;

/**
 * Jackson Mixin class to map upstream version data object to {@link GeneralDataRecord} fields.
 * 
 * @author Simon Goodall
 *
 */
@JsonIgnoreProperties({ "creator", "published", "current" })
public class PricingMixin {

	@JsonAlias("identifier")
	public String uuid;

	@JsonAlias("createdAt")
	@JsonDeserialize(using = CreatedAtInstantDeserializer.class)
	public Instant creationDate;

}

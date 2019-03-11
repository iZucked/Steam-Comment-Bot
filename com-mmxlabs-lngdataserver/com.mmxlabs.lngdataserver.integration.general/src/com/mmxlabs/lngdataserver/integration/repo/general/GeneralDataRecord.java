package com.mmxlabs.lngdataserver.integration.repo.general;

import java.time.Instant;

import com.google.common.base.Objects;

import io.swagger.annotations.ApiModelProperty;

public class GeneralDataRecord {

	@ApiModelProperty(required = true)
	private String uuid;

	private String creator;

	private Instant creationDate;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Instant getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Instant creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof GeneralDataRecord) {
			GeneralDataRecord other = (GeneralDataRecord) obj;
			return Objects.equal(uuid, other.getUuid());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return uuid.hashCode();
	}
}

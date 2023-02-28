package com.mmxlabs.lngdataserver.data.distances.atobviac.impl;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AtoBviaCTimezone {
	@JsonProperty("UtcCurrentOffset")
	private int utcCurrentOffset;
	@JsonProperty("RawOffset")
	private int rawOffset;

	@JsonProperty("TimezoneId")
	private String timezoneId;

	@JsonProperty("DisplayName")
	private String displayName;

	public int getUtcCurrentOffset() {
		return utcCurrentOffset;
	}

	public void setUtcCurrentOffset(int utcCurrentOffset) {
		this.utcCurrentOffset = utcCurrentOffset;
	}

	public int getRawOffset() {
		return rawOffset;
	}

	public void setRawOffset(int rawOffset) {
		this.rawOffset = rawOffset;
	}

	public String getTimezoneId() {
		return timezoneId;
	}

	public void setTimezoneId(String timezoneId) {
		this.timezoneId = timezoneId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}

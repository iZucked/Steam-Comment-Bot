/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.integration.ui.scenarios.cloud;

import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;
import org.json.JSONObject;

public class ResultStatus {

	public static final String STATUS_SUBMITTED = "submitted";
	public static final String STATUS_COMPLETE = "complete";
	public static final String STATUS_RUNNING = "running";
	public static final String STATUS_FAILED = "failed";
	public static final String STATUS_NOTFOUND = "notfound";

	public static ResultStatus submitted() {
		final ResultStatus rs = new ResultStatus();
		rs.progress = 0.0;
		rs.status = STATUS_SUBMITTED;
		return rs;
	}

	private String status;
	private String reason;

	private double progress;

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(final double progress) {
		this.progress = progress;
	}

	public ResultStatus copy() {
		final ResultStatus rs = new ResultStatus();
		rs.progress = this.progress;
		rs.status = this.status;
		rs.reason = this.reason;
		return rs;
	}

	public boolean isRunning() {
		return STATUS_RUNNING.equalsIgnoreCase(status);
	}

	public boolean isFailed() {
		return STATUS_FAILED.equalsIgnoreCase(status);
	}

	public boolean isComplete() {
		return STATUS_COMPLETE.equalsIgnoreCase(status);
	}

	public boolean isNotFound() {
		return STATUS_NOTFOUND.equalsIgnoreCase(status);
	}

	public static ResultStatus from(final String str, @Nullable final ResultStatus previous) {
		final ResultStatus rs = new ResultStatus();
		rs.status = "unknown";

		final JSONObject o = new JSONObject(str);
		if (o.has("status")) {
			rs.status = o.getString("status");
		}
		if (o.has("progress")) {
			rs.progress = o.getDouble("progress");
		}
		if (o.has("reason")) {
			rs.reason = o.getString("reason");
		}
		// AWS Eventually consistent means we can get out of order progress updates!
		// We want to keep the highest progress value found
		if (rs.isRunning()) {
			if (previous != null && previous.getProgress() > rs.getProgress()) {
				rs.progress = previous.getProgress();
			}
		}

		return rs;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof final ResultStatus other) {
			return Objects.equals(status, other.status) && progress == other.progress;
		}
		return false;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}

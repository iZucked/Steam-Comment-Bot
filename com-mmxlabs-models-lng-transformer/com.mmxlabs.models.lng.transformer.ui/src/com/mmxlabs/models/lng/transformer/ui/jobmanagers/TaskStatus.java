package com.mmxlabs.models.lng.transformer.ui.jobmanagers;

import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TaskStatus {

	public static final String STATUS_SUBMITTED = "submitted";
	public static final String STATUS_COMPLETE = "complete";
	public static final String STATUS_RUNNING = "running";
	public static final String STATUS_FAILED = "failed";
	public static final String STATUS_NOTFOUND = "notfound";

	public static @NonNull TaskStatus submitted() {
		final TaskStatus rs = new TaskStatus();
		rs.progress = 0.0;
		rs.status = STATUS_SUBMITTED;
		return rs;
	}

	public static TaskStatus running(double d) {
		final TaskStatus rs = new TaskStatus();
		rs.progress = d;
		rs.status = STATUS_RUNNING;
		return rs;
	}

	public static TaskStatus failed() {
		final TaskStatus rs = new TaskStatus();
		rs.progress = 0.0;
		rs.status = STATUS_FAILED;
		rs.reason = "Failed";
		return rs;
	}

	public static TaskStatus failed(String reason) {
		final TaskStatus rs = new TaskStatus();
		rs.progress = 0.0;
		rs.status = STATUS_FAILED;
		rs.reason = reason;
		return rs;
	}

	public static TaskStatus aborted() {
		final TaskStatus rs = new TaskStatus();
		rs.progress = 0.0;
		rs.status = STATUS_FAILED;
		rs.reason = "Aborted by user";
		return rs;
	}

	public static TaskStatus complete() {
		final TaskStatus rs = new TaskStatus();
		rs.progress = 0.0;
		rs.status = STATUS_COMPLETE;
		return rs;
	}

	public static @NonNull TaskStatus notfound() {
		final TaskStatus rs = new TaskStatus();
		rs.progress = 0.0;
		rs.status = STATUS_NOTFOUND;
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

	@JsonIgnore
	public TaskStatus copy() {
		final TaskStatus rs = new TaskStatus();
		rs.progress = this.progress;
		rs.status = this.status;
		rs.reason = this.reason;
		return rs;
	}

	@JsonIgnore
	public boolean isRunning() {
		return STATUS_RUNNING.equalsIgnoreCase(status);
	}

	@JsonIgnore
	public boolean isFailed() {
		return STATUS_FAILED.equalsIgnoreCase(status);
	}

	@JsonIgnore
	public boolean isComplete() {
		return STATUS_COMPLETE.equalsIgnoreCase(status);
	}

	@JsonIgnore
	public boolean isNotFound() {
		return STATUS_NOTFOUND.equalsIgnoreCase(status);
	}

	@JsonIgnore
	public boolean isSubmitted() {
		return STATUS_SUBMITTED.equalsIgnoreCase(status);

	}

	public static TaskStatus from(final String str, @Nullable final TaskStatus previous) {
		final TaskStatus rs = new TaskStatus();
		rs.status = "unknown";

		if (str != null) {
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
		if (obj instanceof final TaskStatus other) {
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

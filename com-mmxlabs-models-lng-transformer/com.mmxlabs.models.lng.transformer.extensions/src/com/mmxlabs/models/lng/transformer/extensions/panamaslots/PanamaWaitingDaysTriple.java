package com.mmxlabs.models.lng.transformer.extensions.panamaslots;

import org.eclipse.jdt.annotation.NonNull;

public record PanamaWaitingDaysTriple(int changePoint, int northboundWaitingDays, int southboundWaitingDays) {

	public boolean waitingDaysEqual(final @NonNull PanamaWaitingDaysTriple other) {
		return this.northboundWaitingDays == other.northboundWaitingDays //
				&& this.southboundWaitingDays == other.southboundWaitingDays;
	}
}

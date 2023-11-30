package com.mmxlabs.models.lng.transformer.extensions.panamaslots;

import java.util.Arrays;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;

public record SequencedWaitingDays(int[] changePoints, int[] northboundWaitingDays, int[] southboundWaitingDays) {

	@Override
	public int hashCode() {
		return Objects.hash(Arrays.hashCode(changePoints), Arrays.hashCode(northboundWaitingDays), Arrays.hashCode(southboundWaitingDays));
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other instanceof @NonNull SequencedWaitingDays otherWaitingDays) {
			return Arrays.equals(changePoints, otherWaitingDays.changePoints) 
					&& Arrays.equals(northboundWaitingDays, otherWaitingDays.northboundWaitingDays) //
					&& Arrays.equals(southboundWaitingDays, otherWaitingDays.southboundWaitingDays);
		}
		return false;
	}

}

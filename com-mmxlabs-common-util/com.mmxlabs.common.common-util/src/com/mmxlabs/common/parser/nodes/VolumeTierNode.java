/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.parser.nodes;

import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class VolumeTierNode extends AbstractMarkedUpNode {
	private final MarkedUpNode lowTier;
	private final MarkedUpNode highTier;
	private final double volume;
	private boolean isM3Volume;

	public VolumeTierNode(MarkedUpNode lowTier, MarkedUpNode highTier, double volume, boolean isM3Volume) {
		this.lowTier = lowTier;
		this.highTier = highTier;
		this.volume = volume;
		this.isM3Volume = isM3Volume;
	}

	@Override
	public List<MarkedUpNode> getChildren() {
		return List.of(lowTier, highTier);
	}

	public MarkedUpNode getLowTier() {
		return lowTier;
	}

	public MarkedUpNode getHighTier() {
		return highTier;
	}

	public double getVolume() {
		return volume;
	}

	public boolean isM3Volume() {
		return isM3Volume;
	}

}

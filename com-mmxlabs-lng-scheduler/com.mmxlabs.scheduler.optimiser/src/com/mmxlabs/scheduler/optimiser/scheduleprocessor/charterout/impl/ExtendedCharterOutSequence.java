/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.impl;
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IOptionsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;

public class ExtendedCharterOutSequence {
	private List<@NonNull IOptionsSequenceElement> sequence;
	private IPortTimesRecord portTimesRecord;
	private VoyageOptions laden;
	private VoyageOptions toCharter;
	private VoyageOptions fromCharter;
	private PortOptions charter;

	public List<@NonNull IOptionsSequenceElement> getSequence() {
		return sequence;
	}

	public void setSequence(List<@NonNull IOptionsSequenceElement> sequence) {
		this.sequence = sequence;
	}

	public IPortTimesRecord getPortTimesRecord() {
		return portTimesRecord;
	}

	public void setPortTimesRecord(IPortTimesRecord portTimesRecord) {
		this.portTimesRecord = portTimesRecord;
	}

	public VoyageOptions getLaden() {
		return laden;
	}

	public void setLaden(VoyageOptions laden) {
		this.laden = laden;
	}

	public VoyageOptions getToCharter() {
		return toCharter;
	}

	public void setToCharter(VoyageOptions toCharter) {
		this.toCharter = toCharter;
	}

	public VoyageOptions getFromCharter() {
		return fromCharter;
	}

	public void setFromCharter(VoyageOptions fromCharter) {
		this.fromCharter = fromCharter;
	}

	public PortOptions getCharter() {
		return charter;
	}

	public void setCharter(PortOptions charter) {
		this.charter = charter;
	}
}
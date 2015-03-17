package com.mmxlabs.scheduler.optimiser.scheduleprocessor.impl;

import java.util.List;

import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IOptionsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageOptions;

public class ExtendedCharterOutSequence {
	private List<IOptionsSequenceElement> sequence;
	private IPortTimesRecord portTimesRecord;
	private VoyageOptions laden;
	private VoyageOptions toCharter;
	private VoyageOptions fromCharter;
	private PortOptions charter;

	public List<IOptionsSequenceElement> getSequence() {
		return sequence;
	}
	public void setSequence(List<IOptionsSequenceElement> sequence) {
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
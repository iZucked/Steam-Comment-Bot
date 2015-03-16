package com.mmxlabs.scheduler.optimiser.scheduleprocessor.impl;

import com.mmxlabs.common.Triple;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProvider.CharterMarketOptions;

public class GeneratedCharterOutOption {
	private int charterStartTime;
	private int charterDuration;
	private CharterMarketOptions option = null;
	private IPort port;
	private Triple<Integer, String, Integer> toCharterPort;
	private Triple<Integer, String, Integer> fromCharterPort;
	private long maxCharteringRevenue = -1;
	
	public int getCharterStartTime() {
		return charterStartTime;
	}
	
	public void setCharterStartTime(int charterStartTime) {
		this.charterStartTime = charterStartTime;
	}
	public int getCharterDuration() {
		return charterDuration;
	}
	public void setCharterDuration(int charterDuration) {
		this.charterDuration = charterDuration;
	}
	public CharterMarketOptions getOption() {
		return option;
	}
	public void setOption(CharterMarketOptions option) {
		this.option = option;
	}
	public IPort getPort() {
		return port;
	}
	public void setPort(IPort port) {
		this.port = port;
	}
	public Triple<Integer, String, Integer> getToCharterPort() {
		return toCharterPort;
	}
	public void setToCharterPort(Triple<Integer, String, Integer> toCharterPort) {
		this.toCharterPort = toCharterPort;
	}
	public Triple<Integer, String, Integer> getFromCharterPort() {
		return fromCharterPort;
	}
	public void setFromCharterPort(Triple<Integer, String, Integer> fromCharterPort) {
		this.fromCharterPort = fromCharterPort;
	}
	public long getMaxCharteringRevenue() {
		return maxCharteringRevenue;
	}
	public void setMaxCharteringRevenue(long maxCharteringRevenue) {
		this.maxCharteringRevenue = maxCharteringRevenue;
	}
}
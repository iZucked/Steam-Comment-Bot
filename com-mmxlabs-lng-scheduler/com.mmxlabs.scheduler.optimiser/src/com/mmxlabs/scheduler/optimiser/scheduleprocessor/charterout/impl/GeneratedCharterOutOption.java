/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor.charterout.impl;
/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */

import com.mmxlabs.common.Triple;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.ERouteOption;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProvider.CharterMarketOptions;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortOptions;

public class GeneratedCharterOutOption {
	// Data for generating option
	private int charterStartTime;
	private int charterDuration;
	private CharterMarketOptions option = null;
	private IPort port;
	private Triple<Integer, ERouteOption, Integer> toCharterPort;
	private Triple<Integer, ERouteOption, Integer> fromCharterPort;
	private long maxCharteringRevenue = -1;

	// Data for setting cached options
	private PortOptions portOptions = null;
	private IVessel gcoVessel = null;
	private int gcoDuration = 0;
	private String gcoID = "";
	private IPort gcoPort = null;
	private IPort gcoEventStartPort = null;
	private IPort gcoEventEndPort = null;
	private long gcoEventHireOutRevenue = 0;
	private int gcoEventDurationHours = 0;

	private int gcoEventHeelPrice = 0;
	private int gcoEventHeelCV = 0;
	private long gcoEventHeelVolume = 0;

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

	public Triple<Integer, ERouteOption, Integer> getToCharterPort() {
		return toCharterPort;
	}

	public void setToCharterPort(Triple<Integer, ERouteOption, Integer> toCharterPort) {
		this.toCharterPort = toCharterPort;
	}

	public Triple<Integer, ERouteOption, Integer> getFromCharterPort() {
		return fromCharterPort;
	}

	public void setFromCharterPort(Triple<Integer, ERouteOption, Integer> fromCharterPort) {
		this.fromCharterPort = fromCharterPort;
	}

	public long getMaxCharteringRevenue() {
		return maxCharteringRevenue;
	}

	public void setMaxCharteringRevenue(long maxCharteringRevenue) {
		this.maxCharteringRevenue = maxCharteringRevenue;
	}

	public IVessel getGCOVessel() {
		return gcoVessel;
	}

	public void setGCOVessel(IVessel gcoVessel) {
		this.gcoVessel = gcoVessel;
	}

	public int getGCODuration() {
		return gcoDuration;
	}

	public void setGCODuration(int gcoDuration) {
		this.gcoDuration = gcoDuration;
	}

	public String getGCOID() {
		return gcoID;
	}

	public void setGCOID(String gcoID) {
		this.gcoID = gcoID;
	}

	public IPort getGCOPort() {
		return gcoPort;
	}

	public void setGCOPort(IPort gcoPort) {
		this.gcoPort = gcoPort;
	}

	public IPort getGCOEventStartPort() {
		return gcoEventStartPort;
	}

	public void setGCOEventStartPort(IPort gcoEventStartPort) {
		this.gcoEventStartPort = gcoEventStartPort;
	}

	public IPort getGCOEventEndPort() {
		return gcoEventEndPort;
	}

	public void setGCOEventEndPort(IPort gcoEventEndPort) {
		this.gcoEventEndPort = gcoEventEndPort;
	}

	public long getGCOEventHireOutRevenue() {
		return gcoEventHireOutRevenue;
	}

	public void setGCOEventHireOutRevenue(long gcoEventHireOutRevenue) {
		this.gcoEventHireOutRevenue = gcoEventHireOutRevenue;
	}

	public int getGCOEventDurationHours() {
		return gcoEventDurationHours;
	}

	public void setGCOEventDurationHours(int gcoEventDurationHours) {
		this.gcoEventDurationHours = gcoEventDurationHours;
	}

	public int getGCOEventHeelPrice() {
		return gcoEventHeelPrice;
	}

	public void setGCOEventHeelPrice(int gcoEventHeelPrice) {
		this.gcoEventHeelPrice = gcoEventHeelPrice;
	}

	public int getGCOEventHeelCV() {
		return gcoEventHeelCV;
	}

	public void setGCOEventHeelCV(int gcoEventHeelCV) {
		this.gcoEventHeelCV = gcoEventHeelCV;
	}

	public long getGCOEventHeelVolume() {
		return gcoEventHeelVolume;
	}

	public void setGCOEventHeelVolume(long gcoEventHeelVolume) {
		this.gcoEventHeelVolume = gcoEventHeelVolume;
	}

	public PortOptions getPortOptions() {
		return portOptions;
	}

	public void setPortOptions(PortOptions portOptions) {
		this.portOptions = portOptions;
	}

}
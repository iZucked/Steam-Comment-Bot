/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.optimiser.pairing;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 01/12/2017.
 */
public class SerializablePairingData {
	long[][] pnl = null;
	boolean[][] validCargoes = null;
	boolean[] optionalLoads = null;
	boolean[] optionalDischarges = null;
	List<Map<String, List<Integer>>> maxDischargeSlots = null;
	List<Map<String, List<Integer>>> minDischargeSlots = null;
	List<Map<String, List<Integer>>> maxLoadSlots = null;
	List<Map<String, List<Integer>>> minLoadSlots = null;

	public SerializablePairingData() {

	}

	public SerializablePairingData(long[][] pnl, boolean[][] validCargoes, boolean[] optionalLoads, boolean[] optionalDischarges) {
		this.pnl = pnl;
		this.validCargoes = validCargoes;
		this.optionalLoads = optionalLoads;
		this.optionalDischarges = optionalDischarges;
	}

	public SerializablePairingData(String path) throws IOException {
		this.pnl = getPrestoredData(Paths.get(path, "profit.lt").toString());

		// booleans
		this.validCargoes = getPrestoredData(Paths.get(path, "constraints.lt").toString());
		this.optionalLoads = getPrestoredData(Paths.get(path, "loads.lt").toString());
		this.optionalDischarges = getPrestoredData(Paths.get(path, "discharges.lt").toString());
	}

	public long[][] getPnl() {
		return pnl;
	}

	public void setPnl(long[][] pnl) {
		this.pnl = pnl;
	}

	public boolean[][] getValidCargoes() {
		return validCargoes;
	}

	public void setValidCargoes(boolean[][] restricted) {
		this.validCargoes = restricted;
	}

	public boolean[] getOptionalLoads() {
		return optionalLoads;
	}

	public void setOptionalLoads(boolean[] optionalLoads) {
		this.optionalLoads = optionalLoads;
	}

	public boolean[] getOptionalDischarges() {
		return optionalDischarges;
	}

	public void setOptionalDischarges(boolean[] optionalDischarges) {
		this.optionalDischarges = optionalDischarges;
	}

	public List<Map<String, List<Integer>>> getMaxDischargeSlots() {
		return maxDischargeSlots;
	}

	public void setMaxDischargeSlots(List<Map<String, List<Integer>>> maxSlotsConstraints) {
		this.maxDischargeSlots = maxSlotsConstraints;
	}

	public List<Map<String, List<Integer>>> getMinDischargeSlots() {
		return minDischargeSlots;
	}

	public void setMinDischargeSlots(List<Map<String, List<Integer>>> minDischargeSlots) {
		this.minDischargeSlots = minDischargeSlots;
	}

	public List<Map<String, List<Integer>>> getMinLoadSlots() {
		return minLoadSlots;
	}

	public void setMinLoadSlots(List<Map<String, List<Integer>>> minLoadSlots) {
		this.minLoadSlots = minLoadSlots;
	}

	public List<Map<String, List<Integer>>> getMaxLoadSlots() {
		return maxLoadSlots;
	}

	public void setMaxLoadSlots(List<Map<String, List<Integer>>> maxLoadSlots) {
		this.maxLoadSlots = maxLoadSlots;
	}

	public static <E> E getPrestoredData(String path) throws IOException {
		E readCase = null;
		try (FileInputStream streamIn = new FileInputStream(path)) {
			try (ObjectInputStream objectinputstream = new ObjectInputStream(streamIn)) {
				readCase = (E) objectinputstream.readObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException();
		}
		return readCase;
	}

	public boolean validate() {
		if (getPnl() != null && getOptionalDischarges() != null && getOptionalLoads() != null && getValidCargoes() != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "" + "PNL size:" + getPnl().length;
	}

}

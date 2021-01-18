/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.actions.anonymisation;

public class AnonymisationRecord {
	public String oldName;
	public String newName;
	public AnonymisationRecordType type;
	
	public AnonymisationRecord(String oldName, String newName, String type) {
		this.oldName = oldName;
		this.newName = newName;
		switch (type) {
		case "BuyID":
			this.type = AnonymisationRecordType.BuyID;
			break;
		case "SellID":
			this.type = AnonymisationRecordType.SellID;
			break;
		case "BuyContractID":
			this.type = AnonymisationRecordType.BuyContractID;
			break;
		case "SellContractID":
			this.type = AnonymisationRecordType.SellContractID;
			break;
		case "VesselID":
			this.type = AnonymisationRecordType.VesselID;
			break;
		case "VesseShortID":
			this.type = AnonymisationRecordType.VesselShortID;
			break;
		default:
			throw new IllegalStateException("Type must be provided. Allowed types are: LS for load slot; DS for discharge slot; \n PC for purchase contract; SC for sales contract; \n VS for vessel.");
		}
	}
	
	public AnonymisationRecord(String oldName, String newName, AnonymisationRecordType type) {
		this.oldName = oldName;
		this.newName = newName;
		this.type = type;
	}
	
	public AnonymisationRecord() {
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public AnonymisationRecordType getType() {
		return type;
	}

	public void setType(AnonymisationRecordType type) {
		this.type = type;
	}
}

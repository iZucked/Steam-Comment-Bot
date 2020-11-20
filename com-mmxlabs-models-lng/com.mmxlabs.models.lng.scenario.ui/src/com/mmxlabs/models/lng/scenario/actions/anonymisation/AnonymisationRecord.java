package com.mmxlabs.models.lng.scenario.actions.anonymisation;

public class AnonymisationRecord {
	public String oldName;
	public String newName;
	public AnonymisationRecordType type;
	
	public AnonymisationRecord(String oldName, String newName, String type) {
		this.oldName = oldName;
		this.newName = newName;
		switch (type) {
		case "LS":
			this.type = AnonymisationRecordType.LS;
			break;
		case "DS":
			this.type = AnonymisationRecordType.DS;
			break;
		case "PC":
			this.type = AnonymisationRecordType.PC;
			break;
		case "SC":
			this.type = AnonymisationRecordType.SC;
			break;
		case "VN":
			this.type = AnonymisationRecordType.VN;
			break;
		case "VS":
			this.type = AnonymisationRecordType.VS;
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

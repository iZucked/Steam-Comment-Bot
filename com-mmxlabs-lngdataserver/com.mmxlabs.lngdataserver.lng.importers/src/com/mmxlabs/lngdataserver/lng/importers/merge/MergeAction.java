/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.merge;

public class MergeAction {
	MergeType mergeType;
	String sourceName;
	String targetName;
	
	/**
	 * @param mergeType
	 * @param sourceName
	 * @param targetName
	 */
	public MergeAction(MergeType mergeType, String sourceName, String targetName) {
		super();
		this.mergeType = mergeType;
		this.sourceName = sourceName;
		this.targetName = targetName;
	}

	/**
	 * @return the mergeType
	 */
	public MergeType getMergeType() {
		return mergeType;
	}

	/**
	 * @param mergeType the mergeType to set
	 */
	public void setMergeType(MergeType mergeType) {
		this.mergeType = mergeType;
	}

	/**
	 * @return the sourceName
	 */
	public String getSourceName() {
		return sourceName;
	}

	/**
	 * @param sourceName the sourceName to set
	 */
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	/**
	 * @return the targetName
	 */
	public String getTargetName() {
		return targetName;
	}

	/**
	 * @param targetName the targetName to set
	 */
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}	
}

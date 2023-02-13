/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lngdataserver.lng.importers.merge;

import org.eclipse.emf.ecore.EObject;

class MergeMapping {
	EObject sourceObject;
	String sourceName;
	String targetName;
	String info = "";
	
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

	/**
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @param info the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * @return the sourceObject
	 */
	public EObject getSourceObject() {
		return sourceObject;
	}

	/**
	 * @param sourceObject the sourceObject to set
	 */
	public void setSourceObject(EObject sourceObject) {
		this.sourceObject = sourceObject;
	}
	
	@Override
	public String toString() {
		return sourceName;
	}
}
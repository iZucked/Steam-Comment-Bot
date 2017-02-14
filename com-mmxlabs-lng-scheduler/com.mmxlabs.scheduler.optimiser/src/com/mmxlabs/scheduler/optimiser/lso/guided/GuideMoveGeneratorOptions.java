package com.mmxlabs.scheduler.optimiser.lso.guided;

public class GuideMoveGeneratorOptions {

	private boolean strictOptional = true;
	private boolean checkingMove = false;

	// Should the moves which remove elements leave the rest of the segment or not.
	private boolean permitPartialSegments = false;

	public boolean isPermitPartialSegments() {
		return permitPartialSegments;
	}

	public void setPermitPartialSegments(boolean permitPartialSegments) {
		this.permitPartialSegments = permitPartialSegments;
	}

	public void setCheckingMove(boolean checkingMove) {
		this.checkingMove = checkingMove;
	}

	private boolean extendSearch = false;

	private boolean ignoreUsedElements = false;
	private int num_tries = 30;

	public int getNum_tries() {
		return num_tries;
	}

	public void setNum_tries(int num_tries) {
		this.num_tries = num_tries;
	}

	public boolean isStrictOptional() {
		return strictOptional;
	}

	public void setStrictOptional(boolean strictOptional) {
		this.strictOptional = strictOptional;
	}

	public boolean isExtendSearch() {
		return extendSearch;
	}

	public void setExtendSearch(boolean extendSearch) {
		this.extendSearch = extendSearch;
	}

	public boolean isIgnoreUsedElements() {
		return ignoreUsedElements;
	}

	public void setIgnoreUsedElements(boolean ignoreUsedElements) {
		this.ignoreUsedElements = ignoreUsedElements;
	}

	public boolean isCheckingMove() {
		return checkingMove;
	}

	public static GuideMoveGeneratorOptions createDefault() {
		return new GuideMoveGeneratorOptions();
	}
}

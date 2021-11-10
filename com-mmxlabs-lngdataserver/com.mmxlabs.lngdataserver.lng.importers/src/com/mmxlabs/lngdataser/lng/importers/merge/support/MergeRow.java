package com.mmxlabs.lngdataser.lng.importers.merge.support;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.mmxcore.NamedObject;

public class MergeRow<T extends NamedObject> {

	@NonNull
	private final MergePair<T> mergePair;
	@NonNull
	private final String info;

	public MergeRow(@NonNull final MergePair<T> mergePair, @NonNull final String info) {
		this.mergePair = mergePair;
		this.info = info;
	}

	@NonNull
	public MergePair<T> getMergePair() {
		return this.mergePair;
	}

	@NonNull
	public String getInfo() {
		return this.info;
	}

	public boolean hasDefaultMapping() {
		return mergePair.hasDefaultMapping();
	}

	public int getIndex() {
		return mergePair.getIndex();
	}
}

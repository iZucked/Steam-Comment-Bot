package com.mmxlabs.lngdataser.lng.importers.merge.support;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.scenario.model.util.ScenarioElementNameHelper;
import com.mmxlabs.models.mmxcore.NamedObject;

public class MergePair<T extends NamedObject> {
	@NonNull
	private final T from;
	@NonNull
	private MergeOption to;
	@Nullable
	private final T defaultTarget;

	public MergePair(@NonNull final T from, @NonNull final MergeOption to) {
		this.from = from;
		this.to = to;
		this.defaultTarget = to instanceof MappingOption ? ((MappingOption<T>) to).getElement() : null; 
	}

	@NonNull
	public T getFrom() {
		return this.from;
	}

	public void setTo(@NonNull final MergeOption to) {
		this.to = to;
	}

	public MergeOption getTo() {
		return to;
	}

	@Override
	public String toString() {
		return String.format("MergePair<%s, %s>", ScenarioElementNameHelper.getName(from, "<Unknown>"), to.toString());
	}

	public boolean hasDefaultMapping() {
		return this.defaultTarget != null;
	}

	public int getIndex() {
		final int offset = hasDefaultMapping() ? 1 : 2;
		return to.getIndex(offset);
	}

	public T getDefaultTarget() {
		return defaultTarget;
	}
}

package com.mmxlabs.optimiser.common.dcproviders.impl.indexed;

import com.mmxlabs.common.indexedobjects.AutoSizingArrayList;
import com.mmxlabs.common.indexedobjects.IIndexedObject;
import com.mmxlabs.optimiser.common.dcproviders.IOrderedSequenceElementsDataComponentProviderEditor;

public class IndexedOrderedSequenceElementsEditor<T extends IIndexedObject> implements
		IOrderedSequenceElementsDataComponentProviderEditor<T> {

	private final AutoSizingArrayList<T> successors = new AutoSizingArrayList<T>();
	private final AutoSizingArrayList<T> predecessors = new AutoSizingArrayList<T>();
	
	private final String name;
	
	public IndexedOrderedSequenceElementsEditor(String name) {
		super();
		this.name = name;
	}

	@Override
	public T getNextElement(T previousElement) {
		return successors.maybeGet(previousElement);
	}

	@Override
	public T getPreviousElement(final T nextElement) {
		return predecessors.maybeGet(nextElement);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void dispose() {
		successors.clear();
		predecessors.clear();
	}

	@Override
	public void setElementOrder(final T previousElement, final T nextElement) {
		successors.set(previousElement, nextElement);
		predecessors.set(nextElement, previousElement);
	}

}

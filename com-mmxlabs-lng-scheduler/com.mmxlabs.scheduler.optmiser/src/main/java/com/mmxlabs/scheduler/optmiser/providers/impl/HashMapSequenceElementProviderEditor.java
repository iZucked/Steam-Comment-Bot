package com.mmxlabs.scheduler.optmiser.providers.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mmxlabs.scheduler.optmiser.components.ICargo;
import com.mmxlabs.scheduler.optmiser.components.IPort;
import com.mmxlabs.scheduler.optmiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optmiser.providers.ISequenceElementProviderEditor;

public final class HashMapSequenceElementProviderEditor implements
		ISequenceElementProviderEditor {

	private final String name;

	private final List<ISequenceElement> sequenceElements;
	private final List<ISequenceElement> unmodifiableSequenceElements;

	private final Map<ICargo, Map<IPort, ISequenceElement>> cargoPortElementMap;
	
	public HashMapSequenceElementProviderEditor(final String name) {
		this.name = name;
		sequenceElements = new LinkedList<ISequenceElement>();
		// Create wrapper around real list
		unmodifiableSequenceElements = Collections.unmodifiableList(sequenceElements);
		
		portCargoElementMap = new HashMap<ICargo, Map<IPort,ISequenceElement>>();
	}

	@Override
	public void setSequenceElement(ICargo cargo, IPort port,
			ISequenceElement element) {

		if (sequenceElements.contains(element)) {
			throw new RuntimeException("Element has already been added");
		}
		
		if (ca)
		
		sequenceElements.add(element);
		
		
		
	}

	@Override
	public ISequenceElement getSequenceElement(ICargo cargo, IPort port) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ISequenceElement> getSequenceElements() {
		return unmodifiableSequenceElements;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		return name;
	}
}

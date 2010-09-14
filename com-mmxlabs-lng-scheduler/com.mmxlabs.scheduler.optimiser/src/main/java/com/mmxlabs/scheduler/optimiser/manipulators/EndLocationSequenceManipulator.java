package com.mmxlabs.scheduler.optimiser.manipulators;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.PortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProviderEditor;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;


/**
 * The {@link EndLocationSequenceManipulator} replaces the end location with
 * another location. The replacement location is chosen using one of the possible
 * {@link EndLocationSequenceManipulator.EndLocationRule} values. The class needs to 
 * be given an implementation of {@link EndLocationSequenceManipulator.IElementFactory},
 * which it uses to construct the dummy sequence elements to be "swapped out".
 * 
 * This is slightly ugly, because it has to directly alter the IOptimisationData's 
 * data component providers, but hopefully using the provided factory to create dummy elements
 * will ensure sufficient insulation from any awful consequences.
 * 
 * Actually, this is more difficult than it looks; all the auxiliary data structures
 * for an element need to be created and updated accordingly; 
 * 
 * @author Simon Goodall, significantly modified by Tom Hinton
 * 
 * @param <T>
 *            Sequence element type.
 */
public final class EndLocationSequenceManipulator<T> implements
		ISequencesManipulator<T> {

	/**
	 * An interface used to allow the {@link EndLocationSequenceManipulator}
	 * to create new dummy sequence elements to swap out for genuine end elements.
	 * 
	 * @author hinton
	 *
	 * @param <T>
	 */
	public interface IElementFactory<T> {
		public T createElement();
	}
	
	/**
	 * An enum of the different end location rules that can be applied.
	 * 
	 * @author Simon Goodall
	 * 
	 */
	public static enum EndLocationRule {
		/**
		 * No special rules to apply.
		 */
		NONE,

		/**
		 * Return to the first load port.
		 */
		RETURN_TO_FIRST_LOAD,

		/**
		 * Return to the last load port visited.
		 */
		RETURN_TO_LAST_LOAD,
	}

	private IPortTypeProvider<T> portTypeProvider;
	
	/**
	 * This is needed both to find out the location to which we are returning,
	 * and to set that as the location associated with the new dummy sequence element.
	 */
	private IPortProviderEditor portProviderEditor;

	private IPortSlotProviderEditor<T> portSlotProviderEditor;
	
	private final Map<IResource, EndLocationRule> ruleMap = 
		new HashMap<IResource, EndLocationSequenceManipulator.EndLocationRule>();

	private IElementFactory<T> elementFactory = null;

	private final Map<IResource, Pair<T, PortSlot>> dummyElements = 
		new HashMap<IResource, Pair<T, PortSlot>>();

	public EndLocationSequenceManipulator(final IElementFactory<T> elementFactory) {
		this.elementFactory = elementFactory;
	}

	@Override
	public void manipulate(final IModifiableSequences<T> sequences) {
		assert portTypeProvider != null;
		
		// Loop through each sequence in turn and manipulate
		for (final Map.Entry<IResource, IModifiableSequence<T>> entry : sequences
				.getModifiableSequences().entrySet()) {
			manipulate(entry.getKey(), entry.getValue());
		}
	}

	public void manipulate(final IResource resource,
			final IModifiableSequence<T> sequence) {

		final EndLocationRule rule = ruleMap.get(resource);
		if (rule == null) return;
		switch (rule) {
		case RETURN_TO_FIRST_LOAD:
			adjustLastElement(resource, sequence,
					getFirstLoadElement(sequence));
			break;
		case RETURN_TO_LAST_LOAD:
			adjustLastElement(resource, sequence,
					getLastLoadElement(sequence));
			break;
		default:
			break;
		}
	}

	/**
	 * Find and return the last element in the sequence with {@link PortType} PortType.Load
	 * 
	 * @param sequence
	 */
	public T getLastLoadElement(final IModifiableSequence<T> sequence) {
		/*
		 * Search for the last load port
		 */
		T t = null;
		for (final T element : sequence) {
			final PortType type = portTypeProvider.getPortType(element);
			// Record last load port
			if (type == PortType.Load) {
				t = element;
			}
		}
		return t;
//		adjustLastElement(resource, sequence, t);
	}

	/**
	 * Swap the dummy element in for the given sequence, and set its location to the
	 * given port.
	 * @param resource 
	 * 
	 * @param sequence
	 * @param location
	 */
	private final void adjustLastElement(final IResource resource, 
			final IModifiableSequence<T> sequence, 
			final T returnElement) {
		if (returnElement == null) return;
		/*
		 * Look up the port we are returning to, and then set that as the port
		 * for the dummy element.
		 */
		final IPort returnPort = portProviderEditor.getPortForElement(returnElement);
		final Pair<T, PortSlot> dummyElement = dummyElements.get(resource);
		portProviderEditor.setPortForElement(returnPort, dummyElement.getFirst());
		dummyElement.getSecond().setPort(returnPort);
		
		/*
		 * Replace the final sequence element with the dummy
		 */
		sequence.set(sequence.size()-1, dummyElement.getFirst());
	}
	
	/**
	 * Find and return the first element in the sequence with {@link PortType} PortType.Load
	 * 
	 * @param sequence
	 */
	public T getFirstLoadElement(final IModifiableSequence<T> sequence) {
		T t = null;
		for (final T element : sequence) {
			final PortType type = portTypeProvider.getPortType(element);
			// Record first load port
			if (type == PortType.Load) {
				t = element;
				break;
			}
		}
		return t;
	}
	
	public IPortTypeProvider<T> getPortTypeProvider() {
		return portTypeProvider;
	}

	public void setPortTypeProvider(final IPortTypeProvider<T> portTypeProvider) {
		this.portTypeProvider = portTypeProvider;
	}

	public IPortProviderEditor getPortProviderEditor() {
		return portProviderEditor;
	}

	public void setPortProviderEditor(IPortProviderEditor portProviderEditor) {
		this.portProviderEditor = portProviderEditor;
	}

	public IPortSlotProviderEditor<T> getPortSlotProviderEditor() {
		return portSlotProviderEditor;
	}

	public void setPortSlotProviderEditor(
			IPortSlotProviderEditor<T> portSlotProviderEditor) {
		this.portSlotProviderEditor = portSlotProviderEditor;
	}

	/**
	 * Specify the {@link EndLocationRule} for this {@link IResource}
	 * 
	 * @param resource
	 * @param rule
	 */
	public void setEndLocationRule(final IResource resource,
			final EndLocationRule rule) {
		if (ruleMap.containsKey(resource) == false) {
			/* 
			 * Set up a dummy element for this resource,
			 * because we might need it
			 */
			T element = elementFactory.createElement();
			PortSlot slot = new PortSlot();
			slot.setId(resource.getName() + "-return");
			portSlotProviderEditor.setPortSlot(element, slot);
			dummyElements.put(resource, new Pair<T, PortSlot>(element, slot));
		}
		ruleMap.put(resource, rule);
	}

	/**
	 * Returns the {@link EndLocationRule} set for this {@link IResource}.
	 * Returns {@link EndLocationRule#NONE} if nothing has been set.
	 * 
	 * @param resource
	 * @return
	 */
	public EndLocationRule getEndLocationRule(final IResource resource) {
		if (ruleMap.containsKey(resource)) {
			return ruleMap.get(resource);
		}
		return EndLocationRule.NONE;
	}

	@Override
	public void dispose() {
		ruleMap.clear();
	}
}

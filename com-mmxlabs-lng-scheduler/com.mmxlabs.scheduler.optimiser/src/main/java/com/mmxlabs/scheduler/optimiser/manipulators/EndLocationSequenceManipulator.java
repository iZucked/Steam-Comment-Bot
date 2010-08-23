package com.mmxlabs.scheduler.optimiser.manipulators;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * The {@link EndLocationSequenceManipulator} replaces the end location with
 * another location. The end location must be a pre-defined "virtual" sequence
 * element. Each {@link IResource} can applied only one {@link EndLocationRule}.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type.
 */
public final class EndLocationSequenceManipulator<T> implements
		ISequencesManipulator<T> {

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

	private final Map<T, T> elementMap = new HashMap<T, T>();

	private final Map<IResource, EndLocationRule> ruleMap = new HashMap<IResource, EndLocationSequenceManipulator.EndLocationRule>();

	private final T virtualLocation;

	public EndLocationSequenceManipulator(final T virtualLocation) {
		this.virtualLocation = virtualLocation;
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
		switch (rule) {
		case RETURN_TO_FIRST_LOAD:
			returnToFirstLoadPort(resource, sequence);
			break;
		case RETURN_TO_LAST_LOAD:
			returnToLastLoadPort(resource, sequence);
			break;
		default:
			break;
		}
	}

	/**
	 * Apply the {@link EndLocationRule#RETURN_TO_LAST_LOAD} rule. Iterate
	 * through the sequence looking for {@link PortType#Load} and record the
	 * last one in the sequence. Use this element as the key for the end
	 * location replacement.
	 * 
	 * @param resource
	 * @param sequence
	 */
	public void returnToLastLoadPort(final IResource resource,
			final IModifiableSequence<T> sequence) {

		// Check last element is the virtual loc
		final T last = sequence.get(sequence.size() - 1);
		if (last != virtualLocation) {
			return;
		}

		T t = null;
		for (final T element : sequence) {
			final PortType type = portTypeProvider.getPortType(element);
			// Record last load port
			if (type == PortType.Load) {
				t = element;
			}
		}

		if (elementMap.containsKey(t)) {
			final T newEndLocation = elementMap.get(t);
			sequence.set(sequence.size() - 1, newEndLocation);
		} else {
			// Log a warning?
		}
	}

	/**
	 * Apply the {@link EndLocationRule#RETURN_TO_FIRST_LOAD} rule. Iterate
	 * through the sequence looking for {@link PortType#Load} and record the
	 * last one in the sequence. Use this element as the key for the end
	 * location replacement.
	 * 
	 * @param resource
	 * @param sequence
	 */
	public void returnToFirstLoadPort(final IResource resource,
			final IModifiableSequence<T> sequence) {

		// Check last element is the virtual loc
		final T last = sequence.get(sequence.size() - 1);
		if (last != virtualLocation) {
			return;
		}

		T t = null;
		for (final T element : sequence) {
			final PortType type = portTypeProvider.getPortType(element);
			// Record first load port
			if (type == PortType.Load) {
				t = element;
				break;
			}
		}

		if (elementMap.containsKey(t)) {
			final T newEndLocation = elementMap.get(t);
			sequence.set(sequence.size() - 1, newEndLocation);
		} else {
			// Log a warning?
		}
	}
	
	public IPortTypeProvider<T> getPortTypeProvider() {
		return portTypeProvider;
	}

	public void setPortTypeProvider(final IPortTypeProvider<T> portTypeProvider) {
		this.portTypeProvider = portTypeProvider;
	}

	/**
	 * Specify that if the given sequence element is the element used to specify
	 * the end location, then use the given end location as the new end location
	 * 
	 * @param element
	 * @param endLocation
	 */
	public void setElementMapping(final T element, final T endLocation) {
		elementMap.put(element, endLocation);
	}

	/**
	 * Returns the end location mapped to this element.
	 * 
	 * @param element
	 * @return
	 */
	public T getElementMapping(final T element) {
		if (elementMap.containsKey(element)) {
			return elementMap.get(element);
		}
		return null;
	}

	/**
	 * Specify the {@link EndLocationRule} for this {@link IResource}
	 * 
	 * @param resource
	 * @param rule
	 */
	public void setEndLocationRule(final IResource resource,
			final EndLocationRule rule) {
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

	public T getVirtualLocation() {
		return virtualLocation;
	}

	@Override
	public void dispose() {
		elementMap.clear();
		ruleMap.clear();
	}
}

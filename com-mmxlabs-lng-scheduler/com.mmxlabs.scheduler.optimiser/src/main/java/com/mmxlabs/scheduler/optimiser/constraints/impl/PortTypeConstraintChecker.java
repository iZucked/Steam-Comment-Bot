package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collection;
import java.util.List;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * {@link IConstraintChecker} implementation to enforce correct ordering of port
 * types. Specifically:
 * 
 * <pre>
 *  * {@link PortType#Start} can only occur at the start of a {@link ISequence}
 *  * {@link PortType#End} can only occur at the start of a {@link ISequence}
 *  * {@link PortType#Load} must be followed by a {@link PortType#Discharge}
 *  * {@link PortType#Load} cannot be followed by another {@link PortType#Load}
 *  * {@link PortType#Discharge} cannot be followed by another {@link PortType#Discharge}
 *  * {@link PortType#Waypoint} can occur anywhere in the sequence, including between {@link PortType#Load} and {@link PortType#Discharge}.
 *  * {@link PortType#DryDock} and {@link PortType#Other} cannot occur between a {@link PortType#Load} and a {@link PortType#Discharge}.
 *  * {@link PortType#Unknown} should not be seen
 *   
 * @author Simon Goodall
 * 
 * @param <T> Sequence element type
 */
public final class PortTypeConstraintChecker<T> implements
		IPairwiseConstraintChecker<T> {

	private final String name;

	private final String key;

	private IPortTypeProvider<T> portTypeProvider;

	public PortTypeConstraintChecker(final String name, final String key) {
		this.name = name;
		this.key = key;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(final ISequences<T> sequences) {

		return checkConstraints(sequences, null);
	}

	@Override
	public boolean checkConstraints(final ISequences<T> sequences,
			final List<String> messages) {

		if (portTypeProvider == null) {
			// Cannot check port if there is no port type provider
			return true;
		}

		final Collection<ISequence<T>> sequencesCollection = sequences
				.getSequences().values();
		for (final ISequence<T> s : sequencesCollection) {
			if (!checkSequence(s, messages)) {
				return false;
			}
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setOptimisationData(final IOptimisationData<T> optimisationData) {

		setPortTypeProvider(optimisationData.getDataComponentProvider(key,
				IPortTypeProvider.class));
	}
	
	/**
	 * Check ISequence for {@link PortType} ordering violations.
	 * 
	 * @param sequence
	 * @param messages
	 * @return
	 */
	public final boolean checkSequence(final ISequence<T> sequence,
			final List<String> messages) {

		boolean seenLoad = false;
		boolean seenDischarge = false;

		T previous = null;
		PortType previousType = null;
		for (final T t : sequence) {
			final PortType type = portTypeProvider.getPortType(t);
			if (previous == null) {
				if (type != PortType.Start) {
					// Must start with a Start type.
					if (messages != null)
						messages.add("Sequence must begin with PortType.Start");
					return false;
				}
			} else {
				if (previousType == PortType.End) {
					// Cannot have two elements with an End type.
					if (messages != null)
						messages.add("Sequence can only have one PortType.End");
					return false;
				}
			}

			
			
			switch (type) {
			case Discharge:
				if (seenDischarge) {
					// Cannot have two discharges in a row
					if (messages != null)
						messages.add("Cannot have two PortType.Discharge in a row");
					return false;
				}
				if (!seenLoad) {
					// Cannot discharge without loading
					if (messages != null)
						messages.add("Cannot have PortType.Discharge without a PortType.Load");
					return false;
				}
				seenLoad = false;
				seenDischarge = true;
				break;
			case Load:
				if (seenLoad) {
					// Cannot have two loads in a row
					if (messages != null)
						messages.add("Cannot have two PortType.Load in a row");
					return false;
				}
				seenLoad = true;
				seenDischarge = false;
				break;
			case Start:
				if (previous != null) {
					// Start must occur at the start
					if (messages != null)
						messages.add("PortType.Start must occur at beginning of Sequence");
					return false;
				}
				break;
			case Waypoint:
				break;
			case DryDock:
			case Other:
				if (seenLoad) {
					// Cannot insert between load and discharge
					if (messages != null)
						messages.add("Cannot insert "
								+ type
								+ " between PortType.Load and PortType.Discharge");
					return false;
				}
				break;
			case End:
				break;
			default:
				if (messages != null)
					messages.add("Unsupported PortType");
				// Unsupported type
				return false;

			}

			previous = t;
			previousType = type;
		}

		if (previousType != PortType.End) {
			// Must end with an End type.
			if (messages != null)
				messages.add("Sequence must end with PortType.End");
			return false;
		}

		return true;
	}

	public IPortTypeProvider<T> getPortTypeProvider() {
		return portTypeProvider;
	}

	public void setPortTypeProvider(IPortTypeProvider<T> portTypeProvider) {
		this.portTypeProvider = portTypeProvider;
	}

	@Override
	public boolean checkPairwiseConstraint(T first, T second, IResource resource) {
		final PortType firstType = portTypeProvider.getPortType(first);
		final PortType secondType = portTypeProvider.getPortType(second);
		
		//check the legality of this sequencing decision
		//End can't come before anything and Start can't come after anything
		if (firstType.equals(PortType.End) || secondType.equals(PortType.Start)) return false;
		
		if (firstType.equals(PortType.Start) && secondType.equals(PortType.Discharge)) return false; //first port should be a load slot (TODO is this true?)
		
		//load must precede discharge or waypoint, but nothing else
		if (firstType.equals(PortType.Load)) return (secondType.equals(PortType.Discharge) || secondType.equals(PortType.Waypoint));
		
		//discharge may precede anything but Discharge (and start, but we already did that)
		if (firstType.equals(PortType.Discharge) &&
				secondType.equals(PortType.Discharge)) return false; 
		
		return true;
	}
}

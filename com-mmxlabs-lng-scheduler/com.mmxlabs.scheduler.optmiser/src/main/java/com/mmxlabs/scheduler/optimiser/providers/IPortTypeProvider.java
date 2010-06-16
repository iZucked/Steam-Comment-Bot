package com.mmxlabs.scheduler.optimiser.providers;

import com.mmxlabs.optimiser.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.fitness.IFitnessCore;
import com.mmxlabs.optimiser.scenario.IDataComponentProvider;

/**
 * {@link IDataComponentProvider} definition providing a {@link PortType} for
 * sequence elements. This is intended to be combined with a
 * {@link IConstraintChecker} or {@link IFitnessCore} to enfore a particular
 * sequencing of port types.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IPortTypeProvider<T> extends IDataComponentProvider {

	enum PortType {
		Unknown, Load, Discharge, DryDock, Waypoint, Other;
	}

	/**
	 * Return the type of port for the given sequence element. Returns
	 * {@link PortType#Unknown} if not specified.
	 * 
	 * @param sequenceElement
	 * @return
	 */
	PortType getPortType(T sequenceElement);
}

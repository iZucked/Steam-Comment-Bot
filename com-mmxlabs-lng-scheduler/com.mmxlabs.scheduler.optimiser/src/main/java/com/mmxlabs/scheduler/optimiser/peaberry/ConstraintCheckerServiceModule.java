package com.mmxlabs.scheduler.optimiser.peaberry;

import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.util.TypeLiterals;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.mmxlabs.optimiser.common.constraints.OrderedSequenceElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortExclusionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortTypeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TravelTimeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.VirtualVesselConstraintCheckerFactory;

/**
 * Guice module using Peaberry to register {@link IConstraintCheckerFactory} instances as services.
 * 
 * @author Simon Goodall
 * 
 */
public class ConstraintCheckerServiceModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(TypeLiterals.export(IConstraintCheckerFactory.class)).annotatedWith(Names.named(ResourceAllocationConstraintCheckerFactory.class.getCanonicalName())).toProvider(
				Peaberry.service(new ResourceAllocationConstraintCheckerFactory(SchedulerConstants.DCP_resourceAllocationProvider)).export());

		bind(TypeLiterals.export(IConstraintCheckerFactory.class)).annotatedWith(Names.named(OrderedSequenceElementsConstraintCheckerFactory.class.getCanonicalName())).toProvider(
				Peaberry.service(new OrderedSequenceElementsConstraintCheckerFactory(SchedulerConstants.DCP_orderedElementsProvider)).export());

		bind(TypeLiterals.export(IConstraintCheckerFactory.class)).annotatedWith(Names.named(PortExclusionConstraintCheckerFactory.class.getCanonicalName())).toProvider(
				Peaberry.service(new PortExclusionConstraintCheckerFactory(SchedulerConstants.DCP_portExclusionProvider, SchedulerConstants.DCP_vesselProvider, SchedulerConstants.DCP_portProvider))
						.export());

		bind(TypeLiterals.export(IConstraintCheckerFactory.class)).annotatedWith(Names.named(PortTypeConstraintCheckerFactory.class.getCanonicalName())).toProvider(
				Peaberry.service(new PortTypeConstraintCheckerFactory(SchedulerConstants.DCP_portTypeProvider, SchedulerConstants.DCP_vesselProvider)).export());

		bind(TypeLiterals.export(IConstraintCheckerFactory.class)).annotatedWith(Names.named(TravelTimeConstraintCheckerFactory.class.getCanonicalName())).toProvider(
				Peaberry.service(new TravelTimeConstraintCheckerFactory()).export());

		bind(TypeLiterals.export(IConstraintCheckerFactory.class)).annotatedWith(Names.named(VirtualVesselConstraintCheckerFactory.class.getCanonicalName())).toProvider(
				Peaberry.service(new VirtualVesselConstraintCheckerFactory(SchedulerConstants.DCP_vesselProvider)).export());
	}
}

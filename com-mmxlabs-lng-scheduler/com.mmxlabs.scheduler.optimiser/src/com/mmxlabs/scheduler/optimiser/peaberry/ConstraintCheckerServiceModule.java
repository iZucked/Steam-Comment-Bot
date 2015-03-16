/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.peaberry;

import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.util.TypeLiterals;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.mmxlabs.optimiser.common.constraints.OrderedSequenceElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.ContractCvConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.DifferentSTSVesselsConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortCvCompatibilityConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortExclusionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortTypeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.ShippingHoursRestrictionCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.SlotGroupCountConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.SpotToSpotConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TimeSortConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TravelTimeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.VirtualVesselConstraintCheckerFactory;

/**
 * Guice module using Peaberry to register {@link IConstraintCheckerFactory} instances as services.
 * 
 * @author Simon Goodall
 * 
 */
public class ConstraintCheckerServiceModule extends AbstractModule {
	public static final boolean USE_SPOT_TO_SPOT_CONSTRAINT = false;

	@Override
	protected void configure() {

		bind(TypeLiterals.export(IConstraintCheckerFactory.class)).annotatedWith(Names.named(ResourceAllocationConstraintCheckerFactory.class.getCanonicalName())).toProvider(
				Peaberry.service(new ResourceAllocationConstraintCheckerFactory()).export());

		bind(TypeLiterals.export(IConstraintCheckerFactory.class)).annotatedWith(Names.named(OrderedSequenceElementsConstraintCheckerFactory.class.getCanonicalName())).toProvider(
				Peaberry.service(new OrderedSequenceElementsConstraintCheckerFactory()).export());

		bind(TypeLiterals.export(IConstraintCheckerFactory.class)).annotatedWith(Names.named(PortExclusionConstraintCheckerFactory.class.getCanonicalName())).toProvider(
				Peaberry.service(new PortExclusionConstraintCheckerFactory()).export());

		bind(TypeLiterals.export(IConstraintCheckerFactory.class)).annotatedWith(Names.named(PortTypeConstraintCheckerFactory.class.getCanonicalName())).toProvider(
				Peaberry.service(new PortTypeConstraintCheckerFactory()).export());

		bind(TypeLiterals.export(IConstraintCheckerFactory.class)).annotatedWith(Names.named(TimeSortConstraintCheckerFactory.class.getCanonicalName())).toProvider(
				Peaberry.service(new TimeSortConstraintCheckerFactory()).export());

		bind(TypeLiterals.export(IConstraintCheckerFactory.class)).annotatedWith(Names.named(TravelTimeConstraintCheckerFactory.class.getCanonicalName())).toProvider(
				Peaberry.service(new TravelTimeConstraintCheckerFactory()).export());

		bind(TypeLiterals.export(IConstraintCheckerFactory.class)).annotatedWith(Names.named(VirtualVesselConstraintCheckerFactory.class.getCanonicalName())).toProvider(
				Peaberry.service(new VirtualVesselConstraintCheckerFactory()).export());

		bind(TypeLiterals.export(IConstraintCheckerFactory.class)).annotatedWith(Names.named(SlotGroupCountConstraintCheckerFactory.class.getCanonicalName())).toProvider(
				Peaberry.service(new SlotGroupCountConstraintCheckerFactory()).export());

		bind(TypeLiterals.export(IConstraintCheckerFactory.class)).annotatedWith(Names.named(ContractCvConstraintCheckerFactory.class.getCanonicalName())).toProvider(
				Peaberry.service(new ContractCvConstraintCheckerFactory()).export());

		bind(TypeLiterals.export(IConstraintCheckerFactory.class)).annotatedWith(Names.named(PortCvCompatibilityConstraintCheckerFactory.class.getCanonicalName())).toProvider(
				Peaberry.service(new PortCvCompatibilityConstraintCheckerFactory()).export());

		if (USE_SPOT_TO_SPOT_CONSTRAINT) {
			bind(TypeLiterals.export(IConstraintCheckerFactory.class)).annotatedWith(Names.named(SpotToSpotConstraintCheckerFactory.class.getCanonicalName())).toProvider(
					Peaberry.service(new SpotToSpotConstraintCheckerFactory()).export());
		}

		bind(TypeLiterals.export(IConstraintCheckerFactory.class)).annotatedWith(Names.named(DifferentSTSVesselsConstraintCheckerFactory.class.getCanonicalName())).toProvider(
				Peaberry.service(new DifferentSTSVesselsConstraintCheckerFactory()).export());

		bind(TypeLiterals.export(IConstraintCheckerFactory.class)).annotatedWith(Names.named(ShippingHoursRestrictionCheckerFactory.class.getCanonicalName())).toProvider(
				Peaberry.service(new ShippingHoursRestrictionCheckerFactory()).export());

	}
}

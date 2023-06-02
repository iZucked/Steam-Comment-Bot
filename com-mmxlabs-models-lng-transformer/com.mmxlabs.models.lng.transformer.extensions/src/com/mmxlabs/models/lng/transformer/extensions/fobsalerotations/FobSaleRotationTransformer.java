package com.mmxlabs.models.lng.transformer.extensions.fobsalerotations;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.curves.ConstantValueLongCurve;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.optimiser.common.components.impl.MutableTimeWindow;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IEndRequirement;
import com.mmxlabs.scheduler.optimiser.components.IStartRequirement;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.components.VesselTankState;
import com.mmxlabs.scheduler.optimiser.components.impl.ConstantHeelPriceCalculator;
import com.mmxlabs.scheduler.optimiser.providers.IFobSaleRotationProviderEditor;

public class FobSaleRotationTransformer implements ITransformerExtension {

	@Inject
	private IFobSaleRotationProviderEditor fobSaleRotationProviderEditor;

	private LNGScenarioModel rootObject;
	private ModelEntityMap modelEntityMap;
	private ISchedulerBuilder builder;

	@Override
	public void startTransforming(@NonNull LNGScenarioModel rootObject, @NonNull ModelEntityMap modelEntityMap, @NonNull ISchedulerBuilder builder) {
		this.rootObject = rootObject;
		this.modelEntityMap = modelEntityMap;
		this.builder = builder;
	}

	@Override
	public void finishTransforming() {
		final Map<Vessel, IVesselCharter> forwardMap = new HashMap<>();
		for (final DischargeSlot dischargeSlot : rootObject.getCargoModel().getDischargeSlots()) {
			if (!(dischargeSlot instanceof SpotSlot) //
					&& dischargeSlot.isFOBSale()) {
				final Vessel nominatedVessel = dischargeSlot.getNominatedVessel();
				if (nominatedVessel != null) {
					final IVesselCharter oNominatedVesselCharter = forwardMap.computeIfAbsent(nominatedVessel, vess -> {
						final IVessel oVessel = modelEntityMap.getOptimiserObjectNullChecked(nominatedVessel, IVessel.class);
						final IStartRequirement startRequirement = builder.createStartRequirement(null, false, null, null);
						final IEndRequirement endRequirement = builder.createEndRequirement(null, false, new MutableTimeWindow(0, Integer.MAX_VALUE),
								builder.createHeelConsumer(0, 0, VesselTankState.EITHER, new ConstantHeelPriceCalculator(0), false));
						return builder.createVesselCharter(oVessel, new ConstantValueLongCurve(0), VesselInstanceType.NONSHIPPED_ROTATION, startRequirement, endRequirement, null, null, true);
					});
					final IDischargeOption dischargeOption = modelEntityMap.getOptimiserObjectNullChecked(dischargeSlot, IDischargeOption.class);
					fobSaleRotationProviderEditor.addMapping(oNominatedVesselCharter, dischargeOption);
				}
			}
		}
	}
}

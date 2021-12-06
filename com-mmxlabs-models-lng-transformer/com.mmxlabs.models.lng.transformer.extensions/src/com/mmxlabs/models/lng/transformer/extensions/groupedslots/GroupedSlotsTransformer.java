package com.mmxlabs.models.lng.transformer.extensions.groupedslots;

import java.util.List;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.parser.series.CalendarMonthMapper;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.GroupedSlotsConstraint;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.transformer.ITransformerExtension;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;
import com.mmxlabs.scheduler.optimiser.builder.ISchedulerBuilder;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.providers.IGroupedSlotsConstraintDataProviderEditor;

public class GroupedSlotsTransformer implements ITransformerExtension {

	private LNGScenarioModel rootObject;

	@Inject
	private IGroupedSlotsConstraintDataProviderEditor groupedSlotsConstraintEditor;

	@Inject
	private ModelEntityMap modelEntityMap;

	@Inject
	private CalendarMonthMapper calendarMonthMapper;

	@Inject
	private DateAndCurveHelper dateAndCurveHelper;

	@Override
	public void startTransforming(final LNGScenarioModel rootObject, final ModelEntityMap modelEntityMap, final ISchedulerBuilder builder) {
		this.rootObject = rootObject;
	}

	@Override
	public void finishTransforming() {
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(rootObject);
		for (final GroupedSlotsConstraint<SalesContract, DischargeSlot> eConstraint : cargoModel.getGroupedDischargeSlots()) {
			final List<Pair<IDischargeOption, Integer>> oSlots = eConstraint.getSlots().stream().<Pair<IDischargeOption, Integer>> map(s -> Pair
					.of(modelEntityMap.getOptimiserObjectNullChecked(s, IDischargeOption.class), calendarMonthMapper.mapChangePointToMonth(dateAndCurveHelper.convertTime(s.getWindowStart()))))
					.collect(Collectors.toList());
			groupedSlotsConstraintEditor.addMinDischargeSlots(oSlots, eConstraint.getMinimumBound());
		}
	}
}

/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.common.Pair;
import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DefaultMenuCreatorAction;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.NonShippedSequence;
import com.mmxlabs.models.lng.schedule.NonShippedSlotVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.widgets.schedulechart.ENinteyDayNonShippedRotationSelection;
import com.mmxlabs.widgets.schedulechart.viewer.ScheduleChartViewer;

public class NinetyDayScheduleLabelMenuAction extends NinetyDayScheduleAction {
	
	private NinetyDayDrawableEventLabelProvider labelProvider;
	private ScheduleChartViewer<NinetyDayScheduleInput> viewer;

	protected NinetyDayScheduleLabelMenuAction(NinetyDayScheduleReport parent, NinetyDayDrawableEventLabelProvider labelProvider) {
		super("Label", AS_DROP_DOWN_MENU, parent);
		setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Label, IconMode.Enabled));
		
		this.labelProvider = labelProvider;
		this.viewer = parent.getViewer();
	}

	@Override
	protected void createMenuItems(Menu menu) {
		{
			Action canalAction = new Action("Canals", IAction.AS_CHECK_BOX) {
				@Override
				public void run() {
					labelProvider.toggleShowCanals();
					setChecked(labelProvider.showCanals());
					parent.redraw();
				}
			};
			canalAction.setChecked(labelProvider.showCanals());
			final ActionContributionItem aci = new ActionContributionItem(canalAction);
			aci.fill(menu, -1);
		}
		{
			Action destinationLabelsAction = new Action("Cargoes", IAction.AS_CHECK_BOX) {

				@Override
				public void run() {
					labelProvider.toggleShowDestinationLabels();
					setChecked(labelProvider.showDestinationLabels());
					parent.redraw();
				}
			};
			destinationLabelsAction.setChecked(labelProvider.showDestinationLabels());
			final ActionContributionItem aci = new ActionContributionItem(destinationLabelsAction);
			aci.fill(menu, -1);
		}
		{
			final Action showDaysOnEventsAction = new Action("Days", IAction.AS_CHECK_BOX) {
				@Override
				public void run() {
					labelProvider.toggleShowDays();
					setChecked(labelProvider.showDays());
					parent.redraw();
				}
			};
			showDaysOnEventsAction.setChecked(labelProvider.showDays());
			final ActionContributionItem aci = new ActionContributionItem(showDaysOnEventsAction);
			aci.fill(menu, -1);
		}
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_NON_SHIPPED_FOB_ROTATIONS)) {
			NinetyDayScheduleInput input = viewer.getInput();
			ScenarioResult result = input.other().isEmpty() ? input.pinned() : input.other().get(0);
			final ScheduleModel schedule = result.getTypedResult(ScheduleModel.class);
			
			final Action fobSaleRotation = getFobSaleRotationMenu(schedule.getSchedule());
			
			if(fobSaleRotation != null) {
				final ActionContributionItem actionContributionItem = new ActionContributionItem(fobSaleRotation);
				actionContributionItem.fill(menu, -1);
			}
		}
	}
	
	private Action getFobSaleRotationMenu(final @NonNull Schedule schedule) {
		final List<NonShippedSequence> nonShippedSequences = schedule.getNonShippedSequences();
		if (!nonShippedSequences.isEmpty()) {
			return new DefaultMenuCreatorAction("FOB sale rotation") {

				@Override
				protected void populate(Menu menu) {
					final Action offAction = new Action("off", IAction.AS_CHECK_BOX) {
						@Override
						public void run() {
							if (parent.getFobRotationOptionSelection() != ENinteyDayNonShippedRotationSelection.OFF) {
								parent.setFobRotationOptionSelection(ENinteyDayNonShippedRotationSelection.OFF);
								parent.clearFobRotations();
								parent.getViewer().refresh();
							}
						}
					};
					final Action allAction = new Action("all", IAction.AS_CHECK_BOX) {
						@Override
						public void run() {
							if (isChecked()) {
								final Collection<Predicate<NonShippedSequence>> predicates = Collections.singleton(sequence -> true);
								parent.clearFobRotations();
								parent.setFobRotationOptionSelection(ENinteyDayNonShippedRotationSelection.ALL);
								parent.replaceFobRotations(predicates);			
								parent.getViewer().refresh();
							} else {
								offAction.run();
							}
						}
					};
					addActionToMenu(offAction, menu);
					addActionToMenu(allAction, menu);
					final Map<SalesContract, Set<Vessel>> fobSalesContractVesselMap = new HashMap<>();
					for (final NonShippedSequence sequence : nonShippedSequences) {
						final Vessel vessel = sequence.getVessel();
						if (vessel != null) {
							sequence.getEvents().stream() //
									.filter(NonShippedSlotVisit.class::isInstance) //
									.map(NonShippedSlotVisit.class::cast) //
									.map(NonShippedSlotVisit::getSlot) //
									.map(Slot::getContract) //
									.filter(Objects::nonNull) //
									.filter(SalesContract.class::isInstance) //
									.map(SalesContract.class::cast) //
									.forEach(sc -> fobSalesContractVesselMap.computeIfAbsent(sc, k -> new HashSet<>()).add(vessel));
						}
					}
					final List<Pair<SalesContract, Set<Vessel>>> sortedSalesContracts = fobSalesContractVesselMap.entrySet().stream() //
							.sorted((e1, e2) -> e1.getKey().getName().compareTo(e2.getKey().getName())) //
							.map(e -> Pair.of(e.getKey(), e.getValue())) //
							.toList();
					final List<Action> selectedContractActions = new LinkedList<>();
					final Set<Contract> selectedContracts = parent.getSelectedContracts();
					for (final Pair<SalesContract, Set<Vessel>> pair : sortedSalesContracts) {
						final Action nextAction = new Action(pair.getFirst().getName(), IAction.AS_CHECK_BOX) {
							@Override
							public void run() {
								boolean newlyChecked = parent.toggleSelectedContract(pair.getFirst());
								if (newlyChecked) {
									parent.setFobRotationOptionSelection(ENinteyDayNonShippedRotationSelection.POSSIBLY_CONTRACT);
								}
								final Set<Contract> selectedContracts = parent.getSelectedContracts();
								final Predicate<NonShippedSequence> predicate = seq -> selectedContracts.stream() //
										.anyMatch(contract -> {
											final Set<Vessel> vessels = fobSalesContractVesselMap.get(contract);
											return vessels != null && vessels.contains(seq.getVessel());
										});
								final Collection<@NonNull Predicate<NonShippedSequence>> predicates = Collections.singleton(predicate);
								parent.replaceFobRotations(predicates);
								parent.getViewer().refresh();
							}
						};
						if (selectedContracts.contains(pair.getFirst())) {
							selectedContractActions.add(nextAction);
						}
						addActionToMenu(nextAction, menu);
					}
					if (parent.getFobRotationOptionSelection() == ENinteyDayNonShippedRotationSelection.OFF) {
						offAction.setChecked(true);
					} else if (parent.getFobRotationOptionSelection() == ENinteyDayNonShippedRotationSelection.ALL) {
						allAction.setChecked(true);
					} else if (parent.getFobRotationOptionSelection() == ENinteyDayNonShippedRotationSelection.POSSIBLY_CONTRACT) {
						if (selectedContractActions.isEmpty()) {
							parent.setFobRotationOptionSelection(ENinteyDayNonShippedRotationSelection.OFF);
							offAction.setChecked(true);
						} else {
							selectedContractActions.forEach(a -> a.setChecked(true));
						}
					}
				}
			};
		}
		
		return null;
	}

}

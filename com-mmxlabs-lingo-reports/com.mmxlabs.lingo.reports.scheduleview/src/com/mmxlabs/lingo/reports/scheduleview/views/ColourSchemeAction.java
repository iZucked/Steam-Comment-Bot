/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.scheduleview.views;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;

import com.mmxlabs.common.Pair;
import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.lingo.reports.scheduleview.views.positionssequences.ISchedulePositionsSequenceProvider;
import com.mmxlabs.lingo.reports.scheduleview.views.positionssequences.ISchedulePositionsSequenceProviderExtension;
import com.mmxlabs.lingo.reports.scheduleview.views.positionssequences.PositionsSequenceProviderException;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.ui.editorpart.actions.DefaultMenuCreatorAction;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.NonShippedSequence;
import com.mmxlabs.models.lng.schedule.NonShippedSlotVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconMode;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;

class ColourSchemeAction extends SchedulerViewAction {
	
	public ColourSchemeAction(final SchedulerView schedulerView, final EMFScheduleLabelProvider lp, final GanttChartViewer viewer) {
		super("Label", IAction.AS_DROP_DOWN_MENU, schedulerView, viewer, lp);
		setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.Label, IconMode.Enabled));
	}

	@Override
	public void run() {

		// cycle to next colour scheme
		final List<IScheduleViewColourScheme> colourSchemes = lp.getColourSchemes();
		final IScheduleViewColourScheme currentScheme = lp.getCurrentScheme();
		int nextIdx = -1;
		if (currentScheme != null) {
			nextIdx = colourSchemes.indexOf(currentScheme);
			nextIdx = (nextIdx + 1) % colourSchemes.size();
		}
		if (nextIdx != -1) {
			lp.setScheme(colourSchemes.get(nextIdx));
		}

		viewer.setInput(viewer.getInput());
		schedulerView.redraw();
	}

	@Override
	protected void createMenuItems(final Menu menu) {
		Action canalAction;
		Action destinationLabelsAction;
		{
			canalAction = new Action("Canals", IAction.AS_CHECK_BOX) {
				@Override
				public void run() {
					lp.toggleShowCanals();
					setChecked(lp.showCanals());
					viewer.setInput(viewer.getInput());
					schedulerView.redraw();
				}
			};
			canalAction.setChecked(lp.showCanals());
			final ActionContributionItem actionContributionItem = new ActionContributionItem(canalAction);
			actionContributionItem.fill(menu, -1);
		}
		{
			destinationLabelsAction = new Action("Cargoes", IAction.AS_CHECK_BOX) {

				@Override
				public void run() {
					lp.toggleShowDestinationLabels();
					setChecked(lp.showDestinationLabels());
					viewer.setInput(viewer.getInput());
					schedulerView.redraw();
				}
			};
			destinationLabelsAction.setChecked(lp.showDestinationLabels());

			final ActionContributionItem aci = new ActionContributionItem(destinationLabelsAction);
			aci.fill(menu, -1);
		}
		{
			final Action showDaysOnEventsAction = new Action("Days", IAction.AS_CHECK_BOX) {
				@Override
				public void run() {
					lp.toggleShowDays();
					setChecked(lp.showDays());
					viewer.setInput(viewer.getInput());
					schedulerView.redraw();
				}
			};
			showDaysOnEventsAction.setChecked(lp.showDays());
			final ActionContributionItem actionContributionItem = new ActionContributionItem(showDaysOnEventsAction);
			actionContributionItem.fill(menu, -1);
		}
		{
			final Separator separator = new Separator();
			separator.fill(menu, -1);
		}
		{
			final Action toggleShowNominalsByDefaultAction = new Action("Nominals", SWT.CHECK) {
				@Override
				public void run() {
					// TODO: Tidy all this state up.
					schedulerView.showNominalsByDefault = !schedulerView.showNominalsByDefault;
					schedulerView.contentProvider.showNominalsByDefault = schedulerView.showNominalsByDefault;
					setChecked(schedulerView.showNominalsByDefault);
					schedulerView.refresh();
				}
			};
			toggleShowNominalsByDefaultAction.setToolTipText("Toggles always show nominal cargoes or only when selected");
			toggleShowNominalsByDefaultAction.setChecked(schedulerView.showNominalsByDefault);
			final ActionContributionItem actionContributionItem = new ActionContributionItem(toggleShowNominalsByDefaultAction);
			actionContributionItem.fill(menu, -1);
		}
		
		final var enabledTracker = schedulerView.contentProvider.enabledPSPTracker;
		for (ISchedulePositionsSequenceProviderExtension ext: schedulerView.contentProvider.positionsSequenceProviderExtensions) {
			if (ext.showMenuItem().equals("true")) {
				ISchedulePositionsSequenceProvider provider = ext.createInstance();
				final Action toggleShowPartition = new Action(ext.getName(), SWT.CHECK) {
					@Override
					public void run() {
						Optional<PositionsSequenceProviderException> optError = enabledTracker.toggleVisibilityOrGetError(provider.getId());
						if (optError.isPresent()) {
							PositionsSequenceProviderException e = optError.get();
							MessageDialog dialog = new MessageDialog(menu.getShell(), e.getTitle(), null, e.getDescription(), 0, 0, "OK");
							dialog.create();
							dialog.open();
							return;
						}
						setChecked(enabledTracker.isEnabledWithNoError(provider.getId()));
						schedulerView.redraw();
					}
				};
				toggleShowPartition.setToolTipText("Partitions unshipped cargoes based on the selected split");
				toggleShowPartition.setChecked(enabledTracker.isEnabledWithNoError(provider.getId()));
				final ActionContributionItem actionContributionItem = new ActionContributionItem(toggleShowPartition);
				actionContributionItem.fill(menu, -1);
			}
		}
		final Object input = viewer.getInput();
		if (input instanceof final Collection<?> collection) {
			if (collection.size() == 1) {
				if (collection.iterator().next() instanceof final @NonNull Schedule schedule) {
					buildFobSaleRotationMenu(menu, schedule);
				}
			}
		} else if (input instanceof final @NonNull Schedule schedule) {
			buildFobSaleRotationMenu(menu, schedule);
		}
	}

	private void buildFobSaleRotationMenu(final Menu menu, final @NonNull Schedule schedule) {
		final List<NonShippedSequence> nonShippedSequences = schedule.getNonShippedSequences();
		if (!nonShippedSequences.isEmpty()) {
			final Action fobRotation = new DefaultMenuCreatorAction("FOB sale rotation") {

				@Override
				protected void populate(Menu menu) {
					final Action off = new Action("off") {
						@Override
						public void run() {
							schedulerView.clearFobRotations();
							schedulerView.contentProvider.clearFobRotations();
							schedulerView.redraw();
							schedulerView.refresh();
						}
					};
					final Action all = new Action("all") {
						@Override
						public void run() {
							final Collection<Predicate<NonShippedSequence>> predicates = Collections.singleton(sequence -> true);
							schedulerView.clearFobRotations();
							schedulerView.replaceFobRotations(predicates);
							schedulerView.contentProvider.replaceFobRotations(predicates);
							schedulerView.redraw();
							schedulerView.refresh();
						}
					};
					addActionToMenu(off, menu);
					addActionToMenu(all, menu);
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
					for (final Pair<SalesContract, Set<Vessel>> pair : sortedSalesContracts) {
						final Action nextAction = new Action(pair.getFirst().getName()) {
							@Override
							public void run() {
								schedulerView.toggleSelectedContract(pair.getFirst());
								final Set<Contract> selectedContracts = schedulerView.getSelectedContracts();
//								final Predicate<NonShippedSequence> predicate = seq -> pair.getSecond().contains(seq.getVessel());
								final Predicate<NonShippedSequence> predicate = seq -> {
									return selectedContracts.stream() //
											.anyMatch(contract -> {
												final Set<Vessel> vessels = fobSalesContractVesselMap.get(contract);
												if (vessels != null) {
													return vessels.contains(seq.getVessel());
												}
												return false;
											});
//									pair.getSecond().contains(seq.getVessel());
								};
								final Collection<@NonNull Predicate<NonShippedSequence>> predicates = Collections.singleton(predicate);
								schedulerView.replaceFobRotations(predicates);
								schedulerView.contentProvider.replaceFobRotations(predicates);
								schedulerView.redraw();
								schedulerView.refresh();
							}
						};
						addActionToMenu(nextAction, menu);
					}
				}
			};
			final ActionContributionItem actionContributionItem = new ActionContributionItem(fobRotation);
			actionContributionItem.fill(menu, -1);
		}
	}

}
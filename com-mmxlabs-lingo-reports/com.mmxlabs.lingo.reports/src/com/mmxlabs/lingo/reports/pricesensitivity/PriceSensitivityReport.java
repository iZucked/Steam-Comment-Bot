package com.mmxlabs.lingo.reports.pricesensitivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.components.AbstractSimpleTabularReportTransformer;
import com.mmxlabs.lingo.reports.components.ColumnManager;
import com.mmxlabs.lingo.reports.components.SimpleTabularReportContentProvider;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.views.standard.SimpleTabularReportView;
import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.SensitivityModel;
import com.mmxlabs.models.lng.analytics.SolutionOption;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.scenario.service.ScenarioResult;

public class PriceSensitivityReport extends SimpleTabularReportView<PriceSensitivityData> {

	public PriceSensitivityReport() {
		super("com.mmxlabs.lingo.reports.Reports_PriceSensitivity");
	}

	@Override
	protected SimpleTabularReportContentProvider createContentProvider() {
		return new SimpleTabularReportContentProvider() {
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}

			@Override
			public Object getParent(Object element) {
				if (element instanceof PriceSensitivityData data) {
					return data.parent;
				}
				return super.getParent(element);
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof final PriceSensitivityData priceSensitivityData) {
					return priceSensitivityData.children.toArray();
				}
				return super.getChildren(parentElement);
			}
			
			@Override
			public boolean hasChildren(Object element) {
				if (element instanceof final PriceSensitivityData data) {
					return data.children != null && !data.children.isEmpty();
				}
				return super.hasChildren(element);
			}
		};
	}

	@Override
	protected AbstractSimpleTabularReportTransformer<PriceSensitivityData> createTransformer() {
		return new AbstractSimpleTabularReportTransformer<PriceSensitivityData>() {

			@Override
			public @NonNull List<@NonNull ColumnManager<PriceSensitivityData>> getColumnManagers(@NonNull ISelectedDataProvider selectedDataProvider) {
				final ArrayList<ColumnManager<PriceSensitivityData>> result = new ArrayList<>();

//				if (selectedDataProvider.getAllScenarioResults().size() > 1) {
//					result.add(new ColumnManager<PriceSensitivityData>("Scenario") {
//						@Override
//						public String getColumnText(PriceSensitivityData data) {
//							final ScenarioResult scenarioResult = data.scenarioResult;
//							if (scenarioResult != null) {
//								final ScenarioModelRecord modelRecord = scenarioResult.getModelRecord();
//								if (modelRecord != null) {
//									return modelRecord.getName();
//								}
//							}
//							return null;
//						}
//
//						@Override
//						public @Nullable Image getColumnImage(PriceSensitivityData data) {
//							final ScenarioResult scenarioResult = data.scenarioResult;
//							if (selectedDataProvider.getPinnedScenarioResult() == scenarioResult) {
//								return pinImage;
//							}
//							return null;
//						}
//
//						@Override
//						public int compare(PriceSensitivityData obj1, PriceSensitivityData obj2) {
//							final String s1 = getColumnText(obj1);
//							final String s2 = getColumnText(obj2);
//							if (s1 == null) {
//								return -1;
//							}
//							if (s2 == null) {
//								return 1;
//							}
//							return s1.compareTo(s2);
//						}
//
//						@Override
//						public boolean isTree() {
//							return false;
//						}
//					});
//				}

				result.add(new ColumnManager<PriceSensitivityData>("Item") {
					@Override
					public String getColumnText(PriceSensitivityData obj) {
						return obj.key;
					}

					@Override
					public int compare(PriceSensitivityData obj1, PriceSensitivityData obj2) {
						final String s1 = obj1.key != null ? obj1.key : "";
						final String s2 = obj2.key != null ? obj2.key : "";
						return s1.compareTo(s2);
					}

					@Override
					public boolean isTree() {
						return true;
					}
				});

				result.add(new ColumnManager<PriceSensitivityData>("L-ID") {
					@Override
					public String getColumnText(PriceSensitivityData obj) {
						return obj.loadId != null ? obj.loadId : "";
					}

					@Override
					public int compare(PriceSensitivityData obj1, PriceSensitivityData obj2) {
						final String s1 = obj1.loadId != null ? obj1.loadId : "";
						final String s2 = obj2.loadId != null ? obj2.loadId : "";
						return s1.compareTo(s2);
					}

					@Override
					public boolean isTree() {
						return false;
					}
				});

				result.add(new ColumnManager<PriceSensitivityData>("D-ID") {
					@Override
					public String getColumnText(PriceSensitivityData obj) {
						return obj.dischargeId != null ? obj.dischargeId : "";
					}

					@Override
					public int compare(PriceSensitivityData obj1, PriceSensitivityData obj2) {
						final String s1 = obj1.dischargeId != null ? obj1.dischargeId : "";
						final String s2 = obj2.dischargeId != null ? obj2.dischargeId : "";
						return s1.compareTo(s2);
					}

					@Override
					public boolean isTree() {
						return false;
					}
				});

				
				result.add(new ColumnManager<PriceSensitivityData>("Max PnL") {
					@Override
					public String getColumnText(PriceSensitivityData obj) {
						final String formattedString = String.format("%,d", obj.maxPnl);
						return !"Cargo PnL".equals(obj.key) ? formattedString : "";
					}

					@Override
					public boolean isTree() {
						return false;
					}
				});
				
				result.add(new ColumnManager<PriceSensitivityData>("Min PnL") {
					@Override
					public String getColumnText(PriceSensitivityData obj) {
						final String formattedString = String.format("%,d", obj.minPnl);
						return !"Cargo PnL".equals(obj.key) ? formattedString : "";
					}

					@Override
					public boolean isTree() {
						return false;
					}
				});

				return result;
			}

			@Override
			public @NonNull List<PriceSensitivityData> createData(@Nullable Pair<@NonNull Schedule, @NonNull ScenarioResult> pinnedPair,
					@NonNull List<@NonNull Pair<@NonNull Schedule, @NonNull ScenarioResult>> otherPairs) {
				final List<PriceSensitivityData> output = new LinkedList<>();
				for (final Pair<@NonNull Schedule, ScenarioResult> p : otherPairs) {
					final LNGScenarioModel sm = ScenarioModelUtil.getScenarioModel(p.getSecond().getScenarioDataProvider());
					final boolean earlyBreak = sm.getSensitivityModel() == null //
							|| sm.getSensitivityModel().getSensitivityModel() == null //
							|| sm.getSensitivityModel().getSensitivityModel().getResults() == null //
							|| sm.getSensitivityModel().getSensitivityModel().getResults().getOptions() == null //
							|| sm.getSensitivityModel().getSensitivityModel().getResults().getOptions().isEmpty();
					if (earlyBreak) {
						break;
					}
					if (ScenarioModelUtil.getScenarioModel(p.getSecond().getScenarioDataProvider()).getSensitivityModel().getSensitivityModel().getResults() == null) {
						break;
					}
					output.addAll(createData(p.getFirst(), p.getSecond()));
					break;
				}
				return output;
			}

			public List<PriceSensitivityData> createData(final Schedule schedule, final ScenarioResult scenarioResult) {
				final List<PriceSensitivityData> output = new ArrayList<>();
				final PriceSensitivityData pnlData = new PriceSensitivityData(scenarioResult, schedule);
				// Do main pnl
				pnlData.key = "Portfolio PnL";
				calculatePortfolioPnL(pnlData);
				output.add(pnlData);
				// Do per cargo pnl
				final List<LoadSlot> sortedLoadSlots = ScenarioModelUtil.getScenarioModel(scenarioResult.getScenarioDataProvider()).getCargoModel().getCargoes().stream() //
						.map(c -> (LoadSlot) c.getSlots().get(0)) //
						.sorted((l1, l2) -> l1.getSchedulingTimeWindow().getStart().compareTo(l2.getSchedulingTimeWindow().getStart())) //
						.toList();
				final Map<LoadSlot, List<CargoAllocation>> loadSlotToCargoAllocations = new HashMap<>();
				final SensitivityModel sensitivityModel = ScenarioModelUtil.getScenarioModel(pnlData.scenarioResult.getScenarioDataProvider()).getSensitivityModel();
				final AbstractSolutionSet solutions = sensitivityModel.getSensitivityModel().getResults();
				for (final SolutionOption solutionOption : solutions.getOptions()) {
					final Schedule schedule2 = solutionOption.getScheduleModel().getSchedule();
					for (final CargoAllocation cargoAllocation : schedule2.getCargoAllocations()) {
						for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
							if (slotAllocation.getSlot() instanceof final LoadSlot loadSlot) {
								loadSlotToCargoAllocations.computeIfAbsent(loadSlot, k -> new ArrayList<>()).add(cargoAllocation);
								break;
							}
						}
					}
				}
				final PriceSensitivityData cargoData = new PriceSensitivityData(scenarioResult, schedule);
				cargoData.key = "Cargo PnL";
				final List<PriceSensitivityData> aggregatedCargoData = new ArrayList<>();
				for (final LoadSlot loadSlot : sortedLoadSlots) {
					final List<CargoAllocation> cargoAllocations = loadSlotToCargoAllocations.get(loadSlot);
					long minPnL = Long.MAX_VALUE;
					long maxPnL = Long.MIN_VALUE;
					for (final CargoAllocation cargoAllocation : cargoAllocations) {
						final long thisPnL = ScheduleModelKPIUtils.getElementPNL(cargoAllocation);
						if (thisPnL < minPnL) {
							minPnL = thisPnL;
						}
						if (thisPnL > maxPnL) {
							maxPnL = thisPnL;
						}
					}
					final PriceSensitivityData data = new PriceSensitivityData(scenarioResult, schedule);
					data.loadId = loadSlot.getName();
					data.dischargeId = loadSlot.getCargo().getSlots().get(1).getName();
					data.minPnl = minPnL;
					data.maxPnl = maxPnL;
					data.parent = cargoData;
					aggregatedCargoData.add(data);
				}
				
				cargoData.children.addAll(aggregatedCargoData);
				output.add(cargoData);
				return output;
			}

			private void calculatePortfolioPnL(final PriceSensitivityData pnlData) {
				long minPnL = Long.MAX_VALUE;
				long maxPnL = Long.MIN_VALUE;
				final SensitivityModel sensitivityModel = ScenarioModelUtil.getScenarioModel(pnlData.scenarioResult.getScenarioDataProvider()).getSensitivityModel();
				final AbstractSolutionSet solutions = sensitivityModel.getSensitivityModel().getResults();
				if (solutions == null) {
					return;
				}
				for (final SolutionOption solutionOption : solutions.getOptions()) {
					final Schedule schedule = solutionOption.getScheduleModel().getSchedule();
					final long thisPnL = ScheduleModelKPIUtils.getScheduleProfitAndLoss(schedule);
					if (thisPnL < minPnL) {
						minPnL = thisPnL;
					}
					if (thisPnL > maxPnL) {
						maxPnL = thisPnL;
					}
				}
				pnlData.minPnl = minPnL;
				pnlData.maxPnl = maxPnL;
			}

		};
	}

}

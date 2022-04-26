package com.mmxlabs.lingo.reports.pricesensitivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.components.AbstractSimpleTabularReportTransformer;
import com.mmxlabs.lingo.reports.components.ColumnManager;
import com.mmxlabs.lingo.reports.components.SimpleTabularReportContentProvider;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.views.standard.SimpleTabularReportView;
import com.mmxlabs.models.lng.analytics.CargoPnLResult;
import com.mmxlabs.models.lng.analytics.SensitivitySolutionSet;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.scenario.service.ScenarioResult;

public class PriceSensitivityReport extends SimpleTabularReportView<PriceSensitivityData> {

	private static final long SCALE_DOWN_FACTOR = 1_000_000L;

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
						if (s1.equalsIgnoreCase("Portfolio")) {
							return -1;
						} else if (s2.equalsIgnoreCase("Portfolio")) {
							return 1;
						}
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

				
				result.add(new ColumnManager<PriceSensitivityData>("Max") {
					@Override
					public String getColumnText(PriceSensitivityData obj) {
						final String formattedString = String.format("%,.2f", scaleDown(obj.maxPnl));
						return !"Cargo".equals(obj.key) ? formattedString : "";
					}

					@Override
					public boolean isTree() {
						return false;
					}
				});
				
				result.add(new ColumnManager<PriceSensitivityData>("Min") {
					@Override
					public String getColumnText(PriceSensitivityData obj) {
						final String formattedString = String.format("%,.2f", scaleDown(obj.minPnl));
						return !"Cargo".equals(obj.key) ? formattedString : "";
					}

					@Override
					public boolean isTree() {
						return false;
					}
				});

				result.add(new ColumnManager<PriceSensitivityData>("Mean") {
					@Override
					public String getColumnText(PriceSensitivityData obj) {
						final String formattedString = String.format("%,.2f", scaleDown(obj.averagePnl));
						return !"Cargo".equals(obj.key) ? formattedString : "";
					}

					@Override
					public boolean isTree() {
						return false;
					}
				});

				result.add(new ColumnManager<PriceSensitivityData>("Std Dev") {
					@Override
					public String getColumnText(PriceSensitivityData obj) {
						final String formattedString = String.format("%,.2f", scaleDown((long) obj.variance));
						return !"Cargo".equals(obj.key) ? formattedString : "";
					}

					@Override
					public boolean isTree() {
						return false;
					}
				});
				
				return result;
			}

			private double scaleDown(final long val) {
				if (val == 0L) {
					return 0.0;
				}
				final double scaledDownVal = ((double) val)/ SCALE_DOWN_FACTOR;
				return scaledDownVal;
			}

			@Override
			public @NonNull List<PriceSensitivityData> createData(@Nullable Pair<@NonNull Schedule, @NonNull ScenarioResult> pinnedPair,
					@NonNull List<@NonNull Pair<@NonNull Schedule, @NonNull ScenarioResult>> otherPairs) {
				final List<PriceSensitivityData> output = new LinkedList<>();
				for (final Pair<@NonNull Schedule, ScenarioResult> p : otherPairs) {
					final LNGScenarioModel sm = ScenarioModelUtil.getScenarioModel(p.getSecond().getScenarioDataProvider());
					if (sm.getSensitivityModel() != null && sm.getSensitivityModel().getSensitivityModel() != null && sm.getSensitivityModel().getSensitivityModel().getResults() != null) {
						sm.getSensitivityModel().getSensitivityModel().setResults(null);
					}
					
					final boolean earlyBreak = sm.getSensitivityModel() == null //
							|| sm.getSensitivityModel().getSensitivityModel() == null //
							|| sm.getSensitivityModel().getSensitivitySolution() == null;
					if (earlyBreak) {
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
				pnlData.key = "Portfolio";
				final SensitivitySolutionSet sensitivityResult = ScenarioModelUtil.getScenarioModel(pnlData.scenarioResult.getScenarioDataProvider()).getSensitivityModel().getSensitivitySolution();
				pnlData.minPnl = sensitivityResult.getPorfolioPnLResult().getMinPnL();
				pnlData.maxPnl = sensitivityResult.getPorfolioPnLResult().getMaxPnL();
				pnlData.averagePnl = sensitivityResult.getPorfolioPnLResult().getAveragePnL();
				pnlData.variance = sensitivityResult.getPorfolioPnLResult().getVariance();
				output.add(pnlData);

				final PriceSensitivityData cargoData = new PriceSensitivityData(scenarioResult, schedule);
				cargoData.key = "Cargo";
				final List<PriceSensitivityData> aggregatedCargoData = new ArrayList<>();
				for (final CargoPnLResult cargoPnlResult : sensitivityResult.getCargoPnLResults()) {
					final PriceSensitivityData data = new PriceSensitivityData(scenarioResult, schedule);
					data.loadId = cargoPnlResult.getCargo().getSlots().get(0).getName();
					data.dischargeId = cargoPnlResult.getCargo().getSlots().get(1).getName();
					data.minPnl = cargoPnlResult.getMinPnL();
					data.maxPnl = cargoPnlResult.getMaxPnL();
					data.averagePnl = cargoPnlResult.getAveragePnL();
					data.variance = cargoPnlResult.getVariance();
					data.parent = cargoData;
					aggregatedCargoData.add(data);
				}
				cargoData.children.addAll(aggregatedCargoData);
				output.add(cargoData);
				return output;
			}

		};
	}

}

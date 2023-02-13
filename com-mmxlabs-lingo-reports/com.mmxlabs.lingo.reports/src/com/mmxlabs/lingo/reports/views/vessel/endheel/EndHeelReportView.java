/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vessel.endheel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.lingo.reports.IReportContentsGenerator;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.ReportContentsGenerators;
import com.mmxlabs.lingo.reports.ScheduleElementCollector;
import com.mmxlabs.lingo.reports.components.EMFReportView;
import com.mmxlabs.lingo.reports.views.standard.pnlcalcs.AbstractPNLCalcRowFactory;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelUtils;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.ui.tabular.BaseFormatter;
import com.mmxlabs.models.ui.tabular.ICellRenderer;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnBlock;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnHandler;
import com.mmxlabs.models.ui.tabular.columngeneration.ColumnType;
import com.mmxlabs.models.ui.tabular.columngeneration.EObjectTableViewerColumnFactory;
import com.mmxlabs.scenario.service.ScenarioResult;

public class EndHeelReportView extends EMFReportView {
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.EndHeelReportView";

	private LocalResourceManager localResourceManager;

	public EndHeelReportView() {
		super("com.mmxlabs.lingo.doc.Reports_EndHeel");
	}

	@Override
	public void createPartControl(final Composite parent) {
		super.createPartControl(parent);
		localResourceManager = new LocalResourceManager(JFaceResources.getResources(), parent);
		Color highlightColour = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
		
		getBlockManager().setColumnFactory(new EObjectTableViewerColumnFactory(viewer));

		List<ColumnHandler> columns = new ArrayList<>(List.of(addColumn("schedule", "Schedule", ColumnType.MULTIPLE, containingScheduleFormatter),

				addColumn("vesselID", "Vessel", ColumnType.NORMAL, new BaseFormatter() {
					@Override
					public String render(final Object object) {

						if (object instanceof CargoAllocation cargoAllocation) {
							final Sequence sequence = cargoAllocation.getSequence();
							if (sequence != null) {
								final Vessel vessel = ScheduleModelUtils.getVessel(sequence);
								if (vessel != null) {
									return vessel.getName();
								}
							}
						}
						return "";
					}

					@Override
					public Comparable<?> getComparable(final Object object) {

						if (object instanceof CargoAllocation event) {
							final Sequence sequence = event.getSequence();
							if (sequence != null) {
								final Vessel vessel = ScheduleModelUtils.getVessel(sequence);
								if (vessel != null) {
									return vessel.getName();
								}
							}
						}

						return super.getComparable(object);
					}
				}), addColumn("vesselCapacity", "Capacity", ColumnType.NORMAL, new BaseFormatter() {
					@Override
					public String render(final Object object) {

						if (object instanceof CargoAllocation cargoAllocation) {
							final Sequence sequence = cargoAllocation.getSequence();
							if (sequence != null) {
								final Vessel vessel = ScheduleModelUtils.getVessel(sequence);
								if (vessel != null) {
									return AbstractPNLCalcRowFactory.VolumeM3Format.format(getFillCapacityInM3(vessel));
								}
							}
						}
						return "";
					}

					@Override
					public Comparable<?> getComparable(final Object object) {

						if (object instanceof CargoAllocation event) {
							final Sequence sequence = event.getSequence();
							if (sequence != null) {
								final Vessel vessel = ScheduleModelUtils.getVessel(sequence);
								if (vessel != null) {
									return vessel.getVesselOrDelegateCapacity();
								}
							}
						}

						return super.getComparable(object);
					}
				}), addColumn("endHeelMin", "End Heel Min", ColumnType.NORMAL, new BaseFormatter() {
					@Override
					public String render(final Object object) {

						if (object instanceof CargoAllocation event) {
							final Sequence sequence = event.getSequence();
							if (sequence != null) {
								final VesselCharter vesselCharter = sequence.getVesselCharter();
								final CharterInMarket charterInMarket = sequence.getCharterInMarket();
								if (vesselCharter != null) {
									return String.valueOf(vesselCharter.getEndHeel().getMinimumEndHeel());
								} else if(charterInMarket != null) {
									return String.valueOf(charterInMarket.getGenericCharterContract().getEndHeel().getMinimumEndHeel());
								}
							}
						}
						return "";
					}

					@Override
					public Comparable<?> getComparable(final Object object) {

						if (object instanceof CargoAllocation event) {
							final Sequence sequence = event.getSequence();
							if (sequence != null) {
								final VesselCharter vesselCharter = sequence.getVesselCharter();
								final CharterInMarket charterInMarket = sequence.getCharterInMarket();
								if (vesselCharter != null) {
									return vesselCharter.getEndHeel().getMinimumEndHeel();
								} else if(charterInMarket != null) {
									return charterInMarket.getGenericCharterContract().getEndHeel().getMinimumEndHeel();
								}
							}
						}

						return super.getComparable(object);
					}
				}),

				addColumn("endHeelMax", "End Heel Max", ColumnType.NORMAL, new BaseFormatter() {
					@Override
					public String render(final Object object) {

						if (object instanceof CargoAllocation event) {
							final Sequence sequence = event.getSequence();
							if (sequence != null) {
								final VesselCharter vesselCharter = sequence.getVesselCharter();
								final CharterInMarket charterInMarket = sequence.getCharterInMarket();
								if (vesselCharter != null) {
									return String.valueOf(vesselCharter.getEndHeel().getMaximumEndHeel());
								} else if(charterInMarket != null) {
									return String.valueOf(charterInMarket.getGenericCharterContract().getEndHeel().getMaximumEndHeel());
								}
							}
						}
						return "";
					}

					@Override
					public Comparable<?> getComparable(final Object object) {

						if (object instanceof CargoAllocation event) {
							final Sequence sequence = event.getSequence();
							if (sequence != null) {
								final VesselCharter vesselCharter = sequence.getVesselCharter();
								if (vesselCharter != null) {
									return vesselCharter.getEndHeel().getMaximumEndHeel();
								}
								final CharterInMarket charterInMarket = sequence.getCharterInMarket();
								if(charterInMarket != null) {
									return charterInMarket.getGenericCharterContract().getEndHeel().getMaximumEndHeel();
								}
							}
						}

						return super.getComparable(object);
					}
				}),

				addColumn("endHeel", "End Heel", ColumnType.NORMAL, new BaseFormatter() {
					@Override
					public String render(final Object object) {

						if (object instanceof CargoAllocation event) {
							final Sequence sequence = event.getSequence();
							if (sequence != null) {
								Event endEvent = sequence.getEvents().get(sequence.getEvents().size() - 1);
								if (endEvent instanceof EndEvent) {
									return String.valueOf(endEvent.getHeelAtEnd());
								}
							}
						}
						return "";
					}

					@Override
					public Comparable<?> getComparable(final Object object) {

						if (object instanceof CargoAllocation event) {
							final Sequence sequence = event.getSequence();
							if (sequence != null) {
								Event endEvent = sequence.getEvents().get(sequence.getEvents().size() - 1);
								if (endEvent instanceof EndEvent) {
									return endEvent.getHeelAtEnd();
								}
							}
						}

						return super.getComparable(object);
					}
				}),

				addColumn("finalCargoFlex", "Final Cargo Flex", ColumnType.NORMAL, new BaseFormatter() {

					private int getValue(CargoAllocation cargoAllocation) {
						SlotAllocation loadSlotAllocation = cargoAllocation.getSlotAllocations().get(0);
						SlotAllocation dischargeSlotAllocation = cargoAllocation.getSlotAllocations().get(1);

						return calculateUpwardFlex(dischargeSlotAllocation) + calculateDownwardFlex(loadSlotAllocation);
					}

					@Override
					public String render(final Object object) {

						if (object instanceof CargoAllocation cargoAllocation) {
							return AbstractPNLCalcRowFactory.VolumeM3Format.format(getValue(cargoAllocation));
						}
						return "";
					}

					@Override
					public Comparable<?> getComparable(final Object object) {

						if (object instanceof CargoAllocation cargoAllocation) {
							return getValue(cargoAllocation);
						}

						return super.getComparable(object);
					}
				})));

		columns.addAll(createDischargePortColumns());
		columns.addAll(createLoadPortColumns());
		getBlockManager().makeAllBlocksVisible();
		columns.stream().forEach(x -> x.column.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				String text = x.getFormatter().render(cell.getElement());
				cell.setText(text);
				if(cell.getElement() instanceof CargoAllocation cargoAllocation) {
					final SlotAllocation loadSlotAllocation = cargoAllocation.getSlotAllocations().get(0);
					final SlotAllocation dischargeSlotAllocation = cargoAllocation.getSlotAllocations().get(1);
					
					final Sequence sequence = cargoAllocation.getSequence();
					int endHeel = 0;
					if (sequence != null) {
						Event endEvent = sequence.getEvents().get(sequence.getEvents().size() - 1);
						if (endEvent instanceof EndEvent) {
							endHeel = endEvent.getHeelAtEnd();
						}
					}
					int finalCargoFlex = calculateUpwardFlex(dischargeSlotAllocation) + calculateDownwardFlex(loadSlotAllocation);
					if(endHeel <= finalCargoFlex) {
						cell.setForeground(highlightColour);						
					}
				}
				
			}
			
		}));

	}

	private Collection<ColumnHandler> createLoadPortColumns() {
		ColumnBlock block = getBlockManager().createBlock("finalLoad", "Final Load", ColumnType.NORMAL);
		return List.of(addColumn(block, "ID", ColumnType.NORMAL, new BaseFormatter() {
			@Override
			public String render(final Object object) {

				if (object instanceof CargoAllocation cargoAllocation) {
					SlotVisit slotVisit = cargoAllocation.getSlotAllocations().get(0).getSlotVisit();
					return String.valueOf(slotVisit.getSlotAllocation().getSlot().getName());
				}

				return "";
			}

			@Override
			public Comparable<?> getComparable(final Object object) {

				if (object instanceof CargoAllocation cargoAllocation) {
					SlotVisit slotVisit = cargoAllocation.getSlotAllocations().get(0).getSlotVisit();
					return slotVisit.getSlotAllocation().getSlot().getName();
				}
				return super.getComparable(object);
			}
		}), addColumn(block, "Vol", ColumnType.NORMAL, new BaseFormatter() {
			@Override
			public String render(final Object object) {

				if (object instanceof CargoAllocation cargoAllocation) {
					SlotVisit slotVisit = cargoAllocation.getSlotAllocations().get(0).getSlotVisit();
					return AbstractPNLCalcRowFactory.VolumeM3Format.format(slotVisit.getSlotAllocation().getVolumeTransferred());
				}

				return "";
			}

			@Override
			public Comparable<?> getComparable(final Object object) {

				if (object instanceof CargoAllocation cargoAllocation) {
					SlotVisit slotVisit = cargoAllocation.getSlotAllocations().get(0).getSlotVisit();
					return slotVisit.getSlotAllocation().getVolumeTransferred();
				}
				return super.getComparable(object);
			}
		}),

				addColumn(block, "Min Vol", ColumnType.NORMAL, new BaseFormatter() {
					private int getValue(CargoAllocation cargoAllocation) {
						SlotAllocation slotAllocation = cargoAllocation.getSlotAllocations().get(0);
						int minQuantityM3 = getMinQuantityInM3(slotAllocation);
						return minQuantityM3;

					}

					@Override
					public String render(final Object object) {

						if (object instanceof CargoAllocation cargoAllocation) {
							return AbstractPNLCalcRowFactory.VolumeM3Format.format(getValue(cargoAllocation));
						}

						return "";
					}

					@Override
					public Comparable<?> getComparable(final Object object) {

						if (object instanceof CargoAllocation cargoAllocation) {
							return getValue(cargoAllocation);
						}

						return super.getComparable(object);
					}
				}),

				addColumn(block, "Downward Flex", ColumnType.NORMAL, new BaseFormatter() {

					private int getValue(final CargoAllocation cargoAllocation) {
						SlotAllocation slotAllocation = cargoAllocation.getSlotAllocations().get(0);
						return calculateDownwardFlex(slotAllocation);

					}

					@Override
					public String render(final Object object) {

						if (object instanceof CargoAllocation cargoAllocation) {
							return AbstractPNLCalcRowFactory.VolumeM3Format.format(getValue(cargoAllocation));
						}

						return "";
					}

					@Override
					public Comparable<?> getComparable(final Object object) {

						if (object instanceof CargoAllocation cargoAllocation) {
							return getValue(cargoAllocation);
						}

						return super.getComparable(object);
					}
				}));

	}

	private Collection<ColumnHandler> createDischargePortColumns() {
		ColumnBlock block = getBlockManager().createBlock("finalDischarge", "Final Discharge", ColumnType.NORMAL);

		return List.of(addColumn(block, "ID", ColumnType.NORMAL, new BaseFormatter() {
			@Override
			public String render(final Object object) {

				if (object instanceof CargoAllocation cargoAllocation) {
					SlotVisit slotVisit = cargoAllocation.getSlotAllocations().get(1).getSlotVisit();
					return String.valueOf(slotVisit.getSlotAllocation().getSlot().getName());
				}

				return "";
			}

			@Override
			public Comparable<?> getComparable(final Object object) {

				if (object instanceof CargoAllocation cargoAllocation) {
					SlotVisit slotVisit = cargoAllocation.getSlotAllocations().get(1).getSlotVisit();
					return slotVisit.getSlotAllocation().getSlot().getName();
				}
				return super.getComparable(object);
			}
		}),

				addColumn(block, "Vol", ColumnType.NORMAL, new BaseFormatter() {
					@Override
					public String render(final Object object) {

						if (object instanceof CargoAllocation cargoAllocation) {
							SlotVisit slotVisit = cargoAllocation.getSlotAllocations().get(1).getSlotVisit();
							return AbstractPNLCalcRowFactory.VolumeM3Format.format(slotVisit.getSlotAllocation().getVolumeTransferred());
						}

						return "";
					}

					@Override
					public Comparable<?> getComparable(final Object object) {

						if (object instanceof CargoAllocation cargoAllocation) {
							SlotVisit slotVisit = cargoAllocation.getSlotAllocations().get(1).getSlotVisit();
							return slotVisit.getSlotAllocation().getVolumeTransferred();
						}
						return super.getComparable(object);
					}
				}),

				addColumn(block, "Max Vol", ColumnType.NORMAL, new BaseFormatter() {
					private int getValue(CargoAllocation cargoAllocation) {
						SlotAllocation slotAllocation = cargoAllocation.getSlotAllocations().get(1);
						return getMaxQuantityInM3(slotAllocation);

					}

					@Override
					public String render(final Object object) {

						if (object instanceof CargoAllocation cargoAllocation) {
							return AbstractPNLCalcRowFactory.VolumeM3Format.format(getValue(cargoAllocation));
						}

						return "";
					}

					@Override
					public Comparable<?> getComparable(final Object object) {

						if (object instanceof CargoAllocation cargoAllocation) {
							return getValue(cargoAllocation);
						}

						return super.getComparable(object);
					}
				}),

				addColumn(block, "Upward Flex", ColumnType.NORMAL, new BaseFormatter() {
					private Integer getValue(final CargoAllocation cargoAllocation) {
						return calculateUpwardFlex(cargoAllocation.getSlotAllocations().get(1));
					}

					@Override
					public String render(final Object object) {

						if (object instanceof CargoAllocation cargoAllocation) {
							return AbstractPNLCalcRowFactory.VolumeM3Format.format(getValue(cargoAllocation));
						}

						return "";
					}

					@Override
					public Comparable<?> getComparable(final Object object) {

						if (object instanceof CargoAllocation cargoAllocation) {
							return getValue(cargoAllocation);
						}

						return super.getComparable(object);
					}
				}));
	}

	@Override
	protected ITreeContentProvider getContentProvider() {
		final ITreeContentProvider superProvider = super.getContentProvider();
		return new ITreeContentProvider() {

			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
				superProvider.inputChanged(viewer, oldInput, newInput);
			}

			@Override
			public void dispose() {
				superProvider.dispose();
			}

			@Override
			public boolean hasChildren(final Object element) {
				return superProvider.hasChildren(element);
			}

			@Override
			public Object getParent(final Object element) {
				return superProvider.getParent(element);
			}

			@Override
			public Object[] getElements(final Object inputElement) {
				clearInputEquivalents();
				final Object[] result = superProvider.getElements(inputElement);
				
				return result;
			}

			@Override
			public Object[] getChildren(final Object parentElement) {
				return superProvider.getChildren(parentElement);
			}
		};
	}

	@Override
	protected IScenarioInstanceElementCollector getElementCollector() {
		return new ScheduleElementCollector() {

			@Override
			public void beginCollecting(final boolean pinDiffMode) {
				clearPinModeData();
				super.beginCollecting(pinDiffMode);
			}

			@Override
			protected Collection<EObject> collectElements(final ScenarioResult scenarioInstance, final LNGScenarioModel scenarioModel, final Schedule schedule, final boolean pinned) {

				final Collection<EObject> collectedElements = new ArrayList<>();
				for (final Sequence sequence : schedule.getSequences()) {
					SlotVisit lastLoad = null;
					for (final Event e : sequence.getEvents()) {
						if (e instanceof SlotVisit visit) {
							if (visit.getSlotAllocation().getSlot() instanceof LoadSlot && visit.getSlotAllocation().getCargoAllocation().getCargoType() == CargoType.FLEET) {
								lastLoad = visit;
							}
						}
					}
					if (lastLoad != null && lastLoad.getSlotAllocation() != null) {
						if(sequence.getVesselCharter() != null || 
								(sequence.getCharterInMarket() != null && sequence.getCharterInMarket().getGenericCharterContract() != null)) {
							collectedElements.add(lastLoad.getSlotAllocation().getCargoAllocation());
						}
					}
				}
				final List<EObject> elements = new ArrayList<>(collectedElements);
				collectPinModeElements(elements, pinned);
				return collectedElements;
			}

		};
	}

	private int getMaxQuantityInM3(SlotAllocation slotAllocation) {
		int maxQuantityM3 = 0;
		if (slotAllocation.getSlot().getSlotOrDelegateVolumeLimitsUnit() == VolumeUnits.M3) {
			maxQuantityM3 = slotAllocation.getSlot().getSlotOrDelegateMaxQuantity();
		} else if (slotAllocation.getSlot().getSlotOrDelegateVolumeLimitsUnit() == VolumeUnits.MMBTU) {
			maxQuantityM3 = (int) (slotAllocation.getSlot().getSlotOrDelegateMaxQuantity() / slotAllocation.getCv());
		}
		return maxQuantityM3;
	}

	private int getMinQuantityInM3(SlotAllocation slotAllocation) {
		int maxQuantityM3 = 0;
		if (slotAllocation.getSlot().getSlotOrDelegateVolumeLimitsUnit() == VolumeUnits.M3) {
			maxQuantityM3 = slotAllocation.getSlot().getSlotOrDelegateMinQuantity();
		} else if (slotAllocation.getSlot().getSlotOrDelegateVolumeLimitsUnit() == VolumeUnits.MMBTU) {
			maxQuantityM3 = (int) (slotAllocation.getSlot().getSlotOrDelegateMinQuantity() / slotAllocation.getCv());
		}
		return maxQuantityM3;
	}

	private double getFillCapacityInM3(Vessel vessel) {
		return vessel.getVesselOrDelegateCapacity() * vessel.getVesselOrDelegateFillCapacity();
	}

	private Integer calculateUpwardFlex(SlotAllocation slotAllocation) {
		int maxQuantityM3 = getMaxQuantityInM3(slotAllocation);
		int quantity = slotAllocation.getVolumeTransferred();
		Vessel vessel = ScheduleModelUtils.getVessel(slotAllocation.getSlotVisit().getSequence());
		if (vessel != null) {
			return Integer.min(maxQuantityM3, (int) getFillCapacityInM3(vessel)) - quantity;
		} else {
			return null;
		}
	}

	private int calculateDownwardFlex(SlotAllocation slotAllocation) {
		int minQuantityM3 = getMinQuantityInM3(slotAllocation);

		int quantity = slotAllocation.getVolumeTransferred();
		return quantity - minQuantityM3;
	}

	@Override
	protected boolean handleSelections() {
		return true;
	}

	public ColumnHandler addColumn(final String blockID, final String title, final ColumnType columnType, final ICellRenderer formatter, final ETypedElement... path) {
		final ColumnBlock block = getBlockManager().createBlock(blockID, title, columnType);
		return getBlockManager().createColumn(block, title, formatter, path);
	}

	public ColumnHandler addColumn(final ColumnBlock block, final String title, final ColumnType columnType, final ICellRenderer formatter, final ETypedElement... path) {
		return getBlockManager().createColumn(block, title, formatter, path);
	}
	@Override
	public void dispose() {
		localResourceManager.dispose();
		super.dispose();
	}

	@Override
	public <T> T getAdapter(final Class<T> adapter) {

		if (IReportContentsGenerator.class.isAssignableFrom(adapter)) {
			return adapter.cast(ReportContentsGenerators.createJSONFor(selectedScenariosServiceListener, viewer.getGrid()));
		}
		return super.getAdapter(adapter);
	}
}

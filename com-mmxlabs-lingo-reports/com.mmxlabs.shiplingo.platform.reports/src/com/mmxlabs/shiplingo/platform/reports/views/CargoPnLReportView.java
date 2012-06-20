/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.common.CollectionsUtil;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.schedule.AdditionalData;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.shiplingo.platform.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.shiplingo.platform.reports.ScheduleElementCollector;

public class CargoPnLReportView extends EMFReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.CargoPnLReportView";

	public CargoPnLReportView() {
		super("com.mmxlabs.shiplingo.platform.reports.CargoPnLReportView");

		final SchedulePackage s = SchedulePackage.eINSTANCE;

		final EAttribute name = MMXCorePackage.eINSTANCE.getNamedObject_Name();

		addColumn("Schedule", containingScheduleFormatter);

		addColumn("ID", objectFormatter, s.getCargoAllocation__GetName());

		addColumn("Type", objectFormatter, s.getCargoAllocation__GetType());

		addColumn("Load Port", objectFormatter, s.getCargoAllocation_LoadAllocation(), s.getSlotAllocation__GetPort(), name);
		addColumn("Load Date", datePartFormatter, s.getCargoAllocation_LoadAllocation(), s.getSlotAllocation__GetLocalStart());
		addColumn("Purchase Contract", objectFormatter, s.getCargoAllocation_LoadAllocation(), s.getSlotAllocation__GetContract(), name);
		addColumn("Purchase Price", objectFormatter, s.getCargoAllocation_LoadAllocation(), s.getSlotAllocation_Price());

		addColumn("Discharge Port", objectFormatter, s.getCargoAllocation_DischargeAllocation(), s.getSlotAllocation__GetPort(), name);
		addColumn("Discharge Date", datePartFormatter, s.getCargoAllocation_DischargeAllocation(), s.getSlotAllocation__GetLocalStart());
		addColumn("Sales Contract", objectFormatter, s.getCargoAllocation_DischargeAllocation(), s.getSlotAllocation__GetContract(), name);
		addColumn("Sales Price", objectFormatter, s.getCargoAllocation_DischargeAllocation(), s.getSlotAllocation_Price());

		addColumn("Vessel", objectFormatter, s.getCargoAllocation_Sequence(), SchedulePackage.eINSTANCE.getSequence__GetName());

	}

	@Override
	protected boolean handleSelections() {
		return true;
	}

	@Override
	protected Class<?> getSelectionAdaptionClass() {
		return CargoAllocation.class;
	}

	@Override
	protected IStructuredContentProvider getContentProvider() {
		final IStructuredContentProvider superProvider = super.getContentProvider();
		return new IStructuredContentProvider() {
			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
				superProvider.inputChanged(viewer, oldInput, newInput);
				Display.getCurrent().asyncExec(new Runnable() {
					@Override
					public void run() {
						if (viewer.getControl().isDisposed()) {
							return;
						}

						if (newInput instanceof IScenarioViewerSynchronizerOutput) {
							final IScenarioViewerSynchronizerOutput synchronizerOutput = (IScenarioViewerSynchronizerOutput) newInput;
							final Collection<MMXRootObject> rootObjects = synchronizerOutput.getRootObjects();
							for (final String s : entityColumnNames) {
								removeColumn(s);
							}

							entityColumnNames.clear();

							final TreeSet<String> entities = new TreeSet<String>();
							for (final MMXRootObject rootObject : rootObjects) {

								final CommercialModel commercialModel = rootObject.getSubModel(CommercialModel.class);
								final LegalEntity shippingEntity = commercialModel.getShippingEntity();
								if (shippingEntity != null) {
									entities.add(shippingEntity.getName());
								}
							}
							for (final String entity : entities) {
								addEntityColumn(entity);
							}
						}

						viewer.refresh();
					}
				});
			}

			@Override
			public void dispose() {
				superProvider.dispose();
			}

			@Override
			public Object[] getElements(final Object object) {
				clearInputEquivalents();
				final Object[] result = superProvider.getElements(object);

				for (final Object a : result) {
					// map to events
					if (a instanceof CargoAllocation) {
						final CargoAllocation allocation = (CargoAllocation) a;

						setInputEquivalents(
								allocation,
								Arrays.asList(new Object[] { allocation.getLoadAllocation().getSlotVisit(), allocation.getLoadAllocation().getSlot(),
										allocation.getDischargeAllocation().getSlotVisit(), allocation.getDischargeAllocation().getSlot(), allocation.getBallastIdle(), allocation.getBallastLeg(),
										allocation.getLadenIdle(), allocation.getLadenLeg() }));
					}
				}

				return result;
			}
		};
	}

	@Override
	protected IScenarioInstanceElementCollector getElementCollector() {
		return new ScheduleElementCollector() {
			@Override
			protected Collection<? extends Object> collectElements(final Schedule schedule) {
				return schedule.getCargoAllocations();
			}
		};
	}

	private final List<String> entityColumnNames = new ArrayList<String>();

	private void addEntityColumn(final String entityName) {

		final String title = /* entity.getName() + */"P&L";
		entityColumnNames.add(title);
		addColumn(title, new IntegerFormatter() {
			@Override
			public Integer getIntValue(final Object object) {
				if (object instanceof CargoAllocation) {
					// display P&L
					int value = 0;

					final CargoAllocation allocation = (CargoAllocation) object;
					// TODO: make key well known
					final AdditionalData data = allocation.getAdditionalDataWithPath(CollectionsUtil.makeArrayList(entityName, "pnl"));
					if (data != null) {
						value = data.getIntValue();
					}
					// if ((allocation.getLoadRevenue() != null) && entity.equals(allocation.getLoadRevenue().getEntity())) {
					// value += allocation.getLoadRevenue().getValue();
					// }
					// if ((allocation.getShippingRevenue() != null) && entity.equals(allocation.getShippingRevenue().getEntity())) {
					// value += allocation.getShippingRevenue().getValue();
					// }
					// if ((allocation.getDischargeRevenue() != null) && entity.equals(allocation.getDischargeRevenue().getEntity())) {
					// value += allocation.getDischargeRevenue().getValue();
					// }
					return value;
				}

				return null;
			}
		});
	}

}

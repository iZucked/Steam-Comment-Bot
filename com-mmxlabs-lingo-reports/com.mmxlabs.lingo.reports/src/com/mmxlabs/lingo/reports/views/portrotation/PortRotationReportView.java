/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.portrotation;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.google.inject.Inject;
import com.mmxlabs.lingo.reports.IScenarioInstanceElementCollector;
import com.mmxlabs.lingo.reports.ScheduledEventCollector;
import com.mmxlabs.lingo.reports.components.ColumnBlock;
import com.mmxlabs.lingo.reports.components.ColumnType;
import com.mmxlabs.lingo.reports.extensions.EMFReportColumnManager;
import com.mmxlabs.lingo.reports.views.AbstractConfigurableReportView;
import com.mmxlabs.lingo.reports.views.portrotation.extpoint.IPortRotationBasedColumnExtension;
import com.mmxlabs.lingo.reports.views.portrotation.extpoint.IPortRotationBasedColumnFactoryExtension;
import com.mmxlabs.lingo.reports.views.portrotation.extpoint.IPortRotationBasedReportInitialStateExtension;
import com.mmxlabs.lingo.reports.views.portrotation.extpoint.IPortRotationBasedReportInitialStateExtension.InitialColumn;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

/**
 * 
 */
public class PortRotationReportView extends AbstractConfigurableReportView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.mmxlabs.shiplingo.platform.reports.views.PortRotationReportView";

	private final PortRotationBasedReportBuilder builder;

	@Inject(optional = true)
	private Iterable<IPortRotationBasedColumnFactoryExtension> columnFactoryExtensions;

	@Inject(optional = true)
	private Iterable<IPortRotationBasedColumnExtension> columnExtensions;

	@Inject(optional = true)
	private Iterable<IPortRotationBasedReportInitialStateExtension> initialStates;

	@Inject
	public PortRotationReportView(final PortRotationBasedReportBuilder builder) {

		super("com.mmxlabs.shiplingo.platform.reports.PortRotationReportView");
		this.builder = builder;
		builder.setReport(this);

	}

	/**
	 * Check the view extension point to see if we can enable the customise dialog
	 * 
	 * @return
	 */
	@Override
	protected boolean checkCustomisable() {

		if (initialStates != null) {

			for (final IPortRotationBasedReportInitialStateExtension ext : initialStates) {

				final String viewId = ext.getViewID();

				if (viewId != null && viewId.equals(getViewSite().getId())) {
					final String customisableString = ext.getCustomisable();
					if (customisableString != null) {
						return customisableString.equals("true");
					}
				}
			}
		}
		return true;
	}

	/**
	 * Examine the view extension point to determine the default set of columns, order,row types and diff options.
	 */
	@Override
	protected void setInitialState() {

		if (initialStates != null) {

			for (final IPortRotationBasedReportInitialStateExtension ext : initialStates) {

				final String viewId = ext.getViewID();

				// Is this a matching view definition?
				if (viewId != null && viewId.equals(getViewSite().getId())) {
					// Get visibile columns and order
					{
						final InitialColumn[] initialColumns = ext.getInitialColumns();
						if (initialColumns != null) {
							final List<String> defaultOrder = new LinkedList<>();
							for (final InitialColumn col : initialColumns) {
								final String blockID = col.getID();
								ColumnBlock block = getBlockManager().getBlockByID(blockID);
								if (block == null) {
									block = getBlockManager().createBlock(blockID, "", ColumnType.NORMAL);
								}
								block.setUserVisible(true);
								defaultOrder.add(blockID);

							}
							getBlockManager().setBlockIDOrder(defaultOrder);
						}
					}

					break;
				}
			}
		}
	}

	@Override
	protected boolean handleSelections() {
		return true;
	}

	@Override
	protected ITreeContentProvider getContentProvider() {
		final ITreeContentProvider superProvider = super.getContentProvider();
		return new ITreeContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				superProvider.inputChanged(viewer, oldInput, newInput);
			}

			@Override
			public void dispose() {
				superProvider.dispose();
			}

			@Override
			public boolean hasChildren(Object element) {
				return superProvider.hasChildren(element);
			}

			@Override
			public Object getParent(Object element) {
				return superProvider.getParent(element);
			}

			@Override
			public Object[] getElements(Object inputElement) {
				clearInputEquivalents();
				final Object[] result = superProvider.getElements(inputElement);

				for (final Object event : result) {
					if (event instanceof SlotVisit) {
						setInputEquivalents(event, Arrays.asList(new Object[] { ((SlotVisit) event).getSlotAllocation().getCargoAllocation() }));
					} else if (event instanceof VesselEventVisit) {
						setInputEquivalents(event, Arrays.asList(new Object[] { ((VesselEventVisit) event).getVesselEvent() }));
					} else {
						setInputEquivalents(event, Collections.emptyList());
					}
				}

				return result;
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				return superProvider.getChildren(parentElement);
			}
		};
	}

	@Override
	protected IScenarioInstanceElementCollector getElementCollector() {
		return new ScheduledEventCollector();
	}

	/**
	 * Examine the eclipse registry for defined columns for this report and hook them in.
	 */
	@Override
	protected void registerReportColumns() {

		final EMFReportColumnManager manager = new EMFReportColumnManager();

		// Find any shared column factories and install.
		final Map<String, IPortRotationColumnFactory> handlerMap = new HashMap<>();
		if (columnFactoryExtensions != null) {
			for (final IPortRotationBasedColumnFactoryExtension ext : columnFactoryExtensions) {
				final String handlerID = ext.getHandlerID();
				handlerMap.put(handlerID, ext.getFactory());
			}
		}

		// Now find the column definitions themselves.
		if (columnExtensions != null) {

			for (final IPortRotationBasedColumnExtension ext : columnExtensions) {
				IPortRotationColumnFactory factory;
				if (ext.getHandlerID() != null) {
					factory = handlerMap.get(ext.getHandlerID());
				} else {
					factory = ext.getFactory();
				}
				if (factory != null) {
					String columnID = ext.getColumnID();
					factory.registerColumn(columnID, manager, builder);
				}
			}
		}

		// Create the actual columns instances.
		manager.addColumns(PortRotationBasedReportBuilder.PORT_ROTATION_REPORT_TYPE_ID, getBlockManager());
	}
}

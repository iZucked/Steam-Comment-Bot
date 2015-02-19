package com.mmxlabs.lingo.reports.diff.actions;

import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.mmxlabs.lingo.reports.internal.Activator.Implementation;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;

public class FilterScheduleReportBySelection implements IWorkbenchWindowActionDelegate {
	private static final String SCHEDULE_VIEW_ID = "com.mmxlabs.shiplingo.platform.reports.views.SchedulePnLReport";
	// private GridTreeViewer viewer;

	private IViewPart scheduleView;
	private IPartListener listener;
	private Table table;
	private IWorkbenchWindow window;
	private IAction action;

	@Override
	public void run(final IAction action) {
		if (table != null) {
			table.getOptions().setFilterSelectedElements(action.isChecked());
		}
	}

	@Override
	public void selectionChanged(final IAction action, final ISelection selection) {
		this.action = action;
		if (table != null && action != null) {
			setActionChecked(table.getOptions().isFilterSelectedElements());
		}

	}

	private final EContentAdapter adapter = new EContentAdapter() {
		@Override
		public void notifyChanged(final org.eclipse.emf.common.notify.Notification notification) {
			super.notifyChanged(notification);
			if (notification.getFeature() == ScheduleReportPackage.Literals.DIFF_OPTIONS__FILTER_SELECTED_ELEMENTS) {
				setActionChecked(notification.getNewBooleanValue());
			}
		}
	};

	@Override
	public void dispose() {
		window.getPartService().removePartListener(listener);
	}

	@Override
	public void init(final IWorkbenchWindow window) {
		this.window = window;
		hookToScheduleView(window);
	}

	protected void hookToScheduleView(final IWorkbenchWindow window) {
		listener = new IPartListener() {

			@Override
			public void partOpened(final IWorkbenchPart part) {
				if (part instanceof IViewPart) {
					final IViewPart viewPart = (IViewPart) part;
					if (viewPart.getViewSite().getId().equals(SCHEDULE_VIEW_ID)) {
						scheduleView = viewPart;
						observeInput((Table) scheduleView.getAdapter(Table.class));
					}
				}
			}

			private void observeInput(final Table table) {
				if (table != null) {
					table.eAdapters().remove(adapter);
				}

				if (FilterScheduleReportBySelection.this.table != table) {
					FilterScheduleReportBySelection.this.table = table;
					if (table != null && action != null) {
						setActionChecked(table.getOptions().isFilterSelectedElements());
					}
					if (table != null) {
						table.eAdapters().add(adapter);
					}
				}

			}

			@Override
			public void partDeactivated(final IWorkbenchPart part) {

			}

			@Override
			public void partClosed(final IWorkbenchPart part) {
				if (part instanceof IViewPart) {
					final IViewPart viewPart = (IViewPart) part;
					if (viewPart.getViewSite().getId().equals(SCHEDULE_VIEW_ID)) {
						scheduleView = null;
						observeInput(null);
					}
				}

			}

			@Override
			public void partBroughtToTop(final IWorkbenchPart part) {

			}

			@Override
			public void partActivated(final IWorkbenchPart part) {
				if (part instanceof IViewPart) {
					final IViewPart viewPart = (IViewPart) part;
					if (viewPart.getViewSite().getId().equals(SCHEDULE_VIEW_ID)) {
						scheduleView = viewPart;
						observeInput((Table) scheduleView.getAdapter(Table.class));
					}
				}
			}
		};
		window.getPartService().addPartListener(listener);
		for (final IViewPart view : window.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViews()) {
			if (view.getViewSite().getId().equals(SCHEDULE_VIEW_ID)) {
				listener.partOpened(view);
			}
		}
	}

	public void setActionChecked(boolean checked) {
		if (action != null) {
			action.setChecked(checked);
		}
	}

}

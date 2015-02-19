package com.mmxlabs.lingo.reports.diff.actions;

import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;

public class FilterBySelection implements IWorkbenchWindowActionDelegate {
	private static final String SCHEDULE_VIEW_ID = "com.mmxlabs.shiplingo.platform.reports.views.SchedulePnLReport";
//	private GridTreeViewer viewer;

	private IViewPart scheduleView;
	private IPartListener listener;
	private Table table;
	private IWorkbenchWindow window;
	private IAction action;

	@Override
	public void run(IAction action) {
		// TODO Auto-generated method stub
		if (table != null) {
			table.getOptions().setFilterSelectedElements(action.isChecked());
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.action = action;
		// TODO Auto-generated method stub
		if (table != null && action != null) {
			action.setChecked(table.getOptions().isFilterSelectedElements());
		}

	}

	private EContentAdapter adapter = new EContentAdapter() {
		public void notifyChanged(org.eclipse.emf.common.notify.Notification notification) {
			super.notifyChanged(notification);
			if (notification.getFeature() == ScheduleReportPackage.Literals.DIFF_OPTIONS__FILTER_SELECTED_ELEMENTS) {
				if (action != null) {
					action.setChecked(notification.getNewBooleanValue());
				}
			}
		}
	};

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		window.getPartService().removePartListener(listener);
	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
		// TODO Auto-generated method stub
hookToScheduleView(window);
	}

	protected void hookToScheduleView(IWorkbenchWindow window) {
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
				// FilterBySelection.this.is

				if (table != null) {
					table.eAdapters().remove(adapter);
				}

				if (FilterBySelection.this.table != table) {
//					viewer.setInput(table);
					FilterBySelection.this.table = table;
					if (table != null && action != null) {
						action.setChecked(table.getOptions().isFilterSelectedElements());
					}
					//
					// if (newUserGroupAction != null) {
					// newUserGroupAction.setTable(table);
					// }

					// TODO: Here we want to trigger the schedule report view filters.
					// TODO: Add selected to the data model.
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
				// TODO Auto-generated method stub

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
	}
}

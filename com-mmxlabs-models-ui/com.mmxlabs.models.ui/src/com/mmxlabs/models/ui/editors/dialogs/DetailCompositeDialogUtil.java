/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.ui.editors.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.IntSupplier;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;

import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.scenario.service.model.manager.ScenarioLock;
import com.mmxlabs.scenario.service.model.util.MMXAdaptersAwareCommandStack;

public class DetailCompositeDialogUtil {

	private DetailCompositeDialogUtil() {

	}

	public static int editSingleObject(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final EObject target) {
		return editSingleObject(scenarioEditingLocation, target, null);
	}

	public static int editNewObjectWithUndoOnCancel(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final EObject target, @NonNull Command setCommand) {

		final ScenarioLock editorLock = scenarioEditingLocation.getEditorLock();
		ExecutorService executor = Executors.newSingleThreadExecutor();
		try {
			Future<Boolean> locked = executor.submit(() -> editorLock.tryLock(500));
			if (locked.get() == Boolean.TRUE) {
				try {
					scenarioEditingLocation.setDisableUpdates(true);
					try {
						
						final CommandStack commandStack = scenarioEditingLocation.getEditingDomain().getCommandStack();
						commandStack.execute(setCommand);
						Command undoCommand = commandStack.getMostRecentCommand();
						
						final DetailCompositeDialog dcd = new DetailCompositeDialog(scenarioEditingLocation.getShell(), scenarioEditingLocation.getDefaultCommandHandler());
						final int ret = dcd.open(scenarioEditingLocation, scenarioEditingLocation.getRootObject(), Collections.<EObject> singletonList(target), scenarioEditingLocation.isLocked());
						if (ret != Window.OK) {
							if (undoCommand != null) {
								// If not ok, revert state
								if (commandStack.getUndoCommand() != undoCommand) {
									throw new IllegalStateException("Command stack has changed");
								}
								undoCommand(commandStack);
							}
						}
						return ret;
					} finally {
						scenarioEditingLocation.setDisableUpdates(false);
					}
				} finally {
					Future<?> unlocked = executor.submit(editorLock::unlock);
					unlocked.get();
				}
			}
			return Window.CANCEL;
		} catch (Exception e) {
			return Window.CANCEL;

		} finally {
			executor.shutdownNow();
		}
	}

	public static int editSingleObjectWithUndoOnCancel(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final EObject target, @Nullable Command undoCommand) {
		return editSingleObject(scenarioEditingLocation, target, () -> {
			if (undoCommand != null) {
				final CommandStack commandStack = scenarioEditingLocation.getEditingDomain().getCommandStack();
				// If not ok, revert state;
				assert commandStack.getUndoCommand() == undoCommand;
				undoCommand(commandStack);
			}
		});
	}

	public static int editSingleObject(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final EObject target, @Nullable final Runnable notOkRunnable) {

		return editInlock(scenarioEditingLocation, () -> {
			final DetailCompositeDialog dcd = new DetailCompositeDialog(scenarioEditingLocation.getShell(), scenarioEditingLocation.getDefaultCommandHandler());
			final int ret = dcd.open(scenarioEditingLocation, scenarioEditingLocation.getRootObject(), Collections.<EObject> singletonList(target), false);
			if (ret != Window.OK) {
				if (notOkRunnable != null) {
					notOkRunnable.run();
				}
			}
			return ret;
		});
	}

	public static int editSelection(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final IStructuredSelection structuredSelection) {

		return editSelection(scenarioEditingLocation, structuredSelection, null);
	}

	public static int editSelection(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final IStructuredSelection structuredSelection, @Nullable final Runnable notOKAction) {

		if (structuredSelection.isEmpty() == false) {
			if (structuredSelection.size() == 1) {
				return editInlock(scenarioEditingLocation, () -> {
					final DetailCompositeDialog dcd = new DetailCompositeDialog(scenarioEditingLocation.getShell(), scenarioEditingLocation.getDefaultCommandHandler());
					final int ret = dcd.open(scenarioEditingLocation, scenarioEditingLocation.getRootObject(), structuredSelection.toList(), false);
					if (ret != Window.OK) {
						if (notOKAction != null) {
							notOKAction.run();
						}
					}
					return ret;
				});
			} else {
				return editInlock(scenarioEditingLocation, () -> {
					final MultiDetailDialog mdd = new MultiDetailDialog(scenarioEditingLocation.getShell(), scenarioEditingLocation.getRootObject(),
							scenarioEditingLocation.getDefaultCommandHandler());
					final int ret = mdd.open(scenarioEditingLocation, structuredSelection.toList());
					if (ret != Window.OK) {
						if (notOKAction != null) {
							notOKAction.run();
						}
					}
					return ret;
				});
			}
		}
		return IStatus.OK;
	}

	public static int editInlock(@NonNull final IScenarioEditingLocation scenarioEditingLocation, final IntSupplier editFunc) {
		final ScenarioLock editorLock = scenarioEditingLocation.getEditorLock();
		if (editorLock.tryLock(500)) {
			try {
				scenarioEditingLocation.setDisableUpdates(true);
				try {
					return editFunc.getAsInt();
				} finally {
					scenarioEditingLocation.setDisableUpdates(false);
				}
			} finally {
				editorLock.unlock();
			}
		}
		return Window.CANCEL;
	}

	public static int editInLockWithNewThread(@NonNull final IScenarioEditingLocation scenarioEditingLocation, final IntSupplier editFunc) {
		final ScenarioLock editorLock = scenarioEditingLocation.getEditorLock();
		ExecutorService executor = Executors.newSingleThreadExecutor();
		try {
			Future<Boolean> locked = executor.submit(() -> editorLock.tryLock(500));
			if (locked.get() == Boolean.TRUE) {
				try {
					scenarioEditingLocation.setDisableUpdates(true);
					try {
						return editFunc.getAsInt();
					} finally {
						scenarioEditingLocation.setDisableUpdates(false);
					}
				} finally {
					Future<?> unlocked = executor.submit(editorLock::unlock);
					unlocked.get();
				}
			}
			return Window.CANCEL;
		} catch (Exception e) {
			return Window.CANCEL;

		} finally {
			executor.shutdownNow();
		}
	}

	public static int editNewMultiObjectWithUndoOnCancel(@NonNull final IScenarioEditingLocation scenarioEditingLocation, @NonNull final Collection<EObject> target, @NonNull Command setCommand) {
		final ScenarioLock editorLock = scenarioEditingLocation.getEditorLock();
		ExecutorService executor = Executors.newSingleThreadExecutor();
		try {
			Future<Boolean> locked = executor.submit(() -> editorLock.tryLock(500));
			if (locked.get() == Boolean.TRUE) {
				try {
					scenarioEditingLocation.setDisableUpdates(true);
					try {
						
						final CommandStack commandStack = scenarioEditingLocation.getEditingDomain().getCommandStack();
						commandStack.execute(setCommand);								
						Command undoCommand = commandStack.getMostRecentCommand();
						
						if (target.size() == 1) {
							final DetailCompositeDialog dcd = new DetailCompositeDialog(scenarioEditingLocation.getShell(), scenarioEditingLocation.getDefaultCommandHandler());
							final int ret = dcd.open(scenarioEditingLocation, scenarioEditingLocation.getRootObject(), target, scenarioEditingLocation.isLocked());
							if (ret != Window.OK) {
								if (undoCommand != null) {
									// If not ok, revert state
									if (commandStack.getUndoCommand() != undoCommand) {
										throw new IllegalStateException("Command stack has changed");
									}
									undoCommand(commandStack);
								}
							}
							return ret;
						}
						else {
								final MultiDetailDialog mdd = new MultiDetailDialog(scenarioEditingLocation.getShell(), scenarioEditingLocation.getRootObject(),
										scenarioEditingLocation.getDefaultCommandHandler());
								final int ret = mdd.open(scenarioEditingLocation, new ArrayList<EObject>(target));
								if (ret != Window.OK) {
									if (undoCommand != null) {
										// If not ok, revert state
										if (commandStack.getUndoCommand() != undoCommand) {
											throw new IllegalStateException("Command stack has changed");
										}
										undoCommand(commandStack);
									}
								}
								return ret;
						}
					} 
					finally {
						scenarioEditingLocation.setDisableUpdates(false);
					}
				} finally {
					Future<?> unlocked = executor.submit(editorLock::unlock);
					unlocked.get();
				}
			}
			return Window.CANCEL;
		} catch (Exception e) {
			return Window.CANCEL;
		} finally {
			executor.shutdownNow();
		}
	}

	protected static void undoCommand(final CommandStack commandStack) {
		if (commandStack instanceof MMXAdaptersAwareCommandStack) {
			((MMXAdaptersAwareCommandStack)commandStack).undo(false);
		}
		else {
			commandStack.undo();
		}
	}
}

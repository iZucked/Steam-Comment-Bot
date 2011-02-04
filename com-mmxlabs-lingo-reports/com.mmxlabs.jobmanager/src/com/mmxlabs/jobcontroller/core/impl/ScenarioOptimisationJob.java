/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.jobcontroller.core.impl;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.progress.IProgressConstants2;

import scenario.Scenario;
import scenario.optimiser.lso.LSOSettings;
import scenario.schedule.Schedule;

import com.mmxlabs.common.Pair;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.jobcontroller.core.IManagedJobListener;
import com.mmxlabs.jobcontroller.emf.AnnotatedSolutionExporter;
import com.mmxlabs.jobcontroller.emf.IncompleteScenarioException;
import com.mmxlabs.jobcontroller.emf.LNGScenarioTransformer;
import com.mmxlabs.jobcontroller.emf.ModelEntityMap;
import com.mmxlabs.jobcontroller.emf.optimisationsettings.OptimisationTransformer;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IOptimiser;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.IOptimiserProgressMonitor;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;

//TODO Generate a base class and provide some methods for job creation etc.

public class ScenarioOptimisationJob implements IManagedJob {

	private final Set<IManagedJobListener> managedJobListeners = new HashSet<IManagedJobListener>();

	private final String name;
	private Job job;

	private int totalProgress;
	private int progress;

	private JobState state = JobState.UNKNOWN;

	private boolean paused;
	private final Object pauseLock = new Object();

	private IOptimisationContext<ISequenceElement> context;

	private IAnnotatedSolution<ISequenceElement> schedule = null;

	private final Scenario scenario;
	
	/**
	 * Map between EMF entities and builder objects.
	 */
	private final ModelEntityMap entities = new ModelEntityMap();
	

	public ScenarioOptimisationJob(final String name, final Scenario scenario) {
		this.name = name;

		this.scenario = scenario;
	}

	@Override
	public void init() {
		progress = NO_PROGRESS;
		totalProgress = IProgressMonitor.UNKNOWN;

		state = JobState.INITIALISED;

		// updateSchedule(context.getInitialSequences());
	}

	/**
	 * Hacky accessor for easy linking to the scheduleview. Should really be a
	 * sub interface or adaptable object
	 */
	@Override
	public IAnnotatedSolution<ISequenceElement> getSchedule() {
		return schedule;
	}

	@Override
	public void start() {

		if (!(state == JobState.INITIALISED || state == JobState.COMPLETED || state == JobState.ABORTED)) {
			// Already running!
			return;
		}

		progress = 0;

		assert job == null;

		job = new Job(name) {

			@Override
			protected IStatus run(final IProgressMonitor monitor) {

			
				final IOptimiserProgressMonitor<ISequenceElement> optMonitor = new IOptimiserProgressMonitor<ISequenceElement>() {
					long firstFitness;
					int lastReport = 0;
					private long startTime;

					@Override
					public void report(final IOptimiser<ISequenceElement> arg0,
							final int iteration, final long currentFitness,
							final long bestFitness,
							final IAnnotatedSolution<ISequenceElement> currentState,
							final IAnnotatedSolution<ISequenceElement> bestState) {

						final int work = iteration - lastReport;
						monitor.worked(work);
						lastReport = iteration;
						progress += work;
						fireProgressUpdate(work);

						System.out.println("Iteration: " + iteration
								+ " Best Fitness: " + bestFitness
								+ " Current Fitness: " + currentFitness
								+ " gain: " + 100
								* (firstFitness - bestFitness)
								/ (double) firstFitness + "%" + " Wall time : "
								+ (System.currentTimeMillis() - startTime)
								/ 1000 + " s");

						// TODO: We should verify delta fitness is equal to a
						// whole new fitness
						// As currently the fitness report shows different
						// results!

						// HERE: Process the solution in a runnable and update
						// getSchedule() output
//						updateSchedule(bestState, iteration);

						// paused = true;

						synchronized (pauseLock) {
							boolean wasPaused = false;

							while (paused) {

								// Complete state change from PAUSING to PAUSED
								if (!wasPaused) {
									assert state == JobState.PAUSING;
									state = JobState.PAUSED;
									fireJobPaused();
									wasPaused = true;
								}

								try {
									pauseLock.wait();
								} catch (final InterruptedException e) {
									// Ignore -- log?
								}
							}

							// Complete state change from RESUMED to RUNNING
							if (wasPaused) {
								assert state == JobState.RESUMING;
								state = JobState.RUNNING;
								fireJobResumed();
							}
						}

						if (monitor.isCanceled()) {
							throw new RuntimeException("Operation cancelled");
						}

					}

					@Override
					public void done(final IOptimiser<ISequenceElement> arg0,
							final long bestFitness,
							final IAnnotatedSolution<ISequenceElement> arg2) {

						System.out.println("Done. Best Fitness: " + bestFitness
								+ " gain: " + 100
								* (firstFitness - bestFitness)
								/ (double) firstFitness + "%" + " Wall time : "
								+ (System.currentTimeMillis() - startTime)
								/ 1000 + " s");
						
						updateSchedule(arg2, -1);
						

					}

					@Override
					public void begin(final IOptimiser<ISequenceElement> arg0,
							final long arg1,
							final IAnnotatedSolution<ISequenceElement> arg2) {
						firstFitness = arg1;
						updateSchedule(arg2, 0);
						startTime = System.currentTimeMillis();
					}
				};

				try {
					final LNGScenarioTransformer lst = new LNGScenarioTransformer(
							scenario);
	
					// Extract number of steps from settings prior to setting progress monitor
					if (lst.getOptimisationSettings() instanceof LSOSettings) {
						totalProgress = ((LSOSettings)lst.getOptimisationSettings()).getNumberOfSteps();
					}
					
					// TODO: If an exception is thrown before we get here - will monitor.done() be a problem?
					monitor.beginTask("Managed Job", totalProgress);
					
					monitor.subTask("Pre-process scenario");

					final OptimisationTransformer ot = new OptimisationTransformer(
							lst.getOptimisationSettings());
					try {
						final IOptimisationData<ISequenceElement> data = lst
								.createOptimisationData(entities);

						final Pair<IOptimisationContext<ISequenceElement>, LocalSearchOptimiser<ISequenceElement>> optAndContext = ot
								.createOptimiserAndContext(data, entities);

						monitor.subTask("Prepare optimisation");

						context = optAndContext.getFirst();
						final LocalSearchOptimiser<ISequenceElement> optimiser = optAndContext
								.getSecond();

						optimiser.setProgressMonitor(optMonitor);
						optimiser.init();

//						final IFitnessEvaluator<ISequenceElement> fitnessEvaluator = optimiser
//								.getFitnessEvaluator();

						// monitor.subTask("Evaluate fitness of initial solution");
						//
						// final
						// LinearSimulatedAnnealingFitnessEvaluator<ISequenceElement>
						// linearFitnessEvaluator =
						// (LinearSimulatedAnnealingFitnessEvaluator<ISequenceElement>)
						// fitnessEvaluator;
						//
						// linearFitnessEvaluator.setOptimisationData(context
						// .getOptimisationData());
						// linearFitnessEvaluator.setInitialSequences(context
						// .getInitialSequences());
						//
						// // printSequences(context.getInitialSequences());
						//
						// System.out.println("Initial fitness "
						// + linearFitnessEvaluator.getBestFitness());

						monitor.subTask("Run optimisation");
						optimiser.optimise(context);

						// System.out.println("Final fitness "
						// + linearFitnessEvaluator.getBestFitness());
					} catch (final IncompleteScenarioException ex) {
						fireJobCancelled();
						return Status.CANCEL_STATUS; // die
					}
				} finally {
					monitor.done();
				}

				if (monitor.isCanceled()) {
					state = JobState.ABORTED;
					fireJobCancelled();
				} else {
					state = JobState.COMPLETED;
					fireJobCompleted();
				}
				job = null;

				return Status.OK_STATUS;
			}
		};

		job.setProperty(IProgressConstants2.SHOW_IN_TASKBAR_ICON_PROPERTY, true);

		job.schedule();

		state = JobState.RUNNING;

		fireJobStarted();
	}

	/**
	 * Set pause state
	 */
	@Override
	public void pause() {

		if (state == JobState.RUNNING) {
			synchronized (pauseLock) {
				paused = true;
				state = JobState.PAUSING;
			}
			fireJobPausing();
		}
	}

	@Override
	public void resume() {

		if (state == JobState.PAUSED) {

			synchronized (pauseLock) {
				state = JobState.RESUMING;
				paused = false;
				pauseLock.notify();
			}
			fireJobResuming();
		}
	}

	@Override
	public void stop() {

		if (job == null) {
			// Not yet started
			return;
		}

		if (state == JobState.COMPLETED || state == JobState.ABORTED) {
			// Already stopped
			return;
		}

		if (job.getState() == Job.RUNNING) {
			job.cancel();

			// Resume the job is paused, then resume it before "joining" the
			// thread.
			if (state == JobState.PAUSED || state == JobState.PAUSING) {
				resume();
			}

			// Block return until the job has terminated
			try {
				job.join();
			} catch (final InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			state = JobState.ABORTED;

			fireJobStopped();
		}
	}

	@Override
	public int getProgress() {
		return progress;
	}

	@Override
	public int getTotalProgress() {
		return totalProgress;
	}

	@Override
	public void addManagedJobListener(
			final IManagedJobListener managedJobListener) {
		managedJobListeners.add(managedJobListener);
	}

	@Override
	public void removeManagedJobListener(
			final IManagedJobListener managedJobListener) {
		managedJobListeners.remove(managedJobListener);
	}

	protected void fireJobCompleted() {
		// TODO: This is called from within a job, so needs to return quickly
		for (final IManagedJobListener l : managedJobListeners) {
			l.jobCompleted(this);
		}
	}

	protected void fireProgressUpdate(final int progressDelta) {
		// TODO: This is called from within a job, so needs to return quickly
		for (final IManagedJobListener l : managedJobListeners) {
			l.jobProgressUpdate(this, progressDelta);
		}
	}

	protected void fireJobStarted() {
		for (final IManagedJobListener l : managedJobListeners) {
			l.jobStarted(this);
		}
	}

	protected void fireJobStopped() {
		for (final IManagedJobListener l : managedJobListeners) {
			l.jobStopped(this);
		}
	}

	protected void fireJobPaused() {
		for (final IManagedJobListener l : managedJobListeners) {
			l.jobPaused(this);
		}
	}

	protected void fireJobPausing() {
		for (final IManagedJobListener l : managedJobListeners) {
			l.jobPausing(this);
		}
	}

	protected void fireJobResumed() {
		for (final IManagedJobListener l : managedJobListeners) {
			l.jobResumed(this);
		}
	}

	protected void fireJobResuming() {
		for (final IManagedJobListener l : managedJobListeners) {
			l.jobResuming(this);
		}
	}

	protected void fireJobCancelled() {
		for (final IManagedJobListener l : managedJobListeners) {
			l.jobCancelled(this);
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public JobState getJobState() {
		return state;
	}

	private void setSchedule(
			final IAnnotatedSolution<ISequenceElement> annotatedSequences) {

		this.schedule = annotatedSequences;

	}

	private void updateSchedule(final IAnnotatedSolution<ISequenceElement> solution, int iterations) {
		setSchedule(solution);
		
		// transform and add
		final AnnotatedSolutionExporter exporter = new AnnotatedSolutionExporter();
		final Schedule schedule = exporter.exportAnnotatedSolution(scenario, entities, solution);
		if (iterations < 0) {
			schedule.setName("finished");
		} else if (iterations == 0) {
			schedule.setName("initial");
		} else {
			schedule.setName(iterations + " iterations");
		}
		scenario.getScheduleModel().getSchedules().add(schedule);
	}
}

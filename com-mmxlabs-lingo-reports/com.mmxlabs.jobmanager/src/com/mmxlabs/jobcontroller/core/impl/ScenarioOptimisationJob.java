package com.mmxlabs.jobcontroller.core.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.progress.IProgressConstants2;

import scenario.Scenario;

import com.mmxlabs.common.Pair;
import com.mmxlabs.jobcontroller.core.IManagedJob;
import com.mmxlabs.jobcontroller.core.IManagedJobListener;
import com.mmxlabs.jobcontroller.emf.IncompleteScenarioException;
import com.mmxlabs.jobcontroller.emf.LNGScenarioTransformer;
import com.mmxlabs.jobcontroller.emf.optimisationsettings.OptimisationTransformer;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.dcproviders.IElementDurationProvider;
import com.mmxlabs.optimiser.common.dcproviders.ITimeWindowDataComponentProvider;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.IOptimiser;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerFactory;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.core.impl.AnnotatedSequence;
import com.mmxlabs.optimiser.core.impl.AnnotationSolution;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.lso.IOptimiserProgressMonitor;
import com.mmxlabs.optimiser.lso.impl.LinearSimulatedAnnealingFitnessEvaluator;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.fitness.impl.SimpleSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.LNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlanAnnotator;

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

	public ScenarioOptimisationJob(final String name, final Scenario scenario) {
		this.name = name;

		this.scenario = scenario;
	}

	@Override
	public void init() {
		progress = NO_PROGRESS;
		totalProgress = IProgressMonitor.UNKNOWN;

		totalProgress = 2000000;

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

					int lastReport = 0;

					@Override
					public void report(final IOptimiser<ISequenceElement> arg0,
							final int iteration, final long currentFitness,
							final long bestFitness,
							final ISequences<ISequenceElement> currentState,
							final ISequences<ISequenceElement> bestState) {

						final int work = iteration - lastReport;
						monitor.worked(work);
						lastReport = iteration;
						progress += work;
						fireProgressUpdate(work);

						System.out.println("Iteration: " + iteration
								+ " Best Fitness: " + bestFitness + " Current Fitness: " + currentFitness);

						// TODO: We should verify delta fitness is equal to a
						// whole new fitness
						// As currently the fitness report shows different
						// results!

						// HERE: Process the solution in a runnable and update
						// getSchedule() output
						updateSchedule(bestState);

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
							final long arg1,
							final ISequences<ISequenceElement> arg2) {

						updateSchedule(arg2);

					}

					@Override
					public void begin(final IOptimiser<ISequenceElement> arg0,
							final long arg1,
							final ISequences<ISequenceElement> arg2) {

						updateSchedule(arg2);

					}
				};

				monitor.beginTask("Managed Job", totalProgress);
				try {
					monitor.subTask("Pre-process scenario");

					LNGScenarioTransformer lst = new LNGScenarioTransformer(
							scenario);
					OptimisationTransformer ot = new OptimisationTransformer(
							lst.getOptimisationSettings());
					try {
						IOptimisationData<ISequenceElement> data = lst
								.createOptimisationData();

						Pair<IOptimisationContext<ISequenceElement>, LocalSearchOptimiser<ISequenceElement>> optAndContext = ot
								.createOptimiserAndContext(data);

						monitor.subTask("Prepare optimisation");

						context = optAndContext.getFirst();
						final LocalSearchOptimiser<ISequenceElement> optimiser = optAndContext
								.getSecond();

						optimiser.setProgressMonitor(optMonitor);
						optimiser.init();

						final IFitnessEvaluator<ISequenceElement> fitnessEvaluator = optimiser
								.getFitnessEvaluator();

						//check feasibility of initial sequences
						{
							IConstraintCheckerRegistry registry = context.getConstraintCheckerRegistry();
							Collection<IConstraintCheckerFactory> factories = 
								registry.getConstraintCheckerFactories(context.getConstraintCheckers());
							monitor.subTask("Check initial solution is feasible");
							for (IConstraintCheckerFactory factory : factories) {
								List<String> errors = new ArrayList<String>();
								IConstraintChecker<ISequenceElement> checker = factory.instantiate();
								checker.setOptimisationData(data);
								if (checker.checkConstraints(context.getInitialSequences(), errors) == false) {
									System.err.println("Constraint violations for " + checker + ":");
									System.err.println("\t" + errors);
								}
							}
						}
						
						System.err.println("Initial solution feasibility test completed");
						{
							final ISequences<ISequenceElement> sequences = context.getInitialSequences();
							ITimeWindowDataComponentProvider twp = data.getDataComponentProvider(SchedulerConstants.DCP_timeWindowProvider, 
									ITimeWindowDataComponentProvider.class);
							IPortSlotProvider<ISequenceElement> psp = data.getDataComponentProvider(SchedulerConstants.DCP_portSlotsProvider, IPortSlotProvider.class);
							
							for (int i = 0; i<sequences.size(); i++) {
								ISequence<ISequenceElement> sequence = sequences.getSequence(i);
								System.err.print("Sequence " + i + ": ");
								for (int j = 0; j<sequence.size(); j++) {
									final ITimeWindow tw = twp.getTimeWindows(sequence.get(j)).get(0);
									System.err.print(psp.getPortSlot(sequence.get(j)) + " -> ");
								}
								System.err.println();
							}
							
						}
						
						
						monitor.subTask("Evaluate fitness of initial solution");
						
						final LinearSimulatedAnnealingFitnessEvaluator<ISequenceElement> 
							linearFitnessEvaluator = (LinearSimulatedAnnealingFitnessEvaluator<ISequenceElement>) fitnessEvaluator;

						linearFitnessEvaluator.setOptimisationData(context
								.getOptimisationData());
						linearFitnessEvaluator.setInitialSequences(context
								.getInitialSequences());

//						 printSequences(context.getInitialSequences());

						System.out.println("Initial fitness "
								+ linearFitnessEvaluator.getBestFitness());

						monitor.subTask("Run optimisation");
						optimiser.optimise(context);

//						System.out.println("Final fitness "
//								+ linearFitnessEvaluator.getBestFitness());
					} catch (IncompleteScenarioException ex) {
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

	@SuppressWarnings("unchecked")
	private void updateSchedule(final ISequences<ISequenceElement> bestState) {
		{

			final IOptimisationData<ISequenceElement> data = context
					.getOptimisationData();

			final SimpleSequenceScheduler<ISequenceElement> scheduler = new SimpleSequenceScheduler<ISequenceElement>();
			scheduler.setDistanceProvider(data.getDataComponentProvider(
					SchedulerConstants.DCP_portDistanceProvider,
					IMultiMatrixProvider.class));
			scheduler.setDurationsProvider(data.getDataComponentProvider(
					SchedulerConstants.DCP_elementDurationsProvider,
					IElementDurationProvider.class));
			scheduler.setPortProvider(data.getDataComponentProvider(
					SchedulerConstants.DCP_portProvider, IPortProvider.class));
			scheduler.setTimeWindowProvider(data.getDataComponentProvider(
					SchedulerConstants.DCP_timeWindowProvider,
					ITimeWindowDataComponentProvider.class));
			final IPortSlotProvider<ISequenceElement> portSlotProvider = data
					.getDataComponentProvider(
							SchedulerConstants.DCP_portSlotsProvider,
							IPortSlotProvider.class);
			scheduler.setPortSlotProvider(portSlotProvider);
			scheduler.setPortTypeProvider(data.getDataComponentProvider(
					SchedulerConstants.DCP_portTypeProvider,
					IPortTypeProvider.class));
			final IVesselProvider vesselProvider = data
					.getDataComponentProvider(
							SchedulerConstants.DCP_vesselProvider,
							IVesselProvider.class);
			scheduler.setVesselProvider(vesselProvider);

			final LNGVoyageCalculator<ISequenceElement> voyageCalculator = new LNGVoyageCalculator<ISequenceElement>();
			final VoyagePlanOptimiser<ISequenceElement> voyagePlanOptimiser = new VoyagePlanOptimiser<ISequenceElement>();
			voyagePlanOptimiser.setVoyageCalculator(voyageCalculator);
			
			scheduler.setVoyagePlanOptimiser(voyagePlanOptimiser);

			// This may throw IllegalStateException if not all
			// the elements are set.
			scheduler.init();

			// TODO: Integrate this into the optimiser process rather than here
			final AnnotationSolution<ISequenceElement> solution = new AnnotationSolution<ISequenceElement>();
			solution.setSequences(bestState);
			solution.setContext(context);

			for (final Map.Entry<IResource, ISequence<ISequenceElement>> entry : bestState
					.getSequences().entrySet()) {

				final VoyagePlanAnnotator<ISequenceElement> annotator = new VoyagePlanAnnotator<ISequenceElement>();
				annotator.setPortSlotProvider(portSlotProvider);

				final IResource resource = entry.getKey();
				final ISequence<ISequenceElement> sequence = entry.getValue();

				// Schedule sequence
				final List<VoyagePlan> plans = scheduler
				.schedule(resource, sequence);

				final AnnotatedSequence<ISequenceElement> annotatedSequence = new AnnotatedSequence<ISequenceElement>();

				final ArrayList<ISequenceElement> elements = new ArrayList<ISequenceElement>(
						sequence.size());
				for (final ISequenceElement e : sequence) {
					elements.add(e);
				}

				annotator.annotateFromVoyagePlan(resource, plans,
						annotatedSequence);

				solution.setAnnotatedSequence(resource, annotatedSequence);
			}
			setSchedule(solution);
		}
	}
}

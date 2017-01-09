/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.impl.ga;


/**
 * A sequence scheduler which randomly samples a given number of individuals (currently using the {@link GASequenceScheduler} code), and returns whichever randomly sampled individual was best.
 * 
 * (C) Minimax Labs inc. 2010
 * 
 * @author hinton
 * 
 * @param <T>
 */
public class RandomSequenceScheduler { // extends AbstractSequenceScheduler {
// /**
// * For every byte in the individual, we will sample this many random cases
// */
// private int sampleMultiplier = 10;
// private int seed = 1;
// /**
// * The evaluator used; this probably isn't worth caching, because it's odds off that we'll
// * hit duplicate values for any large sequence.
// */
// private IndividualEvaluator<T> individualEvaluator;
// static int TAG = 0;
//
// private static synchronized int getTag() {
// return TAG++;
// }
//
// BufferedWriter output;
//
// public RandomSequenceScheduler() {
// // try {
// // final File file = new File("/Users/hinton/Desktop/data/random_scheduler_log" + getTag() + ".py");
// // System.err.println("Log to " + file.getAbsolutePath());
// // output = new BufferedWriter(new FileWriter(file));
// // } catch (IOException e) {
// // e.printStackTrace();
// // output = null;
// // }
// }
//
// @Override
// public Pair<Integer, List<VoyagePlan>> schedule(IResource resource,
// ISequence<T> sequence) {
// final int bytes = individualEvaluator.setup(resource, sequence);
// final Random random = new Random(seed);
//
// int samples = sampleMultiplier * bytes;
// final byte[] currentBytes = new byte[bytes];
// final byte[] bestBytes = new byte[bytes];
// long bestFitness = Long.MAX_VALUE;
//
// final Individual current = new Individual(currentBytes);
// final Individual best = new Individual(bestBytes);
//
// final long [] all_samples = new long[samples];
// int k = 0;
// while (samples > 0) {
// random.nextBytes(currentBytes);
// final long currentFitness = individualEvaluator.evaluate(current);
//
// all_samples[k++] = currentFitness;
// if (currentFitness < bestFitness) {
// System.arraycopy(currentBytes, 0, bestBytes, 0, bytes);
// bestFitness = currentFitness;
// }
//
// samples--;
// }
//
// final int[] arrivalTimes = new int[sequence.size()];
// individualEvaluator.decode(best, arrivalTimes);
//
// // if (output != null) {
// // try {
// // output.write("Schedule(" + bytes + ", " + Arrays.toString(all_samples) +")\n");
// // output.flush();
// // } catch (IOException e) {
// // e.printStackTrace();
// // }
// // }
//
// return super.schedule(resource, sequence, arrivalTimes);
// }
//
// public int getSampleMultiplier() {
// return sampleMultiplier;
// }
//
// public void setSampleMultiplier(int sampleMultiplier) {
// this.sampleMultiplier = sampleMultiplier;
// }
//
// public IndividualEvaluator<T> getIndividualEvaluator() {
// return individualEvaluator;
// }
//
// public void setIndividualEvaluator(IndividualEvaluator<T> individualEvaluator) {
// this.individualEvaluator = individualEvaluator;
// }
//
// public int getSeed() {
// return seed;
// }
//
// public void setSeed(int seed) {
// this.seed = seed;
// }
}

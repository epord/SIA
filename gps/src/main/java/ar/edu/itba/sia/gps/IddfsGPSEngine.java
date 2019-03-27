package ar.edu.itba.sia.gps;

import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.Problem;
import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;

import java.util.*;

public class IddfsGPSEngine extends GPSEngine {

	private int iddfsDepth, depthReachedCurrentRun, depthReachedPreviousRun;

	// Use this variable in open set order.
	protected SearchStrategy strategy;

	public IddfsGPSEngine(Problem problem, Heuristic heuristic) {
		super(problem, SearchStrategy.IDDFS, heuristic);
		iddfsDepth = 0;
		depthReachedCurrentRun = 0;
		depthReachedPreviousRun = -1;
	}

	public void findSolution() {
		GPSNode rootNode = new GPSNode(problem.getInitState(), 0, null);
		open.add(rootNode);
		boolean done = false;
		do {
			GPSNode currentNode = open.remove();
			if (problem.isGoal(currentNode.getState())) {
				setSolution(currentNode);
				printSolution();
				return;
			} else {
				explode(currentNode);
			}
			if (open.isEmpty()) {
				if (depthReachedPreviousRun != -1 && depthReachedCurrentRun == depthReachedPreviousRun) {
					done = true;
				} else {
					// Reset and try again with increased depth
					iddfsDepth++;
					bestCosts.clear();
					explosionCounter = 0;
					depthReachedPreviousRun = depthReachedCurrentRun;
					depthReachedCurrentRun = 0;
					open.add(rootNode);
					done = false;
				}
//				System.out.format("Increased IDDFS depth to %d\n", iddfsDepth);
			}
		} while (!done);
		failed = true;
		finished = true;
	}

	private void explode(GPSNode node) {
		Collection<GPSNode> newCandidates;
		if (bestCosts.containsKey(node.getState())) {//TODO: add comparison of costs.
			return;
		}
		if (node.getDepth() >= iddfsDepth) {
			return;
		}
		newCandidates = new ArrayList<>();
		addCandidates(node, newCandidates);
		depthReachedCurrentRun = Math.max(
				depthReachedCurrentRun,
				newCandidates.stream().map(GPSNode::getDepth).max(Comparator.naturalOrder()).orElse(depthReachedCurrentRun)
		);
		newCandidates.forEach(((Deque<GPSNode>) open)::addFirst);
	}

}

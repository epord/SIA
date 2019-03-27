package ar.edu.itba.sia.gps;

import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.Problem;
import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;

import java.util.*;

public class GPSEngine {

	Queue<GPSNode> open;
	Map<State, Integer> bestCosts;
	Map<State, Integer> generatedStates;
	Problem problem;
	long explosionCounter;
	boolean finished;
	boolean failed;
	GPSNode solutionNode;
	Optional<Heuristic> heuristic;

	// Use this variable in open set order.
	protected SearchStrategy strategy;

	public GPSEngine(Problem problem, SearchStrategy strategy, Heuristic heuristic) {
		// TODO: open = *Su queue favorito, TENIENDO EN CUENTA EL ORDEN DE LOS NODOS*
		if(strategy.equals(SearchStrategy.BFS) || strategy.equals(SearchStrategy.DFS) ||
				strategy.equals(SearchStrategy.IDDFS)) {
			open = new ArrayDeque<>();
		}
		else {
			Objects.requireNonNull(heuristic);
			if(strategy.equals(SearchStrategy.ASTAR)) {
				open = new PriorityQueue<>(10, Comparator.comparingInt(
						node -> heuristic.getValue(node.getState()) + node.getCost())
				);
			}
			else {
				open = new PriorityQueue<>(10, Comparator.comparingInt(
						node -> heuristic.getValue(node.getState()))
				);
			}
		}
        bestCosts = new HashMap<>();
        generatedStates = new HashMap<>();
		this.problem = problem;
		this.strategy = strategy;
		this.heuristic = Optional.ofNullable(heuristic);
		explosionCounter = 0;
		finished = false;
		failed = false;
	}

	@SuppressWarnings("Duplicates")
    public void findSolution() {
		GPSNode rootNode = new GPSNode(problem.getInitState(), 0, null);
		open.add(rootNode);
		generatedStates.put(problem.getInitState(), 0);
        while (!open.isEmpty()) {
			GPSNode currentNode = open.remove();
            if (problem.isGoal(currentNode.getState())) {
			    setSolution(currentNode);
				return;
			}  else {
                explode(currentNode);
            }
		}
        failed = true;
        finished = true;
	}

    private void explode(GPSNode node) {
		Collection<GPSNode> newCandidates;
		switch (strategy) {
		case BFS:
			if (bestCosts.containsKey(node.getState())) {//TODO: add comparison of costs.
				return;
			}
			newCandidates = new ArrayList<>();
			addCandidates(node, newCandidates);
			newCandidates.forEach(gpsNode -> open.offer(gpsNode));
			break;

		case DFS:
			if (bestCosts.containsKey(node.getState())) {//TODO: add comparison of costs.
				return;
			}
			newCandidates = new ArrayList<>();
			addCandidates(node, newCandidates);
			newCandidates.forEach(((Deque<GPSNode>) open)::addFirst);
			break;

		case GREEDY:
			if (!isBest(node.getState(), node.getCost())) {
				return;
			}
			newCandidates = new ArrayList<>();
			addCandidates(node, newCandidates);
			open.addAll(newCandidates);
			break;

		case ASTAR:
			if (!isBest(node.getState(), node.getCost())) {
				return;
			}
			newCandidates = new ArrayList<>();
			addCandidates(node, newCandidates);
			open.addAll(newCandidates);
			break;
		}
	}

	protected void addCandidates(GPSNode node, Collection<GPSNode> candidates) {
		explosionCounter++;
		updateBest(node);
		generatedStates.remove(node.getState());
		for (Rule rule : problem.getRules()) {
			Optional<State> newState = rule.apply(node.getState());
			if (newState.isPresent()) {
				int newCost = node.getCost() + rule.getCost();
				State state = newState.get();
				if ((!bestCosts.containsKey(state) && !generatedStates.containsKey(state))
                        || (bestCosts.containsKey(state) && newCost < bestCosts.get(state))
                        || (generatedStates.containsKey(state) && newCost < generatedStates.get(state))) {
					GPSNode newNode = new GPSNode(state, newCost, rule);
					generatedStates.put(newState.get(), newCost);
					newNode.setParent(node);
					candidates.add(newNode);
				}
			}
		}
	}

	private boolean isBest(State state, Integer cost) {
		return !bestCosts.containsKey(state) || cost < bestCosts.get(state);
	}

	private void updateBest(GPSNode node) {
		bestCosts.put(node.getState(), node.getCost());
	}

    /**
     * Sets {@link #solutionNode} to the specified node and sets {@link #finished} to true.
     *
     * @param solutionNode Solution node
     */
	protected void setSolution(GPSNode solutionNode) {
	    this.solutionNode = solutionNode;
	    this.finished = true;
    }

    public void printSolution() {
        System.out.println(solutionNode.getSolution());
    }

	// GETTERS FOR THE PEOPLE!

	public Queue<GPSNode> getOpen() {
		return open;
	}

	public Map<State, Integer> getBestCosts() {
		return bestCosts;
	}

	public Problem getProblem() {
		return problem;
	}

	public long getExplosionCounter() {
		return explosionCounter;
	}

	public boolean isFinished() {
		return finished;
	}

	public boolean isFailed() {
		return failed;
	}

	public GPSNode getSolutionNode() {
		return solutionNode;
	}

	public SearchStrategy getStrategy() {
		return strategy;
	}

}

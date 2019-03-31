package ar.edu.itba.sia.gps;

import ar.edu.itba.sia.gps.api.Heuristic;
import ar.edu.itba.sia.gps.api.Problem;
import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;

import java.util.*;

public class GPSEngine {

	Queue<GPSNode> open;
	List<GPSNode> tiedNodes;
	Map<State, Integer> bestCosts;
	Map<State, Integer> generatedStates;
	Problem problem;
	long explosionCounter;
	long borderNodes;
	long analizedStates;
	boolean finished;
	boolean failed;
	GPSNode solutionNode;
	Optional<Heuristic> heuristic;
	// IDDFS-specific fields
	private int iddfsDepth, depthReachedCurrentRun, depthReachedPreviousRun, iddfsDeltaDepth, iddfsDepthFloor;

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
						node -> node.getHeuristicValue() + node.getCost())
				);
			}
			else {
				open = new PriorityQueue<>(10, Comparator.comparingInt(
						node -> node.getHeuristicValue())
				);
			}
		}

		borderNodes = 0;
		analizedStates = 0;
        bestCosts = new HashMap<>();
		generatedStates = new HashMap<>();
		tiedNodes = new ArrayList<>();
		this.problem = problem;
		this.strategy = strategy;
		this.heuristic = Optional.ofNullable(heuristic);
		explosionCounter = 0;
		iddfsDepth = 0;
		depthReachedCurrentRun = 0;
		depthReachedPreviousRun = -1;
		iddfsDeltaDepth = 15;
		finished = false;
		failed = false;
	}

    public void findSolution() {
		GPSNode rootNode = new GPSNode(problem.getInitState(), 0, null);
		open.add(rootNode);
		generatedStates.put(problem.getInitState(), 0);
		boolean done = false;
		do {
			GPSNode currentNode = open.remove();
			if (problem.isGoal(currentNode.getState())) {
                setSolution(currentNode);
                borderNodes = open.size();
				if (strategy == SearchStrategy.IDDFS) {
				    if (iddfsDeltaDepth > 1) { // See if there's a better solution between the previous depth and current depth
				        /*
                            Discard unvisited nodes for this depth, all other solutions will be at greater or equal depth.
                            Do this to avoid the IllegalArgumentException thrown by iddfsReset() which will be called during
                            the binary search.
                         */
                        open.clear();
                        Optional<GPSNode> binarySearchSolution = iddfsBinarySearch(rootNode, iddfsDepthFloor+1, currentNode.getDepth()-1);
                        if (binarySearchSolution.isPresent()) {
                            GPSNode newSolution = binarySearchSolution.get();
                            if (newSolution.getDepth() >= currentNode.getDepth()) {
                                throw new IllegalStateException(String.format(
                                        "Solution found in IDDFS binary search doesn't have less depth than original solution (%d < %d)",
                                        newSolution.getDepth(),
                                        currentNode.getDepth())
                                );
                            }
                            setSolution(binarySearchSolution.get());
                        }
                    }
				}
				return;
			} else {
				explode(currentNode);
			}
			if (open.isEmpty()) {
				if (strategy == SearchStrategy.IDDFS) {
					if (depthReachedPreviousRun != -1 && depthReachedCurrentRun == depthReachedPreviousRun) {
						done = true;
					} else {
						// Reset and try again with increased depth
						iddfsReset(rootNode);
						iddfsDepth += iddfsDeltaDepth;
						depthReachedPreviousRun = depthReachedCurrentRun;
						iddfsDepthFloor = depthReachedPreviousRun;
						depthReachedCurrentRun = 0;
						done = false;
					}
				} else {
					done = true;
				}
			}
		} while (!done);
        failed = true;
        finished = true;
	}

	/**
	 * Perform different DFS runs within the given depth floor and ceiling, using binary search. The objective of this
	 * solution is to find a solution more optimal than the original one found by IDDFS when {@link #iddfsDeltaDepth} > 1.
	 * This should be called with {@code ceiling = solutionDepth-1; floor = solutionDepth-iddfsDeltaDepth+1 }.
	 *
	 *  @param rootNode       Root node to start DFS runs with.
	 * @param iddfsDepthFloor Lower bound for the depth.
	 * @param iddfsDepthCeil  Upper bound for the depth.
	 * @return Optional with found solution, if any.
	 */
	private Optional<GPSNode> iddfsBinarySearch(GPSNode rootNode, int iddfsDepthFloor, int iddfsDepthCeil) {
		if (iddfsDepthFloor > iddfsDepthCeil) {
			throw new IllegalArgumentException(String.format("Floor (%d) > ceil (%d) in IDDFS binary search", iddfsDepthFloor, iddfsDepthCeil));
		}

		List<Integer> depths = new ArrayList<>(iddfsDepthCeil - iddfsDepthFloor + 1),
					excludedDepths = new ArrayList<>();
		binarySearchBounds(iddfsDepthFloor, iddfsDepthCeil, depths);
		Optional<GPSNode> result = Optional.empty();

		ListIterator<Integer> depthsIterator = depths.listIterator();
		while (depthsIterator.hasNext()) {
			int depth = depthsIterator.next();
			if (!excludedDepths.contains(depth)) {
				// Stop iteration if all remaining depths are greater than the depth of the result
				if (result.isPresent()) {
					final int resultDepth = result.get().getDepth();
					if (depths.stream().noneMatch(d -> d < resultDepth)) {
						return result;
					}
				}
				// Reset, update depth, and perform search
				iddfsReset(rootNode);
				iddfsDepth = depth;
				while (!open.isEmpty()) {
					GPSNode currentNode = open.remove();
					if (problem.isGoal(currentNode.getState())) {
						result = Optional.of(currentNode);
						if (depth == iddfsDepthFloor) {
							return result; // There's not a better solution, stop here
						} else {
							// Don't visit this depth in the future if we haven't seen it yet
							excludedDepths.add(depth);
							// Don't keep exploring further down this tree
							break;
						}
					} else {
						explode(currentNode);
					}
				}
			}
			depthsIterator.remove();
		}
		return result;
	}

	private void binarySearchBounds(int left, int right, List<Integer> result) {
		binarySearchBoundsRec(left, right, left, right, result);
	}

	/**
	 * Recursively generate depths in binary search order (assuming the solution is never found, so all depths are
	 * attempted) between the given floor and ceiling.
	 *
	 * @param left   Current lower bound
	 * @param right  Current upper bound
	 * @param floor  Global lower bound
	 * @param ceil   Global upper bound
	 * @param result Result where to add depths.
	 */
	private void binarySearchBoundsRec(int left, int right, int floor, int ceil, List<Integer> result) {
		if (left < floor || right > ceil || right < left) {
			return;
		}
		int middle = (left+right)/2;
		if (!result.contains(middle)) {
			result.add(middle);
		}
		binarySearchBoundsRec(left, middle-1, floor, ceil, result);
		binarySearchBoundsRec(middle+1, right, floor, ceil, result);
	}

    private void explode(GPSNode node) {
		Collection<GPSNode> newCandidates;
		double number;

		switch (strategy) {
		case BFS:
			if (bestCosts.containsKey(node.getState())) {//TODO: add comparison of costs.
				return;
			}
			analizedStates++;
			newCandidates = new ArrayList<>();
			addCandidates(node, newCandidates);
			newCandidates.forEach(gpsNode -> open.offer(gpsNode));
			break;

		case DFS:
			if (bestCosts.containsKey(node.getState())) {//TODO: add comparison of costs.
				return;
			}
			analizedStates++;
			newCandidates = new ArrayList<>();
			addCandidates(node, newCandidates);
			newCandidates.forEach(((Deque<GPSNode>) open)::addFirst);
			break;

		case IDDFS:
			if (bestCosts.containsKey(node.getState())) {//TODO: add comparison of costs.
				return;
			}
			analizedStates++;
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
			break;

		case GREEDY:
			number = Math.random();
			if(number >= 0.5) {
				if (open.size() > 0) {
					tiedNodes.add(node);
					GPSNode aux = open.remove();
					if (aux.getHeuristicValue() == node.getHeuristicValue()) {
						while (!open.isEmpty() && aux.getHeuristicValue() == node.getHeuristicValue()) {
							tiedNodes.add(aux);
							aux = open.remove();
							if (aux.getHeuristicValue() != node.getHeuristicValue()) {
								open.add(aux);
							}
						}
						Collections.shuffle(tiedNodes);
						node = tiedNodes.remove(0);
						open.addAll(tiedNodes);
						tiedNodes.clear();
					} else {
						open.add(aux);
					}
				}
			}

			if (!isBest(node.getState(), node.getCost())) {
				return;
			}

			newCandidates = new ArrayList<>();
			addCandidates(node, newCandidates);
			open.addAll(newCandidates);
			break;

		case ASTAR:
			number = Math.random();
			if(number >= 0.5) {
				if (open.size() > 0) {
					tiedNodes.add(node);
					GPSNode aux = open.remove();
					if (aux.getHeuristicValue() + aux.getCost() == node.getHeuristicValue() + node.getCost()) {
						while (!open.isEmpty() && aux.getHeuristicValue() + aux.getCost() == node.getHeuristicValue() + node.getCost()) {
							tiedNodes.add(aux);
							aux = open.remove();
							if (aux.getHeuristicValue() + aux.getCost() != node.getHeuristicValue() + node.getCost()) {
								open.add(aux);
							}
						}
						Collections.shuffle(tiedNodes);
						node = tiedNodes.remove(0);
						open.addAll(tiedNodes);
						tiedNodes.clear();
					} else {
						open.add(aux);
					}
				}
			}

			if (!isBest(node.getState(), node.getCost())) {
				return;
			}

			newCandidates = new ArrayList<>();
			addCandidates(node, newCandidates);
			open.addAll(newCandidates);
			break;
		}
	}

	/**
	 * Resets exploration variables. Called when using {@link SearchStrategy#IDDFS} before restarting search with a new
	 * depth.
     *
	 * @param rootNode Root node to be added to {@link #open}.
	 * @throws IllegalStateException If called when {@link #open} is not empty.
	 */
	private void iddfsReset(GPSNode rootNode) throws IllegalStateException {
		if (!open.isEmpty()) {
			throw new IllegalStateException("Attempted IDDFS reset when open was not empty");
		}
		bestCosts.clear();
		generatedStates.clear();
		depthReachedCurrentRun = 0;
		open.add(rootNode);
		generatedStates.put(rootNode.getState(), 0);
	}

	protected void addCandidates(GPSNode node, Collection<GPSNode> candidates) {
		int heursiticValue;
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

					GPSNode newNode;
					if (strategy == SearchStrategy.GREEDY || strategy == SearchStrategy.ASTAR) {
						heursiticValue = heuristic.get().getValue(state);
						newNode = new GPSNode(state, newCost, rule, heursiticValue);
					} else {
						newNode = new GPSNode(state, newCost, rule);
					}
					generatedStates.put(newState.get(), newCost);
					newNode.setParent(node);
					candidates.add(newNode);
				}
			}
		}
	}

	private boolean isBest(State state, Integer cost) {
		if(!bestCosts.containsKey(state)) {
			analizedStates++;
			return true;
		}
		return cost < bestCosts.get(state);
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

	public long getAnalizedStates() {
		return analizedStates;
	}

	public long getBorderNodes() {
		return borderNodes;
	}
}

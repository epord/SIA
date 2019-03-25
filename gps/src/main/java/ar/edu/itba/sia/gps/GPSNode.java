package ar.edu.itba.sia.gps;


import ar.edu.itba.sia.gps.api.Rule;
import ar.edu.itba.sia.gps.api.State;

import java.util.Optional;

public class GPSNode {

	private State state;

	private GPSNode parent;

	private Integer cost;

	private Rule generationRule;

	private int depth = 0;

	public GPSNode(State state, Integer cost, Rule generationRule) {
		this.state = state;
		this.cost = cost;
		this.generationRule = generationRule;
	}

	public GPSNode getParent() {
		return parent;
	}

	public void setParent(GPSNode parent) {
		this.parent = parent;
		this.depth = Optional.ofNullable(parent).map(p -> p.depth + 1).orElse(0);
	}

	public State getState() {
		return state;
	}

	public Integer getCost() {
		return cost;
	}

	@Override
	public String toString() {
		return state.toString();
	}

	public String getSolution() {
		if (this.parent == null) {
			return this.state.toString();
		}
		// Run iteratively instead of recursively to prevent stack overflow errors as well as instantiation of many Strings
		GPSNode currentNode = this;
		StringBuilder result = new StringBuilder();
		do {
			result
                    .insert(0, "\n\n")
                    .insert(0, currentNode.state.toString());
			currentNode = currentNode.parent;
		} while (currentNode != null);
        return result
                .toString()
                .trim(); // Remove trailing \n\n
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GPSNode other = (GPSNode) obj;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}

	public Rule getGenerationRule() {
		return generationRule;
	}

	public void setGenerationRule(Rule generationRule) {
		this.generationRule = generationRule;
	}

	public int getDepth() {
		return depth;
	}
}

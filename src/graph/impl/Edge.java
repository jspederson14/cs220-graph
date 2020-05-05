package graph.impl;

import graph.INode;

public class Edge implements Comparable<Edge>{
	INode node;
	INode connect;
	int cost;
	public Edge(INode node, INode connect, int cost) {
		node.addDirectedEdgeToNode(connect, cost);
		this.node = node;
		this.connect = connect;
		this.cost = cost;
	}
	public String edgeName() {
		return node.getName();
	}

public int compareTo(Edge other){
    return this.cost - other.cost;
  }

  public String toString(){
    return this.node.getName()+" to "+this.connect.getName() + " with cost "+this.cost;
  }
}

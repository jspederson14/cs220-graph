package graph.impl;

import graph.INode;

public class Path implements Comparable<Path>
{
  String dst;
  int cost;
  INode node;

  public Path(String dst, int cost){
    this.dst = dst;
    this.cost = cost;
  }
  public Path(INode node, int cost){
	  this.dst = node.getName();
	  this.cost = cost;
	  this.node = node;
	  }

  public int compareTo(Path other){
    return this.cost - other.cost;
  }

  public String toString(){
    return this.dst + " with cost "+this.cost;
  }
}

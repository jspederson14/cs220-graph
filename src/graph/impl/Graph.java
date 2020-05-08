package graph.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import graph.IGraph;
import graph.INode;
import graph.NodeVisitor;

/**
 * A basic representation of a graph that can perform BFS, DFS, Dijkstra,
 * and Prim-Jarnik's algorithm for a minimum spanning tree.
 * 
 * @author jspacco
 *
 */
public class Graph implements IGraph
{
    public HashMap<String,INode> contains = new HashMap<String,INode>();
    /**
     * Return the {@link Node} with the given name.
     * 
     * If no {@link Node} with the given name exists, create
     * a new node with the given name and return it. Subsequent
     * calls to this method with the same name should
     * then return the node just created.
     * 
     * @param name
     * @return
     */
    public INode getOrCreateNode(String name) {
        if(containsNode(name))
        	return contains.get(name);
        INode n = new Node(name);
        contains.put(name, n);
        return n;
    }

    /**
     * Return true if the graph contains a node with the given name,
     * and false otherwise.
     * 
     * @param name
     * @return
     */
    public boolean containsNode(String name) {
        return contains.containsKey(name);
    }

    /**
     * Return a collection of all of the nodes in the graph.
     * 
     * @return
     */
    public Collection<INode> getAllNodes() {
        return contains.values();
    }
    
    /**
     * Perform a breadth-first search on the graph, starting at the node
     * with the given name. The visit method of the {@link NodeVisitor} should
     * be called on each node the first time we visit the node.
     * 
     * 
     * @param startNodeName
     * @param v
     */
    public void breadthFirstSearch(String startNodeName, NodeVisitor v)
    {
        HashSet<INode> visited = new HashSet<INode>();
        Queue <INode> toVisit = new LinkedList<INode>();
        
        toVisit.add(getOrCreateNode(startNodeName));
        
        while(!toVisit.isEmpty()) {
        	INode x = toVisit.remove();
        	if (visited.contains(x))
        		continue;
        	v.visit(x);
        	visited.add(x);
        	
        	Iterator<INode> iter = x.getNeighbors().iterator();
        	
        	while(iter.hasNext()) {
        		INode n = iter.next();
        		if(containsNode(n.getName()))
        			toVisit.add(n);
        	}
        		
        }
        	
    }

    /**
     * Perform a depth-first search on the graph, starting at the node
     * with the given name. The visit method of the {@link NodeVisitor} should
     * be called on each node the first time we visit the node.
     * 
     * 
     * @param startNodeName
     * @param v
     */
    public void depthFirstSearch(String startNodeName, NodeVisitor v)
    {
    	HashSet<INode> visited = new HashSet<INode>();
        Stack<INode> toVisit = new Stack<INode>();
        
        toVisit.push(getOrCreateNode(startNodeName));
        
        while(!toVisit.isEmpty()) {
        	INode x = toVisit.pop();
        	if (visited.contains(x))
        		continue;
        	v.visit(x);
        	visited.add(x);
        	
        	Iterator<INode> iter = x.getNeighbors().iterator();
        	
        	while(iter.hasNext()) {
        		INode n = iter.next();
        		if(containsNode(n.getName()))
        			toVisit.push(n);
        	}
        		
        }
    }

    /**
     * Perform Dijkstra's algorithm for computing the cost of the shortest path
     * to every node in the graph starting at the node with the given name.
     * Return a mapping from every node in the graph to the total minimum cost of reaching
     * that node from the given start node.
     * 
     * <b>Hint:</b> Creating a helper class called Path, which stores a destination
     * (String) and a cost (Integer), and making it implement Comparable, can be
     * helpful. Well, either than or repeated linear scans.
     * 
     * @param startName
     * @return
     */
    public Map<INode,Integer> dijkstra(String startName) {
    	Map<INode,Integer> result = new HashMap<>();
    	PriorityQueue<Path> todo = new PriorityQueue<>();
    	todo.add(new Path(startName, 0));
    	
    	while(result.size()<getAllNodes().size()) {
    		Path nextPath = todo.poll();
    		INode node = getOrCreateNode(nextPath.dst);
    		
    		if(result.containsKey(node))
    			continue;
    		
    		int cost = nextPath.cost;
    		result.put(node, cost);
    		
    		for(INode n:node.getNeighbors())
    			todo.add(new Path(n,cost+node.getWeight(n)));
    	}
    	return result;
    }
    
    /**
     * Perform Prim-Jarnik's algorithm to compute a Minimum Spanning Tree (MST).
     * 
     * The MST is itself a graph containing the same nodes and a subset of the edges 
     * from the original graph.
     * 
     * @return
     */
    public IGraph primJarnik() {
       IGraph mst = new Graph();
       PriorityQueue<Edge> todo = new PriorityQueue<>();
       INode start = getAllNodes().iterator().next();
       Collection<INode> neighbors = start.getNeighbors();
       for(INode n: neighbors) {
    	   todo.add(new Edge(start,n,n.getWeight(start)));
	   }
       mst.getOrCreateNode(start.getName());
       while(mst.getAllNodes().size()<this.getAllNodes().size()) {
    	   Edge nextEdge = todo.poll();
    	   mst.getOrCreateNode(nextEdge.connect.getName());
    	   mst.getOrCreateNode(nextEdge.node.getName()).addDirectedEdgeToNode(nextEdge.connect, nextEdge.cost);
    	   neighbors = nextEdge.connect.getNeighbors();
    	   for(INode n: neighbors) {
    		   if(!mst.containsNode(n.getName())) {
    			   todo.add(new Edge(nextEdge.connect,n,n.getWeight(nextEdge.connect)));
    		   }
    	   } 
       }
       for(INode n : mst.getAllNodes()) {
    	   System.out.println(n.getName());
    	   for(INode m : n.getNeighbors())
    		   System.out.println(" "+n.getWeight(m)+" "+m.getName());
       }
       return mst;
    }
}
package vankrimpen.dustin;

import java.util.LinkedList;

/*
 * Vertex for a graph of Strings
 * Contains list of edges 
 * 
 * Used in WordLadder and GraduationPlan 
 */
public class Node {
	public String label;
	public LinkedList<Node> links; // the edges of the node 
	int credits; // credit hours for CourseGraph 
	public boolean visited; 
	public Node parent; // parent is used to return track of the path in wordLadder 

	
	public Node(String word) {
		this.label = word;
		this.links = new LinkedList<Node>();
		this.visited = false;
		this.parent = null;
	}
	
	public Node(String word, int code) {
		this.label = word;
		this.links = new LinkedList<Node>();
		this.visited = false;
		this.parent = null;
		this.credits = code;
	}
	
	public Node(String word, LinkedList<Node> links) {
		this.label = word;
		this.links = links;
		this.visited = false;
		this.parent = null;
	}
	
	public Node(String word, int code, LinkedList<Node> links) {
		this.label = word;
		this.links = links;
		this.visited = false;
		this.parent = null;
		this.credits = code;
	}
	
	public void addLink(Node child) {
		links.add(child);
	}
	
	public int linkSize() {
		return links.size();
	}
	
	public String toString() {
		return label + "\n";
	}

}

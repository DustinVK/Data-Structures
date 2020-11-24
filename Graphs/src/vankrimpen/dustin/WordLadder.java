package vankrimpen.dustin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

// For task 1: Implement the word ladder problem solver using both DFS and BFS algorithms.


public class WordLadder {
	// nodes is an adjacency list graph implementation . 
	// Each Node in nodes contains a list of connections which represent the edges of the graph. 
	static Node[] nodes;
	
	static Queue<Node> wordQueue; // for BFS 
	static Stack<Node> wordStack; // For DFS 
	
	public static void main(String[] args) {
	
		makeGraph("wordlist.txt", 4);
		printGraph(); // view the graph
		System.out.printf("Graph is finished! Searching...\n");
		System.out.println("----------------------------------------------------------\n");
		
		makeLadder("LAVA", "PULP", false); // make and print ladder using depth first search
		makeLadder("LAVA", "PULP", true); // make and print ladder using breadth first search
		
		//printGraph(); // view the graph 
		//writeGraph("wordgraph.txt"); // write graph to a txt file 
		

}
	
	// Creates a word-ladder.
	// If passed true for bfs then breadth first search is used.
	// Otherwise depth first search is used.
	public static void makeLadder(String start, String end, boolean bfs) {
		wordQueue = new LinkedList<Node>(); // for BFS 
		wordStack = new Stack<Node>(); // For DFS 
		LinkedList<Node> path = new LinkedList<Node>(); // list to store the path from start to end of worladder  

		long time; // used to track and display how much time the search took.
		
		// for start and end word validation 
		boolean validStart = false;
		boolean validEnd = false;
		
		Node tempN = null; // temp node for output at the end
		
		// reset the visited and parent parameters of each node 
		for(int i = 0; i<nodes.length;i++) {
			nodes[i].visited = false;
			nodes[i].parent = null;
		}
		
		// check for valid start and end words 
		for (int i = 0; i< nodes.length; i++) {
			if (nodes[i].label.equals(start)) {
				// push starting node to queue and stack 
				wordQueue.add(nodes[i]);
				wordStack.push(nodes[i]);
				validStart = true;
			}
			if (nodes[i].label.equals(end)) {
				tempN = nodes[i];
				validEnd = true;
			}
			
		}
		// return without searching if either start or end are invalid 
		if (!validStart || !validEnd) {
			if(!validStart) {
				System.out.println("String \"" + start + "\" could not be found.");
			}
			if(!validEnd) {
				System.out.println("String \"" + end + "\" could not be found.");
			}
			return;
		}
		else {
			int result = 0; // result is the output of bfs and dfs - 1 for success, 0 for no path between start and end found
			// breadth first or depth first search options
			if(bfs) {
				// BFS performed and time taken measured
				time = System.currentTimeMillis();
				result = breadthFS(wordQueue, end);
				time = System.currentTimeMillis() - time;
				System.out.printf("Breadth first search finished in %d milliseconds. Result:\n", time);
			}
			else {
				// DFS performed and time taken measured 
				time = System.currentTimeMillis();
				result = depthFS(wordStack, end);
				time = System.currentTimeMillis() - time;
				System.out.printf("Depth first search finished in %d milliseconds. Result: \n", time);
			}
			
			if(result == 0) {
				System.out.println("Unable to connect \"" + start +"\" to \"" + end +"\".\n");
				return;
			}
			
			// Starts from the end and travels up through parents, adding each to path list 
			while (tempN != null) {;
				path.add(0,tempN);
				tempN = tempN.parent;
			}
			
			for(Node node : path) {
				System.out.println(node.label);
			}
			System.out.println("----------------------------------------------------------\n");
		}
	}
	
	/*
	 * recursive breadth first search 
	 * initialized with the starting Node and the string to search for (end) 
	 * 
	 * returns 1 if a path from start to end is found
	 * returns 0 if no path from start to end is found 
	 */
	public static int breadthFS(Queue<Node> q, String end) {
		// return 0 if all nodes in queue have been searched and no match has been found 
		if(q.isEmpty()) {
				return 0;
		}
		Node n = q.poll();
		n.visited = true;
		// return if end is found 
		if(n.label.contentEquals(end)) {
			return 1;
		}
		// add all of n's edges to the queue, and set n as their parent node 
		for(int i = 0;i<n.linkSize();i++) {
			if(!n.links.get(i).visited) {
				n.links.get(i).visited = true;
				if(n.links.get(i).parent == null) {
					// set parent to n 
					n.links.get(i).parent = n;
				}
				// add edges to queue 
				q.add(n.links.get(i));
			}
		}
		return breadthFS(q,end);
	}
	
	// Depth first search. The same code as BFS but with a stack instead of a queue 
	public static int depthFS(Stack<Node> stack, String end) {
		if(stack.isEmpty()) {
			return 0;
		}
		
		Node n = stack.pop();
		n.visited = true;
		
		if(n.label.contentEquals(end)) {
			return 1;
		}
		
		for(int i = 0;i<n.linkSize();i++) {
			if(!n.links.get(i).visited) {
				n.links.get(i).visited = true;
				if(n.links.get(i).parent == null) {
					n.links.get(i).parent = n;
				}
				
				stack.push(n.links.get(i));
			}
		}
		return depthFS(stack,end);
	}
	
	// Creates a list of Nodes from a txt file list of words 
	// Only words of the given wordLength are added to list
	// list is passed to addNodes() which creates a Node for each word and adds to nodes[]
	// addChildren() finishes the graph, searching for edges to add to each Node
	public static void makeGraph(String path, int wordLength) {
		ArrayList<String> wordList = new ArrayList<String>();
			File file = new File(path); 
			Scanner sc;
			String line;
			try {
				sc = new Scanner(file);
				while (sc.hasNextLine()) { 
					line = sc.nextLine();
					if(line.length() == wordLength) {
						wordList.add(line);
					}
					
				}
				sc.close();
				System.out.printf("Word list successfully read. Building graph...\n");
			} catch (FileNotFoundException e) {
				System.out.println("Unable to read from " + path);
				e.printStackTrace();
				return;
			}
			
			addNodes(wordList);
			addChildren();
	}
	
	// add a list of nodes to nodes[]
	public static void addNodes(ArrayList<String> wordList) {
		nodes = new Node[wordList.size()];
		for(int i =0; i< nodes.length; i++) {
			nodes[i] = new Node(wordList.get(i));
		}
	}
	public static void addNodes(ArrayList<String> wordList, ArrayList<Integer> children) {
		nodes = new Node[wordList.size()];
		for(int i =0; i< nodes.length; i++) {
			nodes[i] = new Node(wordList.get(i));
		}
	}
	
	// Searches for each word, looking for other words with one letter difference to add as edge
	public static void addChildren() {
		for(int i = 0; i< nodes.length ;i++) {
			Node n = nodes[i];
			String word = n.label;
			for(int j = 0; j< nodes.length; j++) {
				Node potentialChild = nodes[j];
				if(i != j && isChild(word, potentialChild.label)) {
					n.addLink(potentialChild);
				}
			}
		}
	}
	
	/*
	 * helper to addChildren()
	 * Takes two words and returns true if the second word is one char different than first
	 *  returns false if not 
	 */
	public static boolean isChild(String word, String potential) {
		boolean isChild = false;
		// check for each character in word
		for(int i =0; i<word.length();i++) {
			char[] chars = word.toCharArray();
			// check for all alphabetical characters 
			for(char ch = 'A'; ch <= 'Z'; ch++) {
				chars[i] = ch;
				if(String.valueOf(chars).equals(potential)) {
					return true;
				}
			}
		}
		return isChild;
	}
	
	// prints the graph
	public static void printGraph() 
	{
		for(int i = 0; i< nodes.length; i++) {
			System.out.println("\n*********\n");
			System.out.println("[" + nodes[i].label + "]: ");
			for(int j = 0; j < nodes[i].linkSize(); j++) {
				System.out.println(" " + nodes[i].links.get(j).label +" , ");
			}
			
			
		}
	}
	
	// writes graph to the specified file path 
	public static void writeGraph(String path) {
		try {
		      FileWriter myWriter = new FileWriter(path);
		      for (Node node : nodes) {
		    	  String str = node.label;
		    	  myWriter.write(str);
		    	  for(Node child : node.links) {
		    		  myWriter.write(","+child.label);
		    	  }
		    	  myWriter.write("\n");
		      }
		      myWriter.close();
		      System.out.println("Successfully wrote to the file.");
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
	

	

}

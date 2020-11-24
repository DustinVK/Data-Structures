package vankrimpen.dustin;

import java.util.ArrayList;
import java.util.LinkedList;

/*
 * For task 2:
 * 		
 * 		A graph of courses is initialized where each course is a node containing a list of nodes representing its prereqs.
 * 		
 * 		A modified topological sort sorts the courses into semesters, which are added to a list forming a graduation plan.
 * 	
 * 		The resulting plan is printed to console along with the graph. 
 * 		
 * 		Graduation plan can be created for either machine learning engineering students or game design students
 * 			It sorts courses into semesters based on the maxCreds variable, which represents the max credit hours per semester.   	
 */

public class GraduationPlan {
	// domain choice
	final static int DOMAIN_ML = 0; // machine learning engineering domain 
	final static int DOMAIN_DAGD = 1; // game design and development domain 
	
	static int domain; // the given domain chouice 
	static int maxCreds = 12; // The maximum credit hours to add per semester
	
	// Nodes represents an adjacency-list graph of courses.
	static ArrayList<Node> nodes = new ArrayList<Node>();
	
	static ArrayList<Semester> semesterList = new ArrayList<Semester>(); //When completed, represents a graduation plan 
	static Semester semester = new Semester(); 
	static Semester nextSemester = new Semester(); // run-off list for courses that still need prereqs completed 
	
	// Main method 
	public static void main(String[] args) {
		// set domain path (machine learning engineering or game design 
		domain = DOMAIN_ML; 
		// make graph
		nodes = CourseGraph.makeGraph(domain); 
		// use topological sort to sort courses into semesters and add them to semesterList
		sort(); 
		// print graph to console
		printGraph();
		// print the graduation plan to console
		printPlan();  	
	}
	
	static ArrayList<Node> added = new ArrayList<Node>(); // used to keep track of which courses have been added 
	
	/*
	 * Topological sort is initialized with any Node within the graph 
	 * 		It searches through the graph and calls semesterSort() on each prerequisite course ahead of those which require it. 
	 * 
	 * Graph is recursively searched, and each node is only sent to semesterSort() after all it's edges are added
	 * 
	 * After a node is sent to semesterSort(), nodes is checked and topSort is called on any that haven't been visited
	 */
	
	// initializes topSort, and calls finalizeSemesters() afterwards to finish setting up plan
	private static void sort(){
		// clear out all nodes first
		for(Node n : nodes) {
			n.visited = false;
		}
		topSort(nodes.get(0));
		finalizeSemesters();
	}
	
	// Recursive topological sort
	// Instead of adding nodes to a queue which is returned, it sends them to semesterSort() to account for semesters 
	// (otherwise it would return and be initialized with a queue)   
	private static void topSort(Node node) {
		node.visited = true;
		// TopSort each of node's edges first. 
		for(Node n : node.links) {
			if(!n.visited) { 
				topSort(n);
			}
		}
		// This is where node would be added to a queue if not accounting for semesters 
		semesterSortAdd(node);
		
		// ensure all Nodes have been added 
		for(int i =0;i<nodes.size();i++) {
			if(nodes.get(i).visited == false) {
				topSort(nodes.get(i));
			}
		}
	}
	
	/*
	 * Semester sort separates courses into semesters.
	 * It ensures that a course is only added to semester if its prereqs have been satisfied in a previous semester 
	 */
	private static void semesterSortAdd(Node node){
		// start new semester if adding another course would push the current one over the max credit hour limit 
		if((semester.creds() + 3) > maxCreds) {
			nextSemester.addCourse(node);
			newSemester();
		}
		// if the prereqs haven't been added to previous semesters, then put course in the list for next Semester 
		else if(!preReqSatisfied(node.links)) {
			nextSemester.addCourse(node);
		}
		else {
				// double check that it hasn't been added already  
				if(!semester.courses.contains(node)) {
					semester.addCourse(node);
				}
			}
		}
	
	// starts a new semester
	private static void newSemester() {
		// add all courses of current semester to the added list 
		for(Node n : semester.courses) {
			added.add(n);
		}
		// add the current semester to the semesterList, and initialize a new semester 
		semesterList.add(semester);
		semester = new Semester();
		// search for courses from nextSemester to add to new semester 
		for(int i = 0; i<nextSemester.courses.size();i++) {
			Node course = nextSemester.courses.get(i);
			// ensure the courses prereqs are satisfied 
			if(preReqSatisfied(course.links)) {
				// start a new semester if adding another course would push the current one over the credit limit 
				if((semester.creds()  + 3) > maxCreds) {
					newSemester();
					break;
				}
				// otherwise add course to the semester if it hasn't already been added 
				else {
					if(!added.contains(course)) {
						semester.addCourse(course);
						// remove course from nextSemester list after it has been added to semester
						nextSemester.courses.remove(course);
					}
				
				}
			}
		}
	}
	
	// finishes adding courses to semesters in semesterList
	private static void finalizeSemesters() {
		// add the leftover courses to semesters and semesterList 
		while(!nextSemester.courses.isEmpty()) {
			for(int i = 0;i<nextSemester.courses.size();i++) {
				Node course = nextSemester.courses.get(i);
				nextSemester.courses.remove(course);
				semesterSortAdd(course);
				newSemester(); // create new semester to avoid hangups caused by prequisites not being met 
			}
		}
		if(semester.courses.size() > 0) {
			semesterList.add(semester);
		}
	}
	
	// Takes a list of prerequisite courses (Nodes)
	// Checks if all of the courses given prereqs have been satisfied (added into the semesterList) 
	// Recursively checks all prereq lists of courses in the list 
	public static boolean preReqSatisfied(LinkedList<Node> prereqs) {
		boolean satisfied = true;
		for(Node n : prereqs) {
			if(!added.contains(n)) {
				return false;
			}
			for(Node n1 : n.links) {
				satisfied = preReqSatisfied(n1.links);
			}
		}
		return satisfied;
	}
	
	// Prints the graph 
	public static void printGraph() 
	{
		System.out.println("\n\n------------------------------COURSE GRAPH---------------------------------");
		//System.out.println("* Graph is represented as a list of Nodes (Courses), each containing a list of edges (Prerequisites). *");
		for(int i = 0; i< nodes.size(); i++) {
			System.out.println("\n");
			System.out.printf("%s. Credit hours: %d\n" , nodes.get(i).label, nodes.get(i).credits);
			if(nodes.get(i).linkSize() > 0) {
				System.out.println("	Prerequisites:");
			}
			for(int j = 0; j < nodes.get(i).linkSize(); j++) {
				System.out.printf("	%s\n" , nodes.get(i).links.get(j).label);
			}
		}
		System.out.println("\n\n---------------------------------------------------------------------------\n");
	}
	
	// Prints the graduation plan 
	private static void printPlan(){
		int count = 0;
		int year = 1;
		String path;
		if(domain == DOMAIN_ML) {
			path = "machine learning engineering";
		}
		else {
			path = "game design and development";
		}
		System.out.println("\n\n----------------------------- GRADUATION PLAN -----------------------------");
		System.out.printf("\nThe following plan is for DMSE students following the %s domain path.\n"
				+ "It is based off a maximum of %d credit hours per semester.\n", path, maxCreds);
		System.out.println("\nPrerequisites are listed in parenthesis ().");
		for(Semester semester : semesterList) {
			if(count % 3 == 0) {
				System.out.printf("\n\n................ YEAR %d ................\n", year);
				System.out.printf("\nFALL SEMESTER : %d CREDIT HOURS\n", semester.credTotal);
				++year;
			}
			else if ((count -1) % 3 == 0){
				System.out.printf("\n\nSPRING SEMESTER : %d CREDIT HOURS\n", semester.credTotal);
			}
			else {
				System.out.printf("\n\nSUMMER SEMESTER : %d CREDIT HOURS\n", semester.credTotal);
			}
			for(Node n : semester.courses) {
				String[] fields = n.label.split(":");
				// print elective courses (prereqs not shown for these) 
				if(fields[0].equals("****")) {
					System.out.printf("\n  *  %s %s - %s ", fields[0], fields[1],fields[2]);
				}
				else {
					// print the nodes which represent a list of possible choices 
					if (fields.length > 3) {
						int size = Integer.parseInt(fields[0]);
						System.out.printf("\n  *  Choose 1 of %d :", size);
						for(int j = 1; j<fields.length;j++) {
							if((j-1) % 3 == 0) {
								System.out.print("\n	* "+fields[j]);
							}
							else if((j-3) % 3 == 0) {
								System.out.print(" - " + fields[j]);
								}
							else {
								System.out.print(" " + fields[j]);
							}

						}
						
					}
					else {
						// print course 
						System.out.printf("\n  *  %s %s - %s ", fields[0], fields[1],fields[2]);
						if(!n.links.isEmpty()) {
							System.out.print(" (");
							// print prereqs
							for(int i =0;i<n.links.size();i++) {
								String[] preFields = n.links.get(i).label.split(":");
								System.out.print(preFields[0]+" "+preFields[1]);
								if(i!=n.links.size()-1) {
									System.out.print(", ");
								}
							}
							System.out.print(")");
						}
						
					}
				}
				
			}
			count ++;
		}
		System.out.println("\n\n---------------------------------------------------------------------------\n\n");
	}
	
}

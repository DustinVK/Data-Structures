package vankrimpen.dustin;

import java.util.ArrayList;
import java.util.LinkedList;

/*
 * This class is for creating and returning an ArrayList of Nodes representing a graph of courses 
 */

public class CourseGraph {


	/*
	 * Takes one argument - an int representing student's chosen application domain
	 * 
	 * Returns a graph of Nodes representing courses
	 * 	where each Node contains a list of Nodes representing it's prerequisite courses.
	 * 
	 * Node labels are formated like so:
	 * 		* Labels are a string of fields delimited with a colon ':' 
	 * 		* If the node represents a possible choice, 
	 * 			 then the label begins with an integer representing the number of choices, 
	 * 			 and the fields for the possible choices are listed after.  
	 * 		* Otherwise the label is formatted like so:
	 * 			 "prefix:code:name" 
	 * 		* Credit hours are passed to Node constructor as int 
	 * 
	 * The graph assumes the following:
	 * 		DAGD students will take DAGD 315 over PROJ 320 (and vice versa for ML students since DAGD 315 requires DAGD prereqs) 
	 * 		Machine learning students will take SENG 225 over ISYS 200/272 since SENG 225 is a prereq for courses in the ML path 
	 * 			(DAGD students can select from either of the 3) 
	 * 		 
	 */
	public static ArrayList<Node> makeGraph(int domain) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		LinkedList<Node> prereqs = new LinkedList<Node>();
		Node node;
		
		// Communications Comptency
		node = new Node("COMM:121:Fundamentals of Public Speaking",3);
		nodes.add(node);
		
		node = new Node("ENGL:150:English 1",3);
		nodes.add(node);
		
		prereqs = new LinkedList<Node>();
		prereqs.add(node);
		node = new Node("ENGL:250:English 2",3, prereqs);
		nodes.add(node);
		
		prereqs = new LinkedList<Node>();
		prereqs.add(node);
		node = new Node("3:ENGL:311:Advanced Technical Writing:ENGL:321:Advanced Composition:ENGL:325:Advanced Business Writing",3, prereqs);
		nodes.add(node);
		
		//Mathematics
		//Quantitive Competency 
		Node math220 = new Node("MATH:220:Analytical Geometry-Calculus 1",4);
		nodes.add(math220);
		
		prereqs = new LinkedList<Node>();
		prereqs.add(math220);
		node = new Node("MATH:230:Analytical Geometry Calculus 2",4, prereqs);
		nodes.add(node);
	
		prereqs = new LinkedList<Node>();
		prereqs.add(node);
		node = new Node("MATH:322:Linear Algebra",3, prereqs);
		nodes.add(node);
		
		prereqs = new LinkedList<Node>();
		prereqs.add(math220);
		node = new Node("MATH:328:Discrete Structures",3, prereqs);
		nodes.add(node);
		
		// Natural Sciences Competency
		node = new Node("PHYS:241:General Physics 1",5, prereqs);
		nodes.add(node);
		
		// Software Development / Foundation 
		
		Node seng101 = new Node("SENG:101:Computer Programming 1",3);
		nodes.add(seng101);
		
		prereqs = new LinkedList<Node>();//*********************************
		prereqs.add(seng101);
		Node seng102 = new Node("SENG:102:Computer Programming 2",3, prereqs);
		nodes.add(seng102);
		
		prereqs = new LinkedList<Node>();//***************************************
		prereqs.add(seng102);
		Node seng300 = new Node("SENG:300:Data Structures and Algorithms",3, prereqs);
		nodes.add(seng300);
		
		node = new Node("2:SENG:250:Fundementals of Operating Systems and Networking:ISYS:327:Operating Systems and Network Essentials",3, prereqs);
		nodes.add(node);
		
		prereqs = new LinkedList<Node>();//*****************************************
		prereqs.add(seng300);
		Node seng301 = new Node("SENG:301:Programming Languages",3, prereqs);
		nodes.add(seng301);
		
		Node seng315 = new Node("SENG:315:Software Component Design",3, prereqs);
		nodes.add(seng315);
		
		Node seng160 = new Node("SENG:160:Software Engineering Methodlogies and Processes",3);
		nodes.add(seng160);
		
		prereqs = new LinkedList<Node>();//****************************************
		prereqs.add(seng160);
		node = new Node("SENG:170:Software Requirements Management",3, prereqs);
		nodes.add(node);
		
		node = new Node("SENG:210:Software Configuration Management",3, prereqs);
		nodes.add(node);
		
		Node seng302 = new Node("SENG:302:Software Quality Assurance",3, prereqs);
		nodes.add(seng302);
		
		Node seng355 = new Node("SENG:355:Software Engineering Emerging Tools and Paradigms",3, prereqs);
		nodes.add(seng355);
		
		prereqs = new LinkedList<Node>();//*****************************************
		prereqs.add(seng300);
		prereqs.add(seng160); //**********
		Node seng350 = new Node("SENG:350:Software Design and Architecture",3, prereqs);
		nodes.add(seng350);
		
		prereqs = new LinkedList<Node>();//****************************************
		prereqs.add(seng350);
		node = new Node("SENG:400:Engineering Enterprise Software Applications",3, prereqs);
		nodes.add(node);
		
		node = new Node("SENG:430:Programming Grapical User Interfaces",3, prereqs);
		nodes.add(node);
		
		// Electives - denoted with **** for prefix and *** for code 
		// they are added to each other as prereqs to spread them out
		node = new Node("****:***:Culture Comptency Elective",3);
		nodes.add(node);
		
		prereqs = new LinkedList<Node>();
		prereqs.add(node);
		node = new Node("****:***:Self and Society Elective",3, prereqs);
		nodes.add(node);
		
		prereqs = new LinkedList<Node>();
		prereqs.add(node);
		node = new Node("****:***:Global Diversity Elective",3,prereqs);
		nodes.add(node);
		
		prereqs = new LinkedList<Node>();
		prereqs.add(node);
		node = new Node("****:***:U.S. Diversity Elective",3,prereqs);
		nodes.add(node);
		
		prereqs = new LinkedList<Node>();
		prereqs.add(node);
		node = new Node("****:***:Natural Science Elective", 3, prereqs);
		nodes.add(node);
		
		
		if(domain == GraduationPlan.DOMAIN_ML) { // for machine learning domain 
			
			prereqs = new LinkedList<Node>();
			prereqs.add(seng300);
			node = new Node("SENG:309:Introduction to Machine Learning",3, prereqs);
			nodes.add(node);
			
			prereqs = new LinkedList<Node>();
			prereqs.add(seng300);
			prereqs.add(node);
			node = new Node("SENG:409:Applied Machine Learning in Software as a Service Environments",3, prereqs);
			nodes.add(node);
			
			// assume student takes SENG225 as their choice in engineering fundamentals since it is a preq for SENG 410 and 422
			prereqs = new LinkedList<Node>();
			prereqs.add(seng102);
			node = new Node("SENG:225:Introduction to Database Design",3, prereqs); 
			nodes.add(node);
			
			prereqs = new LinkedList<Node>();
			prereqs.add(node);
			prereqs.add(seng300);
			node = new Node("SENG:410:Introduction to Big Data Concepts and Tools",3, prereqs);
			nodes.add(node);
			
			node = new Node("SENG:422:Introduction to Cloud Application Development",3, prereqs);
			nodes.add(node);
	
		}
		else {
			// engineering fundamentals choice 
			prereqs= new LinkedList<Node>();
			prereqs.add(seng102);
			node = new Node("3:SENG:225:Introduction to Database Design:ISYS:200:Database Design and Implementation"
					+ ":ISYS:200:Database Design and Implementation",3, prereqs);
			nodes.add(node);
		}
		
		// professional development
		prereqs= new LinkedList<Node>();
		prereqs.add(seng350);
		prereqs.add(seng355);
		node = new Node("SENG:420:Software Development Industry Certification",3, prereqs);
		nodes.add(node);
		
		prereqs= new LinkedList<Node>();
		prereqs.add(seng301);
		prereqs.add(seng302);
		prereqs.add(seng315);
		node = new Node("SENG:491:Applied Internship",3, prereqs);
		nodes.add(node);
		
		//Professional development
		prereqs= new LinkedList<Node>();
		prereqs.add(node);
		node = new Node("SENG:499:Capstone in Software Engineering",3, prereqs);
		nodes.add(node);
		
		// Engineering fundamentals
		node = new Node("STQM:260:Introduction to Statistics",3);
		nodes.add(node);
		
		// Self & Society Competency 
		node = new Node("2:ECON:221:Principles of Macroeconomics:ECON:222:Principles of Microeconomics",3);
		nodes.add(node);
		
		//Business Management
		//Culture Competency
		node = new Node("PHIL:216:Introduction to Ethics",3);
		nodes.add(node);
		
		node = new Node("BLAW:321:Contracts and Sales",3);
		nodes.add(node);
		
		if (!(domain == GraduationPlan.DOMAIN_DAGD)) {
			// assume choice of PROJ320 for non DADG focus
			// assume DAGD 315 for DAGD students 
			node = new Node("PROJ:320:Project Managment Fundementals",3);
			nodes.add(node);
		}
		else { // if game design and development domain 
			
			// on list as optional but it's a prereq for DAGD 230... 
			node = new Node("DAGD:100:3D Modeling Animation 1",3);
			nodes.add(node);
			
			prereqs= new LinkedList<Node>();
			prereqs.add(node);
			// not on list but a prereq for DAGD 300
			node = new Node("DAGD:230:3D Modeling Animation 2",3, prereqs);
			nodes.add(node);
			
			prereqs= new LinkedList<Node>();
			prereqs.add(node);
			Node dagd150 = new Node("DAGD:150:Intro to Game Design - Development",3);
			nodes.add(dagd150);
			
			prereqs.add(node);
			node = new Node("DAGD:300:Level Design",3, prereqs);
			nodes.add(node);
		
			// not on list but a prereq for DAGD 260 
			node = new Node("DAGD:104:Digital Imaging",3);
			nodes.add(node);
			
			prereqs= new LinkedList<Node>();
			prereqs.add(node);
			// not on list, but a prereq for DAGD 315 
			node = new Node("DAGD:260:Multimedia Design",3, prereqs);
			nodes.add(node);
			
			// assume DAGD 315 for DAGD students 
			prereqs= new LinkedList<Node>();
			prereqs.add(node);
			prereqs.add(dagd150);
			node = new Node("DAGD:315:Digital Media Productions",3, prereqs);
			nodes.add(node);
					
			prereqs= new LinkedList<Node>();
			prereqs.add(seng101);
			node = new Node("DAGD:255:Game Programming 1",3, prereqs);
			nodes.add(node);

			prereqs= new LinkedList<Node>();
			prereqs.add(node);
			node = new Node("DAGD:355:Game Programming 2",3, prereqs);
			nodes.add(node);
			
		}
		
		return nodes;
	}
}

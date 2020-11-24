package vankrimpen.dustin;

import java.util.ArrayList;

/*
 * Holds a List of courses, and a total number of credit hours 
 * Used in GraduationPlan 
 */
public class Semester {
	ArrayList<Node> courses;
	int credTotal;
	
	public Semester() {
		this.courses = new ArrayList<Node>();
		this.credTotal = 0;
	}
	
	public void addCourse(Node course){
		this.credTotal += course.credits;
		this.courses.add(course);
	}
	
	public int creds() {
		return this.credTotal;
	}
	
	@SuppressWarnings({ "unchecked", "static-access" })
	public Semester clone(){
		Semester sem = new Semester();
		sem.courses = (ArrayList<Node>) this.courses.clone();
		sem.credTotal = creds();
		return sem;
	}
	
	public String toString() {
		String str = "";
		for(Node c : courses) {
			str += c.label + "\n";
		}
		str += "********************\n";
		return str;
	}
	
}

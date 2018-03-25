package CommunityDetection;

import java.util.ArrayList;
import java.util.Comparator;

public class Node{
	public int id;
	public double importance;
	public String name;
	public double memDeg;
	
	public Node(int id, String name)
	{
		this.id=id;
		this.importance = 0;
		this.name = name; 
		this.memDeg = 0;
	}
	
	public static Comparator<Node> memDegreeSort = new Comparator<Node>() {
		@Override
		public int compare(Node n1, Node n2)
		{
			return (int)(n2.memDeg-n1.memDeg);
		}
	};
	
	public static Comparator<ArrayList<Node>> importanceSort = new Comparator<ArrayList<Node>>() {
		@Override
		public int compare(ArrayList<Node> n1, ArrayList<Node> n2)
		{
			return (int)(n2.get(1).importance-n1.get(1).importance);
		}
	};
	
	//New
	
	@Override
	public boolean equals(Object comp)
	{
		boolean isSame = false;
		
		if(comp instanceof Node)
		{
			Node temp = (Node)comp;
			isSame = temp.id==this.id;
		}
		return isSame;
	}
	
	@Override
	public int hashCode()
	{
		return this.id;
	}
	
}

package CommunityDetection;

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
	
	public static Comparator<Node> importanceSort = new Comparator<Node>() {
		@Override
		public int compare(Node n1, Node n2)
		{
			return (int)(n2.importance-n1.importance);
		}
	};
	
	
}

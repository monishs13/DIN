package CommunityDetection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class Communities {
	private int[][] userData;
	private Node[] userListData;
	private HashMap<Integer, String> userName;
	
	public Communities(int[][] users, Node[] userList, HashMap<Integer, String> map)
	{
		this.userData = users;
		this.userListData = userList;
		this.userName = map;
	}
	
	public Node[] getCommunities()
	{
		Node[] p = null;
		ArrayList<ArrayList<Node>> communities = new ArrayList<>();
		ArrayList<Node> importanceList = new ArrayList<Node>();
		int size = userData.length;
		
		for(int i=0; i<size; i++)
		{
				ArrayList<Node> tempList = new ArrayList<>();
				Node tempNode = new Node(i, userName.get(i));
				tempNode.importance = NodeImportance(tempNode);
				tempList.add(tempNode);
				communities.add(tempList);
				
//				importanceList.add(tempList);
//				continue from here
		}
		
		return p;
	}
	
	private double NodeImportance(Node node) 
	{
		return clusteringCoeff(node.id) * border(node.id).size();
	}

	private ArrayList<Node> border(int id) {
		ArrayList<Node> tempList = new ArrayList<>();
		for(int j=0; j<userData.length; j++)
		{
			if(userData[id][j]==1)
			{
				tempList.add(new Node(j, userName.get(j)));
			}
		}
		return tempList;
	}

	private double clusteringCoeff(int id) {
		ArrayList<Node> neighbors = border(id);
		int degree = neighbors.size();
		int connectedNeighbors = 0;
		
		for(int i=0; i<neighbors.size()-1; i++)
		{
			for(int j=i; j<neighbors.size(); j++)
			{
				if(userData[neighbors.get(i).id][neighbors.get(j).id]==1)connectedNeighbors++;
			}
		}
		return (2*connectedNeighbors)/(degree*(degree-1));
	}
	
	private ArrayList<Node> Extension(ArrayList<Node> community)
	{
		ArrayList<Node> borderSet = new ArrayList<>();
		ArrayList<Node> tempBorderSet = null;
		for(int i=0; i<community.size(); i++)
		{
			tempBorderSet = new ArrayList<>();
			tempBorderSet = border(community.get(i).id);
			for(Node temp : tempBorderSet)
			{
				if(!borderSet.contains(temp))
					{
						temp.memDeg = membershipDegree(temp, community);
						borderSet.add(temp);
					}
			}
		}
		Collections.sort(borderSet, Node.memDegreeSort);
		
		while(borderSet!=null)
		{
			Node temp = borderSet.get(0);
			ArrayList<Node> tempList = community;
			tempList.add(temp);
			if(connectivityIndex(tempList)>connectivityIndex(community))
			{
				community.add(temp);
				borderSet.remove(0);
				ArrayList<Node> newBorder = border(temp.id);
				for(int i=0; i<newBorder.size(); i++)
				{
					if(!borderSet.contains(newBorder.get(i)))
					{
						borderSet.add(newBorder.get(i));
					}
				}
			}
			
			else
			{
				borderSet = null;
			}
		}
		
		return community;
	}
	
	private double connectivityIndex(ArrayList<Node> community)
	{
		return (compactness(community)-separability(community))/(Math.sqrt(compactness(community)+separability(community)));
	}

	private int separability(ArrayList<Node> community) {
		int sep = 0;
		for(int i=0; i<community.size(); i++)
		{
			for(int j=0; j<userData.length; j++)
			{
				if(userData[community.get(i).id][j]==1)
				{
//					if(!community.contains(j))sep++;
					boolean add = false;
					for(Node temp : community)
					{
						if(temp.id==j)
						{
							add=true;
							break;
						}
					}
					if(add==false)sep++;
				}
			}
		}
		
		return sep;
	}

	private int compactness(ArrayList<Node> community) {
		int comp = 0;
		for(int i=0; i<community.size()-1; i++)
		{
			for(int j=i; j<community.size(); j++)
			{
				if(userData[community.get(i).id][community.get(j).id]==1)comp++;
			}
		}
		return comp;
	}
	
	private double membershipDegree(Node node, ArrayList<Node> community)
	{
		return 1/(distance(node.id, community)*wtCoeff(node.id, community));
	}

	private int wtCoeff(int id, ArrayList<Node> community) {
		int border = border(id).size();
		int internaldegree = internalDegree(id, community);
		
		return border/internaldegree;
	}
	
	private int internalDegree(int id, ArrayList<Node> community)
	{
		int internaldegree = 0;
		for(int i=0; i<community.size(); i++)
		{
			if(userData[id][community.get(i).id]==1)internaldegree++;
		}
		return internaldegree;
	}

	private int distance(int id, ArrayList<Node> community) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}

package CommunityDetection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
//import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

public class Communities {
	private int[][] userData;
	private Node[] userListData;
	private HashMap<Integer, String> userName;
	public int[][] apspMatrix;
	
	public Communities(int[][] users, Node[] userList, HashMap<Integer, String> map)
	{
		this.userData = users;
		this.userListData = userList;
		this.userName = map;
		this.apspMatrix = new int[users.length][users.length];
	}
	
	public ArrayList<Node> getCommunities()
	{
		ArrayList<Node> influentialNodes = new ArrayList<>();
		ArrayList<ArrayList<Node>> communities = new ArrayList<>();
		ArrayList<ArrayList<Node>> importanceList = new ArrayList<>();
		int size = userData.length;
		
		for(int i=0; i<size; i++)
		{
				ArrayList<Node> tempList = new ArrayList<>();
				Node tempNode = new Node(i, userName.get(i));
				tempNode.importance = NodeImportance(tempNode);
				tempList.add(tempNode);
				communities.add(tempList);
				importanceList.add(tempList);
		}
		Collections.sort(importanceList, Node.importanceSort);
		while(!importanceList.isEmpty())
		{
			ArrayList<Node> centerList = importanceList.get(0);
			Node center = centerList.get(0);
			ArrayList<Node> tempCommunity = border(center.id);
			tempCommunity.add(center);
			ArrayList<Node> finalCommunity = Extension(tempCommunity);
			for(Node i : finalCommunity)
			{
				if(!influentialNodes.contains(i))
				{
					influentialNodes.add(i);
				}
			}
			importanceList.remove(centerList);
		}
		
		
		return influentialNodes;
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
		buildAPSP();
		
		return 0;
	}
	
	private void buildAPSP()
	{
		for(int i=0;i<userData.length;i++)
		{
			dijkstra(i);
		}
	}

	private void dijkstra(int source) {
		Map<Integer, Integer> untouched=new HashMap<Integer, Integer>(userData.length);
		Map<Integer, Integer> processed=new HashMap<Integer, Integer>(userData.length);
		for(int i=0; i<userData.length;i++)
		{
			untouched.put(i,Integer.MAX_VALUE);
		}
		untouched.put(source, 0);
		
		int count=0;
		int selectedIndex;
		while(count!=userData.length)
		{
			selectedIndex=getMinimumvalueIndex(untouched);
			for(int k=0;k<userData.length;k++)
			{
				if(userData[selectedIndex][k]!=0 && untouched.containsKey(k) && ((untouched.get(selectedIndex)+1) <= untouched.get(k)))
				{
					untouched.put(k, (untouched.get(selectedIndex)+1));
				}
			}
			processed.put(selectedIndex, untouched.get(selectedIndex));
			untouched.remove(selectedIndex);
			count++;
		}
		for(int k=0;k<userData.length;k++)
		{
				apspMatrix[source][k]=processed.get(k);
		}
	}
	
	public static int getMinimumvalueIndex(Map<Integer, Integer> map)
	{
		int minValue= Collections.min(map.values());
		int minIndex=-1;
		for (Entry<Integer, Integer> entry : map.entrySet())
		{
		    if(entry.getValue() == minValue) minIndex=entry.getKey();
		    if(minIndex!=-1) break;
		}
		return minIndex;
	}
	
}

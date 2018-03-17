package CommunityDetection;

import java.util.ArrayList;

public class Communities {
	public Node[] getCommunities(int[][] users)
	{
		Node[] p;
		ArrayList<ArrayList<Node>> communities = new ArrayList<>();
		Node[] importance;
		int size = users.length;
		
		for(int i=0; i<size; i++)
		{
			for(int j=0; j<size; j++)
			{
				ArrayList<Node> tempList = new ArrayList<>();
				Node tempNode = new Node(users[i][j]);
				tempNode.importance = NodeImportance(users[i][j]);
				tempList.add(tempNode);
				communities.add(tempList);
			}
		}
		
		return p;
	}
}

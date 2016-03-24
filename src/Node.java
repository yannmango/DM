
public class Node implements Comparable<Node> {
	private String className;//keep the class name of a given node
	private int[] characters;//keep all attributes of a given node
	private Integer distance;//distance between a node and a given test node
	                      //here use Hamming distance
	
	


	public String getClassName() {
		return className;
	}


	public void setClassName(String className) {
		this.className = className;
	}


	public int[] getCharacters() {
		return characters;
	}


	public void setCharacters(int[] characters) {
		this.characters = characters;
	}


	public Integer getDistance() {
		return distance;
	}


	public void setDistance(Integer distance) {
		this.distance = distance;
	}
	
	
	public Node(int[] characters) {
		super();
		this.characters = characters;
	}


	public Node(String className, int[] characters) {
		super();
		this.className = className;
		this.characters = characters;
	}


	@Override
	public int compareTo(Node o) {
		// TODO Auto-generated method stub
		return this.getDistance().compareTo(o.getDistance());
	}
	
	
	
	
	
}

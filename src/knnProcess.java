import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class knnProcess {

	public String testDataPath;//address of test dataset
	public String trainDataPath;//address of train dataset
	public String[][] testData;
	public String[][] trainData;
	public ArrayList<String> classTypes=new ArrayList<String>();
	public HashMap<String,Integer> attributes=new HashMap<String,Integer>();
	public HashMap<Integer,HashMap<String,Integer>> characterMap=new HashMap<Integer,HashMap<String,Integer>>();
	public HashMap<Integer,HashMap<Integer,String>> inver_characterMap=new HashMap<Integer,HashMap<Integer,String>>();
	public ArrayList<Node> testNodes;
	public ArrayList<Node> trainNodes;
	public HashMap<Node,String> verifyNodes;
	public int[] classWeightArray;
	final String[] tempAttrName={"parents","has_nurs","form","children","housing","finance","social","health"};
	
	public knnProcess(String testDataPath, String trainDataPath) {
		super();
		this.testDataPath = testDataPath;
		this.trainDataPath = trainDataPath;
		saveAttributes();
		saveClass();
		//calculateKNN();
	}
	
	private void saveClass(){
		//save train dataset into Array trainData
		ArrayList<String[]> tempArray1=fileConvertToArray(trainDataPath);
		trainData=new String[tempArray1.size()][];
		tempArray1.toArray(trainData);
//		System.out.println("train Length"+trainData.length);
		
		ArrayList<String[]> tempArray2=fileConvertToArray(testDataPath);
		testData=new String[tempArray2.size()][];
		tempArray2.toArray(testData);
//		System.out.println("test Length"+testData.length);
		
		for(String[] s:trainData){
			String tempClassName=s[s.length-1];
			//add new class type
			if(!classTypes.contains(tempClassName)){
				classTypes.add(tempClassName);
			}
			
		}
//		for(int i=0;i<classTypes.size();i++){
//			System.out.println("classType: "+classTypes.get(i));
//		}
		
		//save test dataset into Array testData
		
//		ArrayList<String[]> tempArray2=fileConvertToArray(testDataPath);
//		trainData=new String[tempArray2.size()][];
//		tempArray2.toArray(testData);
//		System.out.println(classTypes);
		numberFeatures(trainData);
	}
	
	private void saveAttributes(){
//		String[] tempAttrName={"parents","has_nurs","form","children","housing","finance","social","health"};
		for(int i=0;i<tempAttrName.length;i++){
			if(!attributes.containsKey(tempAttrName[i])){
				attributes.put(tempAttrName[i], i);	
			}
		}
		
//		Set<String> temp=attributes.keySet();
//		for(String s:temp){
//			System.out.println(s+":"+attributes.get(s));
//		}
		classWeightArray=new int[tempAttrName.length];
		Arrays.fill(classWeightArray, 1);
		
	}
	
	private void numberFeatures(String[][] dataSet){
		for(int i=0;i<attributes.size();i++){
			HashMap<String,Integer> tempChaMap=new HashMap<String,Integer>();
			HashMap<Integer,String> inverTemp=new HashMap<Integer,String>();
			int count=0;
			for(int j=0;j<dataSet.length;j++){
				if(!tempChaMap.containsKey(dataSet[j][i])){
					tempChaMap.put(dataSet[j][i],count);
					inverTemp.put(count, dataSet[j][i]);
					count++;
				}
			}
			if(!characterMap.containsKey(i)){
				characterMap.put(i, tempChaMap);
				inver_characterMap.put(i,inverTemp);
			}
		}
		System.out.println(characterMap);
		System.out.println(inver_characterMap);
		
	}
	/*
	 *convert data in train dataset into array 
	 */
	
	
	private ArrayList<String[]> fileConvertToArray(String DataPath){
		File file=new File(DataPath);
		ArrayList<String[]> dataArray=new ArrayList<String[]>();
		try {
			BufferedReader br=new BufferedReader(new FileReader(file));
			String lineData;
			String[] tempArray;
			while((lineData=br.readLine())!=null){
				tempArray=lineData.split(",");
				dataArray.add(tempArray);
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataArray;
	}
	
	private int calculateDistance(Node n1,Node n2){
		 int[] c1=n1.getCharacters();
		 int[] c2=n2.getCharacters();
		 int distance=0;
		 for(int i=0;i<c1.length;i++){
			 if(c1[i]!=c2[i]){
				 distance+=1;
			 }
		 }
		 return distance;
	}
	
	//calculate k nearest neighbors
	public void calculateKNN(int k){
		Node tempNode;
		trainNodes=new ArrayList<Node>();
		testNodes=new ArrayList<Node>();
		verifyNodes=new HashMap<Node,String>();
		HashMap<String,Integer> classNumber;
		HashMap<String,Integer> classWeight=new HashMap<String,Integer>();
		//transform train dataset into trainNodes
		for(String[] s:trainData){
			int[] tempChar=new int[s.length-1];
			for(int i=0;i<s.length-1;i++){
				String temp=s[i];
//				HashMap<String,Integer> tempAttr=new HashMap<String,Integer>();
				int number=characterMap.get(i).get(temp);
				tempChar[i]=number;
			}
			tempNode=new Node(s[s.length-1],tempChar);
			trainNodes.add(tempNode);
		}
		
		
		//transform test dataset into testNodes
		for(String[] s:testData){
			int[] tempChar=new int[s.length-1];
			for(int i=0;i<s.length-1;i++){
				String temp=s[i];
//				HashMap<String,Integer> tempAttr=new HashMap<String,Integer>();
				int number=characterMap.get(i).get(temp);
				tempChar[i]=number;
			}
			tempNode=new Node(tempChar);
			testNodes.add(tempNode);
			
			verifyNodes.put(tempNode, s[s.length-1]);
		}
		/*need to add test part
		 * 
		 */
//		System.out.println(resultData.size());
		int countConfidence=0;
		ArrayList<Node> knnNodes=new ArrayList<Node>();//keep train data closest to sample
		for(Node n1:testNodes){
			classNumber=new HashMap<String,Integer>();
			int index=0;
			for(String type:classTypes){
				classNumber.put(type, 0);
				classWeight.put(type, classWeightArray[index++]);
			}
			for(Node n2:trainNodes){
				int dis=calculateDistance(n1,n2);
				n2.setDistance(dis); 
			}
			
			Collections.sort(trainNodes);
			knnNodes.clear();
			
			//select k closest data as classification
			for(int i=0;i<trainNodes.size();i++){
				if(i<k){
					knnNodes.add(trainNodes.get(i));
				}else{
					break;
				}
			}
			//give k train dataset classification standard
			for(Node tempN:knnNodes){
				int num=classNumber.get(tempN.getClassName());
				//default:class weight equals to each other
				num+=classWeight.get(tempN.getClassName());
				classNumber.put(tempN.getClassName(), num);
			}
			
			int maxCount=0;
			for(Map.Entry entry:classNumber.entrySet()){
				if((Integer) entry.getValue()>maxCount){
					maxCount=(Integer) entry.getValue();
					String testN=verifyNodes.get(n1);
					n1.setClassName((String) entry.getKey());
//					if(!testN.equals(n1.getDistance())){
//						countConfidence+=1;
//					}
				}
			}
			
		
			System.out.print("test data features: ");
//			for(int i=0;i<attributes.size();i++){
//				
//				for(int j=0;j<n1.getCharacters().length;i++){
//					int[] charac=n1.getCharacters();
//					System.out.print(inver_characterMap.get(i).get(charac[j]));
//				}
//			}
			for(int newS1:n1.getCharacters()){
				System.out.print(newS1+" ");
			}
			
			System.out.println("classfication: "+n1.getClassName());
			
		}
		for(Node n:testNodes){
			if(n.getClassName().equals(verifyNodes.get(n))){
				countConfidence+=1;
			}
		}
		
		System.out.println("confidence"+countConfidence);
		
	}

	
	
	
	
	
}

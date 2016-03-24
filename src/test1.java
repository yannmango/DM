import java.util.ArrayList;

public class test1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String trainDataPath="/Users/yananchen/Desktop/train.txt";
		String testDataPath="/Users/yananchen/Desktop/test.txt";
		knnProcess kp=new knnProcess(testDataPath,trainDataPath);
		kp.calculateKNN(40);
	}

}

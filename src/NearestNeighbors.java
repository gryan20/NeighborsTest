// I need to add a number count in front I know I have to do count ++ but I don't know where to put it.
//It won't print off half of the words it's suppose to


import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;


public class NearestNeighbors {

	// private List<Iris>irisDataSet; DONT NEED
	private List<Iris>testDataSet;
	private List<Iris>trainingDataSet;
	private List<Iris>condensedDataSet;
	
	public NearestNeighbors() {
		// irisDataSet = new ArrayList<Iris>();  DON'T NEED
		testDataSet = new ArrayList<Iris>();
		trainingDataSet = new ArrayList<Iris>();
		condensedDataSet = new ArrayList<Iris>();
	}
	
	public static void main(String[] args) throws IOException {
		int number = 0;
		
		System.out.println("Programming Fundamentals");
		System.out.println("Name: Gabby Ryan");
		System.out.println("PROGRAMMING ASSIGNMENT 3");
		System.out.println(" ");
		
		
		DataReader read = new DataReader();
		Scanner scanner = new Scanner(System.in);
		NearestNeighbors cnnImpl = new NearestNeighbors();

		
		System.out.println("Enter the path for the training data: ");
		String 	  irisPath = scanner.nextLine();
		// storing them separately
		cnnImpl.SetIrisTrainingSet(read.getIrisData(irisPath));
		System.out.println("Enter the path for the testing data: ");
		irisPath = scanner.nextLine();
		System.out.println("EX#:  TRUE LABEL, PREDICTED LABEL");
		cnnImpl.SetIrisTestSet(read.getIrisData(irisPath));
		scanner.close();

		
		cnnImpl.prepareCondensedSet(); // Do i need the condensed Data set?
		for (Iris i : cnnImpl.condensedDataSet)
		{
			System.out.println(i);
		}
		
		double classifyAcc = cnnImpl.calculateClassificationAcc();
		System.out.println("ACCURACY: " + classifyAcc);
	}
	
		private double calculateClassificationAcc()  
		{
			double minDist = 999999;
			double correctClassification = 0;
			Iris closestSample = null;
		
			for (Iris testD : testDataSet)
			{
				minDist = 999999;
				for (Iris trainD : condensedDataSet)
				{
					double calcDistance = testD.distance(trainD);
					if(calcDistance < minDist)
					{
						minDist = calcDistance;
						closestSample = trainD;
					}
				}
				if (testD.type == closestSample.type)
				{
					correctClassification++;
				}
			}
			//System.out.println("Correct classified:" + correctClassification);
			double classAccuracy  = correctClassification/(testDataSet.size());
			return classAccuracy; // ** Need to change this formating to more decimal places I'll work on this while you look at the other stuff.
		}
		
		public void prepareCondensedSet()
		{
			condensedDataSet.add(trainingDataSet.get(0));
			
			int unchangedCount = 0;
			List<Iris>reducedSet = constructReducedSet();
			
			int previousReducedSetSize = 0;
			while (true)
			{
				previousReducedSetSize = reducedSet.size();
				int i = 0;
				while (!reducedSet.isEmpty())
				{
					Iris ir = reducedSet.get(0);
					double minDist = 999999;
					IrisType type =IrisType.SETOSA;
					Iris ic = null;
				
					for (int j = 0; j < condensedDataSet.size(); j++)
					{
						ic = condensedDataSet.get(j);
						double distBetweenPattern = ir.distance(ic);
						if (distBetweenPattern < minDist)
						{
							minDist = distBetweenPattern;
							type = ic.type;
						}
					}

					if (ir.type != type)
					{
						condensedDataSet.add(ir);
					}

					reducedSet.remove(ir);
				}

				reducedSet = constructReducedSet();
				if (previousReducedSetSize == reducedSet.size())
				{
					unchangedCount++;
				}
				else
				{
					unchangedCount = 0;
				}
				if (unchangedCount == 2) {
					break;
				}
			}
		}
		private List<Iris>constructReducedSet()
		{
			List<Iris> reducedSet = new ArrayList<Iris>();
			for (Iris iTrain : trainingDataSet)
			{
				if (condensedDataSet.contains(iTrain))
				{
					continue;
				}
				reducedSet.add(iTrain);
			}

			return reducedSet;
		}
			
		public void SetIrisTrainingSet(List<Iris>irisDataSet)
		{
				this.trainingDataSet = irisDataSet;
		}
			
		public void SetIrisTestSet(List<Iris>irisDataSet)
		{
				this.testDataSet = irisDataSet;
		}
}
class DataReader{
	public List<Iris> getIrisData(String irisFilePath) throws IOException
	{
		Scanner irisDataReader = new Scanner(new FileReader(irisFilePath));
		List<Iris> irisDataSet= new ArrayList<Iris>();

		while(irisDataReader.hasNextLine())
		{
			String input = irisDataReader.nextLine();
			String[] attr = null;
					
			if (input != null)
			{
				attr = ((String) input).split(",");
			}
					
			if (attr != null && attr.length == 5)
			{
				IrisType type = IrisType.getType(attr[4]); // will I have to change this since I only need 2 (or 3 with the # printed?)
				Iris sample = new Iris(Double.parseDouble(attr[0]),
						Double.parseDouble(attr[1]),
						Double.parseDouble(attr[2]),
						Double.parseDouble(attr[3]),
						Double.parseDouble(attr[4]),
						type);
				irisDataSet.add(sample);
			}
		}
			return irisDataSet;
	}
}

class Iris
{
	double sepalLength;
	double sepalWidth;
	double petalLength;
	double petalWidth;
	double number;
	IrisType type;

	Iris(double sl, double sw, double pl, double pw, double number, IrisType t)
	{
		sepalLength = sl;
		sepalWidth = sw;
		petalLength = pl;
		petalWidth = pw;
		type = t;
		
	}
			
	public String toString()
	{
		return type.getLabel() + " " + type.getLabel(); //how do I enter a count before this?
	}
			
	public boolean equals(Iris other)
	{
		return ((this.sepalLength == other.sepalLength) &&
				(this.petalLength == other.petalLength) &&
				(this.sepalWidth == other.petalWidth) &&
				(this.type == other.type));
	}
			
	public double distance(Iris other)
	{
		double d1 = Math.pow((this.sepalLength - other.sepalLength), 2);
		double d2 = Math.pow((this.sepalWidth - other.sepalWidth), 2);
		double d3 = Math.pow((this.petalWidth - other.petalWidth), 2);
		double d4 = Math.pow((this.petalLength - other.petalLength),2);
		return Math.sqrt(d1 + d2 + d3 + d4);
	}
			
}
enum IrisType
{
	SETOSA(1, "Iris-Setosa"), VERSICOLOR(2,"Iris-Versicolor"), VIRGINICA(3,"Iris-Virginica");
	private int code;
	private String label;

	IrisType(int code, String label)
	{
		this.code = code;
		this.label = label;
	}
	public int getCode()
	{
		return this.code;
	}
	public String getLabel()
	{
		return this.label;
	}
	public static IrisType getType(String type)
	{
		if ("Iris-versicolor".equals(type))
		{
			return VERSICOLOR;
		}
		if ("Iris-virginica".equals(type))
		{
			return VIRGINICA;
		}
		return SETOSA;
	}
}

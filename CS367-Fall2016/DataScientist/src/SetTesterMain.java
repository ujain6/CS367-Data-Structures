///////////////////////////////////////////////////////////////////////////////
//                   
// Title:            Program 4
// Files:            ArrayListSetTester.java, BSTIterator.java, BSTNode.java,
//					 BSTreeSetTester.java, DuplicateKeyexception.java, 
//				     RBTreeSetTester.java, SetTesterADT.java, SetTesterMain.java
//				     SetTesterADT, Stats.java
// Semester:         (CS367) Spring 2016
//
// Author:           Utkarsh Jain
// Email:            ujain6@wisc.edu
// CS Login:         utkarsh
// Lecturer's Name:  Jim Skrentny
//
//
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Run several data experiments and display results, so you can analyze the
 * results and answer some questions about the relative efficiencies of various
 * data structures.
 * 
 * <p>
 * The data will be stored in four different data structures and the analysis
 * will include measuring the time to complete three different common operations
 * on different sized random and sorted data sets.
 * </p>
 * 
 * <p>
 * The data types each implement the SetTesterADT interface but have different
 * underlying data structures and thus different performance when the problem
 * size is large. Having each type implement a common interface will make it
 * easier to run equivalent comparisons.
 * </p>
 *
 * @author CS367
 */
public class SetTesterMain {

	/** Data read from a file and used for evaluation timing trials. */
	private List<Integer> originalDataList;

	/** Number of trials to run for each operation */
	private int numOfTrials;

	/** Rebalance threshold for binary search tree with rebalance */
	private int rebalanceThreshold;

	/** The number of levels to display of the BSTree structures */
	private int numOfDisplayLevels;

	/**
	 * <p>
	 * Main method. Every application needs one. Command-line arguments are
	 * required.
	 * </p>
	 * 
	 * <ul>
	 * <li>args[0] is the name of an existing data file that contains a a single
	 * line of [randomly generated] integers. This is the data set that will be
	 * used for all trials.</li>
	 * 
	 * <li>args[1] is a positive integer indicating the number of trials to be
	 * run. The final results display the average(mean) of all trials.</li>
	 * 
	 * <li>args[2] is a positive integer indicating the rebalancing threshold.
	 * See BSTreeSetTester class for more information.</li>
	 * 
	 * <li>args[3] is an integer indicating how many levels deep to display the
	 * contents of the binary search trees.</li>
	 * </ul>
	 * 
	 * <p>
	 * For example:
	 * </p>
	 * 
	 * <pre>
	 * java SetTesterMain random_1000.txt 10 2 3
	 * </pre>
	 *
	 * @param args
	 *            filename numTrials rebalanceThreshold numLevels
	 */
	public static void main(String[] args) {
		if (args.length != 4) {
			System.err.println("Bad invocation! Correct usage: " + "java Evaluator <Data file> "
					+ "<Number of Trials> <Rebalance Threshold> <Number of " + "Levels to Display>");
			System.exit(1);
		}

		String dataFilename = args[0];
		int numOfTrials = Integer.valueOf(args[1]);
		int rebalanceThreshold = Integer.valueOf(args[2]);
		int numOfLevelsToDisplay = Integer.valueOf(args[3]);

		SetTesterMain evaluator = new SetTesterMain(dataFilename, numOfTrials, rebalanceThreshold,
				numOfLevelsToDisplay);

		// Run evaluation for unsorted data.
		evaluator.runEvaluation(false);

		// Run evaluation for sorted data.
		evaluator.runEvaluation(true);

	}

	/**
	 * Evaluator constructor.
	 * 
	 * @param dataFile
	 *            data file name.
	 * @param numOfTrials
	 *            number of trials for each evaluation.
	 * @param rebalanceThreshold
	 *            rebalance threshold for the binary search tree with rebalance.
	 * @param numOfDisplayLevels
	 *            The number of levels to display for BSTree
	 */
	public SetTesterMain(String dataFile, int numOfTrials, int rebalanceThreshold, int numOfDisplayLevels) {

		this.numOfTrials = numOfTrials;
		this.rebalanceThreshold = rebalanceThreshold;
		this.numOfDisplayLevels = numOfDisplayLevels;

		// Read integer data values from the file
		try {
			originalDataList = new ArrayList<Integer>();
			Scanner fileScnr = new Scanner(new File(dataFile));

			// read individual data values from the file
			// all values must be integers
			while (fileScnr.hasNextInt()) {
				originalDataList.add(fileScnr.nextInt());
			}

			fileScnr.close();
		} catch (FileNotFoundException e) {
			System.out.println("SetTesterMain Construction fails..." + dataFile + " file not found.");
			System.exit(0);
		} catch (Exception e) {
			System.out.println("SetTesterMain Construction fails..." + e.getMessage());
			System.exit(0);
		}
	}

	/**
	 * Run the evaluation trials and display result tables.
	 *
	 * Step 1: Evaluate and store times for each data structure:
	 * <ol>
	 * <li>ListSetTester uses an ArrayList for internal data structure</li>
	 * <li>Binary Search Tree(bsTree)</li>
	 * <li>Binary Search Tree with rebalance(bsTreeB)</li>
	 * <li>RedBlack Tree(rbTree)</li>
	 * </ol>
	 *
	 * Step 2: Display the evaluation results in table form for easy analysis.
	 *
	 * Step 3. Display the top (N) levels of the BSTree and BSTreeB, where n is
	 * entered by the user as a command-line argument.
	 *
	 * @param sortData
	 *            if true, sort the data before evaluation.
	 */
	public void runEvaluation(boolean sortData) {

		if (sortData) {
			Collections.sort(originalDataList);
		}

		SetTesterADT<Integer> listSet = new ArrayListSetTester<Integer>();
		Stats listSetResult = evaluateAll(listSet);

		SetTesterADT<Integer> bsTree = new BSTreeSetTester<Integer>(0);
		Stats bsTreeResult = evaluateAll(bsTree);

		SetTesterADT<Integer> bsTreeB = new BSTreeSetTester<Integer>(rebalanceThreshold);
		Stats bsTreeBResult = evaluateAll(bsTreeB);

		SetTesterADT<Integer> rbTree = new RBTreeSetTester<Integer>();
		Stats rbTreeResult = evaluateAll(rbTree);

		displayResultTable(sortData, listSetResult, bsTreeResult, bsTreeBResult, rbTreeResult, rebalanceThreshold);

		bsTree.displayTree(numOfDisplayLevels);
		bsTreeB.displayTree(numOfDisplayLevels);
	}

	/**
	 * The evaluation method runs the trials and stores the results for these
	 * operations:
	 * 
	 * <ol>
	 * <li>the time to build the Integer data structure (add all items)</li>
	 * <li>the time to search for individual items in the data structure</li>
	 * <li>the time to search for all items within a given range of values</li>
	 * </ol>
	 *
	 * The number of trials has been stored as a data field. A Stats instance is
	 * created and returned so that all results for a given data structure are
	 * together for future analysis and reporting.
	 *
	 * @param dataStructure
	 *            the data structure to evaluate.
	 * @return the evaluation results stats
	 */
	public Stats evaluateAll(SetTesterADT<Integer> dataStructure) {

		Stats stats = new Stats();
		for (int t = 0; t < numOfTrials; t++) {

			long trialTime = evaluateBuild(dataStructure);
			stats.addBuildStat(trialTime);

			trialTime = evaluateContainsSearch(dataStructure);
			stats.addContainsSearchStat(trialTime);

			trialTime = evaluateRangeSearch(dataStructure);
			stats.addRangeSearchStat(trialTime);
		}
		return stats;
	}

	/**
	 * Evaluate the building tree process. It should be repeated numOfTrials
	 * times. Note: in each trials, clear the tester before you start the time
	 * and add items to it. However, after the evaluation, the tester should
	 * contain all the data. (Don't clear it).
	 * 
	 * @param tester
	 *            the data structure to evaluate
	 * @return the time (milliseconds) to build the data structure
	 */
	public long evaluateBuild(SetTesterADT<Integer> tester) {
		tester.clear();
		long startTime = System.currentTimeMillis();
		for (Integer elem : originalDataList) {
			tester.add(elem);
		}
		return System.currentTimeMillis() - startTime;
	}

	/**
	 * Run a single trial that times and searches for each item in the original
	 * data listing. The data structure contains any of four different data set
	 * implementations. Returns the elapsed time in milliseconds.
	 *
	 * @param ds
	 *            the data structure filled with data to test
	 * @return elapsed time in milliseconds
	 * @throws RuntimeException
	 *             if a data item is not found in the SetTester
	 */
	public long evaluateContainsSearch(SetTesterADT<Integer> ds) {
		//Initialzing the start time
		long startTime = System.currentTimeMillis();
		
		//Iterates through each element in the data list
		for (Integer elem : originalDataList) {
			if (!ds.contains(elem)) {
				throw new RuntimeException();
			}
		}
		
		//Retunring the time elapsed
		return System.currentTimeMillis() - startTime;
	}

	/**
	 * Time how long it take to search for items within a given range from each
	 * element.
	 * 
	 * @param ds
	 *            the data structure to evaluate
	 * @return the time (milliseconds) for range search
	 */
	public long evaluateRangeSearch(SetTesterADT<Integer> ds) {
		
		//Initialization of the time
		long startTime = System.currentTimeMillis();
		
		//Iterates through the data list
		for (Integer elem : originalDataList) {
			ds.subSet(elem - 10, elem + 10);

		}
		
		//returns elapsed time
		return System.currentTimeMillis() - startTime;
	}

	/**
	 * Display the evaluation results. DO NOT EDIT!
	 * 
	 * @param sorted
	 *            indicates if the data was sorted for these results
	 * @param listResult
	 *            evaluate results of ListSet
	 * @param bsTreeResult
	 *            evaluate results of bsTree
	 * @param bsTreeBResult
	 *            evaluate results of bsTreeB
	 * @param rbTreeResult
	 *            evaluate results of rbTree
	 * @param rebalanceThreshold
	 *            the integer indicating when to rebalance the BSTreeB data
	 *            structure
	 */
	public void displayResultTable(boolean sorted, Stats listResult, Stats bsTreeResult, Stats bsTreeBResult,
			Stats rbTreeResult, int rebalanceThreshold) {

		// DISPLAY IF THESE RESULTS ARE FOR SORTED OR RANDOM DATA
		if (sorted)
			System.out.print("SORTED ");
		else
			System.out.print("RANDOM ");

		// DISPLAY TABLE COLUMN HEADERS
		System.out.println("DATA RESULTS:(Average Time-ms)    #items:" + originalDataList.size() + "  #Trials:"
				+ numOfTrials + "  Thresh:" + rebalanceThreshold);
		System.out.println("Tester D.S.(ms):\t   Build Tree\tContains " + "Search\t\tRange Search");
		System.out.println("=================================================" + "===========================");

		// DISPLAY RESULTS FOR ARRAY LIST
		System.out.printf("ArrayListSet:\t\t%13f\t%14f\t%20f\n", listResult.getBuildMean(),
				listResult.getContainsSearchMean(), listResult.getRangeSearchMean());
		System.out.println("-------------------------------------------------" + "---------------------------");

		// DISPLAY RESULTS FOR BINARY SEARCH TREE (no rebalancing)
		System.out.printf("BSTreeSet:\t\t%13f\t%14f\t%20f\n", bsTreeResult.getBuildMean(),
				bsTreeResult.getContainsSearchMean(), bsTreeResult.getRangeSearchMean());
		System.out.println("-------------------------------------------------" + "---------------------------");

		// DISPLAY RESULTS FOR BINARY SEARCH TREE (w/rebalancing)
		System.out.printf("BSTreeBSet:\t\t%13f\t%14f\t%20f\n", bsTreeBResult.getBuildMean(),
				bsTreeBResult.getContainsSearchMean(), bsTreeBResult.getRangeSearchMean());
		System.out.println("-------------------------------------------------" + "---------------------------");

		// DISPLAY RESULTS FOR RED-BLACK TREE (TreeSet)
		System.out.printf("RBTreeSet:\t\t%13f\t%14f\t%20f\n", rbTreeResult.getBuildMean(),
				rbTreeResult.getContainsSearchMean(), rbTreeResult.getRangeSearchMean());
		System.out.println("=================================================" + "===========================");
	}
}

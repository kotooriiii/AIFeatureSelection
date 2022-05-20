package driver;

import classifier.AbstractClassifier;
import classifier.KNNClassifier;
import evaluation.AbstractEvaluation;
import evaluation.LeaveOneOutEvaluation;
import evaluation.RandomEvaluation;
import instance.DataInstance;
import search.AbstractSearch;
import search.BackwardSelectionSearch;
import search.ForwardSelectionSearch;
import tree.FeatureSelectionTree;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class GUI
{

    private final Set<Integer> maxFeatures;

    private AbstractClassifier classifier;
    private AbstractEvaluation evaluation;
    private AbstractSearch search;

    //Scanning keyboard input
    private Scanner scanner;

    /**
     * The size of the puzzle.
     */
    public GUI()
    {
        this.scanner = new Scanner(System.in);
        this.maxFeatures = new HashSet<>();
    }

    private void populateState()
    {


        System.out.println("-- Type Your Number Of Features --");
        System.out.println("Number Of Features (expecting integer): ");
        final int input = scanner.nextInt();

        for (int i = 1; i <= input; i++)
        {
            maxFeatures.add(i);
        }

        clearScreen();
    }

    private File populateFile()
    {
        while (true)
        {
            System.out.println("-- Type File Name --");
            System.out.println("File name: ");

            final String line = scanner.nextLine();
            try
            {
                File file = new File(line);
                if (!file.exists())
                    throw new NullPointerException();


                return file;
            } catch (NullPointerException e)
            {
                System.out.println("Error: Incorrect file name");
            }
        }
    }

    private boolean isIdentifying()
    {
        clearScreen();

        int input = -1;

        while (input != 0 && input != 1)
        {
            System.out.println("-- Configuration File --");
            System.out.println();
            System.out.println("[EXAMPLE FILE BELOW]");
            System.out.println("-------------------------------------------------");
            System.out.println("  Classes  HairLength  ShoeSize  Height  Weight");
            System.out.println("  Male  5.3  5  8.9  128");
            System.out.println("  Female  12  5  6  88");
            System.out.println("-------------------------------------------------");
            System.out.println();
            System.out.println("Does the first line of the file identify the name of the features like on the example above? ('1' for yes, '0' for no): ");
            System.out.println("Note: If this is for the project assignment grade, we recommend you select '0'");
            input = scanner.nextInt();
        }

        return input == 1;
    }

    private boolean isDebugMode()
    {
        clearScreen();

        int input = -1;

        while (input != 0 && input != 1)
        {
            System.out.println("-- Debug Mode --");
            System.out.println("Do you want to print all debugging messages to console? ('1' for yes, '0' for no): ");
            System.out.println("Note: Pressing '1' can spam the console.");
            System.out.println("Note: Pressing '0' will STILL send you the total time it takes for each step.");

            input = scanner.nextInt();
        }

        return input == 1;
    }

    private void populateClassifier(MachineLearningManager manager)
    {


        int input = 0;
        while (input == 0)
        {
            System.out.println("-- Select Classifier --");
            System.out.println("1. K-Nearest Neighbor Classifier");

            input = scanner.nextInt();

            switch (input)
            {
                case 1:
                    int k = populateKNN();
                    classifier = new KNNClassifier(manager, k);
                    break;
                default:
                    input = 0;
                    break;
            }
        }
        manager.setClassifier(classifier);
        clearScreen();
    }

    private int populateKNN()
    {


        int input = -1;
        while (input <= 0)
        {
            System.out.println("-- Select Your 'K' for KNN --");
            System.out.println("Enter a positive integer: ");

            input = scanner.nextInt();

        }
        clearScreen();
        return input;
    }

    private void populateSearch(MachineLearningManager manager)
    {


        int input = 0;
        while (input == 0)
        {
            System.out.println("-- Select Search --");
            System.out.println("1. Forward Selection Search");
            System.out.println("2. Backward Selection Search");

            input = scanner.nextInt();

            switch (input)
            {
                case 1:
                    search = new ForwardSelectionSearch(manager.getTree());
                    break;
                case 2:
                    search = new BackwardSelectionSearch(manager.getTree());
                    break;
                default:
                    input = 0;
                    break;
            }
        }
        manager.setSearch(search);
        clearScreen();
    }

    private void populateEvaluation(MachineLearningManager manager)
    {


        int input = 0;
        while (input == 0)
        {
            System.out.println("-- Select Evaluation --");
            System.out.println("1. Random Evaluation");
            System.out.println("2. Leave One Out Evaluation");

            input = scanner.nextInt();

            switch (input)
            {
                case 1:
                    evaluation = new RandomEvaluation(manager, manager.getDataInstanceManager(), manager.getClassifier());
                    break;
                case 2:
                    evaluation = new LeaveOneOutEvaluation(manager, manager.getDataInstanceManager(), manager.getClassifier());
                    break;
                default:
                    input = 0;
                    break;
            }
        }
        manager.setEvaluation(evaluation);
        clearScreen();
    }

    /**
     * Sends the menu to the user
     */
    public void sendMenu()
    {


        //populateState();
        File file = populateFile();
        boolean isIdentifying = isIdentifying();

        clearScreen();

        final boolean debugMode = isDebugMode();

        clearScreen();
        MachineLearningManager machineLearningManager = new MachineLearningManager(file, isIdentifying, debugMode);

        populateSearch(machineLearningManager);

        populateClassifier(machineLearningManager);

        populateEvaluation(machineLearningManager);

        machineLearningManager.getTree().init();


        clearScreen();
        System.out.println("-");
        System.out.println("-");
        System.out.println("NOTE: Feature subsets are 0-based!");
        System.out.println("-");
        System.out.println("-");
        System.out.println();
        System.out.println("-");
        System.out.println("-");
        System.out.println("Find best feature subset:");
        System.out.println("-");
        System.out.println("-");

        long beforeSolution = System.currentTimeMillis();


        final FeatureSelectionTree.Node solution = machineLearningManager.getTree().findSolution();


        long afterSolution = System.currentTimeMillis();

        clearScreen();

        System.out.println("-");
        System.out.println("-");
        System.out.println("-- Time Taken --");
        System.out.println();
        System.out.println("Average Classifier Time: " +  machineLearningManager.toStringTime((long) machineLearningManager.getAverageClassifierTime()));
        System.out.println("Average Evaluation Time: " +  machineLearningManager.toStringTime((long) machineLearningManager.getAverageEvaluationTime()));
        System.out.println("Time to find best feature subset: " +  machineLearningManager.toStringTime(afterSolution-beforeSolution));
        System.out.println("Time to load datasets into memory (cleaning noise, adding to memory, mapping ids to instances, normalization, etc): " +  machineLearningManager.toStringTime(machineLearningManager.getLoadDataTime()));
        System.out.println();
        System.out.println("TOTAL TIME (time to find best feature subset + time to load dataset): " + (machineLearningManager.toStringTime(afterSolution-beforeSolution + machineLearningManager.getLoadDataTime())));
        System.out.println();
        System.out.println("Note: Does not account user input.");


        System.out.println("-");
        System.out.println("-");


        //todo show times and other steps?
        //time to load data
        //time to find solution in tree

        clearScreen();

        System.out.println("-");
        System.out.println("-");

        if (solution == null)
        {
            System.out.println("Error: Solution Not Found");
            System.out.println("-");
            System.out.println("-");
        } else
        {
            System.out.println("Solution: ");
            System.out.println("-");
            System.out.println("-");
            System.out.println(solution);
        }


        System.out.println();
        System.out.println("-");
        System.out.println("-");
        System.out.println("NOTE: Feature subsets are 0-based!");
        System.out.println("-");
        System.out.println("-");
        scanner.close();
    }


    public static void clearScreen()
    {
        for (int i = 0; i < 4; i++)
            System.out.println();
    }
}
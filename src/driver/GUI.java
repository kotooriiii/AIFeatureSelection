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
        while(true)
        {
            System.out.println("-- Type File Name --");
            System.out.println("File name: ");

            final String line = scanner.nextLine();
            try
            {
                File file = new File(line);
                if(!file.exists())
                    throw new NullPointerException();
              return file;
            } catch (NullPointerException e)
            {
                System.out.println("Error: Incorrect file name");
            }
        }
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
                    classifier = new KNNClassifier(1); //todo select K?
                    break;
                default:
                    input = 0;
                    break;
            }
        }
        manager.setClassifier(classifier);
        clearScreen();
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
            System.out.println("2. Leave One Out Evaluation (DO NOT CHOOSE THIS, WORK IN PROGRESS, THIS IS FOR PART 2)");

            input = scanner.nextInt();

            switch (input)
            {
                case 1:
                    evaluation = new RandomEvaluation(manager.getDataInstanceManager(), manager.getClassifier());
                    break;
                case 2:
                    evaluation = new LeaveOneOutEvaluation(manager.getDataInstanceManager(), manager.getClassifier());
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

        MachineLearningManager machineLearningManager = new MachineLearningManager(file);

        populateSearch(machineLearningManager);

        populateClassifier(machineLearningManager);

        populateEvaluation(machineLearningManager);

        machineLearningManager.getTree().init();


        clearScreen();

        final FeatureSelectionTree.Node solution = machineLearningManager.getTree().findSolution();


        if (solution == null)
        {
            System.out.println("Error: Solution Not Found");
        } else
        {
            System.out.println("Solution: ");
            System.out.println(solution);
        }

        scanner.close();
    }


    public static void clearScreen()
    {
        for (int i = 0; i < 7; i++)
            System.out.println();
    }
}
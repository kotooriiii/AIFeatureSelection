package driver;

import classifier.KNNClassifier;
import evaluation.LeaveOneOutEvaluation;
import evaluation.RandomEvaluation;
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

        for(int i = 1; i <= input; i++)
        {
            maxFeatures.add(i);
        }

        clearScreen();
    }

    private void populateClassifier(FeatureSelectionTree<Integer> tree)
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
                    tree.setClassifier(new KNNClassifier<Integer>());
                    break;
                default:
                    input = 0;
                    break;
            }
        }
        clearScreen();
    }

    private void populateSearch(FeatureSelectionTree<Integer> tree)
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
                    tree.setSearch(new ForwardSelectionSearch<>(tree));
                    break;
                case 2:
                    tree.setSearch(new BackwardSelectionSearch<>(tree));
                    break;
                default:
                    input = 0;
                    break;
            }
        }
        clearScreen();
    }

    private void populateEvaluation(FeatureSelectionTree<Integer> tree)
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
                    tree.setEvaluation(new RandomEvaluation<>());
                    break;
                case 2:
                    tree.setEvaluation(new LeaveOneOutEvaluation<>());
                    break;
                default:
                    input = 0;
                    break;
            }
        }
        clearScreen();
    }

    /**
     * Sends the menu to the user
     */
    public void sendMenu()
    {


        populateState();

        FeatureSelectionTree<Integer> featureSelectionTree = new FeatureSelectionTree<>(maxFeatures);

        featureSelectionTree.setDebug(true);

        populateClassifier(featureSelectionTree);
        populateSearch(featureSelectionTree);
        populateEvaluation(featureSelectionTree);

        System.out.println("Searching Solution\n");
        clearScreen();

        final FeatureSelectionTree<Integer>.Node solution = featureSelectionTree.findSolution();

        if (solution == null)
        {
            System.out.println("Error: Solution Not Found");
        } else
        {
            System.out.println();
            System.out.println();
            System.out.println(solution);
        }


        scanner.close();
    }


    public static void clearScreen()
    {
        for (int i = 0; i < 50; i++)
            System.out.println();
    }
}
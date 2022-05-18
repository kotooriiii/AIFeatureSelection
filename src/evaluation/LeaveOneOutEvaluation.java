package evaluation;

import classifier.AbstractClassifier;
import instance.DataInstance;
import instance.DataInstanceManager;
import tree.FeatureSelectionTree;

import java.util.*;

public class LeaveOneOutEvaluation extends AbstractEvaluation
{
    final int TESTING_SIZE = 1;

    public LeaveOneOutEvaluation(DataInstanceManager dataInstanceManager, AbstractClassifier classifier)
    {
        super(dataInstanceManager, classifier);
    }

    @Override
    public double getAccuracy(FeatureSelectionTree.Node node)
    {
        if (node.isSavedCostSet())
        {
            return node.getSavedCost();
        } else
        {
            node.setSavedCost(getCost(node));
            return node.getSavedCost();
        }
    }

    private double getCost(FeatureSelectionTree.Node node)
    {
        double sum = 0;
        HashSet<Integer> currentTrainingIndecesSet = new HashSet<>();
        //init
        for (int i = 0; i < TESTING_SIZE; i++)
            currentTrainingIndecesSet.add(i);


        int times = (int) Math.ceil((double) dataInstanceManager.getDataInstances().size() / TESTING_SIZE);

        for (int i = 0; i < times; i++)
        {
            final double localAccuracy = getLocalAccuracy(node.getFeatures(), currentTrainingIndecesSet);
            sum += localAccuracy;
        }

        return sum / times;
    }

    private double getLocalAccuracy(Set<Integer> features, HashSet<Integer> currentTrainingIndecesSet)
    {
        ArrayList<DataInstance> testingList = new ArrayList<>();
        ArrayList<DataInstance> trainingList = new ArrayList<>();


        for (int i = 0; i < dataInstanceManager.getDataInstances().size(); i++)
        {
            if (currentTrainingIndecesSet.contains(i))
            {
                testingList.add(dataInstanceManager.getDataInstances().get(i));
            } else
            {
                trainingList.add(dataInstanceManager.getDataInstances().get(i));

            }
        }

        super.classifier.train(features, trainingList);

        //calc accuracy
        double accuracy = 0;
        int correct = 0;
        final int total = testingList.size();
        for (DataInstance testInstance : testingList)
        {
            final Integer classification = super.classifier.test(testInstance);


            if (classification.equals(testInstance.getClassData()))
            {
                correct++;
            }

        }

        //get accuracy
        accuracy = (double) correct / total;

        //update next indeces
        HashSet<Integer> nextTrainingIndecesSet = new HashSet<>();
        for (Integer indeces : currentTrainingIndecesSet)
        {
            Integer next = indeces + 1;
            if (next >= dataInstanceManager.getDataInstances().size())
                continue;
            nextTrainingIndecesSet.add(next);
        }
        currentTrainingIndecesSet.clear();
        currentTrainingIndecesSet.addAll(nextTrainingIndecesSet);


        //return it
        return accuracy;
    }

}

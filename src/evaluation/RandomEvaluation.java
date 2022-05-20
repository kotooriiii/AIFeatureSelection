package evaluation;

import classifier.AbstractClassifier;
import driver.MachineLearningManager;
import instance.DataInstanceManager;
import tree.FeatureSelectionTree;

public class RandomEvaluation extends AbstractEvaluation
{
    public RandomEvaluation(MachineLearningManager machineLearningManager, DataInstanceManager dataInstanceManager, AbstractClassifier classifier)
    {
        super(machineLearningManager, dataInstanceManager, classifier);
    }

    @Override
    public double getAccuracy(FeatureSelectionTree.Node node)
    {
        final long beforeEval = System.currentTimeMillis();

        machineLearningManager.getClassifierAverageTimeList().add(0L);


        if (node.isSavedCostSet())
        {
            final long afterEval = System.currentTimeMillis();

            machineLearningManager.getEvaluationAverageTimeList().add(afterEval - beforeEval);
            if(machineLearningManager.isDebug())
            {
                System.out.println("RandomEvaluation: Time to retrieve (saved) average accuracy for subset feature(s) {" + node.getFeatures() + "}: " + machineLearningManager.toStringTime(afterEval-beforeEval) + ".");
            }
            return node.getSavedCost();
        } else
        {
            node.setSavedCost(Math.random());
            final long afterEval = System.currentTimeMillis();
            machineLearningManager.getEvaluationAverageTimeList().add(afterEval - beforeEval);
            if(machineLearningManager.isDebug())
            {
                System.out.println("RandomEvaluation: Time to retrieve (not saved) average accuracy for subset feature(s) {" + node.getFeatures() + "}: " + machineLearningManager.toStringTime(afterEval-beforeEval) + ".");
            }
            return node.getSavedCost();
        }
    }


}

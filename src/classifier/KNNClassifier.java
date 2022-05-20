package classifier;

import driver.MachineLearningManager;
import instance.DataInstance;
import instance.DataInstanceManager;
import tree.FeatureSelectionTree;

import java.util.*;

public class KNNClassifier extends AbstractClassifier
{
    private List<DataInstance> trainingList;

    private int k;

    public KNNClassifier(MachineLearningManager machineLearningManager, int k)
    {
        super(machineLearningManager);
        trainingList = new ArrayList<>();
        this.k = k;
    }

    @Override
    public void train(Set<Integer> featureSubset, List<DataInstance> instances)
    {
        this.currentFeatures = featureSubset;

        if (manager.isDebug())
        {
            System.out.println("KNNClassifier: Training with feature(s) {" + featureSubset + "}");
            System.out.println("KNNClassifier: Training with instance(s) {" + instances + "}");
        }

        trainingList.clear();
        trainingList.addAll(instances);
    }

    @Override
    public Integer test(DataInstance instance)
    {
        PriorityQueue<DataInstance> queue = new PriorityQueue<>((o1, o2) ->
        {
            double valueA = 0;
            for (Integer feature : currentFeatures)
            {
                valueA += Math.pow(o1.getFeatureData(feature) - instance.getFeatureData(feature), 2);
            }
            valueA = Math.sqrt(valueA);


            double valueB = 0;
            for (Integer feature : currentFeatures)
            {
                valueB += Math.pow(o2.getFeatureData(feature) - instance.getFeatureData(feature), 2);
            }
            valueB = Math.sqrt(valueB);


            if (valueA > valueB)
            {
                return 1;
            } else if (valueA == valueB)
            {
                return 0;
            } else if (valueA < valueB)
            {
                return -1;
            }

            return 0;
        });

        queue.addAll(trainingList);
        Integer classId = getFrequentK(queue);

        if (manager.isDebug())
            System.out.println("KNNClassifier: Testing found most frequent K class/group for instance #" + instance.toString() + " with K=" + k + " is class id: " + classId + ".");

        return classId;
    }

    private Integer getFrequentK(PriorityQueue<DataInstance> queue)
    {
        HashMap<Integer, Integer> map = new HashMap<>();


        int limit = k;

        if (queue.size() < k)
        {
            limit = queue.size();
        }

        for (int i = 0; i < limit; i++)
        {
            final DataInstance poll = queue.poll();

            assert poll != null;
            Integer timesAppeared = map.get(poll.getClassData());

            if (timesAppeared == null)
            {
                map.put(poll.getClassData(), 1);
            } else
            {
                map.put(poll.getClassData(), timesAppeared + 1);

            }
        }

        PriorityQueue<Map.Entry<Integer, Integer>> priorityQueue = new PriorityQueue<>(new Comparator<Map.Entry<Integer, Integer>>()
        {
            @Override
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2)
            {
                if (o1.getValue() > o2.getValue())
                {
                    return -1;
                } else if (o1.getValue() == o2.getValue())
                {
                    return 0;
                } else if (o1.getValue() < o2.getValue())
                {
                    return 1;
                }

                return 0;
            }
        });

        priorityQueue.addAll(map.entrySet());

        return Objects.requireNonNull(priorityQueue.poll()).getKey();
    }
}

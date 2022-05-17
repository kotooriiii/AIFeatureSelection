package classifier;

import instance.DataInstance;
import instance.DataInstanceManager;
import tree.FeatureSelectionTree;

import java.util.*;

public class KNNClassifier extends AbstractClassifier
{
    private List<DataInstance> trainingList;
    private int k;

    public KNNClassifier(int k, DataInstanceManager manager, FeatureSelectionTree features)
    {
        super(manager, features);
        trainingList = new ArrayList<>();
        this.k = k;
    }

    @Override
    public void train()
    {
        final Iterator<DataInstance> iterator = manager.iterator();
        while (iterator.hasNext())
        {
            final DataInstance next = iterator.next();
            trainingList.add(next);
        }
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
        instance.setClassId(classId);
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

        return priorityQueue.poll().getKey();
    }
}

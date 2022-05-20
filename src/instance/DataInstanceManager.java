package instance;

import driver.MachineLearningManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DataInstanceManager
{
    private List<DataInstance> dataInstances;
    private DataTable dataTable;

    private Set<Integer> maxFeatures;

    private MachineLearningManager manager;


    public DataInstanceManager(MachineLearningManager manager)
    {
        this.dataInstances = new ArrayList<>();
        this.dataTable = new DataTable();
        this.maxFeatures = new HashSet<>();
        this.manager = manager;

    }

    public void load(File input, boolean isFirstLineIdentifier)
    {

        if(manager.isDebug())
        System.out.println("DataInstanceManager: Loading dataset...");
        int lineCounter = 0;

        final long beforeLoad = System.currentTimeMillis();

        ArrayList<String> names = new ArrayList<>();
        try
        {
            Scanner fileScanner = new Scanner(input);
            boolean isFirst = true;
            int instanceId = 0;
            while (fileScanner.hasNextLine())
            {
                lineCounter++;

                DataInstance instance = new DataInstance(instanceId++);

                final String tuple = fileScanner.nextLine();

                if (tuple.isEmpty())
                    continue;

                String[] tupleSplit = tuple.split("(  )|( -)");

                tupleSplit = getBetterSplit(tupleSplit);

                for (int i = 0; i < tupleSplit.length; i++)
                {
                    String data = tupleSplit[i];

                    if (data.isEmpty() || data.equals("null"))
                        continue;

                    if (i == 0)
                    {
                        instance.setClassId(getDataTable().addClass(data));
                    } else
                    {

                        if (isFirst)
                        {
                            if (isFirstLineIdentifier)
                            {
                                names.add(data);
                            } else
                            {
                                names.add("" + i);
                            }
                        }
                        instance.putFeatureData(getDataTable().addFeature(names.get(i - 1)), Double.valueOf(data));
                    }
                }

                if (isFirst)
                {
                    isFirst = false;

                    if (isFirstLineIdentifier)
                        continue;
                }
                add(instance);
            }

        } catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }

        //all data is loaded. time to normalize
        normalize();

        final long afterLoad = System.currentTimeMillis();

        manager.setLoadDataTime(afterLoad-beforeLoad);
        if(manager.isDebug())
            System.out.println("DataInstanceManager: Completed loading dataset.");


    }

    public Set<Integer> getMaxFeatures()
    {
        return maxFeatures;
    }

    public DataTable getDataTable()
    {
        return dataTable;
    }

    public boolean add(DataInstance dataInstance)
    {
        maxFeatures.addAll(dataInstance.getFeaturesId().keySet());

        return this.dataInstances.add(dataInstance);
    }

    public Iterator<DataInstance> iterator()
    {
        return this.dataInstances.iterator();
    }

    private String[] getBetterSplit(String[] split)
    {
        String[] s = new String[split.length - 1];
        for (int i = 1; i < split.length; i++)
        {
            s[i - 1] = split[i];
        }
        return s;
    }

    private void normalize()
    {
        for (Integer featureId : maxFeatures)
        {

            double sum = 0;
            double mean = 0;
            double standardDeviation;
            int count = 0;


            //get mean to later fill in missing values
            for (DataInstance instance : dataInstances)
            {
                final Double featureData = instance.getFeatureData(featureId);
                if (featureData == null)
                    continue;

                count++; //todo do we count the value if its null anyways? maybe if we want to count it move it above featureData. for assignment, we can assume that all instances have full features
                sum += featureData;
            }

            mean = sum / count;

            //fill in missing/null values with mean
            for (DataInstance instance : dataInstances)
            {
                final Double featureData = instance.getFeatureData(featureId);
                if (featureData == null)
                {
                    instance.putFeatureData(featureId, mean);
                }
            }

            sum = 0;
            double squaredDataSum = 0;
            mean = 0;
            double squaredDataMean = 0;
            count = 0;

            //remeasure the mean and count. get the squared sum for standard deviation
            for (DataInstance instance : dataInstances)
            {
                //should no longer be null!
                final Double featureData = instance.getFeatureData(featureId);
                count++;
                sum += featureData;
                squaredDataSum += Math.pow(featureData, 2);
            }

            mean = sum / count;
            squaredDataMean = squaredDataSum / count;


            standardDeviation = Math.sqrt(squaredDataMean - Math.pow(mean, 2));

            //normalize
            for (DataInstance instance : dataInstances)
            {
                instance.putFeatureData(featureId, (instance.getFeatureData(featureId) - mean) / standardDeviation);
            }
        }
    }

    public List<DataInstance> getDataInstances()
    {
        return dataInstances;
    }
}

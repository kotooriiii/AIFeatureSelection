package instance;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DataInstanceManager
{
    private List<DataInstance> dataInstances;
    private DataTable dataTable;

    private Set<Integer> maxFeatures;


    public DataInstanceManager()
    {
        this.dataInstances = new ArrayList<>();
        this.dataTable = new DataTable();
        this.maxFeatures = new HashSet<>();

    }

    public void load(File input)
    {
        boolean isFirstLineIdentifier = false; //todo find a way to make this an option
        int lineCounter = 0;

        ArrayList<String> names = new ArrayList<>();
        try
        {
            Scanner fileScanner = new Scanner(input);
            boolean isFirst = true;
            while (fileScanner.hasNextLine())
            {
                lineCounter++;

                DataInstance instance = new DataInstance();

                final String tuple = fileScanner.nextLine();

                if(tuple.isEmpty())
                    continue;

                String[] tupleSplit = tuple.split("(  )|( -)");

                tupleSplit = getBetterSplit(tupleSplit);

                for (int i = 0; i < tupleSplit.length; i++)
                {
                    String data = tupleSplit[i];

                    if (i == 0)
                    {
                        instance.setClassId(getDataTable().addClass(data));
                    }
                    else
                    {

                        if (isFirst)
                        {
                            if(isFirstLineIdentifier)
                            {
                                names.add(data);
                            }
                            else
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
        String[] s = new String[split.length-1];
        for(int i = 1; i < split.length; i++)
        {
            s[i-1] = split[i];
        }
        return s;
    }


}

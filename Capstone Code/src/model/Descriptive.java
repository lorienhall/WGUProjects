package model;

import javafx.scene.chart.XYChart;

import java.util.*;

/** This class is for the descriptive algorithm. */
public class Descriptive
{
    // Initializing a Dictionary
    private static Dictionary<List<Integer>, Integer> RageFlightDist = new Hashtable();
    private static Dictionary<Integer, List<Integer>> ageFlightDist = new Hashtable();

    /** This method reverses the dictionary.*/
    public static void reverseDict()
    {
        Iterator<List<Integer>> keyIter = RageFlightDist.keys().asIterator();

        while (keyIter.hasNext())
        {
            List<Integer> key = keyIter.next();
            Integer value = RageFlightDist.get(key);
            ageFlightDist.put(value, key);
        }
    }

    /** This method gets the data for the scatter chart. */
    public static XYChart.Series getScatterDat(XYChart.Series set1)
    {
        Enumeration<List<Integer>> it =  RageFlightDist.keys();
        ListIterator<List<Integer>> keyIter = new ArrayList<>(Collections.list(it)).listIterator();
        Dictionary<Integer, List<Integer>> age_num_dist = new Hashtable();

        // get the flight distance and number of reviews for each age
        for (int age = 7; age < 86; age++)
        {
            int i = 0;
            int dist = 0;
            // if the age is odd then go forwards
            if ((age % 2) != 0)
            {
                while (keyIter.hasNext())
                {
                    if (keyIter.next().get(0) == age)
                    {
                        i++;
                        dist += keyIter.next().get(1);
                    }
                }
            }
            // if the age is an even number go backwards across the list
            else if ((age % 2) == 0)
            {
                while (keyIter.hasPrevious())
                {
                    if (age == 62)
                    {
                        break;
                    }
                    if (keyIter.previous().get(0) == age)
                    {
                        i++;
                        dist += keyIter.previous().get(1);
                    }
                }
            }
            List<Integer> num_dist = List.of(i, dist);
            age_num_dist.put(age,num_dist);
        }

        // get the averages
        Iterator<Integer> keyIte = age_num_dist.keys().asIterator();
        while (keyIte.hasNext())
        {
            Integer key = keyIte.next();
            List<Integer> value = age_num_dist.get(key);
            int i = value.get(0);
            int dist = value.get(1);
            if (i != 0)
            {
                int average = dist / i;
                set1.getData().add(new XYChart.Data(key.toString(), average));
            }
        }
       return set1;
    }

    /** This method adds data to the reverse dictionary. */
    public static void addRDict(List<Integer> key, int value ) { RageFlightDist.put(key,value); }


}

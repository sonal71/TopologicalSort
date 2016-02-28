import java.io.*;
import java.util.*;

public class TS {
    
    public static void main(String[] args) throws FileNotFoundException{
        final long startTime = System.nanoTime();
        Scanner reader = new Scanner(new FileInputStream(args[0])); 
        String s = reader.nextLine(); 
        int vertexCount = Integer.parseInt(s), i=0;
        int[] indegree = new int[vertexCount]; //array storing indegrees of all vertices
        ArrayList<ArrayList<Integer>> g = new ArrayList<ArrayList<Integer>>(); //Graph 
        String[] str;
        while(vertexCount != 0) {
            str =  reader.nextLine().split(" ");
            g.add(i, new ArrayList<Integer>());
            for (int j = 0; j < str.length; j++) {
                if(isNumeric(str[j])){
                    g.get(i).add(Integer.parseInt(str[j]));
                    indegree[Integer.parseInt(str[j])]++;
                }
            }     
            i++;
            vertexCount--;
        }
        ArrayList<ArrayList<Integer>> allTopOrders = new ArrayList<ArrayList<Integer>>(topSort(g, indegree));
        for(ArrayList<Integer> order : allTopOrders){
            System.out.println(order);
        }
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Time: "+ totalTime);
    }
    
    /* Function to initialize variables */
    public static ArrayList<ArrayList<Integer>> topSort(ArrayList<ArrayList<Integer>> g, int[] indegree) {
        Queue<Integer> l = new LinkedList<Integer>(); // L, set of vertices with indegree zero
        ArrayList<ArrayList<Integer>> allTop = new ArrayList<ArrayList<Integer>>(); // array storing all topological orders
        ArrayList<Integer> partialTop = new ArrayList<Integer>(); //array storing partial topological order
        int index = 0, count = 0;
        
        for (int integer : indegree) {
            if(integer == 0) { l.add(index); }
            index++;
        }
        count = topSortAll(g, indegree, l, partialTop, allTop, count);
        return display(count, allTop, indegree);
    }
    
    /* Recursive function generating all possiblities of Topological Sorting*/
    public static int topSortAll(ArrayList<ArrayList<Integer>> g, int[] indegree, Queue<Integer> l, ArrayList<Integer> partialTop, ArrayList<ArrayList<Integer>> allTop, int count) {
        int currentVertex; 
        if(l.isEmpty()){
            allTop.add(new ArrayList<Integer>());  
            allTop.get(count).addAll(partialTop);
            partialTop.clear();
            count++;
        } else {
            for(int i=0; i < l.size(); i++){
                currentVertex = l.poll(); 
                partialTop.add(currentVertex);
                ArrayList<Integer> subGraph = g.get(currentVertex);
                for (Integer subGraphVertex : subGraph) {
                    indegree[subGraphVertex]--;
                    if(indegree[subGraphVertex] == 0) {
                        l.add(subGraphVertex);
                    }
                }
                count = topSortAll(g, indegree, l, partialTop, allTop, count);
                l.add(currentVertex);
                for (Integer subGraphVertex : subGraph) {
                    l.remove(subGraphVertex);
                    indegree[subGraphVertex]++;
                }
            }
        }
        return count;
    }
    
    /* Displaying all topological order  */
    public static ArrayList<ArrayList<Integer>> display(int count, ArrayList<ArrayList<Integer>> allTop, int[] indegree){
        for(int i = 1; i<count; i++){
            int tail = allTop.get(i).size();
            ArrayList<Integer> previousOrder = new ArrayList<Integer>(allTop.get((i-1))); 
            ArrayList<Integer> currentOrder = new ArrayList<Integer>();
            for(int j=1; j<=tail; j++){
                previousOrder.remove(indegree.length - j);
            }
            currentOrder.addAll(previousOrder);
            currentOrder.addAll(allTop.get((i)));
            allTop.get(i).clear();
            allTop.get(i).addAll(currentOrder);
        }
       
        System.out.println("Total number or outcomes: "+count);
        return allTop;
    }
    
    public static boolean isNumeric(String input) {
        try {
          Integer.parseInt(input);
          return true;
        } catch (NumberFormatException e) {
          return false;
        }
    }
    
}

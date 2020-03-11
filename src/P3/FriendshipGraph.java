package P3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
 * HIT Software Construction Lab1 FriendshipGraph
 * 
 * Graph is implemented by using adjacency list.
 * 
 * @author 1150810613-liqiuhao
 * 
 * @version V1.0
 */
public class FriendshipGraph {

    private final Map<Person, Set<Person>> adjacencyList;

    public FriendshipGraph() {
        adjacencyList = new HashMap<>();
    }

    /*
     * Add new Vertex(person) into the graph.
     * If the person already exists in the graph,
     * output the error message and exit program.
     * 
     * @param the person to be added
     * 
     * @return the person has been added
     */
    public Person addVertex(Person person) {
        for (Person testPerson : adjacencyList.keySet()) {
            if (testPerson.getName().equals(person.getName())) {
                System.out.println("Failed to add person " + person.getName() + ":");
                System.out.println("Already exist!");
                System.exit(1);
            }
        }
        adjacencyList.put(person, new HashSet<>());
        return person;
    }

    /*
     * Add edge(directed) from one person to another.
     * 
     * @param two persons to be connected
     * 
     * @return true if success, otherwise false
     */
    public boolean addEdge(Person fromPerson, Person toPerson) {
        return adjacencyList.get(fromPerson).add(toPerson);
    }

    /*
     * Find the shortest length between two person
     * Using BFS
     * 
     * @param two persons
     * the person should exist in the graph
     * 
     * @return the shortest length between two person
     * -1 if there is not path between them
     */
    public int getDistance(Person fromPerson, Person toPerson) {
        int distance = 1;
        // The vertexes we have visited
        Set<Person> alreayWalk = new HashSet<>();
        // The vertexes we are going to visit
        Set<Person> readyToWalk = new HashSet<>(adjacencyList.get(fromPerson));
         
        if (fromPerson == toPerson) {
            return 0;
        }

        while (true) {
        	
        	if (readyToWalk.isEmpty()) { 
                 return -1;
             }
           
            if (readyToWalk.contains(toPerson)) {
                return distance;
            }

            alreayWalk.addAll(readyToWalk);
            ++distance;
            // delete the vertexes we have visited
            // and add new vertexes connect to them
            
            Set<Person> toDelete = new HashSet<>(readyToWalk);
            for (Person delete : readyToWalk) {
                Set<Person> toAddPersons = adjacencyList.get(delete);
                for (Person toAddPerson : toAddPersons) {
                    // WE SHOULD'T VISIT THE VERTEXES WHICH HAVE BEEN VISTED!(circle)
                    if (!alreayWalk.contains(toAddPerson)) {
                        readyToWalk.add(toAddPerson);
                    }
                }
            }
            readyToWalk.removeAll(toDelete);
        }
    }

    /*
     * The main method which add five person and
     * six edge(directed). Then test six distances.
     * 
     * @param the command line strings
     * 
     * @return void
     */
    public static void main(String[] args) {

        FriendshipGraph graph = new FriendshipGraph();
        Person rachel = new Person("rachel");
        Person ross = new Person("ross");
        Person ben = new Person("ben");
        Person kramer = new Person("kramer");
        graph.addVertex(rachel);
        graph.addVertex(ross);
        graph.addVertex(ben);
        graph.addVertex(kramer);
        graph.addEdge(rachel, ross);
        graph.addEdge(ross, rachel);
        graph.addEdge(ross, ben);
        graph.addEdge(ben, ross);

        System.out.println(graph.getDistance(rachel, ross));
        System.out.println(graph.getDistance(rachel, ben));
        System.out.println(graph.getDistance(rachel, rachel));
        System.out.println(graph.getDistance(rachel, kramer));

        // additional samples

        System.out.println("-----additional samples:");

        graph.addEdge(ben, kramer);
        graph.addEdge(kramer, ross);

        System.out.println(graph.getDistance(rachel, kramer));
        System.out.println(graph.getDistance(kramer, rachel));

    }

}

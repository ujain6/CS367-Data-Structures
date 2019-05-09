import java.util.ArrayList;
import java.util.List;

/**
 * Add test methods and call them here to facilitate testing 
 * the code you add.
 */

public class Test {

    /**
     * Call the test methods you've written.
     * @param args UNUSED
     */
    public static void main(String[] args) {
        test_SortList();
        test_BFS_vs_DFS();
    }

    /**
     * Create some nodes in random order, store them, 
     * display the list, sort the list, display
     */
    public static void test_SortList() {

        List<GraphNode> nlist = new ArrayList<GraphNode>();
        for ( int i=0; i < 50; i++ ) {
            nlist.add(new GraphNode( "" + (char)('A' + (int)(Math.random()*26)) ));
        }
        System.out.println(nlist);
        nlist.sort(null);
        System.out.println(nlist);

    }
    
    /** 
     * Test that DFS and BFS give expected results for a graph we know.
     * This is the undirected graph example from lecture on Thursday 4/14/16
     *
     * <pre>
     * NODES
     * A 
     * B 
     * C 
     * D 
     * E 
     * F 
     * G 
     * H 
     * I
     * EDGES
     * A B 1 
     * A D 1 
     * B C 1 
     * C D 1 
     * C E 1 
     * C F 1 
     * D G 1 
     * D F 1 
     * E F 1 
     * E H 1 
     * G H 1 
     * H I 1</pre> 
     * 
     * <p>This tests traversals on undirected graph given in class on Thursday 4/14</p>
     * 
     * <pre>DFS(a,i) should be:
     * dfs = a[--1--&gt; b, --1--&gt; c, --1--&gt; d, --1--&gt; f, --1--&gt; e, --1--&gt; h, --1--&gt; i]
     * 
     * BFS(a,i) should be:
     * bfs = a[--1--&gt; d, --1--&gt; g, --1--&gt; h, --1--&gt; i]
     * </pre>
     *
     * <p>You can try other traversals on this graph or any that you configure in a 
     * similar way.</p>
     */
    public static void test_BFS_vs_DFS() {
        String areaFN = "test_area.txt";
        try {
            SpyGraph g = Game.createGraphFromAreaFile(areaFN);
            System.out.println("SpyGraph created");
            String start = "a";
            String end = "i";
            List<Neighbor> dfs = g.DFS(start,end); // should be A,B,C,E,F
            List<Neighbor> bfs = g.BFS(start,end); // should by A,D,F
            System.out.println("dfs = " + start + dfs);
            System.out.println("bfs = " + start + bfs);
            
        } catch (InvalidAreaFileException e) {
            System.out.println(areaFN + " not found. Make sure you have copied the "
                    + " file from the assigment files/input_files directory.");
        }
        
    }

}

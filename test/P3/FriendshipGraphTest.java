package P3;

import static org.junit.Assert.*;

import org.junit.Test;

public class FriendshipGraphTest {


	@Test
	public void testAddVertex() {
		FriendshipGraph graph = new FriendshipGraph();

		Person rachel = new Person("rachel");
		Person ross = new Person("ross");
		Person ben = new Person("ben");
		Person kramer = new Person("kramer");

		assertEquals(rachel, graph.addVertex(rachel));
		assertEquals(ross, graph.addVertex(ross));
		assertEquals(ben, graph.addVertex(ben));
		assertEquals(kramer, graph.addVertex(kramer));
	}

	@Test
	public void testAddEdge() {
		FriendshipGraph graph = new FriendshipGraph();

		Person rachel = new Person("rachel");
		Person ross = new Person("ross");
		Person ben = new Person("ben");
		Person kramer = new Person("kramer");

		graph.addVertex(rachel);
		graph.addVertex(ross);
		graph.addVertex(ben);
		graph.addVertex(kramer);

		assertTrue(graph.addEdge(rachel, ross));
		assertTrue(graph.addEdge(ross, rachel));
		assertTrue(graph.addEdge(ross, ben));
		assertTrue(graph.addEdge(ben, ross));
		assertTrue(graph.addEdge(ben, kramer));
		assertTrue(graph.addEdge(kramer, rachel));
	}

	@Test
	public void testGetDistance() {
		FriendshipGraph graph = new FriendshipGraph();

		Person rachel = new Person("rachel");
		Person ross = new Person("ross");
		Person ben = new Person("ben");
		Person kramer = new Person("kramer");
		Person frank = new Person("frank");

		graph.addVertex(rachel);
		graph.addVertex(ross);
		graph.addVertex(ben);
		graph.addVertex(kramer);
		graph.addVertex(frank);

		graph.addEdge(rachel, ross);
		graph.addEdge(ross, rachel);
		graph.addEdge(ross, ben);
		graph.addEdge(ben, ross);
		graph.addEdge(kramer, ben);
		graph.addEdge(ben, kramer);
		graph.addEdge(kramer, ross);
		graph.addEdge(ross, kramer);
		graph.addEdge(kramer, frank);
		graph.addEdge(frank, kramer);
		graph.addEdge(ross, frank);
		graph.addEdge(frank, ross);

		assertEquals(2, graph.getDistance(rachel, kramer));
		assertEquals(1, graph.getDistance(ross, kramer));
		assertEquals(1, graph.getDistance(ross, frank));
		assertEquals(2, graph.getDistance(ben, frank));

	}

}

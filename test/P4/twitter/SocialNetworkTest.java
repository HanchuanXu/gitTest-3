/*
 * Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course
 * staff.
 */
package P4.twitter;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import P4.twitter.SocialNetwork;
import P4.twitter.Tweet;

/*
 * Testing strategy
 *
 * Just be careful about two things:
 * 
 * 1. Be careful that people who does't follow anyone
 * can have a empty set or not even exist in set.
 * 
 * 2. Be careful that postcondition doesn't define
 * when two person have the same influence
 */
public class SocialNetworkTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment
     * looks like.
     * Make sure you have partitions.
     */
    private static final Instant d11 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d12 = Instant.parse("2016-02-17T10:30:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d31 = Instant.parse("2016-02-17T11:30:00Z");
    private static final Instant d32 = Instant.parse("2016-02-17T12:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d11);
    private static final Tweet tweet2 = new Tweet(2, "frank_8", "Never say nerver@alyssa", d11);
    private static final Tweet tweet3 = new Tweet(3, "i-tayler", "@frank_8 keep life simple", d12);
    private static final Tweet tweet4 = new Tweet(4, "alyssa", "rivest @i-tayler talk in @i-tayler 30 minutes #hype",
            d2);
    private static final Tweet tweet5 = new Tweet(5, "ben", "Who's your bbitdiddle@hit.edu.cn daddy?", d31);
    private static final Tweet tweet6 = new Tweet(6, "bob",
            "ben@hit.edu.cn php is the best language in the @@@ben world! @@@ben", d32);
    private static final Tweet tweet7 = new Tweet(7, "alyssa", "Happy @ben hacking!", d32);

    @Test(expected = AssertionError.class) public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test public void testGuessFollowsGraph() {
        Map<String, Set<String>> followsGraph = SocialNetwork
                .guessFollowsGraph(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5, tweet6, tweet7));

        Set<String> peopleFollowOthers = new HashSet<>();
        peopleFollowOthers.add("alyssa");
        peopleFollowOthers.add("i-tayler");
        peopleFollowOthers.add("bob");
        Set<String> peopleDontFollowOthers = new HashSet<>();
        peopleDontFollowOthers.add("frank_8");
        peopleDontFollowOthers.add("ben");

        for (String name : followsGraph.keySet()) {
            if (peopleFollowOthers.contains(name)) {
                if (name.equals("alyssa")) {
                    Set<String> alyssa = new HashSet<>();
                    alyssa.add("ben");
                    alyssa.add("i-tayler");
                    assertTrue(followsGraph.get(name).equals(new HashSet<String>(alyssa)));
                } else if (name.equals("i-tayler")) {
                    Set<String> tayler = new HashSet<>();
                    tayler.add("frank_8");
                    assertTrue(followsGraph.get(name).equals(new HashSet<String>(tayler)));
                } else {
                    Set<String> bob = new HashSet<>();
                    bob.add("ben");
                    assertTrue(followsGraph.get(name).equals(new HashSet<String>(bob)));
                }
            } else if (peopleDontFollowOthers.contains(name)) {
                // Be careful that people who does't follow anyone
                // can have a emptyset or not even exist in set.
                assertTrue(followsGraph.get(name).isEmpty());
            } else {
                fail("unknow name");
            }
        }
    }

    @Test public void testInfluencers() {
        Map<String, Set<String>> followsGraph = SocialNetwork
                .guessFollowsGraph(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5, tweet6, tweet7));

        List<String> influencers = SocialNetwork.influencers(followsGraph);

        // Be careful that postcondition doesn't define
        // when two person have the same influence
        List<String> possibleResult1 = new ArrayList<>();
        possibleResult1.add("ben");
        possibleResult1.add("frank_8");
        possibleResult1.add("i-tayler");
        List<String> possibleResult2 = new ArrayList<>();
        possibleResult2.add("ben");
        possibleResult2.add("i-tayler");
        possibleResult2.add("frank_8");

        assertTrue(influencers.equals(possibleResult1) || influencers.equals(possibleResult2));
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */

}

/*
 * Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course
 * staff.
 */
package P4.twitter;

import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import P4.twitter.Filter;
import P4.twitter.Timespan;
import P4.twitter.Tweet;

/*
 * Testing strategy
 *
 * Partition the inputs as follows:
 * whether time's boarder equals
 * whether people's name mentioned multiple times
 * whether uppercase
 * 
 * cover ever part
 */
public class FilterTest {

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
    private static final Tweet tweet4 = new Tweet(4, "alyssa", "rivest @i-tayler talk in 30 minutes #hype", d2);
    private static final Tweet tweet5 = new Tweet(5, "ben", "Who's your bbitdiddle@hit.edu.cn daddy?", d31);
    private static final Tweet tweet6 = new Tweet(6, "bob",
            "ben@hit.edu.cn php is the best language in the @@@ben world! @@@ben", d32);
    private static final Tweet tweet7 = new Tweet(7, "alyssa", "Happy hacking!@bob!", d32);

    @Test(expected = AssertionError.class) 
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test 
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5, tweet6, tweet7),
                "alyssa");

        assertTrue(writtenBy.equals(Arrays.asList(tweet1, tweet4, tweet7)));
    }

    @Test 
    public void testInTimespanMultipleTweetsMultipleResults() {
        Instant testStart = Instant.parse("2016-02-17T10:30:00Z");
        Instant testEnd = Instant.parse("2016-02-17T11:30:00Z");

        List<Tweet> inTimespan = Filter.inTimespan(
                Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5, tweet6, tweet7),
                new Timespan(testStart, testEnd));

        assertTrue(inTimespan.equals(Arrays.asList(tweet3, tweet4, tweet5)));
    }

    @Test 
    public void testContaining() {
        List<Tweet> containing = Filter.containing(
                Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5, tweet6, tweet7),
                // According to the spec, upper or lowercase is not a postcondition
                Arrays.asList("Talk", "about", "HACKING", "neVer"));

        assertTrue(containing.equals(Arrays.asList(tweet1, tweet2, tweet4)));
    }

    /*
     * Warning: all the tests you write here must be runnable against any Filter
     * class that follows the spec. It will be run against several staff
     * implementations of Filter, which will be done by overwriting
     * (temporarily) your version of Filter with the staff's version.
     * DO NOT strengthen the spec of Filter or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Filter, because that means you're testing a stronger
     * spec than Filter says. If you need such helper methods, define them in a
     * different class. If you only need them in this test class, then keep them
     * in this test class.
     */

}

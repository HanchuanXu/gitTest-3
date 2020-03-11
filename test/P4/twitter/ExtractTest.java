package P4.twitter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import P4.twitter.Extract;
import P4.twitter.Timespan;
import P4.twitter.Tweet;

/*
 * Testing strategy
 *
 * GetTimespan(Cover each part):
 * Partition the inputs as follows:
 * a(b)...c
 * a.b....c
 * a...b...c
 * a....b.c
 * a.....c(b)
 * 
 * GetMentionedUsers(Cover each part):
 * 
 * For user name:
 * A-Z a-z - _
 * 
 * For @-mention:
 * 
 * @ is the begin character; mutipul @; @ preceded by
 * usernameCharacter; @balabala is the end of text
 * 
 * 
 */
public class ExtractTest {

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
    private static final Tweet tweet4 = new Tweet(4, "bbitdiddle", "rivest @i-tayler talk in 30 minutes #hype", d2);
    private static final Tweet tweet5 = new Tweet(5, "ben", "Who's your bbitdiddle@hit.edu.cn daddy?", d31);
    private static final Tweet tweet6 = new Tweet(6, "bob",
            "ben@hit.edu.cn php is the best language in the @@@ben world! @@@ben", d32);
    private static final Tweet tweet7 = new Tweet(7, "kim", "Happy hacking!@bob!", d32);

    @Test(expected = AssertionError.class) 
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    /*
     * This five tests cover:
     * * a(b)...c
     * a.b....c
     * a...b...c
     * a....b.c
     * a.....c(b)
     */
    @Test 
    public void testGetTimespan1() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet7));

        assertEquals("expected start", d11, timespan.getStart());
        assertEquals("expected end", d11, timespan.getEnd());
    }

    @Test public void testGetTimespan2() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet3, tweet7));

        assertEquals("expected start", d11, timespan.getStart());
        assertEquals("expected end", d12, timespan.getEnd());
    }

    @Test public void testGetTimespan3() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet4, tweet7));

        assertEquals("Multi minimum", d2.getEpochSecond() - d11.getEpochSecond(),
                timespan.getEnd().getEpochSecond() - timespan.getStart().getEpochSecond());
    }

    @Test public void testGetTimespan4() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet5, tweet7));

        assertEquals("expected start", d31, timespan.getStart());
        assertEquals("expected end", d32, timespan.getEnd());
    }

    @Test public void testGetTimespan5() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet6, tweet7));

        assertEquals("expected start", d32, timespan.getStart());
        assertEquals("expected end", d32, timespan.getEnd());
    }

    /*
     * * For user name:
     * A-Z a-z - _
     * 
     * For @-mention:
     * 
     * @ is the begin character; mutipul @; @ preceded by
     * usernameCharacter; @balabala is the end of text
     */
    @Test public void testGetMentionedUsers() {
        Set<String> mentionedUsers = Extract
                .getMentionedUsers(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5, tweet6, tweet7));
        for (String i : mentionedUsers) {
            i.toLowerCase();
        }
        assertTrue(new HashSet<String>(Arrays.asList("frank_8", "i-tayler", "ben", "bob")).equals(mentionedUsers));
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */

}

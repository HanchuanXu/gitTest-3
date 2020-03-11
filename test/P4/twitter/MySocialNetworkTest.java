package P4.twitter;

import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import P4.twitter.SocialNetwork;
import P4.twitter.Tweet;
/*
 * Testing strategy
 *
 * alyssa's followers are more than lancy's
 * but Alyssa RT lancy'e tweet
 * so lancy's influence is more than lancy's.
 */
public class MySocialNetworkTest {

    private static final Instant d11 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d12 = Instant.parse("2016-02-17T10:30:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d31 = Instant.parse("2016-02-17T11:30:00Z");
    private static final Instant d32 = Instant.parse("2016-02-17T12:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa",
            "RT @lancy: is it reasonable to talk about rivest so much?", d11);
    private static final Tweet tweet2 = new Tweet(2, "frank_8", "Never say nerver @alyssa ", d11);
    private static final Tweet tweet3 = new Tweet(3, "i-tayler", " @alyssa keep life simple", d12);
    private static final Tweet tweet4 = new Tweet(4, "cool", "rivest  @alyssa  talk in  @alyssa  30 minutes #hype", d2);
    private static final Tweet tweet5 = new Tweet(5, "ben", "Who's your  @alyssa  daddy?", d31);
    private static final Tweet tweet6 = new Tweet(6, "bob", " @lancy  php is the best language in theworld!", d32);
    private static final Tweet tweet7 = new Tweet(7, "lancy", "Happy @ben hacking!", d32);

    @Test(expected = AssertionError.class) public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test public void testGuessFollowsGraph() {
        Map<String, Set<String>> followsGraph = SocialNetwork
                .guessFollowsGraph(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5, tweet6, tweet7));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue(influencers.get(0).equals("lancy"));
        assertTrue(influencers.get(1).equals("alyssa"));
    }
}

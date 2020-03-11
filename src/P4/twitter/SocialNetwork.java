/*
 * Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course
 * staff.
 */
package P4.twitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames. Users can't follow themselves. If A
 * doesn't follow anybody, then map[A] may be the empty set, or A may not even
 * exist
 * as a key in the map; this is true even if A is followed by other people in
 * the network.
 * Twitter usernames are not case sensitive, so "ernie" is the same as "ERNie".
 * A username should appear at most once as a key in the map or in any given
 * map[A] set.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets
     *        a list of tweets providing the evidence, not modified by this
     *        method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of
     *         tweets.
     *         One kind of evidence that Ernie follows Bert is if Ernie
     * @-mentions Bert in a tweet. This must be implemented. Other kinds
     *            of evidence may be used at the implementor's discretion.
     *            All the Twitter usernames in the returned social network must be
     *            either authors or @-mentions in the list of tweets.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> result = new HashMap<>();
        final String regex = "((?<=^@)|(?<=[^\\w-]@))[\\w-]+";

        for (Tweet tweet : tweets) {
            String author = tweet.getAuthor().toLowerCase();
            if (!result.containsKey(author)) {
                result.put(author, new HashSet<>());
            }
            Set<String> followedUsers = new HashSet<>();
            Matcher m = Pattern.compile(regex).matcher(tweet.getText());
            while (m.find()) {
                followedUsers.add(m.group().toLowerCase());
            }
            result.get(author).addAll(followedUsers);
        }
        // People can't follow themselves
        for (String name : result.keySet()) {
            result.get(name).remove(name);
        }
        // Problem 4: Get smarter Using Awareness
        final String regex2 = "(?<=^RT\\s\\@)[\\w\\-]+(?=\\:)";

        for (Tweet tweet : tweets) {
            List<String> rtedUsers = new ArrayList<>();
            Matcher m = Pattern.compile(regex2).matcher(tweet.getText());
            while (m.find()) {
                rtedUsers.add(m.group());
            }
            if (rtedUsers.size() == 1) {
                String rteduser = rtedUsers.get(0);
                String rtuser = tweet.getAuthor();
                for (String name : result.keySet()) {
                    if (result.get(name).contains(rtuser) && !name.equals(rteduser)) {
                        {
                            result.get(name).add(rteduser);
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     * 
     * @param followsGraph
     *        a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        Map<String, Long> peopleInfluence = new HashMap<>();
        for (String name : followsGraph.keySet()) {
            for (String followedname : followsGraph.get(name)) {
                if (!peopleInfluence.containsKey(followedname)) {
                    peopleInfluence.put(followedname, 0l);
                }
                long influence = peopleInfluence.get(followedname);
                peopleInfluence.put(followedname, influence + 1);
            }
        }
        List<String> result = new ArrayList<>(peopleInfluence.keySet());
        // sort result according to people's influence(How many people follow him/her)
        result.sort((String o1, String o2) -> Math.toIntExact(peopleInfluence.get(o2) - peopleInfluence.get(o1)));
        return result;
    }

}


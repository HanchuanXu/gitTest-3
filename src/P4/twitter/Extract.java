/*
 * Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course
 * staff.
 */
package P4.twitter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *        list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
        List<Instant> timeStampList = new ArrayList<>();

        // First sort the tweets according to their timestamps.
        for (Tweet i : tweets) {
            timeStampList.add(i.getTimestamp());
        }
        timeStampList.sort((o1, o2) -> Math.toIntExact(o1.getEpochSecond() - o2.getEpochSecond()));

        // Find the shortest time between two tweets
        long span = timeStampList.get(1).getEpochSecond() - timeStampList.get(0).getEpochSecond();
        int start = 0;
        for (int i = 0; i < timeStampList.size() - 1; i++) {
            long tempSpan = timeStampList.get(i + 1).getEpochSecond() - timeStampList.get(i).getEpochSecond();
            if (tempSpan < span) {
                start = i;
                span = tempSpan;
            }
        }

        Timespan result = new Timespan(timeStampList.get(start), timeStampList.get(start + 1));

        return result;
    }

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *        list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by
     *         any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        // find the @-mentioned name by regex searching
        // (be careful about the situation where @ is the first character)
    	// \w匹配任何字母、数字和下划线
    	//(?<=pattern) 非获取匹配，反向肯定预查，与正向肯定预查类似，只是方向相反。
    	//例如，“(?<=95|98|NT|2000)Windows”能匹配“2000Windows”中的“Windows”，但不能匹配“3.1Windows”中的“Windows”。
    	//^ 匹配输入字行首
        final String regex = "((?<=^@)|(?<=[^\\w-]@))[\\w-]+";

        Set<String> allMentionedUsers = new HashSet<>();
        for (Tweet tweet : tweets) {
            Matcher m = Pattern.compile(regex).matcher(tweet.getText());
            while (m.find()) {
                allMentionedUsers.add(m.group().toLowerCase());
            }
        }

        return allMentionedUsers;
    }

}

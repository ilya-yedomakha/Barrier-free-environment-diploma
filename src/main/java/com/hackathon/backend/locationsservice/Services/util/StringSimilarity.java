package com.hackathon.backend.locationsservice.Services.util;

public class StringSimilarity {

    public static double likeness(String s1, String s2) {
        if (s1 == null || s2 == null || s1.isBlank() || s2.isBlank()) return 0;
        s1 = s1.toLowerCase().trim();
        s2 = s2.toLowerCase().trim();

        int maxLen = Math.max(s1.length(), s2.length());
        if (maxLen == 0) return 1.0;

        int dist = levenshteinDistance(s1, s2);
        return 1.0 - (double) dist / maxLen;
    }

    private static int levenshteinDistance(String s1, String s2) {
        int[] costs = new int[s2.length() + 1];
        for (int j = 0; j < costs.length; j++) costs[j] = j;

        for (int i = 1; i <= s1.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= s2.length(); j++) {
                int cj = Math.min(
                    1 + Math.min(costs[j], costs[j - 1]),
                    s1.charAt(i - 1) == s2.charAt(j - 1) ? nw : nw + 1
                );
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[s2.length()];
    }
}

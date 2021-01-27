package com.socialmedia.handles.extractor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class LinkedInRegexTest{
    public static void main(String args[]) {
        final String regex = "^https:\\/\\/[a-z]{2,3}\\.linkedin\\.com\\/.*$";
        final String regex3 = "^((?:(?:https?:\\/\\/)?(?:www\\.)?(?:[a-z]{2,3}\\.)?(?:linkedin\\.com\\/in\\/))?([a-z0-9\\-]+)(\\/?)$)";
        final String regex2 = "^((?:(?:https?:\\/\\/)?(?:www\\.)?(?:[a-z]{2,3}\\.)?(?:linkedin\\.com\\/company\\/))?([a-z0-9\\-]+)(\\/?)$)";

        final String string = "https://www.linkedin.com/sometext\n"
                + "https://uk.linkedin.com/in/wiliam-ferraciolli-a9a29795\n"
                + "https://it.linkedin.com/hp/\n"
                + "https://cy.linkedin.com/hp/\n"
                + "https://www.linkedin.com/profile/view?id=AAIAABQnNlYBIx8EtS5T1RTUbxHQt5Ww&trk=nav_responsive_tab_profile\n\n\n\n"
                + "http://stackoverflow.com/questions/ask\n"
                + "google.com\n"
                + "http://uk.linkedin.com/in/someDodgeAddress\n"
                + "http://dodge.linkedin.com/in/someDodgeAddress\n"
                + "http://www.linkedin.com/in/someDodgeAddress";

        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(string);
        final Pattern pattern2 = Pattern.compile(regex2, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        final Matcher matcher2 = pattern2.matcher(string);
        final Pattern pattern3 = Pattern.compile(regex3, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        final Matcher matcher3 = pattern3.matcher(string);

        while (matcher.find()) {
            System.out.println("Full match: " + matcher.group(0));
            for (int i = 1; i <= matcher.groupCount(); i++) {
                System.out.println("Group " + i + ": " + matcher.group(i));
            }
        }

        while (matcher2.find()) {
            System.out.println("Full match: " + matcher2.group(0));
            for (int i = 1; i <= matcher2.groupCount(); i++) {
                System.out.println("Group " + i + ": " + matcher2.group(i));
            }
        }

        while (matcher3.find()) {
            System.out.println("Full match: " + matcher3.group(0));
            for (int i = 1; i <= matcher3.groupCount(); i++) {
                System.out.println("Group " + i + ": " + matcher3.group(i));
            }
        }
    }
}
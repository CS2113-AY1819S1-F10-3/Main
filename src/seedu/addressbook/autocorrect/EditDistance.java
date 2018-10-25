//@@author ShreyasKp
package seedu.addressbook.autocorrect;

/**
 * Returns the edit distance needed to convert one string to the other.
 */
public class EditDistance {

    /**
     * If returns 0, the strings are same.
     * If returns 1, that means either a character is added, removed or replaced and so on.
     *
     * @param inputString The string input by the user.
     * @param storedString The string contained in the Dictionary.
     * @return The minimum number of operations required to transform inputString into storedString.
     */
    public static int computeDistance(String inputString, String storedString) {

        int length1 = inputString.length();
        int length2 = storedString.length();

        //Solution below adapted from https://www.programcreek.com/2013/12/edit-distance-in-java/
        // len1+1, len2+1, because finally return dp[len1][len2]
        int[][] dp = new int[length1 + 1][length2 + 1];

        for (int i = 0; i <= length1; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= length2; j++) {
            dp[0][j] = j;
        }

        //iterate through, and check last char
        for (int i = 0; i < length1; i++) {
            char c1 = inputString.charAt(i);
            for (int j = 0; j < length2; j++) {
                char c2 = storedString.charAt(j);

                //if last two chars equal
                if (c1 == c2) {
                    //update dp value for +1 length
                    dp[i + 1][j + 1] = dp[i][j];
                } else {
                    int replace = dp[i][j] + 1;
                    int insert = dp[i][j + 1] + 1;
                    int delete = dp[i + 1][j] + 1;

                    int min = replace > insert ? insert : replace;
                    min = delete > min ? min : delete;
                    dp[i + 1][j + 1] = min;
                }
            }
        }

        return dp[length1][length2];
    }
}

/* Finalised on 15/06/2020 */

package com.example.swedishnounpractice.helper;

/**
 * Handles the answer checking for the answers given in the QuestionActivity.
 */
public class CheckAnswerHelper
{
    /**
     * Determines whether a provided answer is correct or incorrect.
     * @param answer - The answer provided by the user.
     * @param actual - The answer which is actually correct.
     * @return Whether the answer is correct, has a typo, or is incorrect.
     */
    public static int evaluateAnswer (String answer, String actual)
    {
        // get the answer and actual answer in lowercase
        answer = answer.toLowerCase ();
        actual = actual.toLowerCase ();
        // determine if the answer is longer than the actual answer
        if (answer.length () > actual.length ())
        {
            // if there is an extra character, the answer is correct but spelt wrong
            if (hasExtraCharacter (answer, actual))
                return ConstantHelper.CORRECT_TYPO;
        } else
        {
            // if there is one incorrect character, but the rest are correct, it is spelt wrong
            if (numberIncorrectCharacters (answer, actual) == 1)
            {
                return ConstantHelper.CORRECT_TYPO;
            } else if (numberIncorrectCharacters (answer, actual) == 0)
            {
                // if there are no extra or incorrect characters
                return ConstantHelper.CORRECT;
            }
        }
        return ConstantHelper.INCORRECT;
    }

    /**
     * Determines if a provided answer has an extra character or not.
     * @param answer - The answer provided by the user.
     * @param actual - The answer which is actually correct.
     * @return Whether the provided answer features an extra character or not.
     */
    public static boolean hasExtraCharacter (String answer, String actual)
    {
        return answer.contains (actual) && Math.abs (answer.length () - actual.length ()) == 1;
    }

    /**
     * Returns the number of incorrect characters within a provided answer.
     * @param answer - The answer provided by the user.
     * @param actual - The answer which is actually correct.
     * @return The number of incorrect characters within an answer.
     */
    public static int numberIncorrectCharacters (String answer, String actual)
    {
        int matches = 0;
        // loop through each character of the answer
        for (int i = 0; i < answer.length (); i++)
        {
            // if the character matches the correct character, increment the counter
            if (answer.charAt (i) == actual.charAt (i))
                matches++;
        }
        return actual.length () - matches;
    }
}
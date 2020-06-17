/* Finalised on 15/06/2020 */

package com.example.swedishnounpractice.helper;

public class CheckAnswerHelper
{
    public static int evaluateAnswer (String answer, String actual)
    {
        answer = answer.toLowerCase();
        actual = actual.toLowerCase();

        if (answer.length() > actual.length())
        {
            if (hasExtraCharacter (answer, actual))
                return ConstantHelper.CORRECT_TYPO;
        } else
        {
            if (numberIncorrectCharacters (answer, actual) == 1)
            {
                return ConstantHelper.CORRECT_TYPO;
            } else if (numberIncorrectCharacters (answer, actual) == 0)
            {
                return ConstantHelper.CORRECT;
            }
        }
        return ConstantHelper.INCORRECT;
    }

    public static boolean hasExtraCharacter (String answer, String actual)
    {
        return answer.contains(actual) && Math.abs(answer.length() - actual.length()) == 1;
    }

    public static int numberIncorrectCharacters (String answer, String actual)
    {
        int matches = 0;
        for (int i = 0; i < answer.length(); i++)
        {
            if (answer.charAt(i) == actual.charAt(i))
                matches++;
        }

        return actual.length() - matches;
    }
}
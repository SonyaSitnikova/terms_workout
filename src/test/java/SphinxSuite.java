import org.junit.Assert;
import org.junit.Test;
import ru.ominit.RiddleLoader;
import ru.ominit.model.Sphinx;
import ru.ominit.model.Verdict;

import java.util.Random;

/**
 * @author akryukov
 * 03.04.2018
 */
public class SphinxSuite {
    private RiddleLoader loader;
    private Random random;
    private Sphinx sphinx;

    private String wheat1662 = ("using System;\n" +
        "using System.Collections.Generic;\n" +
        "using System.Linq;\n" +
        "using System.Text;\n" +
        "using System.Threading.Tasks;\n" +
        "\n" +
        "namespace Example\n" +
        "{\n" +
        "    class Program\n" +
        "    {\n" +
        "        static void Main(string[] args)\n" +
        "        {\n" +
        "            Console.Write(\"Ура!\\n\\\"Заработало\\\"!\");\n" +
        "        }\n" +
        "    }\n" +
        "}").replaceAll("\\s+", " ");

    public SphinxSuite() {
        loader = new RiddleLoader();
        random = new Random();
        sphinx = new Sphinx(loader, random);
    }

    @Test
    public void startJourney() {
        Verdict verdict = sphinx.decide("", "");
        Assert.assertFalse(verdict.correct);
        Assert.assertFalse(verdict.incorrect);
        Assert.assertTrue(verdict.relevant);
        Assert.assertNotEquals("", verdict.future.getWheat());
        Assert.assertNotEquals("", verdict.future.getRiddle().getNeedle());
    }

    @Test
    public void getInitial() {
        Verdict verdict = sphinx.decide("1662", "c9e9f0f9-336c-4a90-bb74-a9e9d38ae995");
        Assert.assertFalse(verdict.correct);
        Assert.assertFalse(verdict.incorrect);
        Assert.assertTrue(verdict.relevant);
        Assert.assertEquals(wheat1662, verdict.future.getHaystack().getGrain());
        Assert.assertEquals("точка входа в программу", verdict.future.getRiddle().getNeedle());
    }

    @Test
    public void tryToLoadRiddleNotPresentInHaystack(){
        String riddleId = "a90236d6-c08e-4da7-89d1-234cde20abef";
        String haystackId = "1860";
        Verdict verdict = sphinx.decide(haystackId, riddleId);
        Assert.assertFalse(verdict.correct);
        Assert.assertFalse(verdict.incorrect);
        Assert.assertTrue(verdict.relevant);
    }

    @Test
    public void correctAttemptFullAnswer() {
        String lastRiddleId = "c9e9f0f9-336c-4a90-bb74-a9e9d38ae995";
        String lastHaystackId = "1662";
        String attempt = "static void Main(string[] args)\n        {";
        Verdict verdict = sphinx.decide(lastHaystackId, lastRiddleId, attempt);
        Assert.assertTrue(verdict.correct);
        Assert.assertFalse(verdict.incorrect);
        Assert.assertTrue(verdict.relevant);
    }

    @Test
    public void correctAttemptShortAnswer() {
        String lastRiddleId = "c9e9f0f9-336c-4a90-bb74-a9e9d38ae995";
        String lastHaystackId = "1662";
        String attempt = "Main";
        Verdict verdict = sphinx.decide(lastHaystackId, lastRiddleId, attempt);
        Assert.assertTrue(verdict.correct);
        Assert.assertFalse(verdict.incorrect);
        Assert.assertTrue(verdict.relevant);
    }

    @Test
    public void incorrectAttemptRelevantAnswer() {
        String lastRiddleId = "c9e9f0f9-336c-4a90-bb74-a9e9d38ae995";
        String lastHaystackId = "1662";
        String attempt = "using System;";
        Verdict verdict = sphinx.decide(lastHaystackId, lastRiddleId, attempt);
        Assert.assertFalse(verdict.correct);
        Assert.assertTrue(verdict.incorrect);
        Assert.assertTrue(verdict.relevant);
        Assert.assertEquals(wheat1662, verdict.future.getHaystack().getGrain());
        Assert.assertEquals("точка входа в программу", verdict.future.getRiddle().getNeedle());
    }

    @Test
    public void incorrectAttemptIrrelevantAnswer() {
        String lastRiddleId = "c9e9f0f9-336c-4a90-bb74-a9e9d38ae995";
        String lastHaystackId = "1662";
        String attempt = "irrelevantAnswer";
        Verdict verdict = sphinx.decide(lastHaystackId, lastRiddleId, attempt);
        Assert.assertFalse(verdict.correct);
        Assert.assertFalse(verdict.incorrect);
        Assert.assertFalse(verdict.relevant);
        Assert.assertEquals(wheat1662, verdict.future.getHaystack().getGrain());
        Assert.assertEquals("точка входа в программу", verdict.future.getRiddle().getNeedle());
    }

    @Test
    public void recognizeCorrectMultilineAnswerWithCRLF() {
        String riddleId = "a90236d6-c08e-4da7-89d1-234cde20abef";
        String haystackId = "9231";
        String attempt = "t = Console.ReadLine();\r\n            x = double.Parse(t);";
        Verdict verdict = sphinx.decide(haystackId, riddleId, attempt);
        Assert.assertTrue(verdict.correct);
        Assert.assertFalse(verdict.incorrect);
        Assert.assertTrue(verdict.relevant);
    }

    @Test
    public void acceptAnyOfAnswers(){
        String riddleId = "027eb6c1-d242-48a1-9fa8-6b3671df71c9";
        String haystackId = "1860";
        String firstAttempt = "String first";
        Verdict firstVerdict = sphinx.decide(haystackId, riddleId, firstAttempt);
        Assert.assertTrue(firstVerdict.correct);
        Assert.assertFalse(firstVerdict.incorrect);
        Assert.assertTrue(firstVerdict.relevant);

        String secondAttempt = "String second";
        Verdict secondVerdict = sphinx.decide(haystackId, riddleId, secondAttempt);
        Assert.assertTrue(secondVerdict.correct);
        Assert.assertFalse(secondVerdict.incorrect);
        Assert.assertTrue(secondVerdict.relevant);
    }
}

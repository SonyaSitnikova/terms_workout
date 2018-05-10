package ru.ominit.model;

/**
 * Created by Александр on 30.03.2018.
 */
public class Verdict {
    public final VerdictDecision decision;
    public final String last_attempt;
    public final Fate future;
    public final Fate past;

    public Verdict(
            Fate past,
            VerdictDecision decision,
            String lastAttempt,
            Fate future
    ) {
        this.decision = decision;
        this.last_attempt = lastAttempt;
        this.future = future;
        this.past = past;
    }
}

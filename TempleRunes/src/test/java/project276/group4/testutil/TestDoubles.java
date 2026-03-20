package project276.group4.testutil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import project276.group4.domain.interfaces.GameSignals;
import project276.group4.domain.interfaces.ScoreManager;
import project276.group4.domain.types.GameOverReason;

/**
 * Collection of lightweight test doubles shared across unit and integration tests.
 */
public final class TestDoubles {

    private TestDoubles() {
    }

    /**
     * Simple in-memory implementation of {@link ScoreManager} that records score mutations.
     */
    public static final class RecordingScoreManager implements ScoreManager {
        private int score;
        private final List<Integer> additions = new ArrayList<>();
        private final List<Integer> deductions = new ArrayList<>();
        private int threshold = 0;

        public RecordingScoreManager() {
            this(0);
        }

        public RecordingScoreManager(int startingScore) {
            this.score = startingScore;
        }

        public void setThreshold(int threshold) {
            this.threshold = threshold;
        }

        @Override
        public void addScore(int points) {
            if (points < 0) {
                throw new IllegalArgumentException("points cannot be negative");
            }
            additions.add(points);
            score += points;
        }

        @Override
        public void deductScore(int points) {
            if (points < 0) {
                throw new IllegalArgumentException("points cannot be negative");
            }
            deductions.add(points);
            score -= points;
        }

        @Override
        public int getCurrentScore() {
            return score;
        }

        @Override
        public void resetScore() {
            score = 0;
            additions.clear();
            deductions.clear();
        }

        @Override
        public boolean isScoreBelowThreshold() {
            return score < threshold;
        }

        public List<Integer> getAdditions() {
            return Collections.unmodifiableList(additions);
        }

        public List<Integer> getDeductions() {
            return Collections.unmodifiableList(deductions);
        }
    }

    /**
     * Recording implementation of {@link GameSignals} that captures emitted events for assertions.
     */
    public static final class RecordingGameSignals implements GameSignals {
        private GameOverReason lastGameOverReason;
        private boolean victoryTriggered;
        private boolean exitUnlocked;
        private final List<String> damageSources = new ArrayList<>();
        private final List<String> collectedItems = new ArrayList<>();
        private final List<Integer> collectedScores = new ArrayList<>();
        private int runeCollectedCount;
        private int runeTotalSet;
        private int runeCollectedSet;

        @Override
        public void triggerGameOver(GameOverReason reason) {
            this.lastGameOverReason = reason;
        }

        @Override
        public void triggerVictory() {
            this.victoryTriggered = true;
        }

        @Override
        public void unlockExit() {
            this.exitUnlocked = true;
        }

        @Override
        public void onItemCollected(String itemName, int scoreValue) {
            this.collectedItems.add(itemName);
            this.collectedScores.add(scoreValue);
        }

        @Override
        public void onPlayerDamaged(String damageSource) {
            this.damageSources.add(damageSource);
        }

        @Override
        public void onCollectRune() {
            this.runeCollectedCount++;
        }

        @Override
        public void setRuneTotals(int total, int collected) {
            this.runeTotalSet = total;
            this.runeCollectedSet = collected;
        }

        @Override
        public void onTrapSteppedOn() {}

        @Override
        public void onCursedIdolTouched() {}

        public GameOverReason getLastGameOverReason() {
            return lastGameOverReason;
        }

        public boolean isVictoryTriggered() {
            return victoryTriggered;
        }

        public boolean isExitUnlocked() {
            return exitUnlocked;
        }

        public List<String> getDamageSources() {
            return Collections.unmodifiableList(damageSources);
        }

        public List<String> getCollectedItems() {
            return Collections.unmodifiableList(collectedItems);
        }

        public List<Integer> getCollectedScores() {
            return Collections.unmodifiableList(collectedScores);
        }

        public int getRuneCollectedCount() {
            return runeCollectedCount;
        }

        public int getRuneTotalSet() {
            return runeTotalSet;
        }

        public int getRuneCollectedSet() {
            return runeCollectedSet;
        }
    }
}


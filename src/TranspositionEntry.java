public class TranspositionEntry {
    private int depth;
    private int evaluation;
    private int previousOccurences;
    // Add other fields as needed: best move, flags, etc.
    
    public TranspositionEntry(int depth, int evaluation, int previousOccurences) {
        this.depth = depth;
        this.evaluation = evaluation;
        this.previousOccurences = previousOccurences;
        // Initialize other fields if needed
    }
    
    // Getters and setters for fields
    public int getDepth() {
        return depth;
    }
    
    public void setDepth(int depth) {
        this.depth = depth;
    }
    
    public int getEvaluation() {
        return evaluation;
    }
    
    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    public int getOccurences() {
        return previousOccurences;
    }
    
    public void incrementOccurence() {
        this.previousOccurences++;
    }

    public void decrementOccurence() {
        this.previousOccurences--;
    }
}

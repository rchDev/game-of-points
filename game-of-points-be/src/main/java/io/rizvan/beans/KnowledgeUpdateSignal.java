package io.rizvan.beans;

public class KnowledgeUpdateSignal {
    private boolean runInference = false;

    public boolean shouldRunInference() {
        return runInference;
    }

    public void setRunInference(boolean runInference) {
        this.runInference = runInference;
    }
}

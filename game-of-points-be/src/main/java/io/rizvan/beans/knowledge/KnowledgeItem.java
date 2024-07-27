package io.rizvan.beans.knowledge;

public abstract class KnowledgeItem<T> {
    private boolean isKnown = false;

    public boolean isKnown() {
        return isKnown;
    }

    public void setKnown(boolean known) {
        isKnown = known;
    }

    public abstract T getValue();
    public abstract void setValue(T value);
}

package io.rizvan.beans.knowledge;

public abstract class KnowledgeItem<T> {
    private boolean isKnown = false;
    private String name;

    public boolean isKnown() {
        return isKnown;
    }
    public void setKnown(boolean known) {
        isKnown = known;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract T getValue();
    public abstract void setValue(T value);
}

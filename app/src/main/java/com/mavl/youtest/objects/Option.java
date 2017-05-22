package com.mavl.youtest.objects;

/**
 * Created by mavl on 21.05.2017.
 */
public class Option {
    String text;
    boolean correct;
    public Option() {};

    public Option(String text) {
        this.text = text;
        this.correct = false;
    }

    public Option(String text, boolean cr) {
        this.text = text;
        this.correct = cr;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}

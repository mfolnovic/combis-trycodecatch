package hr.combis.explorer.controller.slack.nlpUtils;

import java.util.ArrayList;
import java.util.List;

public class Document {
    private List<Sentence> sentences;

    public Document(){
        this.sentences = new ArrayList<>();
    }

    public Document(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public void addSentence(Sentence sentence) {
        this.sentences.add(sentence);
    }
}

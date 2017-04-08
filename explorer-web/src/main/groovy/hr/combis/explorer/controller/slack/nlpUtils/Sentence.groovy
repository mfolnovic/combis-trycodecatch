package hr.combis.explorer.controller.slack.nlpUtils;

import java.util.ArrayList;
import java.util.List;

public class Sentence {
    private List<Word> words

    public Sentence(){
        this.words = new ArrayList<>();
    }

    public Sentence(List<Word> words) {
        this.words = words;
    }

    public List<Word> getWords() {
        return words;
    }

    public void addWord(Word word){
        this.words.add(word)
    }

    public double wordScore(Word word){
        double score = 0
        for (Word w : this.words){
            score += w.getSimilarity(word)
        }
        return score
    }
}

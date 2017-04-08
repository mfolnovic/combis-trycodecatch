package hr.combis.explorer.controller.slack.nlpUtils;

public class Word {
    private String token;
    private String pos;
    private String lemma;
    private String ner;

    public Word(String token, String pos, String lemma, String ner) {
        this.token = token;
        this.pos = pos;
        this.lemma = lemma;
        this.ner = ner;
    }

    public String getToken() {
        return token;
    }

    public String getPos() {
        return pos;
    }

    public String getLemma() {
        return lemma;
    }

    public String getNer() {
        return ner;
    }
}

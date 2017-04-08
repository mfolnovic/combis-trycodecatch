package hr.combis.explorer.controller.slack.nlpUtils

public class Document {
    private List<Sentence> sentences

    public Document(){
        this.sentences = new ArrayList<>()
    }

    public Document(List<Sentence> sentences) {
        this.sentences = sentences
    }

    public List<Sentence> getSentences() {
        return sentences
    }

    public void addSentence(Sentence sentence) {
        this.sentences.add(sentence)
    }

    public Sentence getSentence(int index){
        if (index < sentences.size()){
            this.sentences.get(index)
        }
        return null
    }

    public size(){
        return this.sentences.size()
    }
}

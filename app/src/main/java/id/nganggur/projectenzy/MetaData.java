package id.nganggur.projectenzy;

public class MetaData {
    private String query, result;
    private int vote;

    public MetaData(String query, String result) {
        this.query = query;
        this.result = result;
        this.vote = 1;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getVote() {
        return vote;
    }

    public void upVote(){
        this.vote++;
    }

    public void downVote(){
        this.vote--;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }
}

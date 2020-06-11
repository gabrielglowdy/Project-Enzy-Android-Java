package id.nganggur.projectenzy;

public class ObjectInput {
    private String query, result;

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

    public ObjectInput(String query, String result) {
        this.query = query;
        this.result = result;
    }

    public ObjectInput() {
    }
}

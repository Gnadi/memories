package at.memories.model.request;

public class ResponseRequest {
    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ResponseRequest(String token) {
        this.token = token;
    }
}

package io.punch_it.punchit;

/**
 * Created by hemal on 12/24/15.
 */
public class HomeFeed {
    String user, time, question, first_post, second_post, comment;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getFirst_post() {
        return first_post;
    }

    public void setFirst_post(String first_post) {
        this.first_post = first_post;
    }

    public String getSecond_post() {
        return second_post;
    }

    public void setSecond_post(String second_post) {
        this.second_post = second_post;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public HomeFeed(String user, String time, String question, String first_post, String second_post, String comment) {

        this.user = user;
        this.time = time;
        this.question = question;
        this.first_post = first_post;
        this.second_post = second_post;
        this.comment = comment;
    }
}

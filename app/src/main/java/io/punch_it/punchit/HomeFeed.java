package io.punch_it.punchit;

import java.util.Date;

/**
 * Created by hemal on 12/24/15.
 */
public class HomeFeed {
    String user, question, first_post, second_post, comment;
    Date date;
    int likesIn1, likesIn2;

    public HomeFeed(String user, String question, String first_post, String second_post, String comment, Date date, int likesIn1, int likesIn2) {
        this.user = user;
        this.question = question;
        this.first_post = first_post;
        this.second_post = second_post;
        this.comment = comment;
        this.date = date;
        this.likesIn1 = likesIn1;
        this.likesIn2 = likesIn2;
    }

    public String getUser() {
        return user;
    }

    public String getQuestion() {
        return question;
    }

    public String getFirst_post() {
        return first_post;
    }

    public String getSecond_post() {
        return second_post;
    }

    public String getComment() {
        return comment;
    }

    public Date getDate() {
        return date;
    }

    public int getLikesIn1() {
        return likesIn1;
    }

    public int getLikesIn2() {
        return likesIn2;
    }
}

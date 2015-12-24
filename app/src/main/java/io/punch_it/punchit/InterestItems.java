package io.punch_it.punchit;

/**
 * Created by hemal on 12/20/15.
 */
public class InterestItems {
    String interestName;
    boolean isInterested;

    public InterestItems(String interestName, boolean isInterested) {
        this.interestName = interestName;
        this.isInterested = isInterested;
    }

    public String getInterestName() {
        return interestName;

    }

    public void setInterestName(String interestName) {
        this.interestName = interestName;
    }

    public boolean isInterested() {
        return isInterested;
    }

    public void setIsInterested(boolean isInterested) {
        this.isInterested = isInterested;
    }
}

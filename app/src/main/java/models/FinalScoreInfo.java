package models;

import java.util.ArrayList;

public class FinalScoreInfo {

    ArrayList<AdvisoryInfo> objAdvisoryInfoList;

    private boolean errorStatus;

    private String message;

    private int testId = -1;
    private int scoreId = -1;
    private int scoreStatus = 0;
    private int advisoryStatus = 0;

    private double finalTestScore = -1;
    private double finalGlobalMeanScore = -1;
    private double finalModelMeanScore = -1;

    public FinalScoreInfo() {
        objAdvisoryInfoList = new ArrayList<AdvisoryInfo>();
    }

    public void addAdvisoryDetails(String advisoryClass, String advisorySubclass, String advisoryText) {

        AdvisoryInfo objAdvisoryData = new AdvisoryInfo(advisoryClass, advisorySubclass, advisoryText);
        objAdvisoryInfoList.add(objAdvisoryData);

    }

    public ArrayList<AdvisoryInfo> getObjAdvisoryInfoList() {
        return objAdvisoryInfoList;
    }

    public double getFinalTestScore() {
        return finalTestScore;
    }

    public void setFinalTestScore(double finalTestScore) {
        this.finalTestScore = finalTestScore;
    }

    public double getFinalGlobalMeanScore() {
        return finalGlobalMeanScore;
    }

    public void setFinalGlobalMeanScore(double finalGlobalMeanScore) {
        this.finalGlobalMeanScore = finalGlobalMeanScore;
    }

    public double getFinalModelMeanScore() {
        return finalModelMeanScore;
    }

    public void setFinalModelMeanScore(double finalModelMeanScore) {
        this.finalModelMeanScore = finalModelMeanScore;
    }

    public boolean getErrorStatus() {
        return errorStatus;
    }

    public void setErrorStatus(boolean errorStatus) {
        this.errorStatus = errorStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getScoreId() {
        return scoreId;
    }

    public void setScoreId(int scoreId) {
        this.scoreId = scoreId;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public int getScoreStatus() {
        return scoreStatus;
    }

    public void setScoreStatus(int scoreStatus) {
        this.scoreStatus = scoreStatus;
    }

    public int getAdvisoryStatus() {
        return advisoryStatus;
    }

    public void setAdvisoryStatus(int advisoryStatus) {
        this.advisoryStatus = advisoryStatus;
    }

}

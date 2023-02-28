package com.example.dashbosrd;

import com.google.firebase.database.ServerValue;

public class History {
    private String historyKey;
    private String category;
    private String pickedImage;
    private String stage;
    private String probability;
    private String userId;
    private Object timeStamp;

    public History(String category, String pickedImage, String stage, String probability, String userId) {
        this.category = category;
        this.pickedImage = pickedImage;
        this.stage = stage;
        this.probability = probability;
        this.userId = userId;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

    public History() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getHistoryKey() {
        return historyKey;
    }

    public void setHistoryKey(String historyKey) {
        this.historyKey = historyKey;
    }

    public String getPickedImage() {
        return pickedImage;
    }

    public void setPickedImage(String pickedImage) {
        this.pickedImage = pickedImage;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}

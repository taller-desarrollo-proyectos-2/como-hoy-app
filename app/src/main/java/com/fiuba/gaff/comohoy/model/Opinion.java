package com.fiuba.gaff.comohoy.model;

public class Opinion {

    private Long mId;
    private String mNameOpinion;
    private String mDescription;
    private int mPuntuation;
    private String mReplica;
    private Long mOrderId;

    public Opinion(Long id) {
        mId = id;
    }

    public Long getId() {
        return mId;
    }

    public String getNameOpinion() { return mNameOpinion; }
    public void setNameOpinion(String nameOpinion) { mNameOpinion = nameOpinion; }

    public String getDescription() { return mDescription; }
    public void setDescription(String description) { mDescription = description; }

    public int getPuntuation() { return mPuntuation; }
    public void setPuntuation(int puntuation) { mPuntuation = puntuation; }

    public String getReplica() { return mReplica; }
    public void setReplica(String replica) { mReplica = replica; }

    public void setId(Long id) {
        this.mId = id;
    }

    public Long getOrderId() {
        return mOrderId;
    }

    public void setOrderId(Long orderId) {
        mOrderId = orderId;
    }
}
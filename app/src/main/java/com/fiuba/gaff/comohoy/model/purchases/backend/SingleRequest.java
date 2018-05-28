package com.fiuba.gaff.comohoy.model.purchases.backend;

import com.fiuba.gaff.comohoy.model.Extra;
import com.fiuba.gaff.comohoy.model.Plate;

import java.util.List;

public class SingleRequest {

    private Long mId;
    private Plate mPlate;
    private String mClarification;
    private List<Extra> mExtras;
    private int mQuantity;

    public SingleRequest(Long id) {
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public Plate getPlate() {
        return mPlate;
    }

    public void setPlate(Plate plate) {
        mPlate = plate;
    }

    public List<Extra> getExtras() {
        return mExtras;
    }

    public void setExtras(List<Extra> extras) {
        mExtras = extras;
    }

    public String getClarification() {
        return mClarification;
    }

    public void setClarification(String clarification) {
        mClarification = clarification;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }
}

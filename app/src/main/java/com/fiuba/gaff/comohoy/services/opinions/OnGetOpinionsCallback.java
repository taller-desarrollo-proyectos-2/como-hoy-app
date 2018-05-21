package com.fiuba.gaff.comohoy.services.opinions;


import com.fiuba.gaff.comohoy.model.Opinion;

import java.util.List;

public interface OnGetOpinionsCallback {
    void onSuccess(List<Opinion> opinions);
    void onError(String reason);
}

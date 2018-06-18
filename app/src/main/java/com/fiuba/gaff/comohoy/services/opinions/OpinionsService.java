package com.fiuba.gaff.comohoy.services.opinions;

import android.app.Activity;

import com.fiuba.gaff.comohoy.model.Opinion;
import com.fiuba.gaff.comohoy.services.CustomService;

public interface OpinionsService extends CustomService {
    void getOpinions(Activity activity, int commerceId, OnGetOpinionsCallback callback);
    void publishOpinion(Activity activity, Opinion opinion, PublishOpinionCallback callback);

}

package com.fiuba.gaff.comohoy.services.location;

import android.content.Context;

import com.fiuba.gaff.comohoy.model.Location;
import com.fiuba.gaff.comohoy.services.CustomService;

public interface LocationService extends CustomService {
    Location getLocation(Context context);
}

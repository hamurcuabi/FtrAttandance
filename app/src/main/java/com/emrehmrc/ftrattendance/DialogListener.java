package com.emrehmrc.ftrattendance;

import com.emrehmrc.ftrattendance.model.Detail;

public interface DialogListener {
    void onClosed(String name);

    void onClosedDetail(Detail detail);
}

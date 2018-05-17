package com.gdn.cff;

import com.gdn.entity.Cff;
import java.util.List;

public interface CffService {
    List<Cff> getAllCff();
    Cff saveCff(Cff cff);
}

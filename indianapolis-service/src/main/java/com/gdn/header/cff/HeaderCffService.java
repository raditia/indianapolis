package com.gdn.header.cff;

import com.gdn.entity.HeaderCff;
import org.springframework.stereotype.Service;

import java.util.List;

public interface HeaderCffService {

    HeaderCff save(HeaderCff headerCff);
    List<HeaderCff> findAll();
}

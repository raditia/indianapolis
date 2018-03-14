package com.gdn.header.cff;

import com.gdn.HeaderCff;
import org.springframework.stereotype.Service;

@Service
public interface HeaderCffService {

    HeaderCff save(HeaderCff headerCff);
}

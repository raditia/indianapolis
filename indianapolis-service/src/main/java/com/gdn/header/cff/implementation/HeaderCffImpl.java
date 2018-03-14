package com.gdn.header.cff.implementation;

import com.gdn.HeaderCff;
import com.gdn.header.cff.HeaderCffService;
import com.gdn.repository.HeaderCffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HeaderCffImpl implements HeaderCffService {

    @Autowired
    private HeaderCffRepository headerCffRepository;

    @Override
    public HeaderCff save(HeaderCff headerCff) {
        return headerCffRepository.save(headerCff);
    }
}

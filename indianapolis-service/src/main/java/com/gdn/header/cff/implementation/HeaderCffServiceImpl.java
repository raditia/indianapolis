package com.gdn.header.cff.implementation;

import com.gdn.entity.HeaderCff;
import com.gdn.header.cff.HeaderCffService;
import com.gdn.repository.HeaderCffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeaderCffServiceImpl implements HeaderCffService {

    @Autowired
    private HeaderCffRepository headerCffRepository;

    @Override
    public HeaderCff save(HeaderCff headerCff) {
        return headerCffRepository.save(headerCff);
    }

    @Override
    public List<HeaderCff> findAll() {
        return headerCffRepository.findAll();
    }
}

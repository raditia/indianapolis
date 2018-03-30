package com.gdn.batch.upload_cff;

import com.gdn.upload_cff.UploadCffResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class CffProcessor implements ItemProcessor<UploadCffResponse, UploadCffResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CffProcessor.class);

    @Override
    public UploadCffResponse process(UploadCffResponse uploadCffResponse) throws Exception {
        return uploadCffResponse;
    }
}

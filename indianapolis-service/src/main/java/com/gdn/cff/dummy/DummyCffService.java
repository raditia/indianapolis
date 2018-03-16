package com.gdn.cff.dummy;

import com.gdn.upload_cff.UploadCffResponse;

import java.util.List;

public interface DummyCffService {
    UploadCffResponse createOneDummyUploadCffResponse(UploadCffResponse uploadCffResponse);
    List<UploadCffResponse> getDummyUploadCffResponse();
}

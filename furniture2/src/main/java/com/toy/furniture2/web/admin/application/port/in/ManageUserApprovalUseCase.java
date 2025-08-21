package com.toy.furniture2.web.admin.application.port.in;

public interface ManageUserApprovalUseCase {

    void approveBusinessUser(String userId);
    void rejectBusinessUser(String userId);

} 
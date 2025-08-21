package com.toy.furniture2.web.admin.application.port.out;

public interface UpdateAdminUserPort {
    void approveBusinessUser(String userId);
    void rejectBusinessUser(String userId);
} 
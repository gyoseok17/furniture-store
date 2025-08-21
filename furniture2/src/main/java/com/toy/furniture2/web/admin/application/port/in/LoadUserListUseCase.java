package com.toy.furniture2.web.admin.application.port.in;

import java.util.List;
import java.util.Map;

public interface LoadUserListUseCase {

    Map<String, List<?>> loadUsers();

} 
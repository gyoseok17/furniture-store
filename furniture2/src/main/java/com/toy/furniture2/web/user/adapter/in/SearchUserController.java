package com.toy.furniture2.web.user.adapter.in;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.toy.furniture2.web.user.application.port.in.SearchUserUseCase;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class SearchUserController {
    private final SearchUserUseCase searchUserUseCase;

    // ✅ 아이디 중복 확인 API 추가 (AJAX 요청 처리)
    @GetMapping("/checkUserId")
    @ResponseBody
    public Map<String, Boolean> checkUserId(@RequestParam(value = "userId") String userId) {
        boolean exists = searchUserUseCase.countByUserId(userId) > 0;

        Map<String, Boolean> result = new HashMap<>();
        result.put("exists", exists);

        return result;
    }

}

package com.example.assignment.coordinationapi.application.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CustomErrorController extends AbstractErrorController {

    public CustomErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    /**
     * /error path에 대해 정의한다.
     * 해당 path에 대해 오류처리를 하지 않으면
     * spring boot default 설정에 따라 /error 경로로 가려고 시도하고,
     * /error 페이지 핸들러가 없어서 다시 오류를 발생시키면서
     * 오류페이지가 무한한 순환을 하게 되며 오류가 발생한다.
     *
     * @param request
     * @return
     */
    @RequestMapping
    public ResponseEntity<String> error(HttpServletRequest request) {
        String servletPath = request.getServletPath();

        if (servletPath.equals("/error")) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        HttpStatus status = this.getStatus(request);
        return ResponseEntity
                .status(status)
                .build();
    }
}
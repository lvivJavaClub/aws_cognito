package com.example.javaclubdemo.util;

import com.example.javaclubdemo.model.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuditionUtil {

    public static String getAuditor() {
        var context =  SecurityContextHolder.getContext();
        if (context != null) {
            var authentication = context.getAuthentication();
            if (authentication != null) {
                if (authentication.getPrincipal() != null && authentication.getPrincipal() instanceof User user) {
                    return user.getEmail();
                }
            }
        }
        return "unknown";
    }
}

package com.dianagrigore.rem.permissions;

import com.dianagrigore.rem.model.enums.UserType;
import com.dianagrigore.rem.security.SecurityService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class PermissionAspect {
    private static final Logger logger = LoggerFactory.getLogger(PermissionAspect.class);
    private final SecurityService securityService;

    public PermissionAspect(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Around(value = "@annotation(permissionCheck)")
    public Object securedPermission(ProceedingJoinPoint proceedingJoinPoint, PermissionCheck permissionCheck) throws Throwable {
        List<UserType> hasAny = Arrays.asList(permissionCheck.hasAny());
        String signature = proceedingJoinPoint.getSignature().toLongString();
        logger.trace("Enforcing permission for signature {}", signature);
        securityService.enforcePermission(hasAny, signature);
        logger.trace("Successful enforcement for signature {}", signature);
        return proceedingJoinPoint.proceed();
    }
}

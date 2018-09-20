package com.astontech.virl.student.configuration;

import com.astontech.virl.student.domain.UserProfile;
import com.astontech.virl.student.services.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomSuccessHandler.class);

    @Value("${spring.security.authentication.method}")
    String SECURITY_METHOD;

    @Autowired
    private UserProfileService userProfileService;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response,
                          Authentication authentication)
            throws IOException {

        String targetUrl = determineTargetUrl(authentication, request.getSession());



        // last thing
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(Authentication authentication, HttpSession session) {
        String url = "/";

        // COLLECT ALL THE ROLES FROM THE AD RESPONSE
        List<String> roles = getRolesFromAuthorities(authentication);

        Consumer<String> setProfileInfo = role -> {
            session.setAttribute("userRole", role);
            session.setAttribute("username", authentication.getName());
            createProfile(session);
        };

        if      (isMentor(roles)) { setProfileInfo.accept("MENTOR"); }
        else if (isMentee(roles)) { setProfileInfo.accept("MENTEE"); }

        logger.info("User " + session.getAttribute("username") + " logged in.");

        return url;
    }

    private boolean isMentor(List<String> roles) {
        return roles.contains("Mentor-Dev") ||
                roles.contains("Mentor-Cisco") ||
                roles.contains("ROLE_MENTOR");
    }

    private boolean isMentee(List<String> roles) {
        return (roles.contains("Field Engineer - Cisco")  ||
                roles.contains("Field Engineer - Dev")) ||
                roles.contains("ROLE_MENTEE");
    }

    private void createProfile(HttpSession session) {
        String name = removeEmailSignature(session.getAttribute("username").toString());
        Optional<UserProfile> profile = userProfileService.findByUsername(name);

        if(!profile.isPresent()) {
            logger.info("Creating new profile for: " + name);
            UserProfile newProfile = new UserProfile(name, session.getAttribute("userRole").toString());
            userProfileService.saveProfile(newProfile);
        } else {
            profile.ifPresent(userProfile -> {
                if(Objects.isNull(userProfile.getRole())) {
                    logger.info("Updating role to " + session.getAttribute("userRole").toString() + " for user: " + name);
                    userProfile.setRole(session.getAttribute("userRole").toString());
                    userProfileService.saveProfile(userProfile);
                }
            });
        }

    }

    public static String removeEmailSignature(String name) {
        return name.contains("@") ? name.substring(0, name.indexOf("@")) : name;
    }

    private List<String> getRolesFromAuthorities(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        List<String> roles = new ArrayList<String>();

        for(GrantedAuthority grantedAuthority : authorities) {
            roles.add(grantedAuthority.getAuthority());
        }

        roles.forEach(r -> logger.info("Role Found: " + r));

        return roles;
    }
}

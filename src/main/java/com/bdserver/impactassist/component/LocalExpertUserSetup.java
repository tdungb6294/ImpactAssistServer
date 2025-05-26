package com.bdserver.impactassist.component;

import com.bdserver.impactassist.model.RegisterLocalExpertDAO;
import com.bdserver.impactassist.model.ResponseLocalExpertDAO;
import com.bdserver.impactassist.service.LocalExpertService;
import com.bdserver.impactassist.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocalExpertUserSetup implements CommandLineRunner {
    private final UserService userService;
    private final LocalExpertService localExpertService;

    public LocalExpertUserSetup(UserService userService, LocalExpertService localExpertService) {
        this.userService = userService;
        this.localExpertService = localExpertService;
    }

    @Override
    public void run(String... args) {
        List<ResponseLocalExpertDAO> experts = localExpertService.getLocalExpertList("");
        if (experts.stream().noneMatch(expert -> expert.getEmail().equals("service@metroautorepair.lt"))) {
            RegisterLocalExpertDAO registerLocalExpertDAO = RegisterLocalExpertDAO.builder()
                    .fullName("Metro Auto Repair Inc.")
                    .email("service@metroautorepair.lt")
                    .phone("+37061234567")
                    .longitude(25.334190)
                    .latitude(54.723540)
                    .password("metroautorepair")
                    .description("Trusted auto repair shop in Vilnius offering diagnostics, engine repair, and routine maintenance.")
                    .build();
            RegisterLocalExpertDAO registerLocalExpertDAO2 = RegisterLocalExpertDAO.builder()
                    .fullName("Evergreen Auto Solutions")
                    .email("info@evergreenautosolutions.lt")
                    .phone("+37067654321")
                    .longitude(25.332229)
                    .latitude(54.725026)
                    .description("Full-service auto repair shop in Vilnius, offering tire services, brake repairs, and engine diagnostics.")
                    .password("evergreen")
                    .build();
            userService.registerLocalExpertUser(registerLocalExpertDAO);
            userService.registerLocalExpertUser(registerLocalExpertDAO2);
        }
    }
}

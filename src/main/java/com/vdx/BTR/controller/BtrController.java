package com.vdx.BTR.controller;

import com.vdx.BTR.model.BusinessTripRequest;
import com.vdx.BTR.model.User;
import com.vdx.BTR.repository.BusinessTripRequestRepository;
import com.vdx.BTR.service.CurrentUserService;
import com.vdx.BTR.service.SettingsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/btr")
public class BtrController {

    private final BusinessTripRequestRepository btrRepository;
    private final CurrentUserService currentUserService;
    private final SettingsService settingsService;

    public BtrController(BusinessTripRequestRepository btrRepository,
                         CurrentUserService currentUserService,
                         SettingsService settingsService) {
        this.btrRepository = btrRepository;
        this.currentUserService = currentUserService;
        this.settingsService = settingsService;
    }

    //@Value("${app.approval.threshold}")
    //private BigDecimal approvalThreshold; //from properties

    // Dashboard: lista własnych BTR
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        User user = currentUserService.getCurrentUser();
        List<BusinessTripRequest> list = btrRepository.findByUser(user);
        model.addAttribute("btrs", list);
        return "btr-dashboard";
    }

    // Formularz nowego BTR
    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("btr", new BusinessTripRequest());
        return "btr-new";
    }

    // Zapis nowego BTR z walidacją
    @PostMapping("/new")
    public String createNew(@Valid @ModelAttribute("btr") BusinessTripRequest btr,
                            BindingResult bindingResult,
                            Model model) {

        // 1) walidacje z adnotacji (@NotNull, @DecimalMin, itd.)
        if (bindingResult.hasErrors()) {
            return "btr-new";
        }

        // 2) walidacja: endDate >= startDate
        if (btr.getEndDate().isBefore(btr.getStartDate())) {
            bindingResult.rejectValue(
                    "endDate",
                    "endDate.beforeStart",
                    "End date must be after or equal to start date"
            );
            return "btr-new";
        }

        User user = currentUserService.getCurrentUser();
        btr.setUser(user);

        // AUTOMATYCZNE REGUŁY
        BigDecimal approvalThreshold = settingsService.getSettings().getApprovalThreshold();
        BigDecimal amount = btr.getAnticipatedExpenseAmount();

        // 1) C-Level Management -> zawsze APPROVED
        boolean isCLevel = user.getRoles().stream()
                .anyMatch(r -> r.name().equals("CLEVEL"));

        if (isCLevel) {
            btr.setStatus("APPROVED");
        } else {
            // 2) amount <= threshold -> APPROVED
            if (amount.compareTo(approvalThreshold) <= 0) {
                btr.setStatus("APPROVED");
            }
            // 3) amount >= 2 * threshold -> REJECTED (próg 2x)
            else if (amount.compareTo(approvalThreshold.multiply(BigDecimal.valueOf(2))) >= 0) {
                btr.setStatus("REJECTED");
            }
            // 4) w środku -> PENDING (do Approvera)
            else {
                btr.setStatus("PENDING");
            }
        }

        btrRepository.save(btr);
        return "redirect:/btr/dashboard";
    }

    // Usuwanie własnego BTR
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        User current = currentUserService.getCurrentUser();

        BusinessTripRequest btr = btrRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("BTR not found: " + id));

        // użytkownik może usuwać tylko swoje BTR
        if (!btr.getUser().getId().equals(current.getId())) {
            throw new IllegalStateException("You cannot delete this BTR");
        }

        btrRepository.delete(btr);
        return "redirect:/btr/dashboard";
    }
}

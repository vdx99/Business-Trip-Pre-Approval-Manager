package com.vdx.BTR.controller;

import com.vdx.BTR.model.BusinessTripRequest;
import com.vdx.BTR.repository.BusinessTripRequestRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/approver")
public class ApproverController {

    private final BusinessTripRequestRepository btrRepository;

    public ApproverController(BusinessTripRequestRepository btrRepository) {
        this.btrRepository = btrRepository;
    }

    // Dashboard Approvera: wszystkie PENDING (poza CLEVEL)
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<BusinessTripRequest> pending = btrRepository.findByStatus("PENDING");

        List<BusinessTripRequest> toApprove = pending.stream()
                .filter(btr -> btr.getUser() != null)
                .filter(btr -> btr.getUser().getRoles().stream()
                        .noneMatch(role -> role.name().equals("CLEVEL")))
                .collect(Collectors.toList());

        model.addAttribute("btrs", toApprove);
        return "approver-dashboard";
    }

    // Approve
    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id) {
        BusinessTripRequest btr = btrRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("BTR not found: " + id));

        btr.setStatus("APPROVED");
        btrRepository.save(btr);

        return "redirect:/approver/dashboard";
    }

    // Reject
    @PostMapping("/{id}/reject")
    public String reject(@PathVariable Long id) {
        BusinessTripRequest btr = btrRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("BTR not found: " + id));

        btr.setStatus("REJECTED");
        btrRepository.save(btr);

        return "redirect:/approver/dashboard";
    }

    // Delete
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        btrRepository.deleteById(id);
        return "redirect:/approver/dashboard";
    }
}

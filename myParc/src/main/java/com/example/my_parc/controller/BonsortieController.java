package com.example.my_parc.controller;

import com.example.my_parc.domain.User;
import com.example.my_parc.model.BonsortieDTO;
import com.example.my_parc.repos.UserRepository;
import com.example.my_parc.service.BonsortieService;
import com.example.my_parc.util.CustomCollectors;
import com.example.my_parc.util.JsonStringFormatter;
import com.example.my_parc.util.ReferencedWarning;
import com.example.my_parc.util.WebUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


//@Controller
//@RequestMapping("/bonsorties")
//public class BonsortieController {
//
//    private final BonsortieService bonsortieService;
//    private final ObjectMapper objectMapper;
//    private final UserRepository userRepository;
//
//    public BonsortieController(final BonsortieService bonsortieService,
//            final ObjectMapper objectMapper, final UserRepository userRepository) {
//        this.bonsortieService = bonsortieService;
//        this.objectMapper = objectMapper;
//        this.userRepository = userRepository;
//    }
//
//    @InitBinder
//    public void jsonFormatting(final WebDataBinder binder) {
//        binder.addCustomFormatter(new JsonStringFormatter<List<String>>(objectMapper) {
//        }, "listeArticles");
//    }
//
//    @ModelAttribute
//    public void prepareContext(final Model model) {
//        model.addAttribute("userIdValues", userRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(User::getId, User::getNom)));
//    }
//
//    @GetMapping
//    public String list(final Model model) {
//        model.addAttribute("bonsorties", bonsortieService.findAll());
//        return "bonsortie/list";
//    }
//
//    @GetMapping("/add")
//    public String add(@ModelAttribute("bonsortie") final BonsortieDTO bonsortieDTO) {
//        return "bonsortie/add";
//    }
//
//    @PostMapping("/add")
//    public String add(@ModelAttribute("bonsortie") @Valid final BonsortieDTO bonsortieDTO,
//            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
//        if (bindingResult.hasErrors()) {
//            return "bonsortie/add";
//        }
//        bonsortieService.create(bonsortieDTO);
//        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("bonsortie.create.success"));
//        return "redirect:/bonsorties";
//    }
//
//    @GetMapping("/edit/{numero}")
//    public String edit(@PathVariable(name = "numero") final Long numero, final Model model) {
//        model.addAttribute("bonsortie", bonsortieService.get(numero));
//        return "bonsortie/edit";
//    }
//
//    @PostMapping("/edit/{numero}")
//    public String edit(@PathVariable(name = "numero") final Long numero,
//            @ModelAttribute("bonsortie") @Valid final BonsortieDTO bonsortieDTO,
//            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
//        if (bindingResult.hasErrors()) {
//            return "bonsortie/edit";
//        }
//        bonsortieService.update(numero, bonsortieDTO);
//        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("bonsortie.update.success"));
//        return "redirect:/bonsorties";
//    }
//
//    @PostMapping("/delete/{numero}")
//    public String delete(@PathVariable(name = "numero") final Long numero,
//            final RedirectAttributes redirectAttributes) {
//        final ReferencedWarning referencedWarning = bonsortieService.getReferencedWarning(numero);
//        if (referencedWarning != null) {
//            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
//                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
//        } else {
//            bonsortieService.delete(numero);
//            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("bonsortie.delete.success"));
//        }
//        return "redirect:/bonsorties";
//    }
//
//}

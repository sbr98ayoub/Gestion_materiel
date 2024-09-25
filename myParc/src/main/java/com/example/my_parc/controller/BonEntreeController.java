package com.example.my_parc.controller;

import com.example.my_parc.domain.User;
import com.example.my_parc.model.BonEntreeDTO;
import com.example.my_parc.repos.UserRepository;
import com.example.my_parc.service.BonEntreeService;
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


@Controller
@RequestMapping("/bonEntrees")
public class BonEntreeController {
//
//    private final BonEntreeService bonEntreeService;
//    private final ObjectMapper objectMapper;
//    private final UserRepository userRepository;
//
//    public BonEntreeController(final BonEntreeService bonEntreeService,
//            final ObjectMapper objectMapper, final UserRepository userRepository) {
//        this.bonEntreeService = bonEntreeService;
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
//        model.addAttribute("bonEntrees", bonEntreeService.findAll());
//        return "bonEntree/list";
//    }
//
//    @GetMapping("/add")
//    public String add(@ModelAttribute("bonEntree") final BonEntreeDTO bonEntreeDTO) {
//        return "bonEntree/add";
//    }
//
//    @PostMapping("/add")
//    public String add(@ModelAttribute("bonEntree") @Valid final BonEntreeDTO bonEntreeDTO,
//            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
//        if (bindingResult.hasErrors()) {
//            return "bonEntree/add";
//        }
//        bonEntreeService.create(bonEntreeDTO);
//        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("bonEntree.create.success"));
//        return "redirect:/bonEntrees";
//    }
//
//    @GetMapping("/edit/{numero}")
//    public String edit(@PathVariable(name = "numero") final Long numero, final Model model) {
//        model.addAttribute("bonEntree", bonEntreeService.get(numero));
//        return "bonEntree/edit";
//    }
//
//    @PostMapping("/edit/{numero}")
//    public String edit(@PathVariable(name = "numero") final Long numero,
//            @ModelAttribute("bonEntree") @Valid final BonEntreeDTO bonEntreeDTO,
//            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
//        if (bindingResult.hasErrors()) {
//            return "bonEntree/edit";
//        }
//        bonEntreeService.update(numero, bonEntreeDTO);
//        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("bonEntree.update.success"));
//        return "redirect:/bonEntrees";
//    }
//
//    @PostMapping("/delete/{numero}")
//    public String delete(@PathVariable(name = "numero") final Long numero,
//            final RedirectAttributes redirectAttributes) {
//        final ReferencedWarning referencedWarning = bonEntreeService.getReferencedWarning(numero);
//        if (referencedWarning != null) {
//            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
//                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
//        } else {
//            bonEntreeService.delete(numero);
//            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("bonEntree.delete.success"));
//        }
//        return "redirect:/bonEntrees";
//    }

}

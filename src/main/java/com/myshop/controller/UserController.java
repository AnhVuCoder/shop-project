package com.myshop.controller;

import com.myshop.config.Common;
import com.myshop.entity.Role;
import com.myshop.entity.User;
import com.myshop.repository.RoleRepository;
import com.myshop.service.UserService;
import com.myshop.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @GetMapping("/users")
    public String listAllUser(
            Model model){
        return pageUser(1,model,"firstName","asc","");
    }
    @GetMapping("/users/create")
    public String createNewUserPage(Model model){
        List<Role> roles=roleRepository.findAll();
        User user=new User();
        user.setEnabled(true);
        model.addAttribute("pageTitle","Create new user");
        model.addAttribute("listRoles",roles);
        model.addAttribute("users",user);
        return "user_form";
    }
    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("users") User user, RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile multipartFile) throws IOException {
       if(!multipartFile.isEmpty()) {
           String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
           user.setPhotos(fileName);
           userService.saveUser(user);
           String uploadDir = "user-photos/" + user.getId();
           FileUploadUtil.deleteDir(uploadDir);
           FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
       } else userService.saveUser(user);
         redirectAttributes.addFlashAttribute("message","Save user successfully");
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String updateUser(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes){
        User user;
        try{
           user=userService.findById(id);
            List<Role> list=roleRepository.findAll();
            model.addAttribute("users",user);
            model.addAttribute("pageTitle","Edit user");
            model.addAttribute("listRoles",list);
            return "user_form";
        } catch (UsernameNotFoundException ex){
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        try{
            userService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Delete user with id "+id+" successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Delete fail");
        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/{enabled}")
    public String editEnabledUser(@PathVariable("id") Integer id,
                                  @PathVariable("enabled") boolean enabled,
                                  RedirectAttributes redirectAttributes){
        try{
            userService.editEnabled(id,enabled);
            redirectAttributes.addFlashAttribute("message","Edit enabled user with id "+id+" successfully");
            return "redirect:/users";
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("message","Error");
        }
        return "redirect:/users";
    }
    @GetMapping("/users/page/{pageNo}")
    public String pageUser(@PathVariable("pageNo") Integer pageNo,
                           Model model,
                           @RequestParam("sortField") String sortField,
                           @RequestParam("sortDir") String sortDir,
                           @RequestParam("keyword") String keyword){
        Page<User> page=userService.pageNoUser(pageNo, sortField, sortDir,keyword);
        int totalPage= (int) page.getTotalPages();
        List<User> listPageUser=page.getContent();
        int startPage=(pageNo-1)*Common.PAGE_SIZE+1;
        int endPage=startPage+Common.PAGE_SIZE-1;
        if(endPage>page.getTotalElements()){
            endPage= (int) page.getTotalElements();
        }
        String reverseSortDir=sortDir.equals("asc")?"desc":"asc";
        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalElement",page.getTotalElements());
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("users",listPageUser);
        model.addAttribute("listUsers",new ArrayList<User>());
        model.addAttribute("lastPage",totalPage);
        model.addAttribute("reverseSortDir",reverseSortDir);
        model.addAttribute("keyword",keyword);
        return "users";
    }
}

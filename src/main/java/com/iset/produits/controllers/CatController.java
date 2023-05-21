package com.iset.produits.controllers;

import com.iset.produits.dao.RoleRepository;
import com.iset.produits.dao.UserRepository;
import com.iset.produits.entities.Categorie;
import com.iset.produits.entities.Produit;
import com.iset.produits.entities.Role;
import com.iset.produits.entities.User;
import com.iset.produits.service.CategorieService;
import com.iset.produits.service.ProduitService;
import com.iset.produits.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;


@Controller
public class CatController {
    @Autowired
    ProduitService produitService;
    @Autowired
    CategorieService categorieService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @RequestMapping("/showCreate")
    public String showCreate(@RequestParam(name = "id", defaultValue = "") Long id, ModelMap modelMap)
    {
        if (id==null){
        modelMap.addAttribute("produit", new Produit());
        modelMap.addAttribute("categories", categorieService.getAll());
        modelMap.addAttribute("update", false);
       }
        else{
            Produit p= produitService.getProduit(id);
            modelMap.addAttribute("produit", p);
            modelMap.addAttribute("update", true);
        } return "createProduit";
    }
    @RequestMapping("/saveProduit")
    public String saveProduit(@Valid Produit produit,@RequestParam("categorie") long catid, @RequestParam(value = "update", required = false) boolean isUpdate,
                              BindingResult bindingResult,
                              ModelMap modelMap) {
        if (bindingResult.hasErrors()) {
            return "createProduit";
        }
        if(isUpdate){
            Produit updatedProduit = produitService.updateProduit(produit);
            String msg = "produit modifié avec Id " + updatedProduit.getIdProduit();
            modelMap.addAttribute("msg", msg);
            return "listeProduits";}
        else{
            System.out.println(catid);
            System.out.println("hello" + produit.getNomProduit());
            produit.setCategorie(categorieService.getCategorie(catid));
            Produit saveProduit = produitService.saveProduit(produit);
            String msg = "produit enregistré avec Id " + saveProduit.getIdProduit();
            modelMap.addAttribute("msg", msg);
            return "createProduit";}
    }
    @RequestMapping("/signup")
    public String signup(){
        return "signup";
    }
    @RequestMapping("/ListeProduits")
    public String listeProduits(
            ModelMap modelMap,@RequestParam(name = "nom", defaultValue = "") String nom,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "6") int size)
    {
        Page<Produit> prods = produitService.getAllProduitsParPage(nom,page, size);
        modelMap.addAttribute("produits", prods);
        modelMap.addAttribute("pages", new int[prods.getTotalPages()]);
        modelMap.addAttribute("currentPage", page);
        modelMap.addAttribute("nom", nom);
        return "listeProduits";
    }
    @RequestMapping("/supprimerProduit")
    public String supprimerProduit(@RequestParam("id") Long id, ModelMap
            modelMap, @RequestParam(name = "nom", defaultValue = "") String nom,
                                   @RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "size", defaultValue = "6") int size) {
        produitService.deleteProduitById(id);
        Page<Produit> prods = produitService.getAllProduitsParPage(nom,page, size);
        modelMap.addAttribute("produits", prods);
        modelMap.addAttribute("pages", new int[prods.getTotalPages()]);
        modelMap.addAttribute("currentPage", page);
        modelMap.addAttribute("size", size);
        modelMap.addAttribute("nom", nom);
        return "listeProduits";
    }


    @RequestMapping("/rechercherProduit")
    public String rechercherProduit(@RequestParam("nom") String nom, ModelMap modelMap,
                                    @RequestParam(name = "page", defaultValue = "0") int page,
                                    @RequestParam(name = "size", defaultValue = "6") int size) {
        Page<Produit> prods = produitService.findByNomProduit(nom,PageRequest.of(page, size));
        modelMap.addAttribute("produits", prods);
        modelMap.addAttribute("pages", new int[prods.getTotalPages()]);
        modelMap.addAttribute("currentPage", page);
        modelMap.addAttribute("size", size);
        modelMap.addAttribute("nom", nom);
        return "listeProduits";
    }



    @RequestMapping("/showCategorie")
    public String showCategorie(ModelMap modelMap)
    {
        modelMap.addAttribute("categorie", new Categorie());
        return "createCategorie";
    }
    @RequestMapping("/saveCategorie")
    public String saveCategorie(@ModelAttribute("categorie") Categorie categorie,
                                ModelMap modelMap) throws ParseException {
        Categorie saveCategorie = categorieService.saveCategorie(categorie);
        String msg = "categorie enregistré avec Id " + saveCategorie.getIdCat();
        modelMap.addAttribute("msg", msg);
        return "createCategorie";
    }

    @RequestMapping("/ListeCategories")
    public String listeCategories(
            ModelMap modelMap,@RequestParam(name = "nom", defaultValue = "") String nom,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "6") int size)
    {
        Page<Categorie> cats = categorieService.getAllCategorieParPage(nom,page, size);
        modelMap.addAttribute("categories", cats);
        modelMap.addAttribute("pages", new int[cats.getTotalPages()]);
        modelMap.addAttribute("currentPage", page);
        modelMap.addAttribute("nom", nom);
        return "listeCategories";
    }
    @RequestMapping("/supprimerCategorie")
    public String supprimerCategorie(@RequestParam("id") Long id, ModelMap
            modelMap, @RequestParam(name = "nom", defaultValue = "") String nom,
                                   @RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "size", defaultValue = "6") int size) {
        categorieService.deleteCategorieById(id);
        Page<Categorie> cats = categorieService.getAllCategorieParPage(nom,page, size);
        modelMap.addAttribute("categories", cats);
        modelMap.addAttribute("pages", new int[cats.getTotalPages()]);
        modelMap.addAttribute("currentPage", page);
        modelMap.addAttribute("size", size);
        modelMap.addAttribute("nom", nom);
        return "listeCategories";
    }

    @RequestMapping("/modifierCategorie")
    public String editerCategorie(@RequestParam("id") Long id,ModelMap modelMap)
    {
        Categorie c= categorieService.getCategorie(id);
        modelMap.addAttribute("categorie", c);
        return "editerCategorie";
    }
    @RequestMapping("/updateCategorie")
    public String updateCategorie(@ModelAttribute("categorie") Categorie categorie,@RequestParam(name = "page", defaultValue = "0") int page,
                                @RequestParam(name = "size", defaultValue = "6") int size,
                                @RequestParam(name = "nom", defaultValue = "") String nom,
                                ModelMap modelMap)
    {
        categorieService.updateCategorie(categorie);
        Page<Categorie> prods = categorieService.getAllCategorieParPage(nom,page, size);
        modelMap.addAttribute("categories", prods);
        modelMap.addAttribute("pages", new int[prods.getTotalPages()]);
        modelMap.addAttribute("currentPage", page);
        modelMap.addAttribute("size", size);
        modelMap.addAttribute("nom", nom);
        return "listeCategories";
    }
    @RequestMapping("/rechercherCategorie")
    public String rechercherCategorie(@RequestParam("nom") String nom, ModelMap modelMap,
                                    @RequestParam(name = "page", defaultValue = "0") int page,
                                    @RequestParam(name = "size", defaultValue = "6") int size) {
        Page<Categorie> cats = categorieService.findByNomCat(nom,PageRequest.of(page, size));
        modelMap.addAttribute("categories", cats);
        modelMap.addAttribute("pages", new int[cats.getTotalPages()]);
        modelMap.addAttribute("currentPage", page);
        modelMap.addAttribute("size", size);
        modelMap.addAttribute("nom", nom);
        return "listeCategories";
    }
    @PostMapping("/addUser")
    public String addUser(@RequestParam("name") String name,@RequestParam("password") String password , @RequestParam("Role") String role) {
        //userService.saveUser(name,password,role);
        User user = new User();
        user.setUsername(name);
        user.setPassword(password);
        Role role1  = new Role();
        role1.setName(role);
        roleRepository.save(role1);
        Set<Role> roles = new HashSet<>();
        roles.add(role1);
        user.setRoles(roles);
        userRepository.save(user);
        return "login";
    }
}

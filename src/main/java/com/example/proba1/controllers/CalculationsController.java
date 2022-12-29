package com.example.servingwebcontent.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import com.example.servingwebcontent.models.*;

@Controller
public class CalculationsController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Simply selects the home view to render by returning its name.
     */
    @GetMapping("/calculations/list")
    public String list(Model model) {

        String sql = "SELECT * FROM calculations";
        List<Calculation> calculations = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Calculation.class));

        model.addAttribute("calculations", calculations);
        return "calculations/list";
    }

    @GetMapping("/calculations/get/{id}")
    public String get(@PathVariable int id, Model model) {

        String sql = "SELECT * FROM calculations WHERE CalculationId = " + id;
        List<Calculation> calculations = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Calculation.class));
        String sql2 = "SELECT * FROM menus WHERE MenuId = " + id;
        List<Menu> menus = jdbcTemplate.query(sql2,
                BeanPropertyRowMapper.newInstance(Menu.class));

        String sql3 = "SELECT Products.ProductId, Products.Title, Products.Calories FROM Products INNER JOIN MenusProducts ON MenusProducts.ProductId = Products.ProductId WHERE MenuId = " + id;
        List<Product> products = jdbcTemplate.query(sql3,
                BeanPropertyRowMapper.newInstance(Product.class));
        menus.get(0).setProductsObj(products);

        model.addAttribute("products", products);
        model.addAttribute("totalCalories",  menus.get(0).getTotalCalories());
        model.addAttribute("item", calculations.get(0));
        return "calculations/details";
    }

    @GetMapping("/calculations") //the add page will act as an index!
    public String add(Model model)
    {
        Calculation calculation = new Calculation();
        String sql = "SELECT * FROM menus";
        List<Menu> menus = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Menu.class));

        model.addAttribute("allMenus", menus);
        model.addAttribute("calculation", calculation);

        return "calculations/index";
    }
    @RequestMapping(value = "/calculations/add", method = RequestMethod.POST)
    public ModelAndView add(@ModelAttribute("calculation") Calculation calculation, Model model)
    {
        model.addAttribute("item", calculation);
        String sql = "INSERT INTO calculations (Height, Weight, Age, CaloriesIntake, ExtraCalories, MenuId) VALUES(?, ?, ?, ?, ?, ?);";

        //1. get menu
        int menuId = calculation.getMenuId();

        String sql2 = "SELECT * FROM menus WHERE MenuId = " + menuId;
        List<Menu> menus = jdbcTemplate.query(sql2,
                BeanPropertyRowMapper.newInstance(Menu.class));

        String sql3 = "SELECT Products.ProductId, Products.Title, Products.Calories FROM Products INNER JOIN MenusProducts ON MenusProducts.ProductId = Products.ProductId WHERE MenuId = " + menuId;
        List<Product> products = jdbcTemplate.query(sql3,
                BeanPropertyRowMapper.newInstance(Product.class));
        menus.get(0).setProductsObj(products);

        //2. calculate total calories
        Integer totalCalories = menus.get(0).getTotalCalories();

        //3. calculate required calories
        Integer requiredCalories = (int) (calculation.getWeight()*26.4);

        //4. calculate extra (if any)
        Integer extra = (requiredCalories >= totalCalories ? 0 : (totalCalories - requiredCalories));

        jdbcTemplate.update(sql, calculation.getHeight(), calculation.getWeight(), calculation.getAge(), totalCalories, extra, menuId);

        return new ModelAndView("redirect:/calculations/list");
    }

    @GetMapping("/calculations/delete/{id}")
    public ModelAndView delete(@PathVariable int id, Model model)
    {
        String sql = "SELECT * FROM calculations WHERE CalculationId = " + id;
        List<Calculation> calculations = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Calculation.class));

        if(calculations.size() > 0)
        {
            String sqlDelete = "DELETE FROM calculations WHERE CalculationId = ?";
            jdbcTemplate.update(sqlDelete, id);

            return new ModelAndView("redirect:/calculations/list");
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
    }
}
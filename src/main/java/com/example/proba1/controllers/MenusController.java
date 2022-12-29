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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.*;
import com.example.servingwebcontent.models.Menu;
import com.example.servingwebcontent.models.Product;

@Controller
public class MenusController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    /**
     * Simply selects the home view to render by returning its name.
     */
    @GetMapping("/menus")
    public String index(Model model) {

        String sql = "SELECT * FROM menus";
        List<Menu> menus = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Menu.class));
        for (Menu menu : menus) {
            String sql2 = "SELECT Products.ProductId, Products.Title, Products.Calories FROM Products INNER JOIN MenusProducts ON MenusProducts.ProductId = Products.ProductId WHERE MenuId = " + menu.getMenuId();
            List<Product> products = jdbcTemplate.query(sql2,
                    BeanPropertyRowMapper.newInstance(Product.class));
            menu.setProductsObj(products);
        }
        model.addAttribute("menus", menus);
        return "menus/index";
    }

    @GetMapping("/menus/get/{id}")
    public String get(@PathVariable int id, Model model) {

        String sql = "SELECT * FROM menus WHERE MenuId = " + id;
        List<Menu> menus = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Menu.class));

        String sql2 = "SELECT Products.ProductId, Products.Title, Products.Calories FROM Products INNER JOIN MenusProducts ON MenusProducts.ProductId = Products.ProductId WHERE MenuId = " + id;
        List<Product> products = jdbcTemplate.query(sql2,
                BeanPropertyRowMapper.newInstance(Product.class));
        model.addAttribute("products", products);
        model.addAttribute("item", menus.get(0));
        return "menus/details";
    }

    @GetMapping("/menus/add")
    public String add(Model model)
    {
        Menu menu = new Menu();
        String sql = "SELECT * FROM products";
        List<Product> products = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Product.class));

        model.addAttribute("allProducts", products);

        model.addAttribute("menu", menu);

        return "menus/add";
    }

    @RequestMapping(value = "/menus/add", method = RequestMethod.POST)
    public ModelAndView add(@ModelAttribute("menu") Menu menu, Model model)
    {
        model.addAttribute("item", menu);
        String sql = "INSERT INTO menus (title) VALUES(:title);";

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        Map<String, Object> params = new HashMap<>();
        params.put("title", menu.getTitle());

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(params), generatedKeyHolder);
        Integer menuId = generatedKeyHolder.getKey().intValue();

        for (Integer productId : menu.getProducts()) {
            String sql3 = "INSERT INTO MenusProducts (MenuId, ProductId) VALUES(?, ?);";

            jdbcTemplate.update(sql3, menuId, productId);
        }

        return new ModelAndView("redirect:/menus");
    }

    @GetMapping("/menus/edit/{id}")
    public String edit(@PathVariable int id, Model model)
    {
        String sql = "SELECT * FROM menus WHERE MenuId = " + id;
        List<Menu> menus = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Menu.class));
        String sql2 = "SELECT Products.ProductId FROM Products INNER JOIN MenusProducts ON MenusProducts.ProductId = Products.ProductId WHERE MenuId = " + id;
        List<Integer> menuProducts = jdbcTemplate.queryForList(sql2,Integer.class);
        menus.get(0).setProducts(menuProducts);

        String sql3 = "SELECT * FROM products";
        List<Product> products = jdbcTemplate.query(sql3, BeanPropertyRowMapper.newInstance(Product.class));

        model.addAttribute("allProducts", products);
        model.addAttribute("menu", menus.get(0));

        return "menus/edit";
    }

    @RequestMapping(value = "/menus/edit", method = RequestMethod.POST)
    public ModelAndView edit(@ModelAttribute("menu") Menu menu, Model model)
    {
        model.addAttribute("item", menu);
        String sql = "UPDATE menus  SET title = ? WHERE MenuId = ?;";

        jdbcTemplate.update(sql, menu.getTitle(), menu.getMenuId());

        String sql2 = "DELETE FROM MenusProducts WHERE MenuId = ?";
        jdbcTemplate.update(sql2, menu.getMenuId());

        for (Integer productId : menu.getProducts()) {
            String sql3 = "INSERT INTO MenusProducts (MenuId, ProductId) VALUES(?, ?);";

            jdbcTemplate.update(sql3, menu.getMenuId(), productId);
        }

        return new ModelAndView("redirect:/menus");
    }

    @GetMapping("/menus/delete/{id}")
    public ModelAndView delete(@PathVariable int id, Model model)
    {
        String sql = "SELECT * FROM menus WHERE MenuId = " + id;
        List<Menu> menus = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Menu.class));

        if(menus.size() > 0)
        {
            String sqlDelete = "DELETE FROM menus WHERE MenuId = ?";
            jdbcTemplate.update(sqlDelete, id);

            return new ModelAndView("redirect:/menus");
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
    }
}
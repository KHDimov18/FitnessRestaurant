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
import com.example.servingwebcontent.models.Product;

@Controller
public class ProductsController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Simply selects the home view to render by returning its name.
     */
    @GetMapping("/products")
    public String index(Model model) {

        String sql = "SELECT * FROM products";
        List<Product> products = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Product.class));

        model.addAttribute("products", products);
        return "products/index";
    }

    @GetMapping("/products/get/{id}")
    public String get(@PathVariable int id, Model model) {

        String sql = "SELECT * FROM products WHERE ProductId = " + id;
        List<Product> products = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Product.class));

        model.addAttribute("item", products.get(0));
        return "products/details";
    }

    @GetMapping("/products/add")
    public String add(Model model)
    {
        Product product = new Product();
        model.addAttribute("product", product);

        return "products/add";
    }

    @RequestMapping(value = "/products/add", method = RequestMethod.POST)
    public ModelAndView add(@ModelAttribute("product") Product product, Model model)
    {
        model.addAttribute("item", product);
        String sql = "INSERT INTO products (title, calories) VALUES(?, ?);";

        jdbcTemplate.update(sql, product.getTitle(), product.getCalories());

        return new ModelAndView("redirect:/products");
    }

    @GetMapping("/products/edit/{id}")
    public String edit(@PathVariable int id, Model model)
    {
        String sql = "SELECT * FROM products WHERE ProductId = " + id;
        List<Product> products = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Product.class));

        model.addAttribute("product", products.get(0));

        return "products/edit";
    }

    @RequestMapping(value = "/products/edit", method = RequestMethod.POST)
    public ModelAndView edit(@ModelAttribute("product") Product product, Model model)
    {
        model.addAttribute("item", product);
        String sql = "UPDATE products  SET title = ?, calories = ? WHERE ProductId = ?;";

        jdbcTemplate.update(sql, product.getTitle(), product.getCalories(), product.getProductId());

        return new ModelAndView("redirect:/products");
    }

    @GetMapping("/products/delete/{id}")
    public ModelAndView delete(@PathVariable int id, Model model)
    {
        String sql = "SELECT * FROM products WHERE ProductId = " + id;
        List<Product> products = jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(Product.class));

        if(products.size() > 0)
        {
            String sqlDelete = "DELETE FROM products WHERE ProductId = ?";
            jdbcTemplate.update(sqlDelete, id);

            return new ModelAndView("redirect:/products");
        } else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
    }
}
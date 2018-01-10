package com.cdsxt.web.cntroller;

import com.cdsxt.dao.CategoryDao;
import com.cdsxt.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("products")
@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryDao categoryDao;

    // 商城首页
    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) {
        // 进入商城首页后, 将商品分类查询出来放在 session 中
        request.getSession().setAttribute("allCategories", this.categoryDao.selectAllCategory());
        request.getSession().setAttribute("allSecondCategories", this.categoryDao.selectAllCategorySecond());
        // 查询所有所最热商品
        model.addAttribute("hotProducts", this.productService.selectAllHotProducts());
        // 查询所有最新商品
        model.addAttribute("newProducts", this.productService.selectAllNewProducts());
        return "front/index";
    }

    // 商品详情页
    @RequestMapping(value = "selectById", method = RequestMethod.GET)
    public String selectById(@RequestParam(value = "pid") Integer pid, Model model) {
        // 查询当前商品
        model.addAttribute("curProduct", this.productService.selectById(pid));
        // 进入该商品详情页
        return "front/product";
    }

    // 根据商品名称, 商品价格, 商品类型进行搜索
    @RequestMapping(value = "selectProductWithParam", method = RequestMethod.POST)
    public String selectProductWithParam(String productName, Double minPrice, Double maxPrice, String categoryName, Model model) {
        model.addAttribute("searchProducts", this.productService.selectProductWithParam(productName, minPrice, maxPrice, categoryName));
        return "front/searchResult";
    }

    // 根据商品种类二级分类 id 进行查询
    @RequestMapping(value = "selectProductWithCategorySecond", method = RequestMethod.GET)
    public String selectProductWithCategorySecond(Integer csid, Model model) {
        model.addAttribute("searchProducts", this.productService.selectProductWithCategorySecond(csid));
        return "front/searchResult";
    }

    // 根据商品一级分类 id 进行查询
    @RequestMapping(value = "selectProductWithCategory", method = RequestMethod.GET)
    public String selectProductWithCategory(Integer cid, Model model) {
        model.addAttribute("searchProducts", this.productService.selectProductWithCategory(cid));
        return "front/searchResult";
    }
}

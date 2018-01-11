import com.cdsxt.dao.CategoryDao;
import com.cdsxt.dao.ProductDao;
import com.cdsxt.dao.impl.CategoryDaoImpl;
import com.cdsxt.dao.impl.ProductDaoImpl;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class TestProduct {

    @Test
    public void testSelectAll() {
        ProductDao productDao = new ProductDaoImpl();
        System.out.println(productDao.selectAll());
    }

    @Test
    public void testSelectOne() {
        ProductDao productDao = new ProductDaoImpl();
        System.out.println(productDao.selectProductWithCategorySecond(1));
    }

    @Test
    public void testCategory() {
        CategoryDao categoryDao = new CategoryDaoImpl();
        System.out.println(categoryDao.selectAllCategory());
        System.out.println(categoryDao.selectAllCategorySecond());
    }

    @Test
    public void testSelectWithParam() {
        ProductDao ps = new ProductDaoImpl();
        Map<String, Object> params = new HashMap<>();
        params.put("productName", "男");
        params.put("minPrice", 200);
        params.put("maxPrice", 400);
        params.put("categoryName", "裤子");
        System.out.println(ps.selectProductWithParam(params));
    }
}

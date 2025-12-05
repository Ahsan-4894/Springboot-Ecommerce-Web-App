package com.zepox.EcommerceWebApp.repository;

import com.zepox.EcommerceWebApp.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Item, String> {
    @Query("""
            SELECT I FROM Item I JOIN I.category C WHERE C.categoryName in :names
           """)
    List<Item> findByCategoryNames(@Param("names") List<String> names);

    @Query("""
        SELECT I FROM Item I JOIN I.category C WHERE I.price BETWEEN :low AND :high AND I.quantity > 0
        """)
    Page<Item> findByPriceRange(@Param("low") double low, @Param("high") double high, Pageable pageable);

    @Query("""
        SELECT I FROM Item I JOIN I.category C WHERE C.categoryName = :category AND I.price BETWEEN :low AND :high AND I.quantity > 0
        """)
    Page<Item> findByPriceRangeAndCategoryName(@Param("category") String category, @Param("low") double low, @Param("high") double high, Pageable pageable);

    @Query("""
       SELECT I FROM Item I
       JOIN I.category C
       WHERE (:name IS NULL OR I.name ILIKE :name)
         AND (:category IS NULL OR C.categoryName = :category)
         AND (
            :available IS NULL OR
            (:available = 'stock' AND I.quantity > 0) OR
            (:available = 'outofstock' AND I.quantity = 0)
         )
         AND (
            (:priceLower = -1 AND :priceUpper = -1)
            OR (I.price BETWEEN :priceLower AND :priceUpper)
            OR (:priceLower != -1 AND :priceUpper = -1 AND I.price >= :priceLower)
            OR (:priceUpper != -1 AND :priceLower = -1 AND I.price <= :priceUpper)
         )
      """)
    List<Item> searchProducts(@Param("name") String name,
                              @Param("category") String category,
                              @Param("available") String available,
                              @Param("priceLower") double priceLower,
                              @Param("priceUpper") double priceUpper
                              );

    @Query(value = """
        SELECT I.*, O.total_sold_units
        FROM items I 
        JOIN (
            SELECT item_id, SUM(OI.quantity) as total_sold_units
            FROM orderitems OI 
            JOIN orders ORD ON OI.order_id = ORD.id
            WHERE ORD.status = 'DELIVERED'
            GROUP BY item_id
            HAVING SUM(OI.quantity) > :bestSoldProductsLimit
        ) O ON I.id = O.item_id
        LIMIT 10
        """,
            nativeQuery = true)
    List<Item> findBestSellingProducts(@Param("bestSoldProductsLimit") int bestSoldProductsLimit);

    @Query(value = """
         SELECT
                   I.*,
                   C.categ_name
                 FROM(
                     SELECT item_id FROM feedback
                     GROUP BY item_id
                     HAVING AVG(rating)>3
                     ORDER BY AVG(rating) DESC
                   ) AS best_reviewed_items_ids
                   JOIN items I on I.id = best_reviewed_items_ids.item_id
                   JOIN categories C ON I.categoryid = C.categ_id;
       """, nativeQuery = true)
    List<Item> findBestReviewedProducts();

    @Query("SELECT I FROM Item I WHERE I.id IN :ids")
    List<Item> findProductsByIds(@Param("ids") List<String> ids);
    
}

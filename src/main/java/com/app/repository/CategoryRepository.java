package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.pojo.Category;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
	
	public Category findByCategoryName(String name);
    @Query("SELECT c FROM Category c WHERE c.categoryName = :name AND c.parentCategory.categoryName = :parentCategoryName")
	public Category findByNameAndParent(@Param("name") String name,@Param("parentCategoryName") String parentCategoryName);
//    @Query("SELECT c FROM Category c WHERE c.categoryName = :name AND c.parentCategory = :parentCategory")
//	public Category findByNameAndParent(@Param("name") String name,@Param("parentCategory") Category parentCategory);

}

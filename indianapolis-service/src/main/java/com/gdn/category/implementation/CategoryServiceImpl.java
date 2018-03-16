package com.gdn.category.implementation;

import com.gdn.entity.Category;
import com.gdn.category.CategoryService;
import com.gdn.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void saveDefaultCategory() {
        Category categoryCamera = Category.builder()
                .id("category_camera")
                .name("Camera")
                .build();
        Category categoryComputer = Category.builder()
                .id("category_computer")
                .name("Computer")
                .build();
        Category categoryElectronic = Category.builder()
                .id("category_electronic")
                .name("Electronic")
                .build();
        Category categoryFashionPria = Category.builder()
                .id("category_fashion_pria")
                .name("Fashion Pria")
                .build();
        Category categoryFashionWanita = Category.builder()
                .id("category_fashion_wanita")
                .build();
        Category categoryGaleriIndonesia = Category.builder()
                .id("category_galeri_indonesia")
                .name("Galeri Indonesia")
                .build();
        Category categoryGroceries = Category.builder()
                .id("category_groceries")
                .name("Groceries")
                .build();
        Category categoryHandphone = Category.builder()
                .id("category_handphone")
                .name("Handphone")
                .build();
        Category categoryHomeAndLiving = Category.builder()
                .id("category_home&living")
                .name("Home & Living")
                .build();
        Category categoryIbuDanAnak = Category.builder()
                .id("category_ibu&anak")
                .name("Ibu & Anak")
                .build();
        Category categoryKesehatanDanKecantikan = Category.builder()
                .id("category_kesehatan&kecantikan")
                .name("Kesehatan & Kecantikan")
                .build();
        Category categoryMainanDanVideoGames = Category.builder()
                .id("category_mainan&videogames")
                .name("Mainan & Video Games")
                .build();
        Category categoryOtomotif = Category.builder()
                .id("category_otomotif")
                .name("Otomotif")
                .build();
        Category categorySport = Category.builder()
                .id("category_sport")
                .name("Sport")
                .build();
        Category categoryTvDanDigital = Category.builder()
                .id("category_tv&digital")
                .name("TV & Digital")
                .build();
        categoryRepository.save(categoryCamera); categoryRepository.save(categoryComputer); categoryRepository.save(categoryElectronic);
        categoryRepository.save(categoryFashionPria); categoryRepository.save(categoryFashionWanita); categoryRepository.save(categoryGaleriIndonesia);
        categoryRepository.save(categoryGroceries); categoryRepository.save(categoryHandphone); categoryRepository.save(categoryHomeAndLiving);
        categoryRepository.save(categoryIbuDanAnak); categoryRepository.save(categoryKesehatanDanKecantikan); categoryRepository.save(categoryMainanDanVideoGames);
        categoryRepository.save(categoryOtomotif); categoryRepository.save(categorySport); categoryRepository.save(categoryTvDanDigital);
    }
}

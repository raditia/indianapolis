package com.gdn.warehouse.category.implementation;

import com.gdn.entity.Category;
import com.gdn.entity.Warehouse;
import com.gdn.entity.WarehouseCategory;
import com.gdn.repository.WarehouseCategoryRepository;
import com.gdn.warehouse.category.WarehouseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarehouseCategoryServiceImpl implements WarehouseCategoryService{

    @Autowired
    WarehouseCategoryRepository warehouseCategoryRepository;

    @Override
    public void addDefaultWarehouseCategoryInformation() {
        WarehouseCategory warehouseCategoryCamera = WarehouseCategory.builder()
                .id("warehouse_category_cawang_camera")
                .warehouse(Warehouse.builder()
                        .id("warehouse_cawang")
                        .build())
                .category(Category.builder()
                        .id("category_camera")
                        .build())
                .build();
        WarehouseCategory warehouseCategoryComputer = WarehouseCategory.builder()
                .id("warehouse_category_cawang_computer")
                .warehouse(Warehouse.builder()
                        .id("warehouse_cawang")
                        .build())
                .category(Category.builder()
                        .id("category_computer")
                        .build())
                .build();
        WarehouseCategory warehouseCategoryElectronic = WarehouseCategory.builder()
                .id("warehouse_category_cakung_electronic")
                .warehouse(Warehouse.builder()
                        .id("warehouse_cakung")
                        .build())
                .category(Category.builder()
                        .id("category_electronic")
                        .build())
                .build();
        WarehouseCategory warehouseCategoryFashionPria = WarehouseCategory.builder()
                .id("warehouse_category_cakung_fashion_pria")
                .warehouse(Warehouse.builder()
                        .id("warehouse_cakung")
                        .build())
                .category(Category.builder()
                        .id("category_fashion_pria")
                        .build())
                .build();
        WarehouseCategory warehouseCategoryFashionWanita = WarehouseCategory.builder()
                .id("warehouse_category_cakung_fashion_wanita")
                .warehouse(Warehouse.builder()
                        .id("warehouse_cakung")
                        .build())
                .category(Category.builder()
                        .id("category_fashion_wanita")
                        .build())
                .build();
        WarehouseCategory warehouseCategoryGaleriIndonesia = WarehouseCategory.builder()
                .id("warehouse_category_cawang_galeri_indonesia")
                .warehouse(Warehouse.builder()
                        .id("warehouse_cakung")
                        .build())
                .category(Category.builder()
                        .id("category_galeri_indonesia")
                        .build())
                .build();
        WarehouseCategory warehouseCategoryGroceriesCeper = WarehouseCategory.builder()
                .id("warehouse_category_ceper_groceries")
                .warehouse(Warehouse.builder()
                        .id("warehouse_ceper")
                        .build())
                .category(Category.builder()
                        .id("category_groceries")
                        .build())
                .build();
        WarehouseCategory warehouseCategoryGroceriesCakung = WarehouseCategory.builder()
                .id("warehouse_category_cakung_groceries")
                .warehouse(Warehouse.builder()
                        .id("warehouse_cakung")
                        .build())
                .category(Category.builder()
                        .id("category_groceries")
                        .build())
                .build();
        WarehouseCategory warehouseCategoryHandphone = WarehouseCategory.builder()
                .id("warehouse_category_cawang_handphone")
                .warehouse(Warehouse.builder()
                        .id("warehouse_cawang")
                        .build())
                .category(Category.builder()
                        .id("category_handphone")
                        .build())
                .build();
        WarehouseCategory warehouseCategoryHomeAndLivingCakung = WarehouseCategory.builder()
                .id("warehouse_category_cakung_home&living")
                .warehouse(Warehouse.builder()
                        .id("warehouse_cakung")
                        .build())
                .category(Category.builder()
                        .id("category_home&living")
                        .build())
                .build();
        WarehouseCategory warehouseCategoryHomeAndLivingCeper = WarehouseCategory.builder()
                .id("warehouse_category_ceper_home&living")
                .warehouse(Warehouse.builder()
                        .id("warehouse_ceper")
                        .build())
                .category(Category.builder()
                        .id("category_home&living")
                        .build())
                .build();
        WarehouseCategory warehouseCategoryIbuDanAnak = WarehouseCategory.builder()
                .id("warehouse_category_ceper_ibu&anak")
                .warehouse(Warehouse.builder()
                        .id("warehouse_ceper")
                        .build())
                .category(Category.builder()
                        .id("category_ibu&anak")
                        .build())
                .build();
        WarehouseCategory warehouseCategoryKesehatanDanKecantikan = WarehouseCategory.builder()
                .id("warehouse_category_cakung_kesehatan&kecantikan")
                .warehouse(Warehouse.builder()
                        .id("warehouse_cakung")
                        .build())
                .category(Category.builder()
                        .id("category_kesehatan&kecantikan")
                        .build())
                .build();
        WarehouseCategory warehouseCategoryMainanDanVideoGames = WarehouseCategory.builder()
                .id("warehouse_category_cakung_mainan&videogames")
                .warehouse(Warehouse.builder()
                        .id("warehouse_cakung")
                        .build())
                .category(Category.builder()
                        .id("category_mainan&videogames")
                        .build())
                .build();
        WarehouseCategory warehouseCategoryOtomotif = WarehouseCategory.builder()
                .id("warehouse_category_cakung_otomotif")
                .warehouse(Warehouse.builder()
                        .id("warehouse_cakung")
                        .build())
                .category(Category.builder()
                        .id("category_otomotif")
                        .build())
                .build();
        WarehouseCategory warehouseCategorySport = WarehouseCategory.builder()
                .id("warehouse_category_cakung_sport")
                .warehouse(Warehouse.builder()
                        .id("warehouse_cakung")
                        .build())
                .category(Category.builder()
                        .id("category_sport")
                        .build())
                .build();
        WarehouseCategory warehouseCategoryTvDanDigital = WarehouseCategory.builder()
                .id("warehouse_category_cakung_tv&digital")
                .warehouse(Warehouse.builder()
                        .id("warehouse_cakung")
                        .build())
                .category(Category.builder()
                        .id("category_tv&digital")
                        .build())
                .build();
        warehouseCategoryRepository.save(warehouseCategoryCamera); warehouseCategoryRepository.save(warehouseCategoryComputer);
        warehouseCategoryRepository.save(warehouseCategoryElectronic); warehouseCategoryRepository.save(warehouseCategoryFashionPria);
        warehouseCategoryRepository.save(warehouseCategoryFashionWanita); warehouseCategoryRepository.save(warehouseCategoryGaleriIndonesia);
        warehouseCategoryRepository.save(warehouseCategoryGroceriesCakung); warehouseCategoryRepository.save(warehouseCategoryGroceriesCeper);
        warehouseCategoryRepository.save(warehouseCategoryHandphone); warehouseCategoryRepository.save(warehouseCategoryHomeAndLivingCakung);
        warehouseCategoryRepository.save(warehouseCategoryHomeAndLivingCeper); warehouseCategoryRepository.save(warehouseCategoryIbuDanAnak);
        warehouseCategoryRepository.save(warehouseCategoryKesehatanDanKecantikan); warehouseCategoryRepository.save(warehouseCategoryMainanDanVideoGames);
        warehouseCategoryRepository.save(warehouseCategoryOtomotif); warehouseCategoryRepository.save(warehouseCategorySport);
        warehouseCategoryRepository.save(warehouseCategoryTvDanDigital);
    }

}

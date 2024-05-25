package com.back.web.furniture.PythonModel;

import lombok.Data;

@Data
public class GeneratedFurniture {
        private String furnitureType;
        private String name;
        private String company;

        public GeneratedFurniture(String furnitureType, String name, String company) {
            this.furnitureType = furnitureType;
            this.name = name;
            this.company = company;
        }
    }

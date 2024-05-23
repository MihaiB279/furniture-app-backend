package com.back.web.furniture.Utils;

import com.back.web.furniture.Domain.Furniture.Furniture;
import com.back.web.furniture.Domain.Furniture.FurnitureType;
import com.back.web.furniture.Domain.Furniture.Room;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ToPythonScript {
    private static String path = "src/main/resources/scripts/main.py";
    public static Room toGenerateScript(Room room, List<List<Furniture>> alreadyGenerated) throws IOException {
        ProcessBuilder pb = new ProcessBuilder("python", path);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        // Get output stream of the process
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

        List<Furniture> furniture = room.getFurniture();

        // Write number of furniture
        writer.write(String.valueOf(furniture.size()));
        writer.newLine();

        // Write furniture details
        for(Furniture furnitureItem: furniture) {
            writer.write(String.valueOf(furnitureItem.getFurnitureType()));
            writer.newLine();
            writer.write(furnitureItem.getDetails());
            writer.newLine();
        }

        writer.write(String.valueOf(room.getBudget()));
        writer.newLine();

        if(alreadyGenerated != null) {
            writer.write(String.valueOf(alreadyGenerated.size()));
            writer.newLine();
            writer.write(String.valueOf(alreadyGenerated.get(0).size()));
            writer.newLine();
            for (List<Furniture> list : alreadyGenerated) {
                for (Furniture item : list) {
                    writer.write(String.valueOf(item.getFurnitureType()));
                    writer.newLine();
                    writer.write(item.getName());
                    writer.newLine();
                    writer.write(item.getCompany());
                    writer.newLine();
                }
            }
        }
        else{
            writer.write(String.valueOf(0));
            writer.newLine();
            writer.write(String.valueOf(0));
            writer.newLine();
        }

        // Flush and close the writer
        writer.flush();
        writer.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        double sum = 0;
        List<Furniture> newFurniture = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            System.out.println("Python output: " + line);
            if(!line.contains("Name")){
                Room notGoodRoom = new Room();
                notGoodRoom.setBudget(-1);
                Furniture notGoodFurniture= new Furniture();
                notGoodFurniture.setName(line);
                List<Furniture> listNotGood = new ArrayList<>();
                listNotGood.add(notGoodFurniture);
                notGoodRoom.setFurniture(listNotGood);
                return notGoodRoom;
            }
            else {
                Furniture extractedFurniture = constructFurniture(line);
                newFurniture.add(extractedFurniture);
                sum += extractedFurniture.getPrice();
            }
        }
        Room doneRoom = new Room();
        doneRoom.setFurniture(newFurniture);
        doneRoom.setBudget(sum);
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return doneRoom;
    }

    private static Furniture constructFurniture(String furnitureString){
        Furniture furniture = new Furniture();
        int countCommas = 0;
        int index = -1;
        for (int i = furnitureString.length() - 1; i >= 0; i--) {
            if (furnitureString.charAt(i) == ',') {
                countCommas++;
                if (countCommas == 4) {
                    index = i;
                    break;
                }
            }
        }
        String details = furnitureString.substring(0, index);
        String restFurniture = furnitureString.substring(index + 1).trim();
        String[] parts = restFurniture.split(",");
        for(String part: parts) {
            String[] separatedElements = part.split(": ' ");
            if (separatedElements[0].contains("Price")) {
                furniture.setPrice(Float.parseFloat(separatedElements[1].substring(0, separatedElements[1].length() - 1)));
            }
            else if(separatedElements[0].contains("Company")){
                furniture.setCompany(separatedElements[1].substring(0, separatedElements[1].length() - 1));
            }
            else if(separatedElements[0].contains("Name")){
                furniture.setName(separatedElements[1].substring(0, separatedElements[1].length() - 1));
            }
            else if(separatedElements[0].contains("Furniture Type")){
                furniture.setFurnitureType(FurnitureType.valueOf(separatedElements[1].substring(0, separatedElements[1].length() - 2)));
            }
        }
        furniture.setDetails(details.substring(1));
        return furniture;
    }

}

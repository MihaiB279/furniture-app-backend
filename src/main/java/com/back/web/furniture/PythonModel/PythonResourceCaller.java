package com.back.web.furniture.PythonModel;

import com.back.web.furniture.Domain.Furniture.Furniture;
import com.back.web.furniture.Domain.Furniture.Room;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;

public class PythonResourceCaller {
    private static final String PYTHON_ENDPOINT = "https://furniture-app-model.azurewebsites.net/optimize/";

    public static Room toGenerateScript(Room room, List<List<Furniture>> alreadyGenerated, String jwt) throws IOException {
        Gson gson = new Gson();
        RoomRequest roomRequest = new RoomRequest();
        roomRequest.setBudget(room.getBudget());
        roomRequest.setFurniture(room.getFurniture());
        if (alreadyGenerated != null) {
            List<List<GeneratedFurniture>> generatedList = new ArrayList<>();
            for (List<Furniture> list : alreadyGenerated) {
                List<GeneratedFurniture> generatedSingleList = new ArrayList<>();
                for (Furniture item : list) {
                    generatedSingleList.add(new GeneratedFurniture(
                            item.getFurnitureType().toString(),
                            item.getName(),
                            item.getCompany()
                    ));
                }
                generatedList.add(generatedSingleList);
            }
            roomRequest.setAlreadyGenerated(generatedList);
        }

        String jsonInputString = gson.toJson(roomRequest);
        URL url = new URL(PYTHON_ENDPOINT);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + jwt);
        con.setDoOutput(true);

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            Furniture[] newFurniture = gson.fromJson(response.toString(), Furniture[].class);
            double sum = 0;
            for (Furniture furniture : newFurniture) {
                sum += furniture.getPrice();
            }

            Room doneRoom = new Room();
            doneRoom.setFurniture(List.of(newFurniture));
            doneRoom.setBudget(sum);
            return doneRoom;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error reading response", e);
        } finally {
            con.disconnect();
        }
    }
}

package aoc2025.util;

import java.io.IOException;
import java.io.FileWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadInputs {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length != 3) {
            System.out.println("Usage: java aoc2025.util.DownloadInputs <YEAR> <DAY> <SESSION_COOKIE>");
            System.exit(1);
        }
        int year = Integer.parseInt(args[0]);
        int day = Integer.parseInt(args[1]);
        String sessionCookie = args[2];
        String url = String.format("https://adventofcode.com/%d/day/%d/input", year, day);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Cookie", "session=" + sessionCookie.trim())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            Files.createDirectories(Paths.get("resources"));
            String filename = String.format("resources/day%02d.txt", day);
            try (FileWriter writer = new FileWriter(filename)) {
                writer.write(response.body().strip() + "\n");
            }
            System.out.printf("Downloaded input for Day %d to %s\n", day, filename);
            // Also create or update the writeup file with the puzzle title and link
            try {
                aoc2025.util.WriteupUtil.createWriteup(year, day);
            } catch (IOException e) {
                System.out.printf("Could not create writeup for Day %d: %s\n", day, e.getMessage());
            }
        } else {
            System.out.printf("Failed to download input: HTTP %d\n", response.statusCode());
            System.out.println(response.body());
        }
    }
}

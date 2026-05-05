package com.akin.speller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class Uygulama {
    public static void main(String[] args) {
        Trie trie = new Trie();
        String filePath = "words.txt";

        System.out.println("Loading dataset, please wait...");


        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {

            String line;
            int wordCount = 0;


            while ((line = br.readLine()) != null) {

                if (!line.trim().isEmpty()) {
                    trie.insert(line.trim());
                    wordCount++;
                }
            }
            System.out.println(wordCount + " words successfully loaded into memory!\n");

        } catch (Exception e) {
            System.out.println("ERROR: Could not read the file. Make sure 'words.txt' is in the correct location.");
            System.out.println("Error Details: " + e.getMessage());
            return;
        }


        Scanner scanner = new Scanner(System.in);
        System.out.println("======================================");
        System.out.println("🚀 SmartSpeller Ready!");
        System.out.println("Start searching for words (Type 'q' or 'exit' to quit)");
        System.out.println("======================================\n");

        while (true) {
            System.out.print("Enter prefix > ");
            String prefix = scanner.nextLine().trim();

            // Exit control
            if (prefix.equalsIgnoreCase("q") || prefix.equalsIgnoreCase("exit")) {
                System.out.println("Exiting program. See you later!");
                break;
            }


            if (prefix.isEmpty()) {
                continue;
            }


            long startTime = System.nanoTime();
            List<String> suggestions = trie.getSuggestions(prefix, 5);
            long endTime = System.nanoTime();


            if (suggestions.isEmpty()) {
                System.out.println("   [!] No matching words found.");
            } else {

                System.out.print("   🎯 Suggestions: ");
                int displayLimit = Math.min(suggestions.size(), 10);

                for (int i = 0; i < displayLimit; i++) {
                    System.out.print(suggestions.get(i) + (i == displayLimit - 1 ? "" : ", "));
                }

                if (suggestions.size() > 10) {
                    System.out.print(" ... (+" + (suggestions.size() - 10) + " more words)");
                }


                double msDuration = (endTime - startTime) / 1_000_000.0;
                System.out.printf("\n   ⏱️ Search speed: %.3f ms\n", msDuration);
            }
            System.out.println();
        }
        scanner.close();
    }
}
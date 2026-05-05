package com.akin.speller;

import java.util.List;

public class Main {
    public static void main(String arg[]) {
        Trie sözlük = new Trie();

        sözlük.insert("araba");
        sözlük.insert("armut");
        sözlük.insert("arkadaş");
        sözlük.insert("aslan");
        sözlük.insert("balık");

        System.out.println("Araba var mı?" + sözlük.search("araba"));
        System.out.println("Arı var mı ?" + sözlük.search("arı"));
    }
}
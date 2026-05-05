package com.akin.speller;
import java.util.ArrayList;
import java.util.List;

public class Trie {
    private final TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String word) {
        TrieNode current = root;
        for(char ch: word.toLowerCase().toCharArray()){
            current.children.putIfAbsent(ch,new TrieNode());
            current = current.children.get(ch);
        }
        current.isEndOfWord = true;
    }

    // UNUTTUĞUMUZ METOT BURASI :)
    public boolean search(String word) {
        TrieNode current = root;
        for(char ch : word.toLowerCase().toCharArray()){
            if(!current.children.containsKey(ch)) {
                return false;
            }
            current = current.children.get(ch);
        }
        return current.isEndOfWord;
    }

    // Limit parametresi eklenmiş öneri metodu
    public List<String> getSuggestions(String prefix, int limit) {
        List<String> suggestions = new ArrayList<>();
        TrieNode current = root;

        for(char ch : prefix.toLowerCase().toCharArray()) {
            current = current.children.get(ch);
            if(current == null) return suggestions;
        }
        collectAllWords(current, prefix.toLowerCase(), suggestions, limit);
        return suggestions;
    }

    private void collectAllWords(TrieNode node, String currentWord, List<String> results, int limit) {
        // FREN 1
        if (results.size() >= limit) return;

        if(node.isEndOfWord) {
            results.add(currentWord);
        }

        for(char ch : node.children.keySet()) {
            // FREN 2
            if (results.size() >= limit) return;
            collectAllWords(node.children.get(ch), currentWord + ch, results, limit);
        }
    }
}
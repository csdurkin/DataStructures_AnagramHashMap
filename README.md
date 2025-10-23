# DataStructures_Anagram

Computes the group(s) of English words with the most anagrams using a prime-factor hash. Each word maps to a Long via the product of letter primes; anagrams share the same key. Builds a hash table from `words_alpha.txt`, then reports the key, the list of words with the maximum anagram count, and timing.

---

## Overview
This project implements an `Anagrams` class that groups words by a multiplicative hash derived from letter-to-prime mapping (a→2, b→3, …, z→101). Because prime factorization is unique, any permutations of the same letters (anagrams) yield the same product. The program reads a dictionary file, builds the table, and prints the anagram group(s) with maximal size.

Coloring/GUI are not used here; this is a console program.

---

## File Layout
```
DataStructures_Anagrams/
  homework6/
    Anagrams.java
  words_alpha.txt
  README.md
```

Package declaration in code is `package homework6;`, so for now, I am keeping this path. 

---

## Build and Run
From the repository root:
```bash
# Compile
javac -d out homework6/Anagrams.java

# Run (expects words_alpha.txt in the current directory)
java -cp out homework6.Anagrams
```

If you place `words_alpha.txt` elsewhere, either run from that directory or change the filename passed to `processFile` in `main`.

---

## Core Ideas

### Letter-to-Prime Table
```text
a=2, b=3, c=5, d=7, e=11, f=13, g=17, h=19, i=23, j=29, k=31, l=37,
m=41, n=43, o=47, p=53, q=59, r=61, s=67, t=71, u=73, v=79, w=83,
x=89, y=97, z=101
```

### Hash Function
`myHashCode(String s)` multiplies the prime for each character to produce a `Long` key. All anagrams have the same product, hence the same key.

> Notes: Input is expected to be alphabetic (a–z). Non-letter characters throw `IllegalArgumentException`. The dictionary provided is lowercase; if you supply mixed case, normalize to lowercase before hashing.

---

## Implementation Notes

- `buildLetterTable()`: initializes `Map<Character,Integer>` for letter→prime.
- `addWord(String s)`: computes key and appends `s` into `anagramTable` (a `Map<Long, ArrayList<String>>`).
- `processFile(String s)`: reads one word per line and calls `addWord`.
- `getMaxEntries()`: scans `anagramTable` and returns all entries whose value list length is maximal.
- `main(String[] args)`: times the run, processes `words_alpha.txt`, and prints summary lines.

Sample console output (format may vary):
```
Time: 0.68
Key of max anagrams: 236204078
List of max anagrams: [alerts, alters, artels, estral, laster, lastre, rastle, ratels, relast, resalt, salter, slater, staler, stelar, talers]
Length of list of max anagrams: 15
```

---

## Complexity

Let N be the number of words and L the average word length.
- Building letter table: O(1).
- Hashing and inserting all words: O(N·L) time, O(N) space (sum of list sizes).
- `getMaxEntries()`: O(U) over unique keys (≤ N).

> Multiplying primes into a 64-bit `Long` can overflow for long words; anagrams still collide identically, but unrelated words could theoretically collide. This mirrors the assignment’s intended approach.

---

## Testing Tips

- Verify that known anagrams (e.g., alerts/alters/estral/ratels/relast/resalt/salter/slater/staler/stelar/talers) group together.
- Add lines with non-letters to confirm `IllegalArgumentException` is thrown as designed.
- Try small custom dictionaries to check tie handling in `getMaxEntries()`.
- Measure timing with and without JVM warm-up for more stable numbers.
